package com.eagle.smarthome;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity
{
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			ActivtyItems.addActivity(this);
			requestWindowFeature(Window.FEATURE_NO_TITLE);	
		}
		
		@Override
		protected void onDestroy()
		{
			super.onDestroy();
			ActivtyItems.removeActivity(this);
		}
	
}
