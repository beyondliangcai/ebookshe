package sub.two.Activity;

import java.util.ArrayList;
import java.util.List;

import sub.two.PersonalView.PView;
import sub.two.Service.SearchLocalFile;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class Handler_Msg extends Handler{
	@Override
	public void handleMessage(Message msg){
		if(msg.what==SearchLocalFile.ADD_BOOK){
			Log.v("book", "handle add message!");
			Bundle bundle=(Bundle)msg.obj;
        	addbook(bundle.getStringArrayList(SearchLocalFile.File_Name),
        			bundle.getStringArrayList(SearchLocalFile.File_Path), EBookShelfActivity.pview_vec);
		}
		
		if (msg.what==PView.DELETE_BOOK) {
			Log.d("book", "handle delete message!");
		}
		
		if (msg.what==PView.READ_BOOK) {
			Log.d("book", "handle read message!");
		}
		
		if (msg.what==PView.EDIT_BOOK) {
			Log.d("book", "handle edit message!");
		}
	}
	
	public static void addbook(ArrayList<String> name,ArrayList<String> path,List<PView> vec){	
		int ddq=(vec.size()>name.size())? name.size():vec.size();
//		Log.v("book", "add book message received,start adding book!");
		for (int i = 0; i < ddq; i++) {
			if(!vec.get(i).get_occupy()){
				vec.get(i).set_inner_Visibility(View.VISIBLE);
				String[] st=name.get(i).split("\\.");
				if(st[0].length()>5)
					st[0]=st[0].substring(0,5);
				vec.get(i).set_Title(st[0]);
				vec.get(i).set_occupy(true);
				}
		}
	}
}
