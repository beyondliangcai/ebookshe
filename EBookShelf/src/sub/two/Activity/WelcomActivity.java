package sub.two.Activity;

import java.util.ArrayList;

import sub.two.DB.MyDB;
import sub.two.PersonalView.PView;
import sub.two.Service.SearchLocalFile;
import sub.two.searchlocalfile.searchfile;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class WelcomActivity extends Activity {             //想做一个开机的启动画面
	public static MyDB EbookdDb;
	public ArrayList<bok_tmp> Ebooks=new ArrayList<bok_tmp>();
	public Cursor bookCursor;
	public int rows;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcomactivitylayout);
        EbookdDb =new MyDB(this, "EbookDb.db3", 1);
        Start();
      
	}
	
	public void Start() {
        new Thread() {
                public void run() {
                        try {
                        	bok_tmp tempBok_tmp = new bok_tmp();
                        	bookCursor=EbookdDb.getReadableDatabase().query("BookDB", new String[]{"id,title,path,auther,intro,pic"}, null, null, null, null, null);
                 
                        	while (bookCursor.moveToNext()) {
                        	tempBok_tmp.id=bookCursor.getInt(0);
                        	tempBok_tmp.title=bookCursor.getString(1);
                        	tempBok_tmp.auther=bookCursor.getString(2);
                        	tempBok_tmp.path=bookCursor.getString(3);
                        	tempBok_tmp.pic_path=bookCursor.getString(4);
                        	tempBok_tmp.intro=bookCursor.getString(5);						
							Ebooks.add(tempBok_tmp);	
							SearchLocalFile.filenameArrayList.add(tempBok_tmp.title);
							SearchLocalFile.filepathArrayList.add(tempBok_tmp.path);												
						}
                       	Log.v("book", ""+Ebooks.size());	 
                        	Thread.sleep(100);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.setClass(WelcomActivity.this, EBookShelfActivity.class);
                        startActivity(intent);
                        finish();
                }
        }.start();
}
}

	
	

