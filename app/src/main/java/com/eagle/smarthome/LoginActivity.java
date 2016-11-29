package com.eagle.smarthome;

import java.util.List;
import java.util.Vector;

import com.eagle.smarthome.util.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LoginActivity extends BaseActivity
{
	protected ArrayAdapter<String> mAdapter;
	private Spinner spinnerHomes;
	private CheckBox chkShowMima;
	private CheckBox chkShowDemo;
	private EditText CurrentHome;
	private Button buttonaddHome;
	private Button buttondeleteHome;
	private Button btnLogLan;
	private TextView btnDemo;
//	private Button btnLogWan;
	private Button btnExit;	
	private EditText inputaccount;
	private EditText inputpassword;
	private EditText ip1;
	private EditText port1;
//	private EditText ip2;
//	private EditText port2;
//	private TextView phoneame;
	private int connectionmode=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)   //�����
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loging_v2);
		spinnerHomes = (Spinner) findViewById(R.id.cbTimeTasks);
	    chkShowMima = (CheckBox) findViewById(R.id.chkSavePassword);
	    chkShowDemo = (CheckBox) findViewById(R.id.chkShowDemo);
	    CurrentHome= (EditText) findViewById(R.id.inputhome);	 
	    buttonaddHome=(Button) findViewById(R.id.btnaddHome);	 
	    buttondeleteHome=(Button) findViewById(R.id.btndeleteHome);	 	
	    btnLogLan=(Button) findViewById(R.id.btnLogLan);

	    btnDemo=(TextView) findViewById(R.id.btnDemo);

//	    btnLogWan=(Button) findViewById(R.id.btnLogWlan);
	    btnExit=(Button) findViewById(R.id.btnExit);	 		    
//	    phoneame=(TextView) findViewById(R.id.phoneame);
	    inputaccount= (EditText) findViewById(R.id.inputaccount);	 
	    inputpassword= (EditText) findViewById(R.id.inputpassword);	 
	    ip1= (EditText) findViewById(R.id.ip1);
	    port1= (EditText) findViewById(R.id.port1);
//	    ip2= (EditText) findViewById(R.id.ip2);
//	    port2= (EditText) findViewById(R.id.port2);
	    
	    LoadHomes();        //��ȡ���м�ص�������Ϣ�б�
		GetHomes();          //�õ���ص��б�		
		SetConfigPara();   //��ȡ���������ò���
		SetItemSelectedListener();
		localBroadcastManager=LocalBroadcastManager.getInstance(LoginActivity.this);
		//String sdk=android.os.Build.VERSION.SDK_INT+""; // SDK��
		String model=android.os.Build.MODEL; // �ֻ��ͺ�
		//String release=android.os.Build.VERSION.RELEASE; // androidϵͳ�汾��
