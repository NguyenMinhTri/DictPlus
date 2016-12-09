package edu.uit.dictplus.TraTu;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 12/31/2015.
 */
public class Tab_Online extends Fragment{
    EditText edtSeachOn;
    WebView webResult;
    String chuyenNganh;
    RadioButton rbTT,rbKT,rbTD;
    ProgressBar progressBarTraTu = null;
    public Tab_Online(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_online, container, false);
        progressBarTraTu=(ProgressBar)view.findViewById(R.id.progressBarTraTu);
        try {
            Translate.setClientId("triuit");
            Translate.setClientSecret("tEYUWKDLnfpO5zr/olEbHdXqa5Qz33U7Fx4e3AQxVJU=");
        }
        catch (Exception e){}
        edtSeachOn=(EditText)view.findViewById(R.id.edtVocaOnline);
        webResult=(WebView)view.findViewById(R.id.wvResult);
        rbTT=(RadioButton)view.findViewById(R.id.rbToanTin);
        rbKT=(RadioButton)view.findViewById(R.id.rbKinhte);
        rbTD=(RadioButton)view.findViewById(R.id.rbThongdung);
        rbTT.setChecked(true);
        chuyenNganh="TT";
        rbTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenNganh="TT";
                rbTT.setChecked(true);
                rbKT.setChecked(false);
                rbTD.setChecked(false);
            }
        });

        rbKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenNganh="KT";
                rbTT.setChecked(false);
                rbKT.setChecked(true);
                rbTD.setChecked(false);
            }
        });

        rbTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenNganh = "TD";
                rbTT.setChecked(false);
                rbKT.setChecked(false);
                rbTD.setChecked(true);
            }
        });




        webResult.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webResult.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        edtSeachOn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    webResult.loadData("", "text/html; charset=utf-8", null);
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    progressBarTraTu.setVisibility(View.VISIBLE);
                    final HashMap<String,String> vocabulary=new HashMap<String, String>();
                    vocabulary.put("voca",edtSeachOn.getText().toString()+"#"+chuyenNganh);
                    switch (chuyenNganh) {
                        case "TD":

                            ParseCloud.callFunctionInBackground("getPage", vocabulary, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object o, ParseException e) {
                                    if (e == null && (String) o != "") {
                                        String htmlResult = (String) o;
                                        webResult.loadData(htmlResult, "text/html; charset=utf-8", null);
                                        String saveText = htmlResult.replace("<br/>", "♥");
                                        saveText = Html.fromHtml((String) saveText).toString() + "♥";
                                        SaveParse(saveText, edtSeachOn.getText().toString());
                                    }
                                    else {
                                        new BingTranslator().execute(edtSeachOn.getText().toString());
                                    }
                                }
                            });

                            break;
                        case "TT":
                            ParseCloud.callFunctionInBackground("getPage", vocabulary, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object o, ParseException e) {
                                    if(e==null && (String)o!="") {
                                        String htmlResult = (String) o;
                                        webResult.loadData(htmlResult, "text/html; charset=utf-8", null);
                                        String saveText = htmlResult.replace("<br/>", "♥");
                                        saveText = Html.fromHtml((String) saveText).toString()+"♥";
                                        SaveParse(saveText,edtSeachOn.getText().toString());
                                    }
                                    else
                                    {
                                        new BingTranslator().execute(edtSeachOn.getText().toString());
                                    }
                                }
                            });
                            break;
                        case "KT":
                            ParseCloud.callFunctionInBackground("getPage", vocabulary, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object o, ParseException e) {
                                    if(e==null && (String)o!="") {
                                        String htmlResult = (String) o;
                                        webResult.loadData(htmlResult, "text/html; charset=utf-8", null);
                                        String saveText = htmlResult.replace("<br/>", "♥");
                                        saveText = Html.fromHtml((String) saveText).toString() + "♥";
                                        SaveParse(saveText,edtSeachOn.getText().toString());
                                    }
                                    else
                                    {
                                        new BingTranslator().execute(edtSeachOn.getText().toString());
                                    }
                                }
                            });
                            break;

                    }


                }
                return false;
            }
        });

        return view;
    }

    void SaveParse(String Mean,String Voca)
    {
        int index = Mean.indexOf("♥");
        Mean=Mean.substring(index+1);
        index=Mean.indexOf("♥");
        Mean=Mean.substring(0, index);
        Tab_AnhViet.createHistory(Voca, Mean);
        progressBarTraTu.setVisibility(View.GONE);
    }

    private class BingTranslator extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {


                return translate(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                webResult.loadData(result, "text/html; charset=utf-8", null);
                Tab_AnhViet.createHistory(edtSeachOn.getText().toString(), result);
                progressBarTraTu.setVisibility(View.GONE);
            }
        }

    }
    public String translate(String text) throws Exception{


        // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
        Translate.setClientId("triuit"); //Change this
        Translate.setClientSecret("tEYUWKDLnfpO5zr/olEbHdXqa5Qz33U7Fx4e3AQxVJU="); //change


        String translatedText = "";

        translatedText = Translate.execute(text,Language.VIETNAMESE);

        return translatedText;
    }
}
