package edu.uit.dictplus.ActivityTabStudy;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import edu.uit.dictplus.FragmentStudy;
import edu.uit.dictplus.R;
import edu.uit.dictplus.SpeechEnglish;

/**
 * Created by nmtri_000 on 10/28/2015.
 */
public class FragmentTracNghiem  extends AppCompatActivity {

    TextView tvtnVoca;
    RadioButton rbtnA,rbtnB,rbtnC,rbtnD;
    Button btnNext,btnEnd;
    int SoCau=0;
    public FragmentTracNghiem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_tracnghiem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Trắc Nghiệm");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnNext=(Button)findViewById(R.id.btnStudyOk);
        btnEnd=(Button)findViewById(R.id.btnTNend);
        tvtnVoca=(TextView)findViewById(R.id.tvStudyVoca);
        rbtnA=(RadioButton)findViewById(R.id.rbCauA);
        rbtnB=(RadioButton)findViewById(R.id.rbCauB);
        rbtnC=(RadioButton)findViewById(R.id.rbCauC);
        rbtnD=(RadioButton)findViewById(R.id.rbCauD);


        rbtnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbtnA.setChecked(true);
                rbtnB.setChecked(false);
                rbtnC.setChecked(false);
                rbtnD.setChecked(false);
            }
        });
        rbtnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbtnA.setChecked(false);
                rbtnB.setChecked(true);
                rbtnC.setChecked(false);
                rbtnD.setChecked(false);
            }
        });
        rbtnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbtnA.setChecked(false);
                rbtnB.setChecked(false);
                rbtnC.setChecked(true);
                rbtnD.setChecked(false);
            }
        });
        rbtnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbtnA.setChecked(false);
                rbtnB.setChecked(false);
                rbtnC.setChecked(false);
                rbtnD.setChecked(true);
            }
        });
        final MediaPlayer mpDung = MediaPlayer.create(this, R.raw.dung);
        final MediaPlayer mpSai = MediaPlayer.create(this, R.raw.sai);
        tvtnVoca.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getVoca());
        rbtnA.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauA());
        rbtnB.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauB());
        rbtnC.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauC());
        rbtnD.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauD());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ((rbtnA.isChecked() && rbtnA.getText().toString().equals(FragmentStudy.mListDataTracNghiem.get(SoCau).getDapAn()))
                        || (rbtnB.isChecked() && rbtnB.getText().toString().equals(FragmentStudy.mListDataTracNghiem.get(SoCau).getDapAn())
                        || (rbtnC.isChecked() && rbtnC.getText().toString().equals(FragmentStudy.mListDataTracNghiem.get(SoCau).getDapAn()))
                        || (rbtnD.isChecked() && rbtnD.getText().toString().equals(FragmentStudy.mListDataTracNghiem.get(SoCau).getDapAn())))) {
                    if (((CheckBox) findViewById(R.id.cbtnAmthanh)).isChecked())
                        mpDung.start();

                    SoCau++;
                    if (SoCau >= FragmentStudy.mListDataTracNghiem.size())
                        SoCau = 0;
                    if (((CheckBox) findViewById(R.id.cbtnPhatam)).isChecked())
                        new SpeechEnglish(getApplicationContext(),FragmentStudy.mListDataTracNghiem.get(SoCau).getVoca().split("-")[0].trim().toLowerCase());//.execute(new String[]{MessagesFragment.mListDataTracNghiem.get(SoCau).getVoca().split("-")[0].trim().toLowerCase()});
                    tvtnVoca.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getVoca());
                    rbtnA.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauA());
                    rbtnB.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauB());
                    rbtnC.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauC());
                    rbtnD.setText(FragmentStudy.mListDataTracNghiem.get(SoCau).getCauD());
                } else {
                    if (((CheckBox) findViewById(R.id.cbtnAmthanh)).isChecked())
                        mpSai.start();
                }


            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }




}

