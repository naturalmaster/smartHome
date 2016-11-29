package com.eagle.smarthome;

import java.util.List;
import java.util.Vector;

import com.eagle.smarthome.Device.DeviceAO;
import com.eagle.smarthome.Device.DeviceDO;
import com.eagle.smarthome.Device.DeviceSI;
import com.eagle.smarthome.Device.DeviceSO;
import com.eagle.smarthome.Device.DeviceType;
import com.eagle.smarthome.Device.HomeDevice;
import com.eagle.smarthome.Device.SmartHome;
import com.eagle.smarthome.Device.SmartHomeApps;
import com.eagle.smarthome.DeviceControlActivity.myOnItemSelectedListener;
import com.eagle.smarthome.task.EditTaskItem;
import com.eagle.smarthome.task.ScenePlans;
import com.eagle.smarthome.task.ScenePlansItem;
import com.eagle.smarthome.task.SceneTask;
import com.eagle.smarthome.task.TaskItem;
import com.eagle.smarthome.util.DeviceSystemAdapter;
import com.eagle.smarthome.util.FileManager;
import com.eagle.smarthome.util.HomeLocation;
import com.eagle.smarthome.util.SHProtocol;
import com.eagle.smarthome.util.SmartHomeChannel;
import com.eagle.smarthome.util.SmartHomes;
import com.eagle.smarthome.util.TaskAdapter;
import com.eagle.smarthome.util.TaskOneAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class TaskItemModifyActivity extends BaseActivity
{	
	private String workdir="";
	private ImageView imgBack2;	
	private ImageView imgOK;	
	private TextView menditemtext;
	private TextView deviceoperation;
	private EditText oppara; 
	private EditText opdelay; 
	
	private Spinner cbappid;
	private Spinner devicename;
	private Spinner subdevicename;	
	
	
	private void GetUIName()
	{
		try
		{
			imgBack2=(ImageView) findViewById(R.id.imgBack2);	 
			imgOK=(ImageView) findViewById(R.id.imgOK);	 			

			deviceoperation=(TextView)findViewById(R.id.deviceoperation);	 
			
			 oppara =(EditText)findViewById(R.id.oppara);	 
			 opdelay=(EditText)findViewById(R.id.opdelay);	 
					
			cbappid=(Spinner)findViewById(R.id.cbappid);			
			devicename=(Spinner)findViewById(R.id.devicename);			
			subdevicename=(Spinner)findViewById(R.id.subdevicename);					
			menditemtext=(TextView)findViewById(R.id.menditemtext);		
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(TaskItemModifyActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}
	private void SetButtonClickEvent()  //设置按钮响应事件
	{
		try
		{
			SetbimgBackClick();	
			SetbimgOKClick();		
			SetcbappidOnSelect();  				//选择设备系统
			SetdevicesystemOnSelect();  		//选择家居设备
			SetdeviceOnSelect(); 					//选择家居设备子设备
			
			SetdeviceTouchListener();  // 设置触摸事件监听  
			SetdevicesystemTouchListener();  // 设置触摸事件监听  
			SetcbappidTouchListener();  // 设置触摸事件监听  
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(TaskItemModifyActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}	
	private void SetbimgBackClick()  //退出按钮
	{
		imgBack2.setOnClickListener(new View.OnClickListener()   //增加监控点
		{			
			@Override
			public void onClick(View v)
			{  
				finish();
			}
		});
	}
	private void SetbimgOKClick()  //修改后，退出按钮
	{
		imgOK.setOnClickListener(new View.OnClickListener()  
		{			
			@Override
			public void onClick(View v)
			{  
				Intent newintent=new Intent();
		        newintent.putExtra("workdir", workdir);  					//是那个监控点目录
		        newintent.putExtra("editok", true);         //修改生效
                newintent.putExtra("currenttask", currenttask);      //当前选中的任务	 
                newintent.putExtra("currenttaskposition", currenttaskposition);//当前任务中的第几行内容	
                taskitem.action=oppara.getText().toString();
                try
                {
                	taskitem.delayTime=Integer.parseInt(opdelay.getText().toString());
                }
                catch(Exception ex)
                {  taskitem.delayTime=0; }
                setResult(RESULT_OK, newintent);
				finish();
			}
		});
	}

	// 设置触摸事件监听  
	private void SetcbappidTouchListener()  
	{
		cbappid.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				notOnchanged=false;  //只有触摸一下才能改变
				return false;
			}  
		});  
	}
	private void SetcbappidOnSelect()  //选择设备系统
	{
		cbappid.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				if (notOnchanged) return;
				String[] ss=appids.get(position).split("\\-");
				try
				{
					int appid=Integer.parseInt(ss[0]);
					LoadOneDeviceSystem(appid);     	 	  //装入指定任务的内容
					taskitem.appId=appid;  //修改全局变量的值
				}
				catch(Exception e)
				{    	}	
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}

	// 设置触摸事件监听  
	private void SetdevicesystemTouchListener()  
	{
		devicename.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				notOnchanged=false;  //只有触摸一下才能改变
				return false;
			}  
		});  
	}

	
	private void SetdevicesystemOnSelect()  //选择家居设备
	{
		devicename.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				if (notOnchanged) return;
				String[] ss=devicesystems.get(position).split("\\-");
				try
				{
					int deviceid=Integer.parseInt(ss[0]);
					taskitem.devieID=deviceid; //修改全局变量的值
					HomeDevice homedevice= taskitem.FindHomeDevice(homes);  //找到家居设备
					LoadOneDevice(homedevice);   //装入一个具体家居设备				
				}
				catch(Exception e)
				{    	}	
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}
	
	// 设置触摸事件监听  
	private void SetdeviceTouchListener()  
	{
		subdevicename.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				notOnchanged=false;  //只有触摸一下才能改变
				return false;
			}  
		});  
	}
	private void SetdeviceOnSelect()  //选择家居设备子设备
	{
		subdevicename.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				if (notOnchanged) return;
				Object dv=subdvobjs.get(position);
				if (dv instanceof DeviceDO) 
				{
				     taskitem.subDevieID=((DeviceDO) dv).id;  ////修改全局变量的值
				     taskitem.deviceType=DeviceType.DO;			
				     deviceoperation.setText( dooperationhint1+"\r\n"+dooperationhint2);			
				}
				else if (dv instanceof DeviceAO) 
				{
				     taskitem.subDevieID=((DeviceAO) dv).id;
				     taskitem.deviceType=DeviceType.AO;		
				     deviceoperation.setText( ((DeviceAO)dv).controldescription);
				}
				else if (dv instanceof DeviceSO) 
				{
				     taskitem.subDevieID=((DeviceSO) dv).id;
				     taskitem.deviceType=DeviceType.SO;		
				     deviceoperation.setText( ((DeviceSO)dv).controldescription);
				}
				else deviceoperation.setText("");
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)   //创建活动
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.taskitem_set_layout);
		GetUIName();
		taskitem=EditTaskItem.GetTaskItem();
		showitem=new TaskItem();
		TaskItem.Copy(taskitem, showitem);  //保存显示用的TaskItem
		SetButtonClickEvent();
	}

	private TaskItem taskitem;
	private TaskItem showitem;
	private String taskname;  //任务名称	
	private int currenttask=-1;					//当前选中的任务
	private int currenttaskposition=-1;		//当前任务内容中的第几行内容	
	private String dooperationhint1;
	private String dooperationhint2;
	@Override
	protected void onResume()
	{  
		super.onResume();
	    dooperationhint1 =getResources().getString(R.string.dooperationhint1);	
	    dooperationhint2 =getResources().getString(R.string.dooperationhint2);		
		TaskItem.Copy(showitem,taskitem);  //恢复全局TaskItem
		Intent intent=this.getIntent();
		String dir=intent.getStringExtra("workdir");		
		taskname=intent.getStringExtra("taskname");		
		currenttask=intent.getIntExtra("currenttask",-1);		//当前选中的任务
		currenttaskposition=intent.getIntExtra("currenttaskposition",-1);	//当前任务中的第几行内容	
		if (dir!=null) workdir=dir;		
		
		menditemtext.setText(taskname+"("+currenttaskposition+") - "+getResources().getString(R.string.cmdmodify));
		LoadAllHomeDevice();    						//装入智能家居的所有设备系统
		notOnchanged=true;
		ShowTaskItemContent(showitem);		//显示
		TaskItem.Copy(showitem,taskitem);  //恢复全局TaskItem
	}

	Boolean notOnchanged=true;
	private void ShowTaskItemContent(TaskItem showitem)		//显示操作项的内容提到UI
	{
		//TaskItem tmpitem=new TaskItem();
		//TaskItem.Copy(showitem, tmpitem);   //保存临时记录，下拉框选择会改变taskitem的内容
		oppara.setText(showitem.action);  //操作参数
		opdelay.setText(showitem.delayTime+"");  //操作演示
		int appid=showitem.appId;
		int position=FindAppid(appid);  //设备系统所在行
		cbappid.setSelection(position);   //定位行
		LoadOneDeviceSystem(appid);   //装入家居设备	
		HomeDevice homedevice= showitem.FindHomeDevice(homes);  //找到家居设备
		LoadOneDevice(homedevice);   //装入一个家居设备
		position=FindHomeDevice(showitem.devieID);  //设备系统所在行
		devicename.setSelection(position);
		
		Object dv=showitem.FindDevice(homes);  //找到具体子设备
		if (dv==null) deviceoperation.setText("");
		else if (dv instanceof DeviceDO) 
		{
			//deviceoperation.setText( ((DeviceDO)dv).controldescription+"\r\n"+dooperationhint1+"\r\n"+dooperationhint2);
			deviceoperation.setText( dooperationhint1+"\r\n"+dooperationhint2);			
		}
		else if (dv instanceof DeviceAO) deviceoperation.setText( ((DeviceAO)dv).controldescription);
		else if (dv instanceof DeviceSO) deviceoperation.setText( ((DeviceSO)dv).controldescription);
		else deviceoperation.setText("");
		position=FindSubDevice(dv);  //子设备所在行
		subdevicename.setSelection(position);	
		//TaskItem.Copy(tmpitem,taskitem);
	}
	
	private int FindAppid(int appid)  //设备系统所在行
	{
		for (int i=0; i<appids.size(); i++)
		{
			String[] ss=appids.get(i).split("\\-");
			try
			{
				int id=Integer.parseInt(ss[0]);
				if (id==appid) return i;
			}
			catch(Exception e)
			{    	}			
		}
		return -1;
	}
	private int FindHomeDevice(int deviceid)  //找到家居设备所在行
	{
		for (int i=0; i<devicesystems.size(); i++)
		{
			String[] ss=devicesystems.get(i).split("\\-");
			try
			{
				int id=Integer.parseInt(ss[0]);
				if (id==deviceid) return i;
			}
			catch(Exception e)
			{    	}			
		}
		return -1;
	}
	private int FindSubDevice(Object dv)
	{
		String s=TaskItemDeviceToString(dv);
		for (int i=0; i<subdevices.size(); i++)
		{
			if (s.equals(subdevices.get(i))) return i;
		}
		return -1;
	}
	
	private SmartHomes homes;  //每个家居设备系统的内容
	private SmartHomeApps InstlledDeviceSystem; //安装了的设备系统
	protected ArrayAdapter<String> appidadapter;  //家居设备系统适配器：可能包含多个设备子系统
    Vector<String> appids=new Vector<String>();
	private void LoadAllHomeDevice()  //装入智能家居的所有设备系统
	{
		appids.clear();
		homes=new SmartHomes();
        InstlledDeviceSystem = new SmartHomeApps(this,workdir,"EagleSmartHome.esh");
        for (int i=0; i<InstlledDeviceSystem.SmartHomeChannels.size(); i++)
        {
        	int id=InstlledDeviceSystem.SmartHomeChannels.get(i).appid;
        	SmartHome smarthome=new SmartHome(this, workdir, "SmartHome"+id+".smh");
        	homes.smarthomes.add(smarthome);
        	homes.appids.add(InstlledDeviceSystem.SmartHomeChannels.get(i).appid);
        	String s=id+"-"+ InstlledDeviceSystem.SmartHomeChannels.get(i).description;
        	s+="("+smarthome.homedevices.get(0).positiondescription+")";        	
        	appids.add(s);
        }
		 this.appidadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, appids);
		 cbappid.setAdapter(appidadapter);
	}

	protected ArrayAdapter<String> devicesadapter;  //设备子系统适配器：可能包含多个设备
    Vector<String> devicesystems=new Vector<String>();
    private void LoadOneDeviceSystem(int appid)     	 	  //装入指定任务的内容
    {
        devicesystems.clear();
        HomeDevice device=null;
        for (int i=0; i<InstlledDeviceSystem.SmartHomeChannels.size(); i++)
        {
        	if (InstlledDeviceSystem.SmartHomeChannels.get(i).appid==appid)
        	{
        		SmartHome  smarthome=homes.smarthomes.get(i);
        		for (int j=0; j<smarthome.homedevices.size(); j++)
        		{
        			device=smarthome.homedevices.get(j);
        			String s=device.deviceid+"-"+device.devicename;
        			devicesystems.add(s);        			
        		}
        		break;
        	}        	
        }
        this.devicesadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, devicesystems);
		devicename.setAdapter(devicesadapter);    
    }
	
	protected ArrayAdapter<String> onedeviceadapter;  //一个具体设备适配器：可能包含多个子设备
    Vector<String> subdevices =new Vector<String>();
    Vector<Object> subdvobjs =new Vector<Object>();
    private void LoadOneDevice(HomeDevice device)     	 	  //装入具体子设备
    {
        subdevices .clear();
        subdvobjs.clear();
        if (device!=null)
        {
        	String s="";
        	for (int i=0; i<device.DODevices.size(); i++)
        	{
        		DeviceDO dv=device.DODevices.get(i);
        		subdvobjs.add(dv);
        		s=TaskItemDeviceToString(dv);
        		//s=dv.id  +"-"+dv.devicetype.toString()+"("+dv.dotype.toString()+")-"+dv.functiondescription;
        		subdevices.add(s);
        	}
        	for (int i=0; i<device.AODevices.size(); i++)
        	{
        		DeviceAO dv=device.AODevices.get(i);
        		subdvobjs.add(dv);
        		s=TaskItemDeviceToString(dv);
        		//s=dv.id  +"-"+dv.devicetype.toString()+"("+dv.dotplace+")-"+dv.functiondescription;
        		subdevices.add(s);
        	}
        	for (int i=0; i<device.SODevices.size(); i++)
        	{
        		DeviceSO dv=device.SODevices.get(i);
        		subdvobjs.add(dv);
        		s=TaskItemDeviceToString(dv);
        		//s=dv.id  +"-"+dv.devicetype.toString()+"("+dv.streamtype.toString()+")-"+dv.functiondescription;
        		subdevices.add(s);        		
        	}  	        	
        }        
		this.onedeviceadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, subdevices);
		subdevicename.setAdapter(onedeviceadapter);     
    }

    private String TaskItemDeviceToString(Object device)
    {
    	 String s="";
		  if (device instanceof DeviceDO)
		  {
			  DeviceDO dv=(DeviceDO)device;
			  s=dv.id  +"-"+dv.devicetype.toString()+"("+dv.dotype.toString()+")-"+dv.functiondescription;
		  }
		  else  if (device instanceof DeviceAO)
		  {
			  DeviceAO dv=(DeviceAO)device;
      		  s=dv.id  +"-"+dv.devicetype.toString()+"("+dv.dotplace+")-"+dv.functiondescription;
		  }
		  else  if (device instanceof DeviceSO)
		  {
			  DeviceSO dv=(DeviceSO)device;
        	  s=dv.id  +"-"+dv.devicetype.toString()+"("+dv.streamtype.toString()+")-"+dv.functiondescription;
		  }
    	return s;
    }
}
