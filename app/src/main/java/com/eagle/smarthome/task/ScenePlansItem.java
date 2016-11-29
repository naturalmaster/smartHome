package com.eagle.smarthome.task;

public class ScenePlansItem  //一个场景
{
    public Boolean Used;    //无效的任务，不做检查，需要时，再启用：只对定时任务有效
    public String PlanFileName;  //一个场景内容保存的文件
    public String BlackList;  //禁用名单
 
    public String WhiteList; //白名单
    public String reserve1; //保留
    public String reserve2; //保留
    public String reserve3;  //保留
    
    


    /*public int Hour;
    public int Minute;
    public int Second;*/
    public ScenePlansItem()
    {
        Used = false;
        PlanFileName = "";
        BlackList="";
        WhiteList = "";
        reserve1 = "";
        reserve2 = "";
        reserve3 = "";
    }

}
