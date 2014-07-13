package com.lcs.cai;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lcs.cai.dao.AppDao;
import com.lcs.cai.pojo.App;

import java.util.List;


public class MyActivity extends Activity  {
	private AppTool appTool;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		this.appTool = new AppTool(this);

		Log.i("lcs" , this.appTool.getAllApp().toString());
		GridView appView = (GridView) this.findViewById(R.id.appView);
		List<ResolveInfo> appList = (List<ResolveInfo>)this.appTool.getAllApp();

		appView.setAdapter(new MyAdapter( this, appList , getPackageManager() ));
	//	appView.setNumColumns(3);//
		appView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				startActivity((Intent) view.getTag());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class MyAdapter extends  BaseAdapter{

		private List<ResolveInfo> appList ;
		private PackageManager packageManager;
		private Context context;
		private AppDao appDao;
		public  MyAdapter(Context  context ,List<ResolveInfo> appList  , PackageManager packageManager){
			this.appList = appList;
			this.packageManager = packageManager;
			this.context = context;
			this.appDao = new AppDao(context);
		//	this.appDao.onCreate( this.appDao.getWritableDatabase() );
		//	Log.i("abs",this.appDao.get(5).toString());
		}

		@Override
		public int getCount() {
			return this.appList.size();
		}

		@Override
		public Object getItem(int i) {
			return this.appList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ImageView btn = new ImageView(this.context);
			ResolveInfo res = this.appList.get(i);
			btn.setImageDrawable(res.loadIcon(this.packageManager));

			Intent intent = new Intent();
			intent.setComponent(new ComponentName( res.activityInfo.packageName , res.activityInfo.name ));

			App app = new App();
			app.setName( res.activityInfo.name );

		//	this.appDao.insert(app);
			btn.setTag(intent);
			return btn;
		}
	}
}