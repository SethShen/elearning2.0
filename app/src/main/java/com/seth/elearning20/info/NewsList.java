package com.seth.elearning20.info;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Seth on 2017/4/25.
 */

public class NewsList {
    private static NewsList sNewsList;
    private List<NewsInfo> mNewsList;

    public static NewsList get(){
        if(sNewsList == null){
            sNewsList = new NewsList();
        }
        return sNewsList;
    }

    public NewsList(){
        mNewsList = new ArrayList<>();

        for(int i=0; i<100; ++i){
            NewsInfo newsInfo = new NewsInfo(
                    new String("第"+i+"条新闻"),
                    null,
                    i,
                    new Date(),
                    null
            );
            mNewsList.add(newsInfo);
        }
    }

    public List<NewsInfo> getNewsList(){
        return mNewsList;
    }
}
