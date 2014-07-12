package com.lcs.cai.pojo;

/**
 * app 应用信息
 * Created by lcs on 7/12/2014.
 */
public class App extends BasePojo {
	/**
	 * 主键
	 */
	private int appId;
	/**
	 * 应用名
	 */
	private String name;
	/**
	 * 包名
	 */
	private String packAgename;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackAgename() {
		return packAgename;
	}

	public void setPackAgename(String packAgename) {
		this.packAgename = packAgename;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private int count;

}
