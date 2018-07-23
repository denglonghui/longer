package deng.longer.test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import deng.longer.util.MD5;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpTest {
	 @Test
	  public void testPost(){
		 try {
			HttpClient httpClient = new DefaultHttpClient();
			 HttpPost post = null;

			    // 设置超时时间
			    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
			    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
			     String url="http://61.186.157.171:8103/API/EatA/RoomQuery";   
			    post = new HttpPost(url);
			    long timespan=System.currentTimeMillis()/1000;
			    JSONObject reqObj=new JSONObject();
				String usrName="婚庆园";
				String userId="13760429596";
				reqObj.put("KHDA_KHMC", usrName);
			
				reqObj.put("KHDA_SJ", userId);
				reqObj.put("timespan", timespan);
				MD5 md5=new MD5();
				String md5Str=md5.encoderByMd5Chinese(userId+usrName+timespan);
				reqObj.put("Param", md5Str);
			    // 构造消息头
			    post.setHeader("Content-type", "application/json; charset=utf-8");
			    post.setHeader("Connection", "Close");
			    String sessionId = getSessionId();
			    post.setHeader("SessionId", sessionId);
			    post.setHeader("appid", "12345678");
			     System.out.println(reqObj.toString());           
			    // 构建消息实体
			    StringEntity entity = new StringEntity(reqObj.toString(), Charset.forName("UTF-8"));
			    entity.setContentEncoding("UTF-8");
			    // 发送Json格式的数据请求
			    entity.setContentType("application/json");
			    post.setEntity(entity);
			        
			    HttpResponse response = httpClient.execute(post);
			    System.out.println(response.getStatusLine());
			    System.out.println( EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }
	 public static String getSessionId(){
		    UUID uuid = UUID.randomUUID();
		    String str = uuid.toString();
		    return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
		}

}
