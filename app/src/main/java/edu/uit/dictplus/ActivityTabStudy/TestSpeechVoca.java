package edu.uit.dictplus.ActivityTabStudy;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

import edu.uit.dictplus.FragmentStudy;
import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 10/28/2015.
 */
public class TestSpeechVoca extends AppCompatActivity {
    private Button mbtSpeak;
    private TextView tvspVoca,tvspMean;
    private int SoCau=0;
    private  static  final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_speech);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kiểm Tra Phát Âm");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mbtSpeak = (Button) findViewById(R.id.btSpeak);
        tvspVoca=(TextView)findViewById(R.id.tvspVoca);
        tvspMean=(TextView)findViewById(R.id.tvspMean);
        tvspVoca.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getVoca());


        tvspMean.setText(FragmentStudy.mListDataTracNghiem.get(SoCau++).getDapAn());
    }



    public void speak(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,tvspVoca.getText().toString());

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        int noOfMatches =10;
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,noOfMatches);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == RESULT_OK){
            ArrayList<String> textMatchlist = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (!textMatchlist.isEmpty()) {
                {
                    if(textMatchlist.indexOf(tvspVoca.getText().toString().split("-")[0].trim())!=-1)
                    {
                        tvspMean.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getDapAn());
                        tvspVoca.setText(FragmentStudy.mListDataTracNghiem.get(SoCau++).getVoca());
                        speak(null);
                        if(SoCau>=FragmentStudy.mListDataTracNghiem.size())
                            SoCau=0;
                    }
                    else if(textMatchlist.indexOf("next")!=-1){
                        tvspMean.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getDapAn());
                        tvspVoca.setText(FragmentStudy.mListDataTracNghiem.get(SoCau++).getVoca());
                        speak(null);
                        if(SoCau>=FragmentStudy.mListDataTracNghiem.size())
                            SoCau=0;
                    }
                    else
                        new Speech().execute(new String[]{tvspVoca.getText().toString().split("-")[0].trim()});
                }
            }

        }
        else if (resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
            showToastMessage("Audio Error");

        }
        else if ((resultCode == RecognizerIntent.RESULT_CLIENT_ERROR)){
            showToastMessage("Client Error");

        }
        else if (resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
            showToastMessage("Network Error");
        }
        else if (resultCode == RecognizerIntent.RESULT_NO_MATCH){
            showToastMessage("No Match");
        }
        else if (resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
            showToastMessage("Server Error");
        }
        super.onActivityResult(requestCode, resultCode, data);




    }
    void  showToastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    class Speech extends AsyncTask<String, Void, String>

    {
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            HttpClient httpclient = new DefaultHttpClient();


            try {

                URL demo = new URL("http://www.oxfordlearnersdictionaries.com/definition/english/" + arg0[0].toLowerCase());
                HttpResponse response = httpclient.execute(new HttpGet(String.valueOf(demo)));
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                responseString = responseString.substring(responseString.indexOf(".mp3") + 5);
                responseString = responseString.substring(responseString.indexOf("mp3=") + 5);
                responseString = responseString.substring(0, responseString.indexOf(".mp3") + 4);

                //,responseString.indexOf(".mp3")+4-(responseString.indexOf("mp3=")+5));
                try {
                    MediaPlayer mediaPlayer = getMediaPlayer(null);
                    mediaPlayer.setDataSource(responseString);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare(); //don't use prepareAsync for mp3 playback

                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                           speak(null);
                        }
                    });


                } catch (IOException e) {

                    SpeechBing(arg0[0]);
                }
                return responseString;


            } catch (Exception e) {

                SpeechBing(arg0[0]);
            }
            return null;
        }
    }
        static MediaPlayer getMediaPlayer(Context context) {

            MediaPlayer mediaplayer = new MediaPlayer();

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
                return mediaplayer;
            }

            try {
                Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
                Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
                Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
                Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");

                Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

                Object subtitleInstance = constructor.newInstance(context, null, null);

                Field f = cSubtitleController.getDeclaredField("mHandler");

                f.setAccessible(true);
                try {
                    f.set(subtitleInstance, new Handler());
                } catch (IllegalAccessException e) {
                    return mediaplayer;
                } finally {
                    f.setAccessible(false);
                }

                Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

                setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
                //Log.e("", "subtitle is setted :p");
            } catch (Exception e) {
            }

            return mediaplayer;
        }
    void SpeechBing(String text)
    {
        try {
            MediaPlayer mediaPlayer = getMediaPlayer(null);
            mediaPlayer.setDataSource("http://api.microsofttranslator.com/V2/Http.svc/Speak?language=en&format=audio/mp3&options=MaxQuality&appid=6CE9C85A41571C050C379F60DA173D286384E0F2&text=" + text.replace(" ", "%20"));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    speak(null);
                }
            });
        }
        catch (Exception e)
        {

        }

    }


}
