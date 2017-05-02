package com.seth.elearning20.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seth.elearning20.R;
import com.seth.elearning20.info.NewsInfo;

import java.util.List;

/**
 * Created by Seth on 2017/4/25.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private Context mContext;
    private List<NewsInfo> mNewsInfos;

    public NewsAdapter(List<NewsInfo> newsInfos,Context context){
        mContext = context;
        mNewsInfos = newsInfos;
    }

    @Override
    public NewsAdapter.NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.news_list,parent,false);

        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsHolder holder, int position) {
        NewsInfo newsInfo = mNewsInfos.get(position);
        holder.bindNews(newsInfo);
    }

    @Override
    public int getItemCount() {
        return mNewsInfos.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder{
        private TextView newsTitle;
        private TextView newsComments;
        private TextView newsDate;

        private NewsInfo mNewsInfo;

        public NewsHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsComments = (TextView) itemView.findViewById(R.id.news_comments);
            newsDate = (TextView) itemView.findViewById(R.id.news_time);
        }
        public void bindNews(NewsInfo news){
            mNewsInfo = news;
            newsTitle.setText(mNewsInfo.getTitle());
            newsComments.setText(mNewsInfo.getComments()+"条评论");
            newsDate.setText(mNewsInfo.getTime().toString());

        }
    }
}
