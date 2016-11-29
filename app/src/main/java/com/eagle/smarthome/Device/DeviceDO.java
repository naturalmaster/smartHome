package com.eagle.smarthome.Device;

import java.io.InputStream;
import java.io.OutputStream;

import com.eagle.smarthome.util.StreamReadWrite;

public class DeviceDO 
{
	public DeviceType devicetype;
    public DOType dotype;          //7��DO����
    public PowerState powerstate; //6���ϵ�״̬
    public Boolean ON()  //ͨ��״̬:�����ֶ�
    {        
            if (powerstate == PowerState.PowerON)  //��Դ��ͨ
            {
                return (dotype == DOType.Open) ? true : false;
            }
            else
            {
                return (dotype == DOType.Open) ? false : true;
            }
    }
    public int parentid;  //1���豸���
    public short id;          //2�����豸���
    public int tag;          //3�����豸���
    public String unitname;  //4��������λ�ı�����
    public String functiondescription;  //5��DO���豸��������
    public String controldescription;  //���豸��������
    public String pictureoff;  //8��ͼ���ļ�
    public String pictureon;  //9��ͼ���ļ�
    public String blacklist;
    public String data1;
    public String data2;
    public String data3;
    public String data4;
    public DeviceDO()  //���캯��
    {
        id = 0;
        devicetype = DeviceType.DO;
        dotype = DOType.Open;           //��������
        powerstate = PowerState.PowerON;  //��Դ�Ͽ�            
        pictureoff = "";
        pictureon = "";
        unitname = "";
        functiondescription = "DO���豸";
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
        i = StreamReadWrite.ReadInt(br);
        dotype = DOType.values()[i];    //            dotype = (DOType)br.ReadByte();             //6��DO����
        i = StreamReadWrite.ReadInt(br);
        powerstate = PowerState.values()[i];  //powerstate = (SmartControlLib.PowerState)br.ReadByte();     //7���ϵ�״̬
        pictureoff = StreamReadWrite.ReadString(br); //br.ReadString(); //8��ͼ���ļ�
        pictureon = StreamReadWrite.ReadString(br);  //br.ReadString();  //9��ͼ���ļ�
        controldescription = StreamReadWrite.ReadString(br); //br.ReadString();   //���豸��������
        blacklist = StreamReadWrite.ReadString(br);//    public String blacklist;
        data1 = StreamReadWrite.ReadString(br);
        data2 = StreamReadWrite.ReadString(br);
        data3 = StreamReadWrite.ReadString(br);
        data4 = StreamReadWrite.ReadString(br);
    }
    public void WriteToStream(OutputStream bw)
    {
        StreamReadWrite.WriteInt(bw, devicetype.ordinal()); //bw.Write((byte)devicetype);
        StreamReadWrite.WriteInt(bw, parentid); //bw.Write(parentid);    //1���豸���
        StreamReadWrite.WriteInt(bw, id); //bw.Write(id);          //2�����豸���
        StreamReadWrite.WriteInt(bw, tag); //bw.Write(tag);         //3��tag����
        StreamReadWrite.WriteString(bw, unitname); //bw.Write(unitname);    //4��������λ�ı�����
        StreamReadWrite.WriteString(bw, functiondescription); //bw.Write(functiondescription);  //5��DO���豸��������
        StreamReadWrite.WriteInt(bw, dotype.ordinal());//bw.Write((byte)dotype);         //6��DO����
        StreamReadWrite.WriteInt(bw, powerstate.ordinal()); //bw.Write((byte)powerstate);     //7���ϵ�״̬
        StreamReadWrite.WriteString(bw, pictureoff); //bw.Write(pictureoff);           //8��ͼ���ļ�
        StreamReadWrite.WriteString(bw, pictureon); //bw.Write(pictureon);            //9��ͼ���ļ�
        StreamReadWrite.WriteString(bw, controldescription); //bw.Write(controldescription);   //���豸��������  controldescription = br.ReadString();   //���豸��������
        StreamReadWrite.WriteString(bw, blacklist);  //  blacklist = StreamReadWrite.ReadString(br);//    public String blacklist;
        StreamReadWrite.WriteString(bw, data1);  //����
        StreamReadWrite.WriteString(bw, data2);  //����
        StreamReadWrite.WriteString(bw, data3);  //����
        StreamReadWrite.WriteString(bw, data4);  //����
    }

}
