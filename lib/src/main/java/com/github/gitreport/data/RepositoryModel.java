package com.github.gitreport.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TreeSet;

public class RepositoryModel implements Serializable{

	private static final long serialVersionUID = 7347739831464854124L;
	private String title;
	private String url;
	private String folder;
	private TreeSet<String> branchs;
	private String reportTarget;
	private Calendar date;
	
	public String getTitle() {
		return title;
	}
	
	@Override 
	public boolean equals(Object o){
		return o!=null &&(o instanceof RepositoryModel)&&
			(((RepositoryModel)o).getFolder().equals(this.getFolder()));
	}
	public Calendar getDate() {
		if (date==null) {
			date = Calendar.getInstance();
		}
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public TreeSet<String> getBranchs() {
		if (branchs==null) {
			branchs = new TreeSet<String>();
		}
		return branchs;
	}
	public void setBranchs(TreeSet<String> branchs) {
		this.branchs = branchs;
	}
	public String getReportTarget() {
		return reportTarget;
	}
	public void setReportTarget(String reportTarget) {
		this.reportTarget = reportTarget;
	}
	
}
