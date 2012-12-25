package sub.two.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import sub.two.PersonalView.PButton;
import sub.two.PersonalView.PView;
import sub.two.Service.SearchLocalFile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	
	public static ScrollView sv;
	private PButton pb1,pb2;
	
	public static int SHELF_COUNT=3;
	public static int ID_COUNT=9;

	public static List<PView> pview_vec=new ArrayList<PView>();
	public static List<LinearLayout> shelf_show=new ArrayList<LinearLayout>();
	
	public static Context context;
	public static PButton book_store;
	private static int count=0;
	public static Boolean longclick_mode=false;
	
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
        	Message e=handle.obtainMessage(Handler_Msg.ADD_BOOK_WHILE_START);
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
        PButton searchlocal =(PButton)findViewById(R.id.search); 
        searchlocal.set_text("添加");
        searchlocal.setOnClickListener(listener.search);
        
        //连接服务器（书城）
        book_store=(PButton)findViewById(R.id.more);
        book_store.set_text(Handler_Msg.NORMAL_STATE);
        book_store.set_text_size(18);
        book_store.set_text_color(Color.WHITE);
        book_store.setOnClickListener(listener.book_store);
		
		//book mark button
		pb1=(PButton)findViewById(R.id.mark);
		pb1.set_text("记录");
		pb1.setOnClickListener(listener.book_mark);
		
		//set program
		pb2=(PButton)findViewById(R.id.set_pro);
        pb2.set_text("设定");
        pb2.setOnClickListener(listener.set_pro);
    }
    
	private void init_pview(){
        //默认控件初始化
        sv=(ScrollView)findViewById(R.id.scrollview_in_fr);
        sv.setVerticalScrollBarEnabled(false);
		View shelf=sv.findViewById(R.id.shelf1);
		shelf_show.add((LinearLayout)shelf);
		View hr1=shelf.findViewById(R.id.orgin_hline);
		
		PView v1=(PView)hr1.findViewById(R.id.IV1);
		pview_vec.add(v1);
		PView view2=(PView)hr1.findViewById(R.id.IV2);
		pview_vec.add(view2);
		PView view3=(PView)hr1.findViewById(R.id.IV3);
		pview_vec.add(view3);
		
		//shelf2
		View shelf2=sv.findViewById(R.id.shelf2);
		shelf_show.add((LinearLayout)shelf2);

		View hr2=shelf2.findViewById(R.id.orgin_hline);
		PView view4=(PView)hr2.findViewById(R.id.IV1);
		pview_vec.add(view4);
		PView view5=(PView)hr2.findViewById(R.id.IV2);
		pview_vec.add(view5);
		PView view6=(PView)hr2.findViewById(R.id.IV3);
		pview_vec.add(view6);
		 
		//shelf3
		View shelf3=sv.findViewById(R.id.shelf3);
		shelf_show.add((LinearLayout)shelf3);

		View hr3=shelf3.findViewById(R.id.orgin_hline);
		PView view7=(PView)hr3.findViewById(R.id.IV1);
		pview_vec.add(view7);
		PView view8=(PView)hr3.findViewById(R.id.IV2);
		pview_vec.add(view8);
		PView view9=(PView)hr3.findViewById(R.id.IV3);
		pview_vec.add(view9);
		
//		Log.v("book", ""+SHELF_COUNT);
		for (int i = 0; i < pview_vec.size(); i++) {
			pview_vec.get(i).set_id(i);
		}
		
	}
}