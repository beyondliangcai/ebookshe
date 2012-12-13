package sub.two.Activity;

import sub.two.DB.MyDB;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class WelcomActivity extends Activity {             //想做一个开机的启动画面
	public static MyDB EbookdDb;
			
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

	
	

