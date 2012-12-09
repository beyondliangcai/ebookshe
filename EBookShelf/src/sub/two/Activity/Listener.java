package sub.two.Activity;

import sub.two.searchlocalfile.MyFile;
import android.R.raw;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Listener {
	private Context context;
	private View view_coll[];
	private int NOT_FOUND=-1;
	//Construct
	public  Listener(Context context) {
		this.context=context;
		init();
	}
	
	public void init(){
		//import layout 
		LayoutInflater inflater = LayoutInflater.from(context);
		View shelfview=inflater.inflate(R.layout.shelf, null);
		Log.v("book", "1");
		// the shelf in main
		View view1=shelfview.findViewById(R.id.shelf1);       //****************为什么词句话的view为空
		//Log.v("book", "1 "+view1.getId());
		//LayoutInflater.from(context).inflate(view1.getId(), null);
		//TextView tv1=(TextView)view1.findViewById(R.id.tv_in_shelf);
		/*LinearLayout view2=(LinearLayout)shelfview.findViewById(R.id.shelf2);
		LinearLayout view3=(LinearLayout)shelfview.findViewById(R.id.shelf3);
		Log.v("book", "12");
		//the textview in each shelf
		
		TextView tv2=(TextView)view2.findViewById(R.id.tv_in_shelf);
		TextView tv3=(TextView)view3.findViewById(R.id.tv_in_shelf);
		Log.v("book", "name");
		tv1.setText("最近阅读：");
		tv2.setText("魔幻：");
		tv3.setText("武侠：");
		Log.v("book", "123");
		// find the hline in each shelf
		//View hlineView=inflater.inflate(R.layout.hline, null);
        //
		View v1=view1.findViewById(R.id.orgin_hline);
		View v2=view2.findViewById(R.id.orgin_hline);
		View v3=view3.findViewById(R.id.orgin_hline);
		Log.v("book", "1234");
		view_coll=new View[]{
				v1.findViewById(R.id.IV1),v1.findViewById(R.id.IV2),v1.findViewById(R.id.IV3),
				v2.findViewById(R.id.IV1),v2.findViewById(R.id.IV2),v2.findViewById(R.id.IV3),
				v3.findViewById(R.id.IV1),v3.findViewById(R.id.IV2),v3.findViewById(R.id.IV3)};*/
		}
	
	//login listener
    OnClickListener  LogClickListener=new OnClickListener() {
    	@Override
    	public void onClick(View arg0) {
    		Log.v("book", "login button");
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
			Log.v("book", "search button");
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
			Log.v("book", "book store");
		}
	};
	//主界面3栏的pview事件监听
	OnClickListener pview_onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int i=compare(v);
			if (i!=-1)
				Log.v("book", "pview click on :"+((i/3)+1)+","+i%3);
			
			View view=LayoutInflater.from(context).inflate(R.layout.readanddetail, null);
			ImageView im1=(ImageView)view.findViewById(R.id.read);
			ImageView im2=(ImageView)view.findViewById(R.id.detail);
			// read book
			im1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			//jump to detail page
			im2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	};//here is the end of listener
	
	//if the given view is one of the 9 ,return its number
	//else return not found;
	public int compare(View v){
		for(int i=0;i<9;i++){
			if(view_coll[i]==v)
				return i+1;
		}
		return NOT_FOUND;
	}
	
    //imageview 事件处理
    public void click_event(View target){
    	LinearLayout parent=(LinearLayout)target.getParent();
    	Log.v("book", "pview click"+",parent id is "+parent.getChildCount());
    	switch (target.getId()) {
		case R.id.IV1:
		case R.id.IV2:
		case R.id.IV3:
			
			break;
		default:Log.v("ebook", "wrong");
			break;
		}
    }
}
