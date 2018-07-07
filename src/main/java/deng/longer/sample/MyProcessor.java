package deng.longer.sample;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class MyProcessor implements PageProcessor {

	  //// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1);;
    //这里通过page.addTargetRequests()方法来增加要抓取的URL
    // 这里抽取支持链式调用。调用结束后，toString()表示转化为单个String，all()则转化为一个String列表。
    public void process(Page page) {
//        System.out.println(page.getHtml());
        String info = page.getHtml().$("div.stock-name").toString()+page.getHtml().$("div.quote-container").toString();
        
        	page.putField("info",info);
       
      
       
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new MyProcessor())
                //在这里写上自己博客地址，从这个地址开始抓
                .addUrl("https://xueqiu.com/S/SH600783")
                .addPipeline(new ConsolePipeline())
                .run();
    }


}
