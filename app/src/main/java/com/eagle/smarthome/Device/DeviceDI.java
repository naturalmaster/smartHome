package com.eagle.smarthome.Device;
import java.io.InputStream;
import java.io.OutputStream;

import com.eagle.smarthome.util.StreamReadWrite;

public class DeviceDI
{
	public DeviceType devicetype;
	public int parentid;  //1、设备编号    
	public short id;          //2、子设备编号   
	public int tag;          //3、子设备编号  
    public String unitname;   //4、计量单位文本描述  
    public String functiondescription;  //5、AO子设备功能描述
    public String controldescription;  //子设备操作描述
    public Boolean hassignal;  //6、是否有信号  
    public String pictureoff;  //7、OFF图像
    public String pictureon;   //8、ON图像
    public String blacklist;
    public String data1;
    public String data2;
    public String data3;
    public String data4;
    public DeviceDI()
    {
        devicetype = DeviceType.DI;
        functiondescription = "DI子设备";
        pictureoff = "";
        pictureon = "";
        unitname = "";
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
        hassignal = StreamReadWrite.ReadBoolean(br);

        pictureoff = StreamReadWrite.ReadString(br); //br.ReadString(); //8、图像文件
        pictureon = StreamReadWrite.ReadString(br); //br.ReadString(); //8、图像文件
        controldescription = StreamReadWrite.ReadString(br);
        blacklist = StreamReadWrite.ReadString(br);//    public String blacklist;
        data1 = StreamReadWrite.ReadString(br);
        data2 = StreamReadWrite.ReadString(br);
        data3 = StreamReadWrite.ReadString(br);
        data4 = StreamReadWrite.ReadString(br);
    }
    public void WriteToStream(OutputStream bw)
    {
        StreamReadWrite.WriteInt(bw, devicetype.ordinal()); //bw.Write((byte)devicetype);
        StreamReadWrite.WriteInt(bw, parentid); //bw.Write(parentid);    //1、设备编号
        StreamReadWrite.WriteInt(bw, id); //bw.Write(id);          //2、子设备编号
        StreamReadWrite.WriteInt(bw, tag); //bw.Write(tag);         //3、tag对象
        StreamReadWrite.WriteString(bw, unitname); //bw.Write(unitname);    //4、计量单位文本描述
        StreamReadWrite.WriteString(bw, functiondescription); //bw.Write(functiondescription);  //5、DO子设备功能描述
        StreamReadWrite.WriteBoolean(bw, hassignal);//bw.Write((byte)dotype);         //6、DO类型
        StreamReadWrite.WriteString(bw, pictureoff); //bw.Write(pictureoff);           //8、图像文件
        StreamReadWrite.WriteString(bw, pictureon); //bw.Write(pictureon);           //8、图像文件
        StreamReadWrite.WriteString(bw, controldescription); //bw.Write(controldescription);   //子设备操作描述  controldescription = br.ReadString();   //子设备操作描述
        StreamReadWrite.WriteString(bw, blacklist);  //  blacklist = StreamReadWrite.ReadString(br);//    public String blacklist;
        StreamReadWrite.WriteString(bw, data1);  //保留
        StreamReadWrite.WriteString(bw, data2);  //保留
        StreamReadWrite.WriteString(bw, data3);  //保留
        StreamReadWrite.WriteString(bw, data4);  //保留
    }
    
}
