package com.eagle.smarthome.Alarm;

import java.io.InputStream;
import java.io.OutputStream;

import com.eagle.smarthome.Device.DeviceAI;
import com.eagle.smarthome.Device.DeviceDI;
import com.eagle.smarthome.Device.DeviceSI;
import com.eagle.smarthome.Device.DeviceType;
import com.eagle.smarthome.Device.HomeDevice;
import com.eagle.smarthome.Device.SmartHome;
import com.eagle.smarthome.Device.SmartHomeApps;
import com.eagle.smarthome.task.TaskItem;
import com.eagle.smarthome.util.SmartHomes;
import com.eagle.smarthome.util.StreamReadWrite;

public class AlarmItem  //一条报警记录
{
	public String prefix; // 前缀：( 或者空
	public String suffix; // 后缀：)
	public String combine;// 条件组合：& 或者 |,与其他监控条件的组合关系

	public int appId;
	public int devieID;
	public DeviceType deviceType;
	public short subDevieID;
	public String operate; // 数值比较符：<，=，>
	public String value; // 动作数值内容，根据deviceType决定
	public Boolean OK; //计算结果是否成立
	
	//public String taskName; // 报警时执行的任务
	//public Boolean CanDisarm; // 是否可以撤防

	public String info() {
    	return  appId+"-"+devieID+"-"+ subDevieID+"("+ deviceType+")";    
    }      
	public String Expression() {  //910.0.DO1=有信号
    	return  appId+"."+devieID+"."+ deviceType+ subDevieID+operate+value;    
    }      

    public AlarmItem()
    {
        deviceType = DeviceType.DI;  //缺省DI输入 
        operate = "=";
        value = "signal";
        prefix =" ";
        suffix = " ";
        combine="并且"; // &, | 
        //taskName = "";
        //CanDisarm = false;
    }
    public static void Copy(AlarmItem sitem, AlarmItem ditem)
    {
        ditem.appId = sitem.appId;
        ditem.devieID = sitem.devieID;
        ditem.deviceType = sitem.deviceType;
        ditem.subDevieID = sitem.subDevieID;
        ditem.operate = sitem.operate;
        ditem.value = sitem.value;
        ditem.prefix = sitem.prefix;
        ditem.suffix=  sitem.suffix;
        ditem.combine= sitem.combine;
        //ditem.OK= sitem.OK;
        //ditem.taskName = sitem.taskName;
        //ditem.CanDisarm = sitem.CanDisarm;
    }
 
    public void ReadFromStream(InputStream br)
    {
        appId = StreamReadWrite.ReadInt(br);
        devieID = StreamReadWrite.ReadInt(br);
        deviceType = DeviceType.values()[StreamReadWrite.ReadInt(br)];
        subDevieID = (short)StreamReadWrite.ReadInt(br);
        operate = StreamReadWrite.ReadString(br);
        value = StreamReadWrite.ReadString(br);
        prefix =StreamReadWrite.ReadString(br);
        suffix = StreamReadWrite.ReadString(br);
        combine = StreamReadWrite.ReadString(br);
    }
    public void WriteToStream(OutputStream bw)
    {
        StreamReadWrite.WriteInt(bw, appId);
        StreamReadWrite.WriteInt(bw, devieID);
        StreamReadWrite.WriteInt(bw, deviceType.ordinal());
        StreamReadWrite.WriteInt(bw, (int)subDevieID);
        StreamReadWrite.WriteString(bw, operate);
        StreamReadWrite.WriteString(bw, value);
        //StreamReadWrite.WriteString(bw, taskName);
        //StreamReadWrite.WriteBoolean(bw, CanDisarm);
        StreamReadWrite.WriteString(bw, prefix);
        StreamReadWrite.WriteString(bw, suffix);
        StreamReadWrite.WriteString(bw, combine);
    }
   public static int FindDeviceIndex(SmartHome sh, int DevieID)
    {
        for (int i = 0; i < sh.homedevices.size(); i++)
        {
            if (sh.homedevices.get(i).deviceid == DevieID) return i;
        }
        return -1;
    }
   public static String AlarmItemToString(AlarmItem item, SmartHome sh) //子设备的文字描述
    {
        if (sh == null) return "";
        int index = FindDeviceIndex(sh, item.devieID);
        if (index == -1) return "";
        HomeDevice hd = sh.homedevices.get(index);
        if (item.deviceType == DeviceType.DI)
        {
            DeviceDI dv = hd.DIDevices.get(item.subDevieID);
            return dv.id + "," + dv.devicetype.toString() + "," + dv.unitname + "," + dv.functiondescription;
        }
        else if (item.deviceType == DeviceType.AI)
        {
            DeviceAI dv = hd.AIDevices.get(item.subDevieID);
            return dv.id + "," + dv.devicetype.toString() + "," +  dv.unitname + "," + dv.functiondescription;
        }
        else if (item.deviceType == DeviceType.SI)
        {
            DeviceSI dv = hd.SIDevices.get(item.subDevieID);
            return dv.id+ "," + dv.devicetype.toString() + "," +dv.streamtype.toString() + "," + dv.functiondescription;
        }
        return "";
        }

    public static void StringToAlarmItem(AlarmItem item, String sub)  //子设备的文字描述转换为子设备的属性
    {
        String[] ss = sub.split(",");
        try
        {
            DeviceType dt = DeviceType.valueOf(ss[1]);
            item.deviceType = dt;
            item.subDevieID = (short)Integer.parseInt(ss[0]);
        }
        catch(Exception ex)
        {         	
        }
    } 
    public Object dv=null;
    public void GetDevice(SmartHomes homes)
    {
    	dv=FindDevice(homes);
    }
    public String GetDetailInfo()
    {
    		return String.format("%d-%d-%d,%s)",
        		appId, devieID, subDevieID,deviceType.toString());
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


}
