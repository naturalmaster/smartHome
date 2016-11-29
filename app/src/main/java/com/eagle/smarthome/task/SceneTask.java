package com.eagle.smarthome.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class SceneTask //一个场景内容：由多条场景记录组成
{
    private int Flag;                      //唯一识别码：0xE5E5E5E5
    public String FileName;  //保存场景内容的文件
    private String workdir;
    private Context context;
    public Boolean Timed;                   //是否定时任务
    public int StartTime;                 //定时任务开始执行的时间
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
 
    public Boolean SaveToFile()    	//保存到文件
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
            StreamReadWrite.WriteInt(bw, Items.size()); //bw.Write(Items.Count);  //记录数量
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
    public void LoadFromFile()  		//从文件装入
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
        if (flag != Flag) //不是本智能家居系统的文件
        {
    	    try { br.close();}
    	    catch(Exception ex) {}
            return;
        }
        Flag = flag;
        int count = StreamReadWrite.ReadInt(br); //br.ReadInt32();       //设备数量
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

    public void SwapTwoTaslItem(int source, int dest) //交换两个动作的内容
    {
        TaskItem tmp = new TaskItem();
        TaskItem si = Items.get(source);
        TaskItem di = Items.get(dest);
        TaskItem.Copy(si, tmp);
        TaskItem.Copy(di, si);
        TaskItem.Copy(tmp, di); 
    }

}
