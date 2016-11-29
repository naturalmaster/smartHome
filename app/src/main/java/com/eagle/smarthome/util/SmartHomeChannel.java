package com.eagle.smarthome.util;

import com.eagle.smarthome.StringJson;

public class SmartHomeChannel
{
    public static int SHFLAG = 0x5A5A5A5A;  //���ܼҾ�ϵͳͳһ��ʶ�����ڸ�������ͨ�Ź���
    public static String sharememoryfile = "SmartHomeShareFile";
    public static String mutexname = "SmartHomeMutex";

    public int appid;              //ĳƷ�����ܼҾ�Ψһʶ���

    public Boolean canused;           //ϵͳ�ܷ�ʹ��
    public Boolean autostart;         //�Զ�����  

    public Boolean loaded;            //��س���SHM�Ƿ��Ѿ����أ������ڷ�ֹ�����������
    public Boolean Deviceloaded;      //Э�����������豸�����Ƿ��Ѿ����أ������ڷ�ֹ�����������
    public Boolean visible;           //����ʱ�Ƿ���ʾ����

    public Boolean winvisible;        //���򴰿�Ŀǰ�Ƿ���ʾ

    public int PID;                //���ܼҾӽ��̺ţ����������������
    public int DevicePID;          //�豸����Э�����������豸���򣩽��̺�

    public int DeviceId;           //�豸��
    public int Port;               //TCP/IPͨ�Ŷ˿ں�
    public Boolean bServer;           //����˱�־
    public int CommMode;           //ͨ�ŷ�ʽ

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 32)]
    public String password;       //ͨ�����룬����ָ������������

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 32)]
    public String user;           //TCPͨ�ŵ�¼SHM���û�����������

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 16)]
    public String serverIP;       //TCPͨ�ŷ����IP��ַ

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 64)]
    public String name;            //ϵͳ���ƣ�32Unicode�ַ�

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 64)]
    public String starttime;       //ϵͳ����ʱ��

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 64)]
    public String assembly;        //ʵ����ISmartHome�ӿڵĳ����ļ���
    public String description;            //ϵͳ���ƣ�32Unicode�ַ�
    
    public StringJson ToStringJson(int flag)
    {
    	StringJson json = new StringJson(flag);
        json.AddNameVolume("�豸ϵͳ", name);
        json.AddNameVolume("�Ҿ�PID", Integer.toString(appid));
        json.AddNameVolume("����", assembly);
        json.AddNameVolume("����ʱ��", starttime);
        json.AddNameVolume("SHM PID",  Integer.toString(PID));
        json.AddNameVolume("Device PID",  Integer.toString(DevicePID));
        json.AddNameVolume("ͨ�ŷ�ʽ", Integer.toString(CommMode));
        json.AddNameVolume("�����", Boolean.toString(bServer));
        json.AddNameVolume("�����IP", serverIP);
        json.AddNameVolume("�˿�",  Integer.toString(Port));
        json.AddNameVolume("����", password);
        json.AddNameVolume("�û���", user);
        json.AddNameVolume("SHM ���ط�", Boolean.toString(loaded==null?false:loaded));
        json.AddNameVolume("Device���ط�", Boolean.toString(Deviceloaded==null?false:Deviceloaded));
        return json;
    }
}
