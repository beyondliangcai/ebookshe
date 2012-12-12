package sub.two.PersonalView;

import sub.two.Activity.login;
import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class PView extends FrameLayout implements OnClickListener , OnLongClickListener{

	private String title="书名";
	private String path =null;
	private String auther="佚名";
	private String intro="暂无";
	private Bitmap pic=null;
	protected int id=-1;
	private int rc[]=new int[]{-1,-1};
	private ImageView iv;
	private TextView tv;
	private Boolean occupy=false;
	private int new_pview=-2;
	
	public PView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate(){
		iv=(ImageView)findViewById(sub.two.Activity.R.id.iv_in_personalview);
		tv=(TextView)findViewById(sub.two.Activity.R.id.tv_in_personalview);
		iv.setOnClickListener(this);
		iv.setOnLongClickListener(this);
		tv.setClickable(false);
		//Log.v("book", "inflate finish");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		View view=(View)v.getParent();
		rc=RC(v);
//		if (view!=null){
//			Log.v("book", rc[0]+","+rc[1]);
//			Log.e("book", "occupy:"+occupy);
//			Log.d("book", title.toString());
//			}
		
	}
	
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		Log.d("book", "long click");
		return false;
	}
	//判断书籍所在行列
	public int[] RC(View v){
		int[] rac=new int[]{-1,-1};
		int parent_id=((View)(v.getParent())).getId();
		int gparent_id=((View)(v.getParent().getParent().getParent())).getId();
		rac[0]=row(gparent_id);
		rac[1]=colunm(parent_id);
		return rac;
	}
	//书籍所在行 gp
	public int row(int id) {
		switch (id) {
		case sub.two.Activity.R.id.shelf1:
			return 1;
		case sub.two.Activity.R.id.shelf2:
			return 2;
		default:
			return -1;
		}
	}
	//书籍所在列 p
	public int colunm(int id) {
		switch (id) {
		case sub.two.Activity.R.id.IV1:
			return 1;
		case sub.two.Activity.R.id.IV2:
			return 2;
		case sub.two.Activity.R.id.IV3:
			return 3;
		default:
			return -1;
		}
	}
	
	public void set_occupy(Boolean b) {
		occupy=b;
	}
	
	public void set_BitMap(Bitmap pic) {
		iv.setImageBitmap(pic);
	}
	
	public void set_Title(String f) {
		title=f;
		tv.setText(title);
		tv.invalidate();
		//Log.v("book", "refresh");
	}
	
	public void set_path(String pa) {
		path=pa;
	}
	
	public void set_Auther(String a){
		auther=a;
	}
	
	public void set_Intro(String s){
		intro=s;
	}
	
	public Boolean get_occupy(){
		return occupy;
	}
	
	public String get_Title(){
		return title;
	}
}
