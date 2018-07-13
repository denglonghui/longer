package deng.longer.sample;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import deng.longer.dao.PayInfoDao;
import deng.longer.domain.MailInfo;
import deng.longer.domain.PayInfo;
import deng.longer.service.MailService;
import deng.longer.util.CheckUtil;
import deng.longer.util.Constants;
import deng.longer.util.DateUtil;
import us.codecraft.webmagic.Spider;

@Component
public class QuartzService {
	private List<MailInfo> mails;
	@Autowired
	private MailService mailService;
	@Autowired
	private PayInfoDao payInfoDao;
	 @Scheduled(cron = "0 0/1 * * * ?")
	    public void recieveMail(){
		 try {
			mails=mailService.recieveMail();
			for(MailInfo e:mails){
				String time=e.getSendTime();
				String from=e.getFrom();
				if(time.contains(DateUtil.getToday("yyyyMMdd"))&&from.contains(Constants.manageAddress)){
					String content=e.getContent();
					
					if(content.indexOf("#")>0){
						String cmd=content.substring(0, content.indexOf("#"));
						String[] strs=cmd.split("\\,");
						if(CheckUtil.checkEmail(strs[1].trim())){
							PayInfo pay = new PayInfo();
							pay.setAmount(Double.parseDouble(strs[3]));
							pay.setDays(Integer.parseInt(strs[2]));
							pay.setMailAddress(strs[1]);
							pay.setStartDay(DateUtil.getToday("yyyyMMdd"));
							Date d=new Date();
							d.setDate(d.getDate()+pay.getDays());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
							pay.setEndDay(sdf.format(d));
							PayInfo tmp=payInfoDao.queryPayInfo(pay.getMailAddress(), pay.getStartDay());
                              if(tmp==null){
                            	  payInfoDao.addPayInfo(pay);
  								System.out.println("插入一条支付信息");
                              }
								
						}
					}
					
				}
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    }

}
