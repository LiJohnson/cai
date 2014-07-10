package com.lcs.cai;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {
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
		appView.setNumColumns(3);//
	//	appView.setOnItemClickListener(this);
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
		public  MyAdapter(Context  context ,List<ResolveInfo> appList  , PackageManager packageManager){
			this.appList = appList;
			this.packageManager = packageManager;
			this.context = context;
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
			ImageButton btn = new ImageButton(this.context);
			ResolveInfo res = this.appList.get(i);
			btn.setImageDrawable(res.loadIcon( this.packageManager ));
			return btn;
		}
	}
}