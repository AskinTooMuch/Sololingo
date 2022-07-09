package com.example.sololingo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import Adapter.TabAdapter;
import Bean.User;
import DAO.UserDAO;
import Service.NotificationService;
import Service.ReminderBroadcast;
import Service.TimePickerDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int REQUEST_PERMISSION_CODE = 1;
    private ViewPager pager;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView tvNavHeaderName, tvNavHeaderEmail;
    int nightModeFlags;
    SharedPreferences pref;

    /**
     * Binding Views
     */
    protected void bindingView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigationView);
    }

    /**
     * Binding Actions for views
     */
    protected void bindingAction() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        configDrawerHeaderContent();
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


    /**
     * Set main tab layout
     */
    private void setTabLayout() {
        pager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        FragmentManager manager = getSupportFragmentManager();
        TabAdapter adapter = new TabAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }

    /**
     * Overriding OnCreate method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sololingo);
        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        bindingAction();
        setTabLayout();
        setLanguage();
        pref = getApplicationContext().getSharedPreferences("Appearance", MODE_PRIVATE);
        nightModeFlags =
                this.getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        setNotification();
    }

    private void setNotification() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("notification", MODE_PRIVATE);
        boolean notiMode = pref.getBoolean("notiMode", true);
        if (notiMode) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, pref.getInt("hour", 19));
            calendar.set(Calendar.MINUTE, pref.getInt("minute", 0));
            calendar.set(Calendar.SECOND, 0);
            Log.d("12345", "setNotification: "+pref.getInt("hour", 19)+":"+pref.getInt("minute", 0));
            NotificationService n = new NotificationService();
            String[] content = n.getNotificationContent();
            Intent intent1 = new Intent(MainActivity.this, ReminderBroadcast.class);
            intent1.putExtra("title", content[0]);
            intent1.putExtra("data", content[1]);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                    0, intent1,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        /*SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("notiMode", true);
        editor.apply();*/
        } else {
            AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
            Intent myIntent = new Intent(MainActivity.this, ReminderBroadcast.class);
            myIntent.putExtra("title", "no");
            myIntent.putExtra("data", "no");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                    0, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);
            Log.d("12345", "setNotification: cancelled");
        }
    }

    //region set Languages
    private void setLanguage() {
        //Language
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, R.layout.spinner_selector_item);
        Spinner languageSelector = (Spinner) navigationView.getMenu().findItem(R.id.language).getActionView();
        languageSelector.setAdapter(adapter);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Appearance", MODE_PRIVATE);
        switch (pref.getString("curLanguage", "en")) {
            case "vi":
                languageSelector.setSelection(0, false);
                break;
            case "en":
                languageSelector.setSelection(1, false);
                break;
            case "ja":
                languageSelector.setSelection(2, false);
                break;
        }
        languageSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Appearance", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                switch (position) {
                    case 0:
                        editor.putString("curLanguage", "vi");
                        editor.apply();
                        Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i1);
                        //Toast.makeText(MainActivity.this, R.string.restart_to_change, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        editor.putString("curLanguage", "en");
                        editor.apply();
                        Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i2);
                        //Toast.makeText(MainActivity.this, R.string.restart_to_change, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        editor.putString("curLanguage", "ja");
                        editor.apply();
                        Intent i3 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i3);
                        //Toast.makeText(MainActivity.this, R.string.restart_to_change, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    //endregion set Languages

    private void setTheme() {
        //Theme
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Appearance", MODE_PRIVATE);
        int theme = pref.getInt("theme", AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(theme);
    }

    /**
     * On select drawer navigation Items
     *
     * @param item
     * @return
     */
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
                /*Intent intent2 = new Intent(this, MockingTestActivity.class);
                intent2.putExtra("topic_code","N5_moji");
                intent2.putExtra("status",MockingTestActivity.TAKE_TEST_STATUS);
                startActivity(intent2);*/
                Intent intent2 = new Intent(this, SelectTestActivity.class);
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
            case R.id.notificationTime:
                TimePickerDialog.TimePickListener listener = new TimePickerDialog.TimePickListener() {
                    @Override
                    public void onTimePicked(boolean timePicked) {
                        setNotification();
                        if (timePicked) {
                            Toast.makeText(MainActivity.this, R.string.notification_set_confirm, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.notification_cancel_confirm, Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                final TimePickerDialog dialog = new TimePickerDialog(this,listener);
                dialog.show();
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
        String language = pref.getString("curLanguage", "en");
        Locale localeToSwitchTo = new Locale(language);
        ContextWrapper localeUpdatedContext = ContextUtils.updateLocale(newBase, localeToSwitchTo);
        super.attachBaseContext(localeUpdatedContext);
    }
}