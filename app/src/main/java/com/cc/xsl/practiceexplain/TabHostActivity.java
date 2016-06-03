package com.cc.xsl.practiceexplain;

import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.xsl.practiceexplain.utils.ConfigurationUtil;
import com.cc.xsl.practiceexplain.view.TouchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;

/**
 * Created by xushuailong on 2016/5/31.
 */
public class TabHostActivity extends TabActivity{
    private String TAG = "oak_TabHostActivity";
    private static final int COUNTDOWN = 0;
    private TabHost tabHost;
    private ExpandableListView expandableListView;
    private List<Map<String, String>> groups;
    private List<List<Map<String, Object>>> childs;
    private SimpleExpandableListAdapter adapter;
    private Button btn_popup;
    private PopupWindow popup;
    private ConfigurationUtil cfu;
    private Configuration cfg;
    private int timer = 60;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == COUNTDOWN){
                if (timer <= 0 ){
                    timer = 60;
                }else {
                    timer--;
                }
                ((TextView)findViewById(R.id.txt_timer)).setText(timer + "s");
            }
        }
    };

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
        ((LinearLayout)findViewById(R.id.touch_view)).addView(new TouchView(TabHostActivity.this));
        expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);
        initListview();
        btn_popup = (Button) findViewById(R.id.btn_popup);
        init2View();
    }

    private void init2View() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = COUNTDOWN;
                handler.sendMessage(msg);
            }
        },0,1000);
        if (cfu == null) {
            cfu = ConfigurationUtil.getInstance(TabHostActivity.this);
        }
        cfu.setTextViewInfo((ViewGroup) findViewById(R.id.parent_two));
        findViewById(R.id.btn_change_config).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfg = TabHostActivity.this.getResources().getConfiguration();
                if (cfg.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    TabHostActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (cfg.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    TabHostActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        query();
    }

    private void query() {
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = resolver.query(uri,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                Log.d(TAG,"******************");
                String contactId  = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                Log.d(TAG,"contactId: " + contactId + "\tdisplayname: " + displayName + "\tphoneCount: " + phoneCount);
                if (phoneCount > 0){
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +contactId,null,null);
                    if (phones.moveToFirst()){
                        do {
                            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Log.d(TAG,"number: " + number);
                        }while (phones.moveToNext());
                    }
                }
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged---newConfig: " + newConfig);
        if (cfu == null){
            cfu = ConfigurationUtil.getInstance(TabHostActivity.this);
        }
        cfu.setTextViewInfo((ViewGroup) findViewById(R.id.parent_two));
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
                showPopup(v);
            }
        });
    }

    private void showPopup(View popupView) {
//        LayoutInflater inflater = TabHostActivity.this.getLayoutInflater();
//        View contentView = inflater.inflate(R.layout.window_popup,null);
        View contentView = LayoutInflater.from(TabHostActivity.this).inflate(R.layout.window_popup,null);
        popup = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popup.setTouchable(true);
        popup.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "popup onTouch");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popup.setOutsideTouchable(true);
        contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TabHostActivity.this, "close popup", Toast.LENGTH_SHORT).show();
                popup.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 这里是API的一个bug
        popup.setBackgroundDrawable(getResources().getDrawable(R.color.bg_popup));
        popup.showAsDropDown(popupView, 160, 16);
    }

}
