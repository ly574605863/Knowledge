package com.dante.knowledge.news.model;

import com.dante.knowledge.news.other.NewsDetail;

import java.util.List;

/**
 * zhihu news detail class
 */
public class ZhihuDetail implements NewsDetail {
    /**
     * body : <div class="main-wrap content-wrap">
     <div class="headline">
     <div class="img-place-holder"></div>
     </div>

     <div class="content-inner">
     <div class="question">
     <h2 class="question-title"></h2>

     <div class="answer">

     <div class="meta">
     <img class="avatar" src="http://pic1.zhimg.com/da8e974dc_is.jpg">
     <span class="author">匿名用户</span>
     </div>

     <div class="content">
     <p>致敬是对某个桥段，某几个镜头，某个造型，某段对话高度复制，属于表达导演对自己偶像的敬仰，一般只有资深影迷才会发现。</p>
     <p>恶搞又被称为戏仿，和致敬有所不同，一般针对某部电影，某个桥段，高度还原后，再稍微改变几个地方，从而和原来的故事或要传达的意思产生微妙的冲突，比如姜文的《一步之遥》开头，就是戏仿教父，教父原片中是抱着只猫，姜文则是抱着只兔子，影迷看到后会哑然失笑，属于导演和影迷之间开的一个玩笑。</p>
     <p>需要注意的是，致敬不一定得是经典，也可以是小众电影，只要导演自己喜欢就行，甚至有的直接在主角的房间里贴一张某电影的海报都算，它不影响观众观影体验，致敬属于导演在电影里夹带的私货，不一定非得和当前的片子有关系。但是戏仿的影片就一定得是知名度极高的经典或者非常热门的电影才行。因为戏仿出来的情节一定和原电影的情节高度相关，如果大家都没看过被戏仿的电影，就会降低本情节所制造出来的效果。</p>
     <p>借鉴和模仿一般会针对大的层面，像是对故事模式，人物之间的关系设置，或者摄影手段之类的模仿，比如你发现一部电影故意将故事分成好几段来进行，那么你也可以拍一个电影把完整故事切分为好几段。又比如，你看到一部用手机模仿纪录片拍出来的恐怖片，你也照猫画虎用同样的方法拍了另一个恐怖片。再比如你看到一个故事里男主角阴阳怪气，又毒舌，却很招观众喜爱，那么你也在自己的创作中，设计一个这样的角色，这都属于借鉴，像《疯狂的石头》就借鉴了《两杆老烟枪》。在这里一定注意，借鉴只能是模式上面相似，故事是不能借鉴的。</p>
     <p>抄袭通常是指对故事的复制，不论是致敬，恶搞还是借鉴，都不能复制故事。</p>
     <p>举个例子，比如一个男的被蜘蛛咬了，有了超能力。你把这个故事改成了一个女的被蜘蛛咬了，有了超能力，那妥妥的是抄袭。但是你如果改成一个男的被蚊子咬了，变成了僵尸或吸血鬼之类的，就不算抄袭，而且你可以在他被蚊子咬的那个镜头中，让男主角穿着蜘蛛侠的行头参加万圣节舞会，这就是致敬；你也可以让男主角被蚊子咬了之后，学蜘蛛侠的样子，抛蜘蛛丝，抛了半天才发现自己不仅没超能力，竟然还一身的病毒，这就是恶搞；又或者这时候又有一个被苍蝇咬了的女的成了你的死对头，这就是对超级英雄模式的模仿；而最后两人不打不相识，蚊子人和苍蝇人竟然相爱了，那就是你的原创。</p>
     </div>
     </div>

     <div class="view-more"><a href="http://www.zhihu.com/question/29964586">查看知乎讨论<span class="js-question-holder"></span></a></div>

     </div>

     </div>
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
