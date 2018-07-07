package deng.longer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import deng.longer.domain.NewsEntity;

@Component
public class NewsDaoImpl implements NewsDao{
	@Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void addNews(NewsEntity news) {
		// TODO Auto-generated method stub
		mongoTemplate.save(news);
	}
	
		

}
