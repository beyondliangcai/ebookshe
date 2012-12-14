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
	@Override
	public void handleMessage(Message msg){
		//add book
		if(msg.what==SearchLocalFile.ADD_BOOK){		
			//WelcomActivity.EbookdDb.insertdata( WelcomActivity.EbookdDb.getReadableDatabase(),i+1, filenameArrayList.get(i),filepathArrayList.get(i), null, null, null);	
			Log.v("book", "handle add message!");
			Bundle bundle=(Bundle)msg.obj;
        	addbook(bundle.getStringArrayList(SearchLocalFile.File_Name),
        			bundle.getStringArrayList(SearchLocalFile.File_Path), EBookShelfActivity.pview_vec);
		}
		
		//delete book
		if (msg.what==PView.DELETE_BOOK) {
			Log.d("book", "handle delete message!");
			final int i=msg.arg1;
			if(EBookShelfActivity.pview_vec.get(i)!=null){
				AlertDialog.Builder a=new AlertDialog.Builder(EBookShelfActivity.context);
				a.setTitle("");
				a.setMessage("是否删除源文件？");
				a.setPositiveButton("是",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				a.setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EBookShelfActivity.pview_vec.get(i).init();
					}
				});
				a.show();
			}
		}
		
		//read book
		if (msg.what==PView.READ_BOOK) {
			Log.d("book", "handle read message!");
			int i=msg.arg1;
			Log.v("book", ""+i);
		}
		
		//edit book
		if (msg.what==PView.EDIT_BOOK) {
			Log.d("book", "handle edit message!");
			int i=msg.arg1;
			Log.v("book", ""+i);
		}
	}
	
	public static void addbook(ArrayList<String> name,ArrayList<String> path,List<PView> vec){	
		int ddq=(vec.size()>name.size())? name.size():vec.size();
		int temp=0;
		Log.v("book", name.toString());
		for (int i = 0; i < ddq; i++) {
			if(!vec.get(i).get_occupy()){
				vec.get(i).set_inner_Visibility(View.VISIBLE);
				String[] st=name.get(temp).split("\\.");
				if(st[0].length()>5)
					st[0]=st[0].substring(0,5);
				vec.get(i).set_Title(st[0]);
				vec.get(i).set_occupy(true);
				WelcomActivity.EbookdDb.insertdata(WelcomActivity.EbookdDb.getReadableDatabase(),
						vec.get(i).get_id(),vec.get(i).get_Title(),vec.get(i).get_path(),vec.get(i).get_Auther()
						,vec.get(i).get_intro(),vec.get(i).get_image_path());
				}
			temp++;
		}
		Log.v("book", "add book successfully!");
	}
}
