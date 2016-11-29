package com.eagle.smarthome.util;

import java.util.List;
import java.util.Vector;

import com.eagle.smarthome.LoginActivity;
import com.eagle.smarthome.MyApplication;
import com.eagle.smarthome.R;
import com.eagle.smarthome.Alarm.AlarmItem;
import com.eagle.smarthome.Alarm.MonitorAlarm;
import com.eagle.smarthome.Device.DeviceAI;
import com.eagle.smarthome.Device.DeviceAO;
import com.eagle.smarthome.Device.DeviceDI;
import com.eagle.smarthome.Device.DeviceDO;
import com.eagle.smarthome.Device.DeviceSI;
import com.eagle.smarthome.Device.DeviceSO;
import com.eagle.smarthome.Device.SmartHomeApps;
import com.eagle.smarthome.task.ScenePlansItem;
import com.eagle.smarthome.task.TaskItem;
import com.eagle.smarthome.util.TaskOneAdapter.ViewHolder;

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

public class MonitorAdapter extends ArrayAdapter<AlarmItem>
{	
	 Context context;
	 Vector<AlarmItem> items;
	 SmartHomes homes;
	 SmartHomeApps InstlledDeviceSystem;

	public MonitorAdapter(Context context, int textViewResourceId, Vector<AlarmItem> items, SmartHomeApps InstlledDeviceSystem,SmartHomes homes)
	{
		super(context, textViewResourceId, items);
		resourceId=textViewResourceId;
		selectItem=-1;
		 this. context=context;
		 this.items=items;
		 this.homes=homes;
		 this.InstlledDeviceSystem= InstlledDeviceSystem;
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
	 AlarmItem item=getItem(position);
	 View view;
	 ViewHolder viewHolder;
	 if (convertView==null)
		 { 
		 	view=LayoutInflater.from(getContext()).inflate(resourceId, null ); 
		 	viewHolder=new ViewHolder();
		 	viewHolder.tvdevice= (TextView)view.findViewById(R.id.tvdevice);
		 	viewHolder.subdevice= (TextView)view.findViewById(R.id.subdevice);
		 	viewHolder.condition= (TextView)view.findViewById(R.id.condition);
		 	viewHolder.para= (TextView)view.findViewById(R.id.para);
		 	viewHolder.task= (TextView)view.findViewById(R.id.task);
		 	view.setTag(viewHolder);
		 }
	 else
	 {
		  view=convertView; 	 
		  viewHolder=(ViewHolder)view.getTag();
	 } 
	 
	 if (view!=null)
	 {
		 if (position== selectItem)
			 view.setBackgroundColor(Color.BLUE);  	
		 else	 if (position %2 ==1)			   
			 view.setBackgroundColor(Color.rgb(4, 4, 4));  
		 else 
			 view.setBackgroundColor(Color.TRANSPARENT);  			 
	 }
	 
	 ShowDeviceInfo(viewHolder,item);
	 
	 return view;
 }
 
 private void ShowDeviceInfo(ViewHolder  viewHolder,AlarmItem item)
 {
	 viewHolder.condition.setText(item.operate);
	 viewHolder.para.setText(item.value);
	 //viewHolder.task.setText(item.taskName);
	 
	 if (item.dv==null)
		 {
		 	item.GetDevice(homes);  //找到具体设备
		 	item.homename=item.FindHomeName(InstlledDeviceSystem);
		 }

	 if (item.dv==null)
		 {
		 viewHolder.tvdevice.setText(item.GetDetailInfo());
		 viewHolder.subdevice.setText("");
		 }
	 else  if (item.dv instanceof DeviceDO)
	  {
		  DeviceDO dv=(DeviceDO)item.dv;		  
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s(%s) (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), dv.dotype.toString() ,item.positiondescription+"-" +item.homename));
		  viewHolder.subdevice.setText(dv.functiondescription);
			 
	  }
	 else  if (item.dv instanceof DeviceDI)
	  {
		  DeviceDI dv=(DeviceDI)item.dv;		  
		  viewHolder.subdevice.setText(dv.functiondescription);
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), item.positiondescription+"-" +item.homename));
	  }
	 else  if (item.dv instanceof DeviceSO)
	  {
		  DeviceSO dv=(DeviceSO)item.dv;		  
		  viewHolder.subdevice.setText(dv.functiondescription);
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s(%s) (%s)",   //900-0-0,DO(TEXT)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), dv.streamtype.toString() ,item.positiondescription+"-" +item.homename));
	  }
	 else  if (item.dv instanceof DeviceSI)
	  {
		  DeviceSI dv=(DeviceSI)item.dv;		  
		  viewHolder.subdevice.setText(dv.functiondescription);
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s(%s) (%s)",   //900-0-0,DO(TEXT)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), dv.streamtype.toString() ,item.positiondescription+"-" +item.homename));

	  }
	 else  if (item.dv instanceof DeviceAO)
	  {
		  DeviceAO dv=(DeviceAO)item.dv;		  
		  viewHolder.subdevice.setText(dv.functiondescription);
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), item.positiondescription+"-" +item.homename));
	  }
	 else  if (item.dv instanceof DeviceAI)
	  {
		  DeviceAI dv=(DeviceAI)item.dv;		  
		  viewHolder.subdevice.setText(dv.functiondescription);
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), item.positiondescription+"-" +item.homename));
	  } 
	  
 }

 
 class ViewHolder
 {
	    TextView tvdevice;
	    TextView subdevice;
	    TextView condition;
	    TextView para;
	    TextView task;
 }
 
}