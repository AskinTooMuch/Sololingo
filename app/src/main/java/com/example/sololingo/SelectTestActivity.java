package com.example.sololingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Adapter.ExpandableListViewAdapter;
import Bean.GroupObject;
import Bean.ItemObject;
import Bean.User;
import DAO.UserDAO;

public class SelectTestActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView tvNavHeaderName, tvNavHeaderEmail;
    private int nightModeFlags;
    private SharedPreferences pref;
    private ExpandableListView expandableListView;
    private ArrayList<GroupObject> groupObject;
    private Map<GroupObject,ArrayList<ItemObject>> listItem;
    private ExpandableListViewAdapter expandableListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_test);
        bindingView();
        bindingAction();
    }

    private void bindingAction() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        configDrawerHeaderContent();
        expandableListViewAdapter = new ExpandableListViewAdapter(groupObject,listItem);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getApplicationContext(), MockingTestActivity.class);
                Boolean check = true;
                switch (groupPosition){
                    case 0:
                        switch (childPosition){
                            case 0:intent.putExtra("topic_code", "N1_0");break;
                            case 1:intent.putExtra("topic_code", "N1_1");break;
                            case 2:
                                check = false;
                                Toast.makeText(getApplicationContext(),R.string.Function_upcoming,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition){
                            case 0:intent.putExtra("topic_code", "N2_0");break;
                            case 1:intent.putExtra("topic_code", "N2_1");break;
                            case 2:
                                check = false;
                                Toast.makeText(getApplicationContext(),R.string.Function_upcoming,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition){
                            case 0:intent.putExtra("topic_code", "N3_0");break;
                            case 1:intent.putExtra("topic_code", "N3_1");break;
                            case 2:
                                check = false;
                                Toast.makeText(getApplicationContext(),R.string.Function_upcoming,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition){
                            case 0:intent.putExtra("topic_code", "N4_0");break;
                            case 1:intent.putExtra("topic_code", "N4_1");break;
                            case 2:
                                check = false;
                                Toast.makeText(getApplicationContext(),R.string.Function_upcoming,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case 4:
                        switch (childPosition){
                            case 0:intent.putExtra("topic_code", "N5_0");break;
                            case 1:intent.putExtra("topic_code", "N5_1");break;
                            case 2:
                                check = false;
                                Toast.makeText(getApplicationContext(),R.string.Function_upcoming,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                }
                if (check){
                    intent.putExtra("status", MockingTestActivity.TAKE_TEST_STATUS);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.Function_upcoming,Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void bindingView() {
        drawerLayout = findViewById(R.id.drawerLayout2);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigationView2);
        pref = getApplicationContext().getSharedPreferences("Appearance", MODE_PRIVATE);
        nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        expandableListView = findViewById(R.id.expandableListView);
        getExpandableViewContent();
    }

    private void getExpandableViewContent() {
        groupObject = new ArrayList<>();
        groupObject.add(new GroupObject(0,"N1"));
        groupObject.add(new GroupObject(1,"N2"));
        groupObject.add(new GroupObject(2,"N3"));
        groupObject.add(new GroupObject(3,"N4"));
        groupObject.add(new GroupObject(4,"N5"));
        listItem = new HashMap<>();
        for (GroupObject x : groupObject){
            ArrayList<ItemObject> listItemObjects = new ArrayList<>();
            listItemObjects.add(new ItemObject(0,"Vocabulary"));
            listItemObjects.add(new ItemObject(1,"Reading"));
            listItemObjects.add(new ItemObject(2,"Listening"));
            listItem.put(x,listItemObjects);
        }
    }

    private void configDrawerHeaderContent() {
        View view = navigationView.getHeaderView(0);
        tvNavHeaderName = view.findViewById(R.id.tvNavHeaderName);
        tvNavHeaderEmail = view.findViewById(R.id.tvNavHeaderEmail);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserDAO userDAO = new UserDAO();
        userDAO.getUserByEmail(firebaseUser.getEmail(), new UserDAO.FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<User> userList) {

            }

            @Override
            public void onCallBack(User user) {
                tvNavHeaderName.setText(user.getName());
                tvNavHeaderEmail.setText(user.getEmail());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_Home:
                Intent intent3 = new Intent(this, MainActivity.class);
                startActivity(intent3);
                break;
            case R.id.menu_userProfile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
                break;
            case R.id.menu_testMocking:
                drawerLayout.closeDrawer(GravityCompat.START);
                /*
                Intent intent2 = new Intent(this, MockingTestActivity.class);
                intent2.putExtra("topic_code", "N5_moji");
                intent2.putExtra("status", MockingTestActivity.TAKE_TEST_STATUS);
                startActivity(intent2);

                 */
                Intent intent2 = new Intent(this,SelectTestActivity.class);
                startActivity(intent2);
                break;
            case R.id.theme:
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Appearance", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        // Night mode is not active, we're using the light theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putInt("theme", AppCompatDelegate.MODE_NIGHT_YES);
                        editor.apply();
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        // Night mode is active, we're using dark theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.putInt("theme", AppCompatDelegate.MODE_NIGHT_NO);
                        editor.apply();
                        break;
                }
                break;
            case R.id.language:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        pref = newBase.getSharedPreferences("Appearance", MODE_PRIVATE);
        String language = pref.getString("curLanguage","en");
        Log.d("12345", "attachBaseContext: "+language);
        Locale localeToSwitchTo = new Locale(language);
        ContextWrapper localeUpdatedContext = ContextUtils.updateLocale(newBase, localeToSwitchTo);
        super.attachBaseContext(localeUpdatedContext);
    }
}