package com.eagle.smarthome.task;

import java.util.Vector;

public class TimedTaskItem 
{
	 public String TaskName;   //��������
	 public Vector<TimeRange> RunTime;           //��ʱ����ʼִ�е�ʱ�䣬����ʱ��        
     public TimedTaskItem()
     {
         RunTime = new Vector<TimeRange>();
     }
}
