package com.eagle.smarthome.util;

import com.eagle.smarthome.StringJson;

public class SmartHomeChannel
{
    public static int SHFLAG = 0x5A5A5A5A;  //智能家居系统统一标识，便于各个厂家通信公用
    public static String sharememoryfile = "SmartHomeShareFile";
    public static String mutexname = "SmartHomeMutex";

    public int appid;              //某品牌智能家居唯一识别号

    public Boolean canused;           //系统能否被使用
    public Boolean autostart;         //自动运行  

    public Boolean loaded;            //监控程序SHM是否已经加载，可用于防止启动多个进程
    public Boolean Deviceloaded;      //协调器或虚拟设备程序是否已经加载，可用于防止启动多个进程
    public Boolean visible;           //启动时是否显示窗口

    public Boolean winvisible;        //程序窗口目前是否显示

    public int PID;                //智能家居进程号，可以启动多个进程
    public int DevicePID;          //设备程序（协调器，虚拟设备程序）进程号

    public int DeviceId;           //设备号
    public int Port;               //TCP/IP通信端口号
    public Boolean bServer;           //服务端标志
    public int CommMode;           //通信方式

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 32)]
    public String password;       //通行密码，所有指令必须带有密码

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 32)]
    public String user;           //TCP通信登录SHM的用户名带有密码

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 16)]
    public String serverIP;       //TCP通信服务端IP地址

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 64)]
    public String name;            //系统名称：32Unicode字符

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 64)]
    public String starttime;       //系统启动时间

    //[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 64)]
    public String assembly;        //实现了ISmartHome接口的程序集文件名
    public String description;            //系统名称：32Unicode字符
    
    public StringJson ToStringJson(int flag)
    {
    	StringJson json = new StringJson(flag);
        json.AddNameVolume("设备系统", name);
        json.AddNameVolume("家居PID", Integer.toString(appid));
        json.AddNameVolume("程序集", assembly);
        json.AddNameVolume("启动时间", starttime);
        json.AddNameVolume("SHM PID",  Integer.toString(PID));
        json.AddNameVolume("Device PID",  Integer.toString(DevicePID));
        json.AddNameVolume("通信方式", Integer.toString(CommMode));
        json.AddNameVolume("服务端", Boolean.toString(bServer));
        json.AddNameVolume("服务端IP", serverIP);
        json.AddNameVolume("端口",  Integer.toString(Port));
        json.AddNameVolume("密码", password);
        json.AddNameVolume("用户名", user);
        json.AddNameVolume("SHM 加载否", Boolean.toString(loaded==null?false:loaded));
        json.AddNameVolume("Device加载否", Boolean.toString(Deviceloaded==null?false:Deviceloaded));
        return json;
    }
}
