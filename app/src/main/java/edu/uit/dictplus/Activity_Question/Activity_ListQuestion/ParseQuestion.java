package edu.uit.dictplus.Activity_Question.Activity_ListQuestion;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by nmtri_000 on 12/28/2015.
 */
@ParseClassName("ParseQuestion")
public class ParseQuestion extends ParseObject {

    Bitmap bitmap;
    public ParseUser User;
    public String getQues() {
        return getString("QUESTION");
    }

    public void setQues(String describe) {
        put("QUESTION",describe);
    }

    public byte[] getFileImage() {
        try {

            return getParseFile("IMAGE").getData();
        } catch (ParseException e) {

            e.printStackTrace();
            return  null;
        }
    }


    public void setFileImage(ParseFile fileImage) {

        put("IMAGE",fileImage);
    }

    public Date getDate() {
        return getCreatedAt();
    }




    public ParseUser getUser() {
        try {
            User  = getParseUser("User").fetchIfNeeded();
            return  User;
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public void setUser(ParseUser user) {
        put("User",user);
    }

}

