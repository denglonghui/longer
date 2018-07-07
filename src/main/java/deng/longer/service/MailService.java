package deng.longer.service;

import java.io.File;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

@Component
public class MailService {
	@Autowired
	private JavaMailSender mailSender; // 自动注入的Bean

	@Value("${spring.mail.username}")
	private String Sender; // 读取配置文件中的参数
	@Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;  //自动注入

	public void sendHtmlMail(String content) {
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(Sender);
			helper.setTo(Sender);
			helper.setSubject("标题：发送Html内容");

			StringBuffer sb = new StringBuffer();
			sb.append(content);
			helper.setText(sb.toString(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
	 public void sendTemplateMail(Map model,String htmlFile){
	        MimeMessage message = null;
	        try {
	        	System.out.println("开始发送邮件：");
	            message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(Sender);
	         
	            helper.setSubject("主题：模板邮件");
	            String[] to={Sender};
	            helper.setTo(to);
//	            String[] 
//	            helper.setTo({"deng_longhui@sohu.com",""});
//	            helper.addTo(Sender);
	            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(htmlFile);
	            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	            helper.setText(html, true);
//	            FileSystemResource file = new FileSystemResource(new File("src/main/resources/pic.jpg"));
//	            helper.addInline("picture",file);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        mailSender.send(message);
	    }

}
