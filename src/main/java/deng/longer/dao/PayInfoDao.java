package deng.longer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import deng.longer.domain.MailAddressInfo;
import deng.longer.domain.PayInfo;

@Component
public class PayInfoDao {
	@Autowired
    private MongoTemplate mongoTemplate;

	public void addPayInfo(PayInfo pay){
		mongoTemplate.save(pay);
	}
	
	public PayInfo queryPayInfo(String address,String startDay){
		Query query=new Query(Criteria.where("mailAddress").is(address).and("startDay").is(startDay));
		return mongoTemplate.findOne(query, PayInfo.class);
	}
}
