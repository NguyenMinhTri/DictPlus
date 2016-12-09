package edu.uit.dictplus.TraTu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import edu.uit.dictplus.DataOffline.DataAdapter;
import edu.uit.dictplus.DataOffline.Popup_TraTu;
import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 12/31/2015.
 */
public class Tab_VietAnh extends Fragment {
    public static DataAdapter mAdapterVA;
    public static  ListView mListViewVA;
    private EditText editText;
    public Tab_VietAnh(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_anhviet, container, false);
        mListViewVA=(ListView)view.findViewById(R.id.listView);




        editText=(EditText)view.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                mAdapterVA.getFilter().filter(cs);
                mAdapterVA.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mListViewVA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view.findViewById(R.id.tvVoca)).getText().toString();
                Intent acPop=new Intent(getActivity(),Popup_TraTu.class);
                acPop.putExtra("Value",((TextView) view.findViewById(R.id.tvVoca)).getText().toString().replace(" /"," ☺/")+"\n"+((TextView) view.findViewById(R.id.tvMean)).getText().toString());
                acPop.putExtra("VietAnh","VietAnh");
                startActivity(acPop);
                Toast.makeText(getActivity(), ((TextView) view.findViewById(R.id.tvVoca)).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });



        return  view;
    }




}
