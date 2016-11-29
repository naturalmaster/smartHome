package com.eagle.smarthome.util;

public class Tools {

	public static String SecondtoTimeString(int seconds)
	{
		return String.format("%02d:%02d:%02d",seconds/3600, (seconds/60) %60, seconds % 60);

	}
}
