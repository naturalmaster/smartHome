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
	private Button btnExcuteTask;		//ִ������
	private Button btnMessage;		   //����
	private Button btnSetTask;  			//��������
	private Button btnSetAlarm;       //��������
	private Button btnExit;	
	private Button btnChangeMonitor;		

	private Button btnLoadDevice;  	//�����豸
	private Button btnLoadTask;  		//��������
	private Button btnLoadAlarm;    	//���±���
	private Button btnLoadPicture;   //����ͼƬ	
	
	private Button btnStartDevice;		//�����豸
	private Button btnStopDevice;		//�����豸
	
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
	
	private void SetButtonClickEvent()  //���ð�ť��Ӧ�¼�
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
		btnSetTask.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{
				Intent newintent=new Intent(MainActivity.this,TaskSetActivity.class);
		        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼
                startActivity(newintent);
			}
		});
	}
	private void SetbtnSetAlarmClick()
	{
		btnSetAlarm.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{
				Intent newintent=new Intent(MainActivity.this,MonitorSetActivity.class);
		        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼
                startActivity(newintent);
			}
		});
	}
	/*private void SetbtnMessageClick()
	{
		btnMessage.setOnClickListener(new View.OnClickListener()   //����
		{			
			@Override
			public void onClick(View v)
			{ 
				final EditText input = new EditText(MainActivity.this);  
				input.setBackground( getResources().getDrawable(R.drawable.shape));	 	
				//input.setTop(24);
				
				AlertDialog.Builder ad1  = new AlertDialog.Builder(MainActivity.this);  
				ad1.setTitle("������Ϣ");  
		        ad1.setIcon(android.R.drawable.ic_dialog_info);  
		        ad1.setView(input);  
		        ad1.setPositiveButton("����", new DialogInterface.OnClickListener() 
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
		        ad1.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() 
		              {  public void onClick(DialogInterface dialog, int i) {      }    });  
				
		        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
				//		ViewGroup.LayoutParams.WRAP_CONTENT);
				//layoutParams.setMargins(16,16,16,16);//4��������˳��ֱ�����������
				//input.setLayoutParams(layoutParams); 
				
		        ad1.show();// ��ʾ�Ի���  
			}
		});
	}  */
	
	private void SetbtnMessageClick()
	{
		btnMessage.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
				{			
					@Override
					public void onClick(View v)
					{
						Intent newintent=new Intent(MainActivity.this,MessageActivity.class);
				        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼
		                startActivity(newintent);
					}
				});	
	}
	
	ScenePlansItem selectedPlansItem=null;
	SmartHomeChannel selecteHome=null;
	private void SetbtnExcuteTaskClick()
	{
		btnExcuteTask.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{  //RUNTASK = "509";    //֪ͨSHSִ��ĳ������[task=fn][timed=1][starttime=second]
				if (selectedPlansItem==null) return;
				//adapter.setSelectItem(arg2);  
				if (!selectedPlansItem.Used)
				{
				  Toast.makeText(MainActivity.this,  "�����񳡾�����ִֹ��" , Toast.LENGTH_LONG).show();
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
		btnChangeMonitor.setOnClickListener(new View.OnClickListener()   //���û�ȡ�����
		{			
			@Override
			public void onClick(View v)
			{ 
				  SetAlarm(setalarm?false:true);
			}
		});
	}

	private Boolean setalarm=false;
    private void SetAlarm(Boolean setalarm)  //�����������ָ��
   {
       // if (!bCanSetAlarm) { txtOut.Text = "��û���������Ȩ��"; return; }
        StringJson json = new StringJson(SmartHomeChannel.SHFLAG);
        //SETALARM = "515";   ���� ���/���� [set="1/0"]   ��ȡ���/����[value="1/0"]
        json.AddNameVolume("cmd", SHProtocol.SETALARM);
        json.AddNameVolume("set", setalarm ? "1" : "0");
        TcpSend(json);
    }

   private void GetAlarmSetting()  //��ȡ���״̬S
    {
        StringJson json = new StringJson(SmartHomeChannel.SHFLAG);
        //SETALARM = "515";   ���� ���/���� [set="1/0"]   ��ȡ���/����[value="1/0"]
        json.AddNameVolume("cmd", SHProtocol.SETALARM);
    	TcpSend(json);
    }
    
	private void  SetbtnStartDeviceClick()  //�����豸ϵͳ
	{
		btnStartDevice.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{
				if (selecteHome==null) return;
				StringJson json=new StringJson(SHProtocol.SHFLAG);				 
	            //STARTAPP = "502";   //֪ͨ���������SHM������ҪȨ��[appid=?][start=1][rights=NNNNNNNNNNNNNNN]
	            json.AddNameVolume("cmd", SHProtocol.STARTAPP);
	            json.AddNameVolume("appid", selecteHome.appid+"");
	            json.AddNameVolume("start", "1");
				TcpSend(json);
			}
		});
	}
	private void  SetbtnStopDeviceClick()  //�����豸ϵͳ
	{
		btnStopDevice.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{
				if (selecteHome==null) return;
				StringJson json=new StringJson(SHProtocol.SHFLAG);				 
	            //STARTAPP = "502";   //֪ͨ���������SHM������ҪȨ��[appid=?][start=1][rights=NNNNNNNNNNNNNNN]
	            json.AddNameVolume("cmd", SHProtocol.STARTAPP);
	            json.AddNameVolume("appid", selecteHome.appid+"");
	            json.AddNameVolume("start", "0");
				TcpSend(json);
			}
		});
	}
	private void  SetbtnExitClick()  //����
	{
		btnExit.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{
				ActivtyItems.finishAll(); 
			}
		});
	}
	private void  SetbtnLoadDeviceClick()  //�����豸ָ��
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
	private void  SetbtnLoadTaskClick()     //��������ָ��
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
	private void  SetbtnLoadAlarmClick()     //���±���ָ��
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
	private void  SetbtnLoadPictureClick()  //����ͼƬָ��
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
 	private void UpdateTask() //GETTASK = "507";  //��ȡ���ܼҾӵ��������� [task=fn][need="1"][stream=byte[]][index=?]
    {
		if (bdemo) {
			try {
				String filename = "Task.tsk";
				MakeDirectory(workdir); // ������Ŀ¼
				CopyAssetsFile(filename, filename);
			} catch (Exception ex) {
			}
			CopyTaskFile();
			LoadTask("Task.tsk"); 				//װ�������б�
			Toast.makeText(MainActivity.this,  "��ʾ���������Ѿ�����" , Toast.LENGTH_SHORT).show();		
			return; // �����ļ���������ʾ
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
	private void UpdateAlarm() ////GETALARM = "510";   //��ȡ���ܼҾӵļ������ [alarm=fn][need="1"][stream=byte[]][index=?]
    {
		if (bdemo) {
			try {
		    	String filename="Monitor.alm";
				MakeDirectory(workdir);  //������Ŀ¼
				CopyAssetsFile(filename,filename);
			} catch (Exception ex) {
			}
			Toast.makeText(MainActivity.this,  "��ʾ��������Ѿ�����" , Toast.LENGTH_SHORT).show();
			return;  //�����ļ���������ʾ
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

	private void CopyAssetsFile(String sf,String df)  //��assets��ԴĿ¼�µ��ļ����Ƶ�Ӧ�ó���Ŀ¼��wordir
	{
		try {
			InputStream is = this.getAssets().open(sf);  //assets��ԴĿ¼�µ��ļ�
			String filename=this.getFilesDir()+File.separator+workdir+File.separator+df;  //Ӧ�ó���Ŀ¼��wordir���ļ�
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
					lastClickTime = 0;  //ģ��˫���¼�
					if (position>=0)
					{
						Intent newintent=new Intent(MainActivity.this,DeviceControlActivity.class);
				        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼
		                newintent.putExtra("appid", selecteHome.appid);  //���Ǹ���ص� ID       
		                newintent.putExtra("dvname", selecteHome.name);  //���Ǹ���ص� ID       
		                newintent.putExtra("dvdescription", selecteHome.description);  //���Ǹ���ص� ID   
		                startActivity(newintent);
		                return;
					}
				}
				else //���ǵ��ͬһ��,��ʱ�����
				{
					selecteHome=home;
					deviceSystemadapter.setSelectItem(position);  
					deviceSystemadapter.notifyDataSetInvalidated();
					lastClickTime = System.currentTimeMillis();
				}
				StringJson json=new StringJson(SHProtocol.SHFLAG);				 
				//APPSTATE = "501";   //��ȡ����SHM����״̬[stream=�ļ�����][id=appid][state=1/0]
	            json.AddNameVolume("cmd", SHProtocol.APPSTATE);
	            json.AddNameVolume("id", home.appid+"");
	            json.AddNameVolume("state", "1");  //�����ȡ�Ƿ������豸��س���
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
	            //������������
				if (position>=0)
				{
					Intent newintent=new Intent(MainActivity.this,DeviceControlActivity.class);
			        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼
	                newintent.putExtra("appid", selecteHome.appid);  //���Ǹ���ص� ID       
	                newintent.putExtra("dvname", selecteHome.name);  //���Ǹ���ص� ID       
	                newintent.putExtra("dvdescription", selecteHome.description);  //���Ǹ���ص� ID   
	                startActivity(newintent);
	                return true;
				}
				else return false;
			}
		});
	}
	private  void GetAllDeviceSystem() //��ȡ���а�װ���豸ϵͳ�ļ� EagleSmartHome.esh
	{
		if (bdemo) {
			//����ʾ�ĵ�EagleSmartHome.esh�ĵ������Ƶ�workdirĿ¼��
			MakeDirectory(workdir);  //������Ŀ¼		
	       	String filename="EagleSmartHome.esh";
	       	CopyAssetsFile(filename,filename);

			LoadInstalledDeviceSystem();  //���ļ��������а�װ�˵��豸ϵͳ
			//�Ѷ�Ӧ���豸ϵͳ�������ļ����Ƶ���Ŀ¼��
			for (int i=0;i<InstlledDeviceSystem.SmartHomeChannels.size();i++)
			{
		        String shmfn = "SmartHome" +InstlledDeviceSystem.SmartHomeChannels.get(i).appid+ ".smh";  //����ĳ�������豸ϵͳ��Ϣ���ļ�
		        ShowHint(shmfn);
		        CopyAssetsFile(shmfn,shmfn);
			}
			this.HideHint();
			Toast.makeText(MainActivity.this,  "��ʾ�豸�����Ѿ�����" , Toast.LENGTH_SHORT).show();
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
  public void TcpSend(StringJson json)    //����TCP����
	{
		if (tcpClient==null) return;
		if (tcpClient.socket==null) return;
		if (!tcpClient.socket.isConnected()) //�Ͽ�������,���µ�¼
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

	private void Relogin() //���µ�¼
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
		localReceiver=new TCPDataReceiver();  //ע�᱾�ع㲥����
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
	     	//Toast.makeText(MainActivity.this, "��ص㣺\r\n"+ workdir, Toast.LENGTH_SHORT).show();	
	     	MakeDirectory(workdir);  //������Ŀ¼
		}
		LoadInstalledDeviceSystem(); 	//װ���豸ϵͳ
		LoadTask("Task.tsk"); 				//װ�������б�
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
		//Toast.makeText(MainActivity.this, "��ص㣺\r\n"+ file, Toast.LENGTH_SHORT).show();
	}
    
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private void ProcessAPPSTATE(StringJson json)
	{
		 String id = json.GetStrValueByName("id");
		 // APPSTATE = "501";   //��ȡ����SHM����״̬[stream=�ļ�����][id=appid][state=1/0]
         if (id == null)
         {
             String appfn ="EagleSmartHome.esh";  //���������豸ϵͳ��Ϣ���ļ�
             byte[] cont = json.GetValueByName("stream");
             if (cont != null)
             {
             	//Toast.makeText(MainActivity.this,  "Received Data OK:\r\n"+appfn , Toast.LENGTH_SHORT).show();		
                 WriteBytesToFile(appfn, cont);//д���ļ�����
                 //���ļ����¶���APPS
                 LoadInstalledDeviceSystem();            //GetDeviceSystem();
                 devicedownposition=0;
                 SendGetHomeDeviceInfo(devicedownposition++);
             }
             return;
         }
         String state = json.GetStrValueByName("state");
         if (state!=null)  //��س����Ƿ�����
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
         String shmfn = "SmartHome" + id + ".smh";  //����ĳ�������豸ϵͳ��Ϣ���ļ�
         //Toast.makeText(MainActivity.this,  "Received Device Data OK:\r\n"+shmfn , Toast.LENGTH_SHORT).show();
         ShowHint(shmfn);
         byte[] st = json.GetValueByName("stream");
         if (st != null)
             WriteBytesToFile(shmfn, st);//д���ļ����� 
         if(devicedownposition<InstlledDeviceSystem.SmartHomeChannels.size())
         	 SendGetHomeDeviceInfo(devicedownposition++);  //�ٴ������豸ϵͳ��Ϣ�ļ�
         else
     	     txtHint.setVisibility(View.GONE); //������ϣ��ر���ʾ
         //UpdatePicture(); //��Ҫ����ͼƬָ��	��2016-05-05ȡ������ͼ���ܣ���Ϊ����һ��ָ��ִ�У������ã����ݶ�
	}
   private void ProcessUPDATEPICTURE(StringJson json)
   {//    public static final String UPDATEPICTURE = "504";   //����ͼƬ[pic=fn][size=N][need="1"][stream=byte[]]
	   //String need = json.GetStrValueByName("need");
       String size = json.GetStrValueByName("size");
       String pic = json.GetStrValueByName("pic");
       String index = json.GetStrValueByName("index");
       if (pic != null && size != null)  //�յ�ͼƬ��Ϣ
       {
           int nsize = Integer.parseInt(size);         //1�����Ȼ�ȡ����ͼƬ�б�
           int psize = GetFileSzie(pic);        			//2�������ļ��Ĵ�С
           if (nsize == psize)   //��С��ȣ��������
           {
               StringJson js=new StringJson(SHProtocol.SHFLAG);
      		   js.AddNameVolume("cmd",SHProtocol.UPDATEPICTURE);
      		   js.AddNameVolume("pic",pic);
      		   js.AddNameVolume("index",index);
               js.AddNameVolume("need", "0");
               TcpSend(js);	 
           }
           else //��Ҫ����  //����ͼƬ[pic=fn][size=N][need="1"][stream=byte[]]
           {
               ShowHint("������ͼƬ......"+index);
        	   StringJson js=new StringJson(SHProtocol.SHFLAG);
      		   js.AddNameVolume("cmd",SHProtocol.UPDATEPICTURE);
      		   js.AddNameVolume("pic",pic);
      		   js.AddNameVolume("index",index);
               js.AddNameVolume("need", "1");
               TcpSend(js);	  
           }
       }
       else  //�յ�ͼƬ�ļ�����
       {
           byte[] st = json.GetValueByName("stream");
           if (st != null)
           {
               WriteBytesToFile(pic, st); //д���ļ�����
               ShowHint(pic);
           	   //Toast.makeText(MainActivity.this,  "Received Picture OK:\r\n"+pic , Toast.LENGTH_SHORT).show();
           }
  		   StringJson js=new StringJson(SHProtocol.SHFLAG);
  		   js.AddNameVolume("cmd",SHProtocol.UPDATEPICTURE);
           int nindex = Integer.parseInt(index) + 1;  //��һ��ͼƬ
           js.AddNameVolume("index", nindex+"");
           js.AddNameVolume("need", "1");
           TcpSend(js);	  
           ShowHint("������ͼƬ......"+nindex);
           // txtHint.setVisibility(View.GONE); //������ϣ��ر���ʾ
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
           WriteBytesToFile(task, st);    //д���ļ�����
           ShowHint("Received TaskFile OK:"+task);
         	//Toast.makeText(MainActivity.this,  "Received TaskFile OK:\r\n"+task , Toast.LENGTH_LONG).show();	         
         	if(task.equals("Task.tsk"))   //���ļ����¶���
         		LoadTask(task);       
         	//if(task.equals("TimedTask.ttsk"))   //���ļ����¶���
         		//LoadTask(task);   
       } ////��ȡ���ܼҾӵ��������� [task=fn][need="1"][stream=byte[]][index=?]
       StringJson js=new StringJson(SHProtocol.SHFLAG);
	   js.AddNameVolume("cmd",SHProtocol.GETTASK);
       int nindex = Integer.parseInt(index) + 1;  //��һ��ͼƬ
       js.AddNameVolume("index", nindex+"");
       js.AddNameVolume("need", "1");
       TcpSend(js);	  
       ShowHint("����������......"+nindex);
   }
   private void ProcessGETALARM(StringJson json)
   {
       String alarm = json.GetStrValueByName("alarm"); if (alarm == null) return;
       String index = json.GetStrValueByName("index"); if (index == null) return;
       byte[] st = json.GetValueByName("stream");
       if (st != null)
       {
           WriteBytesToFile(alarm, st);//д���ļ�����
           ShowHint("Received AlarmFile OK:"+alarm);     
          //	Toast.makeText(MainActivity.this,  "Received Monitor File OK:\r\n"+alarm , Toast.LENGTH_SHORT).show();		
       }
       StringJson js=new StringJson(SHProtocol.SHFLAG);
	   js.AddNameVolume("cmd",SHProtocol.GETALARM);
       int nindex = Integer.parseInt(index) + 1;  //��һ��ͼƬ
       js.AddNameVolume("index", nindex+"");
       js.AddNameVolume("need", "1");
       TcpSend(js);	  
       ShowHint("�����ͼ������......"+nindex);
   }

   private TCPDataReceiver localReceiver;
	class TCPDataReceiver extends BroadcastReceiver   //������յ�������
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{		
			byte[] data=intent.getByteArrayExtra("json");
			StringJson json=StringJson.BytesToStringJson(data, SHProtocol.SHFLAG);
			//StringJson json=TcpClient.GetStringJson();
			String cmd=json.GetStrValueByName("cmd");
           	//Toast.makeText(MainActivity.this, this.toString()+"\r\n"+ json.toString(), Toast.LENGTH_SHORT).show();		
            if (cmd.equals(SHProtocol.APPSTATE)) //��ȡ����SHM����״̬[stream=�ļ�����][id=0],��id��ȡ��APP�ļ�EagleSmartHome.esh��
            {
            	ProcessAPPSTATE(json);
            }
			else if (cmd.equals(SHProtocol.UPDATEPICTURE))//UPDATEPICTURE = "504";����ͼƬ[pic=fn][size=N][need="1"][stream=byte[]]
			{
				 ProcessUPDATEPICTURE(json);
				 return;
			}
			else if (cmd.equals(SHProtocol.GETTASK)) //GETTASK = "507";    //��ȡ���ܼҾӵ��������� [task=fn][need="1"][stream=byte[]][index=?]
			{
				ProcessGETTASK(json);				
	        }
			else if (cmd.equals(SHProtocol.GETALARM)) //GETALARM = "510";   //��ȡ���ܼҾӵļ������ [alarm=fn][need="1"][stream=byte[]][index=?]
			{
				ProcessGETALARM(json);
			}
			 else if (cmd.equals(SHProtocol.TEXT))  //SHA����SHM��SHS��CLIENT���ı�֪ͨ��Ϣ[text=?]
		         {
					 String text = json.GetStrValueByName("text");
					 Toast.makeText(MainActivity.this,  text , Toast.LENGTH_SHORT).show();	 
					 HideHint(); //�յ��ı���ʾ�󣬹ر��ı���ʾ��
		         }
			 else if (cmd.equals(SHProtocol.ERRHINT))  //SHA����SHM��SHS��CLIENT���ı�֪ͨ��Ϣ[text=?]
		     {
				 String text = json.GetStrValueByName("err");
				 Toast.makeText(MainActivity.this,  text , Toast.LENGTH_SHORT).show();	 
				 HideHint();
		     }    
			 else if (cmd.equals(SHProtocol.TESTCONECTION))  // ͨ�Ų����Ƿ�����:�ͻ����յ���ԭ���ظ���û������
		     {
		         TcpSend(json);		
		     }    
			 else if (cmd.equals(SHProtocol.SETALARM))  // SETALARM = "515";   //���� ���/���� [set="1/0"]   ��ȡ���/����[value="1/0"]
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

    private	void WriteBytesToFile(String appfn, byte[] cont)//д���ļ�����
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
			writer.flush();  //�ӿ챣��
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
	private void  UpdatePicture() //��Ҫ����ͼƬָ��
	{
		if (bdemo)
		{
			CopyPicture();
			Toast.makeText(MainActivity.this,  "��ʾͼƬ�����Ѿ�����" , Toast.LENGTH_SHORT).show();
			return;
		}
        ShowHint("����ͼƬ����......");
		 StringJson json=new StringJson(SHProtocol.SHFLAG);
	    json.AddNameVolume("cmd", SHProtocol.UPDATEPICTURE);
	    json.AddNameVolume("need", "1");
	    TcpSend(json);		
	}
    private SmartHomeApps InstlledDeviceSystem; //��װ�˵��豸ϵͳ
    private DeviceSystemAdapter deviceSystemadapter;
    private void LoadInstalledDeviceSystem()  //���ļ��������а�װ�˵��豸ϵͳ
    {
         InstlledDeviceSystem = new SmartHomeApps(this,workdir,"EagleSmartHome.esh");
        /*String s="";  //������
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
        /*String s="";  //������
        for(ScenePlansItem  item : tasks.Items)
        	s+=item.PlanFileName+"("+item.Time()+")\r\n";
    	Toast.makeText(MainActivity.this,  "Received TaskFile OK:\r\n"+s , Toast.LENGTH_LONG).show();*/	    	
    	taskadapter=new TaskAdapter(MainActivity.this,R.layout.task_layout, tasks.Items);
    	//ListView lv=(ListView)findViewById(R.id.lvTask);
    	lvTask.setAdapter(taskadapter);  	
    	selectedPlansItem=null;
    }
    
    int GetFileSzie(String fn) //��ȡ�ļ���С
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
