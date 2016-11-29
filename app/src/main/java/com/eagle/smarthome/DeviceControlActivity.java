package com.eagle.smarthome;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.eagle.smarthome.Device.*;

import com.eagle.smarthome.util.DeviceAdapter;
import com.eagle.smarthome.util.FileManager;

import com.eagle.smarthome.util.SHProtocol;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

	public class DeviceControlActivity  extends BaseActivity
	{
		LocalBroadcastManager localBroadcastManager;

		private String workdir="";
		private int appid=-1;
		private TcpClient tcpClient;
		public Context thiscontext;
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{  // TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.devicecontrol_layout);
			thiscontext=this;
			tcpClient=TcpClient.GetTcpClient();
			localReceiver=new TCPDataReceiver();  //注册本地广播接收
			IntentFilter filter=new IntentFilter("com.eagle.smarthome.TCPDATABROADCAST");
			localBroadcastManager=LocalBroadcastManager.getInstance(DeviceControlActivity.this);
			localBroadcastManager.registerReceiver(localReceiver,filter);
			GetButtonName();
			GetHisCmds(); //得到历史输入命令列表
			SetButtonClickEvent();			
		}
	
		private TextView devicename;
		private TextView location;				
		private TextView textInfo;	
		private TextView datahint;	
		private LinearLayout moreinfo;
		private ImageView imgPicture;
		private ImageView imgBack;		
		private ImageView imgSave;		
		private ListView lvDevices;		
		private EditText inputcmdtext;		
		private Spinner hiscommand;
		private Spinner 	homedevices;
		private Button btnDO;
		private Button btnAO;
		private Button btnSO;
		private Button btnUpdateState;		
		private Button btnclearcmd;
		private Button btnaddcmd;
		
		private int lastId=-1;
		/*private void Hint(String s)
		{
			textInfo.setText(s);
			textInfo.invalidate();
		}*/
		private void GetButtonName()
		{			
			moreinfo=(LinearLayout)findViewById(R.id.moreinfo);		
			imgPicture=(ImageView)findViewById(R.id.imgPicture);		
			imgBack=(ImageView)findViewById(R.id.imgBack);		
			imgSave=(ImageView)findViewById(R.id.imgSave);		
			textInfo=(TextView)findViewById(R.id.textInfo);	
			datahint=(TextView)findViewById(R.id.datahint);	
			devicename=(TextView)findViewById(R.id.devicename);	
			location=(TextView)findViewById(R.id.location);		
			inputcmdtext=(EditText)findViewById(R.id.inputcmdtext);		
			lvDevices=(ListView)findViewById(R.id.lvDevices);
			hiscommand=(Spinner)findViewById(R.id.hiscommand);
			homedevices=(Spinner)findViewById(R.id.homedevices);			
			btnclearcmd=(Button) findViewById(R.id.btnclearcmd);	 
			btnaddcmd=(Button) findViewById(R.id.btnaddcmd);	 
			btnDO=(Button) findViewById(R.id.btnDO);	 
			btnAO=(Button) findViewById(R.id.btnAO);	 
			btnSO=(Button) findViewById(R.id.btnSO);	 
			btnUpdateState=(Button) findViewById(R.id.btnUpdateState);	 			
		}
		private void SetButtonClickEvent()  //设置按钮响应事件
		{
			try
			{
				 textInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
				 SettextInfoDoubleClick();
				SetbtnDOClick();
				SetbtnAOClick();
				SetbtnSOClick();
				SetbtnUpdateStateClick();
				SetbimgBackClick();
				SetmoreinfoDoubleClick();
				SetlvDeviceSystemOnItemClick();
				SetbtnaddcmdClick();
				SetbtnclearcmdClick();
				SetbimgSaveClick();  //保存图像按钮
				SetdevicesystemOnSelect();
				OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(this, this.mAdapter);
				hiscommand.setOnItemSelectedListener(spinnerListener);
			}
			catch( Exception ex)
			{
				String s=ex.getMessage();
		    	Toast.makeText(DeviceControlActivity.this,  s , Toast.LENGTH_SHORT).show();		
			}
		}	
		
		private void SetdevicesystemOnSelect()  //选择家居设备
		{
			homedevices.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() 
			{  
				@Override  
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
				{  
					homedevice=selectedhome.homedevices.get(position);
					try
					{
						   devices=homedevice.GetAllDevices();
						    deviceadapter=new DeviceAdapter(DeviceControlActivity.this, workdir, R.layout.deviceshow_layout,  devices );
					    	lvDevices.setAdapter(deviceadapter);
						   deviceadapter.notifyDataSetChanged();
					    	selectedDevice=null;		
					    	if (devices.size()>0)    	deviceadapter.setSelectItem(0); //lvDevices.setSelection(0);
					    	location.setText(selectedhome.workdir+" - "+homedevice.devicename);
					}
					catch(Exception e)
					{    	}
			    }  
			   @Override  
			    public void onNothingSelected(AdapterView<?> parent) {   }  
			 });  
		}
		private void SetmoreinfoDoubleClick()
		{
			moreinfo.setOnClickListener(new View.OnClickListener()   
			{			
				@Override
				public void onClick(View v)
				{  
					int id=moreinfo.getId();
					if (id!=lastId)
					{
						lastId=id;
						lastClickTime = System.currentTimeMillis();
						return;
					}
					else if (Math.abs(lastClickTime-System.currentTimeMillis()) < 300)  //双击
					{
						lastClickTime = 0; 
						lastId=id;
						moreinfo.setVisibility(View.GONE);
					    imgSave.setVisibility(View.GONE);
					}
					else
						lastClickTime = System.currentTimeMillis();	
				}
			});
		}
		private void SettextInfoDoubleClick()
		{
			textInfo.setOnClickListener(new View.OnClickListener()  
			{			
				@Override
				public void onClick(View v)
				{  
					int id=textInfo.getId();
					if (id!=lastId)
					{
						lastId=id;
						lastClickTime = System.currentTimeMillis();
						return;
					}
					else if (Math.abs(lastClickTime-System.currentTimeMillis()) < 300)  //双击
					{
						lastClickTime = 0; 
						lastId=id;
						moreinfo.setVisibility(View.GONE);						
					}
					else
						lastClickTime = System.currentTimeMillis();	
				}
			});
		}
		private void SetbimgBackClick()  //后退按钮
		{
			imgBack.setOnClickListener(new View.OnClickListener()   //增加监控点
			{			
				@Override
				public void onClick(View v)
				{  
					finish();
				}
			});
		}
		private void SetbimgSaveClick()  //保存图像按钮
		{
			imgSave.setOnClickListener(new View.OnClickListener()   //增加监控点
			{			
				@Override
				public void onClick(View v)
				{  
					if (imgPicture.getVisibility()==View.VISIBLE)
					{
						imgPicture.setDrawingCacheEnabled(true);
						try
						{
							Bitmap bmp= Bitmap.createBitmap(imgPicture.getDrawingCache());
							addBitmapToAlbumByScanner(bmp);
						}
						catch(Exception e) {}
                        imgPicture.setDrawingCacheEnabled(false);						
					}
				}
			});
		}
	
  	   private void SetbtnDOClick()  //DO设备控制
		{
			btnDO.setOnClickListener(new View.OnClickListener()   //增加监控点
			{			
				@Override
				public void onClick(View v)
				{  //SHACTRL = "505";    //给某个SHA系统的设备发指令[appid=N][devid=M][subid=X][type=Y][act=K]
					lastClickTime = 0; 
					lastId=btnDO.getId();
					if (selectedDevice==null) return;
					if (!(selectedDevice instanceof DeviceDO)) return;					 
					DeviceDO dv=(DeviceDO) selectedDevice;
					StringJson json=new StringJson(SHProtocol.SHFLAG);
			  		json.AddNameVolume("cmd", SHProtocol.SHACTRL);		 
			        json.AddNameVolume("appid", appid+"");
			        json.AddNameVolume("devid", dv.parentid+"");
			        json.AddNameVolume("subid", ""+dv.id);
			        json.AddNameVolume("type", ""+dv.devicetype.toString());   
			        json.AddNameVolume("act", dv.ON()?"0":"1");   
					TcpSend(json);
				}
			});
		}
		private void SetbtnAOClick()  //AO设备控制
		{
			btnAO.setOnClickListener(new View.OnClickListener()   //增加监控点
			{			
				@Override
				public void onClick(View v)
				{  //SHACTRL = "505";    //给某个SHA系统的设备发指令[appid=N][devid=M][subid=X][type=Y][act=K]
					lastClickTime = 0; 
					lastId=btnAO.getId();
					if (selectedDevice==null) return;
					if (!(selectedDevice instanceof DeviceAO)) return;					 
					DeviceAO dv=(DeviceAO) selectedDevice;
					StringJson json=new StringJson(SHProtocol.SHFLAG);
			  		json.AddNameVolume("cmd", SHProtocol.SHACTRL);		 
			        json.AddNameVolume("appid", appid+"");
			        json.AddNameVolume("devid", dv.parentid+"");
			        json.AddNameVolume("subid", ""+dv.id);
			        json.AddNameVolume("type", ""+dv.devicetype.toString());   
			        json.AddNameVolume("act", inputcmdtext.getText().toString());		            
					TcpSend(json);
				}
			});
		}
		private void SetbtnSOClick()  //SO设备控制
		{
			btnSO.setOnClickListener(new View.OnClickListener()   //增加监控点
			{			
				@Override
				public void onClick(View v)
				{  //SHACTRL = "505";    //给某个SHA系统的设备发指令[appid=N][devid=M][subid=X][type=Y][act=K]
					lastClickTime = 0; 
					lastId=btnSO.getId();
					if (selectedDevice==null) return;
					if (!(selectedDevice instanceof DeviceSO)) return;					 
					DeviceSO dv=(DeviceSO) selectedDevice;
					StringJson json=new StringJson(SHProtocol.SHFLAG);
			  		json.AddNameVolume("cmd", SHProtocol.SHACTRL);		 
			        json.AddNameVolume("appid", appid+"");
			        json.AddNameVolume("devid", dv.parentid+"");
			        json.AddNameVolume("subid", ""+dv.id);
			        json.AddNameVolume("type", ""+dv.devicetype.toString());   
			        if (dv.streamtype == StreamType.TEXT)   //发送文本
		                json.AddNameVolume("act",  inputcmdtext.getText().toString());		
		            else if (dv.streamtype == StreamType.IMAGE)  //发送图片
		            {		              
		            	Intent getAlbum = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		            	//Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		            	//getAlbum.setType("image/*");
		            	//getAlbum.putExtra("position",lastposition);
		            	startActivityForResult(getAlbum, 100);
		                return;
		            }
					TcpSend(json);
				}
			});
		}
		private void SetbtnUpdateStateClick()  //获取设备状态
		{
			btnUpdateState.setOnClickListener(new View.OnClickListener()  
			{			
				@Override
				public void onClick(View v)
				{  // DEVSTATE = "506";   //某个SHA系统的设备的子设备状态数据[appid=N][devid=M][type=Y][subid=X][value=V?]
					StringJson json=new StringJson(SHProtocol.SHFLAG);
			  		json.AddNameVolume("cmd", SHProtocol.DEVSTATE);		 
			        json.AddNameVolume("appid", appid+"");
			        json.AddNameVolume("devid", homedevice.deviceid+"");
			        lastClickTime = 0; 
					lastId=btnUpdateState.getId();
					if(homedevice.DODevices.size()>0)
					{	
				        json.AddNameVolume("type", ""+DeviceType.DO.toString());   
						TcpSend(json);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(homedevice.DIDevices.size()>0)
					{	
				        json.AddNameVolume("type", ""+DeviceType.DI.toString());   
						TcpSend(json);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(homedevice.AODevices.size()>0)
					{	
				        json.AddNameVolume("type", ""+DeviceType.AO.toString());   
						TcpSend(json);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(homedevice.AIDevices.size()>0)
					{	
				        json.AddNameVolume("type", ""+DeviceType.AI.toString());   
						TcpSend(json);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(homedevice.SODevices.size()>0)
					{	
				        json.AddNameVolume("type", ""+DeviceType.SO.toString());   
						TcpSend(json);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if(homedevice.SIDevices.size()>0)
					{	
				        json.AddNameVolume("type", ""+DeviceType.SI.toString());   
						TcpSend(json);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
		private void SetbtnaddcmdClick()  //增加历史命令
		{
			btnaddcmd.setOnClickListener(new View.OnClickListener()   
			{			
				@Override
				public void onClick(View v)
				{  
					String s=inputcmdtext.getText().toString();
					if (!hiscmds.contains(s))
					{
						hiscmds.add(s);
						int position=hiscmds.indexOf(s);
						hiscommand.setSelection(position);
						FileManager.WriteStrings(DeviceControlActivity.this,hiscmdfile,hiscmds);
					}					
				}
			});
		}
		private void SetbtnclearcmdClick()  //删除历史命令
		{
			btnclearcmd.setOnClickListener(new View.OnClickListener()   
			{			
				@Override
				public void onClick(View v)
				{  
					int position=hiscommand.getSelectedItemPosition();
					if (position<0) return;
					hiscmds.remove(position);
					hiscommand.setSelection(hiscmds.size()-1);
					FileManager.WriteStrings(DeviceControlActivity.this,hiscmdfile,hiscmds);
				}
			});
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
			Toast.makeText(DeviceControlActivity.this, tcpdisconnection, Toast.LENGTH_SHORT).show();		
			Intent newintent=new Intent(DeviceControlActivity.this,LoginActivity.class);
	         startActivity(newintent);
		}

		private String hiscmdfile="hiscmd.txt;";
		public List<String> hiscmds=null;
		protected ArrayAdapter<String> mAdapter;
		private void  GetHisCmds()  //得到历史输入命令列表
		{
			hiscmds= FileManager.ReadText(this,hiscmdfile);
			 if (hiscmds==null  || hiscmds.size()==0) 
			 {
				 hiscmds=new Vector<String>();
				 hiscmds.add("you can select command here");
			 }
			 this.mAdapter = new ArrayAdapter<String>(
						 this,android.R.layout.simple_spinner_dropdown_item, hiscmds);
			 hiscommand.setAdapter(this.mAdapter);
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

		private SmartHome selectedhome;  	//选中的设备系统
		private HomeDevice homedevice;    	//选中的设备
		//	private Spinner 	homedevices;
	    private DeviceAdapter deviceadapter;
	    public List<String> homedevs=null;
		protected ArrayAdapter<String> mHomedevicesAdapter;
		private int selectedHomedevice=0;
		@Override
		protected void onResume()  //★★★★★★★★活动恢复
		{  // TODO Auto-generated method stub
			super.onResume();
			Intent intent=this.getIntent();
			String dir=intent.getStringExtra("workdir");
			if (dir!=null)
			{
				workdir=dir;	
				appid=intent.getIntExtra("appid",0);
				String dvdescription=intent.getStringExtra("dvdescription");
				selectedhome=new SmartHome(this, workdir, "SmartHome"+appid+".smh");//SmartHome600.smh
				homedevs=new  Vector<String>();
				for (int i=0;i<selectedhome.homedevices.size();i++)
				{
					HomeDevice hd=selectedhome.homedevices.get(i);
					homedevs.add(hd.deviceid+"-"+hd.devicename);
				}
				 this.mHomedevicesAdapter = new ArrayAdapter<String>(
						 this,android.R.layout.simple_spinner_dropdown_item, homedevs);
				 homedevices.setAdapter(this.mHomedevicesAdapter);
			 
				homedevice=selectedhome.homedevices.get(selectedHomedevice);//一般只有一个设备子系统				
				
			    devicename.setText(dvdescription+"("+appid+")");  //+selectedhome.homename;
			    location.setText(selectedhome.workdir+" - "+homedevice.devicename);
			    inputcmdtext.setVisibility(View.VISIBLE);
			    imgSave.setVisibility(View.GONE);
			    devices=homedevice.GetAllDevices();
			    deviceadapter=new DeviceAdapter(DeviceControlActivity.this, workdir, R.layout.deviceshow_layout,  devices );
		    	lvDevices.setAdapter(deviceadapter);
		    	selectedDevice=null;
		    	moreinfo.setVisibility(View.GONE);
		      	if (devices.size()>0)   	deviceadapter.setSelectItem(0);
		      	
		    	if 	(bmppicture!=null)
		    	{		    	
		    		 deviceadapter.setSelectItem(lastposition);
					 deviceadapter.notifyDataSetInvalidated();   
				   	 moreinfo.setVisibility(View.VISIBLE);
				  	 imgPicture.setVisibility(View.VISIBLE);
				     imgSave.setVisibility(View.VISIBLE);
					 textInfo.setVisibility(View.GONE);			    	
			        // imgPicture.setImageBitmap(bmppicture);
			         lvDevices.smoothScrollToPosition(lastposition);  
			     	lvDevices.setSelection(lastposition);
			     	lvDevices.invalidate();
			     	btnSO.setEnabled(false);
			     	if (lastposition<0) return;
			     	selectedDevice=devices.get(lastposition);
					if (!(selectedDevice instanceof DeviceSO)) return;					 
					DeviceSO dv=(DeviceSO) selectedDevice;//   SHACTRL = "505";   给某个SHA系统的设备发指令[appid=N][devid=M][subid=X][type=Y][act=K]
					StringJson json=new StringJson(SHProtocol.SHFLAG);
			        ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
			        bmppicture=zoomImg(bmppicture);
			        bmppicture.compress(Bitmap.CompressFormat.JPEG, 75, stream); 
			        json.AddNameVolume("act", stream.toByteArray());
			  		json.AddNameVolume("cmd", SHProtocol.SHACTRL);		 
			        json.AddNameVolume("appid", appid+"");
			        json.AddNameVolume("devid", dv.parentid+"");
			        json.AddNameVolume("subid", ""+dv.id);
			        json.AddNameVolume("type", ""+dv.devicetype.toString());   
					TcpSend(json);	
					SetImageViewHeight(bmppicture); //imgPicture.setImageBitmap(bmppicture);
		    	}
			}
		}
		
		// 缩放图片
		public static Bitmap zoomImg(Bitmap bm)
		{	
			int maxWH=800;
		    int width = bm.getWidth();   // 获得图片的宽高
		    int height = bm.getHeight();
		    int max=Math.max(width, height);
		    if (max<=maxWH) return bm;   //较小的图片，无需减小
		   
		   float scale=1.0f;
		   if (width>=height)
			   scale=(float)maxWH / width;
		   else 
			   scale=(float)maxWH  / height;		  
		   Matrix matrix = new Matrix();    // 取得想要缩放的matrix参数
		    matrix.postScale(scale, scale);
		    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);		    //得到新的图片
		    return newbm;
		 }
	
		private Bitmap bmppicture=null;
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data)  //获得图像
		{
			 if (requestCode == 100 && resultCode == RESULT_OK)
		    {      
				 try 
			      {
					 bmppicture=null;
					 Uri uri = data.getData();
					 ContentResolver cr = this.getContentResolver();  
					 bmppicture = BitmapFactory.decodeStream(cr.openInputStream(uri));  		
					 //bmppicture = MediaStore.Images.Media.getBitmap(cr, uri);     
					 /*deviceadapter.setSelectItem(position);  
					 deviceadapter.notifyDataSetInvalidated();   
				   	 moreinfo.setVisibility(View.VISIBLE);
				  	 imgPicture.setVisibility(View.VISIBLE);
				     imgSave.setVisibility(View.VISIBLE);
					 textInfo.setVisibility(View.GONE);
			         imgPicture.setImageBitmap(bmppicture);  
			         Uri uri = data.getData(); */
					/*String[] filePathColumns={MediaStore.Images.Media.DATA}; 
					Cursor c = this.getContentResolver().query(uri, filePathColumns, null,null, null);  
					c.moveToFirst();  
					int columnIndex = c.getColumnIndex(filePathColumns[0]); 
					String picturePath= c.getString(columnIndex);
					bmppicture=BitmapFactory.decodeFile(picturePath);
					c.close();*/  			         
			       } catch (Exception e)
			       {        }  
		        return;
		    }
		}

		    
		private long lastClickTime;		
		private int lastposition=-1;
		private void SetlvDeviceSystemOnItemClick()//点击特定设备 事件
		{
			lvDevices.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{   
					lastposition=position;
					lastId=lvDevices.getId();
					Object obj=devices.get(position);
					deviceadapter.setSelectItem(position);  
					deviceadapter.notifyDataSetInvalidated();
					if (obj==null) return;
					hiscommand.setSelection(-1);  //不选择
					if (obj==selectedDevice   && (Math.abs(lastClickTime-System.currentTimeMillis()) < 300))
					{
						lastClickTime = 0;  //模拟双击事件
						ProcessDoubleClick(selectedDevice); //处理双击事件
					}
					else //不是点击同一行,或时间过长
					{
						selectedDevice=obj;    //devices.get(position);					
						ShowDeviceInfo(selectedDevice);
						GetDeviceState(selectedDevice);
						lastClickTime = System.currentTimeMillis();
						hiscommand.setSelection(hiscmds.size()-1);  //不选择
					}
				}		
			});
		}
		private void ProcessDoubleClick(Object device)
		{
			 if (device instanceof DeviceDO) return;
			 if (device instanceof DeviceDI) return;
			 if (device instanceof DeviceAO) return;
			 if (device instanceof DeviceAI) return;
			 if (device instanceof DeviceSO)
			  {
				  DeviceSO dv=(DeviceSO) device;
				  if (dv.streamtype==StreamType.TEXT)
				  {
					  	moreinfo.setVisibility(View.VISIBLE);
						textInfo.setVisibility(View.VISIBLE);
						imgPicture.setVisibility(View.GONE);
					    imgSave.setVisibility(View.GONE);
						if (dv.sovalue!=null)
							textInfo.setText( new  String(dv.sovalue,Charset.forName("UTF-8")));
						else 
							textInfo.setText(getResources().getString(R.string.unknown));
				  }
				  else if (dv.streamtype==StreamType.IMAGE)
				  {
					  	moreinfo.setVisibility(View.VISIBLE);
						textInfo.setVisibility(View.GONE);
					    imgSave.setVisibility(View.VISIBLE);
						imgPicture.setVisibility(View.VISIBLE);
						 if (dv.sovalue!=null)
						   {
							   Bitmap bitmap=getBitmapFromBytes(dv.sovalue);
							   if (bitmap!=null) 
								   SetImageViewHeight(bitmap); // imgPicture.setImageBitmap(bitmap);	    
						   }		
				  }
				  return;
			  }	 
			 if (device instanceof DeviceSI)
			  {
				  DeviceSI dv=(DeviceSI) device;
				  if (dv.streamtype==StreamType.TEXT)
				  {
					  	moreinfo.setVisibility(View.VISIBLE);
						textInfo.setVisibility(View.VISIBLE);
						imgPicture.setVisibility(View.GONE);
					    imgSave.setVisibility(View.GONE);
						if (dv.sivalue!=null)
							textInfo.setText( new  String(dv.sivalue,Charset.forName("UTF-8")));
						else 
							textInfo.setText(getResources().getString(R.string.unknown));
				  }
				  else if (dv.streamtype==StreamType.IMAGE)
				  {
					  	moreinfo.setVisibility(View.VISIBLE);
					  	imgPicture.setVisibility(View.VISIBLE);
					    imgSave.setVisibility(View.VISIBLE);
						textInfo.setVisibility(View.GONE);
					   if (dv.sivalue!=null)
					   {
						   Bitmap bitmap=getBitmapFromBytes(dv.sivalue);
						   if (bitmap!=null) 
							   SetImageViewHeight(bitmap); //imgPicture.setImageBitmap(bitmap);	    
					   }				
				  }
				  return;
			  }	 
		}

		private void ShowDeviceInfo(Object device)  //显示特定设备信息 和操作界面
		{			
			/*String type="";
			String function="";
			String control="";*/
			 try
			 {
				  if (device instanceof DeviceDO)
				  {
					  //DeviceDO dv=(DeviceDO) device;
				 	  //type= dv.devicetype.toString()+"("+appid+"-"+dv.parentid+"-"+dv.id+")";
				 	  //function=dv.functiondescription;
				 	  //control=dv.controldescription;	
				 	  inputcmdtext.setEnabled(false);
				 	  btnDO.setEnabled(true);
				 	  btnAO.setEnabled(false);
				 	  btnSO.setEnabled(false);
				  }
				  else  if (device instanceof DeviceDI)
				  {
					  //DeviceDI dv=(DeviceDI) device;
					  //type= dv.devicetype.toString()+"("+appid+"-"+dv.parentid+"-"+dv.id+")";
				 	  //function=dv.functiondescription;
				 	  //control=dv.controldescription;	
					  inputcmdtext.setEnabled(false);
				 	  btnDO.setEnabled(false);
				 	  btnAO.setEnabled(false);
				 	  btnSO.setEnabled(false);
				  }
				  else  if (device instanceof DeviceAO)
				  {
					  DeviceAO dv=(DeviceAO) device;
					  //type= dv.devicetype.toString()+"("+appid+"-"+dv.parentid+"-"+dv.id+")";
				 	  //function=dv.functiondescription;
				 	  //control=dv.controldescription;
					  inputcmdtext.setEnabled(true);
				 	  btnDO.setEnabled(false);
				 	  btnAO.setEnabled(true);
				 	  btnSO.setEnabled(false);
				 	  String s="%."+dv.dotplace+"f";
					  s=String.format(s, dv.aovalue);
					  inputcmdtext.setText(s);
				  }
				  else  if (device instanceof DeviceAI)
				  {
					  //DeviceAI dv=(DeviceAI) device;
					  //type= dv.devicetype.toString()+"("+appid+"-"+dv.parentid+"-"+dv.id+")";
				 	  //function=dv.functiondescription;
				 	  //control=dv.controldescription;
					  inputcmdtext.setEnabled(false);
				 	  btnDO.setEnabled(false);
				 	  btnAO.setEnabled(false);
				 	  btnSO.setEnabled(false);
				  }
				  else  if (device instanceof DeviceSO)
				  {
					  DeviceSO dv=(DeviceSO) device;
					  //type= dv.devicetype.toString()+"("+appid+"-"+dv.parentid+"-"+dv.id+")";
				 	  //function=dv.functiondescription;
				 	  //control=dv.controldescription;
					  inputcmdtext.setEnabled(true);
				 	  btnDO.setEnabled(false);
				 	  btnAO.setEnabled(false);
				 	  btnSO.setEnabled(true);
					  if (dv.streamtype==StreamType.TEXT)  //文本型流
					  {
						  if (dv.sovalue!=null)
							  inputcmdtext.setText(new String(dv.sovalue,Charset.forName("UTF-8")));
					  }
					  else if  (dv.streamtype==StreamType.IMAGE  && imgPicture.getVisibility()==View.VISIBLE)  //图像流
					  {
						   if (dv.sovalue!=null)
						   {
							   Bitmap bitmap=getBitmapFromBytes(dv.sovalue);
							   if (bitmap!=null) 
								   SetImageViewHeight(bitmap); //imgPicture.setImageBitmap(bitmap);	    
						   }
					  }
				  }
				  else  if (device instanceof DeviceSI)
				  {
					  DeviceSI dv=(DeviceSI) device;
					  //type= dv.devicetype.toString()+"("+appid+"-"+dv.parentid+"-"+dv.id+")";
				 	  //function=dv.functiondescription;
				 	  //control=dv.controldescription;
					  inputcmdtext.setEnabled(false);
				 	  btnDO.setEnabled(false);
				 	  btnAO.setEnabled(false);
				 	  btnSO.setEnabled(false);
					  if (dv.streamtype==StreamType.TEXT)  //文本型流
					  {
						  if (dv.sivalue!=null)
							  textInfo.setText(new String(dv.sivalue,Charset.forName("UTF-8")));
					  }
					  else if  (dv.streamtype==StreamType.IMAGE  && imgPicture.getVisibility()==View.VISIBLE)  //图像流
					  {
						   if (dv.sivalue!=null)
						   {
							   Bitmap bitmap=getBitmapFromBytes(dv.sivalue);
							   if (bitmap!=null) 
								   SetImageViewHeight(bitmap); //imgPicture.setImageBitmap(bitmap);	    
						   }
					  }
				  }
			 	  //textInfo.setText(type+"\r\n"+function+"\r\n"+control);
			 }
			 catch (Exception ex)
			 {
				 ex.printStackTrace();
			 }
		 }
		
		public static Bitmap getBitmapFromBytes(byte[] bs)
		{
		     try
		     {
		    	 ByteArrayInputStream bInputStream = new ByteArrayInputStream(bs);  
		          return BitmapFactory.decodeStream(bInputStream);
		     } catch (Exception e)
		     {
		          e.printStackTrace();
		          return null;
		     }
		}
		private void GetDeviceState(Object device)    //获取设备信息
		{			
			StringJson json=new StringJson(SHProtocol.SHFLAG);
	  		json.AddNameVolume("cmd", SHProtocol.DEVSTATE);		 
	        json.AddNameVolume("appid", appid+"");
	        json.AddNameVolume("devid", homedevice.deviceid+"");
	        if (device instanceof DeviceDO)
			{	
		        json.AddNameVolume("type", ""+DeviceType.DO.toString());   
				TcpSend(json);
			}
	        else if (device instanceof DeviceDI)
			{	
		        json.AddNameVolume("type", ""+DeviceType.DI.toString());   
				TcpSend(json);
			}
	        else if (device instanceof DeviceAO)
			{	
		        json.AddNameVolume("type", ""+DeviceType.AO.toString());   
				TcpSend(json);
			}
	        else if (device instanceof DeviceAI)
			{	
		        json.AddNameVolume("type", ""+DeviceType.AI.toString());   
				TcpSend(json);
			}
	        else if (device instanceof DeviceSO)
			{	
		        json.AddNameVolume("type", ""+DeviceType.SO.toString());   
				TcpSend(json);
			}
	        else if (device instanceof DeviceSI)
			{	
		        json.AddNameVolume("type", ""+DeviceType.SI.toString());   
				TcpSend(json);
			}			
		 }
	
		public static String getNowDate() {
			  Date currentTime = new Date();
			  //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			  return formatter.format(currentTime);
		}
		private Vector<Object> devices;
		private Object selectedDevice;
	    private TCPDataReceiver localReceiver;
		
		class TCPDataReceiver extends BroadcastReceiver   //处理接收到的数据
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{		
				byte[] data=intent.getByteArrayExtra("json");
				StringJson json=StringJson.BytesToStringJson(data, SHProtocol.SHFLAG);
				if (json==null) return;
				String cmd=json.GetStrValueByName("cmd");
				//if (context==thiscontext)
				 datahint.setText( getNowDate()+" command received:  "+cmd);
	           	//Toast.makeText(DeviceControlActivity.this, this.toString()+"\r\n"+ json.toString(), Toast.LENGTH_SHORT).show();		
	            if (cmd.equals(SHProtocol.DEVSTATE)) //DEVSTATE = "506";   //某个SHA系统的设备的子设备状态数据[appid=N][devid=M][type=Y][subid=X][value=V?]
	            {
	            	//Hint(json.GetStrValueByName("value"));
	            	SetDeviceState(json);	   
	            	deviceadapter.notifyDataSetChanged();  //通知适配器更新数据
	            }
			}
		}
        private void SetDeviceState(StringJson json) //★★★根据收到的TCP数据设置设备的状态
        {
        	String value = json.GetStrValueByName("value");    if (value == null) return;
        	//Hint(json.toString());
			 String sappid = json.GetStrValueByName("appid");  if (sappid == null) return;
			 int id=Integer.parseInt(sappid);    	              			   if (id!=appid)  return;     //不是选中的设备系统，不处理			  
			 String devid = json.GetStrValueByName("devid");    if (devid == null) return;
			 String type = json.GetStrValueByName("type");        if (type == null) return;	
			 String subid = json.GetStrValueByName("subid");
            if (subid != null) //是单独的一子字设备的状态数据
            {
                ProcessOneSubDeviceForAllType(appid,Integer.parseInt(devid), (short)Integer.parseInt(subid), type, json);
            }
            else  //是所有该子类设备的状态数据集合，必须是字符串集合
            {
                //DeviceType dt = DeviceType.valueOf(type);
                if (type.equals(DeviceType.DO.toString()))
                {
                    int dvlen = homedevice.DODevices.size();
                    for (int i = 0; i < Math.min(value.length(), dvlen); i++)
                        ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.DODevices.get(i).id, type, value.substring(i, i+1));
                    return;
                }
                else  if (type.equals(DeviceType.DI.toString()))
                {
                    int dvlen =homedevice.DIDevices.size();
                    for (int i = 0; i < Math.min(value.length(), dvlen); i++)
                        ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.DIDevices.get(i).id, type, value.substring(i, i+1));
                    return;
                }
                else  if (type.equals(DeviceType.SO.toString()))
                {
                    int dvlen =homedevice.SODevices.size();
                    String[] val = value.split("\\*");
                    //for (int i = 0; i < Math.min(val.length, dvlen); i++)
                    //    ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.SODevices.get(i).id, type, val[i]);
                    for (int i = 0; i < dvlen; i++)
                    	if (i<val.length)
                    		ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.SODevices.get(i).id, type, val[i]);
                    	else
                    		ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.SODevices.get(i).id, type, "");
                    return;
                }
                else  if (type.equals(DeviceType.SI.toString()))
                {
                    int dvlen = homedevice.SIDevices.size();
                    String[] val = value.split("\\*"); //("\\|"),
                    //for (int i = 0; i < Math.min(val.length, dvlen); i++)
                   //     ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.SIDevices.get(i).id, type, val[i]);
                    for (int i = 0; i < dvlen; i++)
                    	if (i<val.length)
                    		ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.SIDevices.get(i).id, type, val[i]);
                    	else
                    		ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.SIDevices.get(i).id, type, "");
                    return;
                }
                else  if (type.equals(DeviceType.AI.toString()))
                {
                    int dvlen = homedevice.AIDevices.size();
                    String[] val = value.split(",");
                    for (int i = 0; i < Math.min(val.length, dvlen); i++)
                        ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.AIDevices.get(i).id, type, val[i]);
                    return;
                }
                else  if (type.equals(DeviceType.AO.toString()))
                {
                    int dvlen = homedevice.AODevices.size();
                    String[] val = value.split(",");
                    for (int i = 0; i < Math.min(val.length, dvlen); i++)
                        ProcessOneSubDevice(appid, Integer.parseInt(devid), homedevice.AODevices.get(i).id, type, val[i]);
                    return;
                }
            }
        }

        //处理单个设备的状态数据
        void ProcessOneSubDeviceForAllType(int appid, int devid, short subid,String type, StringJson json)
        {
        	ProcessOneSubDevice(appid, devid, subid, type, json);           
        }

		private Object FindSubDODevice(int devid, short subid)
		{
			for (int i=0; i<homedevice.DODevices.size(); i++)
			{
				DeviceDO dv=homedevice.DODevices.get(i);
				if (dv.parentid==devid && subid==dv.id)
					return dv;
			}
			return null;
		}
		private Object FindSubDIDevice(int devid, short subid)
		{
			for (int i=0; i<homedevice.DIDevices.size(); i++)
			{
				DeviceDI dv=homedevice.DIDevices.get(i);
				if (dv.parentid==devid && subid==dv.id)
					return dv;
			}
			return null;
		}
		private Object FindSubAODevice(int devid, short subid)
		{
			for (int i=0; i<homedevice.AODevices.size(); i++)
			{
				DeviceAO dv=homedevice.AODevices.get(i);
				if (dv.parentid==devid && subid==dv.id)
					return dv;
			}
			return null;
		} 
		private Object FindSubAIDevice(int devid, short subid)
		{
			for (int i=0; i<homedevice.AIDevices.size(); i++)
			{
				DeviceAI dv=homedevice.AIDevices.get(i);
				if (dv.parentid==devid && subid==dv.id)
					return dv;
			}
			return null;
		}
		private Object FindSubSODevice(int devid, short subid)
		{
			for (int i=0; i<homedevice.SODevices.size(); i++)
			{
				DeviceSO dv=homedevice.SODevices.get(i);
				if (dv.parentid==devid && subid==dv.id)
					return dv;
			}
			return null;
		}
		private Object FindSubSIDevice(int devid, short subid)
		{
			for (int i=0; i<homedevice.SIDevices.size(); i++)
			{
				DeviceSI dv=homedevice.SIDevices.get(i);
				if (dv.parentid==devid && subid==dv.id)
					return dv;
			}
			return null;
		}
				
		void ProcessOneSubDevice(int appid, int devid, short subid,String type,StringJson json)  //处理一个子设备显示
        {
            if (type.equals(DeviceType.DO.toString()))
            {
    			String value=json.GetStrValueByName("value");
                if (value == null) return;
                DeviceDO dv = (DeviceDO)FindSubDODevice(devid,subid);
                if(dv==null) return;
                //dv.powerstate = value == "1" ? PowerState.PowerON : PowerState.PowerOFF;
                if (dv.dotype == DOType.Open)
                    dv.powerstate = value.equals("1") ? PowerState.PowerON : PowerState.PowerOFF;
                else
                    dv.powerstate = value.equals("1") ? PowerState.PowerOFF : PowerState.PowerON;
                return;
            }
            else if   (type.equals(DeviceType.DI.toString()))
            {
    			String value=json.GetStrValueByName("value");
                if (value == null) return;
                DeviceDI dv = (DeviceDI)FindSubDIDevice(devid,subid);
                if (dv == null) return;
                dv.hassignal =value.equals("1")  ? true : false;
            }
            else if   (type.equals(DeviceType.AO.toString()))
            {
    			String value=json.GetStrValueByName("value");
                if (value == null) return;
               DeviceAO dv = (DeviceAO)FindSubAODevice(devid,subid);
               if (dv == null) return;
                float f = 0.0f;
                try
                {
                	f= Float.parseFloat(value);
                	dv.aovalue = f;
                }
                catch (Exception ex)
                {
                	dv.aovalue = 0;
                }
                return;
            }
            else if   (type.equals(DeviceType.AI.toString()))
            {
    			String value=json.GetStrValueByName("value");
                if (value == null) return;
                DeviceAI dv = (DeviceAI)FindSubAIDevice(devid,subid);
                if (dv == null) return;
                float f = 0.0f;
                try
                {
                	f= Float.parseFloat(value);
                	dv.aivalue = f;
                }
                catch (Exception ex)
                {
                	dv.aivalue = 0;
                }
                return;
            }
            else if   (type.equals(DeviceType.SO.toString()))
            {
    			byte[] value=json.GetValueByName("value");
                if (value == null) return;
                DeviceSO dv = (DeviceSO)FindSubSODevice(devid,subid);
                if (dv == null) return;
                try {
					dv.sovalue =  value;
				} catch (Exception e) {			
					e.printStackTrace();
				}
                if  (dv.streamtype==StreamType.IMAGE  && imgPicture.getVisibility()==View.VISIBLE)  //图像流
 				  {
 					   if (dv.sovalue!=null)
 					   {
 						   Bitmap bitmap=getBitmapFromBytes(dv.sovalue);
 						   if (bitmap!=null) 
 							  SetImageViewHeight(bitmap);	    
 					   }
 				  }
                return;
            }
            else if   (type.equals(DeviceType.SI.toString()))
            {
    			byte[] value=json.GetValueByName("value");
                if (value == null) return;
               DeviceSI dv = (DeviceSI)FindSubSIDevice(devid,subid);
               if (dv == null) return;
               try {
					dv.sivalue =  value;
				} catch (Exception e) {			
					e.printStackTrace();
				}
               if  (dv.streamtype==StreamType.IMAGE  && imgPicture.getVisibility()==View.VISIBLE)  //图像流
				  {
					   if (dv.sivalue!=null)
					   {
						   Bitmap bitmap=getBitmapFromBytes(dv.sivalue);
						   if (bitmap!=null) 
							   SetImageViewHeight(bitmap);	    
					   }
				  }
               return;
            }
        }
   
		private void SetImageViewHeight(Bitmap bitmap)
		{
			int bwidth = bitmap.getWidth();
			int bHeight = bitmap.getHeight();
			//int width = Screen.getScreenWidth(this);
			Point point=new Point();
			 this.getWindowManager().getDefaultDisplay().getSize(point);
			 int width =point.x;
			int height =(int)((float)width * bHeight / bwidth);
			ViewGroup.LayoutParams para = imgPicture.getLayoutParams();
			if (para.height != height)
			{
				para.height = height;
				imgPicture.setLayoutParams(para);
			}
			imgPicture.setImageBitmap(bitmap);	    
		}
		
		void ProcessOneSubDevice(int appid, int devid, short subid,String type,String value)  //处理一个子设备显示
        {
            if (value == null) return;
            //DeviceType dt = DeviceType.valueOf(type);
            //Hint(type);
            if (type.equals(DeviceType.DO.toString()))
            {
                DeviceDO dv = (DeviceDO)FindSubDODevice(devid,subid);
                if(dv==null) return;
                //dv.powerstate = value == "1" ? PowerState.PowerON : PowerState.PowerOFF;
                if (dv.dotype == DOType.Open)
                    dv.powerstate = value.equals("1") ? PowerState.PowerON : PowerState.PowerOFF;
                else
                    dv.powerstate = value.equals("1") ? PowerState.PowerOFF : PowerState.PowerON;
                return;
            }
            else if   (type.equals(DeviceType.DI.toString()))
            {
                DeviceDI dv = (DeviceDI)FindSubDIDevice(devid,subid);
                if (dv == null) return;
                dv.hassignal =value.equals("1")  ? true : false;
            }
            else if   (type.equals(DeviceType.AO.toString()))
            {
               DeviceAO dv = (DeviceAO)FindSubAODevice(devid,subid);
               if (dv == null) return;
                float f = 0.0f;
                try
                {
                	f= Float.parseFloat(value);
                	dv.aovalue = f;
                }
                catch (Exception ex)
                {
                	dv.aovalue = 0;
                }
                return;
            }
            else if   (type.equals(DeviceType.AI.toString()))
            {
                DeviceAI dv = (DeviceAI)FindSubAIDevice(devid,subid);
                if (dv == null) return;
                float f = 0.0f;
                try
                {
                	f= Float.parseFloat(value);
                	dv.aivalue = f;
                }
                catch (Exception ex)
                {
                	dv.aivalue = 0;
                }
                return;
            }
            else if   (type.equals(DeviceType.SO.toString()))
            {
                DeviceSO dv = (DeviceSO)FindSubSODevice(devid,subid);
                if (dv == null) return;
                try {
					dv.sovalue =  value.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {			
					e.printStackTrace();
					dv.sovalue=null;
				}
                return;
            }
            else if   (type.equals(DeviceType.SI.toString()))
            {
               DeviceSI dv = (DeviceSI)FindSubSIDevice(devid,subid);
               if (dv == null) return;
               try {
					dv.sivalue =  value.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {			
					e.printStackTrace();
					dv.sivalue=null;
				}
               return;
            }
        }
   
		protected static boolean isTopActivity(Activity activity)
		{
			String packageName = "com.eagle.smarthome.DeviceControlActivity";
			ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
			if (tasksInfo.size() > 0)
			{
				//System.out.println("---------------包名-----------"+ tasksInfo.get(0).topActivity.getPackageName());
				// 应用程序位于堆栈的顶层
				if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) 
				{
					return true;
				}
			}
			return false;
		}
		String picturefile ="";
		Uri picUri;
		private static final String SAVE_PIC_PATH=Environment.getExternalStorageDirectory().getAbsolutePath() +File.separator+ Environment.DIRECTORY_DCIM+File.separator+"Camera"+File.separator;
		private boolean addBitmapToAlbumByScanner( Bitmap bmp)
		{
			if (bmp==null) return false;
			//String path=Environment.getExternalStorageDirectory().getAbsolutePath() +File.separator+ Environment.DIRECTORY_DCIM+File.separator+"Camera"+File.separator;
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
			picturefile = sDateFormat.format(new java.util.Date())+".jpg";
			//devicename.setText(SAVE_PIC_PATH);			
			saveBitmap(SAVE_PIC_PATH,picturefile,bmp);
			//picturefile = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, fn, "Monitor Picture");
	        //if(picturefile == null)	 return false;	        
	        //picUri  = Uri.parse(picturefile);
	        //if(picUri == null)   return false;
	        MediaScannerConnectionClient scanner=new MediaScannerConnectionClient()
    		{
	        	private MediaScannerConnection msc=null;
	        	{
	        		msc=new  MediaScannerConnection(getApplicationContext(),this);
	        		msc.connect();
	        	}
				@Override
				public void onMediaScannerConnected() {
					//picturefile=getRealPathFromURI(picUri);
					picturefile=SAVE_PIC_PATH+picturefile;
					msc.scanFile(picturefile, null);	
			        //location.setText(picturefile);
					Toast.makeText(DeviceControlActivity.this, "Picture Saved: \r\n"+picturefile, Toast.LENGTH_SHORT).show();
				}
				@Override
				public void onScanCompleted(String path, Uri uri) {
					// TODO Auto-generated method stub
                	//location.setText( "Scanned " + path + "\r\n uri=" + uri.toString()); 
				}
    		};	    	
	        return true;
	    }
		
		 public void saveBitmap(String path,String filename,Bitmap bitmap)
		 {		
			   File f = new File(path, filename);
			   if (f.exists())  f.delete();			
			   try 
			   {
				    FileOutputStream out = new FileOutputStream(f);
				    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				    out.flush();
				    out.close();
			   } catch (Exception e) {
				   e.printStackTrace(); 
			    }
			 }

		private boolean addBitmapToAlbumByContent( Bitmap bmp)
		{
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
			String fn = sDateFormat.format(new java.util.Date())+".jpg";
			String uriStr = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, fn, "Monitor Picture");
	        if(uriStr == null)	 return false;	        
	        
	        ContentValues values = new ContentValues(4);
	        values.put(Images.ImageColumns.DATE_ADDED, System.currentTimeMillis()/1000);
	        values.put(Images.Media.MIME_TYPE, "image/jpeg");
	        values.put(Images.ImageColumns.TITLE, fn);
	        values.put(MediaStore.Images.Media.DATA, uriStr);

	        ContentResolver contentResolver = getContentResolver();
	        //Uri  picUri  =contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);    
	        Uri  picUri  =contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);    
	        String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
	        location.setText(path);
	        try
	        {
	        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(path)));
	        MediaScannerConnection.scanFile(this, new String[]{path + File.separator+"*.jpg" }, null, null);
	        
	        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, picUri));  
	    	Toast.makeText(DeviceControlActivity.this, "Save Picture\r\n"+uriStr, Toast.LENGTH_LONG).show();
	        }
	        catch( Exception ex)
	        {
	            location.setText(ex.getMessage());
	        }
	    	
	        return true;
	    }
		
		private String getRealPathFromURI(Uri contentUri) {
		    String[] proj = { MediaStore.Images.Media.DATA };
		    CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
		    Cursor cursor = loader.loadInBackground();
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		}
		
		// 用户选中下拉列表中得一项
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
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String sel=mLocalAdapter.getItem(position);
				inputcmdtext.setText(sel);  
				inputcmdtext.invalidate();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		}


	}
