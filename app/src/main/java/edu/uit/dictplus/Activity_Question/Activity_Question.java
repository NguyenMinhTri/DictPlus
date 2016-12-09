package edu.uit.dictplus.Activity_Question;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import edu.uit.dictplus.Activity_Question.ActivitySearchImage.ActivitySearchImg;
import edu.uit.dictplus.Activity_Question.Activity_ListQuestion.Activity_ListQuestion;
import edu.uit.dictplus.Activity_Question.Activity_ListQuestion.ParseQuestion;
import edu.uit.dictplus.R;

public class Activity_Question extends AppCompatActivity {
    Button btnAnhThe,btnAnhOnline,btnNew,btnGo;
    ImageView imgImageView;
    EditText etCauHoi;
    ProgressDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques);

        //khoi tao
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hỏi Đáp");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
        });
        btnAnhOnline=(Button)findViewById(R.id._frbtnTimIn);
        btnAnhThe=(Button)findViewById(R.id._frbtnTimThe);
        btnNew=(Button)findViewById(R.id._frbtnNew);
        imgImageView=(ImageView)findViewById(R.id._frImage);
        etCauHoi=(EditText)findViewById(R.id._frEdCauHoi);
        btnGo=(Button)findViewById(R.id.btnGoQues);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_Question.this, Activity_ListQuestion.class);
                startActivity(intent);
            }
        });
        //
        btnAnhOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_search=new Intent(Activity_Question.this, ActivitySearchImg.class);
                startActivityForResult (activity_search,0);
            }
        });
        btnAnhThe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/png");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Activity_Question.this, "Thông Báo", "Vui Long Đợi");
                final ParseQuestion parseQuestion = new ParseQuestion();

                try {


                    try {
                        Bitmap bitmap = ((BitmapDrawable)imgImageView.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        final ParseFile image=new ParseFile("Image",byteArray);
                        image.save();
                        parseQuestion.setFileImage(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    parseQuestion.setQues(etCauHoi.getText().toString());

                    parseQuestion.setUser(ParseUser.getCurrentUser());
                    parseQuestion.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParsePush.subscribeInBackground(parseQuestion.getObjectId());
                            ParsePush push = new ParsePush();
                            push.setChannel("Question");
                            push.setMessage("["+ParseUser.getCurrentUser().getUsername() + "] Đã Đặt Câu Hỏi : [ "+etCauHoi.getText().toString()+" ]");
                            try {
                                push.send();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            dialog.dismiss();
                            Intent intent=new Intent(Activity_Question.this, Activity_ListQuestion.class);
                            startActivity(intent);
                        }
                    });

                    //upload server
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    //nhan url imgge tu activity tim anh
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==0) {
                if (resultCode == Activity.RESULT_OK) {

                    byte[] byteArray = data.getByteArrayExtra("Bitmap");
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    imgImageView.setImageBitmap(bmp);
                }
            }else if(requestCode==1)
            {

                try {
                    imgImageView.setImageURI(data.getData());
                }catch (Exception e){}

            }


    }
    //set image from url for imageview

}
