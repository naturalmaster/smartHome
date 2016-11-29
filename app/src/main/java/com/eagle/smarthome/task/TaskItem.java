package com.eagle.smarthome.task;

import java.io.InputStream;
import java.io.OutputStream;

import com.eagle.smarthome.Device.*;
import com.eagle.smarthome.util.SmartHomes;
import com.eagle.smarthome.util.StreamReadWrite;

public class TaskItem  //һ�����峡����¼
{
    public int appId ;        //ĳ���ܼҾ�ϵͳ��Ψһʶ���
    public int devieID;      //�豸��
    public DeviceType deviceType;   //�豸���ͣ�ֻ����DO,AO,SO
    public short subDevieID;  //���豸���
    public int delayTime;        //������ʱ
    public String action;   		//��������
    //public String info ;
    public String GetInfo()
    {
        return String.format("(%d-%d-%d)", appId, devieID, subDevieID);
    }

    public String sDelayTime;
    public String GetTimestr()
    {
        return String.format("%02d:%02d:%02d", delayTime / 3600, (delayTime / 60) % 60, delayTime % 60);
    }
   public String homename; 
   public  String FindHomeName(SmartHomeApps InstlledDeviceSystem)
    {
    	for (int hm=0;hm<InstlledDeviceSystem.SmartHomeChannels.size();hm++)  //���ÿ���豸ϵͳ
    	{
    		int aid=InstlledDeviceSystem.SmartHomeChannels.get(hm).appid;  //�豸ϵͳ���
    		if (aid!=appId) continue;
    		String s=InstlledDeviceSystem.SmartHomeChannels.get(hm).description;
    		return s;
    	}
    	return "";
    }
    
   public  int FindHome(SmartHomeApps InstlledDeviceSystem)
   {
   	for (int hm=0;hm<InstlledDeviceSystem.SmartHomeChannels.size();hm++)  //���ÿ���豸ϵͳ
   	{
   		int aid=InstlledDeviceSystem.SmartHomeChannels.get(hm).appid;  //�豸ϵͳ���
   		if (aid!=appId) continue;
   		return hm;
   	}
   	return -1;
   }
   
   public String positiondescription="";
   public Object FindDevice(SmartHomes homes)  //�ҵ������ĸ��������豸
    {
    	for (int hm=0;hm<homes.smarthomes.size();hm++)  //���ÿ���豸ϵͳ
    	{
    		int aid=homes.appids.get(hm);  //�豸ϵͳ���
    		if (aid!=appId) continue;
    		SmartHome sh=homes.smarthomes.get(hm);
    	
	        for (int i = 0; i < sh.homedevices.size(); i++)  //���ÿ���豸ϵͳ�е�ÿ���豸
	        {
	        	HomeDevice homedevice=sh.homedevices.get(i);  //һ���豸ϵͳ	        
	        	if (homedevice.deviceid!=devieID) continue;
	        	positiondescription=homedevice.positiondescription;
	        	if(deviceType==DeviceType.DO)
	        	{
	        		   for (int j = 0; j < homedevice.DODevices.size(); j++)  //���ÿ���豸ϵͳ�е�ÿ���豸
	        		   {
	        			    if (homedevice.DODevices.get(j).id==subDevieID) return homedevice.DODevices.get(j);
	        		   }
	        		   return null;
	        	}
	        	if(deviceType==DeviceType.AO)
	        	{
	        		   for (int j = 0; j < homedevice.AODevices.size(); j++)  //���ÿ���豸ϵͳ�е�ÿ���豸
	        		   {
	        			    if (homedevice.AODevices.get(j).id==subDevieID) return homedevice.AODevices.get(j);
	        		   }
	        		   return null;
	        	}	        	
	        	if(deviceType==DeviceType.SO)
	        	{
	        		   for (int j = 0; j < homedevice.SODevices.size(); j++)  //���ÿ���豸ϵͳ�е�ÿ���豸
	        		   {
	        			    if (homedevice.SODevices.get(j).id==subDevieID) return homedevice.SODevices.get(j);
	        		   }
	        		   return null;
	        	}	   
	        	if(deviceType==DeviceType.DI)
	        	{
	        		   for (int j = 0; j < homedevice.DIDevices.size(); j++)  //���ÿ���豸ϵͳ�е�ÿ���豸
	        		   {
	        			    if (homedevice.DIDevices.get(j).id==subDevieID) return homedevice.DIDevices.get(j);
	        		   }
	        		   return null;
	        	}
	        	if(deviceType==DeviceType.AI)
	        	{
	        		   for (int j = 0; j < homedevice.AIDevices.size(); j++)  //���ÿ���豸ϵͳ�е�ÿ���豸
	        		   {
	        			    if (homedevice.AIDevices.get(j).id==subDevieID) return homedevice.AIDevices.get(j);
	        		   }
	        		   return null;
	        	}
	        	if(deviceType==DeviceType.SI)
	        	{
	        		   for (int j = 0; j < homedevice.SIDevices.size(); j++)  //���ÿ���豸ϵͳ�е�ÿ���豸
	        		   {
	        			    if (homedevice.SIDevices.get(j).id==subDevieID) return homedevice.SIDevices.get(j);
	        		   }
	        		   return null;
	        	}
	        }
    	}
        return null;
    }
 
