package com.eagle.smarthome.Device;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Locale;

import com.eagle.smarthome.util.StreamReadWrite;

public class DeviceAI
{
    public DeviceType devicetype;
    public int parentid;  //1、设备编号  
    public short id;          //2、子设备编号
    public int tag;           //3、通用数据
    public String unitname;   //4、计量单位文本描述
    public String functiondescription;  //5、AO子设备功能描述
    public String controldescription;  //子设备操作描述
    public float aivalue; 
    public String picture;  //7、图像文件  
    public byte dotplace;   
    public String blacklist;
    public String data1;
    public String data2;
    public String data3;
    public String data4;

    public DeviceAI()
    {
        devicetype = DeviceType.AI;
        functiondescription = "AI子设备";
        picture = "";
        unitname = "";
        dotplace = 0;
        controldescription = "";
        blacklist="";
        data1="";
        data2="";
        data3="";
        data4="";
    }
    public void ReadFromStream(InputStream br)
    {
        int i = StreamReadWrite.ReadInt(br);
        devicetype = DeviceType.values()[i];
        parentid = StreamReadWrite.ReadInt(br);// br.ReadInt32();     //1、设备编号
        id = (short)StreamReadWrite.ReadInt(br);// br.ReadUInt16();          //2、子设备编号
        tag = StreamReadWrite.ReadInt(br);// br.ReadInt32();          //3、tag
        unitname = StreamReadWrite.ReadString(br);  //br.ReadString();    //4、计量单位文本描述
        functiondescription = StreamReadWrite.ReadString(br); //br.ReadString();      //5、DO子设备功能描述
        String f = StreamReadWrite.ReadString(br);
        try
        {       
        	aivalue = (Float) NumberFormat.getNumberInstance(Locale.US).parse(f);     
        }
        catch (Exception e)
        {
        	aivalue=0;
        }
        picture = StreamReadWrite.ReadString(br); //br.ReadString(); //8、图像文件
        dotplace = (byte)StreamReadWrite.ReadInt(br);  //br.ReadString();  //9、图像文件
        controldescription = StreamReadWrite.ReadString(br); //br.ReadString();   //子设备操作描述
        blacklist = StreamReadWrite.ReadString(br);
        data1 = StreamReadWrite.ReadString(br);
        data2 = StreamReadWrite.ReadString(br);
        data3 = StreamReadWrite.ReadString(br);
        data4 = StreamReadWrite.ReadString(br);
        //StreamReadWrite.WriteString(bw, blacklist);  //  blacklist = StreamReadWrite.ReadString(br);//    public String blacklist;
    }

    public void WriteToStream(OutputStream bw)
    {
        StreamReadWrite.WriteInt(bw, devicetype.ordinal()); //bw.Write((byte)devicetype);
        StreamReadWrite.WriteInt(bw, parentid); //bw.Write(parentid);    //1、设备编号
        StreamReadWrite.WriteInt(bw, id); //bw.Write(id);          //2、子设备编号
        StreamReadWrite.WriteInt(bw, tag); //bw.Write(tag);         //3、tag对象
        StreamReadWrite.WriteString(bw, unitname); //bw.Write(unitname);    //4、计量单位文本描述
        StreamReadWrite.WriteString(bw, functiondescription); //bw.Write(functiondescription);  //5、DO子设备功能描述
        StreamReadWrite.WriteString(bw, ""+aivalue);//bw.Write((byte)dotype);         //6、DO类型: Java和C#可能有区别，怎么办？
        StreamReadWrite.WriteString(bw, picture); //bw.Write(pictureoff);           //8、图像文件
        StreamReadWrite.WriteInt(bw, (int)dotplace);//bw.Write((byte)dotype);       
        StreamReadWrite.WriteString(bw, controldescription); //bw.Write(controldescription);   //子设备操作描述  controldescription = br.ReadString();   //子设备操作描述
        StreamReadWrite.WriteString(bw, blacklist);  //  blacklist = StreamReadWrite.ReadString(br);
        StreamReadWrite.WriteString(bw, data1);  //保留
        StreamReadWrite.WriteString(bw, data2);  //保留
        StreamReadWrite.WriteString(bw, data3);  //保留
        StreamReadWrite.WriteString(bw, data4);  //保留
    }

}
