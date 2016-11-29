package com.eagle.smarthome.Device;

public enum DeviceType  //智能家居设备类型定义:只有6种设备
{
	Unknow(0), DO(1), AO(2), DI(3), AI (4), SO(5), SI(6);   //数值量输入输出设备，模拟量输入输出设备
	//  定义私有变量 
	private   int   nCode ; 
	//  构造函数，枚举类型只能为私有 
    private   DeviceType( int   _nCode)
    {
        this.nCode  = _nCode; 
   } 
 /*  @Override 
    public   String toString() 
    {
        return   String.valueOf ( this. nCode ); 
   } */

}
