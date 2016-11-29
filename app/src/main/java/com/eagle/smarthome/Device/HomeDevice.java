package com.eagle.smarthome.Device;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Vector;

import com.eagle.smarthome.util.StreamReadWrite;

public class HomeDevice   //金鹰智能家居设备定义：一类设备
{
    public int deviceid;  //1   
    public String devicename;  //2  
    public String positiondescription;  //3 
    public Boolean used;  //4  

    public String GetState(DeviceType deviceType)
    {
        String result = "";
        if (deviceType == DeviceType.DO)
        {
            for (int i = 0; i < DODevices.size(); i++)
            {
               DeviceDO dv = DODevices.get(i);
                result += String.format("(%02d)%s%s ", dv.id + 1, dv.unitname, (dv.ON() ? "√" : "ㄨ"));
            }
        }
        else if (deviceType == DeviceType.DI)
        {
            for (int i = 0; i < DIDevices.size(); i++)
            {
               DeviceDI dv = DIDevices.get(i);
                result += String.format("(%02d)%s%s ", dv.id + 1, dv.unitname, (dv.hassignal ? "√" : "ㄨ"));
            }
        }
        else if (deviceType == DeviceType.AI)
        {
            for (int i = 0; i < AIDevices.size(); i++)
            {
               DeviceAI dv = AIDevices.get(i);    
               String fstr = "(%02d)%3." + String.format("%d",dv.dotplace) +"f%s";
                result += String.format(fstr, dv.id + 1, dv.aivalue, dv.unitname);
                if (i < AIDevices.size() - 1) result += ", ";
            }
        }
        else if (deviceType == DeviceType.AO)
        {
            for (int i = 0; i < AODevices.size(); i++)
            {
               DeviceAO dv = AODevices.get(i);
               String fstr = "(%02d)%3." + String.format("%d",dv.dotplace) +"f%s";
                result += String.format(fstr, dv.id + 1, dv.aovalue, dv.unitname);
                if (i < AODevices.size() - 1) result += ",,";
            }
        }
        else if (deviceType == DeviceType.SI)
        {
            for (int i = 0; i < SIDevices.size(); i++)
            {
               DeviceSI dv = SIDevices.get(i);
                if (dv.streamtype == StreamType.TEXT && dv.sivalue != null)
                    result += String.format("%d:%s ", dv.id + 1, new String(dv.sivalue,Charset.forName("UTF-8")));            				
                else result += String.format("%d:%s} ", dv.id + 1, dv.streamtype.toString());
            }
        }
        else if (deviceType == DeviceType.SO)
        {
            for (int i = 0; i < SODevices.size(); i++)
            {
               DeviceSO dv = SODevices.get(i);
                if (dv.streamtype == StreamType.TEXT && dv.sovalue != null)
                    result += String.format("%d:%s ", dv.id + 1, new String(dv.sovalue,Charset.forName("UTF-8")));
                else result += String.format("%d:%s ", dv.id + 1, dv.streamtype.toString());
            }
        }
        return result;
    }
    public HomeDevice()  //构造函数
    {
        devicename = "";
        positiondescription = "";
        DODevices = new Vector<DeviceDO>();
        DIDevices = new Vector<DeviceDI>();
        AODevices = new Vector<DeviceAO>();
        AIDevices = new Vector<DeviceAI>();
        SODevices = new Vector<DeviceSO>();
        SIDevices = new Vector<DeviceSI>();
    }
    void Close()
    {
        DODevices.clear();
        DIDevices.clear();
        AODevices.clear();
        AIDevices.clear();
        SODevices.clear();
        SIDevices.clear();
    }
    
