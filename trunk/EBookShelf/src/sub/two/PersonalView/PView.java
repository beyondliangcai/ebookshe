package sub.two.PersonalView;

import android.R.string;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class PView extends FrameLayout implements OnClickListener{

	string title=null;
    string auther=null;
	string intro=null;
	int resid=-1;
	int id=-1;
	ImageView iv;
	TextView tv;
	public PView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onFinishInflate(){
		iv=(ImageView)findViewById(sub.two.Activity.R.id.iv_in_personalview);
		tv=(TextView)findViewById(sub.two.Activity.R.id.tv_in_personalview);
		iv.setOnClickListener(this);
		tv.setClickable(false);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		View view=(View)v.getParent();
		if (view!=null) {
			int rc[]=new int[]{-1,-1};
			rc=RC(v);
			Log.v("book", rc[0]+","+rc[1]);
		}
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
}
