package edu.uit.dictplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uit.dictplus.ActivityTabStudy.Constants;
import edu.uit.dictplus.ActivityTabStudy.DataTracNghiem;
import edu.uit.dictplus.ActivityTabStudy.DichNhanh;
import edu.uit.dictplus.ActivityTabStudy.FloatingWindow_NhacNho;
import edu.uit.dictplus.ActivityTabStudy.FragmentTracNghiem;
import edu.uit.dictplus.ActivityTabStudy.TestSpeechVoca;

/**
 * Created by nmtri_000 on 12/31/2015.
 */
public class FragmentStudy extends Fragment {
    public Button btnNoti;
    public static boolean isAnh=true,isViet=false;
    public CheckBox isTA,isTV;
    EditText edTime;
    boolean isOn=false;
    public static  Integer mTime=1;
    public static List<DataTracNghiem> mListDataTracNghiem=new ArrayList<>();
    public FragmentStudy() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_study, container, false);

        Button btnStart=(Button)rootView.findViewById(R.id.btnStartTracNghiem);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mListDataTracNghiem.size() <4)

                    Toast.makeText(getActivity(), "Chọn chưa đủ 4 từ trở lên", Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(getActivity(), FragmentTracNghiem.class));
                }
            }
        });
        Button btnSpeech=(Button)rootView.findViewById(R.id.btnCheckPhatAm);
        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListDataTracNghiem.size() == 0)
                    Toast.makeText(getActivity(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
                else {


                    startActivity(new Intent(getActivity(), TestSpeechVoca.class));
                }
            }
        });
        final Button btnSearch=(Button)rootView.findViewById(R.id.btnsdTranhanh);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    getActivity().stopService(new Intent(getActivity(), DichNhanh.class));
                    if(!isOn) {
                        getActivity().startService(new Intent(getActivity(), DichNhanh.class));
                        isOn=true;
                    }else{
                        getActivity().stopService(new Intent(getActivity(), DichNhanh.class));
                        isOn=false;
                    }


                }catch (Exception e)
                {

                }
            }

        });

        isTV=((CheckBox)rootView.findViewById(R.id.cbsdTV));
        isTA=((CheckBox)rootView.findViewById(R.id.cbsdTA));

        edTime=((EditText) rootView.findViewById(R.id.edsdTime));
        isTA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    isTV.setEnabled(true);
                else isTV.setEnabled(false);
            }
        });
        btnNoti=(Button)rootView.findViewById(R.id.btnNotifition);
        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListDataTracNghiem.size() == 0)
                    Toast.makeText(getActivity(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
                else {

                    Intent service = new Intent(getActivity(), FloatingWindow_NhacNho.class);
                    if (!FloatingWindow_NhacNho.IS_SERVICE_RUNNING) {
                        isAnh=isTA.isChecked();
                        isViet=isTV.isChecked();

                        String kiemtra=edTime.getText().toString();
                        try {
                            mTime = Integer.parseInt(kiemtra);
                        }catch (Exception e){
                            e.printStackTrace();
                            mTime=1;
                        }
                        service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                        FloatingWindow_NhacNho.IS_SERVICE_RUNNING = true;
                        btnNoti.setText("Kết Thúc Nhắc Nhở");

                    } else {



                        service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                        FloatingWindow_NhacNho.IS_SERVICE_RUNNING = false;
                        btnNoti.setText("Bắt Đầu Nhắc Nhở");

                    }
                    getActivity().startService(service);


                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

