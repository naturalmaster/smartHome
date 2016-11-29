package com.eagle.smarthome.util;

import java.util.*;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.io.Serializable;

///���еǼǵļ�ص��б�
public class HomeLocations
{
	public List<HomeLocation> Items;
	String FileName;
	Context context;
	public HomeLocations(Context context,String filename)
	{
		Items=new Vector<HomeLocation>();
		FileName=filename;
		this.context=context;
		LoadFromFile();
	}
	public void LoadFromFile()  //���ļ���������
	{
		try
		{
			FileInputStream in=context.openFileInput(FileName);
			ObjectInputStream objectIn=new ObjectInputStream(in);
			int count=objectIn.readInt();
			Items.clear();
			for (int i=0;i<count;i++)
			{
				HomeLocation loc= (HomeLocation)objectIn.readObject();
				if (loc!=null) Items.add(loc);
			}
			objectIn.close();
			//in.close();	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}

	public void SaveToFile()  //����д���ļ�
	{
		FileOutputStream out=null;
		try
		{
			out=context.openFileOutput(FileName, Context.MODE_PRIVATE);
			ObjectOutputStream objectOut=new ObjectOutputStream(out);
			objectOut.writeInt(Items.size()); //��д���¼����
			for( HomeLocation loc:Items)
				objectOut.writeObject(loc);
			objectOut.close();
			//out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
	
	public HomeLocation FindHome(String homename)
	{
		for( HomeLocation loc:Items)
		{			
			if (loc.HomeName.equalsIgnoreCase(homename))
					return loc;
		}
		return null;
	}
}
