package com.example.androidnotes;

import java.io.Serializable;

public class Notes implements Serializable {

    private String title,content,date;
    //private static int count=1;

    public Notes() {
        this.title="";
        this.content="";
        this.date="";

    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }





}