//		phoneame.setText("Phone: "+model);
		if (model.contains("Xiaomi") || model.contains("С��"))//
		{
//			phoneame.setText("Your Phone are not supported");
			btnLogLan.setEnabled(false);
//			btnLogWan.setEnabled(false);
		}
	}

	public final String homesfilename="homes.xml";
	public List<String> homes=null;

	private void  GetHomes()  //�õ���ص��б�
	{
		 homes= FileManager.ReadText(this,homesfilename);
		 if (homes==null  || homes.size()==0) 
		 {
				 homes=new Vector<String>();
				 String myhome=getResources().getString(R.string.myhome);		
				 homes.add(myhome);
				 //homes.add("��������");
		 }
		 this.mAdapter = new ArrayAdapter<String>(
					 this,android.R.layout.simple_spinner_dropdown_item, homes);

		 spinnerHomes.setAdapter(this.mAdapter);
	}
	
	private void SetItemSelectedListener()
	{
		// ѡ�д����¼����ڴ���������ʾ��Ŀ
		OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(this, this.mAdapter);
		spinnerHomes.setOnItemSelectedListener(spinnerListener);
		SetAddButtonClick();
		SetDeleteButtonClick();
		SetLogLanButtonClick();
//		SetLogWanButtonClick();
		SetExitButtonClick();
		SetbtnDemoClick();
		SetchkShowDemolick();
	}
	
	private void SetchkShowDemolick() {
		chkShowDemo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean bshowdemo = isChecked;
				chkShowDemo.setChecked(bshowdemo);
				btnDemo.setVisibility(bshowdemo ? View.VISIBLE : View.GONE);
			}
		});
	}

	
	//btnLogWlan
	private void  SetAddButtonClick()  //���Ӽ�ص�
	{
		buttonaddHome.setOnClickListener(new View.OnClickListener()   //���Ӽ�ص�
		{			
			@Override
			public void onClick(View v)
			{
				String s=CurrentHome.getText().toString();
				int position=homes.indexOf(s);
				if (position>=0) 
				{
					 String exists=getResources().getString(R.string.exists);		
					Toast.makeText(LoginActivity.this,s+" :  "+exists, Toast.LENGTH_SHORT).show();		    
					return;
				}
				homes.add(s);		
				FileManager.WriteStrings(LoginActivity.this,homesfilename,  homes);
		        int index=homes.indexOf(s);
		        if (index>=0) spinnerHomes.setSelection(index);
		        spinnerHomes.invalidate();
			}
		});
	}
	
	private void  SetExitButtonClick()  //����
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
	
	private void  SetDeleteButtonClick() //ɾ����ص�
	{
		buttondeleteHome.setOnClickListener(new View.OnClickListener()   //ɾ����ص�
		{			
			@Override
			public void onClick(View v)
			{
				if (homes.size()<=1) 
				{
					String haveonehome=getResources().getString(R.string.haveonehome);					
					Toast.makeText(LoginActivity.this,haveonehome, Toast.LENGTH_SHORT).show();		    
					return;
				}
				String s=CurrentHome.getText().toString();
				int position=homes.indexOf(s);
				if (position<0) 
				{
					String noexists=getResources().getString(R.string.noexists);		
					Toast.makeText(LoginActivity.this,s+" : "+noexists, Toast.LENGTH_SHORT).show();		    
					return;
				}
				
				Builder dialog=new AlertDialog.Builder(LoginActivity.this);
				String suredeletehome=getResources().getString(R.string.suredeletehome);
				dialog.setTitle(suredeletehome);
				dialog.setMessage(suredeletehome+":"+s);
				dialog.setCancelable(false);
				String del=getResources().getString(R.string.delete);				
				dialog.setPositiveButton(del, new DialogInterface.OnClickListener() 
				{					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						String s=CurrentHome.getText().toString();
						int position=homes.indexOf(s);
						homes.remove(position);	
						FileManager.WriteStrings(LoginActivity.this,homesfilename,  homes);
				        if (homes.size()>=position+1) spinnerHomes.setSelection(position);
				        else  spinnerHomes.setSelection(homes.size()-1);
				        spinnerHomes.invalidate();							
					}
				});
				String cancel=getResources().getString(R.string.cancel);			
				dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() 
				{					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{   	}
				});
				dialog.show();
			}
		});
	}

	private void  SetLogLanButtonClick()  //Lan��¼
	{
		btnLogLan.setOnClickListener(new View.OnClickListener()   //Lan��¼
		{			
			@Override
			public void onClick(View v)
			{
				connectionmode=0;
				String s=CurrentHome.getText().toString();
				currentHomeLocation=homeLocations.FindHome(s);
				if (currentHomeLocation==null)
				{
					currentHomeLocation=new HomeLocation();
					currentHomeLocation.HomeName=s;				
					homeLocations.Items.add(currentHomeLocation);
				}
				UiToHomeLocation(currentHomeLocation);  //UI�����������ת�Ƶ�ҵ�����
				homeLocations.SaveToFile();
				StartTcpCommunication();  //����Socket
			}
		});
	}

