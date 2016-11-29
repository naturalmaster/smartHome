package com.eagle.smarthome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.eagle.smarthome.Device.DeviceAI;
import com.eagle.smarthome.Device.DeviceAO;
import com.eagle.smarthome.Device.DeviceDI;
import com.eagle.smarthome.Device.DeviceDO;
import com.eagle.smarthome.Device.DeviceSI;
import com.eagle.smarthome.Device.DeviceSO;
import com.eagle.smarthome.Device.DeviceType;
import com.eagle.smarthome.Device.HomeDevice;
import com.eagle.smarthome.Device.SmartHome;
import com.eagle.smarthome.Device.SmartHomeApps;

import com.eagle.smarthome.Alarm.AlarmItem;
import com.eagle.smarthome.Alarm.MonitorAlarm;
import com.eagle.smarthome.Alarm.MonitorAlarms;

import com.eagle.smarthome.task.ScenePlans;
import com.eagle.smarthome.task.ScenePlansItem;

import com.eagle.smarthome.util.SHProtocol;
import com.eagle.smarthome.util.SmartHomeChannel;
import com.eagle.smarthome.util.SmartHomes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MonitorSetActivity extends BaseActivity
{	
	private String workdir="";
	private TextView monitorhome;
	private ImageView imgBack3;
	private ImageView imgAddMonitorProject;
	private ImageView imgDeleteMonitorProject;
	private ImageView imgSaveMonitorProject;
	
	private ImageView imgAddMonitor;
	private ImageView imgDeleteMonitor;
	private ImageView imgSaveMonitor;
	private ImageView imgSendMonitor;

	private Spinner spdevicesystem;
	private Spinner spdevice;
	private Spinner spsubdevice;
	private Spinner spcondition;
	private EditText editpara;
	private Spinner sptask;
	private Spinner spprefix;
	private Spinner spsuffix;
	private Spinner spcombine;

	private Spinner	spmonitorproject;
	private Spinner spmonitoritems;
    private  CheckBox  chkdisarm;
    private  CheckBox  chkused; 
    
	private TextView edexpression;    
    private TextView tvExpressionHint;
    
	private void GetUIName()
	{
		try
		{
			tvExpressionHint=(TextView)findViewById(R.id.tvExpressionHint);	
			monitorhome=(TextView)findViewById(R.id.monitorhome);	
			imgBack3=(ImageView) findViewById(R.id.imgBack3);	 		
			imgAddMonitor=(ImageView) findViewById(R.id.imgAddMonitor);	 
			imgDeleteMonitor=(ImageView) findViewById(R.id.imgDeleteMonitor);	 			
			imgSaveMonitor=(ImageView) findViewById(R.id.imgSaveMonitor);	 
			imgSendMonitor=(ImageView) findViewById(R.id.imgSendMonitor);	 
			
			imgAddMonitorProject=(ImageView) findViewById(R.id.imgAddMonitorProject);	 
			imgDeleteMonitorProject=(ImageView) findViewById(R.id.imgDeleteMonitorProject);	 			
			imgSaveMonitorProject=(ImageView) findViewById(R.id.imgSaveMonitorProject);	 
			
			spdevicesystem=(Spinner) findViewById(R.id.spdevicesystem);	 	
			spdevice=(Spinner) findViewById(R.id.spdevice);	 	
			spsubdevice=(Spinner) findViewById(R.id.spsubdevice);	 	
			spcondition=(Spinner) findViewById(R.id.spcondition);	 	
			spmonitorproject=(Spinner) findViewById(R.id.spmonitorproject);	 	
			sptask=(Spinner) findViewById(R.id.sptask);	 			
			spmonitoritems=(Spinner) findViewById(R.id.spmonitoritems);	 
			spprefix=(Spinner) findViewById(R.id.spprefix);	 
			spsuffix=(Spinner) findViewById(R.id.spsuffix);	 
			spcombine=(Spinner) findViewById(R.id.spcombine);	 
			
			editpara=(EditText) findViewById(R.id.editpara);	 
			edexpression=(TextView) findViewById(R.id.edexpression);	 

			chkdisarm=(CheckBox)findViewById(R.id.chkdisarm);	
			chkused=(CheckBox)findViewById(R.id.chkused);	
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(MonitorSetActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}
	private void SetButtonClickEvent()  //���ð�ť��Ӧ�¼�
	{
		try
		{
			SetimgBackClick();
			SetimgSaveMonitorClick();
			SetimgSaveMonitorProjectClick();
			
			SetimgSendMonitorClick();  //���͸�������
			SetimgAddMonitorClick();  //����һ���������
			SetimgDeleteMonitorClick();  //ɾ��һ����ؼ�¼

			SetimgAddMonitorProjectClick();  //����һ�������Ŀ
			SetimgDeleteMonitorProjectClick();  //ɾ��һ�������Ŀ
			
			SetspmonitorprojectOnSelect(); //ѡ��һ�������Ŀ
			SetspmonitoritemsOnSelect();   //ѡ��һ���������
			SetspdevicesystemOnSelect();
			SetspdeviceOnSelect();
			SetspsubdeviceOnSelect();
			SetspconditionOnSelect();
			SetsptaskOnSelect();
			SetspprefixOnSelect();
			SetspsuffixOnSelect();
			SetspcombineOnSelect();
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(MonitorSetActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}	
	
	private void SetimgDeleteMonitorProjectClick()  //ɾ��һ�������Ŀ
	{
		imgDeleteMonitorProject.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if(selectedMonitorProject<0) return;
				MonitorAlarm monitoritem=monitorprojects.MonitorItems.get(selectedMonitorProject);			//��ǰ�����Ŀ			

				AlertDialog.Builder ad=new AlertDialog.Builder(MonitorSetActivity.this);
				ad.setTitle(getResources().getString(R.string.suredelete));
				ad.setMessage(getResources().getString(R.string.suredeletemonitor)+"\r\n"+
						"��"+(selectedMonitorProject+1) +"�������Ŀ��"+monitoritem.TaskName);
				
				ad.setPositiveButton(getResources().getString(R.string.delete), new OnClickListener()
				{
					public void onClick(DialogInterface dialog,int arg)
					{
						monitorprojects.MonitorItems.remove(selectedMonitorProject);  //ɾ��
						monitorprojects.SaveToFile();  //����
						LoadMonitorProjects("Monitor.alm");     //����װ�����м����Ŀ
				    	if (mMonitorProjectAdapter.getCount()>0)
				    		spmonitorproject.setSelection(0);  //ѡ�е�һ��    	
					}
				});
				
				ad.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener()
				{
					public void onClick(DialogInterface dialog,int arg) {   }
				});
				ad.setCancelable(true);
				ad.show();
			}
		});
	}
	private void SetimgAddMonitorProjectClick()  //����һ�������Ŀ
	{
		imgAddMonitorProject.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				//if(selectedMonitorProject<0) return;
				MonitorAlarm monitoritem=new MonitorAlarm();
				monitorprojects.MonitorItems.add(monitoritem); //�����¼����Ŀ
				monitorprojects.SaveToFile();  //����
				LoadMonitorProjects("Monitor.alm");     //����װ�����м����Ŀ
		    	if (mMonitorProjectAdapter.getCount()>0)
		    		spmonitorproject.setSelection(mMonitorProjectAdapter.getCount()-1);  //ѡ���������һ����Ŀ    	
				Toast.makeText(MonitorSetActivity.this,getResources().getString(R.string.addmonitorprojecthint), Toast.LENGTH_SHORT).show();	
			}
		});
	}
	
	
	private void SetimgDeleteMonitorClick()  //ɾ��һ����ؼ�¼
	{
		imgDeleteMonitor.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if(selectedMonitorProject<0) return;
				if(selectedAlarmItemrow<0l) return;				

				AlertDialog.Builder ad=new AlertDialog.Builder(MonitorSetActivity.this);
				ad.setTitle(getResources().getString(R.string.suredelete));
				ad.setMessage(getResources().getString(R.string.suredeletemonitor)+":\r\n"+selectedAlarmItem.info()+selectedAlarmItem.Expression());
				
				ad.setPositiveButton(getResources().getString(R.string.delete), new OnClickListener()
				{
					public void onClick(DialogInterface dialog,int arg)
					{
						MonitorAlarm monitoritem=monitorprojects.MonitorItems.get(selectedMonitorProject);			//��ǰ�����Ŀ				
						monitoritem.Items.remove(selectedAlarmItemrow);  //ɾ��
						LoadMonitors(monitoritem);  //���³���
						selectedAlarmItemrow=monitoritem.Items.size()-1;
						if (selectedAlarmItemrow<monitoritem.Items.size())
							spmonitoritems.setSelection(selectedAlarmItemrow);
						else 
							spmonitoritems.setSelection(monitoritem.Items.size()-1);
						}
				});
				
				ad.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener()
				{
					public void onClick(DialogInterface dialog,int arg) {   }
				});
				ad.setCancelable(true);
				ad.show();
			}
		});
	}
	private void SetimgAddMonitorClick()  //����һ����ؼ�¼
	{
		imgAddMonitor.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if(selectedMonitorProject<0) return;
				//if(selectedAlarmItemrow<0l) return;
				MonitorAlarm monitoritem=monitorprojects.MonitorItems.get(selectedMonitorProject);			//��ǰ�����Ŀ	
				AlarmItem item=new AlarmItem();
				monitoritem.Items.add(item);  //�����¼������
				LoadMonitors(monitoritem);  //���³���
				selectedAlarmItemrow=monitoritem.Items.size()-1;
				spmonitoritems.setSelection(selectedAlarmItemrow);
				Toast.makeText(MonitorSetActivity.this,getResources().getString(R.string.addmonitorhint), Toast.LENGTH_SHORT).show();	
			}
		});
	}
	

	private void SetimgSendMonitorClick()  //���͸���
	{
		imgSendMonitor.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				StringJson json = new StringJson(SmartHomeChannel.SHFLAG);
	            //MENDALARM = "511";  //�޸�SHS���ܼҾӵļ���ļ�[alarm=fn][stream=byte[]]
	            json.AddNameVolume("cmd", SHProtocol.MENDALARM);
	            json.AddNameVolume("alarm", "Monitor.alm");
	            String filename=getFilesDir()+File.separator+workdir;
	     		File file = new File(filename); 
	     		File file2=new File(file.getAbsoluteFile() +File.separator+"Monitor.alm");
	     	  	if (!file2.exists())  return;
	     	  	FileInputStream br =null;
	     	  	try
	     	  	{
						br = new FileInputStream(file2);
						int size=(int)file2.length();
						byte[] nr=new byte[size];
						try {
							br.read(nr,0,size);
					        json.AddNameVolume("stream", nr); 
					        br.close();
					        if (tcpClient.SendMessage(json))
					    	    Toast.makeText(MonitorSetActivity.this,getResources().getString(R.string.sendalarmfileok), Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
	     	  	catch (FileNotFoundException e)
	     	  	{				
						e.printStackTrace();						
				}     	
			}
		});
	}
	private void SetimgSaveMonitorClick()  //�����޸ĵ�����
	{
		imgSaveMonitor.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if(selectedMonitorProject<0) return;
				if(selectedAlarmItemrow<0l) return;
				MonitorAlarm monitoritem=monitorprojects.MonitorItems.get(selectedMonitorProject);				
				/*monitoritem.CanDisarm=chkdisarm.isChecked();
				monitoritem.Used=chkused.isChecked();
				int n=sptask.getSelectedItemPosition();
				if (n>=0) monitoritem.TaskName=taskarray.get(n); */
				editalarmitem.value= editpara.getText().toString();     //������������
				AlarmItem.Copy(editalarmitem, selectedAlarmItem);   //ת�Ƶ��������
				//monitorprojects.SaveToFile();				
				//LoadMonitors(monitoritem);
		    	monitoritemarray.set(selectedAlarmItemrow, selectedAlarmItem.Expression());  //�޸ļ����������
				mMonitorItemAdapter.notifyDataSetChanged();  //������ʾ
				edexpression.setText(monitoritem.GetMonitorExpression());
				showbracksOK(monitoritem);
				Toast.makeText(MonitorSetActivity.this,getResources().getString(R.string.modifyitemok), Toast.LENGTH_SHORT).show();		
			}
		});
	}
	
	private void showbracksOK(MonitorAlarm monitoritem)
	{
		int br=monitoritem.CheckBracketsLegal();
		 if (br > 0)
         {
			 tvExpressionHint.setText("��ر��ʽȱ��������)��" + br + "��");
			 tvExpressionHint.setTextColor(Color.YELLOW);
         }
         else if (br < 0)
         {
        	 tvExpressionHint.setText("��ر��ʽȱ��������(��" + (-br) + "��");
             tvExpressionHint.setTextColor(Color.YELLOW);
         }
         else
         {
        	 tvExpressionHint.setText("��ر��ʽ��ȷ");
             tvExpressionHint.setTextColor(Color.WHITE);
         }
	}
	private void SetimgSaveMonitorProjectClick()
	{
		imgSaveMonitorProject.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if(selectedMonitorProject<0) return;
				//if(selectedAlarmItemrow<0l) return;
				MonitorAlarm monitoritem=monitorprojects.MonitorItems.get(selectedMonitorProject);
				int index=sptask.getSelectedItemPosition();  //this.mAdapter = new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, taskarray);
				if (index>=0)	monitoritem.TaskName=taskarray.get(index);
				monitoritem.CanDisarm=chkdisarm.isChecked();
				monitoritem.Used=chkused.isChecked();
				int n=sptask.getSelectedItemPosition();
				if (n>=0) monitoritem.TaskName=taskarray.get(n);
				monitorsarray.set(selectedMonitorProject, monitoritem.TaskName);
				monitorprojects.SaveToFile();
				mMonitorProjectAdapter.notifyDataSetChanged();
				edexpression.setText(monitoritem.GetMonitorExpression());
				Toast.makeText(MonitorSetActivity.this,getResources().getString(R.string.modifyok), Toast.LENGTH_SHORT).show();		
			}
		});
	}
		
	
	private void SetimgBackClick()  //���˰�ť
	{
		imgBack3.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{  
				finish();
			}
		});
	}
	private TcpClient tcpClient;
	private List<String> prefixs;
	private List<String> suffixs;
	private List<String> combines;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)   //�����
	{
		super.onCreate(savedInstanceState);
		tcpClient=TcpClient.GetTcpClient();
		setContentView(R.layout.monitor_set_layout);
		GetUIName();
		SetButtonClickEvent();
		
		Intent intent=this.getIntent();
		String dir=intent.getStringExtra("workdir");		
		monitorhome.setText(dir+" - "+getResources().getString(R.string.monitorset));
		if (dir!=null) workdir=dir;		
		LoadAllHomeDevice();    				//װ�����ܼҾӵ������豸ϵͳ
		LoadAllSceneTasks("Task.tsk"); 	//װ�������б�
		LoadContindion();
		
		LoadMonitorProjects("Monitor.alm");     //װ�����м����Ŀ
    	if (mMonitorProjectAdapter.getCount()>0)
    		spmonitorproject.setSelection(0);  //ѡ�е�һ��    		
	}

	private void LoadContindion()
	{
		prefixs=new Vector<String>();
		String[] s=this.getResources().getStringArray(R.array.prefix);
		for (int i=0;i<s.length; i++) prefixs.add(s[i]);
		prefixs.add(" ");
		ArrayAdapter<String> mAdapter= new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, prefixs);		
		this.spprefix. setAdapter(mAdapter);
		
		suffixs=new Vector<String>();
		s=this.getResources().getStringArray(R.array.suffix);
		for (int i=0;i<s.length; i++) suffixs.add(s[i]);
		suffixs.add(" ");
		mAdapter= new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, suffixs);		
		this.spsuffix. setAdapter(mAdapter);
		
		combines=new Vector<String>();
		s=this.getResources().getStringArray(R.array.combine);
		for (int i=0;i<s.length; i++) combines.add(s[i]);
		mAdapter= new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, combines);		
		this.spcombine. setAdapter(mAdapter);
	}
	@Override
	protected void onResume()
	{  
		super.onResume();
	}
	
	private SmartHomes homes;
	private SmartHomeApps InstlledDeviceSystem; //��װ�˵��豸ϵͳ
	private List<String> homedevicesystems=null;	    //�����豸ϵͳ�б�
	protected ArrayAdapter<String> homedevicesystemadapter;
	private void LoadAllHomeDevice()  //װ�����ܼҾӵ������豸ϵͳ
	{
		homes=new SmartHomes();
        InstlledDeviceSystem = new SmartHomeApps(this,workdir,"EagleSmartHome.esh");
        homedevicesystems=new Vector<String>();
        for (int i=0; i<InstlledDeviceSystem.SmartHomeChannels.size(); i++)
        {
        	SmartHome smarthome=new SmartHome(this, workdir, "SmartHome"+InstlledDeviceSystem.SmartHomeChannels.get(i).appid+".smh");
        	homes.smarthomes.add(smarthome);
        	int appid=InstlledDeviceSystem.SmartHomeChannels.get(i).appid;
        	homes.appids.add(appid);
        	homedevicesystems.add(appid+"("+InstlledDeviceSystem.SmartHomeChannels.get(i).description+" - " +smarthome.homename+")");
        }
        homedevicesystemadapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, homedevicesystems);
    	this.spdevicesystem.setAdapter(homedevicesystemadapter);
	}
	
	//**************װ�����г��������б�*************************//
	private List<String> taskarray=null;	    //�����б�
    private ScenePlans alltasks;  				//���������б�
	protected ArrayAdapter<String> mAdapter;    
    private void LoadAllSceneTasks(String task) //װ�����г��������б�
    {
    	 alltasks=new ScenePlans(SHProtocol.SHFLAG,this,workdir,task); //    public ScenePlans(int _Flag,Context context,String workdir,String _FileName)
    	 taskarray=new Vector<String>();
    	 for (int i=0; i<alltasks.Items.size(); i++ )
   	     {
    		 ScenePlansItem item=alltasks.Items.get(i);
   	    	 String s=item.PlanFileName;  //+"("+item.StartTime()+" - "+item.EndTime()+")";
   	    	 taskarray.add(s);
   	     }
    	this.mAdapter = new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, taskarray);
    	this.sptask.setAdapter(mAdapter);
    }

	//******************װ�����м����Ŀ**************************//
    private List<String> monitorsarray=null;	    //�����Ŀ�б�
    private MonitorAlarms monitorprojects;
    protected ArrayAdapter<String> mMonitorProjectAdapter;    
    private int selectedMonitorProject;  //ѡ�еļ����Ŀ
    private void LoadMonitorProjects(String monitorfile)
    {
    	monitorprojects=new MonitorAlarms(SHProtocol.SHFLAG,this,workdir,monitorfile); 
    	monitorsarray=new Vector<String>();
    	 for (int i=0; i<monitorprojects.MonitorItems.size(); i++ )
   	     {
    		 MonitorAlarm item=monitorprojects.MonitorItems.get(i);
   	    	 String s=item.TaskName;
   	    	 monitorsarray.add(s);
   	     }
    	this.mMonitorProjectAdapter = new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, monitorsarray);
    	this.spmonitorproject.setAdapter(mMonitorProjectAdapter);    	//�����Ŀ������
    	selectedMonitorProject=-1;
    }
    
	//******************װ��һ�������Ŀ�����м������**************************//
    private List<String> monitoritemarray=new Vector<String>();	    //�����Ŀ�����б�
    protected ArrayAdapter<String> mMonitorItemAdapter;        
    private void LoadMonitors(MonitorAlarm monitors)
    {
    	monitoritemarray.clear();
		sptask.setSelection( taskarray.indexOf(monitors.TaskName) );  //����
	   this.chkdisarm.setChecked(monitors.CanDisarm);
	   this.chkused.setChecked(monitors.Used);
		
   	   for (int i=0; i<monitors.Items.size(); i++ )
	     {
		     AlarmItem item=monitors.Items.get(i);
	    	 String s=item.Expression();
	    	 monitoritemarray.add(s);
	     }   	 
     	mMonitorItemAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, monitoritemarray);
    	this.spmonitoritems.setAdapter(mMonitorItemAdapter);    	
    	selectedAlarmItemrow=-1;
    }
    
	//****************ѡ��һ�������Ŀ **************************//
    private int  selectedAlarmItemrow=-1;
    private AlarmItem  selectedAlarmItem=null;
	private void SetspmonitorprojectOnSelect()  
	{
		this.spmonitorproject.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				selectedMonitorProject=position;
				if (position<0) return;
				LoadMonitors(monitorprojects.MonitorItems.get(position));  //
				MonitorAlarm monitoritem=monitorprojects.MonitorItems.get(selectedMonitorProject);
				edexpression.setText(monitoritem.GetMonitorExpression());
				showbracksOK(monitoritem);				
				if (mMonitorProjectAdapter.getCount()>0)
					spmonitoritems.setSelection(0);
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}
	
	//****************ѡ��һ���������**************************//	
	private void SetspmonitoritemsOnSelect()
	{
		this.spmonitoritems.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{   
				if (selectedMonitorProject<0) return;   //û��ѡ����Ŀ
				selectedAlarmItemrow=position;
				if (position<0) return;
				selectedAlarmItem=monitorprojects.MonitorItems.get(selectedMonitorProject).Items.get(position);
				AlarmItem.Copy(selectedAlarmItem,editalarmitem);  //����༭�õ����ݼ�¼
				AlarmItemToUI(selectedAlarmItem);		
			}		
		   @Override  
		   public void onNothingSelected(AdapterView<?> parent) {   }  
		});	
	}
	private void AlarmItemToUI(AlarmItem item)  //����һ����ؼ�¼����
	{
		editpara.setText(item.value);  //����
		int index=prefixs.indexOf(item.prefix);
		spprefix.setSelection(index);
		spsuffix.setSelection(suffixs.indexOf(item.suffix));
		spcombine.setSelection(combines.indexOf(item.combine));
		
		int row=selectdevicesystem(item.appId); //�豸ϵͳ
		row=selectonedevicesystem(row, item.devieID);     	//����ĳ���豸ϵͳ�������豸
		row=selectsubinputdevice(row, item.subDevieID); 		//ѡ���������豸
		row=selectoperation(row, item.operate); 			        //ѡ��Ƚϲ�����
	}
	private int 	selectdevicesystem(int appId)  //�����豸ϵͳ
	{
		for (int i=0; i<homedevicesystems.size(); i++)
		{
			String s=homedevicesystems.get(i);
			String[] ss=s.split("\\(");
			if  (ss[0].equals(appId+""))
			{
				this.spdevicesystem.setSelection(i);
				return i;
			}
		}
		return -1;
	}
	
    protected ArrayAdapter<String> devicesadapter;  //�豸��ϵͳ�����������ܰ�������豸
	private Vector<String> devicesystems=new Vector<String>();
	private Vector<HomeDevice> homedevices=new Vector<HomeDevice>();
	private int selectonedevicesystem(int row, int deviceId)  //����ĳ���豸ϵͳ���������豸ϵͳ
	{
		homedevices.clear();
		devicesystems.clear();
		if(row>=0)  //�豸ϵͳ����
		{
			SmartHome smarthome=homes.smarthomes.get(row);
			for (int i=0; i<smarthome.homedevices.size(); i++)  //�����������豸ϵͳ
			{
				HomeDevice homedevice=smarthome.homedevices.get(i);		//һ��ֻ��һ���豸��ϵͳ
				homedevices.add(homedevice);
				String s=homedevice.deviceid+"("+homedevice.devicename+")";
				devicesystems.add(s);   
			}
		}
        this.devicesadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, devicesystems);
		spdevice.setAdapter(devicesadapter);    		
		for (int i=0; i<homedevices.size(); i++)
		{
			if  (homedevices.get(i).deviceid==deviceId)
			{
				this.spdevice.setSelection(i);
				return i;  //�ҵ��豸
			}
		}
		return -1;
	}

	protected ArrayAdapter<String> onesubdeviceadapter;  //һ�������豸�����������ܰ���������豸
	protected Vector<String> subdevices =new Vector<String>();
	private Vector<Object> inputdevices=new Vector<Object>();
	private int selectsubinputdevice(int row,  short subDevieID) 			 //ѡ���������豸
	{		
		 subdevices .clear();
		 inputdevices.clear();
		if (row>=0)
		{
			HomeDevice device=homedevices.get(row);
			 if (device!=null)
		        {
		        	String s="";
		        	for (int i=0; i<device.DIDevices.size(); i++)
		        	{
		        		DeviceDI dv=device.DIDevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);
		        	}
		        	for (int i=0; i<device.AIDevices.size(); i++)
		        	{
		        		DeviceAI dv=device.AIDevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);
		        	}
		        	for (int i=0; i<device.SIDevices.size(); i++)
		        	{
		        		DeviceSI dv=device.SIDevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);        		
		        	}  	
		        }        
			this.onesubdeviceadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, subdevices);
			spsubdevice.setAdapter(onesubdeviceadapter); 
			for (int i=0; i<subdevices.size(); i++)
			{
				String s=subdevices.get(i);
				String[] ss=s.split("\\(");
				if  (ss[0].equals(subDevieID+""))
				{
					this.spsubdevice.setSelection(i);
					return i;
				}
			}
		}
		return -1;
	}
	private int selectallsubdevice(int row,  short subDevieID) 			    //ѡ���������豸i��o
	{		
		 subdevices .clear();
		 inputdevices.clear();
		if (row>=0)
		{
			HomeDevice device=homedevices.get(row);
			 if (device!=null)
		        {
		        	String s="";
		        	for (int i=0; i<device.DIDevices.size(); i++)
		        	{
		        		DeviceDI dv=device.DIDevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);
		        	}
		        	for (int i=0; i<device.AIDevices.size(); i++)
		        	{
		        		DeviceAI dv=device.AIDevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);
		        	}
		        	for (int i=0; i<device.SIDevices.size(); i++)
		        	{
		        		DeviceSI dv=device.SIDevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);        		
		        	}  	
		        	for (int i=0; i<device.DODevices.size(); i++)
		        	{
		        		DeviceDO dv=device.DODevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);
		        	}
		        	for (int i=0; i<device.AODevices.size(); i++)
		        	{
		        		DeviceAO dv=device.AODevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);
		        	}
		        	for (int i=0; i<device.SODevices.size(); i++)
		        	{
		        		DeviceSO dv=device.SODevices.get(i);
		        		inputdevices.add(dv);
		        		s=TaskItemDeviceToString(dv);
		        		subdevices.add(s);        		
		        	}  	
		        }        
			this.onesubdeviceadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, subdevices);
			spsubdevice.setAdapter(onesubdeviceadapter); 
			for (int i=0; i<subdevices.size(); i++)
			{
				String s=subdevices.get(i);
				String[] ss=s.split("\\(");
				if  (ss[0].equals(subDevieID+""))
				{
					this.spsubdevice.setSelection(i);
					return i;
				}
			}
		}
		return -1;
	}

	private String TaskItemDeviceToString(Object device)
	{
		 String s="";
		  if (device instanceof DeviceDI)
		  {
			  DeviceDI dv=(DeviceDI)device;
			  s=dv.id  +"("+dv.devicetype.toString()+" - "+dv.functiondescription+")";
		  }
		  else  if (device instanceof DeviceAI)
		  {
			  DeviceAI dv=(DeviceAI)device;
	  		  s=dv.id  +"("+dv.devicetype.toString()+", "+dv.dotplace+" - "+dv.functiondescription+")";
		  }
		  else  if (device instanceof DeviceSI)
		  {
			  DeviceSI dv=(DeviceSI)device;
	    	  s=dv.id  +"("+dv.devicetype.toString()+", "+dv.streamtype.toString()+" - "+dv.functiondescription+")";
		  }
		  else if (device instanceof DeviceDO )  //2016-05-05
		  {
			  DeviceDO dv=(DeviceDO)device;
			  s=dv.id  +"("+dv.devicetype.toString()+" - "+dv.functiondescription+")";
		  }
		  else  if (device instanceof DeviceAO)
		  {
			  DeviceAO dv=(DeviceAO)device;
	  		  s=dv.id  +"("+dv.devicetype.toString()+", "+dv.dotplace+" - "+dv.functiondescription+")";
		  }
		  else  if (device instanceof DeviceSO)
		  {
			  DeviceSO dv=(DeviceSO)device;
	    	  s=dv.id  +"("+dv.devicetype.toString()+", "+dv.streamtype.toString()+" - "+dv.functiondescription+")";
		  }
		return s;
	}

	protected ArrayAdapter<String> operateadapter;  //һ�������豸�����������ܰ���������豸
	protected Vector<String> operators =new Vector<String>();
	private int selectoperation(int row, String operate) 			 //ѡ��Ƚϲ�����
	{
		operators.clear();
		operators.add("=");
		if (row>=0)
		{
			Object subdevice=inputdevices.get(row);
		    if (subdevice instanceof DeviceAI || subdevice instanceof DeviceAO ||
		    		subdevice instanceof DeviceSI || subdevice instanceof DeviceSO)
			{
				operators.add("<");
				operators.add(">");			    
			}
		}
		this.operateadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, operators);
		this.spcondition.setAdapter(operateadapter); 
		for (int i=0; i<operators.size(); i++)
		{
			if  (operators.get(i).equals(operate))
			{
				this.spcondition.setSelection(i);
				return i;
			}
		}			
		return -1;
	}	
	
	//************************ѡ���豸ϵͳ*************************//
	private AlarmItem editalarmitem=new AlarmItem();         //���ڱ༭�ļ�ؼ�¼
	private void SetspdevicesystemOnSelect()  
	{
		this.spdevicesystem.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				//if (notOnchanged) return;
				if(selectedAlarmItemrow==-1) return;
				String s=homedevicesystems.get(position);
				String[] ss=s.split("\\(");
				try
				{
					int appid=Integer.parseInt(ss[0]);
					editalarmitem.appId=appid;
					int row=position;
					row=selectonedevicesystem(row, editalarmitem.devieID);     	//����ĳ���豸ϵͳ�������豸
					row=selectsubinputdevice(row, editalarmitem.subDevieID); 			 //ѡ���������豸
					row=selectoperation(row, editalarmitem.operate); 			 //ѡ��Ƚϲ�����
				}
				catch(Exception e)
				{    	}	
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}

	//************************ѡ���豸��ϵͳ***************************//
	private void SetspdeviceOnSelect()  
	{
		this.spdevice.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				//if (notOnchanged) return;
				if(selectedAlarmItem==null) return;
				String s=devicesystems.get(position);
				String[] ss=s.split("\\(");
				try
				{
					int deviceid=Integer.parseInt(ss[0]);
					editalarmitem.devieID=deviceid;
					int row=position;
					//row=selectsubinputdevice(row, editalarmitem.subDevieID); 			 //ѡ���������豸
					row=selectallsubdevice(row, editalarmitem.subDevieID); 			 //ѡ���������豸�� 2016-05-05�޸�
					row=selectoperation(row, editalarmitem.operate); 			 //ѡ��Ƚϲ�����
				}
				catch(Exception e)
				{    	}	
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}

	//************************ѡ�����豸***************************//
	private void SetspsubdeviceOnSelect()  
	{
		this.spsubdevice.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				//if (notOnchanged) return;
				if(selectedAlarmItem==null) return;
				String s=subdevices.get(position);
				String[] ss=s.split("\\(");
				try
				{
					int subDevieID=Integer.parseInt(ss[0]);
					editalarmitem.subDevieID=(short)subDevieID;
					Object subdv=inputdevices.get(position);
					 if (subdv instanceof DeviceDI) editalarmitem.deviceType=DeviceType.DI;
					 else   if (subdv instanceof DeviceAI) editalarmitem.deviceType=DeviceType.AI;
					 else   if (subdv instanceof DeviceSI) editalarmitem.deviceType=DeviceType.SI;
					 else  if (subdv instanceof DeviceDO) editalarmitem.deviceType=DeviceType.DO;
					 else   if (subdv instanceof DeviceAO) editalarmitem.deviceType=DeviceType.AO ;
					 else   if (subdv instanceof DeviceSO) editalarmitem.deviceType=DeviceType.SO;
					int row=position;
					row=selectoperation(row, editalarmitem.operate); 			 //ѡ��Ƚϲ�����
				}
				catch(Exception e)
				{    	}	
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}
	//************************ѡ����������***************************//
	private void SetspprefixOnSelect()
	{
		this.spprefix.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				if(selectedMonitorProject<0) return;
				if(selectedAlarmItemrow<0l) return;
				String s=prefixs.get(position);
				editalarmitem.prefix=s;			
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  		
	}
	//************************ѡ�����Ƚ�������***************************//
	private void SetspsuffixOnSelect()
	{
		this.spsuffix.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				if(selectedMonitorProject<0) return;
				if(selectedAlarmItemrow<0l) return;
				String s=suffixs.get(position);
				editalarmitem.suffix=s;			
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  		
	}
	
	//************************ѡ��������Ϸ�ʽ**************************//
	private void SetspcombineOnSelect()
	{
		this.spcombine.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				if(selectedMonitorProject<0) return;
				if(selectedAlarmItemrow<0l) return;
				String s=combines.get(position);
				editalarmitem.combine=s;			
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  		
	}
	//************************ѡ������ȽϷ���**************************//
	private void SetspconditionOnSelect()  
	{
		this.spcondition.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				//if (notOnchanged) return;
				if(selectedAlarmItem==null) return;
				String s=operators.get(position);
				editalarmitem.operate=s;
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}
	
	//************************ѡ������**************************//
	private void SetsptaskOnSelect()  
	{
		this.sptask.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
		{  
			@Override  
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
			{  
				//if (notOnchanged) return;
				if(selectedAlarmItem==null) return;
				String s=taskarray.get(position);
				//editalarmitem.taskName=s;
		    }  
		   @Override  
		    public void onNothingSelected(AdapterView<?> parent) {   }  
		 });  
	}
		
}
