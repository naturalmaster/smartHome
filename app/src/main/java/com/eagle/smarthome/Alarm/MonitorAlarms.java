package com.eagle.smarthome.Alarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class MonitorAlarms 
{
	 private int Flag;                      //Ψһʶ���룺0xE5E5E5E5
     public String FileName;          //�����ر������ݵ��ļ�
     public Vector<MonitorAlarm> MonitorItems;  //�������������¼
     private String workdir;
     private Context context;
     
     public MonitorAlarms(int _Flag,Context context,String workdir,String _FileName)
     {        
    	 this.context=context;
        this.workdir=workdir;
         Flag = _Flag;
         FileName = _FileName;
         MonitorItems = new Vector<MonitorAlarm>();
         LoadFromFile();
     }

     public Boolean SaveToFile()    //���浽�ļ�
     {
     	String filename=context.getFilesDir()+File.separator+workdir;
  		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
  		File file2=new File(file.getAbsoluteFile() +File.separator+FileName);
  		FileOutputStream bw=null;
 		try
 		{
 			bw= new FileOutputStream(file2);
             StreamReadWrite.WriteInt(bw, Flag); // bw.Write(Flag);
             StreamReadWrite.WriteInt(bw, MonitorItems.size()); ///��¼����
             for (int i = 0; i < MonitorItems.size(); i++)
             {
            	 MonitorItems.get(i).WriteToStream(bw);
             }
             bw.close();
             return true;
         }
 		catch (Exception ex)
         {
             return false;
         } 		
     }
     
     public void LoadFromFile()  //���ļ�װ��
     {
        MonitorItems.clear();
        String filename=context.getFilesDir()+File.separator+workdir;
  		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
  		File file2=new File(file.getAbsoluteFile() +File.separator+FileName);
  	  	if (!file2.exists())  return;
  	  	FileInputStream br =null;
  	  	try
  	  	{
  	  		br = new FileInputStream(file2);
             int flag = StreamReadWrite.ReadInt(br); // br.ReadInt32();int flag = br.ReadInt32();
             if (flag != Flag) //���Ǳ����ܼҾ�ϵͳ���ļ�
             {
                 br.close();
                 return;
             }
             int count = StreamReadWrite.ReadInt(br); //br.ReadInt32();       //�豸����
             for (int i = 0; i < count; i++)
             {
             	 MonitorAlarm item = new MonitorAlarm();
                  item.ReadFromStream(br);
                  MonitorItems.add(item);
             }
             br.close();
         }
  	  	catch (Exception ex)
         {
         }
     }
}
