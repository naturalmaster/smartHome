package com.eagle.smarthome.util;

import java.util.List;
import com.eagle.smarthome.R;
import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<String>
{	
	public MessageAdapter(Context context, int textViewResourceId, List<String> objects)
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
	 String chl=getItem(position);
	 View view;
	 ViewHolder viewHolder;
	 if (convertView==null)
		 { 
		 	view=LayoutInflater.from(getContext()).inflate(resourceId, null ); 
		 	viewHolder=new ViewHolder();
		 	viewHolder.messagetext= (TextView)view.findViewById(R.id.messagetext);
		 	view.setTag(viewHolder);
		 }
	 else
	 {
		  view=convertView; 	 
		  viewHolder=(ViewHolder)view.getTag();
	 }

	 viewHolder.messagetext.setText(chl);

	 if (view!=null)
	 {
		 if (position== selectItem)
			 view.setBackgroundColor(Color.rgb(15,34,91));
//		 else	 if (position %2 ==1)
//			 view.setBackgroundColor(Color.rgb(72, 40, 40));
		 else
			 view.setBackgroundColor(Color.TRANSPARENT);
//			 view.setBackgroundColor(Color.rgb(72, 40, 40));

	 } 
	 return view;
 }
 
 class ViewHolder
 {
	    TextView messagetext;
 }
 
}
