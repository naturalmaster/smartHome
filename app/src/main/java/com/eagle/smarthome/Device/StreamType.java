package com.eagle.smarthome.Device;

public enum StreamType     //字节流的类型,对流设备SO，SI需要定义
{
    Unknow(0), TEXT(1), IMAGE(2), AUDIO(3), FILE(4), STRINGJSON(5);
	//  定义私有变量 
	private   int   nCode ; 
	//  构造函数，枚举类型只能为私有 
    private   StreamType( int  _nCode)
    {
        this.nCode  = _nCode; 
   } 
  /*  @Override 
    public   String toString() 
    {
        return   String.valueOf ( this. nCode ); 
   } */
}

