package com.lcs.cai.dao;

import android.content.Context;

import com.lcs.cai.pojo.App;
import com.lcs.cai.pojo.Shit;

/**
 * Created by lcs on 7/12/2014.
 */
public class AppDao extends BaseDao<App> {
	public AppDao( Context context){
		super(App.class , context);
	}

}
