package edu.uit.dictplus;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Login extends AppCompatActivity {
    CircleImageView mProfileImage;
    public static Boolean Login=false;
    Button mBtnFb,btnNor;
    TextView mUsername, mEmailID;
    ParseUser parseUser;
    String name = null, email = null;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
    }};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "edu.uit.dictplus",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        if(getIntent().getBooleanExtra("Login",true)) {
            if (ParseUser.getCurrentUser() != null) {
                Toast.makeText(Activity_Login.this, "Welcome back " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                Intent main = new Intent(Activity_Login.this, MainActivity.class);

                startActivity(main);
                finish();
            }
        }
        mBtnFb = (Button) findViewById(R.id.btn_fb_login);
        mProfileImage = (CircleImageView) findViewById(R.id.profile_image);

        mUsername = (TextView) findViewById(R.id.txt_name);
        mEmailID = (TextView) findViewById(R.id.txt_email);

        btnNor=(Button)findViewById(R.id.buttonLognor);
        btnNor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main=new Intent(Activity_Login.this,Activity_Login_Normal.class);
                startActivity(main);
            }
        });



        mBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseFacebookUtils.logInWithReadPermissionsInBackground(Activity_Login.this, mPermissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {

                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            getUserDetailsFromFB();

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            getUserDetailsFromParse();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void saveNewUser(Bitmap imgProfile) {
        parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(name);
        parseUser.setEmail(email);

//        Saving profile photo as a ParseFile
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        imgProfile.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] data = stream.toByteArray();
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        final ParseFile parseFile = new ParseFile("thumb.jpg", data);
        try {
            parseFile.save();
            parseUser.put("image", parseFile);
            parseUser.save();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        Toast.makeText(Activity_Login.this, "New user:" + name + " Signed up", Toast.LENGTH_SHORT).show();



    }


    private void getUserDetailsFromFB() {
        Bundle bundle = new Bundle();
        bundle.putString("fields", "first_name,last_name,email,name");
      GraphRequest rq=  new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        try {
                            email = response.getJSONObject().getString("email");
                            mEmailID.setText(email);
                            name = response.getJSONObject().getString("name");
                            mUsername.setText(name);
                            ProfilePhotoAsync profilePhotoAsync = new ProfilePhotoAsync(response.getJSONObject().getString("id"));
                            profilePhotoAsync.execute();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        rq.setParameters(bundle);
        rq.executeAsync();


    }

    private void getUserDetailsFromParse() {
        parseUser = ParseUser.getCurrentUser();

//Fetch profile photo
        try {
            ParseFile parseFile = parseUser.getParseFile("image");
            byte[] data = parseFile.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mProfileImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mEmailID.setText(parseUser.getEmail());
        mUsername.setText(parseUser.getUsername());
        Login=true;
        Toast.makeText(Activity_Login.this, "Welcome back " + mUsername.getText().toString(), Toast.LENGTH_SHORT).show();
        Intent main=new Intent(Activity_Login.this,MainActivity.class);
        startActivity(main);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }




    class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        String id;
        public Bitmap bitmap;

        public ProfilePhotoAsync(String id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Fetching data from URI and storing in bitmap


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProfileImage.setImageBitmap(bitmap);
            saveNewUser(bitmap);
        }
    }



}
