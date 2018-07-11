package deng.longer.sample;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class MailAddressProcessor implements PageProcessor {

	@Override
	public void process(Page page) {
		System.out.println(page.getHtml().toString());
		
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return null;
	}
	 public static void main(String[] args) {
	        Spider.create(new MailAddressProcessor())
	                //在这里写上自己博客地址，从这个地址开始抓
	                .addUrl("https://user.qzone.qq.com/313906686")
	                .addPipeline(new ConsolePipeline())
	                .run();
	    }



}
