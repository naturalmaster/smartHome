package com.eagle.smarthome.util;

import java.io.Serializable;

///¼à¿ØµãµÇÂ¼ÐÅÏ¢
public class HomeLocation implements Serializable
{
     	private static final long serialVersionUID = 1L;
		public String HomeName;
		public String Account;
		public String Password;
		public String IP1;
		public String Port1;
		public String IP2;
		public String Port2;
		
		public HomeLocation()
		{
			HomeName="MyHome";
			Account="rainbow";
			Password="123";
			IP1="192.168.1.220";
			Port1="50001";
			IP2="smarthomemusic.vicp.cc";
			Port2="24625";
		}

}
