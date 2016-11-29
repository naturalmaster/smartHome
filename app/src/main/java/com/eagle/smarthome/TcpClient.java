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
    String ServerIP; //服务器IP地址
    int Port;             //TCP端口
    private LocalBroadcastManager localBroadcastManager;

    static TcpClient tcpClient = null;  //单例模式
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
                    if (count > 0) //有数据
                    {
                        byte[] tmp = new byte[count];
                        for (int i = 0; i < count; i++) tmp[i] = buffer[i];

                        StringJson json = StringJson.BytesToStringJson(tmp, flag);
                        if (json != null) {
                            bTwoPart = false;
                            ProcessCommand(json);
                        } else //不合法，可能被分成几个包发送过来
                        {    //先判断flag
                            if (!bTwoPart) {
                                if (count < 4) continue; //数据长度不合法
                                int b1, b2, b3, b4;
                                int position = 0;
                                //---首先读取标志
                                b1 = tmp[position++];
                                b2 = tmp[position++];
                                b3 = tmp[position++];
                                b4 = tmp[position++];
                                int _flag = (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
                                if (_flag == flag) //合法数据被分成两次发过来
                                {
                                    bTwoPart = true;
                                    firstPart = Arrays.copyOf(tmp, count);   //保存该数据
                                }
                            } else if (bTwoPart)  //接着上次的部分，合成一个数据
                            {
                                int len = firstPart.length;  //第一部分长度
                                if (len + count >= MaxBufferSize) //数据太多，不处理
                                {
                                    bTwoPart = false;
                                    continue;
                                }

                                byte[] allPart = new byte[len + count];//保存该数据
                                System.arraycopy(firstPart, 0, allPart, 0, len);
                                System.arraycopy(tmp, 0, allPart, len, count);
                                json = StringJson.BytesToStringJson(allPart, flag);
                                if (json == null)  //还没接收完毕，继续接收
                                {
                                    firstPart = Arrays.copyOf(allPart, len + count);   //重新保存该数据
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

        void ProcessCommand(StringJson json)  //处理数据
        {
            //Log.d("SmartHome", json.toString());
            Intent intent = new Intent("com.eagle.smarthome.TCPDATABROADCAST");
            intent.putExtra("json", json.GetBytes());
            //intent.putExtra("json", json.GetStrValueByName("cmd"));
            localBroadcastManager.sendBroadcast(intent);       //本地广播
        }

    }

}
