package com.eagle.smarthome.Device;

public enum StreamType     //�ֽ���������,�����豸SO��SI��Ҫ����
{
    Unknow(0), TEXT(1), IMAGE(2), AUDIO(3), FILE(4), STRINGJSON(5);
	//  ����˽�б��� 
	private   int   nCode ; 
	//  ���캯����ö������ֻ��Ϊ˽�� 
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