    public Vector<Object> GetAllDevices()
    {
    	Vector<Object> devices=new Vector<Object>();
    	 for (int i = 0; i < DODevices.size(); i++)
    		 devices.add(DODevices.get(i));

    	 for (int i = 0; i < DIDevices.size(); i++)
    		 devices.add(DIDevices.get(i));

    	 for (int i = 0; i < AODevices.size(); i++)
    		 devices.add(AODevices.get(i));

    	 for (int i = 0; i < AIDevices.size(); i++)
    		 devices.add(AIDevices.get(i));
    	 
    	 for (int i = 0; i < SODevices.size(); i++)
    		 devices.add(SODevices.get(i));

    	 for (int i = 0; i < SIDevices.size(); i++)
    		 devices.add(SIDevices.get(i));
    	return devices;
    }
    
    public Vector<Object> GetAllInputDevices()
    {
    	Vector<Object> devices=new Vector<Object>();

    	 for (int i = 0; i < DIDevices.size(); i++)
    		 devices.add(DIDevices.get(i));

    	 for (int i = 0; i < AIDevices.size(); i++)
    		 devices.add(AIDevices.get(i));

    	 for (int i = 0; i < SIDevices.size(); i++)
    		 devices.add(SIDevices.get(i));
    	return devices;
    }
    
    public void ReadFromStream(InputStream br)
    {
        deviceid = StreamReadWrite.ReadInt(br); // br.ReadInt32();      //1 设备唯一识别码：
        used = StreamReadWrite.ReadBoolean(br); // br.ReadBoolean();         //2 设备能否被使用或禁止，被禁用的设备不受监控
        devicename = StreamReadWrite.ReadString(br); // br.ReadString();              //3 设备名称文本描述
        positiondescription = StreamReadWrite.ReadString(br);// br.ReadString();     //4 设备位置信息描述

        int count = StreamReadWrite.ReadInt(br);// br.ReadUInt16();               //5 DO子设备的数量
        DODevices.clear();
        for (int i = 0; i < count; i++)
        {
            DeviceDO device = new DeviceDO();
            device.ReadFromStream(br);
            DODevices.add(device);
        }
        count = StreamReadWrite.ReadInt(br);// br.ReadUInt16();               //6 DI子设备的数量
        DIDevices.clear();
        for (int i = 0; i < count; i++)
        {
            DeviceDI device = new DeviceDI();
            device.ReadFromStream(br);
            DIDevices.add(device);
        }

        count = StreamReadWrite.ReadInt(br); //br.ReadUInt16();               //7 AO子设备的数量
        AODevices.clear();
        for (int i = 0; i < count; i++)
        {
            DeviceAO device = new DeviceAO();
            device.ReadFromStream(br);
            AODevices.add(device);
        }
        count = StreamReadWrite.ReadInt(br); //br.ReadUInt16();               //8 AI子设备的数量
        AIDevices.clear();
        for (int i = 0; i < count; i++)
        {
            DeviceAI device = new DeviceAI();
            device.ReadFromStream(br);
            AIDevices.add(device);
        }
        count = StreamReadWrite.ReadInt(br); //br.ReadUInt16();               //9 SO子设备的数量
        SODevices.clear();
        for (int i = 0; i < count; i++)
        {
            DeviceSO device = new DeviceSO();
            device.ReadFromStream(br);
            SODevices.add(device);
        }
        count = StreamReadWrite.ReadInt(br); //br.ReadUInt16();               //10 SI子设备的数量
        SIDevices.clear();
        for (int i = 0; i < count; i++)
        {
            DeviceSI device = new DeviceSI();
            device.ReadFromStream(br);
            SIDevices.add(device);
        }
    }
    public void WriteToStream(OutputStream bw)
    {
        StreamReadWrite.WriteInt(bw, deviceid); //bw.Write(DeviceID);     //1 设备唯一识别码：
        StreamReadWrite.WriteBoolean(bw, used); //bw.Write(Used);         //2 设备能否被使用或禁止，被禁用的设备不受监控
        StreamReadWrite.WriteString(bw, devicename);// bw.Write(DeviceName);            //3 设备名称文本描述
        StreamReadWrite.WriteString(bw, positiondescription); //bw.Write(PositionDescription);   //4 设备位置信息描述

        StreamReadWrite.WriteInt(bw, DODevices.size()); //bw.Write((ushort)DODevices.Count);     //5 DO子设备的数量
        for (int i = 0; i < DODevices.size(); i++)
        {
           DeviceDO device = DODevices.get(i);
            device.WriteToStream(bw);
        }
        StreamReadWrite.WriteInt(bw, DIDevices.size()); //bw.Write((ushort)DIDevices.Count);     //6 DI子设备的数量
        for (int i = 0; i < DIDevices.size(); i++)
        {
           DeviceDI device = DIDevices.get(i);
            device.WriteToStream(bw);
        }
        StreamReadWrite.WriteInt(bw, AODevices.size()); //bw.Write((ushort)AODevices.Count);     //7 AO子设备的数量
        for (int i = 0; i < AODevices.size(); i++)
        {
           DeviceAO device = AODevices.get(i);
            device.WriteToStream(bw);
        }
        StreamReadWrite.WriteInt(bw, AIDevices.size()); //bw.Write(AIDevices.Count);     //8 AI子设备的数量
        for (int i = 0; i < AIDevices.size(); i++)
        {
           DeviceAI device = AIDevices.get(i);
            device.WriteToStream(bw);
        }
        StreamReadWrite.WriteInt(bw, SODevices.size()); //bw.Write((ushort)SODevices.Count);     //9 SO子设备的数量
        for (int i = 0; i < SODevices.size(); i++)
        {
           DeviceSO device = SODevices.get(i);
            device.WriteToStream(bw);
        }
        StreamReadWrite.WriteInt(bw, SIDevices.size()); //bw.Write((ushort)SIDevices.Count);     //10 SI子设备的数量
        for (int i = 0; i < SIDevices.size(); i++)
        {
           DeviceSI device = SIDevices.get(i);
            device.WriteToStream(bw);
        }
    }

