package edu.uit.dictplus;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.interceptors.ParseLogInterceptor;

import edu.uit.dictplus.Activity_Question.Activity_ListComment.ParseComment;
import edu.uit.dictplus.Activity_Question.Activity_ListQuestion.ParseQuestion;
import edu.uit.dictplus.ParseHistory.History;

/**
 * Created by nmtri_000 on 12/30/2015.
 */
public class DictPlusApp extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        Parse.enableLocalDatastore(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseObject.registerSubclass(History.class);
        ParseObject.registerSubclass(ParseQuestion.class);
        ParseObject.registerSubclass(ParseComment.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId") // should correspond to APP_ID env variable
                .clientKey("myMasterKey")  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://dictplus.herokuapp.com/parse/").enableLocalDataStore().build());

        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("Question");

        ParseFacebookUtils.initialize(getApplicationContext());




    }
}
