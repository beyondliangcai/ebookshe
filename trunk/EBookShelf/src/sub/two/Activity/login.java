package sub.two.Activity;


import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class login extends Service {
	
@Override
public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void onStart(Intent intent, int startId) {
	// TODO Auto-generated method stub
	System.out.println("�ͻ��˲��Գ���!");
	Socket socket = null;
	InputStream is = null;
	OutputStream os = null;
	String result = null;
	
	try {
		socket = new Socket("xinluke.eicp.net", 9999);
//		socket = new Socket("127.0.0.1", 9999);
		
		is = socket.getInputStream();
		os = socket.getOutputStream();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		System.out.println("�ͻ�������ʧ��!");
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("�ͻ�������ʧ��!");
		return;		//����ʧ�ܣ��˳�����
	}
	
	System.out.println("�ͻ������ӳɹ�!");
	
	try {
		os.write("��¼:lc|lc".getBytes());
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
//	os.write("ע��:aaa|aaa".getBytes());
	System.out.println("�ͻ��˷���������!");
	try {
		System.out.println("�ͻ����ڻ�ȡ���: result");
		byte[] bufIn = new byte[1024];
		int num = is.read(bufIn);
		result = new String(bufIn, 0, num);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("�ͻ��˶�ȡ�������˷�����֤��Ϣʧ��!");
	}
	
	System.out.println("lc��¼״̬:---->" + result);
}
}





  

