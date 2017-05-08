package com.seth.elearning20.frontfragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seth.elearning20.Login;
import com.seth.elearning20.R;
import com.seth.elearning20.dialog.ImageDialogFragment;
import com.seth.elearning20.info.UserInfo;
import com.seth.elearning20.sqlite.SqlDao;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Seth on 2017/4/26.
 */

public class MinePage extends Fragment implements View.OnClickListener{
    private TextView quit;
    private static CircleImageView usr_frog;
    private static File imgFile;
    private UserInfo userinfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userinfo = UserInfo.getUserInfo(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);

        quit = (TextView) view.findViewById(R.id.quit);
        usr_frog = (CircleImageView) view.findViewById(R.id.frog_image);
        /*设置头像*/
        if(userinfo.getFrogUrl()!=null) {
            usr_frog.setImageBitmap(BitmapFactory.decodeFile(userinfo.getFrogUrl()));
            Log.i("resultsql",userinfo.getFrogUrl());
        }
        quit.setOnClickListener(this);
        usr_frog.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.quit:
                new SqlDao(getContext()).delUsr();
                startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
                break;

            case R.id.frog_image:
                ImageDialogFragment dialog = ImageDialogFragment.NewInstace(2,"18868217689");
                dialog.show(getFragmentManager(),"imageDialogFragment");

                break;
        }
    }

    public static void setImg_file(File img_file) {
        imgFile = img_file;
    }

    public static CircleImageView getFrog() {
        return usr_frog;
    }

}
