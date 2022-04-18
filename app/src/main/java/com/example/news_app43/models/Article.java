package com.example.news_app43.models;

import java.io.Serializable;

public class Article implements Serializable {
    private String text;
    private long date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Article(String text, long date) {
        this.text = text;
        this.date = date;
    }
}