package com.dante.knowledge.news.model;

import java.io.Serializable;

/**
 * Zhihu news item in top banner
 */
public class ZhihuTop implements Serializable{
    private String image;
    private int id;
    private String title;

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
