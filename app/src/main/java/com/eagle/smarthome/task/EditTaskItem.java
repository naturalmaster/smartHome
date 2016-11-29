package com.eagle.smarthome.task;

public class EditTaskItem 
{
	static TaskItem taskitem=null;  //µ¥ÀıÄ£Ê½
	public static TaskItem GetTaskItem()
	{
		if (taskitem==null)	 
			taskitem=new TaskItem();
		return taskitem;
	}
	
}
