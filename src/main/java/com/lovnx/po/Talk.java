package com.lovnx.po;

public class Talk {

    private String title;
    private String name;
    private Integer timeAfter;
    
	public Talk(String title, String name, Integer timeAfter) {
		super();
		this.title = title;
		this.name = name;
		this.timeAfter = timeAfter;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTimeAfter() {
		return timeAfter;
	}
	public void setTimeAfter(Integer timeAfter) {
		this.timeAfter = timeAfter;
	}
    
}
