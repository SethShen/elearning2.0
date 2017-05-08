package com.seth.elearning20.frontfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.seth.elearning20.R;
import com.seth.elearning20.adapter.MusicAdapter;
import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.info.MusicList;
import com.seth.elearning20.utils.AudioRecoderUtils;

import java.util.List;

/**
 * Created by Seth on 2017/4/26.
 */

public class SpokenPage extends Fragment implements View.OnClickListener{
    private ImageView startOrPauseRecoder;
    private ImageView storeRecoder;
    private ImageView deleteRecoder;
    private AudioRecoderUtils recoder;
    private boolean recoderFlag = false;
    private boolean IsStart = true;

    private RecyclerView mRadioRecyclerView;
    private MusicAdapter radioAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_page_fragment, container, false);
        /*注册RecyclerView*/
        mRadioRecyclerView = (RecyclerView) view.findViewById(R.id.radio_list);
        mRadioRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        recoder = new AudioRecoderUtils(getContext());
        FindViewById(view);
        UpDateUI();
        return view;
    }

    private void UpDateUI() {

        MusicList musicList = MusicList.get(getActivity());
        final List<MusicInfo> musicInfos = musicList.getmRadioInfos();
        radioAdapter = new MusicAdapter(musicInfos, getActivity());
        mRadioRecyclerView.setAdapter(radioAdapter);

    }

    private void FindViewById(View view) {
        startOrPauseRecoder = (ImageView) view.findViewById(R.id.start_pause);
        storeRecoder = (ImageView) view.findViewById(R.id.save_recoder);
        deleteRecoder = (ImageView) view.findViewById(R.id.delete_recoder);
        /*设置控件不可被点击*/
        storeRecoder.setClickable(false);
        deleteRecoder.setClickable(false);
        /*设置点击监听*/
        startOrPauseRecoder.setOnClickListener(this);
        storeRecoder.setOnClickListener(this);
        deleteRecoder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_pause:
                if(recoderFlag==false) {
                    if(IsStart==true){
                        AudioRecoderUtils.InitFile();
                        storeRecoder.setVisibility(View.VISIBLE);//设置可见
                        deleteRecoder.setVisibility(View.VISIBLE);
                        storeRecoder.setClickable(true);
                        deleteRecoder.setClickable(true);
                        recoder.StartRecoder();
                    }else{
                        recoder.ResumeRecoder();
                    }
                    startOrPauseRecoder.setImageResource(R.drawable.ic_pause_music);
                    recoderFlag = true;
                }else{
                    recoder.PauseRecoder();
                    startOrPauseRecoder.setImageResource(R.drawable.ic_play_music);
                    recoderFlag = false;
                }
                break;
            case R.id.save_recoder:
                recoder.saveRecoder();
                startOrPauseRecoder.setImageResource(R.drawable.ic_pause_music);
                IsStart = true;
                recoderFlag = false;
                storeRecoder.setVisibility(View.INVISIBLE);//设置不可见
                deleteRecoder.setVisibility(View.INVISIBLE);
                storeRecoder.setClickable(false);
                deleteRecoder.setClickable(false);
                break;

            case R.id.delete_recoder:
                recoder.deleteRecoder();
                startOrPauseRecoder.setImageResource(R.drawable.ic_pause_music);
                IsStart = true;
                recoderFlag = false;
                storeRecoder.setVisibility(View.INVISIBLE);//设置可见
                deleteRecoder.setVisibility(View.INVISIBLE);
                storeRecoder.setClickable(false);
                deleteRecoder.setClickable(false);
                break;
        }
    }
}
