package com.eagle.smarthome.Device;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Locale;

import com.eagle.smarthome.util.StreamReadWrite;

public class DeviceAI
{
    public DeviceType devicetype;
    public int parentid;  //1���豸���  
    public short id;          //2�����豸���
    public int tag;           //3��ͨ������
    public String unitname;   //4��������λ�ı�����
    public String functiondescription;  //5��AO���豸��������
    public String controldescription;  //���豸��������
    public float aivalue; 
    public String picture;  //7��ͼ���ļ�  
    public byte dotplace;   
    public String blacklist;
    public String data1;
    public String data2;
    public String data3;
    public String data4;

    public DeviceAI()
    {
        devicetype = DeviceType.AI;
        functiondescription = "AI���豸";
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
        parentid = StreamReadWrite.ReadInt(br);// br.ReadInt32();     //1���豸���
        id = (short)StreamReadWrite.ReadInt(br);// br.ReadUInt16();          //2�����豸���
        tag = StreamReadWrite.ReadInt(br);// br.ReadInt32();          //3��tag
        unitname = StreamReadWrite.ReadString(br);  //br.ReadString();    //4��������λ�ı�����
        functiondescription = StreamReadWrite.ReadString(br); //br.ReadString();      //5��DO���豸��������
        String f = StreamReadWrite.ReadString(br);
        try
        {       
        	aivalue = (Float) NumberFormat.getNumberInstance(Locale.US).parse(f);     
        }
        catch (Exception e)
        {
        	aivalue=0;
        }
        picture = StreamReadWrite.ReadString(br); //br.ReadString(); //8��ͼ���ļ�
        dotplace = (byte)StreamReadWrite.ReadInt(br);  //br.ReadString();  //9��ͼ���ļ�
        controldescription = StreamReadWrite.ReadString(br); //br.ReadString();   //���豸��������
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
        StreamReadWrite.WriteInt(bw, parentid); //bw.Write(parentid);    //1���豸���
        StreamReadWrite.WriteInt(bw, id); //bw.Write(id);          //2�����豸���
        StreamReadWrite.WriteInt(bw, tag); //bw.Write(tag);         //3��tag����
        StreamReadWrite.WriteString(bw, unitname); //bw.Write(unitname);    //4��������λ�ı�����
        StreamReadWrite.WriteString(bw, functiondescription); //bw.Write(functiondescription);  //5��DO���豸��������
        StreamReadWrite.WriteString(bw, ""+aivalue);//bw.Write((byte)dotype);         //6��DO����: Java��C#������������ô�죿
        StreamReadWrite.WriteString(bw, picture); //bw.Write(pictureoff);           //8��ͼ���ļ�
        StreamReadWrite.WriteInt(bw, (int)dotplace);//bw.Write((byte)dotype);       
        StreamReadWrite.WriteString(bw, controldescription); //bw.Write(controldescription);   //���豸��������  controldescription = br.ReadString();   //���豸��������
        StreamReadWrite.WriteString(bw, blacklist);  //  blacklist = StreamReadWrite.ReadString(br);
        StreamReadWrite.WriteString(bw, data1);  //����
        StreamReadWrite.WriteString(bw, data2);  //����
        StreamReadWrite.WriteString(bw, data3);  //����
        StreamReadWrite.WriteString(bw, data4);  //����
    }

}
