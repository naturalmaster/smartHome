package com.eagle.smarthome.Device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.eagle.smarthome.util.SmartHomeChannel;
import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class SmartHomeApps  //安装的智能家居设备系统
    {
        public Vector<SmartHomeChannel> SmartHomeChannels;   //所有SH程序的列表        
        public String Filename;
        private String workdir;
        private Context context;
        public SmartHomeApps(Context context,String workdir, String _Filename)
        {
            this.Filename = _Filename;
            this.workdir=workdir;
            this.context=context;
            SmartHomeChannels = new Vector<SmartHomeChannel>();            
            LoadFromFile();
            //taskmanager = new TaskManager();  //任务管理者 
        }

        private void Close()
        {
            SmartHomeChannels.clear();             
        }

        public int smarthomeAppidExists(int appid) //指定编号的SH程序是否存在
        {
            for (int i = 0; i < SmartHomeChannels.size(); i++)
            {
                if (  SmartHomeChannels.get(i).appid == appid) return i;
            }
            return -1;
        }
        public int smarthomePIDExists(int pid) //指定PID的SH程序是否存在
        {
            for (int i = 0; i < SmartHomeChannels.size(); i++)
            {
                if (SmartHomeChannels.get(i).PID == pid) return i;
            }
            return -1;
        }
        public SmartHomeChannel FindSmartHomeChannelByAppid(String appid)   //查找SHM程序
        {
            int id = Integer.getInteger(appid,-1);
            if (id==-1) return null;
            
            int index = smarthomeAppidExists(id);
            if (index < 0) return null;
            return SmartHomeChannels.get(index);
        }
        public SmartHomeChannel FindSmartHomeChannelByPID(String pid)       //查找SHM程序
        {
        	int id = Integer.getInteger(pid,-1);
            if (id==-1) return null;
            int index = smarthomePIDExists(id);
            if (index < 0) return null;
            return SmartHomeChannels.get(index);
        }
        
        public Boolean AddOneSmartHome(int appid)  //增加一个SH程序
        {            
            if (smarthomeAppidExists(appid) >= 0) return false; //已经存在了
            SmartHomeChannel item = new SmartHomeChannel();
            item.appid = appid;
            SmartHomeChannels.add(item);
            return true;
        }
        
        public void SaveToFile()
        {
        	String filename=context.getFilesDir()+File.separator+workdir;
    		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
    		File file2=new File(file.getAbsoluteFile() +File.separator+Filename);
    	  	if (file2.exists()) 
    	  		file2.delete(); 
    	  	FileOutputStream writer=null;
    		try
    		{
    			writer= new FileOutputStream(file2);
    			StreamReadWrite.WriteInt(writer,SmartHomeChannels.size());     //1、先写入登记的监控程序数量
                for (int i = 0; i < SmartHomeChannels.size(); i++)
                    {
                        SmartHomeChannel item = SmartHomeChannels.get(i);
                        StreamReadWrite.WriteInt(writer, item.appid);  					//某品牌智能家居唯一识别号
                        StreamReadWrite.WriteString(writer, item.description);  	//监控设备描述
                        StreamReadWrite.WriteBoolean(writer, item.canused);      	//系统能否被使用
                        StreamReadWrite.WriteBoolean(writer, item.autostart);   	//自动运行  
                        StreamReadWrite.WriteBoolean(writer, item.visible);        	//显示 
                        StreamReadWrite.WriteString(writer,item.name);        		//系统名称：32Unicode字符
                        StreamReadWrite.WriteString(writer,item.assembly);    		//程序集
                        StreamReadWrite.WriteString(writer,item.password);    		//密码      
                        StreamReadWrite.WriteString(writer,item.starttime);   		//系统最近启动的时间
                        StreamReadWrite.WriteString(writer,item.user);
                        StreamReadWrite.WriteInt(writer,item.Port);
                        StreamReadWrite.WriteInt(writer,item.DeviceId);
                        StreamReadWrite.WriteBoolean(writer, item.bServer);
                        StreamReadWrite.WriteString(writer,item.serverIP);
                        StreamReadWrite.WriteInt(writer,item.CommMode);
                    }
                }
    		catch (IOException e)
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

        public void LoadFromFile()
        {   
            Close();
            String filename=context.getFilesDir()+File.separator+workdir;
    		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
    		File file2=new File(file.getAbsoluteFile() +File.separator+Filename);
    	  	if (!file2.exists())  return;
    	  	FileInputStream reader=null;
            //{ "序号", "智能家居系统", "AID", "使用", "自启", "可见", "状态", "PID", 8"窗口", "最近启动时间","驱动程序", "登录密码","用户名","端口", "服务端","IP", "通信","17设备号" };
    	  	try
    	  	{
    	  		reader = new FileInputStream(file2);
                int count =StreamReadWrite.ReadInt(reader); //程序数量
                for (int i = 0; i < count; i++)
                    {
                        int id = StreamReadWrite.ReadInt(reader);  //某品牌智能家居唯一识别号
                        if (!AddOneSmartHome(id)) break;     //增加一个CH程序
                        SmartHomeChannel item = SmartHomeChannels.get(i);
                        item.description = StreamReadWrite.ReadString(reader);  //监控设备描述
                        item.canused = StreamReadWrite.ReadBoolean(reader);   //系统能否被使用
                        item.autostart = StreamReadWrite.ReadBoolean(reader);  //自动运行  
                        item.visible =StreamReadWrite.ReadBoolean(reader);      	//显示 
                        item.name =StreamReadWrite.ReadString(reader);           	//系统名称：32Unicode字符
                        item.assembly = StreamReadWrite.ReadString(reader);      //程序集
                        item.password =  StreamReadWrite.ReadString(reader);  	//密码
                        item.starttime = StreamReadWrite.ReadString(reader);  	//系统最近启动的时间
                        item.user = StreamReadWrite.ReadString(reader);
                        item.Port =  StreamReadWrite.ReadInt(reader);
                        item.DeviceId =StreamReadWrite.ReadInt(reader);
                        item.bServer = StreamReadWrite.ReadBoolean(reader);
                        item.serverIP = StreamReadWrite.ReadString(reader);
                        item.CommMode = StreamReadWrite.ReadInt(reader);
                    }
    	  	}  catch(Exception ex)
    	  	{
    	  		ex.printStackTrace();
    	  	}    
    		finally
    		{
    			if (reader!=null)
    			{
    				try
    				{
    					reader.close();
    				}
    				catch (Exception e)
    				{
    					e.printStackTrace();
    				}
    			}				
    		}   
        }
    }
 