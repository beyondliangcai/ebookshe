package sub.two.PersonalView;

import sub.two.Activity.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class PButton extends FrameLayout{
	private ImageView iv;
	private TextView tv;
	private int id;
	
	public PButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onFinishInflate(){
		iv=(ImageView)findViewById(R.id.iv_in_pbutton);
		tv=(TextView)findViewById(R.id.tv_in_pbutton);
	}
	
	 public void set_text(String s) {
		tv.setText(s);
	}
	 
	 public void setBitMap(int resid) {
		iv.setImageResource(resid);
	}
	
	 public void set_id(int qq) {
		id=qq;
	}

	 public int get_id() {
		return id;
	}
	 public void set_text_size(int i){
		 tv.setTextSize(i);
	 }
	 
	 public void set_text_color(int color){
		 tv.setTextColor(color);
	 }
	 
	 public void set_tv_padding(int left,int top) {
		tv.setPadding(left, top, 0, 0);
	}
	 
	 public void set_padding(int left,int top,int right,int bottom) {
		this.setPadding(left, top, right, bottom);
	}
	 
	 public CharSequence get_text(){
		 return tv.getText();
	 }
}
