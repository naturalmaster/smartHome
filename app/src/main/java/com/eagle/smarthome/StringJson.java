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
	int flag = 0x5A5A5A5A;  //        public static int SHFLAG = 0x5A5A5A5A;  //智能家居系统统一标识，便于各个厂家通信公用
	
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
		
		//将字符数组转换为字节数组
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
	
	public byte[] GetBytes()   //序列化该对象
	{
		try
		{
			ByteArrayOutputStream Stream = new ByteArrayOutputStream();
			//OutputStreamWriter out=new OutputStreamWriter(Stream, Charset.forName("UTF-8"));		
			WriteWInt(Stream,flag);      //1、写入标志位
			WriteWInt(Stream,(int)0);   //2、 先写入一个占位符，表示整个字节流的长度 4字节
	        
			int len = mDictionary.size();  //3、写入字典词条数量
			WriteWInt(Stream,len);   
			
			Iterator<String> iter = mDictionary.keySet().iterator();
			while(iter.hasNext())  //4、循环写入每个词条
			{
				String name = iter.next();		
				byte[] buffer = name.getBytes(Charset.forName("UTF-8"));
				len = buffer.length;
				WriteWInt(Stream, len); 		//先写词条名字长度
				Stream.write(buffer,0, len); 	//再写词条名字
				
				buffer = mDictionary.get(name); 
				len = buffer.length;
				WriteWInt(Stream, len);			//先写词条内容长度
				Stream.write(buffer,0, len); 	//再写词条内容
			}	  
			
			byte[] result = Stream.toByteArray();
			len=result.length;
			result[4]=(byte) ((len>>24)&0xFF);  //修改占位符数据
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
	public static StringJson BytesToStringJson(byte[] buffer,int flag)  //从字节序列反序列化对象
	{
		if( 8 > buffer.length) return null;  //字节数不合法
		try
		{
			ByteArrayInputStream Stream = new ByteArrayInputStream(buffer);
			//1 读取标志
			int len = ReadInt(Stream);
			if(flag != len) return null;  //不是本公司的标志位
			
			//2 读取数据总长
	    	len = ReadInt(Stream);
			if(buffer.length != len) return null;  //数据长度不对

			//3 读取词条数目
			int size = ReadInt(Stream);
			if(size > 100) return null; //不能超过100个词条
			
			int count = 0;
			StringJson json = new StringJson(flag);		
			while (count < size ) //4、读取所有词条
			{
				count++;
				len = ReadInt(Stream); //词条名字长度			
				byte[] destBuffer = new byte[len];
				Stream.read(destBuffer);
				String Name = new String(destBuffer,Charset.forName("UTF-8"));  //词条名字			
				len = ReadInt(Stream); 			//词条内容长度
				destBuffer = new byte[len];  //词条内容
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
		while(iter.hasNext())  //循环写入每个词条
		{
			String name = iter.next();			
			s+=name+"=";			
			String tmp=new String(mDictionary.get(name));
			s+=tmp+"\r\n";
		}
		return s;
	}

}