//	private void  SetLogWanButtonClick()  //WLan��¼
//	{
//		btnLogWan.setOnClickListener(new View.OnClickListener()   //Lan��¼
//		{
//			@Override
//			public void onClick(View v)
//			{
//				connectionmode=1;
//				String s=CurrentHome.getText().toString();
//				currentHomeLocation=homeLocations.FindHome(s);
//				if (currentHomeLocation==null)
//				{
//					currentHomeLocation=new HomeLocation();
//					currentHomeLocation.HomeName=s;
//					homeLocations.Items.add(currentHomeLocation);
//				}
//				UiToHomeLocation(currentHomeLocation);  //UI�����������ת�Ƶ�ҵ�����
//				homeLocations.SaveToFile();
//				StartTcpCommunication2();  //����Socket
//			}
//		});
//	}
	
	private void UiToHomeLocation(HomeLocation currentHomeLocation)
	{
		currentHomeLocation.Account=inputaccount.getText().toString();
		currentHomeLocation.Password=inputpassword.getText().toString();
		currentHomeLocation.IP1=ip1.getText().toString();
		currentHomeLocation.Port1=port1.getText().toString();
//		currentHomeLocation.IP2=ip2.getText().toString();
//		currentHomeLocation.Port2=port2.getText().toString();
	}
	
	private void HomeLocationToUI(HomeLocation currentHomeLocation)
	{
		inputaccount.setText(currentHomeLocation.Account);		
		inputpassword.setText(currentHomeLocation.Password);		
		ip1.setText(currentHomeLocation.IP1);
		port1.setText(currentHomeLocation.Port1);
//		ip2.setText(currentHomeLocation.IP2);
//		port2.setText(currentHomeLocation.Port2);
	}
	
	public final String configfilename="config.xml";
	private void SetConfigPara()     //��ȡ���������ò���
	{
		boolean bshowMima=FileManager.GetConfigBoolean(this, configfilename , "bShowMima", true);
	    chkShowMima.setChecked(bshowMima);
	    
		boolean bshowdemo=FileManager.GetConfigBoolean(this, configfilename , "bShowDemo", true);
	    chkShowDemo.setChecked(bshowdemo);
	    btnDemo.setVisibility(bshowdemo? View.VISIBLE : View.GONE);
	    
		String homename=FileManager.GetConfigString(this, configfilename , "CurrentHome", "�Ҽ�501");
	    CurrentHome.setText(homename);
	    
		//Toast.makeText(LoginActivity.this,homename, Toast.LENGTH_SHORT).show();		    
        int index=homes.indexOf(homename);
        if (index>=0) spinnerHomes.setSelection(index);
	}
	private void SaveConfigPara()  //��ȡ���������ò���
	{
		FileManager.WriteConfigBoolean(this, configfilename , "bShowMima", chkShowMima.isChecked());
		FileManager.WriteConfigBoolean(this, configfilename , "bShowDemo", chkShowDemo.isChecked());
		FileManager.WriteConfigString(this, configfilename , "CurrentHome",CurrentHome.getText().toString());
	}
	
	HomeLocations homeLocations=null;
	HomeLocation currentHomeLocation=null;
	public final String homelocationfile="homelocation.xml";
	private void LoadHomes()        //��ȡ���м�ص�������Ϣ�б�
	{
		homeLocations=new HomeLocations(this,homelocationfile);
	}
	
	// �û�ѡ�������б��е�һ��
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
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String sel=mLocalAdapter.getItem(position);
		    CurrentHome= (EditText) findViewById(R.id.inputhome);
		    CurrentHome.setText(sel);
			currentHomeLocation=homeLocations.FindHome(sel);
			if (currentHomeLocation==null)
			{
				currentHomeLocation=new HomeLocation();
				currentHomeLocation.HomeName=sel;
			    homeLocations.Items.add(currentHomeLocation);
			}
		    HomeLocationToUI(currentHomeLocation);
			//Toast.makeText(LoginActivity.this,sel, Toast.LENGTH_SHORT).show();		    
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub			
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy()
	{
		StopTCP();
		homeLocations.SaveToFile();
		SaveConfigPara();
		if (homes!=null && homes.size()>0)
		    FileManager.WriteStrings(LoginActivity.this,homesfilename,  homes);
		super.onDestroy();
	}
	void StopTCP()
	{
		if (localBroadcastManager!=null && localReceiver!=null)
		{
			localBroadcastManager.unregisterReceiver(localReceiver);
			localReceiver=null;
		}
		if (tcpClient!=null) 
		{
				LogoutSystem();
				tcpClient.Close();
		}	
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent)
		{
			return false;
		}
	};

	TcpClient tcpClient=null;
	private void StartTcpCommunication()  //����TCP
	{
		StopTCP();
		int port= 50001;
		try  //���port�Ƿ�Ϸ�
		{
		     port= Integer.parseInt(currentHomeLocation.Port1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			String ipporterror=getResources().getString(R.string.ipporterror);		
			Toast.makeText(LoginActivity.this, ipporterror, Toast.LENGTH_SHORT).show();		    
			return;
		}
		if (port<1024 || port>65535)
		{
			String ipporterror=getResources().getString(R.string.ipporterror);		
			Toast.makeText(LoginActivity.this, ipporterror, Toast.LENGTH_SHORT).show();		    
			return;
		}
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				int port=50001;
				try
				{
				     port= Integer.parseInt(currentHomeLocation.Port1);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					//String ipporterror=getResources().getString(R.string.ipporterror);		
					//Toast.makeText(LoginActivity.this, ipporterror, Toast.LENGTH_SHORT).show();		    
					return;
				}
				Message msg=new Message();				
			    tcpClient=TcpClient.GetTcpClient(localBroadcastManager, currentHomeLocation.IP1,port);
			    if (tcpClient.socket==null) 
			    {					
						msg.what=0;  //ʧ��
						handler.sendMessage(msg);
						return;
				}
				localReceiver=new TCPDataReceiver();  //ע�᱾�ع㲥����
				IntentFilter filter=new IntentFilter("com.eagle.smarthome.TCPDATABROADCAST");
				localBroadcastManager.registerReceiver(localReceiver,filter);
				msg.what=1;  //�ɹ�
				handler.sendMessage(msg);				
			}
		 }).start();
	}
	private void StartTcpCommunication2()  //����TCP
	{
		StopTCP();
		int port= 50001;
		try  //���port�Ƿ�Ϸ�
		{
		     port= Integer.parseInt(currentHomeLocation.Port2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			String ipporterror=getResources().getString(R.string.ipporterror);		
			Toast.makeText(LoginActivity.this, ipporterror, Toast.LENGTH_SHORT).show();		    
			return;
		}
		if (port<1024 || port>65535)
		{
			String ipporterror=getResources().getString(R.string.ipporterror);		
			Toast.makeText(LoginActivity.this, ipporterror, Toast.LENGTH_SHORT).show();		    
			return;
		}
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				int port=50001;
				try
				{
				     port= Integer.parseInt(currentHomeLocation.Port2);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					//String ipporterror=getResources().getString(R.string.ipporterror);		
					//Toast.makeText(LoginActivity.this, ipporterror, Toast.LENGTH_SHORT).show();		    
					return;
				}
				Message msg=new Message();				
			    tcpClient=TcpClient.GetTcpClient(localBroadcastManager, currentHomeLocation.IP2,port);
			    if (tcpClient.socket==null) 
			    {					
						msg.what=0;  //ʧ��
						handler.sendMessage(msg);
						return;
				}
				localReceiver=new TCPDataReceiver();  //ע�᱾�ع㲥����
				IntentFilter filter=new IntentFilter("com.eagle.smarthome.TCPDATABROADCAST");
				localBroadcastManager.registerReceiver(localReceiver,filter);
				msg.what=1;  //�ɹ�
				handler.sendMessage(msg);				
			}
		 }).start();
	}

	public Handler handler=new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					String tcpclientfail=getResources().getString(R.string.tcpclientfail);		
					Toast.makeText(LoginActivity.this, tcpclientfail, Toast.LENGTH_SHORT).show();
					break;
				case 1:
					String tcpclientok=getResources().getString(R.string.tcpclientok);		
					Toast.makeText(LoginActivity.this, tcpclientok, Toast.LENGTH_SHORT).show();
				    Log.d(tcpclientok,tcpclientok);
				    //LoginSystem();		
					break;
				default:
					break;
			}
		}
	};
	
	public void TcpSend(StringJson json)    //����TCP����
	{
		if (tcpClient==null) return;
		if (tcpClient.socket==null) return;
		if (!tcpClient.socket.isConnected()) //�Ͽ�������
			{
				if (connectionmode==1)
					StartTcpCommunication();
				else 
					StartTcpCommunication2();
				return;
			}
		tcpClient.SendMessage(json.GetBytes());
	}

	public void LoginSystem()  //���͵�¼ָ��  //�ͻ���¼SHS��ֻ�е�¼�ɹ����ܷ���SHS [login=1/OK]:1Ҫ�����µ�¼��OK��¼�ɹ���[error=XX] [user=][password=]
	{
		StringJson json=new StringJson(SHProtocol.SHFLAG);
  		json.AddNameVolume("cmd", SHProtocol.LOGIN);
        json.AddNameVolume("user", inputaccount.getText().toString());
        json.AddNameVolume("password",  inputpassword.getText().toString());	
		TcpSend(json);
	}
	
	public void LogoutSystem()  //�����˳�
	{
		StringJson json=new StringJson(SHProtocol.SHFLAG);
  		json.AddNameVolume("cmd", SHProtocol.CLIENTEXIT);
		TcpSend(json);
	}

	private LocalBroadcastManager localBroadcastManager;
	private TCPDataReceiver localReceiver;
	
	class TCPDataReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{		
			byte[] data=intent.getByteArrayExtra("json");
			StringJson json=StringJson.BytesToStringJson(data, SHProtocol.SHFLAG);
			 //�ͻ���¼SHS��ֻ�е�¼�ɹ����ܷ���SHS [login=1/OK]:1Ҫ�����µ�¼��OK��¼�ɹ���[error=XX] [user=][password=]
			String cmd=json.GetStrValueByName("cmd");
            if (cmd.equalsIgnoreCase(SHProtocol.LOGIN) )//  ֻ���ĵ�¼��Ϣ
            {
                String login = json.GetStrValueByName("login");
                if (login != null && login.equals("1"))  //Ҫ���Զ���¼
                	 LoginSystem();		         //����������Ϣ
                else if (login != null && login.equalsIgnoreCase("OK"))             //�ɹ���½��������Ƶ��Ȩ��
                {
                	   chkShowDemo.setChecked(false);  //һ���ɹ���¼���ر�Demo
                    //String lbInfo =getResources().getString(R.string.loginOK);		// + tcpClient.Addresses[0].ToString();
                    //Toast.makeText(LoginActivity.this, lbInfo, Toast.LENGTH_SHORT).show();	
                    //������������
        		    FileManager.WriteStrings(LoginActivity.this,homesfilename,  homes);
                	String right=json.GetStrValueByName("right");
                    Intent newintent=new Intent(LoginActivity.this,MainActivity.class);
                    newintent.putExtra("workdir", currentHomeLocation.HomeName);  //���Ǹ���ص�
                    newintent.putExtra("right", right);  
                    startActivity(newintent);
                }
                else
                {
                    //tcpClient.Close();
        			String error=json.GetStrValueByName("error");
                	if (error==null)
                		error=getResources().getString(R.string.loginerror);	
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();		
                }
            }
           //else  Toast.makeText(LoginActivity.this, this.toString()+"\r\n"+ json.toString(), Toast.LENGTH_SHORT).show();				   
		}
	}
	
	private void  SetbtnDemoClick()  //������ʾϵͳ
	{
		btnDemo.setOnClickListener(new View.OnClickListener()   //
		{			
			@Override
			public void onClick(View v)
			{
                //������������
    		    FileManager.WriteStrings(LoginActivity.this,homesfilename,  homes);
            	String right="1111";
                Intent newintent=new Intent(LoginActivity.this,MainActivity.class);
                currentHomeLocation.HomeName="��ʾ��ͥ";
                newintent.putExtra("workdir", currentHomeLocation.HomeName);  //���Ǹ���ص�
                newintent.putExtra("right", right);  
                newintent.putExtra("demo", true);  
                startActivity(newintent);
			}
		});
	}
	
}
