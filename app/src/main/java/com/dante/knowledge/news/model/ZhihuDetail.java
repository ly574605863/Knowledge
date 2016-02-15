package com.dante.knowledge.news.model;

import com.dante.knowledge.news.other.NewsDetail;

import java.util.List;

/**
 * zhihu news detail class
 */
public class ZhihuDetail implements NewsDetail {
    /**
     * body : <div class="main-wrap content-wrap">
     <p>致敬是对某个桥段，某几个镜头，某个造型，某段对话高度复制，属于表达导演对自己偶像的敬仰，一般只有资深影迷才会发现。</p>
     * blah blah
     </div>
     * image_source : 《一步之遥》
     * title : 致敬、恶搞、借鉴、模仿、抄袭，到底怎么区分？
     * image : http://pic1.zhimg.com/930cf6f414db290556cd068235ff8f1c.jpg
     * share_url : http://daily.zhihu.com/story/7815067
     * js : []
     * ga_prefix : 013010
     * type : 0
     * id : 7815067
     * css : ["http://news-at.zhihu.com/css/news_qa.auto.css?v=77778"]
     */

    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private int id;
    private List<?> js;
    private List<String> css;

    public void setBody(String body) {
        this.body = body;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJs(List<?> js) {
        this.js = js;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public String getBody() {
        return body;
    }

    public String getImage_source() {
        return image_source;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getShare_url() {
        return share_url;
    }

    public int getId() {
        return id;
    }

    public List<?> getJs() {
        return js;
    }

    public List<String> getCss() {
        return css;
    }
}
