package com.eagle.smarthome.task;

import java.util.Vector;

public class TimedTaskItem 
{
	 public String TaskName;   //任务名字
	 public Vector<TimeRange> RunTime;           //定时任务开始执行的时间，结束时间        
     public TimedTaskItem()
     {
         RunTime = new Vector<TimeRange>();
     }
}
