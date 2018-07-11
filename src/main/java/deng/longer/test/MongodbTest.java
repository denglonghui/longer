package deng.longer.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import deng.longer.dao.MailAddressDao;
import deng.longer.dao.NewsDao;
import deng.longer.domain.MailAddressInfo;
import deng.longer.domain.NewsEntity;

@RunWith(SpringRunner.class)
@SpringBootTest

public class MongodbTest {
	@Autowired
    private NewsDao newsDao;
	@Autowired
	private MailAddressDao mailDao;

//    @Test
    public void testSaveUser() throws Exception {
        NewsEntity news=new NewsEntity();
        news.setNewsDate("20180101");
        news.setContent("adsfadsf");
        news.setMainTopic("邓龙辉");
        news.setSubTopic("地方");
        news.setTitle("stod");
        newsDao.addNews(news);
    }
    @Test
    public void testSaveMail(){
    	MailAddressInfo mail=new MailAddressInfo();
    	mail.setMailAddress("313906686@qq.com");
    	mail.setNickName("Sunny");
    	mail.setQq("313906686");
    	mail.setQqZone("https://user.qzone.qq.com/313906686");
    	mail.setRealName("");
    	mail.setSpiderCnt(0);
    	mail.setAddCnt(0);
    	mailDao.addMailAddress(mail);
    }

}
