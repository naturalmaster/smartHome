package com.eagle.smarthome.Device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import com.eagle.smarthome.StringJson;
import com.eagle.smarthome.util.SmartHomeChannel;
import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;
public class SmartHome   //��ӥ���ܼҾӶ��壺��������ͳһ���Ϊ��SmartHome��
{
    //�������ͨ�ţ�����socket
    public SmartHomeChannel smarthomechannel;  //ӳ�䵽�����ڴ�Ķ���ָ��  
    public Vector<HomeDevice> homedevices; 
    public String homename;  
    public String password;   
    public String filename;   //�������ܼҾӵ������ļ������������ݿ�
    public String workdir;      //�������ܼҾӵ������ļ�����Ŀ¼
    private Context context;
    public SmartHome(Context context,String workdir, String _filename)  //�������Ĺ��캯��
    {
    	this.context=context;
    	this.filename = _filename;
        this.workdir=workdir;
        homedevices = new Vector<HomeDevice>();
        ReadFromFile();            
    }

    public void InitComm(Object[] commObjects)       //Ӧ�ó�����Ҫ���ø÷���֮ǰ����Ҫ��ȷ����commObject
    {
        
    }
   void Close()
    {
        homedevices.clear();
    }

    public void ReadFromFile()  //���ļ�����SH���������
    {    	
        homedevices.clear();   //�����ԭ���豸����         
        String filedir=context.getFilesDir()+File.separator+workdir;
		File file = new File(filedir); 
		File file2=new File(file.getAbsoluteFile() +File.separator+filename);
	  	if (!file2.exists())  return;
	  	FileInputStream br=null;
	  	try
	  	{
	  		br = new FileInputStream(file2);
	        ReadFromStream(br);
	        br.close();
        }
        catch (Exception ex) 
        { }
    }
    public void SaveToFile()    //SmartHome����д���ļ�
    {
    	String filedir= context.getFilesDir()+File.separator+workdir;
		File file = new File(filedir); //String path = getFilesDir().getAbsolutePath() ;
		File file2=new File(file.getAbsoluteFile() +File.separator+filename);
	  	if (file2.exists()) 
	  		file2.delete(); 
	  	FileOutputStream bw=null;
		try
		{
			bw= new FileOutputStream(file2);
            WriteToStream(bw);
            bw.close();
        }
        catch (Exception ex) 
        { }
    }
    public void ReadFromStream(InputStream br) // �����ж���SH���������
    {
        homename = StreamReadWrite.ReadString(br);  //homename = br.ReadString();       //1����ȡ����
        password = StreamReadWrite.ReadString(br);  //password = br.ReadString();       //2����ȡ����
        int count = StreamReadWrite.ReadInt(br);    //br.ReadInt32();                   //3����ȡ�豸����
        for (int i = 0; i < count; i++)
        {
            HomeDevice hd = new HomeDevice();
            hd.ReadFromStream(br);
            homedevices.add(hd);
        }
    }

    public void WriteToStream(OutputStream bw)   //SmartHome����д����
    {
        StreamReadWrite.WriteString(bw, homename); //bw.Write(homename);     //1��д������
        StreamReadWrite.WriteString(bw, password); //bw.Write(password);     //2��д�����
        StreamReadWrite.WriteInt(bw, homedevices.size()); //bw.Write(homedevices.Count);         //3��д���豸����
        for (int i = 0; i < homedevices.size(); i++)
        {
            HomeDevice hd = homedevices.get(i);
            hd.WriteToStream(bw);
        }
    }
    public void ProcessDeviceStateData(byte[] states)   //��ﴦ�������(�豸ϵͳ)ͨ�ŷ���״̬���ݵķ����������ṩ��������       
    {
       
    }
    public HomeDevice FindHomeDevice(int deviceid)    //�����豸��ƥ����豸
    {
        for (int i = 0; i < homedevices.size(); i++)
        {
            if (homedevices.get(i).deviceid == deviceid)
                return homedevices.get(i);
        }
        return null;
    }

    public Boolean GetAIState(int deviceID)
    {
        return false;            
    }
    public Boolean GetAOState(int deviceID)
    {
        return false;
    }
    public Boolean GetSIState(int deviceID)
    {
        return false; //throw new NotImplementedException();//�޴��豸����ʵ��
    }
    public Boolean GetSOState(int deviceID)
    {
        return false; //throw new NotImplementedException();//�޴��豸����ʵ��
    }
    public Boolean GetDIState(int deviceID)  //��ȡDI״̬�ķ���
    {
        return false;
    }

    public Boolean GetOneDOState(int deviceID, short subID)
    {
        return false;
    }

    public Boolean GetOneDIState(int deviceID, short subID)
    {
        return false;
    }

    public Boolean GetOneAOState(int deviceID, short subID)
    {
        return false;
    }

    public Boolean GetOneAIState(int deviceID, short subID)
    {
        return false;
    }

    public Boolean GetOneSOState(int deviceID, short subID)
    {
        return false;
    }

    public Boolean GetOneSIState(int deviceID, short subID)
    {
        return false;
    }
    void TCPNotifyDevice(StringJson json)  //��TCPͨ�ţ�֪ͨ�豸ϵͳ
    {
        //byte[] msg = json.GetBytes();
        //for (int i = 0; i < Clients.Count; i++)
        //    Clients[i].Send(msg);
    }
    public Boolean GetDOState(int deviceID)  //��ȡDO״̬�ķ���
    {            
        return false;            
    }

    //SHACTRL = "505";    //��ĳ��SHAϵͳ���豸��ָ��[appid=N][devid=M][subid=X][type=Y][act=K]
    public void SendDO(int deviceID, short subID, Boolean On)  //ͨ��DO�豸������ʵ�֣�
    {
        return;
    }
    public void SendAO(int deviceID, short subID, float[] value)
    {
        return;
    }
    public void SendSO(int deviceID, short subID, byte[] value)
    {
        return;
    }
    public int GetDeviceCount()  //ֻ��һ���豸
    {
        return 1;
    }
    public HomeDevice MakeNewHomeDevice()  //�������豸
    {
        return new HomeDevice();
    }
    public CommMode commmode;

    public Boolean bserver;
  
    public void Login(Object[] loginParas)
    {
        return;
    }
}
