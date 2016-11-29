package com.eagle.smarthome.Alarm;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;
import com.eagle.smarthome.util.StreamReadWrite;


public class MonitorAlarm //��ر����ļ����������������¼
{
    public String TaskName;         //����ʱִ�е�����
    public Boolean CanDisarm;     //�Ƿ���Գ���    
    public Boolean Used;               //�Ƿ����/����
    public Vector<AlarmItem> Items;  //�������������¼

    public MonitorAlarm()
    {
        TaskName = "";
        CanDisarm = false;
        Used=true;
        Items = new Vector<AlarmItem>();
    }

    public int CheckBracketsLegal()  //����������ʽ�е������Ƿ�ƥ�䣺Ҫ���
    {
        int brackets = 0;// ����ƥ����

        for (int i = 0; i < Items.size(); i++)   //910.0.SI1>10:00:00
        {
        	AlarmItem item=Items.get(i);
            if (item.prefix.equals("(") && item.suffix.equals(")"))  //���������
            {
            	item.prefix = " ";
            	item.suffix = " ";
            }
            if (item.prefix.equals("(")) brackets++;
            if (item.suffix.equals(")")) brackets--;
            if (brackets<0) break; //��ƥ�䡣�����˳�
        }
        return brackets;
    }

    
    public Boolean WriteToStream(FileOutputStream bw )    //���浽�ļ�
    {	
		try
		{
            StreamReadWrite.WriteString(bw, TaskName); 
            StreamReadWrite.WriteBoolean(bw, CanDisarm);
            StreamReadWrite.WriteBoolean(bw, Used);
            StreamReadWrite.WriteInt(bw, Items.size()); //bw.Write(Items.Count);  //��¼����
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
    public void ReadFromStream(FileInputStream br)  //���ļ�װ��
    {
        Items.clear();        
 	  	try
 	  	{
            TaskName = StreamReadWrite.ReadString(br);
            CanDisarm = StreamReadWrite.ReadBoolean(br);
            Used= StreamReadWrite.ReadBoolean(br);
            int count = StreamReadWrite.ReadInt(br); //�豸����
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
    public String GetMonitorExpression() //����������Ѻ��ַ������ʽ
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