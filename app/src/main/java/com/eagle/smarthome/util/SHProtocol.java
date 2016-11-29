package com.eagle.smarthome.util;

public class SHProtocol 
{
		    public final static int SHFLAG = 0x5A5A5A5A;  //���ܼҾ�ϵͳͳһ��ʶ�����ڸ�������ͨ�Ź���
	        public  final static String LOGIN = "500";      //�ͻ���¼SHS��ֻ�е�¼�ɹ����ܷ���SHS [login=1/OK]:1Ҫ�����µ�¼��OK��¼�ɹ�[right=][ip=]��[error=XX]��[error=XX] [user=][password=]
	        public static final String APPSTATE = "501";   //��ȡ����SHM����״̬[stream=�ļ�����][id=appid][state=1/0]
	        public static final String STARTAPP = "502";   //֪ͨ���������SHM������ҪȨ��[appid=?][start=1][rights=NNNNNNNNNNNNNNN]
	        public static final String SHOWSHMUI = "503";  //֪ͨSHM������ʾ������[visible=1]��ʾ����������
	        public static final String UPDATEPICTURE = "504";   //����ͼƬ[pic=fn][size=N][need="1"][stream=byte[]]

	        public static final String SHACTRL = "505";    //��ĳ��SHAϵͳ���豸��ָ��[appid=N][devid=M][subid=X][type=Y][act=K]

	        public static final String DEVSTATE = "506";   //ĳ��SHAϵͳ���豸�����豸״̬����[appid=N][devid=M][type=Y][subid=X][value=V?]

	        public static final String GETTASK = "507";    //��ȡ���ܼҾӵ��������� [task=fn][need="1"][stream=byte[]][index=?]

	        public static final String MENDTASK = "508";  //�޸����ܼҾӵ���������[task=fn][stream=byte[]][plan=tskfn][timetask=ttskfn]
	        public static final String RUNTASK = "509";    //֪ͨSHSִ��ĳ������[task=fn][timed=1][starttime=second]
	        public static final String GETALARM = "510";   //��ȡ���ܼҾӵļ������
	        public static final String MENDALARM = "511";  //�޸����ܼҾӵļ������
	        public static final String TEXT = "512";       //SHA����SHM��SHS��CLIENT���ı�֪ͨ��Ϣ[text=?]

	        public static final String CAMERA = "513";     //�й�����ͷ����
	        public static final String SCREEM = "514";     //��ȡSHS��Ļͼ��
	        public static final String SETALARM = "515";   //���� ���/���� [set="1/0"]   ��ȡ���/����[value="1/0"]
	        public static final String MESSAGE = "516";   //�ƶ������Ը�������  [text=?]
	        
	        
	        public static final String CLIENTEXIT = "520";   //�ͻ����˳�
	        public static final String ERRHINT = "521";    //ͨ�ô�����Ϣ��ʾ[err=text]
	        public static final String TESTCONECTION = "522";       //ͨ�Ų����Ƿ�����:�ͻ����յ���ԭ���ָ���û������

}
