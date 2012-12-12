package sub.two.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import sub.two.Activity.EBookShelfActivity;
import sub.two.searchlocalfile.MyFile;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class SearchLocalFile extends Service{
	
	public static ArrayList name=new ArrayList<String>();
	public static ArrayList localfile=new ArrayList<String>();
	public static int add_book= -8;
	
	public static final String File_Name="filename";
	public static final String File_Path="filepath";
	private ArrayList<String> filenameArrayList=new ArrayList<String>();
	private ArrayList<String> filepathArrayList=new ArrayList<String>();
	//public static int count=0;             本打算计算添加了几本书籍的，但貌似启动service的时候有延迟总是少一次....
	public RandomAccessFile locallog;
	private void getFileName(File[] files) {       //收索sd卡上所有.txt的文件
        if (files != null) {
        	 
            for (File file : files) {  
                if (file.isDirectory()) {         
  
                    getFileName(file.listFiles());    
                    
                } 
                else {  
                    String fileName = file.getName();  
                    String localpathString=file.getAbsolutePath();
                    if (fileName.endsWith(".txt")) {  
                        HashMap map = new HashMap();  
                        HashMap map2 = new HashMap();
                        int i;
                        
                        for ( i = 0; i < filenameArrayList.size(); i++) {
							if (fileName.equals(filenameArrayList.get(i).toString())) {
								
								break;
							}
						}
                        if (i==filenameArrayList.size()||filenameArrayList.size()==0) {
                        	 filenameArrayList.add(fileName);
                        	 filepathArrayList.add(localpathString);
                        //	 count++;
						}                       
                                         
                        String s = fileName.substring(0, fileName.lastIndexOf(".")).toString();                    
                       map.put("", fileName.substring(0, fileName.lastIndexOf(".")));  
                       map2.put(fileName, localpathString);
                        name.add(map);  
                        localfile.add(map2);
                    }  
                }  
            }  
        }  
    }  
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);		
		//File path = Environment.getExternalStorageDirectory();// 获得SD卡路径  
        //  File path = new File("/mnt/sdcard/");  
		File path=new File(MyFile.addpath);
		
		if (!path.isDirectory()&&path.getName().endsWith(".txt")) {
			int i;
			 for ( i = 0; i < filenameArrayList.size(); i++) {
					if (path.getName().equals(filenameArrayList.get(i).toString())) {
						
						break;
					}
				}
             if (i==filenameArrayList.size()||filenameArrayList.size()==0) {
             	 filenameArrayList.add(path.getName());
             	 filepathArrayList.add(path.getAbsolutePath());
				}                       
                              
		}
		else {
			 File[] files = path.listFiles();// 读取  
			getFileName(files);
		}
         
             
        File localFile=new File("/sdcard/Ebookdir/locallog.txt");
		try {
			locallog=new RandomAccessFile(localFile, "rw");//创建日志文件
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < filenameArrayList.size(); i++) {
			System.out.println(filenameArrayList.get(i)+filepathArrayList.get(i));
		}
	//System.out.println(count);
		
		for (int i = 0; i <localfile.size(); i++) {
			try {				
				locallog.writeUTF(localfile.get(i).toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Bundle bundle=new Bundle();
		bundle.putStringArrayList(File_Name, filenameArrayList);
		bundle.putStringArrayList(File_Path, filepathArrayList);
		Message.obtain(EBookShelfActivity.handler,add_book,bundle).sendToTarget();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
