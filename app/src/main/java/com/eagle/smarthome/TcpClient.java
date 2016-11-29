package com.eagle.smarthome;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class TcpClient {
    public final int flag = 0x5A5A5A5A;
    public final int MaxBufferSize = 0x2FFFFF;
    public Socket socket;
    //public Vector<IProcessTcpData> forms;

    InputStream instream;
    OutputStream outstream;
    String ServerIP; //������IP��ַ
    int Port;             //TCP�˿�
    private LocalBroadcastManager localBroadcastManager;

    static TcpClient tcpClient = null;  //����ģʽ
    //private  static  StringJson json;

    private TcpClient(LocalBroadcastManager localBroadcastManager, String IP, int port) {
        this.localBroadcastManager = localBroadcastManager;
        InitSocket(IP, port);
    }

    public static TcpClient GetTcpClient(LocalBroadcastManager localBroadcastManager, String IP, int port) {
        if (tcpClient != null) {
            if (tcpClient.socket == null)
                tcpClient.InitSocket(IP, port);
            return tcpClient;
        }
        tcpClient = new TcpClient(localBroadcastManager, IP, port);
        return tcpClient;
    }

    public static TcpClient GetTcpClient() {
        return tcpClient;
    }

    public void InitSocket(String IP, int port) {
        this.ServerIP = IP;
        this.Port = port;
        try {
            this.socket = new Socket(ServerIP, Port);
            socket.setReceiveBufferSize(MaxBufferSize);
            socket.setSendBufferSize(MaxBufferSize);
            socket.setTcpNoDelay(true);
            //socket.setSoTimeout(20);
            //socket.setKeepAlive(true);
            //socket.setSoTimeout(20000);
            instream = socket.getInputStream();
            outstream = socket.getOutputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
            socket = null;
        }
        if (socket != null) {
            ReceiveData receive = new ReceiveData();
            Thread td = new Thread(receive);
            td.start();
        }
    }

    public boolean SendMessage(byte[] msg) {
        if (socket == null) return false;
        if (!socket.isConnected()) return false;
        try {
            outstream.write(msg, 0, msg.length);
            outstream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean SendMessage(StringJson json) {
        if (socket == null) return false;
        if (!socket.isConnected()) return false;
        try {
            byte[] msg = json.GetBytes();
            outstream.write(msg, 0, msg.length);
            outstream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void Close() {
        if (socket == null) return;
        try {
            instream.close();
            outstream.close();
            socket.close();
            socket = null;
        } catch (Exception ex) {
            socket = null;
        }
    }

/*	public static  StringJson GetStringJson()
	{
		return json;
	} */

    class ReceiveData implements Runnable {
        byte[] buffer = new byte[32768];
        Boolean bTwoPart = false;
        byte[] firstPart;

        @Override
        public void run() {
            while (true) {
                try {
                    if (socket == null) break;
                    int count = instream.read(buffer);
                    if (count > 0) //������
                    {
                        byte[] tmp = new byte[count];
                        for (int i = 0; i < count; i++) tmp[i] = buffer[i];

                        StringJson json = StringJson.BytesToStringJson(tmp, flag);
                        if (json != null) {
                            bTwoPart = false;
                            ProcessCommand(json);
                        } else //���Ϸ������ܱ��ֳɼ��������͹���
                        {    //���ж�flag
                            if (!bTwoPart) {
                                if (count < 4) continue; //���ݳ��Ȳ��Ϸ�
                                int b1, b2, b3, b4;
                                int position = 0;
                                //---���ȶ�ȡ��־
                                b1 = tmp[position++];
                                b2 = tmp[position++];
                                b3 = tmp[position++];
                                b4 = tmp[position++];
                                int _flag = (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
                                if (_flag == flag) //�Ϸ����ݱ��ֳ����η�����
                                {
                                    bTwoPart = true;
                                    firstPart = Arrays.copyOf(tmp, count);   //���������
                                }
                            } else if (bTwoPart)  //�����ϴεĲ��֣��ϳ�һ������
                            {
                                int len = firstPart.length;  //��һ���ֳ���
                                if (len + count >= MaxBufferSize) //����̫�࣬������
                                {
                                    bTwoPart = false;
                                    continue;
                                }

                                byte[] allPart = new byte[len + count];//���������
                                System.arraycopy(firstPart, 0, allPart, 0, len);
                                System.arraycopy(tmp, 0, allPart, len, count);
                                json = StringJson.BytesToStringJson(allPart, flag);
                                if (json == null)  //��û������ϣ���������
                                {
                                    firstPart = Arrays.copyOf(allPart, len + count);   //���±��������
                                } else {
                                    bTwoPart = false;
                                    ProcessCommand(json);
                                }
                            } else
                                bTwoPart = false;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        void ProcessCommand(StringJson json)  //��������
        {
            //Log.d("SmartHome", json.toString());
            Intent intent = new Intent("com.eagle.smarthome.TCPDATABROADCAST");
            intent.putExtra("json", json.GetBytes());
            //intent.putExtra("json", json.GetStrValueByName("cmd"));
            localBroadcastManager.sendBroadcast(intent);       //���ع㲥
        }

    }

}
