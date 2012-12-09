package sub.two.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class showweb extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
				setContentView(R.layout.webview);
				WebView showView=(WebView) findViewById(R.id.showview);
				//showView.loadUrl("http://www.baidu.com/");
				showView.loadUrl("http://192.168.151.50:8080/EBookShelf/");
			

		

	}
	

}
