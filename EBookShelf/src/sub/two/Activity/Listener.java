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
		LayoutInflater inflater = LayoutInflater.from(context);
		View shelfview=inflater.inflate(R.layout.shelf, null);
		View view1=shelfview.findViewById(R.id.shelf1);
		View view2=shelfview.findViewById(R.id.shelf2);
		View view3=shelfview.findViewById(R.id.shelf3);
		/*view_coll=new View[]{
				view1.findViewById(R.id.IV1),view1.findViewById(R.id.IV2),view1.findViewById(R.id.IV3),
				view2.findViewById(R.id.IV1),view2.findViewById(R.id.IV2),view2.findViewById(R.id.IV3),
				view3.findViewById(R.id.IV1),view3.findViewById(R.id.IV2),view3.findViewById(R.id.IV3)};*/
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
			Intent searchIntent=new Intent();
			searchIntent.setClass(context, MyFile.class);
  		    context.startActivity(searchIntent);
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
