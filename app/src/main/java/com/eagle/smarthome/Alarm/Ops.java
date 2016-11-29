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
    public Boolean GetResult()  //得到运算结果
    {
        if (Items.size() == 0) return false;
        if (Items.size() == 1) return Items.get(0).Ex;
        if (Items.size() == 2) return Items.get(0).Operation(Items.get(1));
        //有多个逻辑值参入运算，先计算“并且”运算
        int n = 0;
        while (true)
        {
            if (Items.get(n).combine == "并且")
            {
            	Items.get(n).Ex = Items.get(n).Operation(Items.get(n+1)); // 并且运算
            	Items.get(n).combine = Items.get(n+1).combine;        //后续运算符
                Items.remove(n + 1);  //移除后面的Op
            }
            else
                n++;  //跳过“或者”运算

            if (Items.size() == 2) return Items.get(0).Operation(Items.get(1));  //剩下两个逻辑运算时
            if (n >= Items.size() - 1) break;
        }
        Boolean ret = false;
        for (int i = 0; i < Items.size() - 1; i++) //剩下的全是“或者”
        {
            ret = Items.get(i).Operation(Items.get(i + 1));
            if (ret) return true;
        }
        return ret;
    }

}
