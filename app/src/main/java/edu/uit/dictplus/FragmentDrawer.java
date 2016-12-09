package edu.uit.dictplus;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();


    private ActionBarDrawerToggle mDrawerToggle;
    public  static DrawerLayout mDrawerLayout;
    TextView tvName,tvEmail;
    ListView recyclerView;
    public static View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    CircleImageView mProfileImage;
    Context activity;
    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener,Context activity) {
        this.drawerListener = listener;
        this.activity=activity;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // drawer labels

        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        try {
            recyclerView = (ListView) layout.findViewById(R.id.drawerList);
            tvName=(TextView)layout.findViewById(R.id.tvNameUser);
            tvEmail=(TextView)layout.findViewById(R.id.tvEmail);

            mProfileImage = (CircleImageView) layout.findViewById(R.id.profile_image);
            tvEmail.setText(ParseUser.getCurrentUser().getEmail());
            tvName.setText(ParseUser.getCurrentUser().getUsername());
            ParseFile parseFile = ParseUser.getCurrentUser().getParseFile("image");
            byte[] data = parseFile.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mProfileImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapterA=new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1,titles);

        recyclerView.setAdapter(adapterA);
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerListener.onDrawerItemSelected(view, i);
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/png");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        return layout;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) {
            try {
                mProfileImage.setImageURI(data.getData());
                Bitmap bitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] byteArray = stream.toByteArray();
                final ParseFile file = new ParseFile("image.png", byteArray);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseUser.getCurrentUser().put("image",file);
                        try {
                            ParseUser.getCurrentUser().save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

            }catch (Exception e)
            {

            }
        }


    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }




    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
