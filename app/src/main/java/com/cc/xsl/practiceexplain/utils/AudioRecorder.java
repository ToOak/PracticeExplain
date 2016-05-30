package com.cc.xsl.practiceexplain.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import java.io.File;

/**
 * Created by xushuailong on 2016/5/30.
 */
public class AudioRecorder {
    private static final String TAG = "oak_AudioRecorder";
    private static final int SAMPLE_RATE = 44100; //采样率(CD音质)
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO; //音频通道(单声道)
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT; //音频格式
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;  //音频源（麦克风）
    private static boolean is_recording = false;
    //    public static File recordFile ;
    private static AudioRecorder instance;
    private RecorderTask recorderTask = new RecorderTask();
    private AudioCallBack<BackResult> callBack;
    private Handler handler = new Handler();

    public enum BackResult {OK, ERROR}

    //    private AudioRecorder(File file){
//       recordFile = file;
//    }
//    public static AudioRecorder getInstance(File file) {
//           return new AudioRecorder(file);
//    }
    private AudioRecorder() {}

    public static AudioRecorder getInstance() {
        if (instance == null){
            instance = new AudioRecorder();
        }
        return instance;
    }

    /*
        开始录音
     */
    public void startAudioRecording(AudioCallBack<BackResult> callBack) {
        this.callBack = callBack;
        new Thread(recorderTask).start();
    }

    /*
        停止录音
     */
    public void stopAudioRecording() {
        is_recording = false;
    }

    class RecorderTask implements Runnable {
        int bufferReadResult = 0;
        public int samples_per_frame = 2048;

        @Override
        public void run() {
            //获取最小缓冲区大小
            int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
            AudioRecord audioRecord = new AudioRecord(
                    AUDIO_SOURCE,   //音频源
                    SAMPLE_RATE,    //采样率
                    CHANNEL_CONFIG,  //音频通道
                    AUDIO_FORMAT,    //音频格式
                    bufferSizeInBytes //缓冲区
            );
            audioRecord.startRecording();
            is_recording = true;

            while (is_recording) {
                byte[] buffer = new byte[samples_per_frame];
                //从缓冲区中读取数据，存入到buffer字节数组数组中
                bufferReadResult = audioRecord.read(buffer, 0, samples_per_frame);
                Log.e(TAG, "bufferReadResult：" + bufferReadResult);

//                判断是否读取成功
//                if (bufferReadResult == AudioRecord.ERROR_BAD_VALUE || bufferReadResult == AudioRecord.ERROR_INVALID_OPERATION) {
                if (bufferReadResult < 0){
                    Log.e(TAG, "没有权限");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onBack(BackResult.ERROR);
                        }
                    });
                    stopAudioRecording();
                    return;
                } else {
                    Log.e(TAG, "获得权限");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onBack(BackResult.OK);
                        }
                    });
                    stopAudioRecording();
                    return;
                }
            }
            if (audioRecord != null) {
                audioRecord.setRecordPositionUpdateListener(null);
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }


        }
    }

    public interface AudioCallBack<T> {
        void onBack(T result);
    }


}
