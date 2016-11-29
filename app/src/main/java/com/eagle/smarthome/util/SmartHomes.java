package com.eagle.smarthome.util;

import java.util.Vector;

import com.eagle.smarthome.Device.HomeDevice;
import com.eagle.smarthome.Device.SmartHome;

//import android.content.Context;

/// <summary>
/// ���ܼҾ��豸ϵͳ������ϵͳ��Ӳ���ṹ
/// ���Ի�ȡ�κεǼǵ�ĳ���豸�������豸����Ϣ
/// </summary>
public class SmartHomes  //���ܼҾ��豸ϵͳ
{
    public Vector<SmartHome> smarthomes = null;   //��ͬ���ܼҾ�ϵͳ�б�        
    public Vector<Integer> appids = null;             //��ͬ���ܼҾӳ����id
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

    String[] GetAllAppInfo()  //����SH�豸ϵͳ��������Ϣ
    {
    	Vector<String> result = new Vector<String>();
        for (int i = 0; i < smarthomes.size(); i++)
        {
            result.add(appids.get(i)+ "," + smarthomes.get(i).homename);
        }
        return (String[])result.toArray();
    }
    public String[] GetOneSmartHomeInfo(int appid)  //��ȡһ��SH�豸Ӧ��ϵͳ����Ϣ���������������豸
    {
        SmartHome sh = FindSmartHomeByAppid(appid);
        if (sh == null) return null;
        Vector<String> result = new Vector<String>();
        for (int i = 0; i < sh.homedevices.size(); i++)  //�����⣬����ӵ��豸�������������������֣���
        {
            result.add(sh.homedevices.get(i).deviceid + "," + sh.homedevices.get(i).devicename);
        }
        return (String[])result.toArray();
    }
    public String[] GetAllOutputSubdeviceInfo(int appid, int deviceid) //��ȡĳ���豸������������豸��Ϣ
    {
        SmartHome sh = FindSmartHomeByAppid(appid);
        if (sh == null) return null;
        int index = -1;
        for (int i = 0; i < sh.homedevices.size(); i++)
        {
            if (sh.homedevices.get(i).deviceid == deviceid) //�ж�Ӧ���豸
            {
                index = i; break;
            }
        }
        if (index == -1) return null;
        Vector<String> result = new Vector<String>();
        HomeDevice hd = sh.homedevices.get(index);
        for (int j = 0; j < hd.DODevices.size(); j++)  //DO���豸
            result.add(hd.DODevices.get(j).id + "," +
                       hd.DODevices.get(j).devicetype.toString() + "," + hd.DODevices.get(j).dotype.toString() + "," +
                       hd.DODevices.get(j).functiondescription);

        for (int j = 0; j < hd.AODevices.size(); j++)  //AO���豸
            result.add(hd.AODevices.get(j).id + "," +
                       hd.AODevices.get(j).devicetype.toString() + "," + hd.AODevices.get(j).unitname + "," +
                       hd.AODevices.get(j).functiondescription);

        for (int j = 0; j < hd.SODevices.size(); j++)  //SO���豸
            result.add(hd.SODevices.get(j).id + "," +
                       hd.SODevices.get(j).devicetype.toString() + "," + hd.SODevices.get(j).streamtype.toString() + "," +
                       hd.SODevices.get(j).functiondescription);
        return (String[])result.toArray();
    }
    public String[] GetAllInputSubdeviceInfo(int appid, int deviceid) //��ȡĳ���豸�������������豸��Ϣ
    {
        SmartHome sh = FindSmartHomeByAppid(appid);
        if (sh == null) return null;
        int index = -1;
        for (int i = 0; i < sh.homedevices.size(); i++)
        {
            if (sh.homedevices.get(i).deviceid == deviceid) //�ж�Ӧ���豸
            {
                index = i; break;
            }
        }
        if (index == -1) return null;
        Vector<String> result = new Vector<String>();
        HomeDevice hd = sh.homedevices.get(index);
        for (int j = 0; j < hd.DIDevices.size(); j++)  //DI���豸
            result.add(hd.DIDevices.get(j).id + "," +
                       hd.DIDevices.get(j).devicetype.toString() + "," + hd.DIDevices.get(j).unitname + "," +
                       hd.DIDevices.get(j).functiondescription);

        for (int j = 0; j < hd.AIDevices.size(); j++)  //AI���豸
            result.add(hd.AIDevices.get(j).id + "," +
                       hd.AIDevices.get(j).devicetype.toString() + "," + hd.AIDevices.get(j).unitname + "," +
                       hd.AIDevices.get(j).functiondescription);

        for (int j = 0; j < hd.SIDevices.size(); j++)  //SI���豸
            result.add(hd.SIDevices.get(j).id + "," +
                       hd.SIDevices.get(j).devicetype.toString() + "," + hd.SIDevices.get(j).streamtype.toString() + "," +
                       hd.SIDevices.get(j).functiondescription);
        return (String[])result.toArray();
    }
    public String[] GetAllSubdeviceInfo(int appid, int deviceid) //��ȡĳ���豸����������������豸��Ϣ
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
    public void Add(int appid, String dll)  //����һ��SH�豸ϵͳ��dll�����ġ���������:���ﲻʹ�ã�ֱ�Ӵ��ļ������豸��Ϣ
    {
        //SmartHome SmartHome = null;
        //IsolatedStorageSettings AppSetting = IsolatedStorageSettings.ApplicationSettings;  //ϵͳ����
        //String lastMonitorSystem = (String)AppSetting["lastMonitorSystem"];  //��ص�ϵͳ
        //String appfn =lastMonitorSystem + "\\smarthome" + appid.ToString() + ".smh";
        //SmartHome = (SmartHome)(new SmartHome(appfn));  //�������������ˣ�
        //SmartHome(Context context,String workdir, String _filename) 
        //smarthomes.add(SmartHome);
        //appids.add(appid);
    }
} 
