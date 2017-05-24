package com.seth.elearning20.frontfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seth.elearning20.R;
import com.seth.elearning20.adapter.MusicAdapter;
import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.info.MusicList;

import java.util.List;

/**
 * Created by Seth on 2017/5/22.
 */

public class SecondChildPage1 extends Fragment {
    private RecyclerView mMusicRecyclerView;
    private MusicAdapter musicAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_page_child, container, false);

        mMusicRecyclerView = (RecyclerView) view.findViewById(R.id.music_list);
        mMusicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        MusicList musicList = MusicList.get(getActivity());
        final List<MusicInfo> musicInfos = musicList.getMusicInfos();
        musicAdapter = new MusicAdapter(musicInfos, getActivity(),0);
        mMusicRecyclerView.setAdapter(musicAdapter);

    }
}
