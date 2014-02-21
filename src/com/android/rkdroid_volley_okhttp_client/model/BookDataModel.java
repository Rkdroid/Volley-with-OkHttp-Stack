package com.android.rkdroid_volley_okhttp_client.model;

public class BookDataModel {
	private String link;
	private String title;
	private Integer id;
	
	public Integer getBookId() {
		return id;
	}
	public void setBookId(Integer id) {
		this.id = id;
	}
	public String getBookDetailsUrl() {
		return link;
	}
	public void setBookDetailsUrl(String link) {
		this.link = link;
	}
	public String getBookTitle() {
		return title;
	}
	public void setBookTitle(String title) {
		this.title = title;
	}

}
