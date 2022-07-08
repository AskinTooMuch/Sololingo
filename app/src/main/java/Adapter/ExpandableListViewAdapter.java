package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.sololingo.R;

import java.util.ArrayList;
import java.util.Map;

import Bean.GroupObject;
import Bean.ItemObject;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private ArrayList<GroupObject> groupObject;
    private Map<GroupObject,ArrayList<ItemObject>> listItem;

    public ExpandableListViewAdapter(ArrayList<GroupObject> groupObject, Map<GroupObject, ArrayList<ItemObject>> listItem) {
        this.groupObject = groupObject;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        if(groupObject != null){
            return groupObject.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupObject != null && listItem != null){
            return listItem.get(groupObject.get(groupPosition)).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupObject.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(groupObject.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupObject.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return listItem.get(groupObject.get(groupPosition)).get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_test_group,parent,false);

        }
        TextView tvGroup = convertView.findViewById(R.id.tv_group);
        GroupObject object = groupObject.get(groupPosition);
        tvGroup.setText(object.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_test_item,parent,false);
        }
        TextView tvItem = convertView.findViewById(R.id.tv_item);
        ItemObject object = listItem.get(groupObject.get(groupPosition)).get(childPosition);
        tvItem.setText(object.getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
