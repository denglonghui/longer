package deng.longer.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import deng.longer.domain.MailInfo;
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
	 
	 public List<MailInfo> recieveMail() throws MessagingException, IOException{
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
//	        session.setDebug(true);  
	       
	        // 利用Session对象获得Store对象，并连接pop3服务器  
	        Store store = session.getStore();  
	        store.connect(pop3Server, sender, password);  
	    
	          
	        // 获得邮箱内的邮件夹Folder对象，以"只读"打开  
	        Folder folder = store.getFolder("inbox");  
	        folder.open(Folder.READ_ONLY);  
	          
	        // 获得邮件夹Folder内的所有邮件Message对象  
	        Message [] messages = folder.getMessages();  
	          
	        int mailCounts = messages.length;  
	        List<MailInfo> list=new ArrayList<MailInfo>();
	        for(int i = 0; i < mailCounts; i++) {  
	              MailInfo mail=new MailInfo();
	        	MimeMessage msg = (MimeMessage) messages[i];
	           String from=getFrom(msg);
	           String sub=getSubject(msg);
	           String sendTime=getSentDate(msg, "yyyyMMddHHmmss");
	           String recAddr=getReceiveAddress(msg, null);
	             System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++"); 
	            System.out.println("第 " + (i+1) + "封邮件的主题：" + sub);  
	            System.out.println("第 " + (i+1) + "封邮件的发件人地址：" +from );
	            System.out.println("第 " + (i+1) + "封邮件的发件时间：" + sendTime); 
	            System.out.println("第 " + (i+1) + "封邮件的收件地址：" + recAddr); 
	            StringBuffer content = new StringBuffer(30);  
	            getMailTextContent(msg, content);  
	            System.out.println("开始打印邮件内容：");
                 System.out.println(content);
                 System.out.println("打印邮件内容结束");
                 mail.setContent(content.toString());
                 mail.setFrom(from);
                 mail.setSendTime(sendTime);
                 mail.setSubject(sub);
                 mail.setTo(recAddr);
                 list.add(mail);
                 
	        }  
	        folder.close(true);  
	        store.close();  
	        return list;
	 }

	 /** 
	     * 获得邮件文本内容 
	     * @param part 邮件体 
	     * @param content 存储邮件文本内容的字符串 
	     * @throws MessagingException 
	     * @throws IOException 
	     */  
	    private  void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {  
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

	    /** 
	     * 获得邮件主题 
	     * @param msg 邮件内容 
	     * @return 解码后的邮件主题 
	     */  
	    private  String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {  
	        return MimeUtility.decodeText(msg.getSubject());  
	    }  
	      
	    /** 
	     * 获得邮件发件人 
	     * @param msg 邮件内容 
	     * @return 姓名 <Email地址> 
	     * @throws MessagingException 
	     * @throws UnsupportedEncodingException  
	     */  
	    private String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {  
	        String from = "";  
	        Address[] froms = msg.getFrom();  
	        if (froms.length < 1)  
	            throw new MessagingException("没有发件人!");  
	          
	        InternetAddress address = (InternetAddress) froms[0];  
	        String person = address.getPersonal();  
	        if (person != null) {  
	            person = MimeUtility.decodeText(person) + " ";  
	        } else {  
	            person = "";  
	        }  
	        from = person + "<" + address.getAddress() + ">";  
	          
	        return from;  
	    }  
	      
	    /** 
	     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人 
	     * <p>Message.RecipientType.TO  收件人</p> 
	     * <p>Message.RecipientType.CC  抄送</p> 
	     * <p>Message.RecipientType.BCC 密送</p> 
	     * @param msg 邮件内容 
	     * @param type 收件人类型 
	     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ... 
	     * @throws MessagingException 
	     */  
	    private String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {  
	        StringBuffer receiveAddress = new StringBuffer();  
	        Address[] addresss = null;  
	        if (type == null) {  
	            addresss = msg.getAllRecipients();  
	        } else {  
	            addresss = msg.getRecipients(type);  
	        }  
	          
	        if (addresss == null || addresss.length < 1)  
	            throw new MessagingException("没有收件人!");  
	        for (Address address : addresss) {  
	            InternetAddress internetAddress = (InternetAddress)address;  
	            receiveAddress.append(internetAddress.toUnicodeString()).append(",");  
	        }  
	          
	        receiveAddress.deleteCharAt(receiveAddress.length()-1); //删除最后一个逗号  
	          
	        return receiveAddress.toString();  
	    }  
	      
	    /** 
	     * 获得邮件发送时间 
	     * @param msg 邮件内容 
	     * @return yyyy年mm月dd日 星期X HH:mm 
	     * @throws MessagingException 
	     */  
	    private String getSentDate(MimeMessage msg, String pattern) throws MessagingException {  
	        Date receivedDate = msg.getSentDate();  
	        if (receivedDate == null)  
	            return "";  
	          
	        if (pattern == null || "".equals(pattern))  
	            pattern = "yyyy年MM月dd日 E HH:mm ";  
	          
	        return new SimpleDateFormat(pattern).format(receivedDate);  
	    }  
	      
	    /** 
	     * 判断邮件中是否包含附件 
	     * @param msg 邮件内容 
	     * @return 邮件中存在附件返回true，不存在返回false 
	     * @throws MessagingException 
	     * @throws IOException 
	     */  
	    private boolean isContainAttachment(Part part) throws MessagingException, IOException {  
	        boolean flag = false;  
	        if (part.isMimeType("multipart/*")) {  
	            MimeMultipart multipart = (MimeMultipart) part.getContent();  
	            int partCount = multipart.getCount();  
	            for (int i = 0; i < partCount; i++) {  
	                BodyPart bodyPart = multipart.getBodyPart(i);  
	                String disp = bodyPart.getDisposition();  
	                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {  
	                    flag = true;  
	                } else if (bodyPart.isMimeType("multipart/*")) {  
	                    flag = isContainAttachment(bodyPart);  
	                } else {  
	                    String contentType = bodyPart.getContentType();  
	                    if (contentType.indexOf("application") != -1) {  
	                        flag = true;  
	                    }    
	                      
	                    if (contentType.indexOf("name") != -1) {  
	                        flag = true;  
	                    }   
	                }  
	                  
	                if (flag) break;  
	            }  
	        } else if (part.isMimeType("message/rfc822")) {  
	            flag = isContainAttachment((Part)part.getContent());  
	        }  
	        return flag;  
	    }  
	      
	    /**  
	     * 判断邮件是否已读  
	     * @param msg 邮件内容  
	     * @return 如果邮件已读返回true,否则返回false  
	     * @throws MessagingException   
	     */  
	    private boolean isSeen(MimeMessage msg) throws MessagingException {  
	        return msg.getFlags().contains(Flags.Flag.SEEN);  
	    }  
	      
	    /** 
	     * 判断邮件是否需要阅读回执 
	     * @param msg 邮件内容 
	     * @return 需要回执返回true,否则返回false 
	     * @throws MessagingException 
	     */  
	    private boolean isReplySign(MimeMessage msg) throws MessagingException {  
	        boolean replySign = false;  
	        String[] headers = msg.getHeader("Disposition-Notification-To");  
	        if (headers != null)  
	            replySign = true;  
	        return replySign;  
	    }  
	      
	    /** 
	     * 获得邮件的优先级 
	     * @param msg 邮件内容 
	     * @return 1(High):紧急  3:普通(Normal)  5:低(Low) 
	     * @throws MessagingException  
	     */  
	    private String getPriority(MimeMessage msg) throws MessagingException {  
	        String priority = "普通";  
	        String[] headers = msg.getHeader("X-Priority");  
	        if (headers != null) {  
	            String headerPriority = headers[0];  
	            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)  
	                priority = "紧急";  
	            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)  
	                priority = "低";  
	            else  
	                priority = "普通";  
	        }  
	        return priority;  
	    }   
	      
	    
	      
	    /**  
	     * 保存附件  
	     * @param part 邮件中多个组合体中的其中一个组合体  
	     * @param destDir  附件保存目录  
	     * @throws UnsupportedEncodingException  
	     * @throws MessagingException  
	     * @throws FileNotFoundException  
	     * @throws IOException  
	     */  
	    private  void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,  
	            FileNotFoundException, IOException {  
	        if (part.isMimeType("multipart/*")) {  
	            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件  
	            //复杂体邮件包含多个邮件体  
	            int partCount = multipart.getCount();  
	            for (int i = 0; i < partCount; i++) {  
	                //获得复杂体邮件中其中一个邮件体  
	                BodyPart bodyPart = multipart.getBodyPart(i);  
	                //某一个邮件体也有可能是由多个邮件体组成的复杂体  
	                String disp = bodyPart.getDisposition();  
	                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {  
	                    InputStream is = bodyPart.getInputStream();  
	                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));  
	                } else if (bodyPart.isMimeType("multipart/*")) {  
	                    saveAttachment(bodyPart,destDir);  
	                } else {  
	                    String contentType = bodyPart.getContentType();  
	                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {  
	                        saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));  
	                    }  
	                }  
	            }  
	        } else if (part.isMimeType("message/rfc822")) {  
	            saveAttachment((Part) part.getContent(),destDir);  
	        }  
	    }  
	      
	    /**  
	     * 读取输入流中的数据保存至指定目录  
	     * @param is 输入流  
	     * @param fileName 文件名  
	     * @param destDir 文件存储目录  
	     * @throws FileNotFoundException  
	     * @throws IOException  
	     */  
	    private static void saveFile(InputStream is, String destDir, String fileName)  
	            throws FileNotFoundException, IOException {  
	        BufferedInputStream bis = new BufferedInputStream(is);  
	        BufferedOutputStream bos = new BufferedOutputStream(  
	                new FileOutputStream(new File(destDir + fileName)));  
	        int len = -1;  
	        while ((len = bis.read()) != -1) {  
	            bos.write(len);  
	            bos.flush();  
	        }  
	        bos.close();  
	        bis.close();  
	    }  
	      
	    /** 
	     * 文本解码 
	     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本 
	     * @return 解码后的文本 
	     * @throws UnsupportedEncodingException 
	     */  
	    private  String decodeText(String encodeText) throws UnsupportedEncodingException {  
	        if (encodeText == null || "".equals(encodeText)) {  
	            return "";  
	        } else {  
	            return MimeUtility.decodeText(encodeText);  
	        }  
	    }


}
