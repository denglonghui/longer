package deng.longer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import us.codecraft.webmagic.Spider;
import deng.longer.sample.MyProcessor;

@SpringBootApplication
public class LongerApplication {

	public static void main(String[] args) {
		System.out.println("hello world");
		SpringApplication.run(LongerApplication.class, args);
		  long startTime, endTime;
	        System.out.println("开始爬取...");
	        startTime = System.currentTimeMillis();
	        Spider.create(new MyProcessor()).addUrl("https://www.cnblogs.com/").thread(5).run();
	        endTime = System.currentTimeMillis();
	        System.out.println("爬取结束，耗时约" + ((endTime - startTime) / 1000) + "秒，抓取了"+1+"条记录");
	}
}
