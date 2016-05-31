package com.cc.xsl.practiceexplain;

import android.app.TabActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xushuailong on 2016/5/31.
 */
public class TabHostActivity extends TabActivity{
    private TabHost tabHost;
    private ExpandableListView expandableListView;
    private List<Map<String, String>> groups;
    private List<List<Map<String, Object>>> childs;
    private SimpleExpandableListAdapter adapter;
    private Button btn_popup, btn_open, btn_save, btn_close;
    private PopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentViews();
        initViews();
        viewEvents();
    }

    private void initContentViews() {
        tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.activity_tabhost, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("①").setContent(R.id.parent_one));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("②").setContent(R.id.parent_two));
        tabHost.addTab(tabHost.newTabSpec("three").setIndicator("③").setContent(R.id.parent_three));
    }
    private void initViews(){
        expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);
        initListview();
        btn_popup = (Button) findViewById(R.id.btn_popup);
    }

    private void initListview() {
        groups = new ArrayList<>();
        Map<String, String> groupBoy = new HashMap<>();
        groupBoy.put("group","boy");
        groups.add(groupBoy);
        Map<String, String> groupGirl = new HashMap<>();
        groupGirl.put("group", "girl");
        groups.add(groupGirl);
        List<Map<String, Object>> childBoy = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        child.put("child", "Boa Yu");
        childBoy.add(child);
        child = new HashMap<>();
        child.put("child", "Wu Song");
        childBoy.add(child);
        child = new HashMap<>();
        child.put("child", "Wu Kong");
        childBoy.add(child);
        child = new HashMap<>();
        child.put("child", "Kong Ming");
        childBoy.add(child);
        List<Map<String, Object>> childGirl = new ArrayList<>();
        child = new HashMap<>();
        child.put("child", "Pin Pin");
        childGirl.add(child);
        child = new HashMap<>();
        child.put("child", "Shi Shi");
        childGirl.add(child);
        child = new HashMap<>();
        child.put("child", "Zhen Zhen");
        childGirl.add(child);
        child = new HashMap<>();
        child.put("child", "Chan Chan");
        childGirl.add(child);
        childs = new ArrayList<>();
        childs.add(childBoy);
        childs.add(childGirl);
        adapter = new SimpleExpandableListAdapter(
                this,
                groups,R.layout.expand_group,new String[]{"group"},
                new int[]{R.id.group},
                childs,R.layout.expand_child,new String[]{"child"},
                new int[]{R.id.child}
        );
    }

    private void viewEvents(){
        expandableListView.setAdapter(adapter);
        btn_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    private void showPopup() {
        LayoutInflater inflater = TabHostActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.window_popup,null);
        popup = new PopupWindow(view,300,500,true);
        popup.showAsDropDown(btn_popup,10,10);
        popup.setOutsideTouchable(true);
        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TabHostActivity.this,"close popup",Toast.LENGTH_SHORT).show();
                popup.dismiss();
            }
        });
    }
}
