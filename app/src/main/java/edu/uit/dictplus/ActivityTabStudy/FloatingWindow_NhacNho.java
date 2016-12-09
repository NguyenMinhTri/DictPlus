package edu.uit.dictplus.ActivityTabStudy;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import edu.uit.dictplus.FragmentStudy;
import edu.uit.dictplus.MainActivity;
import edu.uit.dictplus.ParseHistory.FragmentAllHistory;
import edu.uit.dictplus.ParseHistory.FragmentHistory;
import edu.uit.dictplus.R;



public class FloatingWindow_NhacNho extends Service implements TextToSpeech.OnInitListener,TextToSpeech.OnUtteranceCompletedListener {
    public static boolean IS_SERVICE_RUNNING = false;
    boolean isAnh,isViet,isLichSu=true;
    int mTime;
    MediaPlayer mediaPlayer;
    private WindowManager windowManager;
    private RelativeLayout chatheadView;
    int postion=0;
    long start;
    private TextView tvsvVoca,tvsvMean;
    private Point szWindow = new Point();
    TextToSpeech t1,ttsVN;
    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3",getBaseContext().getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();
            mediaPlayer = new MediaPlayer();
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }
    void SpeakVNOF(String text)
    {
        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
        ttsVN.setLanguage(Locale.getDefault());
        ttsVN.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
    }
    void SpeakENOF(String text)
    {
        try {
            if(!isLichSu)
            playMp3(FragmentAllHistory.mAdapter.getItem(postion).getMp3());
            else
                playMp3(FragmentHistory.mAdapter.getItem(postion).getMp3());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (chatheadView != null)
                        try {
                            if(isAnh && !isViet)
                            {
                                DelayEnglish();

                            }
                            else {
                                SpeakVNOF(tvsvMean.getText().toString().toLowerCase().trim());
                                //  new SpeechVN().execute(tvsvMean.getText().toString().toLowerCase().trim());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });

        }catch (Exception e)
        {
            HashMap<String, String> myHashAlarm = new HashMap<String, String>();
            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
            myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");

            t1.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
        }
    }
    @Override
    public void onUtteranceCompleted(String utteranceId) {



        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run() {
                nextSpeechVN();
            }
        });

    }


    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //noinspection ResourceType
                t1.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE).build());
            }

            t1.setOnUtteranceCompletedListener(this);
            t1.setLanguage(Locale.US);


        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    void autoNextVoca()
    {
        if(!isLichSu) {
            while (postion < FragmentAllHistory.mAdapter.getCount()) {
                if (FragmentAllHistory.mAdapter.getItem(postion).getCheck())
                    break;
                postion++;
                if (postion == FragmentAllHistory.mAdapter.getCount())
                    postion = 0;
            }
            if (postion == FragmentAllHistory.mAdapter.getCount())
                postion = 0;
            tvsvVoca.setText(FragmentAllHistory.mAdapter.getItem(postion).getVocabulay());
            tvsvMean.setText(FragmentAllHistory.mAdapter.getItem(postion).getMean());
        }else
        {
            while (postion < FragmentHistory.mAdapter.getCount()) {
                if (FragmentHistory.mAdapter.getItem(postion).getCheck())
                    break;
                postion++;
                if (postion == FragmentHistory.mAdapter.getCount())
                    postion = 0;
            }
            if (postion == FragmentHistory.mAdapter.getCount())
                postion = 0;
            tvsvVoca.setText(FragmentHistory.mAdapter.getItem(postion).getVocabulay());
            tvsvMean.setText(FragmentHistory.mAdapter.getItem(postion).getMean());

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void handleStart() {
        SpeechVNOfline();
        t1=new TextToSpeech(getApplicationContext(), this);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        chatheadView = (RelativeLayout) inflater.inflate(R.layout.floatwindow_nhacnho, null);
        tvsvVoca=(TextView)chatheadView.findViewById(R.id.tvsvVoca);
        tvsvMean=(TextView)chatheadView.findViewById(R.id.tvsvMean);
        isLichSu=MainActivity.HocTuLichSu;
        autoNextVoca();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        windowManager.addView(chatheadView, params);


        chatheadView.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = (WindowManager.LayoutParams) chatheadView.getLayoutParams();
            double x;
            double y;
            double pressedX;
            double pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));
                        windowManager.updateViewLayout(chatheadView, updatedParameters);

                    default:
                        break;
                }

                return false;
            }
        });

        if(isAnh) {
            Handler delayTime = new Handler();
            delayTime.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start = System.currentTimeMillis();
                    SpeakENOF(tvsvVoca.getText().toString().toLowerCase().trim());
                }
            }, 1000);
        }else
        {

            Handler delayTime = new Handler();
            delayTime.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NofitionMute();
                }
            }, 1000);
        }




    }
    void NofitionMute()
    {
        if (chatheadView != null) {
            final Handler handler = new Handler();
            final Integer time = mTime * 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatheadView.setVisibility(View.GONE);


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (chatheadView != null) {
                                chatheadView.setVisibility(View.VISIBLE);
                                postion++;
                                autoNextVoca();

                                updateNotification(tvsvVoca.getText().toString(), tvsvMean.getText().toString());
                                start = System.currentTimeMillis();

                                NofitionMute();

                            }
                        }
                    }, time);
                }
            },time);

        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopForeground(true);
        if(chatheadView != null){
            windowManager.removeView(chatheadView);
            chatheadView=null;
        }
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        super.onCreate();


        isAnh= FragmentStudy.isAnh;
        isViet=FragmentStudy.isViet;
        mTime=FragmentStudy.mTime;

        handleStart();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        super.onStart(intent, startId);
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {

            showNotification(tvsvVoca.getText().toString(),tvsvMean.getText().toString());

        } else if (intent.getAction().equals(Constants.ACTION.CLOSE_ACTION)) {

            stopForeground(true);
            stopSelf();

        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {

            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;




    }
    private void updateNotification(String Voca,String Mean) {
        Notification notification = getMyActivityNotification(Voca,Mean);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }
    private void showNotification(String Voca,String Mean) {


        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                getMyActivityNotification(Voca, Mean));

    }
    private Notification getMyActivityNotification(String Voca,String Mean){
        // The PendingIntent to launch our activity if the user selects
        // this notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, FloatingWindow_NhacNho.class);
        previousIntent.setAction(Constants.ACTION.CLOSE_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);



        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.dictplus);


        return new Notification.Builder(this)
                .setContentTitle(Voca)
                .setTicker(Voca)
                .setContentText(Mean)
                .setSmallIcon(R.drawable.dictplus)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_delete, "Close",
                        ppreviousIntent).getNotification();
    }



    void SpeechVNOfline()
    {
        ttsVN=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //noinspection ResourceType

                        ttsVN.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE).build());
                    }


                    ttsVN.setEngineByPackageName("com.vnspeak.ttsengine.vitts.hongvi");
                    ttsVN.setLanguage(Locale.US);
                    ttsVN.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                        @Override
                        public void onUtteranceCompleted(String utteranceId) {
                            Handler refresh = new Handler(Looper.getMainLooper());
                            refresh.post(new Runnable() {
                                public void run() {
                                    DelayAnhViet();
                                }
                            });
                        }
                    });
                }
            }
        });

    }
    void DelayAnhViet()
    {
        if (chatheadView != null) {

            chatheadView.setVisibility(View.GONE);
            Integer time = mTime * 1000;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (chatheadView != null) {
                        chatheadView.setVisibility(View.VISIBLE);
                        postion++;
                        autoNextVoca();

                        updateNotification(tvsvVoca.getText().toString(), tvsvMean.getText().toString());
                        start = System.currentTimeMillis();

                        SpeakENOF(tvsvVoca.getText().toString().toLowerCase().trim());

                    }
                }
            }, time);
        }
    }
    void DelayEnglish()
    {
        if (chatheadView != null) {

            chatheadView.setVisibility(View.GONE);
            Integer time = mTime * 1000;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (chatheadView != null) {
                        chatheadView.setVisibility(View.VISIBLE);
                        postion++;
                        autoNextVoca();


                        updateNotification(tvsvVoca.getText().toString(), tvsvMean.getText().toString());
                        start = System.currentTimeMillis();

                        SpeakENOF(tvsvVoca.getText().toString().toLowerCase().trim());

                    }
                }
            }, time);
        }
    }
    void nextSpeechVN()
    {
        if (chatheadView != null)
            try {
                if(isAnh && !isViet)
                {
                    DelayEnglish();

                }
                else {
                    SpeakVNOF(tvsvMean.getText().toString().toLowerCase().trim());
                  //  new SpeechVN().execute(tvsvMean.getText().toString().toLowerCase().trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


    }


}
