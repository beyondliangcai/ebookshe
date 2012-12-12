package sub.two.Activity;

import sub.two.Service.SearchLocalFile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class broadcast_receiver extends BroadcastReceiver{
	private String intent_for_addbook="sub.two.intent.addbook";
	private EBookShelfActivity eA=new EBookShelfActivity();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(intent_for_addbook)) {
			Log.d("book", "add book intent is been received");
			
			if (intent.getStringArrayListExtra(SearchLocalFile.File_Name)==null)
				Log.v("book", "null string");
			else{ 
				//Log.v("book", intent.getStringArrayListExtra(SearchLocalFile.File_Name).toString());
				//EBookShelfActivity.addbook(intent.getStringArrayListExtra("filename"), intent.getStringArrayListExtra("filepath"),ManageBook.pview_vec);
			}
		}
	}

}
