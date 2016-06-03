package com.cc.xsl.practiceexplain;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cc.xsl.practiceexplain.utils.AudioRecorder;
import com.cc.xsl.practiceexplain.view.TouchView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private String TAG = "oak_MainActivity";
    private static final int SPEECH_RECOGNIZE_CODE = 0x1010;
    private Button btn_speechRecognize, btn_contact, btn_dial, btn_call, btn_isAccessRecord, btn_isNetConnect;
    private TextView txt_recognised;
    private Intent intent;
    private Context context;
    private AudioRecorder recorder;
    private ToggleButton btn_toggle_lantern;
    private LinearLayout parent;
    private RatingBar ratingBar;
    private Configuration cfg;
    private int xSpan = 0;
    private int ySpan = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_RECOGNIZE_CODE && resultCode == RESULT_OK) {
            try {
                List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txt_recognised.setText(result.get(0));
            } catch (NullPointerException e) {
                e.printStackTrace();
                txt_recognised.setText("返回结果 NullPointerException 异常");
                Toast.makeText(context, "返回结果 NullPointerException 异常", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
//        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.SET_WALLPAPER) == PackageManager.PERMISSION_GRANTED) {
//            try {
//                setWallpaper(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        setContentView(R.layout.activity_main);
        Log.d(TAG, "speecch_recognize_code:" + SPEECH_RECOGNIZE_CODE);
        context = MainActivity.this;
        setTitle("这里是title");//TODO 这里是title ? kitting me ??
        initViews();
        viewEvents();
//        this.addContentView(new TouchView(this),new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 隐藏标题和最上面的电池电量及信号栏（全屏）
     * 在setContentView之前
     */
    public void setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void viewEvents() {
        findViewById(R.id.btn_go2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WidgetActivity.class));
            }
        });
        findViewById(R.id.btn_go3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TabHostActivity.class));
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int numStars = ratingBar.getNumStars();
                Toast.makeText(MainActivity.this, numStars + " : " + rating, Toast.LENGTH_SHORT).show();
            }
        });
        btn_speechRecognize.setOnClickListener(this);
        btn_dial.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_contact.setOnClickListener(this);
        btn_isAccessRecord.setOnClickListener(this);
        btn_isNetConnect.setOnClickListener(this);
        btn_toggle_lantern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    parent.setBackgroundColor(getResources().getColor(R.color.lantern_on));
                } else {
                    parent.setBackgroundColor(getResources().getColor(R.color.lantern_off));
                }
            }
        });

        findViewById(R.id.txt_touch).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        xSpan = (int) event.getX();
                        ySpan = (int) event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:{
                        TextView newText = (TextView) findViewById(R.id.txt_touch);
                        int rawX = (int) event.getX();
                        int rawY = (int) event.getY();
                        ViewGroup.LayoutParams mLP = new AbsoluteLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                rawX - xSpan,
                                rawY - ySpan
                        );
                        newText.setLayoutParams(mLP);
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void initViews() {
        cfg = getResources().getConfiguration();
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        parent = (LinearLayout) findViewById(R.id.parent);
        btn_toggle_lantern = (ToggleButton) findViewById(R.id.btn_toggle_lantern);
        btn_isNetConnect = (Button) findViewById(R.id.btn_isNetConnect);
        btn_isAccessRecord = (Button) findViewById(R.id.btn_isAccessRecord);
        btn_speechRecognize = (Button) findViewById(R.id.btn_speechRecognize);
        txt_recognised = (TextView) findViewById(R.id.txt_recognised);
        btn_contact = (Button) findViewById(R.id.btn_contact);
        btn_call = (Button) findViewById(R.id.btn_call);
        btn_dial = (Button) findViewById(R.id.btn_dial);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_speechRecognize: {
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "你说的话将以文本形式显示在屏幕上");
                try {
                    startActivityForResult(intent, SPEECH_RECOGNIZE_CODE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    txt_recognised.setText("ActivityNotFoundException 异常");
                    Toast.makeText(context, "ActivityNotFoundException 异常", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_contact: {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/people/")));
                break;
            }
            case R.id.btn_dial: {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1008611")));
                break;
            }
            case R.id.btn_call: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(context,"manifest 中没有 android.permission.CALL_PHONE",Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:1008611")));
                break;
            }
            case R.id.btn_isAccessRecord:{
                recorder = AudioRecorder.getInstance();
                recorder.startAudioRecording(new AudioRecorder.AudioCallBack<AudioRecorder.BackResult>() {
                    @Override
                    public void onBack(AudioRecorder.BackResult result) {
                        if (result == AudioRecorder.BackResult.OK){
                            txt_recognised.setText("已获得录音权限");
                        }else if (result == AudioRecorder.BackResult.ERROR){
                            txt_recognised.setText("未获得录音权限");
                        }
                    }
                });
                break;
            }
            case R.id.btn_isNetConnect:{
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Service.CONNECTIVITY_SERVICE);
                HashMap<String, Object> map = new HashMap<String, Object>();
                if (cm != null) {
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        txt_recognised.setText("net could connect ！");
                    } else {
                        txt_recognised.setText("net couldn't connect ！");
                    }
                } else {
                    txt_recognised.setText( "can't get the status of the network");
                }
                break;
            }
        }
    }

    /**
     * 关于回调方法   接口KeyEvent.Callback中抽象方法
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"onkeyDown---keycode: "+keyCode+"\tkeyevent: "+event);
        // 按下home键，log没有打印
        // keycode: 82	keyevent: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_MENU, scanCode=139, metaState=0, flags=0x8, repeatCount=0, eventTime=7126985, downTime=7126985, deviceId=3, source=0x101 }
        // keycode: 4	keyevent: KeyEvent { action=ACTION_DOWN, keyCode=KEYCODE_BACK, scanCode=158, metaState=0, flags=0x8, repeatCount=0, eventTime=7102213, downTime=7102213, deviceId=3, source=0x101 }
        return super.onKeyDown(keyCode, event);
        // 返回true与false的区别
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG,"onkeyUp---keycode: "+keyCode+"\tkeyevent: "+event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyLongPress---keycode: "+keyCode+"\tkeyevent: "+event);
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        Log.d(TAG,"onKeyMultiple---keycode: "+keyCode+"\tkeyevent: "+event);
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        Log.d(TAG,"onKeyShortcut---keycode: "+keyCode+"\tkeyevent: "+event);
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        Log.d(TAG,"onTrackballEvent---keyevent: "+event);
        return super.onTrackballEvent(event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG,"onConfigurationChanged---newConfig: "+newConfig);
        super.onConfigurationChanged(newConfig);
    }
}
