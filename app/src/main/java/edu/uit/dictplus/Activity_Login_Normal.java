package edu.uit.dictplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A login screen that offers login via email/password.
 */
public class Activity_Login_Normal extends AppCompatActivity  {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    Button login,signup;
    EditText user,pass;
    ProgressDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_normal);
        // Set up the login form.
        login=(Button)findViewById(R.id.btn_fb_login);
        signup=(Button)findViewById(R.id.btn_fb_dangki);

        user=(EditText)findViewById(R.id.editText2);
        pass=(EditText)findViewById(R.id.editText3);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main=new Intent(Activity_Login_Normal.this,Activity_Resgister.class);
                startActivity(main);
            }
        });


        //dang nhap
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Activity_Login_Normal.this, "Thông Báo", "Vui Long Đợi");
                ParseUser.logInInBackground(user.getText().toString(), pass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e==null)
                        {
                            Activity_Login.Login=true;
                            Toast.makeText(Activity_Login_Normal.this, "Welcome back " + user.getText().toString(), Toast.LENGTH_SHORT).show();
                            Intent main=new Intent(Activity_Login_Normal.this,MainActivity.class);
                            startActivity(main);
                            finish();
                        }
                        else {
                            Toast.makeText(Activity_Login_Normal.this, "Tên Đăng Nhập Hoặc Mật Khẩu Không Đúng", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

}

