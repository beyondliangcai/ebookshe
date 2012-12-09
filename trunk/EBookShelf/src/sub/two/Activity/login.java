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
	System.out.println("客户端测试程序!");
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
		System.out.println("客户端连接失败!");
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("客户端连接失败!");
		return;		//连接失败，退出程序
	}
	
	System.out.println("客户端连接成功!");
	
	try {
		os.write("登录:lc|lc".getBytes());
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
//	os.write("注册:aaa|aaa".getBytes());
	System.out.println("客户端发送完数据!");
	try {
		System.out.println("客户端在获取结果: result");
		byte[] bufIn = new byte[1024];
		int num = is.read(bufIn);
		result = new String(bufIn, 0, num);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("客户端读取服务器端返回验证信息失败!");
	}
	
	System.out.println("lc登录状态:---->" + result);
}
}





  

