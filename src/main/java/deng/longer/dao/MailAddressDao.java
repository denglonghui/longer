package deng.longer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import deng.longer.domain.MailAddressInfo;

@Component
public class MailAddressDao {
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public void addMailAddress(MailAddressInfo mail){
		mongoTemplate.save(mail);
	}
	public List<MailAddressInfo> queryMailAddress(String address){
		Query query=new Query(Criteria.where("mailAddress").is(address));
		return mongoTemplate.find(query, MailAddressInfo.class);
	}
}
