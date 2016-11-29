package com.eagle.smarthome.util;

import java.util.List;
import com.eagle.smarthome.R;
import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeviceSystemAdapter extends ArrayAdapter<SmartHomeChannel>
{	
	public DeviceSystemAdapter(Context context, int textViewResourceId, List<SmartHomeChannel> objects)
	{
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
		selectItem=-1;
	}
	private int resourceId;
    private int  selectItem=-1;
    public  void setSelectItem(int selectItem)
    {  
    	this.selectItem = selectItem;  
     }  

 @Override
 public View getView(int position,View convertView,ViewGroup parent)
 {
	 SmartHomeChannel chl=getItem(position);
	 View view;
	 ViewHolder viewHolder;
	 if (convertView==null)
		 { 
		 	view=LayoutInflater.from(getContext()).inflate(resourceId, null ); 
		 	viewHolder=new ViewHolder();
		 	viewHolder.dsname= (TextView)view.findViewById(R.id.dsname);
		 	viewHolder.dsappid= (TextView)view.findViewById(R.id.dsappid);
		 	viewHolder.dsdriver= (TextView)view.findViewById(R.id.dsdriver);
		 	view.setTag(viewHolder);
		 }
	 else
	 {
		  view=convertView; 	 
		  viewHolder=(ViewHolder)view.getTag();
	 }

	 viewHolder.dsname.setText(chl.description);
	 viewHolder.dsappid.setText(String.valueOf(chl.appid));
	 viewHolder.dsdriver.setText(chl.name);
	 if (view!=null)
	 {
		 if (position== selectItem)
			 view.setBackgroundColor(Color.rgb(70,92,79));
//		 else	 if (position %2 ==1)
//			 view.setBackgroundColor(Color.argb(128,4, 40, 4));
		 else 
			 view.setBackgroundColor(Color.TRANSPARENT);  			 
	 } 
	 return view;
 }
 
 class ViewHolder
 {
	    TextView dsname;
	    TextView dsappid;
	    TextView dsdriver;
 }
 
}
