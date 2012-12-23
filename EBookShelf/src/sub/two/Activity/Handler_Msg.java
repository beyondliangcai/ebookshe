package sub.two.Activity;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import sub.two.DB.MyDB;
import sub.two.PersonalView.PView;
import sub.two.Service.SearchLocalFile;
import sub.two.searchlocalfile.searchfile;
import android.R.integer;
import android.R.string;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Handler_Msg extends Handler{
	
	public static List<PView> pview=new ArrayList<PView>();
	public static String INTENT_READBOOK="sub.two.myintent.read_book";
	public static String BOOKPATH="BOOK_PATH";
	public static String NORMAL_STATE="书城";
	public static String EDIT_STATE="完成";
	
	public static int TRUE=-95;
	public static int FLASE=-94;
	public int MOVE_BOOK=-93;
	public static int ADD_BOOK_WHILE_START=-92;
	public static int CHECK_SHELF=-91;
	public static int EDIT_FINISHED=-90;
	public static int AT_EDIT_MODE=0;
	
	private SearchLocalFile sea1;
	private List<String> s1=new ArrayList<String>();
	private static int SHELF_NAME=20121200; 
	
	public Handler_Msg() {
		
	}

	@Override
	public void handleMessage(Message msg){
		//ADD_BOOK_WHILE_START
		if(msg.what==ADD_BOOK_WHILE_START){
//			Log.v("book", "add book while the app starts");
			if(SearchLocalFile.filenameArrayList.size()>EBookShelfActivity.pview_vec.size()) {
				add_shelf(SearchLocalFile.filenameArrayList,EBookShelfActivity.pview_vec);
			}
			addbook(SearchLocalFile.filenameArrayList,
					SearchLocalFile.filepathArrayList, EBookShelfActivity.pview_vec);
		}
		
		//add book
		if(msg.what==SearchLocalFile.ADD_BOOK){		
			//WelcomActivity.EbookdDb.insertdata( WelcomActivity.EbookdDb.getReadableDatabase(),i+1, filenameArrayList.get(i),filepathArrayList.get(i), null, null, null);	
			Log.v("book", "handle add message!");
			if (EBookShelfActivity.pview_vec.size()==0) {
				return;
			}
			Bundle bundle=(Bundle)msg.obj;
			
			if(SearchLocalFile.filenameArrayList.size()>EBookShelfActivity.pview_vec.size()) {
				Log.v("book", "more shelf is needed!");
				add_shelf(bundle.getStringArrayList(SearchLocalFile.File_Name),EBookShelfActivity.pview_vec);
			}
        	addbook(bundle.getStringArrayList(SearchLocalFile.File_Name),
        			bundle.getStringArrayList(SearchLocalFile.File_Path), EBookShelfActivity.pview_vec);
		}
		
		//move
		if(msg.what==MOVE_BOOK){
			int i=msg.arg1;
			try {
				Thread.sleep(100);
				movebook(SearchLocalFile.filenameArrayList, SearchLocalFile.filepathArrayList,EBookShelfActivity.pview_vec, i);
				Message.obtain(Handler_Msg.this,CHECK_SHELF).sendToTarget();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//delete book
		if (msg.what==PView.DELETE_BOOK) {
			final int i=msg.arg1;
//			Log.d("book", "delete id:"+i);
			if(EBookShelfActivity.pview_vec.get(i)!=null){
				AlertDialog.Builder a=new AlertDialog.Builder(EBookShelfActivity.context);
				a.setTitle("");
				a.setMessage("是否删除源文件？");
				a.setPositiveButton("是",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						delete_book(EBookShelfActivity.pview_vec, i);
						EBookShelfActivity.pview_vec.get(i).init();
						SearchLocalFile.filenameArrayList.remove(i);
						SearchLocalFile.filepathArrayList.remove(i);
						//修改数据库
						WelcomActivity.EbookdDb.deletealldata(WelcomActivity.EbookdDb.getReadableDatabase());
						for (int j = 0; j < SearchLocalFile.filenameArrayList.size(); j++) {				
							WelcomActivity.EbookdDb.insertdata(WelcomActivity.EbookdDb.getReadableDatabase(),
								j,
								SearchLocalFile.filenameArrayList.get(j),
								SearchLocalFile.filepathArrayList.get(j),
								null,
								null,
								null);
						
						}

						Log.v("book", "list size after delete:"+SearchLocalFile.filenameArrayList.size());
						WelcomActivity.EbookdDb.close();
						
						Message.obtain(Handler_Msg.this, MOVE_BOOK, i, SearchLocalFile.filenameArrayList.size()).sendToTarget();
						
						AT_EDIT_MODE--;
						Log.v("book", "AT_EDIT_MODE:"+AT_EDIT_MODE);
						if (AT_EDIT_MODE==0) {
							try {
								Thread.sleep(100);
								book_store_state();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
				
				a.setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						EBookShelfActivity.pview_vec.get(i).init();
						SearchLocalFile.filenameArrayList.remove(i);
						SearchLocalFile.filepathArrayList.remove(i);
						//修改数据库
						WelcomActivity.EbookdDb.deletealldata(WelcomActivity.EbookdDb.getReadableDatabase());
						for (int j = 0; j < SearchLocalFile.filenameArrayList.size(); j++) {				
							WelcomActivity.EbookdDb.insertdata(WelcomActivity.EbookdDb.getReadableDatabase(),
								j,
								SearchLocalFile.filenameArrayList.get(j),
								SearchLocalFile.filepathArrayList.get(j),
								null,
								null,
								null);
						
						}

						Log.v("book", "list size after delete:"+SearchLocalFile.filenameArrayList.size());
						WelcomActivity.EbookdDb.close();
						
						Message.obtain(Handler_Msg.this, MOVE_BOOK, i, SearchLocalFile.filenameArrayList.size()).sendToTarget();
						
						AT_EDIT_MODE--;
						Log.v("book", "AT_EDIT_MODE:"+AT_EDIT_MODE);
						if (AT_EDIT_MODE==0) {
							try {
								Thread.sleep(100);
								book_store_state();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					});
				a.show();
				}
			}
		
		//read book
		if (msg.what==PView.READ_BOOK) {
			int i=msg.arg1;
			Log.d("book", "handle read message!");
			try {
				Log.v("book", EBookShelfActivity.pview_vec.get(i).get_path().toString());
			} catch (Exception e) {
				// TODO: handle exception
				Log.v("book","null pointer exception "+i);
			}
//			Bundle bundle=new Bundle();
//			bundle.putString(BOOKPATH, EBookShelfActivity.pview_vec.get(i).get_path());
//			Intent intent=new Intent(INTENT_READBOOK);
//			intent.putExtras(bundle);
//			intent.setClassName("com.wust.txt", "TxtBookActivity");
//			EBookShelfActivity.context.startActivity(intent);
		}
		
		//edit book
		if (msg.what==PView.EDIT_BOOK) {
			Log.d("book", "handle edit message!");
			int i=msg.arg1;
			}
		
		//change book state
		if (msg.what==PView.CHANGE_BOOK_STATE) {
			int id=msg.arg1;
			AT_EDIT_MODE++;
			Log.v("book", "AT_EDIT_MODE:"+AT_EDIT_MODE);
			try {
//				Log.v("book", "long click, longclick_mode: "+EBookShelfActivity.longclick_mode);
				EBookShelfActivity.pview_vec.get(id).set_editing(true);
				EBookShelfActivity.pview_vec.get(id).set_outer_Visibility(View.VISIBLE);
				if (!EBookShelfActivity.longclick_mode) {
					Log.v("book", "change button state");
					EBookShelfActivity.book_store.setBitMap(R.drawable.change_mode);
					EBookShelfActivity.book_store.set_text(EDIT_STATE);
					EBookShelfActivity.book_store.set_text_color(Color.RED);
					EBookShelfActivity.longclick_mode=true;
				}
				} catch (Exception e) {
					// TODO: handle exception
					Log.v("book", "failed id:"+id);
					}
			}
		
		//check if the shelf is empty
		if (msg.what==CHECK_SHELF) {
			int k=EBookShelfActivity.SHELF_COUNT;
//			Log.v("book", "shelf count:"+k); 
			if(k>3){
				LinearLayout layout=(LinearLayout)(EBookShelfActivity.sv).findViewById(R.id.main_shelf);
				for (int i = k; i >3; i--) {
					if(!EBookShelfActivity.pview_vec.get((i-1)*3).get_occupy()){
//						Log.v("book", "remove shelf at:"+i);
						View view=EBookShelfActivity.sv.findViewById(SHELF_NAME+i);
						EBookShelfActivity.shelf_show.remove(i-1);
						EBookShelfActivity.pview_vec.remove(3*EBookShelfActivity.SHELF_COUNT-1);
						EBookShelfActivity.pview_vec.remove(3*EBookShelfActivity.SHELF_COUNT-2);
						EBookShelfActivity.pview_vec.remove(3*EBookShelfActivity.SHELF_COUNT-3);
						EBookShelfActivity.ID_COUNT-=3;
						layout.removeView(view);
						EBookShelfActivity.SHELF_COUNT--;
					}
				}
			}
		}
	
		//edit finished
		if (msg.what==EDIT_FINISHED) {
			for (int i = 0; i < EBookShelfActivity.pview_vec.size(); i++) {
				EBookShelfActivity.pview_vec.get(i).set_outer_Visibility(View.GONE);
				EBookShelfActivity.pview_vec.get(i).set_editing(false);
			}
			book_store_state();
			AT_EDIT_MODE=0;
		}
	}
	 
	public void addbook(ArrayList<String> name,ArrayList<String> path,List<PView> vec){	
//		int ddq=(vec.size()>name.size())? name.size():vec.size();
//		Log.d("book", "vec size:"+vec.size()+"          list size:"+name.size());
		int temp=0;
		if(name.size()>vec.size()){
			Log.v("book", "failed , add book has encounted an error");
			return ;
		}
		Log.v("book", path.toString());
	
		for (int i = 0; i < vec.size(); i++) {
			vec.get(i).init();
		}
		
		WelcomActivity.EbookdDb.deletealldata(WelcomActivity.EbookdDb.getReadableDatabase());
		
		for (int i = 0; i < name.size(); i++) {
			if(!vec.get(i).get_occupy()){
				vec.get(i).set_inner_Visibility(View.VISIBLE);
				vec.get(i).set_Title(create_title(SearchLocalFile.filenameArrayList,EBookShelfActivity.pview_vec,i));
				vec.get(i).set_path(path.get(temp));
				vec.get(i).set_occupy(true);	
				}		
			temp++;
		}		
		for (int j = 0; j < SearchLocalFile.filenameArrayList.size();j++) {
			WelcomActivity.EbookdDb.insertdata(WelcomActivity.EbookdDb.getReadableDatabase(),
					j,
					SearchLocalFile.filenameArrayList.get(j),
					SearchLocalFile.filepathArrayList.get(j),
					null,
					null,
					null);	
			}
		
		WelcomActivity.EbookdDb.close();
			
	
	}

	public void movebook(ArrayList<String> name,ArrayList<String> path,List<PView> vec,int start_id){
		int ddq=(vec.size()>name.size())? name.size():vec.size();
//		Log.d("book", "move:"+ddq);
		for (int i = start_id; i < ddq; i++) {
			Boolean flag=vec.get(i+1).get_editing();
			if (vec.get(i).get_occupy())
				Log.e("book", "error mode book");
			else {
				if (flag) {
					vec.get(i).set_outer_Visibility(View.VISIBLE);
					vec.get(i).set_editing(true);
				}
				vec.get(i).set_Title(create_title(SearchLocalFile.filenameArrayList,EBookShelfActivity.pview_vec,i));
				vec.get(i).set_inner_Visibility(View.VISIBLE);
				vec.get(i).set_path(path.get(i));
				vec.get(i).set_occupy(true);
				if (i!=ddq-1)
					vec.get(i+1).set_occupy(false);				
			}
		}
		try {
			EBookShelfActivity.pview_vec.get(ddq).init();
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			Log.e("book", "IndexOutOfBoundsException:"+ddq);
		}
	}

	public void add_shelf(ArrayList<String> name,List<PView> vec) {
		
		int i=name.size()-vec.size();
		int k=i%3;
//		Log.v("book", "list - vec = "+i);
		LinearLayout linearLayout=(LinearLayout)(EBookShelfActivity.sv).findViewById(R.id.main_shelf);
		
		while(k!=0){
			k=(++i)%3;
		}
		for (int j = 0; j < i/3; j++) {
			View view=LayoutInflater.from(EBookShelfActivity.context).inflate(R.layout.shelf, null);
			view.setId(SHELF_NAME+(++EBookShelfActivity.SHELF_COUNT));
			linearLayout.addView(view);
			
			View shelf=EBookShelfActivity.sv.findViewById(SHELF_NAME+EBookShelfActivity.SHELF_COUNT);
			EBookShelfActivity.shelf_show.add((LinearLayout)shelf);
			
			View hr=shelf.findViewById(R.id.orgin_hline);
			
			PView view1=(PView)hr.findViewById(R.id.IV1);
			view1.set_id(EBookShelfActivity.ID_COUNT++);
			vec.add(view1);
			
			PView view2=(PView)hr.findViewById(R.id.IV2);
			view2.set_id(EBookShelfActivity.ID_COUNT++);
			vec.add(view2);
			
			PView view3=(PView)hr.findViewById(R.id.IV3);
			view3.set_id(EBookShelfActivity.ID_COUNT++);
			vec.add(view3);	
		}
	}
	
	public String create_title(ArrayList<String> name,List<PView> vec,int i){
		String[] st=name.get(i).split("\\.");
		String s=st[0];
		String result=null;
		
		if (s.getBytes().length==s.length()) {
			int q=s.length();
			if (q>0&&q<=7) {
				result=s;
			}
			if (q>7&&q<=14) {
				vec.get(i).set_tv_paddingtop(20);
				String s1=s.substring(0,7);
				String s2="\n";
				String s3=s.substring(7);
				result=s1+s2+s3;
			}
			if (q>14) {
				vec.get(i).set_tv_paddingtop(20);
				String s1=s.substring(0,7);
				String s2="\n";
				String s3=s.substring(7,12);
				String s4="..";
				result=s1+s2+s3+s4;
			}
		}
		else {
			int q=s.length();
			if (q>0&&q<=4) {
				result=s;
			}
			
			if (q>4&&q<=8) {
				vec.get(i).set_tv_paddingtop(20);
				String s1=s.substring(0,4);
				String s2="\n";
				String s3=s.substring(4);
				result=s1+s2+s3;
			}
			if (q>8) {
				vec.get(i).set_tv_paddingtop(20);
				String s1=s.substring(0,4);
				String s2="\n";
				String s3=s.substring(4,8);
				String s4="..";
				result=s1+s2+s3+s4;
			}
		}
		return result;
	}
	
	public void delete_book(List<PView> vec,int i){
		String string = vec.get(i).get_path();
		if(string==null){
			Log.v("book", "vec path is null");
		}
		else {
			File file=new File(string);
			if (file.exists()) {
				file.delete();
				Log.v("book", "file delete!");
			}
			else {
				Log.v("book", "file mot exists!");
			}
		}
	}
	
	public void book_store_state(){
		EBookShelfActivity.book_store.set_text(NORMAL_STATE);
		EBookShelfActivity.book_store.set_text_color(Color.WHITE);
		EBookShelfActivity.book_store.setBitMap(R.drawable.button_mode);
		EBookShelfActivity.longclick_mode=false;
	}
}
