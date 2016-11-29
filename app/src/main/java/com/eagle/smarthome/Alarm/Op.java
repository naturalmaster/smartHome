package com.eagle.smarthome.Alarm;

public class Op
{
	 public Boolean Ex;         //运算结果：真或者假
     public String combine;  //与其他Op的运算连接符
     public Op()
     {
         Ex = false;
         combine = "并且";
     }

     public Boolean Operation(Op op)  //与其他Op的运算
     {
         if (combine.equals("并且")) return (Ex && op.Ex);
         return (Ex || op.Ex);
     }

     public  String toString()
     {
         return  Ex+ " "+ combine; 
     }
}
