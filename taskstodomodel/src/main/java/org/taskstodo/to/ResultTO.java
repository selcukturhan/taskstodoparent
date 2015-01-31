package org.taskstodo.to;

public class ResultTO {
	private String url;
	private String value;
	
	public ResultTO(String value, String url) {
		this.url = url;
		this.value = value;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
