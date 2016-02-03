package com.dante.knowledge.news.model;

import com.dante.knowledge.news.other.NewsItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yons on 16/1/29.
 */
public class ZhihuItem implements Serializable, NewsItem{

    /**
     * images : ["http://pic1.zhimg.com/aef18b16a9a6dcb445d5c235784c25a8.jpg"]
     * type : 0
     * id : 7813824
     * ga_prefix : 012915
     * title : 运气好的话，说不定 3 万年就把木星挪过来
     */

    private int id;
    private String title;
    private List<String> images;
//    private String image;

//    @Override
//    public String getImage() {
//        return image;
//    }
//
//    @Override
//    public void setImage(String image) {
//        this.image = images.get(0);
//    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getImages() {
        return images;
    }
}