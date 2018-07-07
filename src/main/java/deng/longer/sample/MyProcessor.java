package deng.longer.sample;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class MyProcessor implements PageProcessor {

	  //// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1);;
    //这里通过page.addTargetRequests()方法来增加要抓取的URL
    // 这里抽取支持链式调用。调用结束后，toString()表示转化为单个String，all()则转化为一个String列表。
    public void process(Page page) {
        //用于获取所有满足这个正则表达式的链接
        List<String> links = page.getHtml().links().regex("https://blog\\.csdn\\.net/*").all();
        //将这些链接加入到待抓取的队列中去
        page.addTargetRequests(links);
            //相同元素的结果加到相应的集合中去。
            List<String>titlelist=page.getHtml().xpath("//span[@class='link_title']/a/text()").all();
            List<String>readlist=page.getHtml().xpath("//span[@class='link_view']/text()").all();
            List<String>pinlunlist=page.getHtml().xpath("//span[@class='link_comments']/text()").all();
            //遍历这些list，输出自己想要的结果。
            for (int i=0;i<titlelist.size();i++){
                if (i==0){
                    System.out.println("###################################################");
                    System.out.println("###################################################");
                }
                System.out.println("题目："+titlelist.get(i));
                System.out.println("阅读人数："+readlist.get(i).replace("(","").replace(")",""));
                System.out.println("评论次数："+pinlunlist.get(i).replace("(","").replace(")",""));
                if (i!=titlelist.size()-1) {
                    System.out.println("*******************我是可爱的分割线*******************\n");

                }
            }

    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new MyProcessor())
                //在这里写上自己博客地址，从这个地址开始抓
                .addUrl("http://blog.csdn.net/h295928126")
                .run();
    }


}
