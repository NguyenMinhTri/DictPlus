package edu.uit.dictplus.DataOffline;

/**
 * Created by nmtri_000 on 10/18/2015.
 */
public class DataOffline {
    String Voca;
    String Mean;

    public DataOffline(String voca, String mean) {
        Voca = voca;
        Mean = mean;
    }

    public void setMean(String mean) {
        Mean = mean;
    }

    public void setVoca(String voca) {
        Voca = voca;
    }

    public String getVoca() {
        return Voca;
    }

    public String getMean() {
        return Mean;
    }
}
