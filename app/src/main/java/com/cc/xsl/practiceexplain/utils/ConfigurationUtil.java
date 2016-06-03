package com.cc.xsl.practiceexplain.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by xushuailong on 2016/6/3.
 */
public class ConfigurationUtil {
    private static Context context;
    private String mccInfo;
    private String mncInfo;
    private static Configuration cfg;
    private String navigationInfo;
    private String touchScreenInfo;
    private String orientationInfo;
    private static ConfigurationUtil instance;
    private ConfigurationUtil(Context context){
        this.context = context;
    }
    public static ConfigurationUtil getInstance(Context context){
        cfg = context.getResources().getConfiguration();
        if (instance == null){
            instance = new ConfigurationUtil(context);
        }
        return instance;
    }

    public String getMccInfo() {
        mccInfo = cfg.mcc + "";
        return mccInfo;
    }

    public String getMncInfo() {
        mncInfo = cfg.mnc + "";
        return mncInfo;
    }

    public String getNavigationInfo() {
        navigationInfo = cfg.navigation == Configuration.NAVIGATION_NONAV ? "没有导航" :
                cfg.navigation == Configuration.NAVIGATION_DPAD ? "DPAD导航" :
                        cfg.navigation == Configuration.NAVIGATION_TRACKBALL ? "轨迹球导航" : "滚轮导航";
        return navigationInfo;
    }

    public String getOrientationInfo() {
        orientationInfo = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横向屏幕" : "竖向屏幕";
        return orientationInfo;
    }

    public String getTouchScreenInfo() {
        touchScreenInfo = cfg.touchscreen == Configuration.TOUCHSCREEN_NOTOUCH ? "无触摸屏" :
                cfg.touchscreen == Configuration.TOUCHSCREEN_STYLUS ? "触摸笔" :
                        cfg.touchscreen == Configuration.TOUCHSCREEN_FINGER ? "手指触摸" : "这啥？";
        return touchScreenInfo;
    }

    public void setTextViewInfo(ViewGroup view){
        TextView mccInfo = (TextView) view.getChildAt(0);
        mccInfo.setText("mcc:"+getMccInfo());//移动信号国家码
        TextView mncInfo = (TextView) view.getChildAt(1);
        mncInfo.setText("mnc:"+getMncInfo());//移动信号网络码
        TextView navigationInfo = (TextView) view.getChildAt(2);
        navigationInfo.setText(getNavigationInfo());
        TextView touchScreenInfo = (TextView) view.getChildAt(3);
        touchScreenInfo.setText(getTouchScreenInfo());
        TextView orientationInfo = (TextView) view.getChildAt(4);
        orientationInfo.setText(getOrientationInfo());
    }
}
