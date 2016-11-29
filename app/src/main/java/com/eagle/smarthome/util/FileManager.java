package com.eagle.smarthome.util;

import java.io.*;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;

public class FileManager
{   
   public static List<String> ReadText(Context context, String filename)
   {
	   BufferedReader reader=null;
	   Vector<String> ss=new Vector<String>();
	   try
	   {
		   FileInputStream in=context.openFileInput(filename);
		   reader = new BufferedReader( new InputStreamReader(in));
           String line="";
           while(true)
           {
        	   line=reader.readLine();
        	   if (line==null) break;
        	   ss.add(line);
           }
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
			return null;
	   }
		finally
		{
			if (reader!=null)
			{
				try
				{
					reader.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}				
		}
	   return ss;
   }
   
   public static boolean WriteStrings(Context context, String filename, List<String> strings)
   {
	   BufferedWriter writer=null;
		try
		{
			FileOutputStream out=context.openFileOutput(filename, Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));		
			int size=strings.size();
			for (int i=0;i<size;i++)
			{
				writer.write(strings.get(i));
				if (i<size-1)
					writer.newLine();
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			if (writer!=null)
			{
				try
				{
					writer.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}				
		}
		return true;
   }

   public static boolean WriteConfigString(Context context, String filename, String key,String value)
   {
	   SharedPreferences.Editor editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit() ;
	   editor.putString(key, value);
	   return editor.commit();
   }
   
   public static boolean WriteConfigInt(Context context, String filename, String key,int value)
   {
	   SharedPreferences.Editor editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit() ;
	   editor.putInt(key, value);
	   return editor.commit();
   }
   
   public static boolean WriteConfigBoolean(Context context, String filename, String key,boolean value)
   {
	   SharedPreferences.Editor editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit() ;
	   editor.putBoolean(key, value);
	   return editor.commit();
   }
   
   public static boolean WriteConfigFloat(Context context, String filename, String key,float value)
   {
	   SharedPreferences.Editor editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit() ;
	   editor.putFloat(key, value);
	   return editor.commit();
   }
      
   public static String GetConfigString(Context context, String filename, String key,String defaultvalue)
   {
	   SharedPreferences editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE);
	   return editor.getString(key, defaultvalue);
   }
   
   public static int GetConfigInt(Context context, String filename, String key,int defaultvalue)
   {
	   SharedPreferences editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE);
	   return editor.getInt(key, defaultvalue);
   }
   
   public static boolean GetConfigBoolean(Context context, String filename, String key,boolean defaultvalue)
   {
	   SharedPreferences editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE);
	   return editor.getBoolean(key, defaultvalue);
   }
   
   public static float GetConfigFloat(Context context, String filename, String key,float defaultvalue)
   {
	   SharedPreferences editor=context.getSharedPreferences(filename, Context.MODE_PRIVATE);
	   return editor.getFloat(key, defaultvalue);
   }
   
}
