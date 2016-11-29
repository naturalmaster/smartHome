package com.eagle.smarthome.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class TimedTasks
{
	public int Flag; // 唯一识别码
	public String FileName; // 保存所有定时场景的文件，知道它可以了解所有定时场景的名字 .ttsk扩展名
	private String workdir;
	private Context context;

	public Vector<TimedTaskItem> Items; // 定时列表
	// public TimedTasks(int _Flag, String _FileName)

	public TimedTasks(int _Flag, Context context, String workdir, String _FileName) {
		this.workdir = workdir;
		this.context = context;
		Flag = _Flag;
		FileName = _FileName;
		Items = new Vector<TimedTaskItem>();
		LoadFromFile();
	}

	public void LoadFromFile() // 从文件装入
	{
		Items.clear();
		String filename = context.getFilesDir() + File.separator + workdir;
		File file = new File(filename); // String path = getFilesDir().getAbsolutePath() ;
		File file2 = new File(file.getAbsoluteFile() + File.separator + FileName);
		if (!file2.exists())
			return;
		FileInputStream br = null;
		try {
			br = new FileInputStream(file2);
			int flag = StreamReadWrite.ReadInt(br); // br.ReadInt32();int flag =
													// br.ReadInt32();
			if (flag != Flag) // 不是本智能家居系统的文件
			{
				br.close();
				return;
			}
			int count = StreamReadWrite.ReadInt(br); // br.ReadInt32(); //设备数量
			for (int i = 0; i < count; i++) {
				TimedTaskItem item = new TimedTaskItem();
				Items.add(item); // 增加一个定时任务
				item.TaskName = StreamReadWrite.ReadString(br);
				int itemcount = StreamReadWrite.ReadInt(br); // 定时列表数量
				for (int j = 0; j < itemcount; j++) {
					TimeRange tr = new TimeRange();
					tr.StartTime = StreamReadWrite.ReadInt(br);
					tr.EndTime = StreamReadWrite.ReadInt(br);
					item.RunTime.add(tr); // 增加一个执行时间段
				}
			}
			br.close();
		} catch (Exception ex) {
		}
	}

	public Boolean SaveToFile() // 保存到文件
	{
		String filename = context.getFilesDir() + File.separator + workdir;
		File file = new File(filename); 
		File file2 = new File(file.getAbsoluteFile() + File.separator + FileName);
		FileOutputStream bw = null;
		try {
			bw = new FileOutputStream(file2);
			StreamReadWrite.WriteInt(bw, Flag); // bw.Write(Flag);
			StreamReadWrite.WriteInt(bw, Items.size()); // bw.Write(Items.Count);
														// //记录数量
			for (int i = 0; i < Items.size(); i++) {
				TimedTaskItem item = Items.get(i);
				StreamReadWrite.WriteString(bw, item.TaskName); // 对应的任务名称
				StreamReadWrite.WriteInt(bw, item.RunTime.size()); // 定时列表数量
				for (int j = 0; j < item.RunTime.size(); j++) {
					StreamReadWrite.WriteInt(bw, item.RunTime.get(j).StartTime);
					StreamReadWrite.WriteInt(bw, item.RunTime.get(j).EndTime);
				}
			}
			bw.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public TimedTaskItem FindTimedTaskItem(String taskname)
	{
		for (int i=0;i<Items.size();i++)
		{
			if (Items.get(i).TaskName.equals(taskname))
				return Items.get(i);
		}
		return null;
	}
}
