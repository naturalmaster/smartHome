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
	private void SetButtonClickEvent()  //���ð�ť��Ӧ�¼�
	{
		try
		{
			SetbimgBackClick();	
			SetbimgOKClick();		
			SetcbappidOnSelect();  				//ѡ���豸ϵͳ
			SetdevicesystemOnSelect();  		//ѡ��Ҿ��豸
			SetdeviceOnSelect(); 					//ѡ��Ҿ��豸���豸
			
			SetdeviceTouchListener();  // ���ô����¼�����  
			SetdevicesystemTouchListener();  // ���ô����¼�����  
			SetcbappidTouchListener();  // ���ô����¼�����  
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(TaskItemModifyActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}	
	private void SetbimgBackClick()  //�˳���ť
	{
		imgBack2.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{  
				finish();
			}
		});
	}
	private void SetbimgOKClick()  //�޸ĺ��˳���ť
	{
		imgOK.setOnClickListener(new View.OnClickListener()  
		{			
			@Override
			public void onClick(View v)
			{  
				Intent newintent=new Intent();
		        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼
		        newintent.putExtra("editok", true);         //�޸���Ч
                newintent.putExtra("currenttask", currenttask);      //��ǰѡ�е�����	 
                newintent.putExtra("currenttaskposition", currenttaskposition);//��ǰ�����еĵڼ�������	
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

	// ���ô����¼�����  
	private void SetcbappidTouchListener()  
	{
		cbappid.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				notOnchanged=false;  //ֻ�д���һ�²��ܸı�
				return false;
			}  
		});  
	}
	private void SetcbappidOnSelect()  //ѡ���豸ϵͳ
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
					LoadOneDeviceSystem(appid);     	 	  //װ��ָ�����������
					taskitem.appId=appid;  //�޸�ȫ�ֱ�����ֵ
				}
				catch(Exception e)
				{    	}	
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}

	// ���ô����¼�����  
	private void SetdevicesystemTouchListener()  
	{
		devicename.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				notOnchanged=false;  //ֻ�д���һ�²��ܸı�
				return false;
			}  
		});  
	}

	
	private void SetdevicesystemOnSelect()  //ѡ��Ҿ��豸
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
					taskitem.devieID=deviceid; //�޸�ȫ�ֱ�����ֵ
					HomeDevice homedevice= taskitem.FindHomeDevice(homes);  //�ҵ��Ҿ��豸
					LoadOneDevice(homedevice);   //װ��һ������Ҿ��豸				
				}
				catch(Exception e)
				{    	}	
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}
	
	// ���ô����¼�����  
	private void SetdeviceTouchListener()  
	{
		subdevicename.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				notOnchanged=false;  //ֻ�д���һ�²��ܸı�
				return false;
			}  
		});  
	}
	private void SetdeviceOnSelect()  //ѡ��Ҿ��豸���豸
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
				     taskitem.subDevieID=((DeviceDO) dv).id;  ////�޸�ȫ�ֱ�����ֵ
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
	protected void onCreate(Bundle savedInstanceState)   //�����
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.taskitem_set_layout);
		GetUIName();
		taskitem=EditTaskItem.GetTaskItem();
		showitem=new TaskItem();
		TaskItem.Copy(taskitem, showitem);  //������ʾ�õ�TaskItem
		SetButtonClickEvent();
	}

	private TaskItem taskitem;
	private TaskItem showitem;
	private String taskname;  //��������	
	private int currenttask=-1;					//��ǰѡ�е�����
	private int currenttaskposition=-1;		//��ǰ���������еĵڼ�������	
	private String dooperationhint1;
	private String dooperationhint2;
	@Override
	protected void onResume()
	{  
		super.onResume();
	    dooperationhint1 =getResources().getString(R.string.dooperationhint1);	
	    dooperationhint2 =getResources().getString(R.string.dooperationhint2);		
		TaskItem.Copy(showitem,taskitem);  //�ָ�ȫ��TaskItem
		Intent intent=this.getIntent();
		String dir=intent.getStringExtra("workdir");		
		taskname=intent.getStringExtra("taskname");		
		currenttask=intent.getIntExtra("currenttask",-1);		//��ǰѡ�е�����
		currenttaskposition=intent.getIntExtra("currenttaskposition",-1);	//��ǰ�����еĵڼ�������	
		if (dir!=null) workdir=dir;		
		
		menditemtext.setText(taskname+"("+currenttaskposition+") - "+getResources().getString(R.string.cmdmodify));
		LoadAllHomeDevice();    						//װ�����ܼҾӵ������豸ϵͳ
		notOnchanged=true;
		ShowTaskItemContent(showitem);		//��ʾ
		TaskItem.Copy(showitem,taskitem);  //�ָ�ȫ��TaskItem
	}

	Boolean notOnchanged=true;
	private void ShowTaskItemContent(TaskItem showitem)		//��ʾ������������ᵽUI
	{
		//TaskItem tmpitem=new TaskItem();
		//TaskItem.Copy(showitem, tmpitem);   //������ʱ��¼��������ѡ���ı�taskitem������
		oppara.setText(showitem.action);  //��������
		opdelay.setText(showitem.delayTime+"");  //������ʾ
		int appid=showitem.appId;
		int position=FindAppid(appid);  //�豸ϵͳ������
		cbappid.setSelection(position);   //��λ��
		LoadOneDeviceSystem(appid);   //װ��Ҿ��豸	
		HomeDevice homedevice= showitem.FindHomeDevice(homes);  //�ҵ��Ҿ��豸
		LoadOneDevice(homedevice);   //װ��һ���Ҿ��豸
		position=FindHomeDevice(showitem.devieID);  //�豸ϵͳ������
		devicename.setSelection(position);
		
		Object dv=showitem.FindDevice(homes);  //�ҵ��������豸
		if (dv==null) deviceoperation.setText("");
		else if (dv instanceof DeviceDO) 
		{
			//deviceoperation.setText( ((DeviceDO)dv).controldescription+"\r\n"+dooperationhint1+"\r\n"+dooperationhint2);
			deviceoperation.setText( dooperationhint1+"\r\n"+dooperationhint2);			
		}
		else if (dv instanceof DeviceAO) deviceoperation.setText( ((DeviceAO)dv).controldescription);
		else if (dv instanceof DeviceSO) deviceoperation.setText( ((DeviceSO)dv).controldescription);
		else deviceoperation.setText("");
		position=FindSubDevice(dv);  //���豸������
		subdevicename.setSelection(position);	
		//TaskItem.Copy(tmpitem,taskitem);
	}
	
	private int FindAppid(int appid)  //�豸ϵͳ������
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
	private int FindHomeDevice(int deviceid)  //�ҵ��Ҿ��豸������
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
	
	private SmartHomes homes;  //ÿ���Ҿ��豸ϵͳ������
	private SmartHomeApps InstlledDeviceSystem; //��װ�˵��豸ϵͳ
	protected ArrayAdapter<String> appidadapter;  //�Ҿ��豸ϵͳ�����������ܰ�������豸��ϵͳ
    Vector<String> appids=new Vector<String>();
	private void LoadAllHomeDevice()  //װ�����ܼҾӵ������豸ϵͳ
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

	protected ArrayAdapter<String> devicesadapter;  //�豸��ϵͳ�����������ܰ�������豸
    Vector<String> devicesystems=new Vector<String>();
    private void LoadOneDeviceSystem(int appid)     	 	  //װ��ָ�����������
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
	
	protected ArrayAdapter<String> onedeviceadapter;  //һ�������豸�����������ܰ���������豸
    Vector<String> subdevices =new Vector<String>();
    Vector<Object> subdvobjs =new Vector<Object>();
    private void LoadOneDevice(HomeDevice device)     	 	  //װ��������豸
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
