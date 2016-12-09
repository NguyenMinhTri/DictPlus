package edu.uit.dictplus.Activity_Question.Activity_ListQuestion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.List;

import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 12/29/2015.
 */
public class AdapterQuestion  extends ArrayAdapter<ParseQuestion> {
    List<ParseQuestion> listData;
    ViewHolder viewHolder;
    Context mContext;
    public AdapterQuestion(Context context, List<ParseQuestion> objects){
        super(context, R.layout.item_status, objects);
        listData = objects;
        mContext = context;
    }
    @Override
    public long getItemId(int position) {
        return  position;
    }
    static class ViewHolder {
        protected ImageView imgUser,imgQues;
        protected TextView tvUsername,tvDate,tvQues;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.item_status, null);

             viewHolder = new ViewHolder();
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.tvQues = (TextView) convertView.findViewById(R.id.txtStatusMsg);
            viewHolder.imgQues = (ImageView) convertView.findViewById(R.id.feedImage1);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.profilePic);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.name, viewHolder.tvUsername);
            convertView.setTag(R.id.timestamp, viewHolder.tvDate);
            convertView.setTag(R.id.txtStatusMsg, viewHolder.tvQues);
            convertView.setTag(R.id.feedImage1, viewHolder.imgQues);
            convertView.setTag(R.id.profilePic, viewHolder.imgUser);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //set tag

        //set data
        viewHolder.tvDate.setText(listData.get(position).getDate().toString());
        viewHolder.tvUsername.setText(listData.get(position).getUser().getUsername());
        viewHolder.tvQues.setText(listData.get(position).getQues());
        //set image
        Bitmap bitmap=null;
        try {
            viewHolder.imgQues.setVisibility(View.VISIBLE);
             bitmap = BitmapFactory.decodeByteArray(listData.get(position).getFileImage(), 0, listData.get(position).getFileImage().length);
            viewHolder.imgQues.setImageBitmap(bitmap);



            viewHolder.imgQues.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            viewHolder.imgQues.layout(0, 0,
                    viewHolder.imgQues.getMeasuredWidth(),
                    viewHolder.imgQues.getMeasuredHeight());
            ViewTreeObserver viewTree =  viewHolder.imgQues.getViewTreeObserver();
            final Bitmap finalBitmap = bitmap;
            viewTree.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {


                    int swidth =viewHolder.imgQues.getMeasuredWidth();
                    int new_height = 0;
                    new_height = swidth * finalBitmap.getHeight() / finalBitmap.getWidth();
                    viewHolder.imgQues.getLayoutParams().height=new_height;
                    viewHolder.imgQues.requestLayout();
                    return true;
                }
            });

        }catch (Exception e)
        {
            String error=e.getMessage();
            viewHolder.imgQues.setVisibility(View.GONE);
        }
        try {
            bitmap= BitmapFactory.decodeByteArray(listData.get(position).getUser().getParseFile("image").getData(), 0,listData.get(position).getUser().getParseFile("image").getData().length);
            viewHolder.imgUser.setImageBitmap(bitmap);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return convertView;
    }

}
