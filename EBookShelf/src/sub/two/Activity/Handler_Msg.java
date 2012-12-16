package sub.two.Activity;

import java.util.ArrayList;
import java.util.List;

import sub.two.DB.MyDB;
import sub.two.PersonalView.PView;
import sub.two.Service.SearchLocalFile;
import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Handler_Msg extends Handler{
//	public List<PView> pview=new ArrayList<PView>();
	public static List<PView> pview=new ArrayList<PView>();
	
	public static int TRUE=-95;
	public static int FLASE=-94;
	private SearchLocalFile sea1;
	private int MOVE_BOOK=-93;
	private List<String> s1=new ArrayList<String>();
	private static int SHELF_NAME=20121200; 
	
	public Handler_Msg() {
//		pview=EBookShelfActivity.pview_vec;
	}
	
	@Override
	public void handleMessage(Message msg){
		//ADD_BOOK_WHILE_START
		if(msg.what==EBookShelfActivity.ADD_BOOK_WHILE_START){
			Log.v("book", "add book while the app starts");
			addbook(SearchLocalFile.filenameArrayList,
					SearchLocalFile.filepathArrayList, EBookShelfActivity.pview_vec);
		}
		
		//add book
		if(msg.what==SearchLocalFile.ADD_BOOK){		
			//WelcomActivity.EbookdDb.insertdata( WelcomActivity.EbookdDb.getReadableDatabase(),i+1, filenameArrayList.get(i),filepathArrayList.get(i), null, null, null);	
			Log.v("book", "handle add message!");
			
			Bundle bundle=(Bundle)msg.obj;
			
			if(SearchLocalFile.filenameArrayList.size()>EBookShelfActivity.pview_vec.size()) {
				int i=SearchLocalFile.filenameArrayList.size()-EBookShelfActivity.pview_vec.size();
				LinearLayout linearLayout=(LinearLayout)(EBookShelfActivity.sv).findViewById(R.id.main_shelf);
				for (int j = 0; j < i/3+1; j++) {
					View view=LayoutInflater.from(EBookShelfActivity.context).inflate(R.layout.shelf, null);
					view.setId(SHELF_NAME+(++EBookShelfActivity.SHELF_COUNT));
					linearLayout.addView(view);
					
					View shelf=EBookShelfActivity.sv.findViewById(SHELF_NAME+EBookShelfActivity.SHELF_COUNT);
					View hr=shelf.findViewById(R.id.orgin_hline);
					
					TextView tx1=(TextView)shelf.findViewById(R.id.tv_in_shelf);
					EBookShelfActivity.shelf_tv.add(tx1);
					tx1.setText("shelf"+EBookShelfActivity.SHELF_COUNT);
					
					PView view1=(PView)hr.findViewById(R.id.IV1);
					view1.set_id(EBookShelfActivity.ID_COUNT++);
					EBookShelfActivity.pview_vec.add(view1);
					
					PView view2=(PView)hr.findViewById(R.id.IV2);
					view2.set_id(EBookShelfActivity.ID_COUNT++);
					EBookShelfActivity.pview_vec.add(view2);
					
					PView view3=(PView)hr.findViewById(R.id.IV3);
					view3.set_id(EBookShelfActivity.ID_COUNT++);
					EBookShelfActivity.pview_vec.add(view3);
					
				}
			}
        	addbook(bundle.getStringArrayList(SearchLocalFile.File_Name),
        			bundle.getStringArrayList(SearchLocalFile.File_Path), EBookShelfActivity.pview_vec);
		}
		
		//move
		if(msg.what==MOVE_BOOK){
			int i=msg.arg1;
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			movebook(SearchLocalFile.filenameArrayList, SearchLocalFile.filepathArrayList,
					EBookShelfActivity.pview_vec, i);
			
			WelcomActivity.EbookdDb.deletealldata(WelcomActivity.EbookdDb.getReadableDatabase());
			for (int j = 0; j < SearchLocalFile.filenameArrayList.size(); j++) {				
				WelcomActivity.EbookdDb.insertdata(WelcomActivity.EbookdDb.getReadableDatabase(),
					j,
					SearchLocalFile.filenameArrayList.get(j),
					SearchLocalFile.filepathArrayList.get(j),
					EBookShelfActivity.pview_vec.get(j).get_Auther(),
					EBookShelfActivity.pview_vec.get(j).get_intro(),
					EBookShelfActivity.pview_vec.get(j).get_image_path());
			
			}
			
			Cursor testCursor;
			testCursor=WelcomActivity.EbookdDb.getReadableDatabase().query("BookDB", new String[]{"id,title,path,auther,intro,pic"}, null, null, null, null, null);
            
            bok_tmp tempBok_tmp=new bok_tmp();
        	while (testCursor.moveToNext()) {
        	tempBok_tmp.id=testCursor.getInt(0);
        	tempBok_tmp.title=testCursor.getString(1);
        	tempBok_tmp.auther=testCursor.getString(2);
        	tempBok_tmp.path=testCursor.getString(3);
        	tempBok_tmp.pic_path=testCursor.getString(5);
        	tempBok_tmp.intro=testCursor.getString(4);						
        	Log.v("book",tempBok_tmp.title);
			}					
			WelcomActivity.EbookdDb.close();
		}
		
		//delete book
		if (msg.what==PView.DELETE_BOOK) {
			final int i=msg.arg1;
			Log.d("book", "delete id:"+i);
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
						SearchLocalFile.filenameArrayList.remove(i);
						Message.obtain(Handler_Msg.this, MOVE_BOOK, i, SearchLocalFile.filenameArrayList.size()).sendToTarget();
					}
				});
				a.show();
			}
		}
		
		//read book
		if (msg.what==PView.READ_BOOK) {
			Log.d("book", "handle read message!");
			int i=msg.arg1;
		}
		
		//edit book
		if (msg.what==PView.EDIT_BOOK) {
			Log.d("book", "handle edit message!");
			int i=msg.arg1;
		}
		
		//change book state
		if (msg.what==PView.CHANGE_BOOK_STATE) {
			int id=msg.arg1;
			try {
				EBookShelfActivity.pview_vec.get(id).set_outer_Visibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
				Log.v("book", "failed id:"+id);
			}
			}
	}
	 
	public void addbook(ArrayList<String> name,ArrayList<String> path,List<PView> vec){	
		int ddq=(vec.size()>name.size())? name.size():vec.size();
		int temp=0;
		Log.v("book", name.toString());
		for (int i = 0; i < vec.size(); i++) {
			vec.get(i).init();
		}
//		for (int i = 0; i < vec.size(); i++) {
//			Log.v("book",vec.get(i).get_Title() );
//		}
		
		WelcomActivity.EbookdDb.deletealldata(WelcomActivity.EbookdDb.getReadableDatabase());
		
		Log.v("book",""+1);
		for (int i = 0; i < ddq; i++) {
			if(!vec.get(i).get_occupy()){
				vec.get(i).set_inner_Visibility(View.VISIBLE);
				String[] st=name.get(temp).split("\\.");
				if(st[0].length()>5)
					st[0]=st[0].substring(0,5);
				vec.get(i).set_Title(st[0]);
				vec.get(i).set_occupy(true);	
				}		
			temp++;
		}
		Log.v("book",""+2);
//		for (int j = 0; j < SearchLocalFile.filenameArrayList.size();j++) {
//			WelcomActivity.EbookdDb.insertdata(WelcomActivity.EbookdDb.getReadableDatabase(),
//					j,
//					SearchLocalFile.filenameArrayList.get(j),
//					SearchLocalFile.filepathArrayList.get(j),
//					pview.get(j).get_Auther(),
//					pview.get(j).get_intro(),
//					pview.get(j).get_image_path());	
//			}
		Log.v("book",""+3);
		
		WelcomActivity.EbookdDb.close();
			
		Log.v("book", "add book successfully!");
	}
	
	public void movebook(ArrayList<String> name,ArrayList<String> path,List<PView> vec,int start_id){
		int ddq=(vec.size()>name.size())? name.size():vec.size();
		for (int i = start_id; i < ddq; i++) {
			if (vec.get(i).get_occupy())
				Log.e("book", "error");
			else {
				String[] st=name.get(i).split("\\.");
				if(st[0].length()>5)
					st[0]=st[0].substring(0,5);
				vec.get(i).set_Title(st[0]);
				vec.get(i).set_inner_Visibility(View.VISIBLE);
				vec.get(i).set_occupy(true);
				if (i!=ddq-1) {
					vec.get(i+1).set_occupy(false);	
				}
			}
		}
		EBookShelfActivity.pview_vec.get(ddq).init();
	}
}
