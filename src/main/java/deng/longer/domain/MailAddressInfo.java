package deng.longer.domain;

import java.io.Serializable;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "mailaddress")
public class MailAddressInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String qq;
	
	private String mailAddress;
	private String qqZone;
	private String nickName;
	private String realName;
	private int spiderCnt=0;
	private int addCnt=0;
	public int getAddCnt() {
		return addCnt;
	}
	public void setAddCnt(int addCnt) {
		this.addCnt = addCnt;
	}
	public int getSpiderCnt() {
		return spiderCnt;
	}
	public void setSpiderCnt(int spiderCnt) {
		this.spiderCnt = spiderCnt;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getQqZone() {
		return qqZone;
	}
	public void setQqZone(String qqZone) {
		this.qqZone = qqZone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
