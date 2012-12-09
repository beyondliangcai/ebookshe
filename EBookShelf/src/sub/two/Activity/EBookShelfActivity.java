package sub.two.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;

import sub.two.WebviewBrowser.Main;
import sub.two.searchlocalfile.MyFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class EBookShelfActivity extends Activity {
    /** Called when the activity is first created. */
	public  Socket socket = null;
	public	BufferedReader bufr = null;
	public	PrintWriter pw = null;
	//ScrollView���������,��������������Ӧ������
	private int columNUM;
	private Context context;
	private ScrollView SV;
	
	public	String result = null;
	public File[] currentfiles;
	public File Ebookdir;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 
        super.onCreate(savedInstanceState);       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);  
        ImageView moreImageView=(ImageView)findViewById(R.id.more);
       
        moreImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(123);
				Intent webviewIntent =new Intent();
				webviewIntent.setClass(EBookShelfActivity.this, Main.class);
				startActivity(webviewIntent);
				//showView.loadUrl("http://www.baidu.com");
				//showView.loadUrl("http://192.168.151.50:8080/EBookShelf/");
			}
		});
        //��ʼ��һЩ����
        init();
     
        if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
        	Ebookdir=new File("/sdcard/Ebookdir");
        	if (!Ebookdir.exists()) {
				Ebookdir.mkdirs();
				}
        	}
        else {
        	Ebookdir=new File("/sdcard/Ebookdir");           //����Ŀ¼ebookdirĿ¼
        	if (!Ebookdir.exists()) {
				Ebookdir.mkdirs();		
				}        	      
        	}
    }//onCreat 
    private void init(){
    	SV=(ScrollView)findViewById(R.id.scrollview_in_fr);
    	context=SV.getContext();
    	//listener
    	Listener listener=new Listener(this);
        //��¼�¼�  
        ImageView LoginImageView=(ImageView)findViewById(R.id.login);
        LoginImageView.setOnClickListener(listener.LogClickListener);
     
        //���������鼮����ӱ����鼮��ʵ��
        ImageView searchlocalImageView =(ImageView)findViewById(R.id.search); 
        searchlocalImageView.setOnClickListener(listener.search);
        
        //���ӷ���������ǣ�
        ImageView book_store=(ImageView)findViewById(R.id.more);
        book_store.setOnClickListener(listener.book_store);
    }
 
   
}