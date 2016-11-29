package com.eagle.smarthome;

import java.util.List;
import java.util.Vector;
import com.eagle.smarthome.util.DeviceAdapter;
import com.eagle.smarthome.util.FileManager;
import com.eagle.smarthome.util.MessageAdapter;
import com.eagle.smarthome.util.SHProtocol;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;

import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.content.LocalBroadcastManager;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

	public class MessageActivity  extends BaseActivity
	{
		LocalBroadcastManager localBroadcastManager;

		private String workdir="";
		private TcpClient tcpClient;
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{  // TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.message_layout);
			tcpClient=TcpClient.GetTcpClient();
			localReceiver=new TCPDataReceiver();  //ע�᱾�ع㲥����
			IntentFilter filter=new IntentFilter("com.eagle.smarthome.TCPDATABROADCAST");
			localBroadcastManager=LocalBroadcastManager.getInstance(MessageActivity.this);
			localBroadcastManager.registerReceiver(localReceiver,filter);
			GetButtonName();
			GetHisCmds(); //�õ���ʷ���������б�
			SetButtonClickEvent();			
		}
	
		private TextView devicename;
		private ImageView imgBackMessage;
		private ListView lvMessage;		
		private EditText inputmessage;		
		private Button btnclearmessage;
		private Button btnaddmessage;
		private Button btnsendmessage;
		
		private void GetButtonName()
		{			
			imgBackMessage=(ImageView)findViewById(R.id.imgBackMessage);		
			devicename=(TextView)findViewById(R.id.devicename);	
			inputmessage=(EditText)findViewById(R.id.inputmessage);		
			lvMessage=(ListView)findViewById(R.id.lvMessage);
			btnclearmessage=(Button) findViewById(R.id.btnclearmessage);	 
			btnaddmessage=(Button) findViewById(R.id.btnaddmessage);	 	
			btnsendmessage=(Button) findViewById(R.id.btnsendmessage);	 			
		}
		private void SetButtonClickEvent()  //���ð�ť��Ӧ�¼�
		{
			try
			{
				SetbimgBackClick();
				SetlvMessageOnItemClick();
				SetbtnaddcmdClick();
				SetbtnclearcmdClick();
				SetbtnSendMessageClick();
			}
			catch( Exception ex)
			{
				String s=ex.getMessage();
		    	Toast.makeText(MessageActivity.this,  s , Toast.LENGTH_SHORT).show();		
			}
		}	
		
	private void SendMessage()
		{
			String text=inputmessage.getText().toString();
        	if (text.equals("")) return;
        	StringJson json=new StringJson(SHProtocol.SHFLAG);
	  		json.AddNameVolume("cmd", SHProtocol.MESSAGE);		 
	        json.AddNameVolume("text", text);		        
			TcpSend(json);	
		}
	 private void SetbtnSendMessageClick()  
		{
		 btnsendmessage.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
			{			
				@Override
				public void onClick(View v)
				{  
					SendMessage();
				}
			});
		}
	 private void SetbimgBackClick()  //���˰�ť
		{
			imgBackMessage.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
			{			
				@Override
				public void onClick(View v)
				{  
					finish();
				}
			});
		}
	
  	 	private void SetbtnaddcmdClick()  //������ʷ����
		{
			btnaddmessage.setOnClickListener(new View.OnClickListener()   
			{			
				@Override
				public void onClick(View v)
				{  
					String s=inputmessage.getText().toString();
					if (!messages.contains(s))
					{
						messages.add(s);
						int position=messages.indexOf(s);
						lvMessage.setSelection(position);
						FileManager.WriteStrings(MessageActivity.this,hiscmdfile,messages);
					}					
				}
			});
		}
		private void SetbtnclearcmdClick()  //ɾ����ʷ����
		{
			btnclearmessage.setOnClickListener(new View.OnClickListener()   
			{			
				@Override
				public void onClick(View v)
				{  
					if (lastposition<0) return;
					messages.remove(lastposition);
					lvMessage.setSelection(messages.size()-1);
					FileManager.WriteStrings(MessageActivity.this,hiscmdfile,messages);
					messageAdapter.notifyDataSetChanged();
				}
			});
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
			Toast.makeText(MessageActivity.this, tcpdisconnection, Toast.LENGTH_SHORT).show();		
			Intent newintent=new Intent(MessageActivity.this,LoginActivity.class);
	         startActivity(newintent);
		}

		private String hiscmdfile="message.txt;";
		public List<String> messages=null;
		//protected ArrayAdapter<String> mAdapter;
		private MessageAdapter messageAdapter;
		private void  GetHisCmds()  //�õ���ʷ���������б�
		{
			messages= FileManager.ReadText(this,hiscmdfile);
			 if (messages==null  || messages.size()==0) 
			 {
				 messages=new Vector<String>();
				 messages.add("you can select message here");
				 messages.add("���������Ҳ��ؼҳԷ�");
			 }
			 messageAdapter=new MessageAdapter(MessageActivity.this,  R.layout.messageshow_layout,  messages );
		    //	lvDevices.setAdapter(deviceadapter);
			 //this.mAdapter = new ArrayAdapter<String>(
				//		 this,android.R.layout.simple_spinner_dropdown_item, hiscmds);
			lvMessage.setAdapter(this.messageAdapter);
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
	   // private DeviceAdapter deviceadapter;
		@Override
		protected void onResume()  //����������ָ�
		{  // TODO Auto-generated method stub
			super.onResume();
			Intent intent=this.getIntent();
			String dir=intent.getStringExtra("workdir");
			if (dir!=null)
			{
				workdir=dir;	
			    devicename.setText(workdir );	
			    inputmessage.setVisibility(View.VISIBLE);
			    GetHisCmds();
			}
		}
		
		private long lastClickTime;		
		private int lastposition=-1;
		//private Vector<Object> devices;
		private Object selectedDevice;
		private void SetlvMessageOnItemClick()//����ض���Ϣ�ı��¼�
		{
			lvMessage.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{   
					lastposition=position;
					Object obj=messages.get(position);
					messageAdapter.setSelectItem(position);  
					messageAdapter.notifyDataSetInvalidated();
					if (obj==null) return;
					if (obj==selectedDevice   && (Math.abs(lastClickTime-System.currentTimeMillis()) < 300))
					{
						lastClickTime = 0;  //ģ��˫���¼�
						//ProcessDoubleClick(selectedDevice); //����˫���¼�
					}
					else //���ǵ��ͬһ��,��ʱ�����
					{
						selectedDevice=obj;    //devices.get(position);					
						inputmessage.setText((String)obj);					
						lastClickTime = System.currentTimeMillis();
					}
				}		
			});
		}
		
	    private TCPDataReceiver localReceiver;
		
		class TCPDataReceiver extends BroadcastReceiver   //������յ�������
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{		
				return; //�����κδ���
			}
		}

	}
