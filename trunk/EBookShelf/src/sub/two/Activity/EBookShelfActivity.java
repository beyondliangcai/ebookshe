package sub.two.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;


public class EBookShelfActivity extends Activity {
    /** Called when the activity is first created. */
	public  Socket socket = null;
	public	BufferedReader bufr = null;
	public	PrintWriter pw = null;
	
	public	String result = null;
	public File[] currentfiles;
	public File Ebookdir;
	
	public ScrollView sv;
	Handler handler;
	ManageBook manager;
	 
	addbook ass=new addbook();

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 
        super.onCreate(savedInstanceState);       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);  
        sv=(ScrollView)findViewById(R.id.scrollview_in_fr);
        //初始化一些参数
        init();

        if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
        	Ebookdir=new File("/sdcard/Ebookdir");
        	if (!Ebookdir.exists()) {
				Ebookdir.mkdirs();
				}
        	}
        else {
        	Ebookdir=new File("/sdcard/Ebookdir");           //创建目录ebookdir目录
        	if (!Ebookdir.exists()) {
				Ebookdir.mkdirs();		
				}        	      
        	}
    }//onCreat 
    private void init(){
    	manager=new ManageBook(sv);
    	//listener
    	Listener listener=new Listener(this,sv);
        //登录事件  
        ImageView LoginImageView=(ImageView)findViewById(R.id.login);
        LoginImageView.setOnClickListener(listener.LogClickListener);
     
        //收索本地书籍并添加本地书籍的实现
        ImageView searchlocalImageView =(ImageView)findViewById(R.id.search); 
        searchlocalImageView.setOnClickListener(listener.search);
        
        //连接服务器（书城）
        ImageView book_store=(ImageView)findViewById(R.id.more);
        book_store.setOnClickListener(listener.book_store);
        
    }
    class addbook extends BroadcastReceiver{
    	Bundle mybundle=new Bundle();
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			

		}
    	
    }
   
}