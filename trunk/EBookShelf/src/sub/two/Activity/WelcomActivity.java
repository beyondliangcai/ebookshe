package sub.two.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class WelcomActivity extends Activity {             //����һ����������������
	
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
        setContentView(R.layout.welcomactivitylayout);
        Start();
      
	}
	public void Start() {
        new Thread() {
                public void run() {
                        try {
                                Thread.sleep(1000);
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

	
	
