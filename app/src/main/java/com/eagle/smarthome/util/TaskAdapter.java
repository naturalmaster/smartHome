package com.eagle.smarthome.util;

import java.util.List;

import com.eagle.smarthome.LoginActivity;
import com.eagle.smarthome.MyApplication;
import com.eagle.smarthome.R;
import com.eagle.smarthome.task.ScenePlansItem;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAdapter extends ArrayAdapter<ScenePlansItem>
{	
	 Context context;
	 String timedtask;
	 String notimedtask;
	 
	public TaskAdapter(Context context, int textViewResourceId, List<ScenePlansItem> objects)
	{
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
		selectItem=-1;
		 this. context=context;// MyApplication.getAppContext();
		 timedtask =context.getResources().getString(R.string.timedtask);	
		 notimedtask =context.getResources().getString(R.string.notimedtask);
	}
	private int resourceId;
    private int  selectItem=-1;
    public  void setSelectItem(int selectItem)
    {  
    	this.selectItem = selectItem;  
     }  
  
 @Override
 public View getView(int position,View convertView,ViewGroup parent)
 {
	 ScenePlansItem chl=getItem(position);
	 View view;
	 ViewHolder viewHolder;
	 if (convertView==null)
		 { 
		 	view=LayoutInflater.from(getContext()).inflate(resourceId, null ); 
		 	viewHolder=new ViewHolder();
		 	viewHolder.UsedTask= (CheckBox)view.findViewById(R.id.UsedTask);
		 	viewHolder.taskname= (TextView)view.findViewById(R.id.taskname);
		 	view.setTag(viewHolder);
		 }
	 else
	 {
		  view=convertView; 	 
		  viewHolder=(ViewHolder)view.getTag();
	 } 
	 
	 /*android 获得当前活动的Acticity
	 ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
	Log.d("", "package:"+componentName .getPackageName());
	Log.d("", "class:"+componentName .getClassName());
	添加权限：android.permission.GET_TASKS*/

	/* Context context= MyApplication.getAppContext();
	 String timedtask =context.getResources().getString(R.string.timedtask);	
	 String notimedtask =context.getResources().getString(R.string.notimedtask);*/
	 
	 viewHolder.UsedTask.setChecked(chl.Used);
	 viewHolder.taskname.setText(String.valueOf(chl.PlanFileName));
	 
	 if (view!=null)
	 {
		 if (position== selectItem)
			 view.setBackgroundColor(Color.rgb(70,92,79));
		 else	 if (position %2 ==1)			
		 {
			 view.setBackgroundColor(Color.argb(128,4, 40, 4));  
			 if (viewHolder.UsedTask.isChecked())
//			    viewHolder.taskname.setTextColor(Color.rgb(240, 240, 4));  //设置字体黄色、、丑
			    viewHolder.taskname.setTextColor(Color.WHITE);  //设置为白色
			 else
				 viewHolder.taskname.setTextColor(Color.rgb(120,120,120));
		 }
		 else 
		 {
			 view.setBackgroundColor(Color.TRANSPARENT);  	
			 if (viewHolder.UsedTask.isChecked())
		         viewHolder.taskname.setTextColor(Color.rgb(240, 240, 240));
			 else 
				 viewHolder.taskname.setTextColor(Color.rgb(120,120,120));
		 }
	 }
	 return view;
 }
 
 class ViewHolder
 {
	    CheckBox UsedTask;
	    TextView taskname;
 }
 
}