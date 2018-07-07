package deng.longer.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import deng.longer.dao.NewsDao;
import deng.longer.domain.NewsEntity;
import deng.longer.service.MailService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
@Component
public class MyPipeline implements Pipeline {
	@Autowired
    private NewsDao newsDao;
	@Autowired
	private MailService mailService;
	@Override
	public void process(ResultItems resultItems, Task task) {
		
		String info=resultItems.get("info").toString();
		NewsEntity news=new NewsEntity();
		news.setContent(info);
		news.setMainTopic("鲁信创新");
		news.setSubTopic("行情");
		news.setNewsDate("20180101");
		news.setTitle("基本信息");
		newsDao.addNews(news);
//		mailService.sendHtmlMail(info);
		mailService.sendTemplateMail(resultItems.getAll(), "mail.html");

	}

}
