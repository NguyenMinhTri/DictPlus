package edu.uit.dictplus.Activity_Question.Activity_ListComment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import edu.uit.dictplus.Activity_Question.Activity_ListQuestion.ParseQuestion;
import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 12/29/2015.
 */


public class Activity_Comment extends AppCompatActivity {
    Button btnCm;
    ParsePushBroadcastReceiver receiver = null;
    EditText etCm;
    ParseQuestion parseQuestion;
    TextView tvUserName, tvDate, tvQues;
    ImageView imgUser, imgQues;
    ListView lvCm;
    ProgressBar progressBar = null;
    AdapterComment mAdapter;
    String id;

    @Override
    protected void onStop() {
        super.onStop();
        if (ParseUser.getCurrentUser().getUsername() != tvUserName.getText().toString()) {
            ParsePush.unsubscribeInBackground(id);
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ParseUser.getCurrentUser().getUsername() != tvUserName.getText().toString()) {
            ParsePush.unsubscribeInBackground(id);
        }
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bình Luận");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnCm = (Button) findViewById(R.id._cmBtncm);
        etCm = (EditText) findViewById(R.id._cmEtcm);
        lvCm = (ListView) findViewById(R.id._cmlistview);
        imgQues = (ImageView) findViewById(R.id.feedImage1);
        imgUser = (ImageView) findViewById(R.id.profilePic);
        tvQues = (TextView) findViewById(R.id.txtStatusMsg);
        tvDate = (TextView) findViewById(R.id.timestamp);
        tvUserName = (TextView) findViewById(R.id.name);

        tvDate.setText(getIntent().getStringExtra("Date"));
        tvQues.setText(getIntent().getStringExtra("Ques"));
        tvUserName.setText(getIntent().getStringExtra("Username"));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        byte[] byteArray = getIntent().getByteArrayExtra("ImageUser");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imgUser.setImageBitmap(bmp);


        try {
            byteArray = getIntent().getByteArrayExtra("ImageQes");
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imgQues.setImageBitmap(bmp);

        } catch (Exception e) {
            imgQues.setVisibility(View.GONE);
        }
        id = getIntent().getStringExtra("IdQues");
        if (ParseUser.getCurrentUser().getUsername() != tvUserName.getText().toString()) {
            ParsePush.subscribeInBackground(id);
        }
        //get data comment

        parseQuestion = (ParseQuestion) ParseObject.createWithoutData("ParseQuestion", id);
        ParseQuery<ParseComment> query = ParseQuery.getQuery(ParseComment.class);
        query.orderByDescending("createdAt");
        query.whereEqualTo("PARSEQUESTION", parseQuestion);
        //    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

        query.findInBackground(new FindCallback<ParseComment>() {
            @Override
            public void done(List<ParseComment> list, ParseException e) {
                if (e == null && list != null) {
                    mAdapter = new AdapterComment(getApplicationContext(), list);
                    lvCm.setAdapter(mAdapter);
                    progressBar.setVisibility(View.GONE);
                }


            }
        });
        ParsePush push = new ParsePush();

        btnCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etCm.getText().toString();
                if (comment != "" && comment != null) {
                    etCm.setText("");
                    btnCm.setEnabled(false);

                        ParsePush push = new ParsePush();
                        push.setChannel(id);
                        push.setMessage("[" + ParseUser.getCurrentUser().getUsername() + "] Đã Trả Lời Câu Hỏi : [ " + tvQues.getText().toString() + " ]");
                        push.sendInBackground();

                    final ParseComment parseComment = new ParseComment();
                    parseComment.setUser(ParseUser.getCurrentUser());
                    parseComment.setComment(comment);
                    parseComment.setParseQuestion(parseQuestion);
                    parseComment.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {


                        }
                    });
                }
            }
        });
        IntentFilter filter = new IntentFilter
                ("com.parse.push.intent.RECEIVE");
        receiver = new ParsePushBroadcastReceiver() {
            //hàm này sẽ tự động được kích hoạt khi có tin nhắn gửi tới
            public void onPushReceive(Context arg0, Intent arg1) {
                //Tui tách ra hàm riêng để xử lý
                //chú ý là dữ liệu trong tin nhắn được lưu trữ trong arg1
                processReceive(arg0, arg1);
            }
        };
        registerReceiver(receiver, filter);
        //set data commnet

    }

    protected void onDestroy() {
        super.onDestroy();
        //hủy bỏ đăng ký khi tắt ứng dụng
        unregisterReceiver(receiver);
        if (ParseUser.getCurrentUser().getUsername() != tvUserName.getText().toString()) {
            ParsePush.unsubscribeInBackground(id);
        }
    }

    public void processReceive(Context context, Intent intent) {


        String channel = intent.getExtras().getString("com.parse.Channel");
        if (channel.toLowerCase().equals(id.toLowerCase()) ) {

            try {
                parseQuestion = (ParseQuestion) ParseObject.createWithoutData("ParseQuestion", id);
                ParseQuery<ParseComment> query = ParseQuery.getQuery(ParseComment.class);
                query.orderByDescending("createdAt");
                query.whereEqualTo("PARSEQUESTION", parseQuestion);
                query.getFirstInBackground(new GetCallback<ParseComment>() {
                    @Override
                    public void done(ParseComment parseComment, ParseException e) {
                        try {
                            if (parseComment.getObjectId() != mAdapter.getItem(0).getObjectId()) {
                                mAdapter.insert(parseComment, 0);
                                mAdapter.notifyDataSetChanged();
                            }
                            btnCm.setEnabled(true);
                        }catch (Exception e2)
                        {
                            mAdapter.insert(parseComment, 0);
                            mAdapter.notifyDataSetChanged();
                            btnCm.setEnabled(true);
                        }
                    }
                });

            } catch (Exception e) {

            }
        }
    }
}