   public HomeDevice FindHomeDevice(SmartHomes homes)  //�ҵ������ĸ��߼Ҿ��豸
   {
   	for (int hm=0;hm<homes.smarthomes.size();hm++)  //���ÿ���豸ϵͳ
   	{
   		int aid=homes.appids.get(hm);  //�豸ϵͳ���
   		if (aid!=appId) continue;
   		SmartHome sh=homes.smarthomes.get(hm);   	
        for (int i = 0; i < sh.homedevices.size(); i++)  //���ÿ���豸ϵͳ�е�ÿ���豸
        {
        	HomeDevice homedevice=sh.homedevices.get(i);  //һ���豸ϵͳ	        
        	if (homedevice.deviceid!=devieID) continue;
        	return homedevice;
        }
   	}
       return null;
   }

   public TaskItem()
    {
        delayTime = 1;
        deviceType = DeviceType.DO;  //ȱʡDO���� 
        action = "";  //��ֹΪnull
    }
    public static void Copy(TaskItem sitem, TaskItem ditem)
    {
        //ditem.No = sitem.No;
        ditem.appId = sitem.appId;
        ditem.devieID = sitem.devieID;
        ditem.deviceType = sitem.deviceType;
        ditem.subDevieID = sitem.subDevieID;
        ditem.delayTime = sitem.delayTime;
        ditem.action = sitem.action;
        //ditem.FunctionDescription = sitem.FunctionDescription;
    }
    public static int FindDeviceIndex(SmartHome sh, int DevieID)
    {
        for (int i = 0; i < sh.homedevices.size(); i++)
        {
            if (sh.homedevices.get(i).deviceid == DevieID) return i;
        }
        return -1;
    }
    public static String TaskItemToString(TaskItem item, SmartHome sh) //���豸����������
    {
        if (sh == null) return "";
        int index = FindDeviceIndex(sh, item.devieID);
        if (index == -1) return "";
        HomeDevice hd = sh.homedevices.get(index);
        if (item.deviceType == DeviceType.DO)
        {
            DeviceDO dv = hd.DODevices.get(item.subDevieID);
            return dv.id + "," + dv.devicetype.toString() + "," +
                   dv.dotype.toString() + "," + dv.functiondescription;
        }
        else if (item.deviceType == DeviceType.AO)
        {
            DeviceAO dv = hd.AODevices.get(item.subDevieID);
            //return string.Format("{0},{1},{2}", item.DeviceType, dv.Id, dv.FunctionDescription);
            return dv.id+ "," + dv.devicetype.toString() + "," +
                   dv.unitname + "," + dv.functiondescription;
        }
        else if (item.deviceType == DeviceType.SO)
        {
            DeviceSO dv = hd.SODevices.get(item.subDevieID);
            //return string.Format("{0},{1},{2}", item.DeviceType, dv.Id, dv.FunctionDescription);
            return dv.id+ "," + dv.devicetype.toString() + "," +
                   dv.streamtype.toString() + "," + dv.functiondescription;
        }
        return "";
    }
    public static void StringToTaskItem( TaskItem item, String sub)  //���豸����������ת��Ϊ���豸������
    {
        String[] ss = sub.split(",");
        try
        {
            DeviceType dt = DeviceType.valueOf(ss[1]);
            item.deviceType = dt;
            item.subDevieID = (short)Integer.parseInt(ss[0]);
        }
        catch (Exception ex)
        { }
    }
    public void WriteToStream(OutputStream bw)     //�Ѷ���д������
    {
        StreamReadWrite.WriteInt(bw, appId);
        StreamReadWrite.WriteInt(bw, devieID);
        StreamReadWrite.WriteInt(bw, deviceType.ordinal());
        StreamReadWrite.WriteInt(bw, (int)subDevieID);
        StreamReadWrite.WriteInt(bw, delayTime);
        StreamReadWrite.WriteString(bw, action);        
    }
    public void ReadFromStream(InputStream br)   //�����ж�ȡ����
    {
        appId = StreamReadWrite.ReadInt(br);
        devieID = StreamReadWrite.ReadInt(br);
        deviceType =DeviceType.values()[StreamReadWrite.ReadInt(br)];
        subDevieID = (short)StreamReadWrite.ReadInt(br);
        delayTime = StreamReadWrite.ReadInt(br);
        action = StreamReadWrite.ReadString(br);
    }
    @Override
    public  String toString()
    {
        return String.format("(%d,%d,%d) %s,%s}", appId, devieID, subDevieID, deviceType.toString(), action); 
    }
    public Object dv=null;
    public void GetDevice(SmartHomes homes)
    {
    	dv=FindDevice(homes);
    }
    public String GetDetailInfo()
    {
    		return String.format("%d-%d-%d,%s(%s)",
        		appId, devieID, subDevieID,deviceType.toString(),GetTimestr());
    }

    
}
