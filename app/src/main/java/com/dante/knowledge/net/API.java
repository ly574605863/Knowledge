package com.dante.knowledge.net;

/**
 * Created by yons on 16/1/29.
 */
public class API {
    /**
     * ZhiHu API
     */
    public static final String BASE_URL="http://news-at.zhihu.com/api/4/news/";
    public static final String TAG_LATEST="latest";
    public static final String TAG_BEFORE="before";

    public static final int TYPE_LATEST= 0;
    public static final int TYPE_BEFORE= 1;

    public static final String NEWS_LATEST="http://news-at.zhihu.com/api/4/news/latest";
    public static final String NEWS_BEFORE="http://news-at.zhihu.com/api/4/news/before/";

    public static final String SPLASH="http://news-at.zhihu.com/api/4/start-image/1080*1776";

    /**
     * Fresh things API
     */
    public static final String FRESH_NEWS = "http://jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&custom_fields=thumb_c,views&dev=1&page=";
    public static final String FRESH_NEWS_DETAIL = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_post&include=content&id=";

}
