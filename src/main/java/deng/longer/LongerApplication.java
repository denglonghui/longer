package deng.longer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import us.codecraft.webmagic.Spider;
import deng.longer.sample.MyProcessor;

@EnableScheduling
@SpringBootApplication
public class LongerApplication {

	public static void main(String[] args) {
		System.out.println("hello world");
		SpringApplication.run(LongerApplication.class, args);
		 
	}
}
