package com.eagle.smarthome.util;

import java.util.Vector;
import com.eagle.smarthome.R;
import com.eagle.smarthome.Device.*;
import com.eagle.smarthome.task.TaskItem;
import com.eagle.smarthome.util.TaskAdapter.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskOneAdapter extends ArrayAdapter<TaskItem>
{	
	 Context context;
	 Vector<TaskItem> items;
	 SmartHomes homes;
	 SmartHomeApps InstlledDeviceSystem;
	 //String timedtask;	 String notimedtask;
	 
	public TaskOneAdapter(Context context, int textViewResourceId, Vector<TaskItem> items,SmartHomeApps InstlledDeviceSystem,SmartHomes homes)
	{
		super(context, textViewResourceId, items);
		resourceId=textViewResourceId;
		selectItem=-1;
		 this. context=context;// MyApplication.getAppContext();
		 this.items=items;
		 this.homes=homes;
		 this.InstlledDeviceSystem= InstlledDeviceSystem;
		// timedtask =context.getResources().getString(R.string.timedtask);	
		 //notimedtask =context.getResources().getString(R.string.notimedtask);
	}
	private int resourceId;
    private int  selectItem=-1;
    public  void setSelectItem(int selectItem)
    {  
    	this.selectItem = selectItem;  
     }  
  
    public void setList(Vector<TaskItem> items)
    {
    	this.items = items;
    	this.notifyDataSetChanged();
    }
    
 @Override
 public View getView(int position,View convertView,ViewGroup parent)
 {
	 TaskItem item=items.get(position);   // getItem(position);	
	 View view;
	 ViewHolder viewHolder;
	 if (convertView==null)
		 { 
		 	view=LayoutInflater.from(getContext()).inflate(resourceId, null ); 
		 	viewHolder=new ViewHolder();
		 	viewHolder.tvdevice= (TextView)view.findViewById(R.id.tvdevice);
		 	viewHolder.tvoperation= (TextView)view.findViewById(R.id.tvoperation);
		 	viewHolder.dvfunction= (TextView)view.findViewById(R.id.dvfunction);
		 	viewHolder.systemname= (TextView)view.findViewById(R.id.systemname);		 	
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
			 view.setBackgroundColor(Color.rgb(70,92,79));
		 else	 if (position %2 ==1)			   
   		    view.setBackgroundColor(Color.argb(128,4, 40, 4));  
		 else 
			 view.setBackgroundColor(Color.TRANSPARENT);  			 
	 }
	 ShowDeviceInfo(viewHolder,item);
	 return view;
 }
 
 private void ShowDeviceInfo(ViewHolder  viewHolder,TaskItem item)
 {
	 if (item.dv==null)
		 {
		 	item.GetDevice(homes);  //找到具体设备
		 	item.homename=item.FindHomeName(InstlledDeviceSystem);
		 }
	 viewHolder.tvoperation.setText(item.action);	 
	 if (item.dv==null)
		 {
		     viewHolder.dvfunction.setText("");
			 viewHolder.tvdevice.setText(item.GetDetailInfo());
			 viewHolder.systemname.setText("");
		 }
	 else  if (item.dv instanceof DeviceDO)
	  {
		  DeviceDO dv=(DeviceDO)item.dv;		  
	      viewHolder.dvfunction.setText(dv.functiondescription);	//+"("+dv.controldescription+")"); 
	      viewHolder.systemname.setText(item.homename+"("+item.positiondescription+")"); 
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s(%s) (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), dv.dotype.toString() ,item.GetTimestr()));
	  }
	 else  if (item.dv instanceof DeviceDI)
	  {
		  DeviceDI dv=(DeviceDI)item.dv;		  
	      viewHolder.dvfunction.setText(dv.functiondescription);	//+"("+dv.controldescription+")"); 
	      viewHolder.systemname.setText(item.homename+"("+item.positiondescription+")"); 
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), item.GetTimestr() ));
	  }
	 else  if (item.dv instanceof DeviceSO)
	  {
		  DeviceSO dv=(DeviceSO)item.dv;		  
	      viewHolder.dvfunction.setText(dv.functiondescription);	//+"("+dv.controldescription+")"); 	    
	      viewHolder.systemname.setText(item.homename+"("+item.positiondescription+")"); 
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s(%s) (%s)",   //900-0-0,DO(TEXT)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), dv.streamtype.toString() ,item.GetTimestr() ));
	  }
	 else  if (item.dv instanceof DeviceSI)
	  {
		  DeviceSI dv=(DeviceSI)item.dv;		  
	      viewHolder.dvfunction.setText(dv.functiondescription);	//+"("+dv.controldescription+")"); 	     
	      viewHolder.systemname.setText(item.homename+"("+item.positiondescription+")"); 
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s(%s) (%s)",   //900-0-0,DO(TEXT)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), dv.streamtype.toString() ,item.GetTimestr()));
	  }
	 else  if (item.dv instanceof DeviceAO)
	  {
		  DeviceAO dv=(DeviceAO)item.dv;		  
	      viewHolder.dvfunction.setText(dv.functiondescription);	//+"("+dv.controldescription+")"); 
	      viewHolder.systemname.setText(item.homename+"("+item.positiondescription+")"); 
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), item.GetTimestr()));
	  }
	 else  if (item.dv instanceof DeviceAI)
	  {
		  DeviceAI dv=(DeviceAI)item.dv;		  
	      viewHolder.dvfunction.setText(dv.functiondescription);	//+"("+dv.controldescription+")"); 
	      viewHolder.systemname.setText(item.homename+"("+item.positiondescription+")"); 
		  viewHolder.tvdevice.setText(String.format("%d-%d-%d,%s (%s)",   //900-0-0,DO(open)(00:00:01)
				  item.appId, item.devieID, item.subDevieID,item.deviceType.toString(), item.GetTimestr() ));
	  }
	  
 }
 class ViewHolder
	 {
		    TextView tvdevice;
		    TextView tvoperation;
		    TextView dvfunction;
		    TextView systemname;    
	 }
	 
}