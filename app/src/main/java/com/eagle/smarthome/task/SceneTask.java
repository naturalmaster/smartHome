package com.eagle.smarthome.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class SceneTask //һ���������ݣ��ɶ���������¼���
{
    private int Flag;                      //Ψһʶ���룺0xE5E5E5E5
    public String FileName;  //���泡�����ݵ��ļ�
    private String workdir;
    private Context context;
    public Boolean Timed;                   //�Ƿ�ʱ����
    public int StartTime;                 //��ʱ����ʼִ�е�ʱ��
    public Vector<TaskItem> Items;
    public SceneTask(int _Flag, Context context,String workdir,String _FileName, Boolean Timed, int StartTime)
    {
        Flag = _Flag;
        this.workdir=workdir;
        this.context=context;
        FileName = _FileName;
        this.StartTime = StartTime;
        this.Timed = Timed;
        Items = new Vector<TaskItem>();
        LoadFromFile();
    }
    public void Clear()
    {
        Items.clear();
    }
 
    public Boolean SaveToFile()    	//���浽�ļ�
    {
    	 String filename=context.getFilesDir()+File.separator+workdir;
 		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
 		File file2=new File(file.getAbsoluteFile() +File.separator+FileName);
 	  	//if (!file2.exists())  return false;
 		FileOutputStream bw=null;
		try
		{
			bw= new FileOutputStream(file2);
            StreamReadWrite.WriteInt(bw, Flag); // bw.Write(Flag);
            StreamReadWrite.WriteInt(bw, Items.size()); //bw.Write(Items.Count);  //��¼����
            for (int i = 0; i < Items.size(); i++)
                Items.get(i).WriteToStream(bw);
            bw.close();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
    public void LoadFromFile()  		//���ļ�װ��
    {
        Items.clear();
        String filename=context.getFilesDir()+File.separator+workdir;
 		File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
 		File file2=new File(file.getAbsoluteFile() +File.separator+FileName);
 	  	if (!file2.exists())  return;
 	  	FileInputStream br =null;
 	  	try
 	  	{
 	  		br = new FileInputStream(file2);
 	  	}
 	   catch(Exception ex) { return;}
        int flag = StreamReadWrite.ReadInt(br); // br.ReadInt32();int flag = br.ReadInt32();
        if (flag != Flag) //���Ǳ����ܼҾ�ϵͳ���ļ�
        {
    	    try { br.close();}
    	    catch(Exception ex) {}
            return;
        }
        Flag = flag;
        int count = StreamReadWrite.ReadInt(br); //br.ReadInt32();       //�豸����
        try
        {
            for (int i = 0; i < count; i++)
            {
                TaskItem item = new TaskItem();
                item.ReadFromStream(br);
                Items.add(item);
            }
            br.close();
        }
        catch (Exception ex)
        {
        }        
    }

    public void SwapTwoTaslItem(int source, int dest) //������������������
    {
        TaskItem tmp = new TaskItem();
        TaskItem si = Items.get(source);
        TaskItem di = Items.get(dest);
        TaskItem.Copy(si, tmp);
        TaskItem.Copy(di, si);
        TaskItem.Copy(tmp, di); 
    }

}
