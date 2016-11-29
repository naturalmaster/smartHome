package com.eagle.smarthome.util;

import java.util.Vector;

import com.eagle.smarthome.Device.HomeDevice;
import com.eagle.smarthome.Device.SmartHome;

//import android.content.Context;

/// <summary>
/// 智能家居设备系统：整个系统的硬件结构
/// 可以获取任何登记的某个设备及其子设备的信息
/// </summary>
public class SmartHomes  //智能家居设备系统
{
    public Vector<SmartHome> smarthomes = null;   //不同智能家居系统列表        
    public Vector<Integer> appids = null;             //不同智能家居程序的id
    //public String Filename;
    public SmartHomes()
    {
        smarthomes = new Vector<SmartHome>();
        appids = new Vector<Integer>();
    }
    void Close()
    {
        Clear();
    }
    public SmartHome FindSmartHomeByAppid(int appid)
    {
        for (int i = 0; i < smarthomes.size(); i++)
        {
            if (appid == appids.get(i)) return smarthomes.get(i);
        }
        return null;
    }

    public HomeDevice FindSmartHomeDeviceById(int appid,int deviceid)
    {
        SmartHome sh = FindSmartHomeByAppid(appid);
        if (sh == null) return null;
        for (int i = 0; i < sh.homedevices.size(); i++)
        {
            if (deviceid == sh.homedevices.get(i).deviceid) return sh.homedevices.get(i);
        }
        return null;
    }

    String[] GetAllAppInfo()  //所有SH设备系统的描述信息
    {
    	Vector<String> result = new Vector<String>();
        for (int i = 0; i < smarthomes.size(); i++)
        {
            result.add(appids.get(i)+ "," + smarthomes.get(i).homename);
        }
        return (String[])result.toArray();
    }
    public String[] GetOneSmartHomeInfo(int appid)  //获取一个SH设备应用系统的信息：它包含的所有设备
    {
        SmartHome sh = FindSmartHomeByAppid(appid);
        if (sh == null) return null;
        Vector<String> result = new Vector<String>();
        for (int i = 0; i < sh.homedevices.size(); i++)  //有问题，新添加的设备不在驱动程序里面体现！！
        {
            result.add(sh.homedevices.get(i).deviceid + "," + sh.homedevices.get(i).devicename);
        }
        return (String[])result.toArray();
    }
    public String[] GetAllOutputSubdeviceInfo(int appid, int deviceid) //获取某个设备的所有输出子设备信息
    {
        SmartHome sh = FindSmartHomeByAppid(appid);
        if (sh == null) return null;
        int index = -1;
        for (int i = 0; i < sh.homedevices.size(); i++)
        {
            if (sh.homedevices.get(i).deviceid == deviceid) //有对应的设备
            {
                index = i; break;
            }
        }
        if (index == -1) return null;
        Vector<String> result = new Vector<String>();
        HomeDevice hd = sh.homedevices.get(index);
        for (int j = 0; j < hd.DODevices.size(); j++)  //DO子设备
            result.add(hd.DODevices.get(j).id + "," +
                       hd.DODevices.get(j).devicetype.toString() + "," + hd.DODevices.get(j).dotype.toString() + "," +
                       hd.DODevices.get(j).functiondescription);

        for (int j = 0; j < hd.AODevices.size(); j++)  //AO子设备
            result.add(hd.AODevices.get(j).id + "," +
                       hd.AODevices.get(j).devicetype.toString() + "," + hd.AODevices.get(j).unitname + "," +
                       hd.AODevices.get(j).functiondescription);

        for (int j = 0; j < hd.SODevices.size(); j++)  //SO子设备
            result.add(hd.SODevices.get(j).id + "," +
                       hd.SODevices.get(j).devicetype.toString() + "," + hd.SODevices.get(j).streamtype.toString() + "," +
                       hd.SODevices.get(j).functiondescription);
        return (String[])result.toArray();
    }
    public String[] GetAllInputSubdeviceInfo(int appid, int deviceid) //获取某个设备的所有输入子设备信息
    {
        SmartHome sh = FindSmartHomeByAppid(appid);
        if (sh == null) return null;
        int index = -1;
        for (int i = 0; i < sh.homedevices.size(); i++)
        {
            if (sh.homedevices.get(i).deviceid == deviceid) //有对应的设备
            {
                index = i; break;
            }
        }
        if (index == -1) return null;
        Vector<String> result = new Vector<String>();
        HomeDevice hd = sh.homedevices.get(index);
        for (int j = 0; j < hd.DIDevices.size(); j++)  //DI子设备
            result.add(hd.DIDevices.get(j).id + "," +
                       hd.DIDevices.get(j).devicetype.toString() + "," + hd.DIDevices.get(j).unitname + "," +
                       hd.DIDevices.get(j).functiondescription);

        for (int j = 0; j < hd.AIDevices.size(); j++)  //AI子设备
            result.add(hd.AIDevices.get(j).id + "," +
                       hd.AIDevices.get(j).devicetype.toString() + "," + hd.AIDevices.get(j).unitname + "," +
                       hd.AIDevices.get(j).functiondescription);

        for (int j = 0; j < hd.SIDevices.size(); j++)  //SI子设备
            result.add(hd.SIDevices.get(j).id + "," +
                       hd.SIDevices.get(j).devicetype.toString() + "," + hd.SIDevices.get(j).streamtype.toString() + "," +
                       hd.SIDevices.get(j).functiondescription);
        return (String[])result.toArray();
    }
    public String[] GetAllSubdeviceInfo(int appid, int deviceid) //获取某个设备的所有输入输出子设备信息
    {
        String[] outInfo = GetAllOutputSubdeviceInfo(appid, deviceid);
        String[] inInfo = GetAllInputSubdeviceInfo(appid, deviceid);
        Vector<String> result = new Vector<String>();
        for ( String s:outInfo)
        	result.add(s);
        for ( String s:inInfo)
        	result.add(s);
        return (String[])result.toArray();
    }
    public void Clear()
    {
        /*for (int i = smarthomes.size() - 1; i >= 0; i--)
        {
        	SmartHome sh=smarthomes.get(i);
        	sh=null;
        }*/
        smarthomes.clear();
        appids.clear();
    }
    public void RemoveAt(int index)
    {
        smarthomes.remove(index);
        appids.remove(index);
    }
    public void Add(int appid, String dll)  //增加一个SH设备系统，dll是它的“驱动程序”:这里不使用，直接从文件加载设备信息
    {
        //SmartHome SmartHome = null;
        //IsolatedStorageSettings AppSetting = IsolatedStorageSettings.ApplicationSettings;  //系统设置
        //String lastMonitorSystem = (String)AppSetting["lastMonitorSystem"];  //监控的系统
        //String appfn =lastMonitorSystem + "\\smarthome" + appid.ToString() + ".smh";
        //SmartHome = (SmartHome)(new SmartHome(appfn));  //工作都在这里了！
        //SmartHome(Context context,String workdir, String _filename) 
        //smarthomes.add(SmartHome);
        //appids.add(appid);
    }
} 
