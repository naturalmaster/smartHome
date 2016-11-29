package com.eagle.smarthome.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.eagle.smarthome.MyApplication;
import com.eagle.smarthome.R;
import com.eagle.smarthome.Device.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends ArrayAdapter<Object>
{	
	    Context context;
	    String workdir;
		public DeviceAdapter(Context context, String workdir, int textViewResourceId, Vector<Object> homedevices)
		{
			super(context, textViewResourceId, homedevices);
			this.devices=homedevices;
			resourceId=textViewResourceId;
			this.context=context;
			this.workdir=workdir;
			selectItem=-1;
			ON=context.getResources().getString(R.string.on);
			OFF=context.getResources().getString(R.string.off);		
			state=context.getResources().getString(R.string.state);			
			control=context.getResources().getString(R.string.control);
			function=context.getResources().getString(R.string.function);
			signal=context.getResources().getString(R.string.signal);			
			nosignal=context.getResources().getString(R.string.nosignal);		
			unknown=context.getResources().getString(R.string.unknown);		
	    	LoadAllPicture();		//
		}
		
	    public static String getExtensionName(String filename) {    
	        if ((filename != null) && (filename.length() > 0)) {    
	            int dot = filename.lastIndexOf('.');    
	            if ((dot >-1) && (dot < (filename.length() - 1))) {    
	                return filename.substring(dot + 1);    
	            }    
	        }    
	        return filename;    
	    } 		
		public static Bitmap getLoacalBitmap(String url)
		{
		     try
		     {
		          FileInputStream fis = new FileInputStream(url);
		          return BitmapFactory.decodeStream(fis);
		     } catch (FileNotFoundException e)
		     {
		          e.printStackTrace();
		          return null;
		     }
		}
		private Hashtable<String, Bitmap> mPictures = null;
		private void LoadAllPicture()  //装载所有监控点的图片
		{			
			if(mPictures!=null) mPictures.clear();
			mPictures=new Hashtable<String, Bitmap>();
			String filename=context.getFilesDir()+File.separator+workdir;
			File file = new File(filename); //String path = getFilesDir().getAbsolutePath() ;
			String[] files=file.list(); 			 //String s="";	 String lastfile="";
			for (String f:files)
			{
				String ext=getExtensionName(f).toUpperCase();
				if (ext.equals("JPG") || ext.equals("PNG"))
				{	//s+=f+", "; lastfile=f;
					mPictures.put(f, getLoacalBitmap(filename+File.separator+f));					
				}
			}	
		}		
		
		List<Object> devices;
		private int resourceId;
	    private int  selectItem=-1;
	    private String ON;
	    private String OFF;
	    private String state;
	    private String control;
	    private String function;
	    private String signal;
	    private String nosignal;	    
	    private String unknown;
	    
	    public  void setSelectItem(int selectItem)
	    {  
	    	this.selectItem = selectItem;  
	    }

	 @Override
	 public View getView(int position,View convertView,ViewGroup parent)
	 {
		 View view;
		 ViewHolder viewHolder;
		 if (convertView==null)
			 { 
			 	view=LayoutInflater.from(getContext()).inflate(resourceId, null ); 
			 	viewHolder=new ViewHolder();
			 	viewHolder.imagedevice=(ImageView)view.findViewById(R.id.imagedevice);
			 	viewHolder.dvdescription= (TextView)view.findViewById(R.id.dvdescription);
			 	viewHolder.dvidtype= (TextView)view.findViewById(R.id.dvidtype);
			 	viewHolder.dvvalume= (TextView)view.findViewById(R.id.dvvalume);
			 	view.setTag(viewHolder);
			 }
		 else
		 {
			  view=convertView; 	 
			  viewHolder=(ViewHolder)view.getTag();
		 }
		 if (view!=null)
		 {
			 if (position== selectItem)
				 view.setBackgroundColor(Color.argb(128,4, 4, 255));  
			 else	 if (position %2 ==1)			   
				 view.setBackgroundColor(Color.argb(128,4, 40, 4));  
			 else 
				 view.setBackgroundColor(Color.TRANSPARENT);  			 
		 }
		 ShowDevice(viewHolder,devices.get(position));
		 return view;
	 }
	 
	protected void resetViewHolder(ViewHolder viewHolder)
	{
		viewHolder.dvidtype.setText(null);
		viewHolder.dvdescription.setText(null);
		viewHolder.dvvalume.setText(null);
	}
	
	 private void ShowDevice(ViewHolder viewHolder,Object device)
	 {
		 try
		 {
		  if (device instanceof DeviceDO)
		  {
			  DeviceDO dv=(DeviceDO) device;
			  String s=function+dv.functiondescription+" ("+dv.devicetype.toString()+"-"+dv.parentid+"-"+dv.id+")";
			  viewHolder.dvdescription.setText(s);  //功能设备说明
			  viewHolder.dvidtype.setText(control+dv.controldescription); //控制方法
			  s=dv.ON() ? ON : OFF;
		      viewHolder.dvvalume.setText(state+s);  //当前状态
		      if (dv.ON())  //显示开启图片
		      {
		    	  Bitmap bitmap=mPictures.get(dv.pictureon);
		    	  if (bitmap==null) bitmap=mPictures.get("DOON.jpg");
		    	  viewHolder.imagedevice.setImageBitmap(bitmap);
		      }
		      else
		      {
		    	  Bitmap bitmap=mPictures.get(dv.pictureoff);
		    	  if (bitmap==null) bitmap=mPictures.get("DOOFF.jpg");
		    	  viewHolder.imagedevice.setImageBitmap(bitmap);
		      }
		      return;
		  }
		  else  if (device instanceof DeviceDI)
		  {
			  DeviceDI dv=(DeviceDI) device;
			  String s=function+dv.functiondescription+" ("+dv.devicetype.toString()+"-"+dv.parentid+"-"+dv.id+")";
			  viewHolder.dvdescription.setText(s);
			  viewHolder.dvidtype.setText(control+dv.controldescription); //控制方法
			  s=dv.hassignal ? signal : nosignal;
		      viewHolder.dvvalume.setText(state+s);  
		      if (dv.hassignal)  //显示开启图片
		      {
		    	  Bitmap bitmap=mPictures.get(dv.pictureon);
		    	  if (bitmap==null) bitmap=mPictures.get("DION.jpg");
		    	  viewHolder.imagedevice.setImageBitmap(bitmap);
		      }
		      else
		      {
		    	  Bitmap bitmap=mPictures.get(dv.pictureoff);
		    	  if (bitmap==null) bitmap=mPictures.get("DIOFF.jpg");
		    	  viewHolder.imagedevice.setImageBitmap(bitmap);
		      }
		      return;
		  }
		  else  if (device instanceof DeviceAO)
		  {
			  DeviceAO dv=(DeviceAO) device;
			  String s=function+dv.functiondescription+" ("+dv.devicetype.toString()+"-"+dv.parentid+"-"+dv.id+")";
			  viewHolder.dvdescription.setText(s);  //1、功能描述
			  viewHolder.dvidtype.setText(control+dv.controldescription); //2、控制方法
			  s="%."+dv.dotplace+"f";
			  s=String.format(s, dv.aovalue);
		      viewHolder.dvvalume.setText(state+s);  //3、上次数据
		      Bitmap bitmap=mPictures.get(dv.picture);
	    	  if (bitmap==null) bitmap=mPictures.get("AO.jpg");
	    	  viewHolder.imagedevice.setImageBitmap(bitmap);	    	  
		      return;
		  }
		  else  if (device instanceof DeviceAI)
		  {
			  DeviceAI dv=(DeviceAI) device;
			  String s=function+dv.functiondescription+" ("+dv.devicetype.toString()+"-"+dv.parentid+"-"+dv.id+")";
			  viewHolder.dvdescription.setText(s);  //1、功能描述
			  viewHolder.dvidtype.setText(control+dv.controldescription); //2、控制方法
			  s="%."+dv.dotplace+"f";
			  s=String.format(s, dv.aivalue);
		      viewHolder.dvvalume.setText(state+s);  //3、上次数据
		      Bitmap bitmap=mPictures.get(dv.picture);
	    	  if (bitmap==null) bitmap=mPictures.get("AI.jpg");
	    	  viewHolder.imagedevice.setImageBitmap(bitmap);	    
		      return;
		  }
		  else  if (device instanceof DeviceSO)
		  {
			  DeviceSO dv=(DeviceSO) device;
			  String s=function+dv.functiondescription+" ("+dv.devicetype.toString()+"-"+dv.parentid+"-"+dv.id+")";
			  viewHolder.dvdescription.setText(s);  //1、功能描述
			  viewHolder.dvidtype.setText(control+dv.controldescription); //2、控制方法
			  if (dv.streamtype==StreamType.TEXT)  //文本型流
			  {
				  if (dv.sovalue!=null)
					  s=new String(dv.sovalue,Charset.forName("UTF-8"));
				  else 
					  s=unknown;					 
			  }
			  else 
			 {
				  s=dv.streamtype.toString();  //显示媒体类型即可
				  if (dv.sovalue!=null)
					  s+="("+dv.sovalue.length+")";
			  }
		      viewHolder.dvvalume.setText(state+s);  //3、上次数据
		      Bitmap bitmap=mPictures.get(dv.picture);
	    	  if (bitmap==null) bitmap=mPictures.get("SO.jpg");
	    	  viewHolder.imagedevice.setImageBitmap(bitmap);	    
		      return;
		  }
		  else  if (device instanceof DeviceSI)
		  {
			  DeviceSI dv=(DeviceSI) device;
			  String s=function+dv.functiondescription+" ("+dv.devicetype.toString()+"-"+dv.parentid+"-"+dv.id+")";
			  viewHolder.dvdescription.setText(s);  //1、功能描述
			  viewHolder.dvidtype.setText(control+dv.controldescription); //2、控制方法
			  if (dv.streamtype==StreamType.TEXT)  //文本型流
			  {
				  if (dv.sivalue!=null)
					  s=new String(dv.sivalue,Charset.forName("UTF-8"));
				  else 
					  s=unknown;		
			  }  
			  else 
				  {
				  s=dv.streamtype.toString();  //显示媒体类型即可
				  if (dv.sivalue!=null)
					  s+="("+dv.sivalue.length+")";
				  }
			  
		      viewHolder.dvvalume.setText(state+s);  //3、上次数据
		      Bitmap bitmap=mPictures.get(dv.picture);
	    	  if (bitmap==null) bitmap=mPictures.get("SI.jpg");
	    	  viewHolder.imagedevice.setImageBitmap(bitmap);	    
		      return;
		  }
		 }
		 catch (Exception ex)
		 {
			 ex.printStackTrace();
		 }
	 }
	 class ViewHolder
	 {
		    //Object device;
		    ImageView imagedevice;
		    TextView dvdescription;
		    TextView dvidtype;
		    TextView dvvalume;
	 }
	 
	}
