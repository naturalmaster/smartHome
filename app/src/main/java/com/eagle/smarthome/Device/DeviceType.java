package com.eagle.smarthome.Device;

public enum DeviceType  //���ܼҾ��豸���Ͷ���:ֻ��6���豸
{
	Unknow(0), DO(1), AO(2), DI(3), AI (4), SO(5), SI(6);   //��ֵ����������豸��ģ������������豸
	//  ����˽�б��� 
	private   int   nCode ; 
	//  ���캯����ö������ֻ��Ϊ˽�� 
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
