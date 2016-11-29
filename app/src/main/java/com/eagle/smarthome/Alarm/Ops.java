package com.eagle.smarthome.Alarm;

import java.util.Vector;

public class Ops
{
	public Vector<Op> Items;
    public Ops()
    {
        Items = new Vector<Op>();
    }

    public String toString()
    {
       String s = "";
        for (int i = 0; i < Items.size(); i++)
            s += Items.get(i).toString();
        return s;
    }
    public Boolean GetResult()  //�õ�������
    {
        if (Items.size() == 0) return false;
        if (Items.size() == 1) return Items.get(0).Ex;
        if (Items.size() == 2) return Items.get(0).Operation(Items.get(1));
        //�ж���߼�ֵ�������㣬�ȼ��㡰���ҡ�����
        int n = 0;
        while (true)
        {
            if (Items.get(n).combine == "����")
            {
            	Items.get(n).Ex = Items.get(n).Operation(Items.get(n+1)); // ��������
            	Items.get(n).combine = Items.get(n+1).combine;        //���������
                Items.remove(n + 1);  //�Ƴ������Op
            }
            else
                n++;  //���������ߡ�����

            if (Items.size() == 2) return Items.get(0).Operation(Items.get(1));  //ʣ�������߼�����ʱ
            if (n >= Items.size() - 1) break;
        }
        Boolean ret = false;
        for (int i = 0; i < Items.size() - 1; i++) //ʣ�µ�ȫ�ǡ����ߡ�
        {
            ret = Items.get(i).Operation(Items.get(i + 1));
            if (ret) return true;
        }
        return ret;
    }

}
