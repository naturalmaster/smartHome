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
public class SmartHome   //金鹰智能家居定义：类名必须统一设计为“SmartHome”
{
    //如果网络通信，就用socket
    public SmartHomeChannel smarthomechannel;  //映射到共享内存的对象指针  
    public Vector<HomeDevice> homedevices; 
    public String homename;  
    public String password;   
    public String filename;   //保存智能家居的数据文件，不采用数据库
    public String workdir;      //保存智能家居的数据文件的子目录
    private Context context;
    public SmartHome(Context context,String workdir, String _filename)  //带参数的构造函数
    {
    	this.context=context;
    	this.filename = _filename;
        this.workdir=workdir;
        homedevices = new Vector<HomeDevice>();
        ReadFromFile();            
    }

    public void InitComm(Object[] commObjects)       //应用程序需要调用该方法之前，需要正确设置commObject
    {
        
    }
   void Close()
    {
        homedevices.clear();
    }

    public void ReadFromFile()  //从文件读入SH对象的数据
    {    	
        homedevices.clear();   //先清除原来设备数据         
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
    public void SaveToFile()    //SmartHome对象写入文件
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
    public void ReadFromStream(InputStream br) // 从流中读入SH对象的数据
    {
        homename = StreamReadWrite.ReadString(br);  //homename = br.ReadString();       //1、读取名字
        password = StreamReadWrite.ReadString(br);  //password = br.ReadString();       //2、读取口令
        int count = StreamReadWrite.ReadInt(br);    //br.ReadInt32();                   //3、读取设备数量
        for (int i = 0; i < count; i++)
        {
            HomeDevice hd = new HomeDevice();
            hd.ReadFromStream(br);
            homedevices.add(hd);
        }
    }

    public void WriteToStream(OutputStream bw)   //SmartHome对象写入流
    {
        StreamReadWrite.WriteString(bw, homename); //bw.Write(homename);     //1、写入名字
        StreamReadWrite.WriteString(bw, password); //bw.Write(password);     //2、写入口令
        StreamReadWrite.WriteInt(bw, homedevices.size()); //bw.Write(homedevices.Count);         //3、写入设备数量
        for (int i = 0; i < homedevices.size(); i++)
        {
            HomeDevice hd = homedevices.get(i);
            hd.WriteToStream(bw);
        }
    }
    public void ProcessDeviceStateData(byte[] states)   //★★处理服务器(设备系统)通信返回状态数据的方法：厂家提供解析方法       
    {
       
    }
    public HomeDevice FindHomeDevice(int deviceid)    //搜索设备号匹配的设备
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
        return false; //throw new NotImplementedException();//无此设备，空实现
    }
    public Boolean GetSOState(int deviceID)
    {
        return false; //throw new NotImplementedException();//无此设备，空实现
    }
    public Boolean GetDIState(int deviceID)  //获取DI状态的方法
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
    void TCPNotifyDevice(StringJson json)  //用TCP通信，通知设备系统
    {
        //byte[] msg = json.GetBytes();
        //for (int i = 0; i < Clients.Count; i++)
        //    Clients[i].Send(msg);
    }
    public Boolean GetDOState(int deviceID)  //获取DO状态的方法
    {            
        return false;            
    }

    //SHACTRL = "505";    //给某个SHA系统的设备发指令[appid=N][devid=M][subid=X][type=Y][act=K]
    public void SendDO(int deviceID, short subID, Boolean On)  //通断DO设备：厂家实现！
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
    public int GetDeviceCount()  //只有一个设备
    {
        return 1;
    }
    public HomeDevice MakeNewHomeDevice()  //建立新设备
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
