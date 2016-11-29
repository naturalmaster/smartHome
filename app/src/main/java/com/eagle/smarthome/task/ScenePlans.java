package com.eagle.smarthome.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class ScenePlans   //场景列表管理对象
{
    public int Flag;                   //唯一识别码
    public String FileName;   //保存所有场景的文件，四级优先规则。知道它可以了解所有场景的名字
    private String workdir;
    private Context context;
    public Vector<ScenePlansItem> Items;     //保存所有场景列表
    public ScenePlans(int _Flag,Context context,String workdir,String _FileName)
    {
        Flag = _Flag;
        this.workdir=workdir;
        this.context=context;
        FileName = _FileName;
        Items = new Vector<ScenePlansItem>();
        LoadFromFile();
    }
   public void  Clear()
    {
        Items.clear();
    }
    public Boolean SaveToFile()    //保存到文件
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
            {
                StreamReadWrite.WriteBoolean(bw, Items.get(i).Used);// bw.Write(Items.get(i).Used);
                StreamReadWrite.WriteString(bw, Items.get(i).PlanFileName); //bw.Write(Items.get(i).PlanFileName);
                StreamReadWrite.WriteString(bw, Items.get(i).BlackList); //bw.Write(Items.get(i).PlanFileName);
                StreamReadWrite.WriteString(bw,  Items.get(i).WhiteList); //2016-2-25
                StreamReadWrite.WriteString(bw,  Items.get(i).reserve1); 
                StreamReadWrite.WriteString(bw,  Items.get(i).reserve2); 
                StreamReadWrite.WriteString(bw,  Items.get(i).reserve3); 
            }
            bw.close();
            return true;
        }
		catch (Exception ex)
        {
            return false;
        }
    }
    public void LoadFromFile()  //从文件装入
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
            int flag = StreamReadWrite.ReadInt(br); // br.ReadInt32();int flag = br.ReadInt32();
            if (flag != Flag) //不是本智能家居系统的文件
            {
                br.close();
                return;
            }
            int count = StreamReadWrite.ReadInt(br); //br.ReadInt32();       //设备数量
            for (int i = 0; i < count; i++)
            {
                ScenePlansItem item = new ScenePlansItem();
                item.Used = StreamReadWrite.ReadBoolean(br);
                item.PlanFileName = StreamReadWrite.ReadString(br);
                item.BlackList = StreamReadWrite.ReadString(br);
                item.WhiteList = StreamReadWrite.ReadString(br);
                item.reserve1 = StreamReadWrite.ReadString(br);
                item.reserve2 = StreamReadWrite.ReadString(br);
                item.reserve3 = StreamReadWrite.ReadString(br);
                Items.add(item);
            }
            br.close();
        }
 	  	catch (Exception ex)
        {
        }
    }
    public int ScenePlansItemExists(String ItemFileName)
    {
    	for (int i=0; i<Items.size(); i++)
    	{
    		if (ItemFileName.equals(Items.get(i).PlanFileName))
    			return i;
    	}
    	return -1;
    }
}
