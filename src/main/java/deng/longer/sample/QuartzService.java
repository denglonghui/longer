package deng.longer.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Spider;

@Component
public class QuartzService {
	@Autowired
	private MyPipeline p;
	 @Scheduled(cron = "0 0/1 * * * ?")
	    public void timerToNow(){
		 Spider.create(new MyProcessor())
         //在这里写上自己博客地址，从这个地址开始抓
         .addUrl("https://xueqiu.com/S/SH600783")
         .addPipeline(p)
         .run();

	    }

}
