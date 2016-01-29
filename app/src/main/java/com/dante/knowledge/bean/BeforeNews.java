package com.dante.knowledge.bean;

import java.util.List;

/**
 * Created by yons on 16/1/29.
 */
public class BeforeNews {
    /**
     * date : 20160128
     * stories : [{"images":["http://pic3.zhimg.com/0a1f90b819f3dbf604a04d98f16394ae.jpg"],"type":0,"id":7810069,"ga_prefix":"012822","title":"深夜惊奇 · 以霸制霸"},{"title":"20 岁看，几乎什么也不懂；30 岁再看，想哭","ga_prefix":"012821","images":["http://pic4.zhimg.com/f8d20429eb7f646d9f495e4566e53037.jpg"],"multipic":true,"type":0,"id":7809940},{"images":["http://pic2.zhimg.com/365414a36b403004163ef512907285cd.jpg"],"type":0,"id":7808950,"ga_prefix":"012820","title":"没有私人电话的年代里，我曾经自制了一套对讲机"},{"images":["http://pic4.zhimg.com/555697b146ee4c349a3c8bc5b2ff6bbf.jpg"],"type":0,"id":7701363,"ga_prefix":"012819","title":"乱入的「大明咒」、「气功」\u2026\u2026星球大战中还有哪些冷知识？"},{"images":["http://pic4.zhimg.com/34c00cb197518b00f9fbe935c14b99f7.jpg"],"type":0,"id":7810131,"ga_prefix":"012818","title":"读读日报推荐 · 一场游戏半条命"},{"images":["http://pic4.zhimg.com/bf8615593f84319dcf0f93bd45f09e47.jpg"],"type":0,"id":7803692,"ga_prefix":"012818","title":"五年过去了 · 从外人艳羡的圈子里，走出来"},{"images":["http://pic1.zhimg.com/bca1b75a775459bcf0da9d259df67208.jpg"],"type":0,"id":7808597,"ga_prefix":"012817","title":"我想要这张「小鸡啄米图」，一定要去买真迹吗？"},{"images":["http://pic1.zhimg.com/a56bbf4bc744f9b7ad87c99daeded6d4.jpg"],"type":0,"id":7808634,"ga_prefix":"012816","title":"谷歌人工智能第一次秒杀欧洲围棋冠军，是人类打败了人类"},{"images":["http://pic2.zhimg.com/52a8847937391e924cc8318ba1acd801.jpg"],"type":0,"id":7782247,"ga_prefix":"012815","title":"为什么供水系统在低温天气很少瘫痪？"},{"images":["http://pic4.zhimg.com/b653905b06a7e431f0aef6160aef24c3.jpg"],"type":0,"id":7761065,"ga_prefix":"012814","title":"花了 4 天，成功给陷入传销骗局的朋友反洗脑"},{"images":["http://pic4.zhimg.com/1a9bea37d8178cb26500cb04b41c119b.jpg"],"type":0,"id":7808569,"ga_prefix":"012813","title":"睡觉时不能睁着眼，否则大脑也会忙不过来"},{"images":["http://pic4.zhimg.com/d240c05a185a48630b8460504ced1a9b.jpg"],"type":0,"id":7799086,"ga_prefix":"012812","title":"在我的领域里，这一点跟大家的认知不太一样"},{"images":["http://pic2.zhimg.com/9196cb87410aeab706ff6956f727bb85.jpg"],"type":0,"id":7806886,"ga_prefix":"012811","title":"首先你要知道，抗生素并不能分「强弱」"},{"images":["http://pic3.zhimg.com/ec32e382d8f81beb52c5a0c7c1054e12.jpg"],"type":0,"id":7806905,"ga_prefix":"012810","title":"要防范入室盗窃，来点「高科技」的监控设备"},{"title":"说到公司福利，这个公司表示：在座的各位都是\u2026\u2026","ga_prefix":"012809","images":["http://pic3.zhimg.com/8d7e2e967eee02e44324e0cd5f3e9f92.jpg"],"multipic":true,"type":0,"id":7806004},{"images":["http://pic3.zhimg.com/723d8627073b1de136a9671d64da74f2.jpg"],"type":0,"id":7806215,"ga_prefix":"012808","title":"对每款车来说，有没有各自对应的「最省油车速」？"},{"images":["http://pic4.zhimg.com/3148a24e6f3db02928191e2c674dbf0b.jpg"],"type":0,"id":7803870,"ga_prefix":"012807","title":"父母对某些症状羞于开口，但我们不能等出了事再来后悔"},{"images":["http://pic3.zhimg.com/28b8671715d31e3476f9d0e1834abfee.jpg"],"type":0,"id":7806968,"ga_prefix":"012807","title":"六小龄童演的猴确实好，但它未必像孙悟空"},{"images":["http://pic4.zhimg.com/46de39a7c37fcd05193b8cefb32c3f23.jpg"],"type":0,"id":7804505,"ga_prefix":"012807","title":"初恋女友要求我结婚了之后也不能有性生活，怎么办？"},{"images":["http://pic4.zhimg.com/255832d68abb6a72e0e047c32c435317.jpg"],"type":0,"id":7737727,"ga_prefix":"012806","title":"瞎扯 · 如何正确地吐槽"}]
     */

    private String date;
    /**
     * images : ["http://pic3.zhimg.com/0a1f90b819f3dbf604a04d98f16394ae.jpg"]
     * type : 0
     * id : 7810069
     * ga_prefix : 012822
     * title : 深夜惊奇 · 以霸制霸
     */

    private List<StoriesEntity> stories;

    public void setDate(String date) {
        this.date = date;
    }

    public void setStories(List<StoriesEntity> stories) {
        this.stories = stories;
    }

    public String getDate() {
        return date;
    }

    public List<StoriesEntity> getStories() {
        return stories;
    }

}
