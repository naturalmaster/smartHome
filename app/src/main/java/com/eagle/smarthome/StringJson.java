package com.eagle.smarthome;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import java.util.*;
//import java.util.Iterator;

public class StringJson  //wuzihui 2015-09-23
{
	public Hashtable<String, byte[]> mDictionary = null;
	int flag = 0x5A5A5A5A;  //        public static int SHFLAG = 0x5A5A5A5A;  //���ܼҾ�ϵͳͳһ��ʶ�����ڸ�������ͨ�Ź���
	
	public StringJson(int _flag)
	{
		mDictionary = new Hashtable<String, byte[]>();
		flag = _flag;
	}
	
	public void Clear()
	{
		mDictionary.clear();
	}
	
	public void AddNameVolume(String name,String value) 
	{
		byte[] bytevalue = null;
		try
		{
			bytevalue = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDictionary.put(name, bytevalue);
		
	}
	
	public void AddNameVolume(String name,char[] value)
	{
		if(value.length == 0) return;
		
		//���ַ�����ת��Ϊ�ֽ�����
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(value.length);
		cb.put(value);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		byte[] bytevalue = bb.array();
		
		mDictionary.put(name, bytevalue);		
	}
	
	public void AddNameVolume(String name,InputStream value)
	{
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		
		byte[] buff = new byte[1000];
		int rc = 0;
		
		try
		{
			while((rc = value.read(buff, 0, 1000))>0)
			{
				swapStream.write(buff, 0, rc);
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] bytevalue = swapStream.toByteArray();
		
		mDictionary.put(name, bytevalue);		
	}
	
	public void AddNameVolume(String name,byte[] value)
	{
		mDictionary.put(name, value);
	}
	
	public void DeleteName(String Name)
	{
		if(mDictionary.containsKey(Name))
		{
			mDictionary.remove(Name);
		}		
	}
	
	public byte[] GetValueByName(String Name)
	{
		if(mDictionary.containsKey(Name))
		{
			return mDictionary.get(Name);
		}
		else {
			return null;
		}
	}
	
	public String GetStrValueByName(String Name)
	{
		 
		if(mDictionary.containsKey(Name))
		{
			return new String(mDictionary.get(Name));
		}
		else {
			return null;
		}
	}
	
	public void WriteWInt(OutputStream stream,int i)
	{
		try
		{
			stream.write((byte) ((i>>24)&0xFF));
			stream.write((byte) ((i>>16)&0xFF));
			stream.write((byte) ((i>>8)&0xFF));
			stream.write((byte) (i&0xFF));
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] GetBytes()   //���л��ö���
	{
		try
		{
			ByteArrayOutputStream Stream = new ByteArrayOutputStream();
			//OutputStreamWriter out=new OutputStreamWriter(Stream, Charset.forName("UTF-8"));		
			WriteWInt(Stream,flag);      //1��д���־λ
			WriteWInt(Stream,(int)0);   //2�� ��д��һ��ռλ������ʾ�����ֽ����ĳ��� 4�ֽ�
	        
			int len = mDictionary.size();  //3��д���ֵ��������
			WriteWInt(Stream,len);   
			
			Iterator<String> iter = mDictionary.keySet().iterator();
			while(iter.hasNext())  //4��ѭ��д��ÿ������
			{
				String name = iter.next();		
				byte[] buffer = name.getBytes(Charset.forName("UTF-8"));
				len = buffer.length;
				WriteWInt(Stream, len); 		//��д�������ֳ���
				Stream.write(buffer,0, len); 	//��д��������
				
				buffer = mDictionary.get(name); 
				len = buffer.length;
				WriteWInt(Stream, len);			//��д�������ݳ���
				Stream.write(buffer,0, len); 	//��д��������
			}	  
			
			byte[] result = Stream.toByteArray();
			len=result.length;
			result[4]=(byte) ((len>>24)&0xFF);  //�޸�ռλ������
			result[5]=(byte) ((len>>16)&0xFF);
			result[6]=(byte) ((len>>8)&0xFF);
			result[7]=(byte) (len&0xFF);
			return result;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	static int ReadInt(ByteArrayInputStream Stream)
	{
		int b1,b2,b3,b4;
		b1 = Stream.read();
		if(b1 < 0) b1 = b1 + 256;
		b2 =Stream.read();
		if(b2 < 0) b2 = b2 + 256;
		b3 = Stream.read();
		if(b3 < 0) b3 = b3 + 256;
		b4 = Stream.read();
		if(b4 < 0) b4 = b4 + 256;		
		return  (b1<<24) + (b2<< 16) + (b3<<8) + b4;
	}
	public static StringJson BytesToStringJson(byte[] buffer,int flag)  //���ֽ����з����л�����
	{
		if( 8 > buffer.length) return null;  //�ֽ������Ϸ�
		try
		{
			ByteArrayInputStream Stream = new ByteArrayInputStream(buffer);
			//1 ��ȡ��־
			int len = ReadInt(Stream);
			if(flag != len) return null;  //���Ǳ���˾�ı�־λ
			
			//2 ��ȡ�����ܳ�
	    	len = ReadInt(Stream);
			if(buffer.length != len) return null;  //���ݳ��Ȳ���

			//3 ��ȡ������Ŀ
			int size = ReadInt(Stream);
			if(size > 100) return null; //���ܳ���100������
			
			int count = 0;
			StringJson json = new StringJson(flag);		
			while (count < size ) //4����ȡ���д���
			{
				count++;
				len = ReadInt(Stream); //�������ֳ���			
				byte[] destBuffer = new byte[len];
				Stream.read(destBuffer);
				String Name = new String(destBuffer,Charset.forName("UTF-8"));  //��������			
				len = ReadInt(Stream); 			//�������ݳ���
				destBuffer = new byte[len];  //��������
				Stream.read(destBuffer);			
				json.AddNameVolume(Name, destBuffer);			
			}		
			return json;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		String s="";
		Iterator<String> iter = mDictionary.keySet().iterator();
		while(iter.hasNext())  //ѭ��д��ÿ������
		{
			String name = iter.next();			
			s+=name+"=";			
			String tmp=new String(mDictionary.get(name));
			s+=tmp+"\r\n";
		}
		return s;
	}

}

