package com.seth.elearning20.frontfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.seth.elearning20.R;
import com.seth.elearning20.adapter.MusicAdapter;
import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.info.MusicList;

import java.util.List;

/**
 * Created by Seth on 2017/4/27.
 */

public class SecondPage extends Fragment implements View.OnClickListener{

    private Button LocalBtn;
    private Button OnlineBtn;
    private Fragment currentFragenmt;
    private Fragment localMusic;
    private Fragment onlineMusic;
    private FragmentManager fm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_page_fragment, container, false);
        /*调用getChildFragmentManager()获取子fragment*/

        LocalBtn = (Button) view.findViewById(R.id.local_music);
        OnlineBtn = (Button) view.findViewById(R.id.online_music);


        localMusic = new SecondChildPage1();
        currentFragenmt = localMusic;
        fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.music_content, localMusic).commit();

        LocalBtn.setOnClickListener(this);
        OnlineBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.local_music:
                if(localMusic==null)
                    localMusic = new SecondChildPage1();
                addOrShowFragment(fm.beginTransaction(),localMusic);

                break;
            case R.id.online_music:
                if(onlineMusic==null)
                    onlineMusic = new SecondChildPage2();
                addOrShowFragment(fm.beginTransaction(),onlineMusic);
                break;
        }
    }

    //fragment替换方法
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment){
        if(currentFragenmt == fragment){
            return ;
        }
        if(!fragment.isAdded()){    //如果当前fragment为被添加，则添加到Fragment管理器中
            transaction.hide(currentFragenmt).add(R.id.music_content, fragment).commit();
        }else{
            transaction.hide(currentFragenmt).show(fragment).commit();
        }
        currentFragenmt = fragment;
    }

}
