package com.eagle.smarthome.Device;

public enum CommMode   //智能家居设备系统与监控程序的通信类型定义
{
     RS232(0), SHAREMEMORY (1), TCP (2), ZIGBEE(3), BLUETOOTH(4), UNKNOW(5);   //目前只用前三种
    
    //  定义私有变量 
	private   int   nCode ; 
	//  构造函数，枚举类型只能为私有 
    private   CommMode( int   _nCode)
    {
        this.nCode  = _nCode; 
   } 
    @Override 
    public   String toString() 
    {
        return   String.valueOf ( this. nCode ); 
   } 


}
