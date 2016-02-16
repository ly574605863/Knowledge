package com.dante.knowledge.net;

/**
 * Net request APIs and types
 */
public class API {
    /**
     * request type, latest stands for getting newly information
     */
    public static final int TYPE_LATEST = 0;
    public static final int TYPE_BEFORE = 1;
    /**
     * request tag, added to cancel that request conveniently (to avoid bugs).
     */
    public static final Object TAG_ZHIHU = "zhihu";
    public static final String TAG_ZHIHU_LATEST = "zhihu_latest";
    public static final String TAG_ZHIHU_BEFORE = "zhihu_before";
    public static final String TAG_ZHIHU_DETAIL = "zhihu_detail";
    public static final String TAG_SPLASH = "splash";
    public static final String TAG_FRESH = "fresh";
    public static final String TAG_FRESH_LATEST = "fresh_latest";
    public static final String TAG_FRESH_DETAIL = "fresh_before";
    /**
     * ZhiHu API
     */
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/news/";
    public static final String NEWS_LATEST = "http://news-at.zhihu.com/api/4/news/latest";
    public static final String NEWS_BEFORE = "http://news-at.zhihu.com/api/4/news/before/";
    public static final String SPLASH = "http://news-at.zhihu.com/api/4/start-image/1080*1920";

    /**
     * Fresh things API
     */
    public static final String FRESH_NEWS = "http://jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&custom_fields=thumb_c,views&dev=1&page=";
    public static final String FRESH_NEWS_DETAIL = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_post&include=content&id=";

}
