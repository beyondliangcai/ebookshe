package sub.two.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import sub.two.PersonalView.PView;
import sub.two.Service.SearchLocalFile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


public class EBookShelfActivity extends Activity {
    /** Called when the activity is first created. */
	public  Socket socket = null;
	public	BufferedReader bufr = null;
	public	PrintWriter pw = null;
	
	public	String result = null;
	public File[] currentfiles;
	public File Ebookdir;
	
	public ScrollView sv;
	private View shelf,shelf2,shelf3,hr1,hr2,hr3;
	//public static Handler handler;
	public static List<PView> pview_vec=new ArrayList<PView>();
	public static List<TextView> shelf_tv=new ArrayList<TextView>();
	public static Context context;
	public static int ADD_BOOK_WHILE_START=-92;
	private static int count=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 
        super.onCreate(savedInstanceState);       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);  

        //初始化一些参数
        init();
        init_pview();
        
        if(count==0){
        	Handler_Msg handle=new Handler_Msg();
        	Message e=handle.obtainMessage(ADD_BOOK_WHILE_START);
        	handle.sendMessage(e);
        	count++;
        }
        
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

    //按返回键事件处理
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	 if(keyCode == KeyEvent.KEYCODE_BACK){
    		 android.os.Process.killProcess(android.os.Process.myPid());
    		 
    	 }
    	 
		return super.onKeyDown(keyCode, event);
	}

	
    private void init(){

    	context=this;
    	//listener
    	Listener listener=new Listener(this);
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
    
	private void init_pview(){
        //默认控件初始化
        sv=(ScrollView)findViewById(R.id.scrollview_in_fr);
        
		shelf=sv.findViewById(R.id.shelf1);
		hr1=shelf.findViewById(R.id.orgin_hline);
		
		TextView tx1=(TextView)shelf.findViewById(R.id.tv_in_shelf);
		shelf_tv.add(tx1);
		
		PView v1=(PView)hr1.findViewById(R.id.IV1);
		pview_vec.add(v1);
		PView view2=(PView)hr1.findViewById(R.id.IV2);
		pview_vec.add(view2);
		PView view3=(PView)hr1.findViewById(R.id.IV3);
		pview_vec.add(view3);
		//shelf2
		shelf2=sv.findViewById(R.id.shelf2);
		TextView tx2=(TextView)shelf2.findViewById(R.id.tv_in_shelf);
		shelf_tv.add(tx2);
		hr2=shelf2.findViewById(R.id.orgin_hline);
		PView view4=(PView)hr2.findViewById(R.id.IV1);
		pview_vec.add(view4);
		PView view5=(PView)hr2.findViewById(R.id.IV2);
		pview_vec.add(view5);
		PView view6=(PView)hr2.findViewById(R.id.IV3);
		pview_vec.add(view6);
		
		//shelf3
		shelf3=sv.findViewById(R.id.shelf3);
		TextView tx3=(TextView)shelf3.findViewById(R.id.tv_in_shelf);
		shelf_tv.add(tx3);
		hr3=shelf3.findViewById(R.id.orgin_hline);
		PView view7=(PView)hr3.findViewById(R.id.IV1);
		pview_vec.add(view7);
		PView view8=(PView)hr3.findViewById(R.id.IV2);
		pview_vec.add(view8);
		PView view9=(PView)hr3.findViewById(R.id.IV3);
		pview_vec.add(view9);
		
		for (int j = 1; j <= shelf_tv.size(); j++) {
			shelf_tv.get(j-1).setText("shelf"+j);
		}
		for (int i = 0; i < pview_vec.size(); i++) {
			pview_vec.get(i).set_id(i);
		}
		

	}
}