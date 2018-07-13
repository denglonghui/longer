package deng.longer.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Security;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
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

import com.sun.mail.pop3.POP3Message;
import com.sun.mail.util.BASE64DecoderStream;

import freemarker.template.Template;

@Component
public class MailService {
	@Autowired
	private JavaMailSender mailSender; // 自动注入的Bean

	@Value("${spring.mail.username}")
	private String sender; // 读取配置文件中的参数
	@Value("${spring.mail.password}")
	private String password;
	@Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;  //自动注入

	public void sendHtmlMail(String content) {
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(sender);
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
	            helper.setFrom(sender);
	         
	            helper.setSubject("主题：模板邮件");
	            String[] to={sender};
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
	 
	 public void recieveMail() throws MessagingException, IOException{
		 String pop3Server = "pop.qq.com";  
	        String protocol = "pop3";  
	        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	        Properties props = new Properties();  
	        props.setProperty("mail.store.protocol", protocol);  
	        props.setProperty("mail.pop3.host", pop3Server);
	        props.setProperty("mail.pop3.port", "995");
	        props.setProperty("mail.pop3.keepmessagecontent", "true");
	        props.setProperty("mail.pop3.startssl.required", "true");
	        props.setProperty("mail.pop3.isSSL", "true"); 
	        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
	        props.setProperty("mail.pop3.socketFactory.fallback", "false");
	        props.setProperty("mail.pop3.port", "995");
	        props.setProperty("mail.pop3.socketFactory.port", "995");
	        props.setProperty("mail.pop3.auth", "true");

	        
	          
	        // 使用Properties对象获得Session对象  
	        Session session = Session.getInstance(props);  
	        session.setDebug(true);  
	       
	        // 利用Session对象获得Store对象，并连接pop3服务器  
	        Store store = session.getStore();  
	        store.connect(pop3Server, sender, password);  
	    
	          
	        // 获得邮箱内的邮件夹Folder对象，以"只读"打开  
	        Folder folder = store.getFolder("inbox");  
	        folder.open(Folder.READ_ONLY);  
	          
	        // 获得邮件夹Folder内的所有邮件Message对象  
	        Message [] messages = folder.getMessages();  
	          
	        int mailCounts = messages.length;  
	        for(int i = 0; i < mailCounts; i++) {  
	              
	            String subject = messages[i].getSubject();  
	            String from = (messages[i].getFrom()[0]).toString();  
	             System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++"); 
	            System.out.println("第 " + (i+1) + "封邮件的主题：" + subject);  
	            System.out.println("第 " + (i+1) + "封邮件的发件人地址：" + from);  
	              
	            System.out.println("是否打开该邮件(yes/no)?：");  
//	            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
//	            String input = br.readLine();  
//	            if("yes".equalsIgnoreCase(input)) {  
	                // 直接输出到控制台中  
	            System.out.println(messages[i].getClass().getName());
	            MimeMessage msg = (MimeMessage) messages[i];  

	            StringBuffer content = new StringBuffer(30);  
	            getMailTextContent(msg, content);  
	            System.out.println("开始打印邮件内容：");
                 System.out.println(content);
                 System.out.println("打印邮件内容结束");
//	               String a=new String( messages[i]);
	              
//	                System.out.println(BASE64DecoderStream.decode(a.getBytes()));
//	            }             
	        }  
	        folder.close(false);  
	        store.close();  
	 }

	 /** 
	     * 获得邮件文本内容 
	     * @param part 邮件体 
	     * @param content 存储邮件文本内容的字符串 
	     * @throws MessagingException 
	     * @throws IOException 
	     */  
	    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {  
	        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断  
	        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;   
	        if (part.isMimeType("text/*") && !isContainTextAttach) {  
	            content.append(part.getContent().toString());  
	        } else if (part.isMimeType("message/rfc822")) {   
	            getMailTextContent((Part)part.getContent(),content);  
	        } else if (part.isMimeType("multipart/*")) {  
	            Multipart multipart = (Multipart) part.getContent();  
	            int partCount = multipart.getCount();  
	            for (int i = 0; i < partCount; i++) {  
	                BodyPart bodyPart = multipart.getBodyPart(i);  
	                getMailTextContent(bodyPart,content);  
	            }  
	        }  
	    }  


}
