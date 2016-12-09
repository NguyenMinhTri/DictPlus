package edu.uit.dictplus.Activity_Question.ActivitySearchImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import edu.uit.dictplus.R;



public class ThumbnailAdapter extends ArrayAdapter<Object> {

	public static final int text1 = android.R.id.text1;
	public static final int text2 = android.R.id.text2;
	public static final int icon = android.R.id.icon;
	public LayoutInflater mInflater;
	public Bitmap[] bm;
	public Object[] objects;
	
	//�R���X�g���N�^
	public ThumbnailAdapter(Context context, Object[] objects, Bitmap[] bm){
		super(context, text1, objects);
		this.bm = bm;
		this.objects = objects;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_image_search, null);
		}
		
		TextView tv1 = (TextView)convertView.findViewById(text1);
		TextView tv2 = (TextView)convertView.findViewById(text2);
		ImageView iv = (ImageView)convertView.findViewById(icon);
		
		JSONObject jo = (JSONObject)objects[position];
		String hostname ="";
		String title = "";
		
		try{
			URL mediaUrl = new URL(jo.getString("MediaUrl"));
			hostname = mediaUrl.getHost();
			title = jo.getString("Title");
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		tv1.setText(title);
		tv2.setText(hostname);
		iv.setImageBitmap(bm[position]);
		
		return convertView;
	}
	
}
