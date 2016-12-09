package edu.uit.dictplus.DataOffline;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.uit.dictplus.R;
import edu.uit.dictplus.SpeechEnglish;


/**
 * Created by nmtri_000 on 10/18/2015.
 */
public class DataAdapter  extends ArrayAdapter<DataOffline> implements Filterable {
    private Context mContext;
    private List<DataOffline> listData,myArrAllFilter;
    private TextView tvVoca,tvMean;
    private Button btnNghe;
    private boolean TabVietAnh=false;
    private contactViewFilter filter;
    public DataAdapter(Context context, List<DataOffline> objects,boolean isVietAnh) {
        super(context, R.layout.customlisview, objects);
        try {
            myArrAllFilter = new ArrayList<>();
            myArrAllFilter.addAll(objects);
            listData = objects;
            mContext = context;
            TabVietAnh = isVietAnh;
        }catch (Exception e)
        {

        }
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) {
            filter = new contactViewFilter();
        }
        return filter;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.customlisview, null);
        }

        tvVoca=(TextView)convertView.findViewById(R.id.tvVoca);
        tvMean=(TextView)convertView.findViewById(R.id.tvMean);
        btnNghe=(Button)convertView.findViewById(R.id.btnNghe);
        if(TabVietAnh==true)
            btnNghe.setVisibility(View.GONE);
        btnNghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout vwParentRow = (RelativeLayout) v.getParent();
                TextView child = (TextView) vwParentRow.getChildAt(0);
               new SpeechEnglish(mContext,child.getText().toString().split(" /")[0].toLowerCase().trim());
            }
        });

        tvVoca.setText(listData.get(position).getVoca());
        tvMean.setText(listData.get(position).getMean());
        tvVoca.setTextColor(Color.RED);


        return convertView;
    }

    private class contactViewFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                List<DataOffline> myArrFilter = new ArrayList<>();

                for(int i = 0; i < myArrAllFilter.size(); i++)
                {


                    if(myArrAllFilter.get(i).getVoca().toLowerCase(Locale.getDefault()).contains(constraint))
                    {
                        if(myArrAllFilter.get(i).getVoca().toLowerCase().indexOf((String.valueOf(constraint)).toLowerCase().trim())==0)
                        myArrFilter.add(myArrAllFilter.get(i));


                    }
                }
                result.count = myArrFilter.size();
                result.values = myArrFilter;
            }
            else
            {
                synchronized(this)
                {
                    result.values = myArrAllFilter;
                    result.count = myArrAllFilter.size();
                }
            }
            return result;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // TODO Auto-generated method stub

            listData = (ArrayList<DataOffline>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = listData.size(); i < l; i++)
                add(listData.get(i));
            notifyDataSetInvalidated();
        }

    }
}
