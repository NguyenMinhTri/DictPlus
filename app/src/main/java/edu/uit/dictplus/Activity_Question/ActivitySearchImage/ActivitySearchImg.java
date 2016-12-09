package edu.uit.dictplus.Activity_Question.ActivitySearchImage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;

import edu.uit.dictplus.R;


/**
 * Created by nmtri_000 on 12/28/2015.
 */
public class ActivitySearchImg extends AppCompatActivity {

    public Context mContext = this;
    public Object[] objects;
    public static String strUrl = "https://api.datamarket.azure.com/Bing/Search/Image?$format=json&$top=10";
    public char[] accountKey = "O84qNII9ftGEG3Lou+h6lt9Ff0oKw9VS5MxRKlfylJ4=".toCharArray();
    ProgressBar progressBar = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activty_search_img);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tìm Ảnh Trên Bing");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
        });



        View button1 = this.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActivitySearchImg.this.doSearch();
            }
        });


        View editText1 = this.findViewById(R.id.editText1);
        editText1.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER){
                    progressBar.setVisibility(View.VISIBLE);
                    ActivitySearchImg.this.doSearch();
                    return true;
                }
                return false;
            }
        });


        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", ActivitySearchImg.this.accountKey);
            }
        });
    }

    public void doSearch(){


        URL url;
        String q = null;


        EditText et = (EditText)this.findViewById(R.id.editText1);


        try{
            q = URLEncoder.encode(et.getText().toString(),"UTF-8");
            url = new URL(strUrl + "&Query='" + q + "'");
            new jsonTask().execute(url);
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
    }


    private class jsonTask extends AsyncTask<URL, Integer, String>{

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection connection = null;
            try{
                connection = (HttpURLConnection)params[0].openConnection();
                connection.setDoInput(true);
                connection.connect();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(),"UTF-8"));
                String jsonText = reader.readLine();
                reader.close();
                return(jsonText);
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result != null){
                try{
                    JSONObject jo = new JSONObject(result).getJSONObject("d");
                    JSONArray jsonArray = null;
                    jsonArray = jo.getJSONArray("results");

                    objects = new Object[jsonArray.length()];
                    URL[] thumbUrls = new URL[jsonArray.length()];

                    for(int i=0; i<jsonArray.length(); i++){
                        objects[i] = jsonArray.getJSONObject(i);
                        thumbUrls[i] = new URL(((JSONObject)objects[i]).getJSONObject("Thumbnail").getString("MediaUrl"));
                    }

                    new getThumbTask().execute(thumbUrls);

                }catch(JSONException el){
                    el.printStackTrace();
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
            }
        }
    }


    private class getThumbTask extends AsyncTask<URL, Integer, Bitmap[]>{
        @Override
        protected Bitmap[] doInBackground(URL... params) {
            HttpURLConnection connection = null;
            Bitmap[] bm = new Bitmap[params.length];

            try{
                for(int i=0; i<params.length; i++){
                    connection = (HttpURLConnection)params[i].openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream si = connection.getInputStream();
                    bm[i] = BitmapFactory.decodeStream(si);
                    si.close();
                }
                return bm;

            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap[] result){
            super.onPostExecute(result);

            final ThumbnailAdapter adapter = new ThumbnailAdapter(mContext, objects, result);

            //GridView�I�u�W�F�N�g�̎擾
            GridView lv = (GridView)findViewById(android.R.id.list);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    ImageView image = ((ImageView) view.findViewById(android.R.id.icon));
                    Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();


                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Bitmap", byteArray);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();


                }
            });
            progressBar.setVisibility(View.GONE);
        }
    }
}
