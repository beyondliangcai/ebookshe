package sub.two.Activity;

import sub.two.WebviewBrowser.Main;
import sub.two.searchlocalfile.MyFile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class Listener {
	private Context context;
	private View view_coll[];
	private int NOT_FOUND=-1;
	private EBookShelfActivity EA=new EBookShelfActivity();
	//Construct
	public  Listener(Context context) {
		this.context=context;
	}
	
	//login listener
    OnClickListener  LogClickListener=new OnClickListener() {
    	@Override
    	public void onClick(View arg0) {
//    		Log.v("book", "login button");
    		// TODO Auto-generated method stub
    		Intent intent =new Intent();
    		intent.setClass(context,LoginMainActivity.class);
    		context.startActivity(intent);
    	}
    };
    //search listener
    OnClickListener search=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Log.v("book", "search button");
			Intent searchIntent=new Intent();
			searchIntent.setClass(context, MyFile.class);
  		    context.startActivity(searchIntent);
		}
	};
	
	//book store listener
	OnClickListener book_store=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent webviewIntent=new Intent();
			webviewIntent.setClass(context, Main.class);
			context.startActivity(webviewIntent);
			Log.v("book", "book store");
		}
	};
	
	
}
