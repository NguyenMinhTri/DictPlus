package edu.uit.dictplus.Activity_Question.Activity_ListQuestion;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import java.util.List;

import edu.uit.dictplus.Activity_Question.Activity_ListComment.Activity_Comment;
import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 12/28/2015.
 */
public class Activity_ListQuestion extends AppCompatActivity {
    ListView listView;
    AdapterQuestion mAdapter;
    ParsePushBroadcastReceiver receiver = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question);
        listView=(ListView)findViewById(R.id.listQuestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Danh Sách Các Câu Hỏi");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ParseQuery<ParseQuestion> query= ParseQuery.getQuery(ParseQuestion.class);
        query.orderByDescending("createdAt");
    //    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

        query.findInBackground(new FindCallback<ParseQuestion>() {
            @Override
            public void done(List<ParseQuestion> list, ParseException e) {
                if (e == null && list!=null) {
                    mAdapter=new AdapterQuestion(getApplicationContext(),list);
                    listView.setAdapter(mAdapter);
                }


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent comment=new Intent(Activity_ListQuestion.this, Activity_Comment.class);
                try {

                    try {
                        comment.putExtra("ImageQes", mAdapter.getItem(position).getFileImage());
                    }catch (Exception e){}
                    comment.putExtra("Ques",mAdapter.getItem(position).getQues());
                    comment.putExtra("IdQues",mAdapter.getItem(position).getObjectId());
                    comment.putExtra("Date",mAdapter.getItem(position).getDate().toString());
                    comment.putExtra("Username",mAdapter.getItem(position).getUser().getUsername());
                    comment.putExtra("ImageUser",mAdapter.getItem(position).User.getParseFile("image").getData());
                    startActivity(comment);
                } catch (ParseException e) {
                    e.printStackTrace();
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
    }
    public void processReceive(Context context, Intent intent) {


        String channel = intent.getExtras().getString("com.parse.Channel");
        if (channel.toLowerCase().equals("question") ) {

            try {
                ParseQuery<ParseQuestion> query= ParseQuery.getQuery(ParseQuestion.class);
                query.orderByDescending("createdAt");
                //    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);

                query.getFirstInBackground(new GetCallback<ParseQuestion>() {
                    @Override
                    public void done(ParseQuestion parseQuestion, ParseException e) {
                        try {
                            if (parseQuestion.getObjectId() != mAdapter.getItem(0).getObjectId()) {
                                mAdapter.insert(parseQuestion, 0);
                                mAdapter.notifyDataSetChanged();
                            }
                        }catch (Exception e2)
                        {
                            mAdapter.insert(parseQuestion, 0);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });

            } catch (Exception e) {

            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        //hủy bỏ đăng ký khi tắt ứng dụng
        unregisterReceiver(receiver);

    }
}