    public Vector<DeviceAI> AIDevices;  //5
    public Vector<DeviceAO> AODevices;  //6   
    public Vector<DeviceDI> DIDevices;  //7  
    public Vector<DeviceDO> DODevices;   
    public Vector<DeviceSI> SIDevices;  //9  
    public Vector<DeviceSO> SODevices;  //10
  
    public int GetAIDeviceCount()
    {
        return AIDevices.size();
    }
    public int GetAODeviceCount()
    {
        return AODevices.size();
    }
    public int GetDIDeviceCount()
    {
        return DIDevices.size();
    }
    public int GetDODeviceCount()
    {
        return DODevices.size();
    }
    public int GetSIDeviceCount()
    {
        return SIDevices.size();
    }
    public int GetSODeviceCount()
    {
        return SODevices.size();
    }
    public DeviceAI MakeNewDeviceAI()
    {
        DeviceAI dv = new DeviceAI();
        dv.parentid = deviceid;
        dv.id = (short)AIDevices.size();
        return dv;
    }
    public DeviceAO MakeNewDeviceAO()
    {
        DeviceAO dv = new DeviceAO();
        dv.parentid = deviceid;
        dv.id = (short)AODevices.size();
        return dv;
    }
    public DeviceDI MakeNewDeviceDI()
    {
        DeviceDI dv = new DeviceDI();
        dv.parentid = deviceid;
        dv.id = (short)DIDevices.size();
        return dv;
    }
    public DeviceDO MakeNewDeviceDO()
    {
        DeviceDO dv = new DeviceDO();
        dv.parentid = deviceid;
        dv.id = (short)DODevices.size();
        return dv;
    }
    public DeviceSI MakeNewDeviceSI()
    {
        DeviceSI dv = new DeviceSI();
        dv.parentid = deviceid;
        dv.id = (short)SIDevices.size();
        return dv;
    }
    public DeviceSO MakeNewDeviceSO()
    {
        DeviceSO dv = new DeviceSO();
        dv.parentid = deviceid;
        dv.id = (short)SODevices.size();
        return dv;
    }
}

