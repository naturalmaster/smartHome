package com.eagle.smarthome.util;

public class SHProtocol 
{
		    public final static int SHFLAG = 0x5A5A5A5A;  //智能家居系统统一标识，便于各个厂家通信公用
	        public  final static String LOGIN = "500";      //客户登录SHS，只有登录成功才能访问SHS [login=1/OK]:1要求重新登录，OK登录成功[right=][ip=]。[error=XX]。[error=XX] [user=][password=]
	        public static final String APPSTATE = "501";   //获取所有SHM程序状态[stream=文件内容][id=appid][state=1/0]
	        public static final String STARTAPP = "502";   //通知启动或结束SHM程序，需要权限[appid=?][start=1][rights=NNNNNNNNNNNNNNN]
	        public static final String SHOWSHMUI = "503";  //通知SHM程序显示或隐藏[visible=1]显示，否则隐藏
	        public static final String UPDATEPICTURE = "504";   //更新图片[pic=fn][size=N][need="1"][stream=byte[]]

	        public static final String SHACTRL = "505";    //给某个SHA系统的设备发指令[appid=N][devid=M][subid=X][type=Y][act=K]

	        public static final String DEVSTATE = "506";   //某个SHA系统的设备的子设备状态数据[appid=N][devid=M][type=Y][subid=X][value=V?]

	        public static final String GETTASK = "507";    //获取智能家居的任务数据 [task=fn][need="1"][stream=byte[]][index=?]

	        public static final String MENDTASK = "508";  //修改智能家居的任务数据[task=fn][stream=byte[]][plan=tskfn][timetask=ttskfn]
	        public static final String RUNTASK = "509";    //通知SHS执行某个任务[task=fn][timed=1][starttime=second]
	        public static final String GETALARM = "510";   //获取智能家居的监控设置
	        public static final String MENDALARM = "511";  //修改智能家居的监控设置
	        public static final String TEXT = "512";       //SHA发给SHM、SHS、CLIENT的文本通知信息[text=?]

	        public static final String CAMERA = "513";     //有关摄像头操作
	        public static final String SCREEM = "514";     //获取SHS屏幕图像
	        public static final String SETALARM = "515";   //设置 设防/撤防 [set="1/0"]   获取设防/撤防[value="1/0"]
	        public static final String MESSAGE = "516";   //移动端留言给服务器  [text=?]
	        
	        
	        public static final String CLIENTEXIT = "520";   //客户端退出
	        public static final String ERRHINT = "521";    //通用错误信息提示[err=text]
	        public static final String TESTCONECTION = "522";       //通信测试是否连接:客户端收到后原样恢复，没有子项

}
