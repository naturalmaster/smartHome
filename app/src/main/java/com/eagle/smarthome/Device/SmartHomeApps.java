package com.eagle.smarthome.Device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.eagle.smarthome.util.SmartHomeChannel;
import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class SmartHomeApps  //��װ�����ܼҾ��豸ϵͳ
    {
        public Vector<SmartHomeChannel> SmartHomeChannels;   //����SH������б�        
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
            //taskmanager = new TaskManager();  //��������� 
        }

        private void Close()
        {
            SmartHomeChannels.clear();             
        }

        public int smarthomeAppidExists(int appid) //ָ����ŵ�SH�����Ƿ����
        {
            for (int i = 0; i < SmartHomeChannels.size(); i++)
            {
                if (  SmartHomeChannels.get(i).appid == appid) return i;
            }
            return -1;
        }
        public int smarthomePIDExists(int pid) //ָ��PID��SH�����Ƿ����
        {
            for (int i = 0; i < SmartHomeChannels.size(); i++)
            {
                if (SmartHomeChannels.get(i).PID == pid) return i;
            }
            return -1;
        }
        public SmartHomeChannel FindSmartHomeChannelByAppid(String appid)   //����SHM����
        {
            int id = Integer.getInteger(appid,-1);
            if (id==-1) return null;
            
            int index = smarthomeAppidExists(id);
            if (index < 0) return null;
            return SmartHomeChannels.get(index);
        }
        public SmartHomeChannel FindSmartHomeChannelByPID(String pid)       //����SHM����
        {
        	int id = Integer.getInteger(pid,-1);
            if (id==-1) return null;
            int index = smarthomePIDExists(id);
            if (index < 0) return null;
            return SmartHomeChannels.get(index);
        }
        
        public Boolean AddOneSmartHome(int appid)  //����һ��SH����
        {            
            if (smarthomeAppidExists(appid) >= 0) return false; //�Ѿ�������
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
    			StreamReadWrite.WriteInt(writer,SmartHomeChannels.size());     //1����д��Ǽǵļ�س�������
                for (int i = 0; i < SmartHomeChannels.size(); i++)
                    {
                        SmartHomeChannel item = SmartHomeChannels.get(i);
                        StreamReadWrite.WriteInt(writer, item.appid);  					//ĳƷ�����ܼҾ�Ψһʶ���
                        StreamReadWrite.WriteString(writer, item.description);  	//����豸����
                        StreamReadWrite.WriteBoolean(writer, item.canused);      	//ϵͳ�ܷ�ʹ��
                        StreamReadWrite.WriteBoolean(writer, item.autostart);   	//�Զ�����  
                        StreamReadWrite.WriteBoolean(writer, item.visible);        	//��ʾ 
                        StreamReadWrite.WriteString(writer,item.name);        		//ϵͳ���ƣ�32Unicode�ַ�
                        StreamReadWrite.WriteString(writer,item.assembly);    		//����
                        StreamReadWrite.WriteString(writer,item.password);    		//����      
                        StreamReadWrite.WriteString(writer,item.starttime);   		//ϵͳ���������ʱ��
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
            //{ "���", "���ܼҾ�ϵͳ", "AID", "ʹ��", "����", "�ɼ�", "״̬", "PID", 8"����", "�������ʱ��","��������", "��¼����","�û���","�˿�", "�����","IP", "ͨ��","17�豸��" };
    	  	try
    	  	{
    	  		reader = new FileInputStream(file2);
                int count =StreamReadWrite.ReadInt(reader); //��������
                for (int i = 0; i < count; i++)
                    {
                        int id = StreamReadWrite.ReadInt(reader);  //ĳƷ�����ܼҾ�Ψһʶ���
                        if (!AddOneSmartHome(id)) break;     //����һ��CH����
                        SmartHomeChannel item = SmartHomeChannels.get(i);
                        item.description = StreamReadWrite.ReadString(reader);  //����豸����
                        item.canused = StreamReadWrite.ReadBoolean(reader);   //ϵͳ�ܷ�ʹ��
                        item.autostart = StreamReadWrite.ReadBoolean(reader);  //�Զ�����  
                        item.visible =StreamReadWrite.ReadBoolean(reader);      	//��ʾ 
                        item.name =StreamReadWrite.ReadString(reader);           	//ϵͳ���ƣ�32Unicode�ַ�
                        item.assembly = StreamReadWrite.ReadString(reader);      //����
                        item.password =  StreamReadWrite.ReadString(reader);  	//����
                        item.starttime = StreamReadWrite.ReadString(reader);  	//ϵͳ���������ʱ��
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
 