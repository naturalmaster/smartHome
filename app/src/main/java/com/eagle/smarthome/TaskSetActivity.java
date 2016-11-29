package com.eagle.smarthome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.eagle.smarthome.Device.SmartHome;
import com.eagle.smarthome.Device.SmartHomeApps;
import com.eagle.smarthome.DeviceControlActivity.myOnItemSelectedListener;
import com.eagle.smarthome.task.EditTaskItem;
import com.eagle.smarthome.task.ScenePlans;
import com.eagle.smarthome.task.ScenePlansItem;
import com.eagle.smarthome.task.SceneTask;
import com.eagle.smarthome.task.TaskItem;
import com.eagle.smarthome.task.TimeRange;
import com.eagle.smarthome.task.TimedTasks;
import com.eagle.smarthome.task.TimedTaskItem;
import com.eagle.smarthome.util.DeviceSystemAdapter;
import com.eagle.smarthome.util.FileManager;
import com.eagle.smarthome.util.HomeLocation;
import com.eagle.smarthome.util.SHProtocol;
import com.eagle.smarthome.util.SmartHomeChannel;
import com.eagle.smarthome.util.SmartHomes;
import com.eagle.smarthome.util.TaskAdapter;
import com.eagle.smarthome.util.TaskOneAdapter;
import com.eagle.smarthome.util.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class TaskSetActivity extends BaseActivity
{	
	private String workdir="";
	private ImageView imgBack2;
	private ImageView imgAddTask;
	private ImageView imgDeleteTask;		
	private ImageView imgSend;		
	private ImageView imgSendTime;		

	private EditText workitem;		
	
	private ImageView imgSaveTime;
	private ImageView imgAddTimeTask;
	private ImageView imgDeleteTimeTask;		
	
	private ImageView imgAddTaskItem;
	private ImageView imgAddTaskItemAfter;	

	private ImageView imgModifyTaskItem;  			//�����������޸�
	private ImageView imgDeleteTaskItem;  			//ɾ������������
	private ImageView imgSaveTask;
	
	private TextView taskset;
	private Spinner cbtasks;
	private Spinner	cbTimeTasks;
	private ListView	lvTaskItem;

    private  CheckBox  chkTaskUsed;
    private EditText edithour;
    private EditText editminute;
    private EditText editsecond;

    private EditText edithour2;
    private EditText editminute2;
    private EditText editsecond2;
    
    private Button btnSetTimedTask;
    
    private LinearLayout layoutEditTimetask;
    //private EditText blacklist;    
    
	private void GetUIName()
	{
		try
		{
			imgModifyTaskItem=(ImageView) findViewById(R.id.imgModifyTaskItem);	 
			imgBack2=(ImageView) findViewById(R.id.imgOK);	 
			imgAddTask=(ImageView) findViewById(R.id.imgAddTask);	 
			imgSaveTask=(ImageView) findViewById(R.id.imgSaveTask);	 
			
			imgSaveTime=(ImageView) findViewById(R.id.imgSaveTime);	 
			imgAddTimeTask=(ImageView) findViewById(R.id.imgAddTimeTask);	 
			imgDeleteTimeTask=(ImageView) findViewById(R.id.imgDeleteTimeTask);	 	
			
			
			workitem=(EditText) findViewById(R.id.workitem);	 
			imgDeleteTask=(ImageView) findViewById(R.id.imgDeleteTask);	 
			imgSend=(ImageView) findViewById(R.id.imgSend);	 
			imgSendTime=(ImageView) findViewById(R.id.imgSendTime);	 
			imgAddTaskItem=(ImageView) findViewById(R.id.imgAddTaskItem);	 	
			imgAddTaskItemAfter=(ImageView) findViewById(R.id.imgAddTaskItemAfter);	 
			imgDeleteTaskItem=(ImageView) findViewById(R.id.imgDeleteTaskItem);	 	
			lvTaskItem=(ListView)findViewById(R.id.lvTaskItem);		
			cbtasks=(Spinner)findViewById(R.id.cbtasks);			
			cbTimeTasks=(Spinner)findViewById(R.id.cbTimeTasks);		
			taskset=(TextView)findViewById(R.id.taskset);	
			chkTaskUsed=(CheckBox)findViewById(R.id.chkTaskUsed);	
			layoutEditTimetask=( LinearLayout)findViewById(R.id.layoutEditTimetask);
			btnSetTimedTask=(Button) findViewById(R.id.btnSetTimedTask);		
			edithour=(EditText) findViewById(R.id.edithour);	 
			editminute=(EditText) findViewById(R.id.editminute);	 
			editsecond=(EditText) findViewById(R.id.editsecond);	 
			edithour2=(EditText) findViewById(R.id.edithour2);	 
			editminute2=(EditText) findViewById(R.id.editminute2);	 
			editsecond2=(EditText) findViewById(R.id.editsecond2);	
			//blacklist=(EditText) findViewById(R.id.blacklist);	 
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(TaskSetActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}
	private void SetButtonClickEvent()  //���ð�ť��Ӧ�¼�
	{
		try
		{
			SetimgBackClick();
			SetimgModifyTaskItemClick();			
			SetimgSendClick();
			SetimgSendTimeClick();
			SetimgAddTaskClick();		
			SetimgAddTimeTaskClick();		
			SetimgSaveTaskClick();
			SetimgSaveTimeTaskClick();

			SetimgDeleteTaskClick();
			SetimgDeleteTimeTaskClick();
			SetimgAddTaskItemClick();						
			SetimgAddTaskItemAfterClick();
			SetimgDeleteTaskItemClick();	
			btnSetTimedTaskClick();
			
			OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(this, this.mAdapter);
			cbtasks.setOnItemSelectedListener(spinnerListener);
			SetcbtasksTouchListener();
			
			OnItemSelectedListener spinnerListener2=new OnTimeTaskItemSelectedListener(this,this.mtimeAdapter);
            cbTimeTasks.setOnItemSelectedListener(spinnerListener2);
			//SetcbTimeTasksTouchListener();
			SetlvTaskItemOnItemClick();
		}
		catch( Exception ex)
		{
			String s=ex.getMessage();
	    	Toast.makeText(TaskSetActivity.this,  s , Toast.LENGTH_SHORT).show();		
		}
	}	
					
	private void  btnSetTimedTaskClick()
 {
		btnSetTimedTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (layoutEditTimetask.getVisibility() == View.GONE)
					layoutEditTimetask.setVisibility(View.VISIBLE);
				else
					layoutEditTimetask.setVisibility(View.GONE);
			}
		});
	}
	
	private void SetimgSendClick()  //���͸�������
	{
		imgSend.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if (currenttask<0) return; //�ж���������ѡ��
	            StringJson json = new StringJson(SHProtocol.SHFLAG);
	            //MENDTASK = "508";   //�޸����ܼҾӵ���������[task=fn][stream=byte[]]
	            json.AddNameVolume("cmd", SHProtocol.MENDTASK);
	            String taskact=alltasks.Items.get(currenttask).PlanFileName;
	            json.AddNameVolume("task", taskact);
	            String filename=getFilesDir()+File.separator+workdir;
	     		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
	     		File file2=new File(file.getAbsoluteFile() +File.separator+taskact+".act");
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
					        tcpClient.SendMessage(json);
					    	//Toast.makeText(TaskSetActivity.this,getResources().getString(R.string.sendactionok), Toast.LENGTH_SHORT).show();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
	     	  	catch (FileNotFoundException e)
	     	  	{				
						e.printStackTrace();						
				}
	     	  	
	     	    json = new StringJson(SHProtocol.SHFLAG);
	            //MENDTASK = "508";   //�޸����ܼҾӵ���������[task=fn][stream=byte[]]
	            json.AddNameVolume("cmd", SHProtocol.MENDTASK);
	            String plantaskt="Task";
	            json.AddNameVolume("plan", plantaskt);
	            filename=getFilesDir()+File.separator+workdir;
	     		file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
	     		file2=new File(file.getAbsoluteFile() +File.separator+plantaskt+".tsk");
	     	  	if (!file2.exists())  return;
	     	  	br =null;
	     	  	try
	     	  	{
						br = new FileInputStream(file2);
						int size=(int)file2.length();
						byte[] nr=new byte[size];
						try {
							br.read(nr,0,size);
					        json.AddNameVolume("stream", nr); 
					        br.close();
					       if ( tcpClient.SendMessage(json))
					    	   Toast.makeText(TaskSetActivity.this,getResources().getString(R.string.sendactionok), Toast.LENGTH_SHORT).show();
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
	
	private void SetimgSendTimeClick()  //���͸�������
	{
		imgSendTime.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if (currenttask<0) return; //�ж���������ѡ��
	            StringJson json = new StringJson(SHProtocol.SHFLAG);
	            //MENDTASK = "508";  	     	  	//�޸����ܼҾӵ���������[task=fn][stream=byte[]][plan=tskfn][timetask=ttskfn]
	            json.AddNameVolume("cmd", SHProtocol.MENDTASK);
	            json.AddNameVolume("timetask", "TimedTask");
	            String filename=getFilesDir()+File.separator+workdir;
	     		File file = new File(filename); 
	     	  	File file2=new File(file.getAbsoluteFile() +File.separator+"TimedTask.ttsk");
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
					       if ( tcpClient.SendMessage(json))
					    	   Toast.makeText(TaskSetActivity.this,getResources().getString(R.string.sendactionok), Toast.LENGTH_SHORT).show();
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
	
	private void SetimgModifyTaskItemClick()  //�༭��ť
	{
		imgModifyTaskItem.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if (currenttaskposition>=0 && currenttask>=0 && selectedtaskitem!=null)
				{
					Intent newintent=new Intent(TaskSetActivity.this,TaskItemModifyActivity.class);
			        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼:  �ҵļ�
			        newintent.putExtra("taskname", selectedtask.PlanFileName);         //��ǰѡ�е����� �� ��������	 
	                newintent.putExtra("currenttask", currenttask);      //��ǰѡ�е�����	 �� ��������	������
	                newintent.putExtra("currenttaskposition", currenttaskposition);//��ǰ�����еĵڼ�������	�� �����������б�ڼ��У�
	                TaskItem.Copy(selectedtaskitem, taskitem);
	                editok=false;
	                startActivityForResult(newintent,101);
				}
			}
		});
	}

	private void SetimgDeleteTaskItemClick()  //ɾ��һ�����Ʋ���TaskItem
	{
		imgDeleteTaskItem.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //�ж���������ѡ��
				if(currenttaskposition<0) return;		
				AlertDialog.Builder ad=new AlertDialog.Builder(TaskSetActivity.this);
				ad.setTitle(getResources().getString(R.string.suredelete));
				int pos =selectedtaskitem.FindHome(InstlledDeviceSystem);
				if (pos<0) return;
				ad.setMessage(getResources().getString(R.string.suredeletetaskitem)+":\r\n"+
						TaskItem.TaskItemToString(selectedtaskitem,homes.smarthomes.get (pos)));
				ad.setPositiveButton(getResources().getString(R.string.delete), new OnClickListener()
				{
					public void onClick(DialogInterface dialog,int arg)
					{						
						alltaskitems.Items.remove(currenttaskposition);
						if (currenttaskposition>=alltaskitems.Items.size())
							currenttaskposition=alltaskitems.Items.size()-1;
						selectedtaskitem=alltaskitems.Items.get(currenttaskposition);
						alltaskitems.SaveToFile();
						taskoneadapter.setSelectItem(currenttaskposition);
					    taskoneadapter.notifyDataSetInvalidated();
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

	private void SetimgDeleteTaskClick()  //����������ɾ��һ��������
	{
		imgDeleteTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //�ж���������ѡ��
				AlertDialog.Builder ad=new AlertDialog.Builder(TaskSetActivity.this);
				ad.setTitle(getResources().getString(R.string.suredelete));
				ad.setMessage(getResources().getString(R.string.suredeletetask)+":\r\n"+selectedtask.PlanFileName);
				ad.setPositiveButton(getResources().getString(R.string.delete), new OnClickListener()
				{
					public void onClick(DialogInterface dialog,int arg)
					{
						alltasks.Items.remove(currenttask);  //ɾ������
						alltasks.SaveToFile();  //����
						LoadAllSceneTasks("Task.tsk"); 	//����װ�������б�	
						currenttask=alltasks.Items.size()-1;
						if (currenttask>=0)
						{
							selectedtask=alltasks.Items.get(currenttask);
							LoadAllTaskItems(selectedtask);  //װ��ָ�����������
							selectedtaskitem=null; //alltaskitems.Items.get(currenttaskposition);	
							oldselectedtaskitem=selectedtaskitem;
							taskoneadapter.setList(alltaskitems.Items);	//taskoneadapter.setSelectItem(currenttaskposition);  
							taskoneadapter.notifyDataSetInvalidated();
						}
						else					
						{
							selectedtaskitem=null; //alltaskitems.Items.get(currenttaskposition);
							taskoneadapter.setList(null);  
							taskoneadapter.notifyDataSetChanged();	//taskoneadapter.notifyDataSetInvalidated();
						}
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
	
	private void SetimgAddTaskClick()  //��������������һ��������
	{
		imgAddTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				String fn=workitem.getText().toString().trim();
				if (fn.equals(getResources().getString(R.string.newtask))) return; //�������������������
				if (alltasks.ScenePlansItemExists(fn)>=0) return; //�Ѿ�����ͬ��������
				ScenePlansItem item=new ScenePlansItem();
				item.Used=true;
				item.PlanFileName=fn;
				alltasks.Items.add(item);  //����һ��������
				alltasks.SaveToFile();  //����
				LoadAllSceneTasks("Task.tsk"); 	//����װ�������б�
				int position=alltasks.ScenePlansItemExists(fn); //���б��е�λ��
				mAdapter.notifyDataSetChanged();
				cbtasks.setSelection(position);
				currenttaskposition=-1;
			}
		});
	}
	
	private void  SetimgSaveTaskClick()  //�����޸ĺ������
	{
		imgSaveTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //�ж���������ѡ��
				if (currenttask>=0)
				{
					selectedtask=alltasks.Items.get(currenttask);
					UIToTask();
					alltasks.SaveToFile();
					Toast.makeText(TaskSetActivity.this,getResources().getString(R.string.modifyok), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	private void SetimgAddTimeTaskClick()  //��������������һ��������
	{
		imgAddTimeTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (timedTaskItem==null) return;		
				if (timedTasks.FindTimedTaskItem(timedTaskItem.TaskName)==null)
					timedTasks.Items.add(timedTaskItem);  //��ǰû�м���Ļ�����Ҫ����
				TimeRange tr=new TimeRange();
				timedTaskItem.RunTime.add(tr);
				timetaskarray.add(Tools.SecondtoTimeString(tr.StartTime)+"  -  "+Tools.SecondtoTimeString(tr.EndTime));
				mtimeAdapter.notifyDataSetChanged();
				cbTimeTasks.setSelection(timedTaskItem.RunTime.size()-1);
				timedTasks.SaveToFile();
		    	Toast.makeText(TaskSetActivity.this,  "One Plan Added !" , Toast.LENGTH_SHORT).show();		
			}
		});
	}
	
	private void SetimgDeleteTimeTaskClick()  //�ڶ�ʱ��������ɾ��һ��������
	{
		imgDeleteTimeTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (timedTaskItem==null) return;		
				int pos=cbTimeTasks.getSelectedItemPosition();
				if (pos>=0) //��ѡ��
				{
					timetaskarray.remove(pos);
					timedTaskItem.RunTime.remove(pos);
					mtimeAdapter.notifyDataSetChanged();
					cbTimeTasks.setSelection(timedTaskItem.RunTime.size()-1);
					timedTasks.SaveToFile();
			    	Toast.makeText(TaskSetActivity.this,  "One Plan Removed !" , Toast.LENGTH_SHORT).show();		
				}
			}
		});
	}	
	
	private void  SetimgSaveTimeTaskClick()  //���涨ʱ�����б�����
	 {
			imgSaveTime.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (timedTaskItem==null) return;		
					int pos=cbTimeTasks.getSelectedItemPosition();
					if (pos>=0) //��ѡ�񣬰��޸ĺ������д�ض�����
					{
						TimeRange tr=timedTaskItem.RunTime.get(pos);
						int h=GetIntFromEditText(edithour);
						int m=GetIntFromEditText(editminute);
						int s=GetIntFromEditText(editsecond);
						tr.StartTime=(h*3600+m*60+s) % 86400;
						h=GetIntFromEditText(edithour2);
						m=GetIntFromEditText(editminute2);
						s=GetIntFromEditText(editsecond2);
						tr.EndTime=(h*3600+m*60+s) % 86400;
						timetaskarray.set(pos,Tools.SecondtoTimeString(tr.StartTime)+"  -  "+
						                                    Tools.SecondtoTimeString(tr.EndTime));
						mtimeAdapter.notifyDataSetChanged();
					}
					timedTasks.SaveToFile();
			    	Toast.makeText(TaskSetActivity.this,  "Timed Plana Saved !" , Toast.LENGTH_SHORT).show();		
				}
			});
		}
	
	private void  UIToTask()
	{
		if (selectedtask==null) return;
		selectedtask.Used=chkTaskUsed.isChecked();
	}	
	
	private void SetimgAddTaskItemClick()  //����һ�����Ʋ���TaskItem
	{
		imgAddTaskItem.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //�ж���������ѡ��
				
				if(currenttaskposition>=0)  //ѡ����һ�в��������ڸ���֮��
				{
					TaskItem item=new TaskItem();
					alltaskitems.Items.add(currenttaskposition, item);
				}
				else  //���������һ��
				{
					TaskItem item=new TaskItem();
					if (homes.smarthomes.size()>0)
					{
		        	   item.appId=InstlledDeviceSystem.SmartHomeChannels.get(0).appid;
					   item.devieID=0;
					}
					alltaskitems.Items.add( item);
					currenttaskposition=alltaskitems.Items.size()-1;
				}
				selectedtaskitem=alltaskitems.Items.get(currenttaskposition);
				alltaskitems.SaveToFile();
				if (currenttaskposition>=0 && currenttask>=0 && selectedtaskitem!=null)
				{
					Intent newintent=new Intent(TaskSetActivity.this,TaskItemModifyActivity.class);
			        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼:  �ҵļ�
			        newintent.putExtra("taskname", selectedtask.PlanFileName);         //��ǰѡ�е����� �� ��������	 
	                newintent.putExtra("currenttask", currenttask);      //��ǰѡ�е�����	 �� ��������	������
	                newintent.putExtra("currenttaskposition", currenttaskposition);//��ǰ�����еĵڼ�������	�� �����������б�ڼ��У�
	                TaskItem.Copy(selectedtaskitem, taskitem);
	                editok=false;
	                startActivityForResult(newintent,101);
				}
			}
		});
	}
	private void SetimgAddTaskItemAfterClick()  //�ں����һ�����Ʋ���TaskItem
	{
		imgAddTaskItemAfter.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //�ж���������ѡ��
				
				if(currenttaskposition>=0)  //ѡ����һ�в��������ڸ���֮��
				{
					TaskItem item=new TaskItem();
					alltaskitems.Items.add(currenttaskposition+1, item);
					currenttaskposition++;
				}
				else  //���������һ��
				{
					TaskItem item=new TaskItem();
					if (homes.smarthomes.size()>0)
					{
		        	   item.appId=InstlledDeviceSystem.SmartHomeChannels.get(0).appid;
					   item.devieID=0;
					}
					alltaskitems.Items.add( item);
					currenttaskposition=alltaskitems.Items.size()-1;
				}
				selectedtaskitem=alltaskitems.Items.get(currenttaskposition);
				alltaskitems.SaveToFile();
				if (currenttaskposition>=0 && currenttask>=0 && selectedtaskitem!=null)
				{
					Intent newintent=new Intent(TaskSetActivity.this,TaskItemModifyActivity.class);
			        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼:  �ҵļ�
			        newintent.putExtra("taskname", selectedtask.PlanFileName);         //��ǰѡ�е����� �� ��������	 
	                newintent.putExtra("currenttask", currenttask);      //��ǰѡ�е�����	 �� ��������	������
	                newintent.putExtra("currenttaskposition", currenttaskposition);//��ǰ�����еĵڼ�������	�� �����������б�ڼ��У�
	                TaskItem.Copy(selectedtaskitem, taskitem);
	                editok=false;
	                startActivityForResult(newintent,101);
				}
			}
		});
	}
		
	private void SetimgBackClick()  //���˰�ť
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
	private TcpClient tcpClient;
	@Override
	protected void onCreate(Bundle savedInstanceState)   //�����
	{
		super.onCreate(savedInstanceState);
		tcpClient=TcpClient.GetTcpClient();
		setContentView(R.layout.task_set_layout);
		GetUIName();
		SetButtonClickEvent();
		
		Intent intent=this.getIntent();
		String dir=intent.getStringExtra("workdir");		
		taskset.setText(dir+" - "+getResources().getString(R.string.taskset));
		if (dir!=null) workdir=dir;		
		LoadAllHomeDevice();    				//װ�����ܼҾӵ������豸ϵͳ
		LoadAllSceneTasks("Task.tsk"); 	//װ�������б�
		LoadAllTimeTasks("TimedTask.ttsk"); 	//װ�붨ʱ�����б�"TimedTask.ttsk"
		layoutEditTimetask.setVisibility(View.GONE);
	}

	@Override
	protected void onResume()
	{  
		super.onResume();
		/*Intent intent=this.getIntent();
		String dir=intent.getStringExtra("workdir");		
		taskset.setText(dir+" - "+getResources().getString(R.string.taskset));
		if (dir!=null) workdir=dir;		
		LoadAllHomeDevice();    				//װ�����ܼҾӵ������豸ϵͳ
		LoadAllSceneTasks("Task.tsk"); 	//װ�������б� */
		
		/*if (currenttask>=0 && currenttask<alltasks.Items.size()) //���onActivityResult���ص���ֵ
		{
			int oldcurrenttaskposition=currenttaskposition;
			cbtasks.setSelection(currenttask);
			selectedtask=alltasks.Items.get(currenttask); 
			LoadAllTaskItems(selectedtask);  //װ��ָ�����������:�����޸���currenttaskposition��ֵ���������ȱ�����oldcurrenttaskposition
			if (oldcurrenttaskposition>=0 && oldcurrenttaskposition<alltaskitems.Items.size())
			{
				currenttaskposition=oldcurrenttaskposition;
				if (editok)  //�޸�������
				{
					selectedtaskitem=alltaskitems.Items.get(currenttaskposition);
				    TaskItem.Copy(taskitem,selectedtaskitem);
				    taskoneadapter.notifyDataSetChanged();
					SaveAllTaskItems(alltaskitems); //��������
					Toast.makeText(TaskSetActivity.this,getResources().getString(R.string.modifyok), Toast.LENGTH_SHORT).show();
				}
				taskoneadapter.setSelectItem(currenttaskposition);
				taskoneadapter.notifyDataSetInvalidated();
				lvTaskItem.setSelection(currenttaskposition);
			}
		} */
	}
	private Boolean editok=false;
	//�޸Ļ���ص����ݣ�onActivityResult��onResume֮ǰִ�У�
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data) //��ȡ�޸Ľ��
	{
		switch (requestCode)
		{
			case 101:
				if (resultCode==RESULT_OK)
				{					
					currenttask=data.getIntExtra("currenttask",-1);		//��ǰѡ�е�����
					currenttaskposition=data.getIntExtra("currenttaskposition",-1);	//��ǰ�����еĵڼ�������
					editok=data.getBooleanExtra("editok", false);         //�޸���Ч��
			     	//Toast.makeText(TaskSetActivity.this, "currenttask="+ currenttask+"\r\ncurrenttaskposition="+currenttaskposition, Toast.LENGTH_SHORT).show();		
					
			     	if (currenttask>=0 && currenttask<alltasks.Items.size()) //���onActivityResult���ص���ֵ
					{
						int oldcurrenttaskposition=currenttaskposition;
						cbtasks.setSelection(currenttask);
						selectedtask=alltasks.Items.get(currenttask); 
						LoadAllTaskItems(selectedtask);  //װ��ָ�����������:�����޸���currenttaskposition��ֵ���������ȱ�����oldcurrenttaskposition
						if (oldcurrenttaskposition>=0 && oldcurrenttaskposition<alltaskitems.Items.size())
						{
							currenttaskposition=oldcurrenttaskposition;
							if (editok)  //�޸�������
							{
								selectedtaskitem=alltaskitems.Items.get(currenttaskposition);
							    TaskItem.Copy(taskitem,selectedtaskitem);
							    taskoneadapter.notifyDataSetChanged();
								SaveAllTaskItems(alltaskitems); //��������
								Toast.makeText(TaskSetActivity.this,getResources().getString(R.string.modifyok), Toast.LENGTH_SHORT).show();
							}
							taskoneadapter.setSelectItem(currenttaskposition);
							taskoneadapter.notifyDataSetInvalidated();
							lvTaskItem.setSelection(currenttaskposition);
						}
					}
				}
				break;
			default:
				break;
		}
	}
	private SmartHomes homes;
	private SmartHomeApps InstlledDeviceSystem; //��װ�˵��豸ϵͳ
	private void LoadAllHomeDevice()  //װ�����ܼҾӵ������豸ϵͳ
	{
		homes=new SmartHomes();
        InstlledDeviceSystem = new SmartHomeApps(this,workdir,"EagleSmartHome.esh");
        for (int i=0; i<InstlledDeviceSystem.SmartHomeChannels.size(); i++)
        {
        	SmartHome smarthome=new SmartHome(this, workdir, "SmartHome"+InstlledDeviceSystem.SmartHomeChannels.get(i).appid+".smh");
        	homes.smarthomes.add(smarthome);
        	homes.appids.add(InstlledDeviceSystem.SmartHomeChannels.get(i).appid);
        }
	}
	
	
    private TimedTasks  timedTasks;
    private void LoadAllTimeTasks(String timetask) 	//װ�붨ʱ�����б�"TimedTask.ttsk"
    {
    	timedTasks=new TimedTasks(SHProtocol.SHFLAG,this, workdir,timetask);
    }
    
    private TimedTaskItem timedTaskItem;
	private List<String> timetaskarray=null;	    //�����б�
	protected ArrayAdapter<String> mtimeAdapter;
    private void LoadOneTimeTasks(String timetask) //װ��ָ�������Ӧ�Ķ�ʱ�б�
	{
		timedTaskItem = timedTasks.FindTimedTaskItem(timetask);
		timetaskarray = new Vector<String>();
		if (timedTaskItem == null)
		{
			timedTaskItem = new TimedTaskItem();
			timedTaskItem.TaskName=timetask;
		}
		for (int i = 0; i < timedTaskItem.RunTime.size(); i++) {
			TimeRange tr=timedTaskItem.RunTime.get(i);
			timetaskarray.add(Tools.SecondtoTimeString(tr.StartTime)+"  -  "+Tools.SecondtoTimeString(tr.EndTime));
		}
    	this.mtimeAdapter = new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, timetaskarray);
    	this.cbTimeTasks.setAdapter(mtimeAdapter);
	}

    private int currenttask=-1;					//��ǰѡ�е�����
	private int currenttaskposition=-1;		//��ǰ���������еĵڼ�������	
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
   	    	 String s=item.PlanFileName;
   	    	 taskarray.add(s);
   	     }
    	this.mAdapter = new ArrayAdapter<String>(	this,android.R.layout.simple_spinner_dropdown_item, taskarray);
    	this.cbtasks.setAdapter(mAdapter);
    }
    
    
    private ScenePlansItem selectedtask;  //ѡ�еĵ�ǰ����
    private SceneTask alltaskitems;    		//��ǰ��������������б�
    private TaskItem selectedtaskitem;    	//��ǰѡ�е������м�¼
    private TaskOneAdapter taskoneadapter;
    private void LoadAllTaskItems(ScenePlansItem selectedtask)     	 	//װ��ָ�����������
    {
    	if (selectedtask==null) return;
    	alltaskitems=new  SceneTask(SHProtocol.SHFLAG,this, workdir,selectedtask.PlanFileName+".act", selectedtask.Used, 0);
    	taskoneadapter=new TaskOneAdapter(TaskSetActivity.this,R.layout.taskone_layout, alltaskitems.Items, InstlledDeviceSystem, homes);
    	lvTaskItem.setAdapter(taskoneadapter);  	
    }    
	private void  SaveAllTaskItems(SceneTask selectedtask)     	 	//����ָ�����������
	{
		selectedtask.SaveToFile();
	}
	// ѡ��һ������
	public class myOnItemSelectedListener implements OnItemSelectedListener
	{
		ArrayAdapter<String> mLocalAdapter;
		Activity mLocalContext;
		// ���췽��
		public myOnItemSelectedListener(Activity c,ArrayAdapter<String> ad)
		{
			this.mLocalContext = c;
			this.mLocalAdapter = ad;
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
		{
			if (editok) return;
			currenttask=position;
			selectedtask=alltasks.Items.get(currenttask);
			LoadAllTaskItems(selectedtask);  //װ��ָ�����������
			selectedtaskitem=null; //alltaskitems.Items.get(currenttaskposition);	
			oldselectedtaskitem=selectedtaskitem;
			taskoneadapter.setList(alltaskitems.Items);	//taskoneadapter.setSelectItem(currenttaskposition);  
			taskoneadapter.notifyDataSetInvalidated();
			lvTaskItem.invalidateViews();
			lastClickTime = 0;
			TaskToUI();
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent)	{  }
	}
	
	private void TaskToUI()
	{
		if (selectedtask==null) return;
		chkTaskUsed.setChecked(selectedtask.Used);
		LoadOneTimeTasks(selectedtask.PlanFileName);
		//blacklist.setText(selectedtask.BlackList);
	}
	
	// �û�ѡ�������б��е�һ��
	public class OnTimeTaskItemSelectedListener implements OnItemSelectedListener
	{
		ArrayAdapter<String> mLocalAdapter;
		Activity mLocalContext;
		// ���췽��
		public OnTimeTaskItemSelectedListener(Activity c,ArrayAdapter<String> ad)
		{
			this.mLocalContext = c;
			this.mLocalAdapter = ad;
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if (timedTaskItem==null) return;
			TimeRange tr=timedTaskItem.RunTime.get(position);
			edithour.setText(String.format("%02d", GetHour(tr.StartTime)));
			editminute.setText(String.format("%02d", GetMinute(tr.StartTime)));
			editsecond.setText(String.format("%02d", GetSecond(tr.StartTime)));
			edithour2.setText(String.format("%02d", GetHour(tr.EndTime)));
			editminute2.setText(String.format("%02d", GetMinute(tr.EndTime)));
			editsecond2.setText(String.format("%02d", GetSecond(tr.EndTime)));
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {	}
	}
	
	int GetHour(int second)	{		return second /3600;	}
	int GetMinute(int second)	{		return (second /60) % 60;	}
	int GetSecond(int second)	{		return second % 60;	}
	int GetIntFromEditText(EditText et)
	{
		int n=0;
		String s=et.getText().toString();
		try {
			n = Integer.parseInt(s);
		} catch (Exception e) {	}
		if (n<0) n=0;
		return n % 60;
	}
	// ���ô����¼�����  
 	private void SetcbtasksTouchListener()  
	{
		cbtasks.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				editok=false;  //ֻ�д���һ�²��ܸı�
				return false;
			}  
		});  
	}
 	
	private TaskItem taskitem=EditTaskItem.GetTaskItem();
	private long lastClickTime;
    private TaskItem oldselectedtaskitem;    	//��ǰѡ�е������м�¼
	private void SetlvTaskItemOnItemClick()  //ѡ����������������
	{
		lvTaskItem.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{   
				selectedtaskitem=alltaskitems.Items.get(position);	
				if (oldselectedtaskitem==selectedtaskitem   && (Math.abs(lastClickTime-System.currentTimeMillis()) < 300))
				{
					lastClickTime = 0;  //ģ��˫���¼�
					if (position>=0)
					{
						Intent newintent=new Intent(TaskSetActivity.this,TaskItemModifyActivity.class);
				        newintent.putExtra("workdir", workdir);  					//���Ǹ���ص�Ŀ¼
				        newintent.putExtra("taskname", selectedtask.PlanFileName);         //��ǰѡ�е�����	 
		                newintent.putExtra("currenttask", currenttask);      //��ǰѡ�е�����	 
		                newintent.putExtra("currenttaskposition", currenttaskposition);//��ǰ�����еĵڼ�������	
		                TaskItem.Copy(selectedtaskitem, taskitem);
		                editok=false;
		                startActivityForResult(newintent,101);
					}
				}
				else //���ǵ��ͬһ��,��ʱ�����
				{
					currenttaskposition=position;
					oldselectedtaskitem=selectedtaskitem;
					taskoneadapter.setSelectItem(position);  
					taskoneadapter.notifyDataSetInvalidated();
					lastClickTime = System.currentTimeMillis();
				}
			}		
		});
	}
}

