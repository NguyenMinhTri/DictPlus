package edu.uit.dictplus.Activity_Question.Activity_ListComment;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.uit.dictplus.Activity_Question.Activity_ListQuestion.ParseQuestion;


/**
 * Created by nmtri_000 on 12/29/2015.
 */
@ParseClassName("ParseComment")
public class ParseComment extends ParseObject {

    public ParseUser User;
    public String getComment() {
        return getString("COMMENT");
    }

    public void setComment(String describe) {
        put("COMMENT",describe);
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
        User=user;
        put("User",User);
    }
    public void setParseQuestion(ParseQuestion parseQuestion) {
        put("PARSEQUESTION",parseQuestion);
    }

}


