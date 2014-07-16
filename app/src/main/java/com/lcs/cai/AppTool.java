package com.lcs.cai;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by lcs on 7/10/2014.
 */
public class AppTool {
	private Activity activity;
	public AppTool(Activity activity){
		this.activity = activity;
	}

	public List<?> getAllApp(){
		PackageManager packageManager = this.activity.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN , null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, 0);
		//排序
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(packageManager));

		return apps.subList(0,apps.size()/2);
	}
}
