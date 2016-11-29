package com.eagle.smarthome.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.eagle.smarthome.util.StreamReadWrite;

import android.content.Context;

public class TimedTasks
{
	public int Flag; // Ψһʶ����
	public String FileName; // �������ж�ʱ�������ļ���֪���������˽����ж�ʱ���������� .ttsk��չ��
	private String workdir;
	private Context context;

	public Vector<TimedTaskItem> Items; // ��ʱ�б�
	// public TimedTasks(int _Flag, String _FileName)

	public TimedTasks(int _Flag, Context context, String workdir, String _FileName) {
		this.workdir = workdir;
		this.context = context;
		Flag = _Flag;
		FileName = _FileName;
		Items = new Vector<TimedTaskItem>();
		LoadFromFile();
	}

	public void LoadFromFile() // ���ļ�װ��
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
			if (flag != Flag) // ���Ǳ����ܼҾ�ϵͳ���ļ�
			{
				br.close();
				return;
			}
			int count = StreamReadWrite.ReadInt(br); // br.ReadInt32(); //�豸����
			for (int i = 0; i < count; i++) {
				TimedTaskItem item = new TimedTaskItem();
				Items.add(item); // ����һ����ʱ����
				item.TaskName = StreamReadWrite.ReadString(br);
				int itemcount = StreamReadWrite.ReadInt(br); // ��ʱ�б�����
				for (int j = 0; j < itemcount; j++) {
					TimeRange tr = new TimeRange();
					tr.StartTime = StreamReadWrite.ReadInt(br);
					tr.EndTime = StreamReadWrite.ReadInt(br);
					item.RunTime.add(tr); // ����һ��ִ��ʱ���
				}
			}
			br.close();
		} catch (Exception ex) {
		}
	}

	public Boolean SaveToFile() // ���浽�ļ�
	{
		String filename = context.getFilesDir() + File.separator + workdir;
		File file = new File(filename); 
		File file2 = new File(file.getAbsoluteFile() + File.separator + FileName);
		FileOutputStream bw = null;
		try {
			bw = new FileOutputStream(file2);
			StreamReadWrite.WriteInt(bw, Flag); // bw.Write(Flag);
			StreamReadWrite.WriteInt(bw, Items.size()); // bw.Write(Items.Count);
														// //��¼����
			for (int i = 0; i < Items.size(); i++) {
				TimedTaskItem item = Items.get(i);
				StreamReadWrite.WriteString(bw, item.TaskName); // ��Ӧ����������
				StreamReadWrite.WriteInt(bw, item.RunTime.size()); // ��ʱ�б�����
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
