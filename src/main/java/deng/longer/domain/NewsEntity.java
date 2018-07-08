package deng.longer.domain;

import java.io.Serializable;

import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "news")

public class NewsEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String newsDate;
	private String mainTopic;
	private String subTopic;
	private String from;
	private String title;
	private String content;
	public String getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}
	public String getMainTopic() {
		return mainTopic;
	}
	public void setMainTopic(String mainTopic) {
		this.mainTopic = mainTopic;
	}
	public String getSubTopic() {
		return subTopic;
	}
	public void setSubTopic(String subTopic) {
		this.subTopic = subTopic;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	

}
