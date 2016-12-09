package edu.uit.dictplus.ActivityTabStudy;

import edu.uit.dictplus.TraTu.Tab_AnhViet;

/**
 * Created by nmtri_000 on 10/29/2015.
 */
public class DauNhan {
    public static String getDauNhan(String voca)
    {
        // nhận full  danh sach tieng anh
        voca="@"+voca;
        String result="♥";
        int index= Tab_AnhViet.dataVocabulary.indexOf(voca);
        if(index!=-1)
        {
            result=Tab_AnhViet.dataVocabulary.substring(index);
            index=result.indexOf("\n");
            result=result.substring(0, index);
            index=result.indexOf("/");
            if(index!=-1) {
                result = result.split("/")[1];
                result=result.replace("/","");
            }
            else
                result="♥";

        }
        return  result;
    }
}
