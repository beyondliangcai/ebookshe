package sub.two.Activity;

import sub.two.PersonalView.PButton;
import sub.two.WebviewBrowser.Main;
import sub.two.searchlocalfile.MyFile;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
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
	
	//set program
	OnClickListener set_pro= new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	//book mark
    OnClickListener book_mark=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(context,Mark.class);
			context.startActivity(intent);
		}
	};
	
	//search book button
	public OnClickListener search_book=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
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
    
    //add book listener
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
			String textString=(String)((PButton)v).get_text();
			if (textString.equals(Handler_Msg.EDIT_STATE)) {
				Handler_Msg handler_Msg=new Handler_Msg();
				Message e1=handler_Msg.obtainMessage(Handler_Msg.EDIT_FINISHED);
				handler_Msg.sendMessage(e1);
			}
			if (textString.equals(Handler_Msg.NORMAL_STATE)) {
				Intent webviewIntent=new Intent();
				webviewIntent.setClass(context, Main.class);
				context.startActivity(webviewIntent);
				Log.v("book", "book store");
			}
			else {
				Log.v("book", "error in store button");
			}
		}
	};
	
	
}
