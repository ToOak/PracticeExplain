package com.cc.xsl.practiceexplain;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.xsl.practiceexplain.utils.AudioRecorder;

import java.io.File;
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
        setContentView(R.layout.activity_main);
        Log.d(TAG, "speecch_recognize_code:" + SPEECH_RECOGNIZE_CODE);
        context = MainActivity.this;
        setTitle("这里是title");//TODO 这里是title ? kitting me ??
        initViews();
        viewEvents();
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
        btn_speechRecognize.setOnClickListener(this);
        btn_dial.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_contact.setOnClickListener(this);
        btn_isAccessRecord.setOnClickListener(this);
        btn_isNetConnect.setOnClickListener(this);
    }

    private void initViews() {
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
}
