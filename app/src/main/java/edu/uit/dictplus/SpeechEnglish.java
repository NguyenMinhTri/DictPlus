package edu.uit.dictplus;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by nmtri_000 on 10/28/2015.
 */
public class SpeechEnglish implements TextToSpeech.OnInitListener
{

    TextToSpeech t1;

    String voca;

    public SpeechEnglish(Context context, final String text){
        t1=new TextToSpeech(context,this);

        voca=text;
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {


                t1.setLanguage(Locale.US);
            if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                t1.speak(voca, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                t1.speak(voca, TextToSpeech.QUEUE_FLUSH, null);
            }

        }
    }
}
