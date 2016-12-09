package edu.uit.dictplus.ParseHistory;

/**
 * Created by nmtri_000 on 10/26/2015.
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.uit.dictplus.R;


public class HistoryAdapter extends ArrayAdapter<History>  {
    private Context mContext;
    public  List<History> mTasks;
    public  byte[] dataMp3;
    TextToSpeech t1;
    View.OnTouchListener mTouchListener;
    private boolean isUser;
    private ImageButton btnNghe;

    private CheckBox cbCheck;
    private TextView tvVoca,tvMean;
    public HistoryAdapter(Context context, List<History> objects,View.OnTouchListener listener,boolean user) {
        super(context, R.layout.custom_listview_lichsu, objects);
        isUser=user;
        this.mContext = context;
        mTasks=objects;


        mTouchListener = listener;
    }

    static class ViewHolder {
        protected TextView textVoca,textMean;
        protected CheckBox checkbox;
        protected ImageButton btnNghe;
        protected MediaPlayer media;
    }
    @Override
    public long getItemId(int position) {
        return  position;
    }
    public View getView(final int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder = null;

        if(convertView == null){
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.custom_listview_lichsu, null);

            viewHolder = new ViewHolder();
            viewHolder.btnNghe=(ImageButton) convertView.findViewById(R.id.lvbtnNghe);
            viewHolder.textVoca = (TextView) convertView.findViewById(R.id.lvtvVoca);
            viewHolder.textMean = (TextView) convertView.findViewById(R.id.lvtvMean);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.lvcbCheck);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    mTasks.get(getPosition).setCheck(buttonView.isChecked());
                    if(isUser)
                    mTasks.get(getPosition).saveEventually();

                     // Set the value of checkbox to maintain its state.
                }
            });
            viewHolder.btnNghe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final int getPosition = (Integer) view.getTag();
                    try {
                        playMp3(mTasks.get(getPosition).getMp3());
                    } catch (Exception e) {
                        t1 = new TextToSpeech(mContext.getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status == TextToSpeech.SUCCESS) {

                                    t1.setLanguage(Locale.US);
                                    String voca=mTasks.get(getPosition).getVocabulay().split(" - ")[0];
                                    if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                        t1.speak(voca, TextToSpeech.QUEUE_FLUSH, null, null);
                                    } else {
                                        t1.speak(voca, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            }
                        });
                    }


                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.lvtvVoca, viewHolder.textVoca);
            convertView.setTag(R.id.lvtvMean, viewHolder.textMean);
            convertView.setTag(R.id.lvcbCheck, viewHolder.checkbox);
            convertView.setTag(R.id.lvbtnNghe, viewHolder.btnNghe);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.checkbox.setTag(position); // This line is important.
        viewHolder.btnNghe.setTag(position); // This line is important.

        viewHolder.textVoca.setText(mTasks.get(position).getVocabulay() + " - |" + mTasks.get(position).getDauNhan()+"|");
        viewHolder.textMean.setText(mTasks.get(position).getMean());
        viewHolder.checkbox.setChecked(mTasks.get(position).getCheck());
        convertView.setOnTouchListener(mTouchListener);
        return convertView;
    }
    @Override
    public void notifyDataSetChanged() {
        //do your sorting here

        super.notifyDataSetChanged();
    }
    private void playMp3(byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("kurchina", "mp3",mContext.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // Tried reusing instance of media player
            // but that resulted in system crashes...
            MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }



}





