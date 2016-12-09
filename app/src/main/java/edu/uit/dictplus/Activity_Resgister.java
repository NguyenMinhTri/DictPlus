package edu.uit.dictplus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nmtri_000 on 1/2/2016.
 */
public class Activity_Resgister  extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    Button res;
    EditText user,pass;
    CircleImageView img;
    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Set up the login form.

        res=(Button)findViewById(R.id.buttonRes);
        img=(CircleImageView)findViewById(R.id.profile_image);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/png");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        user=(EditText)findViewById(R.id.editText2);
        pass=(EditText)findViewById(R.id.editText3);

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Activity_Resgister.this, "Thông Báo", "Vui Long Đợi");

                        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        final byte[] byteArray = stream.toByteArray();
                        final ParseFile file = new ParseFile("image.png", byteArray);

                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                ParseUser parseUser = new ParseUser();
                                parseUser.setUsername(user.getText().toString());
                                parseUser.setPassword(pass.getText().toString());
                                parseUser.put("image", file);
                                try {
                                    parseUser.signUp();
                                    Toast.makeText(Activity_Resgister.this, "Đăng Kí Thành Công Vui Lòng Đăng Nhập !", Toast.LENGTH_SHORT).show();
                                    Intent main = new Intent(Activity_Resgister.this, Activity_Login_Normal.class);
                                    startActivity(main);
                                    finish();

                                } catch (ParseException ee) {
                                    Toast.makeText(getApplicationContext(),
                                            "Sign up Error " + ee.getMessage(), Toast.LENGTH_LONG)
                                            .show();
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });





            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) {
            try {
                img.setImageURI(data.getData());
            } catch (Exception e)
            {

            }

        }


    }

}

