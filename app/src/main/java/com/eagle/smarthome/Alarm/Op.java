package com.eagle.smarthome.Alarm;

public class Op
{
	 public Boolean Ex;         //������������߼�
     public String combine;  //������Op���������ӷ�
     public Op()
     {
         Ex = false;
         combine = "����";
     }

     public Boolean Operation(Op op)  //������Op������
     {
         if (combine.equals("����")) return (Ex && op.Ex);
         return (Ex || op.Ex);
     }

     public  String toString()
     {
         return  Ex+ " "+ combine; 
     }
}
