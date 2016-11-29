package com.eagle.smarthome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.eagle.smarthome.Device.SmartHomeApps;
import com.eagle.smarthome.task.ScenePlans;
import com.eagle.smarthome.task.ScenePlansItem;
import com.eagle.smarthome.util.DeviceSystemAdapter;
import com.eagle.smarthome.util.SHProtocol;
import com.eagle.smarthome.util.SmartHomeChannel;
import com.eagle.smarthome.util.TaskAdapter;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends BaseActivity
{
	LocalBroadcastManager localBroadcastManager;

	private String workdir="";
	private Button btnExcuteTask;		//执行任务
	private Button btnMessage;		   //留言
	private Button btnSetTask;  			//任务设置
	private Button btnSetAlarm;       //任务设置
	private Button btnExit;	
	private Button btnChangeMonitor;		

	private Button btnLoadDevice;  	//更新设备
	private Button btnLoadTask;  		//更新任务
	private Button btnLoadAlarm;    	//更新报警
	private Button btnLoadPicture;   //更新图片	
	
	private Button btnStartDevice;		//启动设备
	private Button btnStopDevice;		//结束设备
	
	private ListView lvTask;					
	private ListView 	lvDeviceSystem;
	private TextView 	txtHint;
	private TextView 	monitorhome;
	//monitorhome


	private void ShowHint(String txt)
	{
		if (txtHint.getVisibility()==View.GONE)
			txtHint.setVisibility(View.VISIBLE);
		txtHint.setText(txt);
		txtHint.invalidate();
	}
	private void HideHint()
	{
		if (txtHint==null) return;
		txtHint.setVisibility(View.GONE);
	}
	private void GetButtonName()
	{
		try
		{
			btnExcuteTask=(Button) findViewById(R.id.btnExcuteTask);	 			
			btnMessage=(Button) findViewById(R.id.btnMessage);	 
			btnSetTask=(Button) findViewById(R.id.btnSetTask);	 
			btnSetAlarm=(Button) findViewById(R.id.btnSetAlarm);	 
			btnExit=(Button) findViewById(R.id.btnExit);	 
			btnChangeMonitor=(Button) findViewById(R.id.btnChangeMonitor);	 
			btnLoadDevice=(Button) findViewById(R.id.btnLoadDevice);	 
			btnLoadTask=(Button) findViewById(R.id.btnLoadTask);	 
			btnLoadAlarm=(Button) findViewById(R.id.btnLoadAlarm);	 
			btnLoadPicture=(Button) findViewById(R.id.btnLoadPicture);	 		

			btnStartDevice=(Button) findViewById(R.id.btnStartDevice);		
			btnStopDevice=(Button) findViewById(R.id.btnStopDevice);		
			lvTask=(ListView)findViewById(R.id.lvTask);		
			lvDeviceSystem=(ListView)findViewById(R.id.lvDeviceSystem);		
			txtHint=(TextView)findViewById(R.id.txtHint);		
			monitorhome=(TextView)findViewById(R.id.monitorhome);		
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(MainActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}
	
	private void SetButtonClickEvent()  //设置按钮响应事件
	{
		try
		{
			SetbtnExcuteTaskClick();
			SetbtnMessageClick();
			SetbtnSetTaskClick();
			SetbtnSetAlarmClick();
			SetbtnExitClick();		
			SetbtnChangeMonitorClick();					

			SetbtnLoadDeviceClick();
			SetbtnLoadTaskClick();
			SetbtnLoadAlarmClick();
			SetbtnLoadPictureClick();
			SetbtnStartDeviceClick();
			SetbtnStopDeviceClick();
			SetlvTaskeOnItemClick();
			SetlvDeviceSystemOnItemClick();
			SetlvDeviceSystemOnItemLongClick();
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(MainActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}
	private void SetbtnSetTaskClick()
	{
		btnSetTask.setOnClickListener(new View.OnClickListener()   //增加监控点
		{			
			@Override
			public void onClick(View v)
			{
				Intent newintent=new Intent(MainActivity.this,TaskSetActivity.class);
		        newintent.putExtra("workdir", workdir);  					//是那个监控点目录
                startActivity(newintent);
			}
		});
	}
	private void SetbtnSetAlarmClick()
	{
		btnSetAlarm.setOnClickListener(new View.OnClickListener()   //增加监控点
		{			
			@Override
			public void onClick(View v)
			{
				Intent newintent=new Intent(MainActivity.this,MonitorSetActivity.class);
		        newintent.putExtra("workdir", workdir);  					//是那个监控点目录
                startActivity(newintent);
			}
		});
	}
	/*private void SetbtnMessageClick()
	{
		btnMessage.setOnClickListener(new View.OnClickListener()   //留言
		{			
			@Override
			public void onClick(View v)
			{ 
				final EditText input = new EditText(MainActivity.this);  
				input.setBackground( getResources().getDrawable(R.drawable.shape));	 	
				//input.setTop(24);
				
				AlertDialog.Builder ad1  = new AlertDialog.Builder(MainActivity.this);  
				ad1.setTitle("留言信息");  
		        ad1.setIcon(android.R.drawable.ic_dialog_info);  
		        ad1.setView(input);  
		        ad1.setPositiveButton("发送", new DialogInterface.OnClickListener() 
				        {  
				            	public void onClick(DialogInterface dialog, int i) 
					            {  
					            	String text=input.getText().toString();
					            	if (text.equals("")) return;
					            	StringJson json=new StringJson(SHProtocol.SHFLAG);
							  		json.AddNameVolume("cmd", SHProtocol.MESSAGE);		 
							        json.AddNameVolume("text", text);		        
									TcpSend(json);						
					            }  
				        });  
		        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		              {  public void onClick(DialogInterface dialog, int i) {      }    });  
				
		        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
				//		ViewGroup.LayoutParams.WRAP_CONTENT);
				//layoutParams.setMargins(16,16,16,16);//4个参数按顺序分别是左上右下
				//input.setLayoutParams(layoutParams); 
				
		        ad1.show();// 显示对话框  
			}
		});
	}  */
	
	private void SetbtnMessageClick()
	{
		btnMessage.setOnClickListener(new View.OnClickListener()   //增加监控点
				{			
					@Override
					public void onClick(View v)
					{
						Intent newintent=new Intent(MainActivity.this,MessageActivity.class);
				        newintent.putExtra("workdir", workdir);  					//是那个监控点目录
		                startActivity(newintent);
					}
				});	
	}
	
	ScenePlansItem selectedPlansItem=null;
	SmartHomeChannel selecteHome=null;
	private void SetbtnExcuteTaskClick()
	{
		btnExcuteTask.setOnClickListener(new View.OnClickListener()   //增加监控点
		{			
			@Override
			public void onClick(View v)
			{  //RUNTASK = "509";    //通知SHS执行某个任务[task=fn][timed=1][starttime=second]
				if (selectedPlansItem==null) return;
				//adapter.setSelectItem(arg2);  
				if (!selectedPlansItem.Used)
				{
				  Toast.makeText(MainActivity.this,  "该任务场景被禁止执行" , Toast.LENGTH_LONG).show();
				  return;
				}
				StringJson json=new StringJson(SHProtocol.SHFLAG);
		  		json.AddNameVolume("cmd", SHProtocol.RUNTASK);		 
		        json.AddNameVolume("task", selectedPlansItem.PlanFileName);
		        json.AddNameVolume("timed", "0");
		        json.AddNameVolume("starttime", "0");
				TcpSend(json);
			}
		});
	}

	private void SetbtnChangeMonitorClick()
	{
		btnChangeMonitor.setOnClickListener(new View.OnClickListener()   //设置或取消监控
		{			
			@Override
			public void onClick(View v)
			{ 
				  SetAlarm(setalarm?false:true);
			}
		});
	}

	private Boolean setalarm=false;
    private void SetAlarm(Boolean setalarm)  //发送设防撤防指令
   {
       // if (!bCanSetAlarm) { txtOut.Text = "您没有设防撤防权限"; return; }
        StringJson json = new StringJson(SmartHomeChannel.SHFLAG);
        //SETALARM = "515";   设置 设防/撤防 [set="1/0"]   获取设防/撤防[value="1/0"]
        json.AddNameVolume("cmd", SHProtocol.SETALARM);
        json.AddNameVolume("set", setalarm ? "1" : "0");
        TcpSend(json);
    }

   private void GetAlarmSetting()  //获取设防状态S
    {
        StringJson json = new StringJson(SmartHomeChannel.SHFLAG);
        //SETALARM = "515";   设置 设防/撤防 [set="1/0"]   获取设防/撤防[value="1/0"]
        json.AddNameVolume("cmd", SHProtocol.SETALARM);
    	TcpSend(json);
    }
    
	private void  SetbtnStartDeviceClick()  //启动设备系统
	{
		btnStartDevice.setOnClickListener(new View.OnClickListener()   //增加监控点
		{			
			@Override
			public void onClick(View v)
			{
				if (selecteHome==null) return;
				StringJson json=new StringJson(SHProtocol.SHFLAG);				 
	            //STARTAPP = "502";   //通知启动或结束SHM程序，需要权限[appid=?][start=1][rights=NNNNNNNNNNNNNNN]
	            json.AddNameVolume("cmd", SHProtocol.STARTAPP);
	            json.AddNameVolume("appid", selecteHome.appid+"");
	            json.AddNameVolume("start", "1");
				TcpSend(json);
			}
		});
	}
	private void  SetbtnStopDeviceClick()  //技术设备系统
	{
		btnStopDevice.setOnClickListener(new View.OnClickListener()   //增加监控点
		{			
			@Override
			public void onClick(View v)
			{
				if (selecteHome==null) return;
				StringJson json=new StringJson(SHProtocol.SHFLAG);				 
	            //STARTAPP = "502";   //通知启动或结束SHM程序，需要权限[appid=?][start=1][rights=NNNNNNNNNNNNNNN]
	            json.AddNameVolume("cmd", SHProtocol.STARTAPP);
	            json.AddNameVolume("appid", selecteHome.appid+"");
	            json.AddNameVolume("start", "0");
				TcpSend(json);
			}
		});
	}
	private void  SetbtnExitClick()  //结束
	{
		btnExit.setOnClickListener(new View.OnClickListener()   //增加监控点
		{			
			@Override
			public void onClick(View v)
			{
				ActivtyItems.finishAll(); 
			}
		});
	}
	private void  SetbtnLoadDeviceClick()  //更新设备指令
	{
		btnLoadDevice.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{
				String working=getResources().getString(R.string.working);	
				if (!bdemo)  ShowHint(working);
	            GetAllDeviceSystem();
			}
		});
	}
	private void  SetbtnLoadTaskClick()     //更新任务指令
	{
		btnLoadTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{
				String working=getResources().getString(R.string.working);
				ShowHint(working);
            	//Toast.makeText(MainActivity.this,  working , Toast.LENGTH_SHORT).show();		
				UpdateTask();
			}
		});
	}
	private void  SetbtnLoadAlarmClick()     //更新报警指令
	{
		btnLoadAlarm.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{
				String working=getResources().getString(R.string.working);	
				if(!bdemo) ShowHint(working);
				UpdateAlarm();
			}
		});
	}
	private void  SetbtnLoadPictureClick()  //更新图片指令
	{
		btnLoadPicture.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{
				String working=getResources().getString(R.string.working);	
				ShowHint(working);
            	//Toast.makeText(MainActivity.this,  working , Toast.LENGTH_SHORT).show();		
				UpdatePicture(); 
			}
		});
	}
 	private void UpdateTask() //GETTASK = "507";  //获取智能家居的任务数据 [task=fn][need="1"][stream=byte[]][index=?]
    {
		if (bdemo) {
			try {
				String filename = "Task.tsk";
				MakeDirectory(workdir); // 创建子目录
				CopyAssetsFile(filename, filename);
			} catch (Exception ex) {
			}
			CopyTaskFile();
			LoadTask("Task.tsk"); 				//装入任务列表
			Toast.makeText(MainActivity.this,  "演示任务数据已经复制" , Toast.LENGTH_SHORT).show();		
			return; // 保存文件，无需显示
		}
        try
        {
			StringJson json=new StringJson(SHProtocol.SHFLAG);
            json.AddNameVolume("cmd", SHProtocol.GETTASK);
            json.AddNameVolume("need", "1");
            TcpSend(json);
        }
        catch (Exception ex)
        { 
     	   ex.printStackTrace();
        }
    }
 	
	final String[] taskf={"playmusic.act","stopmusic.act","lighton.act","lightoff.act","sendSMS.act","voicehint.act"};
	public void CopyTaskFile()
	{
		for (int i=0;i<taskf.length;i++)
		{
	        String fn=taskf[i];
	        ShowHint(fn);
		    CopyAssetsFile(fn,fn);
		}
		this.HideHint();
	}
	private void UpdateAlarm() ////GETALARM = "510";   //获取智能家居的监控设置 [alarm=fn][need="1"][stream=byte[]][index=?]
    {
		if (bdemo) {
			try {
		    	String filename="Monitor.alm";
				MakeDirectory(workdir);  //创建子目录
				CopyAssetsFile(filename,filename);
			} catch (Exception ex) {
			}
			Toast.makeText(MainActivity.this,  "演示监控数据已经复制" , Toast.LENGTH_SHORT).show();
			return;  //保存文件，无需显示
		}
        try
        {
			StringJson json=new StringJson(SHProtocol.SHFLAG);
            json.AddNameVolume("cmd", SHProtocol.GETALARM);
            json.AddNameVolume("need", "1");
            TcpSend(json);
        }
        catch (Exception ex)
        { 
     	   ex.printStackTrace();
        }
    }
	private void SetlvTaskeOnItemClick()
	{
		lvTask.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{   
				selectedPlansItem=tasks.Items.get(position);
				 taskadapter.setSelectItem(position);  
				 taskadapter.notifyDataSetInvalidated();
			 	//Toast.makeText(MainActivity.this,  selectedPlansItem.PlanFileName, Toast.LENGTH_LONG).show();	    		
			}		
		});
	}

	private void CopyAssetsFile(String sf,String df)  //把assets资源目录下的文件复制到应用程序目录下wordir
	{
		try {
			InputStream is = this.getAssets().open(sf);  //assets资源目录下的文件
			String filename=this.getFilesDir()+File.separator+workdir+File.separator+df;  //应用程序目录下wordir的文件
			FileOutputStream fos = new FileOutputStream(new File(filename));
			byte[] buffer = new byte[1024];
			while (true) {
				int len = is.read(buffer);
				if (len == -1)
					break;
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();
		} catch (Exception ex) {
		}	
	}
	private long lastClickTime;
	private void SetlvDeviceSystemOnItemClick()
	{
		lvDeviceSystem.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{   
				SmartHomeChannel home=InstlledDeviceSystem.SmartHomeChannels.get(position);	
				if (home==selecteHome   && (Math.abs(lastClickTime-System.currentTimeMillis()) < 300))
				{
					lastClickTime = 0;  //模拟双击事件
					if (position>=0)
					{
						Intent newintent=new Intent(MainActivity.this,DeviceControlActivity.class);
				        newintent.putExtra("workdir", workdir);  					//是那个监控点目录
		                newintent.putExtra("appid", selecteHome.appid);  //是那个监控点 ID       
		                newintent.putExtra("dvname", selecteHome.name);  //是那个监控点 ID       
		                newintent.putExtra("dvdescription", selecteHome.description);  //是那个监控点 ID   
		                startActivity(newintent);
		                return;
					}
				}
				else //不是点击同一行,或时间过长
				{
					selecteHome=home;
					deviceSystemadapter.setSelectItem(position);  
					deviceSystemadapter.notifyDataSetInvalidated();
					lastClickTime = System.currentTimeMillis();
				}
				StringJson json=new StringJson(SHProtocol.SHFLAG);				 
				//APPSTATE = "501";   //获取所有SHM程序状态[stream=文件内容][id=appid][state=1/0]
	            json.AddNameVolume("cmd", SHProtocol.APPSTATE);
	            json.AddNameVolume("id", home.appid+"");
	            json.AddNameVolume("state", "1");  //请求获取是否启动设备监控程序
				TcpSend(json);
			}		
		});
	}
	private void SetlvDeviceSystemOnItemLongClick()
	{
		lvDeviceSystem.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long id)
			{
				selecteHome=InstlledDeviceSystem.SmartHomeChannels.get(position);
				deviceSystemadapter.setSelectItem(position);  
				deviceSystemadapter.notifyDataSetInvalidated();
	            //进入其他界面
				if (position>=0)
				{
					Intent newintent=new Intent(MainActivity.this,DeviceControlActivity.class);
			        newintent.putExtra("workdir", workdir);  					//是那个监控点目录
	                newintent.putExtra("appid", selecteHome.appid);  //是那个监控点 ID       
	                newintent.putExtra("dvname", selecteHome.name);  //是那个监控点 ID       
	                newintent.putExtra("dvdescription", selecteHome.description);  //是那个监控点 ID   
	                startActivity(newintent);
	                return true;
				}
				else return false;
			}
		});
	}
	private  void GetAllDeviceSystem() //获取所有安装的设备系统文件 EagleSmartHome.esh
	{
		if (bdemo) {
			//把演示文档EagleSmartHome.esh文档，复制到workdir目录下
			MakeDirectory(workdir);  //创建子目录		
	       	String filename="EagleSmartHome.esh";
	       	CopyAssetsFile(filename,filename);

			LoadInstalledDeviceSystem();  //从文件加载所有安装了的设备系统
			//把对应子设备系统的配置文件复制到根目录下
			for (int i=0;i<InstlledDeviceSystem.SmartHomeChannels.size();i++)
			{
		        String shmfn = "SmartHome" +InstlledDeviceSystem.SmartHomeChannels.get(i).appid+ ".smh";  //保存某个具体设备系统信息的文件
		        ShowHint(shmfn);
		        CopyAssetsFile(shmfn,shmfn);
			}
			this.HideHint();
			Toast.makeText(MainActivity.this,  "演示设备数据已经复制" , Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			StringJson json = new StringJson(SHProtocol.SHFLAG);
			json.AddNameVolume("cmd", SHProtocol.APPSTATE);
			TcpSend(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	final String[] picf={"AI.jpg","alarm.jpg","AO.jpg","device.jpg","DIOFF.jpg","DION.jpg","DOOFF.jpg",
			"DOON.jpg","firealarm.jpg","MusicOFF.jpg","MusicON.jpg","no.jpg","nosound.jpg","play.jpg",
			"playing.jpg","rob.jpg","SI.jpg","SO.jpg","smoke.jpg","TextSpeak.jpg","VideoOFF.jpg","VideoON.jpg"};
	public void CopyPicture()
	{
		for (int i=0;i<picf.length;i++)
		{
	        String fn=picf[i];
	        ShowHint(fn);
		    CopyAssetsFile(fn,fn);
		}
		this.HideHint();
	}
  public void TcpSend(StringJson json)    //发送TCP数据
	{
		if (tcpClient==null) return;
		if (tcpClient.socket==null) return;
		if (!tcpClient.socket.isConnected()) //断开了连接,重新登录
			{
			    Relogin();
				return;
			}
		if (!tcpClient.SendMessage(json))
		{
			Relogin();
			return;		
		}
	}

	private void Relogin() //重新登录
	{
        String tcpdisconnection=getResources().getString(R.string.tcpdisconnection);		
		Toast.makeText(MainActivity.this, tcpdisconnection, Toast.LENGTH_SHORT).show();		
		Intent newintent=new Intent(MainActivity.this,LoginActivity.class);
         startActivity(newintent);
	}
	
	TcpClient tcpClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{  // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainlayout);
		GetButtonName();
		SetButtonClickEvent();
		
		tcpClient=TcpClient.GetTcpClient();
		localReceiver=new TCPDataReceiver();  //注册本地广播接收
		IntentFilter filter=new IntentFilter("com.eagle.smarthome.TCPDATABROADCAST");
		localBroadcastManager=LocalBroadcastManager.getInstance(MainActivity.this);
		localBroadcastManager.registerReceiver(localReceiver,filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (localBroadcastManager!=null && localReceiver!=null)
		{
			localBroadcastManager.unregisterReceiver(localReceiver);
			localReceiver=null;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	private String right="";
	private int  nchkTaskSet=0;
	private int  nchkAlarmSet=1;
	private int  nchkStartAlarm=2;
	private int  nchkShutDown=3;
    
    private Boolean GetStartAlarmRight()
    {
    	if (right.length()<nchkStartAlarm+1) return false;
    	try
    	{
    	String s=right.substring(nchkStartAlarm, nchkStartAlarm+1);
    	return  s.equals("1")?true:false;
    	}
    	catch (Exception e)
    	{
    		return false;
    	}
    }
    private Boolean GetSetAlarmRight()
    {
    	if (right.length()<nchkAlarmSet+1) return false;
    	try
    	{
    	String s=right.substring(nchkAlarmSet, nchkAlarmSet+1);
    	return  s.equals("1")?true:false;
    	}
    	catch (Exception e)
    	{
    		return false;
    	}
    }
    private Boolean GetSetTaskRight()
    {
    	if (right.length()<nchkTaskSet+1) return false;
    	try
    	{
    	String s=right.substring(nchkTaskSet, nchkTaskSet+1);
    	return  s.equals("1")?true:false;
    	}
    	catch (Exception e)
    	{
    		return false;
    	}
    }
    private Boolean GetStartDeviceRight()
    {
    	if (right.length()<nchkShutDown+1) return false;
    	try
    	{
	    	String s=right.substring(nchkShutDown, nchkShutDown+1);
	    	return  s.equals("1")?true:false;
    	}
		catch (Exception e)
		{
			return false;
		}
    }
    boolean bdemo=false;
	@Override
	protected void onResume()
	{  // TODO Auto-generated method stub
		super.onResume();
		Intent intent=this.getIntent();
		String dir=intent.getStringExtra("workdir");		
		bdemo=intent.getBooleanExtra("demo", false);
		right=intent.getStringExtra("right");		
		if (right==null) right="0000";
		
		monitorhome.setText(getResources().getString(R.string.monitorlocation)+" - "+dir);
		if (dir!=null)
		{
			workdir=dir;			
	     	//Toast.makeText(MainActivity.this, "监控点：\r\n"+ workdir, Toast.LENGTH_SHORT).show();	
	     	MakeDirectory(workdir);  //创建子目录
		}
		LoadInstalledDeviceSystem(); 	//装入设备系统
		LoadTask("Task.tsk"); 				//装入任务列表
		btnChangeMonitor.setEnabled(false);
		GetAlarmSetting();
		//this.btnStartDevice.setEnabled(GetStartDeviceRight());
		//this.btnStopDevice.setEnabled(GetStartDeviceRight());
		
		this.btnStartDevice.setVisibility(View.GONE);
		this.btnStopDevice.setVisibility(View.GONE);
		this.btnSetTask.setEnabled(GetSetTaskRight());
		this.btnSetAlarm.setEnabled(GetSetAlarmRight());				
	    //Toast.makeText(MainActivity.this, right, Toast.LENGTH_SHORT).show();	
	}

	void MakeDirectory(String dir)
	{
		String file=this.getFilesDir()+File.separator+dir;		
		File destDir = new File(file);
		if (!destDir.exists()) 
		  {
		     destDir.mkdirs();
		     String updateinfo =getResources().getString(R.string.updateinfo);		
		     String makelocation=getResources().getString(R.string.makelocation);		
		     Toast.makeText(MainActivity.this, makelocation+": \r\n"+ dir+"\r\n"+updateinfo, Toast.LENGTH_SHORT).show();
		  }		
		//Toast.makeText(MainActivity.this, "监控点：\r\n"+ file, Toast.LENGTH_SHORT).show();
	}
    
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void ProcessAPPSTATE(StringJson json)
	{
		 String id = json.GetStrValueByName("id");
		 // APPSTATE = "501";   //获取所有SHM程序状态[stream=文件内容][id=appid][state=1/0]
         if (id == null)
         {
             String appfn ="EagleSmartHome.esh";  //保存整个设备系统信息的文件
             byte[] cont = json.GetValueByName("stream");
             if (cont != null)
             {
             	//Toast.makeText(MainActivity.this,  "Received Data OK:\r\n"+appfn , Toast.LENGTH_SHORT).show();		
                 WriteBytesToFile(appfn, cont);//写入文件保存
                 //从文件重新读入APPS
                 LoadInstalledDeviceSystem();            //GetDeviceSystem();
                 devicedownposition=0;
                 SendGetHomeDeviceInfo(devicedownposition++);
             }
             return;
         }
         String state = json.GetStrValueByName("state");
         if (state!=null)  //监控程序是否启动
         {
        	 //btnStartDevice.setEnabled(state.equals("0"));
           	 //btnStopDevice.setEnabled(state.equals("1"));
        	if( !GetStartDeviceRight()) return;
			if (selecteHome==null) return;
			if (id.equals(selecteHome.appid+"") ) 
			{
	     		this.btnStartDevice.setVisibility(state.equals("0")? View.VISIBLE:View.GONE);
	    		this.btnStopDevice.setVisibility(state.equals("1")? View.VISIBLE:View.GONE);
			}
        	 return;
         }
         String shmfn = "SmartHome" + id + ".smh";  //保存某个具体设备系统信息的文件
         //Toast.makeText(MainActivity.this,  "Received Device Data OK:\r\n"+shmfn , Toast.LENGTH_SHORT).show();
         ShowHint(shmfn);
         byte[] st = json.GetValueByName("stream");
         if (st != null)
             WriteBytesToFile(shmfn, st);//写入文件保存 
         if(devicedownposition<InstlledDeviceSystem.SmartHomeChannels.size())
         	 SendGetHomeDeviceInfo(devicedownposition++);  //再次请求设备系统信息文件
         else
     	     txtHint.setVisibility(View.GONE); //接收完毕，关闭显示
         //UpdatePicture(); //需要更新图片指令	：2016-05-05取消接收图像功能，作为单独一条指令执行：不常用，数据多
	}
   private void ProcessUPDATEPICTURE(StringJson json)
   {//    public static final String UPDATEPICTURE = "504";   //更新图片[pic=fn][size=N][need="1"][stream=byte[]]
	   //String need = json.GetStrValueByName("need");
       String size = json.GetStrValueByName("size");
       String pic = json.GetStrValueByName("pic");
       String index = json.GetStrValueByName("index");
       if (pic != null && size != null)  //收到图片信息
       {
           int nsize = Integer.parseInt(size);         //1、首先获取所有图片列表
           int psize = GetFileSzie(pic);        			//2、现有文件的大小
           if (nsize == psize)   //大小相等，无需更新
           {
               StringJson js=new StringJson(SHProtocol.SHFLAG);
      		   js.AddNameVolume("cmd",SHProtocol.UPDATEPICTURE);
      		   js.AddNameVolume("pic",pic);
      		   js.AddNameVolume("index",index);
               js.AddNameVolume("need", "0");
               TcpSend(js);	 
           }
           else //需要更新  //更新图片[pic=fn][size=N][need="1"][stream=byte[]]
           {
               ShowHint("请求发送图片......"+index);
        	   StringJson js=new StringJson(SHProtocol.SHFLAG);
      		   js.AddNameVolume("cmd",SHProtocol.UPDATEPICTURE);
      		   js.AddNameVolume("pic",pic);
      		   js.AddNameVolume("index",index);
               js.AddNameVolume("need", "1");
               TcpSend(js);	  
           }
       }
       else  //收到图片文件内容
       {
           byte[] st = json.GetValueByName("stream");
           if (st != null)
           {
               WriteBytesToFile(pic, st); //写入文件保存
               ShowHint(pic);
           	   //Toast.makeText(MainActivity.this,  "Received Picture OK:\r\n"+pic , Toast.LENGTH_SHORT).show();
           }
  		   StringJson js=new StringJson(SHProtocol.SHFLAG);
  		   js.AddNameVolume("cmd",SHProtocol.UPDATEPICTURE);
           int nindex = Integer.parseInt(index) + 1;  //下一个图片
           js.AddNameVolume("index", nindex+"");
           js.AddNameVolume("need", "1");
           TcpSend(js);	  
           ShowHint("请求发送图片......"+nindex);
           // txtHint.setVisibility(View.GONE); //接收完毕，关闭显示
       }  
   }
   private void ProcessGETTASK(StringJson json)
   {
	   String task = json.GetStrValueByName("task"); 		
		if (task == null) return;
       String index = json.GetStrValueByName("index"); 		
       if (index == null) return;
       byte[] st = json.GetValueByName("stream");
       if (st != null)
       {
           WriteBytesToFile(task, st);    //写入文件保存
           ShowHint("Received TaskFile OK:"+task);
         	//Toast.makeText(MainActivity.this,  "Received TaskFile OK:\r\n"+task , Toast.LENGTH_LONG).show();	         
         	if(task.equals("Task.tsk"))   //从文件重新读入
         		LoadTask(task);       
         	//if(task.equals("TimedTask.ttsk"))   //从文件重新读入
         		//LoadTask(task);   
       } ////获取智能家居的任务数据 [task=fn][need="1"][stream=byte[]][index=?]
       StringJson js=new StringJson(SHProtocol.SHFLAG);
	   js.AddNameVolume("cmd",SHProtocol.GETTASK);
       int nindex = Integer.parseInt(index) + 1;  //下一个图片
       js.AddNameVolume("index", nindex+"");
       js.AddNameVolume("need", "1");
       TcpSend(js);	  
       ShowHint("请求发送任务......"+nindex);
   }
   private void ProcessGETALARM(StringJson json)
   {
       String alarm = json.GetStrValueByName("alarm"); if (alarm == null) return;
       String index = json.GetStrValueByName("index"); if (index == null) return;
       byte[] st = json.GetValueByName("stream");
       if (st != null)
       {
           WriteBytesToFile(alarm, st);//写入文件保存
           ShowHint("Received AlarmFile OK:"+alarm);     
          //	Toast.makeText(MainActivity.this,  "Received Monitor File OK:\r\n"+alarm , Toast.LENGTH_SHORT).show();		
       }
       StringJson js=new StringJson(SHProtocol.SHFLAG);
	   js.AddNameVolume("cmd",SHProtocol.GETALARM);
       int nindex = Integer.parseInt(index) + 1;  //下一个图片
       js.AddNameVolume("index", nindex+"");
       js.AddNameVolume("need", "1");
       TcpSend(js);	  
       ShowHint("请求发送监控设置......"+nindex);
   }

   private TCPDataReceiver localReceiver;
	class TCPDataReceiver extends BroadcastReceiver   //处理接收到的数据
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{		
			byte[] data=intent.getByteArrayExtra("json");
			StringJson json=StringJson.BytesToStringJson(data, SHProtocol.SHFLAG);
			//StringJson json=TcpClient.GetStringJson();
			String cmd=json.GetStrValueByName("cmd");
           	//Toast.makeText(MainActivity.this, this.toString()+"\r\n"+ json.toString(), Toast.LENGTH_SHORT).show();		
            if (cmd.equals(SHProtocol.APPSTATE)) //获取所有SHM程序状态[stream=文件内容][id=0],无id获取总APP文件EagleSmartHome.esh，
            {
            	ProcessAPPSTATE(json);
            }
			else if (cmd.equals(SHProtocol.UPDATEPICTURE))//UPDATEPICTURE = "504";更新图片[pic=fn][size=N][need="1"][stream=byte[]]
			{
				 ProcessUPDATEPICTURE(json);
				 return;
			}
			else if (cmd.equals(SHProtocol.GETTASK)) //GETTASK = "507";    //获取智能家居的任务数据 [task=fn][need="1"][stream=byte[]][index=?]
			{
				ProcessGETTASK(json);				
	        }
			else if (cmd.equals(SHProtocol.GETALARM)) //GETALARM = "510";   //获取智能家居的监控设置 [alarm=fn][need="1"][stream=byte[]][index=?]
			{
				ProcessGETALARM(json);
			}
			 else if (cmd.equals(SHProtocol.TEXT))  //SHA发给SHM、SHS、CLIENT的文本通知信息[text=?]
		         {
					 String text = json.GetStrValueByName("text");
					 Toast.makeText(MainActivity.this,  text , Toast.LENGTH_SHORT).show();	 
					 HideHint(); //收到文本提示后，关闭文本提示行
		         }
			 else if (cmd.equals(SHProtocol.ERRHINT))  //SHA发给SHM、SHS、CLIENT的文本通知信息[text=?]
		     {
				 String text = json.GetStrValueByName("err");
				 Toast.makeText(MainActivity.this,  text , Toast.LENGTH_SHORT).show();	 
				 HideHint();
		     }    
			 else if (cmd.equals(SHProtocol.TESTCONECTION))  // 通信测试是否连接:客户端收到后原样回复，没有子项
		     {
		         TcpSend(json);		
		     }    
			 else if (cmd.equals(SHProtocol.SETALARM))  // SETALARM = "515";   //设置 设防/撤防 [set="1/0"]   获取设防/撤防[value="1/0"]
		     {
				 String text = json.GetStrValueByName("value");
				 setalarm=(text!=null  && text.equals("1"))?true:false;		
				 SetbtnChangeMonitorText();
				btnChangeMonitor.setEnabled(GetStartAlarmRight());
		     }    
		}
	}
	
	private void SetbtnChangeMonitorText()
	{
		 btnChangeMonitor.setText(setalarm?getResources().getString(R.string.enablemonitor) : getResources().getString(R.string.unenablemonitor));
	}
	private int devicedownposition=0;
	private void SendGetHomeDeviceInfo(int position)
	{
		 SmartHomeChannel chl = InstlledDeviceSystem.SmartHomeChannels.get(position);
		 StringJson json=new StringJson(SHProtocol.SHFLAG);
         json.AddNameVolume("cmd", SHProtocol.APPSTATE);
         json.AddNameVolume("id", chl.appid+"");
         TcpSend(json);		
	}

    private	void WriteBytesToFile(String appfn, byte[] cont)//写入文件保存
	{
		String filename=this.getFilesDir()+File.separator+workdir;
		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
		File file2=new File(file.getAbsoluteFile() +File.separator+appfn);
	  	if (file2.exists()) 
	  		file2.delete(); 
	  	FileOutputStream writer=null;
		try
		{
			writer= new FileOutputStream(file2);
			writer.write(cont);
			writer.flush();  //加快保存
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (writer!=null)
			{
				try
				{
					writer.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}				
		}
	}
	private void  UpdatePicture() //需要更新图片指令
	{
		if (bdemo)
		{
			CopyPicture();
			Toast.makeText(MainActivity.this,  "演示图片数据已经复制" , Toast.LENGTH_SHORT).show();
			return;
		}
        ShowHint("请求图片更新......");
		 StringJson json=new StringJson(SHProtocol.SHFLAG);
	    json.AddNameVolume("cmd", SHProtocol.UPDATEPICTURE);
	    json.AddNameVolume("need", "1");
	    TcpSend(json);		
	}
    private SmartHomeApps InstlledDeviceSystem; //安装了的设备系统
    private DeviceSystemAdapter deviceSystemadapter;
    private void LoadInstalledDeviceSystem()  //从文件加载所有安装了的设备系统
    {
         InstlledDeviceSystem = new SmartHomeApps(this,workdir,"EagleSmartHome.esh");
        /*String s="";  //调试用
        for(SmartHomeChannel  chl : InstlledDeviceSystem.SmartHomeChannels)
        	s+=chl.name+"("+chl.appid+")\r\n";
    	Toast.makeText(MainActivity.this,  "Received Data OK:\r\n"+s , Toast.LENGTH_LONG).show();*/	
    	
    	deviceSystemadapter=new DeviceSystemAdapter(MainActivity.this,R.layout.devicesystem_layout,InstlledDeviceSystem.SmartHomeChannels);
    	//ListView lv=(ListView)findViewById(R.id.lvDeviceSystem);
    	lvDeviceSystem.setAdapter(deviceSystemadapter);
    	selecteHome=null;
    }
    private ScenePlans tasks;
    private TaskAdapter taskadapter;
    private void LoadTask(String task)
    {
    	tasks=new ScenePlans(SHProtocol.SHFLAG,this,workdir,task); //    public ScenePlans(int _Flag,Context context,String workdir,String _FileName)
        /*String s="";  //调试用
        for(ScenePlansItem  item : tasks.Items)
        	s+=item.PlanFileName+"("+item.Time()+")\r\n";
    	Toast.makeText(MainActivity.this,  "Received TaskFile OK:\r\n"+s , Toast.LENGTH_LONG).show();*/	    	
    	taskadapter=new TaskAdapter(MainActivity.this,R.layout.task_layout, tasks.Items);
    	//ListView lv=(ListView)findViewById(R.id.lvTask);
    	lvTask.setAdapter(taskadapter);  	
    	selectedPlansItem=null;
    }
    
    int GetFileSzie(String fn) //获取文件大小
    {
       /* int len = 0;
        if (!File.Exists(fn)) return 0;
        FileStream fs = new FileStream(fn, FileMode.Open,FileAccess.Read );
        len = (int)fs.Length;
        fs.Dispose();
        return len; */
    	return 0;
    }
    
}
