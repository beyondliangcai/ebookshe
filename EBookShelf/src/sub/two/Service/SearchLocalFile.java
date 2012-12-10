package sub.two.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

import sub.two.searchlocalfile.MyFile;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SearchLocalFile extends Service{
	
	public static ArrayList name=new ArrayList<String>();
	public static ArrayList localfile=new ArrayList<String>();
	
	public static final String File_Name="filename";
	public static final String File_Path="filepath";
	public static ArrayList<String> filenameArrayList=new ArrayList<String>();
	private ArrayList<String> filepathArrayList=new ArrayList<String>();
	//public static int count=0;             �������������˼����鼮�ģ���ò������service��ʱ�����ӳ�������һ��....
	public RandomAccessFile locallog;
	private void getFileName(File[] files) {       //����sd��������.txt���ļ�
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
		//File path = Environment.getExternalStorageDirectory();// ���SD��·��  
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
			 File[] files = path.listFiles();// ��ȡ  
			getFileName(files);
		}
         
             
        File localFile=new File("/sdcard/Ebookdir/locallog.txt");
		try {
			locallog=new RandomAccessFile(localFile, "rw");//������־�ļ�
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
		
		Intent add_book=new Intent("sub.two.intent.addbook");
		//intent.setAction(intent_for_addbook);
		Log.d("book", filenameArrayList.toString());
		intent.putStringArrayListExtra(File_Name, filenameArrayList);
		intent.putStringArrayListExtra(File_Path, filepathArrayList);
		sendBroadcast(add_book);
		Log.v("book", "brocast send out!");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
