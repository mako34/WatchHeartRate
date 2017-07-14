package com.tkt.cora1;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MSG_REFRESH_RESULT = 2012;
    private static final int MSG_DETECT_ERROR = 2013;
    private static final int MSG_DETECT_START = 2014;
    private static final int DETECT_COUNT = 400;
    private static final int DETECT_TIME_INTERVAL = 5*1000;

    private float MEMS_DATA[] = {0.0F,0.0F,0.0F};
    private int mDetectResult = 0;
    private int mDetectCount = 0;
    private boolean isStopDetect = true;
    private int mDeviceFd = -1;
    private long mLastTimeMillis = 0;
    private long mCurrentTimeMillis = 0;


    Button button;
    TextView textView;

    private Thread mLooperThread;
    private Thread mDetectThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mk","tkt");

                if (isStopDetect) {
                    mDetectThread = new StartDetectThread();
                    mDetectThread.start();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopHeartRateDetection();
    }


    class StartDetectThread extends Thread{
        @Override
        public void run() {
            super.run();
            mDeviceFd = HeartRateDetection.OpenPixart();
            handler.sendEmptyMessage(MSG_DETECT_START);
        }
    }

    Handler handler = new Handler(){
        int mProgress = 0;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_RESULT:
                    if((mDetectResult > 0) && (mDetectCount >= DETECT_COUNT )){
//					mGifView.setVisibility(View.GONE);
                        Log.d("mk","i can see a mGifView gone");

//					mTvHeartRate.setVisibility(View.VISIBLE);
                        textView.setText(mDetectResult+"");



                    }
                    mProgress = mDetectCount * 100 / DETECT_COUNT;
//				mCircleProgressBar.setProgress(mProgress);
                    Log.d("mk","i can see a mGifView gone");


                    break;
                case MSG_DETECT_ERROR:
                    Toast.makeText(MainActivity.this, "error notify", Toast.LENGTH_SHORT).show();
                    stopHeartRateDetection();
                    break;
                case MSG_DETECT_START:
                    strtHeartRateDetection();
                    break;
                default:
                    break;
            }
        }
    };


    private void stopHeartRateDetection(){
        if (mDeviceFd > 0) {
            HeartRateDetection.ClosePixart(mDeviceFd);
            if(mLooperThread != null){
                mLooperThread.interrupt();
            }
        }

        if(mDetectThread != null){
            mDetectThread.interrupt();
        }

//		mRelDetectContainer.setVisibility(View.GONE);
        isStopDetect = true;
        mDetectCount = 0;
//		mCircleProgressBar.setProgress(0);

        textView.setText("stopped");

        Log.d("mk","i can see a zero");
    }

    private void strtHeartRateDetection(){
        if(mDeviceFd > 0){
            isStopDetect = false;

            mCurrentTimeMillis=mLastTimeMillis= SystemClock.uptimeMillis();
            mLooperThread = new LooperThread();
            mLooperThread.start();

//			mRelDetectContainer.setVisibility(View.VISIBLE);
//			mGifView.setVisibility(View.VISIBLE);
//			mTvHeartRate.setVisibility(View.GONE);
//			mGifView.setGifImage(R.drawable.gif_heart_curve);
//			mGifView.setLoopAnimation();

            textView.setText("started");

        }else {
            Toast.makeText(this, "no soportao", Toast.LENGTH_SHORT).show();
        }
    }

    class LooperThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (true) {
                if(mDeviceFd > 0){
                    char[] data = HeartRateDetection.GetPpgData(mDeviceFd);
                    if ((data != null) && (data.length == 13)) {
                        mDetectResult = PXIALGMOTION.Process(data, MEMS_DATA);
                        handler.sendEmptyMessage(MSG_REFRESH_RESULT);
                        mCurrentTimeMillis=mLastTimeMillis=SystemClock.uptimeMillis();
                    }else{
                        mCurrentTimeMillis=SystemClock.uptimeMillis();
                        if (mCurrentTimeMillis - mLastTimeMillis > DETECT_TIME_INTERVAL) {
                            handler.sendEmptyMessage(MSG_DETECT_ERROR);
                            break;
                        }
                    }
                }
                if(isStopDetect || (mDeviceFd < 0)){
                    break;
                }
            }
        }
    }

}
