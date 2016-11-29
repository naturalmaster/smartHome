package com.eagle.smarthome.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class StreamReadWrite  //为兼容各类语言，特编写底层读写方法
{
	public static void WriteInt(OutputStream writer,int i)
	{
		try
		{
			writer.write((byte) ((i>>24)&0xFF));
			writer.write((byte) ((i>>16)&0xFF));
			writer.write((byte) ((i>>8)&0xFF));
			writer.write((byte) (i&0xFF));    			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void WriteByte(OutputStream writer,byte b)
	{
		try
		{
			writer.write(b);    			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void WriteString(OutputStream writer,String str)
	{
		try
		{
			byte[] buffer = str.getBytes(Charset.forName("UTF-8"));
			int len = buffer.length;
			WriteInt(writer, len); 		    //先写字符串长度
			writer.write(buffer,0, len); 	//再写字符串内容    			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public static void WriteBoolean(OutputStream writer,Boolean bool)
	{
		try
		{
			writer.write(bool? (byte)1: (byte)0); 
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static int ReadInt(InputStream reader)
	{
		try
		{
			int b1,b2,b3,b4;
			b1 = reader.read();
			if(b1 < 0) b1 = b1 + 256;
			b2 =reader.read();
			if(b2 < 0) b2 = b2 + 256;
			b3 = reader.read();
			if(b3 < 0) b3 = b3 + 256;
			b4 = reader.read();
			if(b4 < 0) b4 = b4 + 256;		
			return  (b1<<24) + (b2<< 16) + (b3<<8) + b4;			
		} catch (IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	public static String ReadString(InputStream reader)
	{
		try
		{
			int len=ReadInt(reader);  //长度
			byte[] destBuffer = new byte[len];  //内容
			reader.read(destBuffer);		
			return new String(destBuffer,Charset.forName("UTF-8"));  //字符串			
		} catch (IOException e)
		{
			e.printStackTrace();
			return "";
		}
	}
	public static Boolean ReadBoolean(InputStream reader)
	{
		try
		{
			int b=reader.read();
			return b==1? true:false; 
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public static byte ReadByte(InputStream reader)
	{
		try
		{
			int b=reader.read();
			return (byte)b; 
		} catch (IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

}
