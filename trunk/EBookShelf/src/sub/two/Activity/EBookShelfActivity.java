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
import android.view.View;
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
	private View shelf,shelf2,hr1,hr2;
	public static Handler handler;
	private List<PView> pview_vec=new ArrayList<PView>();
	//private PView v1,v2,v3,v4,v5,v6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 
        super.onCreate(savedInstanceState);       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);  

        //初始化一些参数
        init();
        init_pview();
        
        handler=new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		if (msg.what==SearchLocalFile.add_book) {        			
        			Bundle bundle=(Bundle)msg.obj;
                	addbook(bundle.getStringArrayList(SearchLocalFile.File_Name),
                			bundle.getStringArrayList(SearchLocalFile.File_Path), pview_vec);
				}
        	}
        };
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
    
	public void init_pview(){
        //默认控件初始化
        sv=(ScrollView)findViewById(R.id.scrollview_in_fr);
		shelf=sv.findViewById(R.id.shelf1);
		hr1=shelf.findViewById(R.id.orgin_hline);
		PView v1=(PView)hr1.findViewById(R.id.IV1);
		pview_vec.add(v1);
		PView view2=(PView)hr1.findViewById(R.id.IV2);
		pview_vec.add(view2);
		PView view3=(PView)hr1.findViewById(R.id.IV3);
		pview_vec.add(view3);
		//shelf2
		shelf2=sv.findViewById(R.id.shelf2);
		hr2=shelf2.findViewById(R.id.orgin_hline);
		PView view4=(PView)hr2.findViewById(R.id.IV1);
		pview_vec.add(view4);
		PView view5=(PView)hr2.findViewById(R.id.IV2);
		pview_vec.add(view5);
		PView view6=(PView)hr2.findViewById(R.id.IV3);
		pview_vec.add(view6);
		for (int i = 0; i < pview_vec.size(); i++) {
			pview_vec.get(i).setVisibility(View.INVISIBLE);
			pview_vec.get(i).set_occupy(false);
		}
	}
	
	public static void addbook(ArrayList<String> name,ArrayList<String> path,List<PView> vec){	
		int ddq=(vec.size()>name.size())? name.size():vec.size();
		for (int i = 0; i < ddq; i++) {
			Log.v("book", "size:"+ddq);
			if(!vec.get(i).get_occupy()){
				vec.get(i).setVisibility(View.VISIBLE);
				vec.get(i).set_Title(name.get(i));
				vec.get(i).set_occupy(true);
				}
		}
		Log.d("book", "add book successfully! ");
	}
}