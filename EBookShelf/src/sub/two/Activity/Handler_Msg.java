package sub.two.Activity;

import java.util.ArrayList;
import java.util.List;

import sub.two.DB.MyDB;
import sub.two.PersonalView.PView;
import sub.two.Service.SearchLocalFile;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class Handler_Msg extends Handler{
	private Listener li=new Listener(EBookShelfActivity.context);
	@Override
	public void handleMessage(Message msg){
		
		if(msg.what==SearchLocalFile.ADD_BOOK){		
			//WelcomActivity.EbookdDb.insertdata( WelcomActivity.EbookdDb.getReadableDatabase(),i+1, filenameArrayList.get(i),filepathArrayList.get(i), null, null, null);	
			Log.v("book", "handle add message!");
			Bundle bundle=(Bundle)msg.obj;
        	addbook(bundle.getStringArrayList(SearchLocalFile.File_Name),
        			bundle.getStringArrayList(SearchLocalFile.File_Path), EBookShelfActivity.pview_vec);
		}
		
		if (msg.what==PView.DELETE_BOOK) {
			Log.d("book", "handle delete message!");
			int i=msg.arg1;
			if(EBookShelfActivity.pview_vec.get(i)!=null){
				AlertDialog.Builder a=new AlertDialog.Builder(EBookShelfActivity.context);
				a.setMessage("是否删除源文件？");
				a.setPositiveButton("是",li.delete_confirm_yes );
				a.setNegativeButton("否", li.delete_confirm_no);
				a.show();
			}
		}
		
		if (msg.what==PView.READ_BOOK) {
			Log.d("book", "handle read message!");
			int i=msg.arg1;
			Log.v("book", ""+i);
		}
		
		if (msg.what==PView.EDIT_BOOK) {
			Log.d("book", "handle edit message!");
			int i=msg.arg1;
			Log.v("book", ""+i);
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
				WelcomActivity.EbookdDb.insertdata(WelcomActivity.EbookdDb.getReadableDatabase(),
						vec.get(i).get_id(),vec.get(i).get_Title(),vec.get(i).get_path(),vec.get(i).get_Auther()
						,vec.get(i).get_intro(),vec.get(i).get_image_path());
				}
		}
	}
}
