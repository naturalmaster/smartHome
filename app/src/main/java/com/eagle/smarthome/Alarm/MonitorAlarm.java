package com.eagle.smarthome.Alarm;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;
import com.eagle.smarthome.util.StreamReadWrite;


public class MonitorAlarm //监控报警文件，包含多个报警记录
{
    public String TaskName;         //报警时执行的任务
    public Boolean CanDisarm;     //是否可以撤防    
    public Boolean Used;               //是否可用/禁用
    public Vector<AlarmItem> Items;  //包含多个报警记录

    public MonitorAlarm()
    {
        TaskName = "";
        CanDisarm = false;
        Used=true;
        Items = new Vector<AlarmItem>();
    }

    public int CheckBracketsLegal()  //检查条件表达式中的括号是否匹配：要配对
    {
        int brackets = 0;// 括号匹配数

        for (int i = 0; i < Items.size(); i++)   //910.0.SI1>10:00:00
        {
        	AlarmItem item=Items.get(i);
            if (item.prefix.equals("(") && item.suffix.equals(")"))  //多余的括号
            {
            	item.prefix = " ";
            	item.suffix = " ";
            }
            if (item.prefix.equals("(")) brackets++;
            if (item.suffix.equals(")")) brackets--;
            if (brackets<0) break; //不匹配。立即退出
        }
        return brackets;
    }

    
    public Boolean WriteToStream(FileOutputStream bw )    //保存到文件
    {	
		try
		{
            StreamReadWrite.WriteString(bw, TaskName); 
            StreamReadWrite.WriteBoolean(bw, CanDisarm);
            StreamReadWrite.WriteBoolean(bw, Used);
            StreamReadWrite.WriteInt(bw, Items.size()); //bw.Write(Items.Count);  //记录数量
            for (int i = 0; i < Items.size(); i++)
            {
            	Items.get(i).WriteToStream(bw);
            }
            return true;
        }
		catch (Exception ex)
        {
            return false;
        }
    }
    public void ReadFromStream(FileInputStream br)  //从文件装入
    {
        Items.clear();        
 	  	try
 	  	{
            TaskName = StreamReadWrite.ReadString(br);
            CanDisarm = StreamReadWrite.ReadBoolean(br);
            Used= StreamReadWrite.ReadBoolean(br);
            int count = StreamReadWrite.ReadInt(br); //设备数量
            for (int i = 0; i < count; i++)
            {
            	 AlarmItem item = new AlarmItem();
                 item.ReadFromStream(br);
                 Items.add(item);
            }
        }
 	  	catch (Exception ex)
        {
        }
    }
    public String GetMonitorExpression() //监控条件的友好字符串表达式
    {
        String s = "";
        int size= Items.size();
        for (int i = 0; i <size;  i++)   //910.0.SI1>10:00:00
        {
            AlarmItem it = Items.get(i);
            s += it.prefix + it.appId+"."+it.devieID+"."+it.deviceType+it.subDevieID+it.operate +it.value + it.suffix;
            if (i < size - 1)
                s += " "+it.combine+" ";
        }
        return s;
    }
}