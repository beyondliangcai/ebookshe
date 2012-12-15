package sub.two.PersonalView;

import sub.two.Activity.Handler_Msg;
import sub.two.Activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class PView extends FrameLayout implements OnClickListener , OnLongClickListener{

	private String title;
	private String path ;
	private String auther;
	private String intro;
	private Bitmap pic;
	private Boolean occupy=false;
	private String pic_path;
	private int id;
	
	private int rc[]=new int[]{-1,-1};
	
	private ImageView iv;
	private TextView tv;
	private ImageView delete_book;
	private ImageView edit_book;
	
	public static int DELETE_BOOK=-99;
	public static int READ_BOOK=-98;
	public static int EDIT_BOOK=-97;
	public static int CHANGE_BOOK_STATE=-96;
	
	public static Handler h1;
	
	public PView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate(){
		iv=(ImageView)findViewById(sub.two.Activity.R.id.iv_in_personalview);
		tv=(TextView)findViewById(sub.two.Activity.R.id.tv_in_personalview);
		delete_book=(ImageView)findViewById(sub.two.Activity.R.id.delete_book);
		edit_book=(ImageView)findViewById(sub.two.Activity.R.id.edit_book);
		
		iv.setOnClickListener(this);
		iv.setOnLongClickListener(this);
		
		delete_book.setOnClickListener(this);
		edit_book.setOnClickListener(this);
		init();
		FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)edit_book.getLayoutParams();
		params.leftMargin=10;		
		edit_book.setLayoutParams(params);
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Handler_Msg handler_Msg=new Handler_Msg();
		int idd=-100;
		switch (v.getId()) {
		case sub.two.Activity.R.id.iv_in_personalview:
		case sub.two.Activity.R.id.tv_in_personalview:
			idd=READ_BOOK;
			break;
		case sub.two.Activity.R.id.delete_book:
			idd=DELETE_BOOK;
			break;
		case sub.two.Activity.R.id.edit_book:
			idd=EDIT_BOOK;
			break;
		default:
			Log.v("book", "error");
			break;
		}
		Message e=handler_Msg.obtainMessage(idd, id, 0);
		handler_Msg.sendMessage(e);
	}
	
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		Handler_Msg handle=new Handler_Msg();
		int ss=-100;
		switch (v.getId()) {
		case sub.two.Activity.R.id.iv_in_personalview:
		case sub.two.Activity.R.id.tv_in_personalview:
			ss=CHANGE_BOOK_STATE;
//			set_outer_Visibility(View.VISIBLE);
			break;
		default:
			break;
		}
		Message e=handle.obtainMessage(ss, id, 0);
		handle.sendMessage(e);
		return false;
	}
	
	public void init(){
		title="书名";
		path =null;
		auther="佚名";
		intro="暂无";
		pic=null;
		occupy=false;
		
		set_inner_Visibility(View.INVISIBLE);
		set_outer_Visibility(View.GONE);
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
		case sub.two.Activity.R.id.shelf3:
			return 3;
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
	
	public void set_clickable(Boolean b) {
		iv.setClickable(b);
		tv.setClickable(b);
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
	
	public void set_id(int i) {
		id=i;
	}
	
	public void set_pic_path(String qq){
		pic_path=qq;
	}
	
	public void set_inner_Visibility(int visiable){
		tv.setVisibility(visiable);
		iv.setVisibility(visiable);
	}
	
	public void set_outer_Visibility(int visiable){
		delete_book.setVisibility(visiable);
		edit_book.setVisibility(visiable);
	}
	
	public Boolean get_occupy(){
		return occupy;
	}
	
	public String get_Title(){
		return title;
	}

	public int get_id(){
		return id;
	}
	
	public String get_path(){
		return path;
	}
	
	public String get_intro(){
		return intro;
	}
	
	public String get_Auther(){
		return auther;
	}
	
	public String get_image_path(){
		return pic_path;
	}
	
	public int get_delelt_id(){
		return delete_book.getId();
	}
	
	public int get_edit_id(){
		return edit_book.getId();
	}
}
