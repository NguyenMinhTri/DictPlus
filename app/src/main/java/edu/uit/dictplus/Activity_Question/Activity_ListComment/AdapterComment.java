package edu.uit.dictplus.Activity_Question.Activity_ListComment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 12/29/2015.
 */
public class AdapterComment   extends ArrayAdapter<ParseComment> {
    List<ParseComment> listData;
    ViewHolder viewHolder;
    Context mContext;
    public AdapterComment(Context context, List<ParseComment> objects){
        super(context, R.layout.item_comment, objects);
        listData = objects;
        mContext = context;
    }
    @Override
    public long getItemId(int position) {
        return  position;
    }
    static class ViewHolder {
        protected ImageView imgUser;
        protected TextView tvUsername,tvCm;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.item_comment, null);

            viewHolder = new ViewHolder();
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.ittvUser);
            viewHolder.tvCm = (TextView) convertView.findViewById(R.id.ittvcm);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.itimgUser);


            convertView.setTag(viewHolder);
            convertView.setTag(R.id.ittvUser, viewHolder.tvUsername);
            convertView.setTag(R.id.ittvcm, viewHolder.tvCm);
            convertView.setTag(R.id.itimgUser, viewHolder.imgUser);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //set tag

        //set data
        viewHolder.tvCm.setText(listData.get(position).getComment());
        viewHolder.tvUsername.setText(listData.get(position).getUser().getUsername());
        Bitmap bitmap=null;
        try {

            bitmap= BitmapFactory.decodeByteArray(listData.get(position).getUser().getParseFile("image").getData(), 0,listData.get(position).getUser().getParseFile("image").getData().length);

            viewHolder.imgUser.setImageBitmap(bitmap);
        } catch (Exception e) {

            try
            {
                bitmap= BitmapFactory.decodeByteArray(listData.get(position).getParseUser("User").fetchIfNeeded().getParseFile("image").getData(), 0,listData.get(position).getParseUser("User").fetchIfNeeded().getParseFile("image").getData().length);

                viewHolder.imgUser.setImageBitmap(bitmap);
            }catch (Exception e2){}
            e.printStackTrace();
        }



        return convertView;
    }
}
