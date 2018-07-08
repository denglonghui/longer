package deng.longer.test;

import java.io.File;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("sohu")
public class EmailTest {
	 @Autowired
	    private JavaMailSender mailSender; //自动注入的Bean

	    @Value("${spring.mail.username}")
	    private String Sender; //读取配置文件中的参数
	    @Autowired
	    private FreeMarkerConfigurer freeMarkerConfigurer;  //自动注入


	 //  @Test
	    public void sendSimpleMail() throws Exception {
	        SimpleMailMessage message = new SimpleMailMessage();
	        System.out.println(Sender);
	        message.setFrom(Sender);
	        message.setTo("515604205@qq.com"); //自己给自己发送邮件
	        message.setSubject("主题：简单邮件");
	        message.setText("测试邮件内容");
	       
	        mailSender.send(message);
	    }
	  // @Test
	    public void sendHtmlMail() {
	        MimeMessage message = null;
	        try {
	            message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(Sender);
	            helper.setTo(Sender);
	            helper.setSubject("标题：发送Html内容");

	            StringBuffer sb = new StringBuffer();
	            sb.append("<h1>大标题-h1</h1>")
	                    .append("<p style='color:#F00'>红色字</p>")
	                    .append("<p style='text-align:right'>右对齐</p>");
	            helper.setText(sb.toString(), true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        mailSender.send(message);
	    }
	 //  @Test
	    public void sendAttachmentsMail() {
	        MimeMessage message = null;
	        try {
	            message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(Sender);
	            helper.setTo(Sender);
	            helper.setSubject("主题：带附件的邮件");
	            helper.setText("带附件的邮件内容");
	            //注意项目路径问题，自动补用项目路径
	            FileSystemResource file = new FileSystemResource(new File("src/main/resources/help.txt"));
	            //加入邮件
	            helper.addAttachment("help.txt", file);
	        } catch (Exception e){
	            e.printStackTrace();
	        }
	        mailSender.send(message);
	    }
	//   @Test
	    public void sendInlineMail() {
	        MimeMessage message = null;
	        try {
	            message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(Sender);
	            helper.setTo(Sender);
	            helper.setSubject("主题：带静态资源的邮件");
	            //第二个参数指定发送的是HTML格式,同时cid:是固定的写法
	            helper.setText("<html><body>带静态资源的邮件内容 图片:<img src='cid:picture' /></body></html>", true);

	            FileSystemResource file = new FileSystemResource(new File("src/main/resources/pic.jpg"));
	            helper.addInline("picture",file);
	        } catch (Exception e){
	            e.printStackTrace();
	        }
	        mailSender.send(message);
	    }
	   
	   @Test
	    public void sendTemplateMail(){
	        MimeMessage message = null;
	        try {
	            message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(Sender);
	            helper.setTo("515604205@qq.com");
	            helper.setSubject("主题：模板邮件");

	            Map<String, Object> model = new HashedMap();
	            model.put("username", "zggdczfr");
	            model.put("info", "zggdczfr");

	            //修改 application.properties 文件中的读取路径
//	            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//	            configurer.setTemplateLoaderPath("classpath:resources/templates");
	            //读取 html 模板
	            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mail.html");
	            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	            helper.setText(html, true);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        mailSender.send(message);
	    }

	public static void main(String[] args) {
		

	}

}
