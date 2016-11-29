package com.eagle.smarthome.task;

import java.io.InputStream;
import java.io.OutputStream;

import com.eagle.smarthome.Device.*;
import com.eagle.smarthome.util.SmartHomes;
import com.eagle.smarthome.util.StreamReadWrite;

public class TaskItem  //一个具体场景记录
{
    public int appId ;        //某智能家居系统的唯一识别号
    public int devieID;      //设备号
    public DeviceType deviceType;   //设备类型，只能是DO,AO,SO
    public short subDevieID;  //子设备编号
    public int delayTime;        //动作延时
    public String action;   		//动作名称
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
    	for (int hm=0;hm<InstlledDeviceSystem.SmartHomeChannels.size();hm++)  //检查每个设备系统
    	{
    		int aid=InstlledDeviceSystem.SmartHomeChannels.get(hm).appid;  //设备系统编号
    		if (aid!=appId) continue;
    		String s=InstlledDeviceSystem.SmartHomeChannels.get(hm).description;
    		return s;
    	}
    	return "";
    }
    
   public  int FindHome(SmartHomeApps InstlledDeviceSystem)
   {
   	for (int hm=0;hm<InstlledDeviceSystem.SmartHomeChannels.size();hm++)  //检查每个设备系统
   	{
   		int aid=InstlledDeviceSystem.SmartHomeChannels.get(hm).appid;  //设备系统编号
   		if (aid!=appId) continue;
   		return hm;
   	}
   	return -1;
   }
   
   public String positiondescription="";
   public Object FindDevice(SmartHomes homes)  //找到属于哪个具体子设备
    {
    	for (int hm=0;hm<homes.smarthomes.size();hm++)  //检查每个设备系统
    	{
    		int aid=homes.appids.get(hm);  //设备系统编号
    		if (aid!=appId) continue;
    		SmartHome sh=homes.smarthomes.get(hm);
    	
	        for (int i = 0; i < sh.homedevices.size(); i++)  //检查每个设备系统中的每个设备
	        {
	        	HomeDevice homedevice=sh.homedevices.get(i);  //一个设备系统	        
	        	if (homedevice.deviceid!=devieID) continue;
	        	positiondescription=homedevice.positiondescription;
	        	if(deviceType==DeviceType.DO)
	        	{
	        		   for (int j = 0; j < homedevice.DODevices.size(); j++)  //检查每个设备系统中的每个设备
	        		   {
	        			    if (homedevice.DODevices.get(j).id==subDevieID) return homedevice.DODevices.get(j);
	        		   }
	        		   return null;
	        	}
	        	if(deviceType==DeviceType.AO)
	        	{
	        		   for (int j = 0; j < homedevice.AODevices.size(); j++)  //检查每个设备系统中的每个设备
	        		   {
	        			    if (homedevice.AODevices.get(j).id==subDevieID) return homedevice.AODevices.get(j);
	        		   }
	        		   return null;
	        	}	        	
	        	if(deviceType==DeviceType.SO)
	        	{
	        		   for (int j = 0; j < homedevice.SODevices.size(); j++)  //检查每个设备系统中的每个设备
	        		   {
	        			    if (homedevice.SODevices.get(j).id==subDevieID) return homedevice.SODevices.get(j);
	        		   }
	        		   return null;
	        	}	   
	        	if(deviceType==DeviceType.DI)
	        	{
	        		   for (int j = 0; j < homedevice.DIDevices.size(); j++)  //检查每个设备系统中的每个设备
	        		   {
	        			    if (homedevice.DIDevices.get(j).id==subDevieID) return homedevice.DIDevices.get(j);
	        		   }
	        		   return null;
	        	}
	        	if(deviceType==DeviceType.AI)
	        	{
	        		   for (int j = 0; j < homedevice.AIDevices.size(); j++)  //检查每个设备系统中的每个设备
	        		   {
	        			    if (homedevice.AIDevices.get(j).id==subDevieID) return homedevice.AIDevices.get(j);
	        		   }
	        		   return null;
	        	}
	        	if(deviceType==DeviceType.SI)
	        	{
	        		   for (int j = 0; j < homedevice.SIDevices.size(); j++)  //检查每个设备系统中的每个设备
	        		   {
	        			    if (homedevice.SIDevices.get(j).id==subDevieID) return homedevice.SIDevices.get(j);
	        		   }
	        		   return null;
	        	}
	        }
    	}
        return null;
    }
 
   public HomeDevice FindHomeDevice(SmartHomes homes)  //找到属于哪个具家居设备
   {
   	for (int hm=0;hm<homes.smarthomes.size();hm++)  //检查每个设备系统
   	{
   		int aid=homes.appids.get(hm);  //设备系统编号
   		if (aid!=appId) continue;
   		SmartHome sh=homes.smarthomes.get(hm);   	
        for (int i = 0; i < sh.homedevices.size(); i++)  //检查每个设备系统中的每个设备
        {
        	HomeDevice homedevice=sh.homedevices.get(i);  //一个设备系统	        
        	if (homedevice.deviceid!=devieID) continue;
        	return homedevice;
        }
   	}
       return null;
   }

   public TaskItem()
    {
        delayTime = 1;
        deviceType = DeviceType.DO;  //缺省DO动作 
        action = "";  //防止为null
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
    public static String TaskItemToString(TaskItem item, SmartHome sh) //子设备的文字描述
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
    public static void StringToTaskItem( TaskItem item, String sub)  //子设备的文字描述转换为子设备的属性
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
    public void WriteToStream(OutputStream bw)     //把对象写入流中
    {
        StreamReadWrite.WriteInt(bw, appId);
        StreamReadWrite.WriteInt(bw, devieID);
        StreamReadWrite.WriteInt(bw, deviceType.ordinal());
        StreamReadWrite.WriteInt(bw, (int)subDevieID);
        StreamReadWrite.WriteInt(bw, delayTime);
        StreamReadWrite.WriteString(bw, action);        
    }
    public void ReadFromStream(InputStream br)   //从流中读取数据
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
