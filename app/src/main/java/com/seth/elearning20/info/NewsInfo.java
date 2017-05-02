package com.seth.elearning20.info;

import java.util.Date;

/**
 * Created by Seth on 2017/4/25.
 */

public class NewsInfo {
    private String Title;
    private String img;
    private int comments;
    private Date time;
    private String content;

    public NewsInfo(){};

    public NewsInfo(String title, String img, int comments, Date time, String content) {
        Title = title;
        this.img = img;
        this.comments = comments;
        this.time = time;
        this.content = content;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
