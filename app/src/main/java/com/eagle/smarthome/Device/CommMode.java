package com.eagle.smarthome.Device;

public enum CommMode   //���ܼҾ��豸ϵͳ���س����ͨ�����Ͷ���
{
     RS232(0), SHAREMEMORY (1), TCP (2), ZIGBEE(3), BLUETOOTH(4), UNKNOW(5);   //Ŀǰֻ��ǰ����
    
    //  ����˽�б��� 
	private   int   nCode ; 
	//  ���캯����ö������ֻ��Ϊ˽�� 
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
