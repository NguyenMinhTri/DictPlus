package edu.uit.dictplus.ParseHistory;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 11/2/2015.
 */
public class PopupEditHistory extends Activity {
    Button btnSave,btnCancel;
    EditText edtVoca,edtMean,edtDau;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popedithistory);
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width= dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * .4));

        btnSave=(Button)findViewById(R.id.hisbtnSave);
        btnCancel=(Button)findViewById(R.id.hisbtnCancel);
        edtVoca=(EditText)findViewById(R.id.hisedtVoca);
        edtMean=(EditText)findViewById(R.id.hisedtMean);
        edtDau=(EditText)findViewById(R.id.hisedtDau);
        edtVoca.setText(getIntent().getStringExtra("Voca"));
        edtMean.setText(getIntent().getStringExtra("Mean"));
        edtDau.setText(getIntent().getStringExtra("DauNhan"));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String changeDauNhan="♥";
                if(!edtDau.getText().toString().equals(""))
                    changeDauNhan=edtDau.getText().toString();
                FragmentHistory.mAdapter.getItem(getIntent().getIntExtra("Position", 0)).setDauNhan(changeDauNhan);
                FragmentHistory.mAdapter.getItem(getIntent().getIntExtra("Position", 0)).setVocabulary(edtVoca.getText().toString());
                FragmentHistory.mAdapter.getItem(getIntent().getIntExtra("Position", 0)).setMean(edtMean.getText().toString());
                FragmentHistory.mAdapter.getItem(getIntent().getIntExtra("Position", 0)).saveEventually();
                FragmentHistory.mAdapter.notifyDataSetChanged();
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }
}
