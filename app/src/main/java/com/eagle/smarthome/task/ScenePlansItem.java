package com.eagle.smarthome.task;

public class ScenePlansItem  //һ������
{
    public Boolean Used;    //��Ч�����񣬲�����飬��Ҫʱ�������ã�ֻ�Զ�ʱ������Ч
    public String PlanFileName;  //һ���������ݱ�����ļ�
    public String BlackList;  //��������
 
    public String WhiteList; //������
    public String reserve1; //����
    public String reserve2; //����
    public String reserve3;  //����
    
    


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
