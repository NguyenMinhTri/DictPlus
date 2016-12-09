package edu.uit.dictplus.ParseHistory;

/**
 * Created by nmtri_000 on 10/25/2015.
 */

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("History")
public class History extends ParseObject {

    public History(){

    }
    public void setVocabulary(String Voca){
        put("Voca", Voca);
    }
    public Boolean getCheck(){
        return getBoolean("Check");
    }
    public String getDauNhan(){return getString("DauNhan");}
    public String getVocabulay(){return getString("Voca");}
    public String getMean(){
        return getString("Mean");
    }
    public int getSTT(){return getInt("STT");}
    public void setMean(String mean){put("Mean", mean);}
    public void setVoca(String Voca){put ("Voca",Voca);}
    public void setDauNhan(String dauNhan){put("DauNhan",dauNhan);}
    public void setCheck(boolean check){put("Check",check);}
    public void setSTT(int stt){put("STT",stt);}
    public byte[] getMp3() {
        try {
            return getParseFile("Listen").getData();
        } catch (Exception e) {

            return  null;
        }
    }

}
