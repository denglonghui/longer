package deng.longer.domain;

import java.io.Serializable;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "payInfo")
public class PayInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mailAddress;
	private double amount;
	private int days;
	private String startDay;
	private String endDay;
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
