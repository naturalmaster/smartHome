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

	private ImageView imgModifyTaskItem;  			//任务内容项修改
	private ImageView imgDeleteTaskItem;  			//删除任务内容项
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
	private void SetButtonClickEvent()  //设置按钮响应事件
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
	
	private void SetimgSendClick()  //发送更新任务
	{
		imgSend.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if (currenttask<0) return; //判断有无任务选中
	            StringJson json = new StringJson(SHProtocol.SHFLAG);
	            //MENDTASK = "508";   //修改智能家居的任务数据[task=fn][stream=byte[]]
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
	            //MENDTASK = "508";   //修改智能家居的任务数据[task=fn][stream=byte[]]
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
	
	private void SetimgSendTimeClick()  //发送更新任务
	{
		imgSendTime.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if (currenttask<0) return; //判断有无任务选中
	            StringJson json = new StringJson(SHProtocol.SHFLAG);
	            //MENDTASK = "508";  	     	  	//修改智能家居的任务数据[task=fn][stream=byte[]][plan=tskfn][timetask=ttskfn]
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
	
	private void SetimgModifyTaskItemClick()  //编辑按钮
	{
		imgModifyTaskItem.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  
				if (currenttaskposition>=0 && currenttask>=0 && selectedtaskitem!=null)
				{
					Intent newintent=new Intent(TaskSetActivity.this,TaskItemModifyActivity.class);
			        newintent.putExtra("workdir", workdir);  					//是那个监控点目录:  我的家
			        newintent.putExtra("taskname", selectedtask.PlanFileName);         //当前选中的任务 ： 播放音乐	 
	                newintent.putExtra("currenttask", currenttask);      //当前选中的任务	 ： 播放音乐	所在行
	                newintent.putExtra("currenttaskposition", currenttaskposition);//当前任务中的第几行内容	： 开启（内容列表第几行）
	                TaskItem.Copy(selectedtaskitem, taskitem);
	                editok=false;
	                startActivityForResult(newintent,101);
				}
			}
		});
	}

	private void SetimgDeleteTaskItemClick()  //删除一个控制操作TaskItem
	{
		imgDeleteTaskItem.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //判断有无任务选中
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

	private void SetimgDeleteTaskClick()  //在任务项中删除一个任务项
	{
		imgDeleteTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //判断有无任务选中
				AlertDialog.Builder ad=new AlertDialog.Builder(TaskSetActivity.this);
				ad.setTitle(getResources().getString(R.string.suredelete));
				ad.setMessage(getResources().getString(R.string.suredeletetask)+":\r\n"+selectedtask.PlanFileName);
				ad.setPositiveButton(getResources().getString(R.string.delete), new OnClickListener()
				{
					public void onClick(DialogInterface dialog,int arg)
					{
						alltasks.Items.remove(currenttask);  //删除任务
						alltasks.SaveToFile();  //保存
						LoadAllSceneTasks("Task.tsk"); 	//重新装入任务列表	
						currenttask=alltasks.Items.size()-1;
						if (currenttask>=0)
						{
							selectedtask=alltasks.Items.get(currenttask);
							LoadAllTaskItems(selectedtask);  //装入指定任务的内容
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
	
	private void SetimgAddTaskClick()  //在任务项中增加一个任务项
	{
		imgAddTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				String fn=workitem.getText().toString().trim();
				if (fn.equals(getResources().getString(R.string.newtask))) return; //不能用新任务这个名字
				if (alltasks.ScenePlansItemExists(fn)>=0) return; //已经存在同名任务项
				ScenePlansItem item=new ScenePlansItem();
				item.Used=true;
				item.PlanFileName=fn;
				alltasks.Items.add(item);  //增加一个任务项
				alltasks.SaveToFile();  //保存
				LoadAllSceneTasks("Task.tsk"); 	//重新装入任务列表
				int position=alltasks.ScenePlansItemExists(fn); //在列表中的位置
				mAdapter.notifyDataSetChanged();
				cbtasks.setSelection(position);
				currenttaskposition=-1;
			}
		});
	}
	
	private void  SetimgSaveTaskClick()  //保存修改后的任务
	{
		imgSaveTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //判断有无任务选中
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
	private void SetimgAddTimeTaskClick()  //在任务项中增加一个任务项
	{
		imgAddTimeTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (timedTaskItem==null) return;		
				if (timedTasks.FindTimedTaskItem(timedTaskItem.TaskName)==null)
					timedTasks.Items.add(timedTaskItem);  //以前没有加入的话，需要加入
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
	
	private void SetimgDeleteTimeTaskClick()  //在定时任务项中删除一个任务项
	{
		imgDeleteTimeTask.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (timedTaskItem==null) return;		
				int pos=cbTimeTasks.getSelectedItemPosition();
				if (pos>=0) //有选择
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
	
	private void  SetimgSaveTimeTaskClick()  //保存定时任务列表数据
	 {
			imgSaveTime.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (timedTaskItem==null) return;		
					int pos=cbTimeTasks.getSelectedItemPosition();
					if (pos>=0) //有选择，把修改后的数据写回对象中
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
	
	private void SetimgAddTaskItemClick()  //插入一个控制操作TaskItem
	{
		imgAddTaskItem.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //判断有无任务选中
				
				if(currenttaskposition>=0)  //选中了一行操作：插在该行之后
				{
					TaskItem item=new TaskItem();
					alltaskitems.Items.add(currenttaskposition, item);
				}
				else  //加在最后面一行
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
			        newintent.putExtra("workdir", workdir);  					//是那个监控点目录:  我的家
			        newintent.putExtra("taskname", selectedtask.PlanFileName);         //当前选中的任务 ： 播放音乐	 
	                newintent.putExtra("currenttask", currenttask);      //当前选中的任务	 ： 播放音乐	所在行
	                newintent.putExtra("currenttaskposition", currenttaskposition);//当前任务中的第几行内容	： 开启（内容列表第几行）
	                TaskItem.Copy(selectedtaskitem, taskitem);
	                editok=false;
	                startActivityForResult(newintent,101);
				}
			}
		});
	}
	private void SetimgAddTaskItemAfterClick()  //在后插入一个控制操作TaskItem
	{
		imgAddTaskItemAfter.setOnClickListener(new View.OnClickListener()   
		{			
			@Override
			public void onClick(View v)
			{  				
				if (currenttask<0) return; //判断有无任务选中
				
				if(currenttaskposition>=0)  //选中了一行操作：插在该行之后
				{
					TaskItem item=new TaskItem();
					alltaskitems.Items.add(currenttaskposition+1, item);
					currenttaskposition++;
				}
				else  //加在最后面一行
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
			        newintent.putExtra("workdir", workdir);  					//是那个监控点目录:  我的家
			        newintent.putExtra("taskname", selectedtask.PlanFileName);         //当前选中的任务 ： 播放音乐	 
	                newintent.putExtra("currenttask", currenttask);      //当前选中的任务	 ： 播放音乐	所在行
	                newintent.putExtra("currenttaskposition", currenttaskposition);//当前任务中的第几行内容	： 开启（内容列表第几行）
	                TaskItem.Copy(selectedtaskitem, taskitem);
	                editok=false;
	                startActivityForResult(newintent,101);
				}
			}
		});
	}
		
	private void SetimgBackClick()  //后退按钮
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
	private TcpClient tcpClient;
	@Override
	protected void onCreate(Bundle savedInstanceState)   //创建活动
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
		LoadAllHomeDevice();    				//装入智能家居的所有设备系统
		LoadAllSceneTasks("Task.tsk"); 	//装入任务列表
		LoadAllTimeTasks("TimedTask.ttsk"); 	//装入定时任务列表"TimedTask.ttsk"
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
		LoadAllHomeDevice();    				//装入智能家居的所有设备系统
		LoadAllSceneTasks("Task.tsk"); 	//装入任务列表 */
		
		/*if (currenttask>=0 && currenttask<alltasks.Items.size()) //检查onActivityResult返回的数值
		{
			int oldcurrenttaskposition=currenttaskposition;
			cbtasks.setSelection(currenttask);
			selectedtask=alltasks.Items.get(currenttask); 
			LoadAllTaskItems(selectedtask);  //装入指定任务的内容:这里修改了currenttaskposition的值，必须事先保存在oldcurrenttaskposition
			if (oldcurrenttaskposition>=0 && oldcurrenttaskposition<alltaskitems.Items.size())
			{
				currenttaskposition=oldcurrenttaskposition;
				if (editok)  //修改了数据
				{
					selectedtaskitem=alltaskitems.Items.get(currenttaskposition);
				    TaskItem.Copy(taskitem,selectedtaskitem);
				    taskoneadapter.notifyDataSetChanged();
					SaveAllTaskItems(alltaskitems); //保存数据
					Toast.makeText(TaskSetActivity.this,getResources().getString(R.string.modifyok), Toast.LENGTH_SHORT).show();
				}
				taskoneadapter.setSelectItem(currenttaskposition);
				taskoneadapter.notifyDataSetInvalidated();
				lvTaskItem.setSelection(currenttaskposition);
			}
		} */
	}
	private Boolean editok=false;
	//修改活动返回的数据，onActivityResult在onResume之前执行！
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data) //获取修改结果
	{
		switch (requestCode)
		{
			case 101:
				if (resultCode==RESULT_OK)
				{					
					currenttask=data.getIntExtra("currenttask",-1);		//当前选中的任务
					currenttaskposition=data.getIntExtra("currenttaskposition",-1);	//当前任务中的第几行内容
					editok=data.getBooleanExtra("editok", false);         //修改生效否
			     	//Toast.makeText(TaskSetActivity.this, "currenttask="+ currenttask+"\r\ncurrenttaskposition="+currenttaskposition, Toast.LENGTH_SHORT).show();		
					
			     	if (currenttask>=0 && currenttask<alltasks.Items.size()) //检查onActivityResult返回的数值
					{
						int oldcurrenttaskposition=currenttaskposition;
						cbtasks.setSelection(currenttask);
						selectedtask=alltasks.Items.get(currenttask); 
						LoadAllTaskItems(selectedtask);  //装入指定任务的内容:这里修改了currenttaskposition的值，必须事先保存在oldcurrenttaskposition
						if (oldcurrenttaskposition>=0 && oldcurrenttaskposition<alltaskitems.Items.size())
						{
							currenttaskposition=oldcurrenttaskposition;
							if (editok)  //修改了数据
							{
								selectedtaskitem=alltaskitems.Items.get(currenttaskposition);
							    TaskItem.Copy(taskitem,selectedtaskitem);
							    taskoneadapter.notifyDataSetChanged();
								SaveAllTaskItems(alltaskitems); //保存数据
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
	private SmartHomeApps InstlledDeviceSystem; //安装了的设备系统
	private void LoadAllHomeDevice()  //装入智能家居的所有设备系统
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
    private void LoadAllTimeTasks(String timetask) 	//装入定时任务列表"TimedTask.ttsk"
    {
    	timedTasks=new TimedTasks(SHProtocol.SHFLAG,this, workdir,timetask);
    }
    
    private TimedTaskItem timedTaskItem;
	private List<String> timetaskarray=null;	    //任务列表
	protected ArrayAdapter<String> mtimeAdapter;
    private void LoadOneTimeTasks(String timetask) //装入指定任务对应的定时列表
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

    private int currenttask=-1;					//当前选中的任务
	private int currenttaskposition=-1;		//当前任务内容中的第几行内容	
	private List<String> taskarray=null;	    //任务列表
    private ScenePlans alltasks;  				//所有任务列表
	protected ArrayAdapter<String> mAdapter;
    
    private void LoadAllSceneTasks(String task) //装入所有场景任务列表
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
    
    
    private ScenePlansItem selectedtask;  //选中的当前任务
    private SceneTask alltaskitems;    		//当前任务的所有内容列表
    private TaskItem selectedtaskitem;    	//当前选中的任务行记录
    private TaskOneAdapter taskoneadapter;
    private void LoadAllTaskItems(ScenePlansItem selectedtask)     	 	//装入指定任务的内容
    {
    	if (selectedtask==null) return;
    	alltaskitems=new  SceneTask(SHProtocol.SHFLAG,this, workdir,selectedtask.PlanFileName+".act", selectedtask.Used, 0);
    	taskoneadapter=new TaskOneAdapter(TaskSetActivity.this,R.layout.taskone_layout, alltaskitems.Items, InstlledDeviceSystem, homes);
    	lvTaskItem.setAdapter(taskoneadapter);  	
    }    
	private void  SaveAllTaskItems(SceneTask selectedtask)     	 	//保存指定任务的内容
	{
		selectedtask.SaveToFile();
	}
	// 选择一项任务
	public class myOnItemSelectedListener implements OnItemSelectedListener
	{
		ArrayAdapter<String> mLocalAdapter;
		Activity mLocalContext;
		// 构造方法
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
			LoadAllTaskItems(selectedtask);  //装入指定任务的内容
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
	
	// 用户选中下拉列表中得一项
	public class OnTimeTaskItemSelectedListener implements OnItemSelectedListener
	{
		ArrayAdapter<String> mLocalAdapter;
		Activity mLocalContext;
		// 构造方法
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
	// 设置触摸事件监听  
 	private void SetcbtasksTouchListener()  
	{
		cbtasks.setOnTouchListener(new Spinner.OnTouchListener() {  
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				editok=false;  //只有触摸一下才能改变
				return false;
			}  
		});  
	}
 	
	private TaskItem taskitem=EditTaskItem.GetTaskItem();
	private long lastClickTime;
    private TaskItem oldselectedtaskitem;    	//以前选中的任务行记录
	private void SetlvTaskItemOnItemClick()  //选择具体任务的内容项
	{
		lvTaskItem.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{   
				selectedtaskitem=alltaskitems.Items.get(position);	
				if (oldselectedtaskitem==selectedtaskitem   && (Math.abs(lastClickTime-System.currentTimeMillis()) < 300))
				{
					lastClickTime = 0;  //模拟双击事件
					if (position>=0)
					{
						Intent newintent=new Intent(TaskSetActivity.this,TaskItemModifyActivity.class);
				        newintent.putExtra("workdir", workdir);  					//是那个监控点目录
				        newintent.putExtra("taskname", selectedtask.PlanFileName);         //当前选中的任务	 
		                newintent.putExtra("currenttask", currenttask);      //当前选中的任务	 
		                newintent.putExtra("currenttaskposition", currenttaskposition);//当前任务中的第几行内容	
		                TaskItem.Copy(selectedtaskitem, taskitem);
		                editok=false;
		                startActivityForResult(newintent,101);
					}
				}
				else //不是点击同一行,或时间过长
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

