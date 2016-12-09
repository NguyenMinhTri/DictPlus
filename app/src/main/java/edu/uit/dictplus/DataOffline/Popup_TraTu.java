package edu.uit.dictplus.DataOffline;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import edu.uit.dictplus.R;
import edu.uit.dictplus.SpeechEnglish;

/**
 * Created by nmtri_000 on 10/19/2015.
 */
public class Popup_TraTu extends Activity {
    Button btnClose,btnNghe;
    WebView web;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width= dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * .6));

        web=(WebView)findViewById(R.id.webView);
        web.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = web.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        btnClose=(Button)findViewById(R.id.btnClose);
        btnNghe=(Button)findViewById(R.id.btnpopNghe);
        try{
            if(getIntent().getStringExtra("VietAnh").equals("VietAnh"))
                btnNghe.setEnabled(false);
        }catch (Exception e){}
        btnNghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=getIntent().getStringExtra("Value").split(" ☺/")[0].toLowerCase().trim();
                new SpeechEnglish(getApplicationContext(),text);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //<font color=\"#FF0000\"><b>
        String dataWeb=getIntent().getStringExtra("Value");
        dataWeb="<font color=\\\"#FF0000\\\"><b>"+dataWeb;
        if(dataWeb.indexOf("☺")==-1)
        {
            dataWeb=dataWeb.replaceFirst("\n","</b></font>\n");
        }
        else
        dataWeb=dataWeb.replace("☺", "</b></font>");
      //  dataWeb=dataWeb.replace("♥", "<font color=\\\"#FF0000\\\"><b>");
        dataWeb = dataWeb.replace("\n*", "<br/><b>*");
        dataWeb = dataWeb.replace("\n-", "</b><br/>&nbsp;-");
        dataWeb = dataWeb.replace("\n", "<br/>");
        web.loadData(dataWeb, "text/html; charset=utf-8",null);

    }
}
