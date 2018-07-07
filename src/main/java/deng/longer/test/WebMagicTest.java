package deng.longer.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import deng.longer.sample.MyPipeline;
import deng.longer.sample.MyProcessor;
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebMagicTest {
	@Autowired
	private MyPipeline p;
	@Test
    public void testWebMagic() throws Exception {
		 Spider.create(new MyProcessor())
         //在这里写上自己博客地址，从这个地址开始抓
         .addUrl("https://xueqiu.com/S/SH600783")
         .addPipeline(p)
         .run();
	}
}
