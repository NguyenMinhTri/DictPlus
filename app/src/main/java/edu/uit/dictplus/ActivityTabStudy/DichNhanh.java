package edu.uit.dictplus.ActivityTabStudy;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TimerTask;

import edu.uit.dictplus.R;
import edu.uit.dictplus.TraTu.Tab_AnhViet;

/**
 * Created by nmtri_000 on 11/3/2015.
 */
public class DichNhanh  extends Service {
    public   String dataAllAnhViet;
    String realTimeText;
    String temp;
    TimerTask timerTask;

    ClipboardManager clipboard;
    TextView tvresult;
    private WindowManager windowManager;
    private RelativeLayout chatheadView;
    CountDownTimer timer;
    private Point
            szWindow = new Point();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void handleStart() {

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        chatheadView = (RelativeLayout) inflater.inflate(R.layout.service_dichnhanh, null);
        tvresult= ((TextView)chatheadView.findViewById(R.id.dichnhanhResult));
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


     //   chatheadView.setVisibility(View.GONE);
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        tvresult.setText(temp);
        try {
            temp = clipboard.getText().toString();
        }catch(Exception e)
        {
            temp=" ";
        }


            timer=        new CountDownTimer(10000000, 100) {

                        public void onTick(long millisUntilFinished) {


                            try {
                                realTimeText=clipboard.getText().toString();
                            }catch(Exception e)
                            {
                                realTimeText=" ";
                            }
                            if(!realTimeText.equals(temp)) {
                                try {
                                    String result = "";

                                    int index = dataAllAnhViet.indexOf("@" + realTimeText.trim().toLowerCase() + " ");
                                    if (index != -1) {

                                        result = dataAllAnhViet.substring(index);
                                        index = result.indexOf("- ");
                                        result = result.substring(index);
                                        index = result.indexOf("\n");
                                        result = result.substring(0, index);
                                        try {
                                            Log.e("Fail","Loi");
                                          //  FragmentA.createHistory(realTimeText, result);

                                        }catch (Exception e){}

                                        tvresult.setText(result);
                                        chatheadView.setVisibility(View.VISIBLE);
                                        //    Toast.makeText(DichNhanh.this, result, Toast.LENGTH_SHORT).show();
                                        temp = realTimeText;
                                    }
                                }catch (Exception e)
                                {
                                    Log.e("Fail","Loi");
                                }


                            }
                        }

                        public void onFinish() {

                        }
                    }.start();

        tvresult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Handler giancach = new Handler();
                giancach.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            chatheadView.setVisibility(View.GONE);
                        }catch (Exception e)
                        {

                        }

                    }
                },5000);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataAllAnhViet= Tab_AnhViet.dataFullAV.toString();
        handleStart();
        Toast.makeText(DichNhanh.this, "Đã bật tra nhanh", Toast.LENGTH_SHORT).show();
        startForeground();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(DichNhanh.this, "Đã tắt tra nhanh", Toast.LENGTH_SHORT).show();
        if(chatheadView != null){
            windowManager.removeView(chatheadView);
            chatheadView=null;
            timer.cancel();
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startForeground() {
     //   registerReceiver(stopServiceReceiver, new IntentFilter("myFilter"));
        RemoteViews notificationView = new RemoteViews(getPackageName(),
                R.layout.mynotification);
        PendingIntent contentIntent =
                PendingIntent.getBroadcast(this, 0, new Intent("myFilter"), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText("Running")
                .setSmallIcon(R.drawable.dictplus)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .build();


    //    notification.contentView=notificationView;
      //  notificationView.setOnClickPendingIntent(R.id.closeOnFlash, contentIntent);
        startForeground(9999, notification);


    }

}
