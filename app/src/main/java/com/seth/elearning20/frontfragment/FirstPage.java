package com.seth.elearning20.frontfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.seth.elearning20.R;
import com.seth.elearning20.adapter.NewsAdapter;
import com.seth.elearning20.info.NewsInfo;
import com.seth.elearning20.info.NewsList;
import com.seth.elearning20.info.UserInfo;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seth on 2017/4/24.
 */

public class FirstPage extends Fragment {
    private static String TAG = "fragmentTest";
    private ViewPager advPager;
    private RecyclerView mNewsRecyclerView;
    private NewsAdapter mAdapter;
    private Banner banner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(TAG,"创建布局");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.first_page_fragment, container, false);
        //获取RecyclerView控件
        mNewsRecyclerView = (RecyclerView)view.findViewById(R.id.news_list_recyclerView);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        banner = (Banner) view.findViewById(R.id.adv_pager);
        //Log.i(TAG,"界面加载");
        UpdateAdpage();

        UpdateUI();
        return view;

    }

    private void UpdateAdpage() {

        //加载图片资源
        Integer[] images={R.mipmap.ow1,R.mipmap.ow2,R.mipmap.ow3,R.mipmap.ow4};
        List<Integer> img  = new ArrayList<>();
        for(Integer s : images){
            img.add(s);
        }
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集和
        banner.setImages(img);
        //自动轮播
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void UpdateUI() {
        NewsList newsList = NewsList.get();
        final  List<NewsInfo> newsInfos = newsList.getNewsList();

        mAdapter = new NewsAdapter(newsInfos, getActivity());
        mNewsRecyclerView.setAdapter(mAdapter);


    }
}
