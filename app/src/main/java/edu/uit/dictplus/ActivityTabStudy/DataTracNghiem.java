package edu.uit.dictplus.ActivityTabStudy;

/**
 * Created by nmtri_000 on 10/28/2015.
 */
public class DataTracNghiem {
    String cauA,CauB,CauC,CauD,DapAn,Voca;



    byte[] fileMp3;

    public String getCauA() {
        return cauA;
    }

    public String getCauB() {
        return CauB;
    }

    public String getCauC() {
        return CauC;
    }

    public void setCauA(String cauA) {
        this.cauA = cauA;
    }

    public void setVoca(String voca) {
        Voca = voca;
    }

    public void setDapAn(String dapAn) {
        DapAn = dapAn;
    }

    public void setCauD(String cauD) {
        CauD = cauD;
    }

    public void setCauB(String cauB) {
        CauB = cauB;
    }

    public void setCauC(String cauC) {
        CauC = cauC;
    }

    public String getCauD() {

        return CauD;
    }

    public String getDapAn() {
        return DapAn;
    }

    public String getVoca() {
        return Voca;
    }

    public DataTracNghiem(String voca, String dapAn, String cauD, String cauC, String cauB, String cauA) {
        Voca = voca;
        DapAn = dapAn;
        CauD = cauD;
        CauC = cauC;
        CauB = cauB;
        this.cauA = cauA;

    }
}
