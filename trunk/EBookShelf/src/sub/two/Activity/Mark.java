package sub.two.Activity;

import sub.two.PersonalView.PButton;
import sub.two.PersonalView.PView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Mark extends Activity{
	private PButton history,book_mark,back_in_mark;
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.markmain);
		init();
	}
	
	public void init(){
		//
		history=(PButton)findViewById(R.id.history_mark);
		history.setBitMap(R.drawable.blank);
		history.set_text("历史");
		history.set_text_color(Color.WHITE);
		history.set_text_size(20);
		//
		book_mark=(PButton)findViewById(R.id.book_mark);
		book_mark.setBitMap(R.drawable.tab_selected);
		book_mark.set_text("书签");
		book_mark.set_text_color(Color.WHITE);
		book_mark.set_text_size(20);
		//
		back_in_mark=(PButton)findViewById(R.id.back_to_shelf_in_mark);
		back_in_mark.set_text("返回");
		back_in_mark.setBitMap(R.drawable.back_to_shelf);
		back_in_mark.set_tv_padding(10, 0);
		back_in_mark.set_text_size(16);
		back_in_mark.set_text_color(Color.WHITE);
		back_in_mark.setPadding(5, 7, 0, 0);
		back_in_mark.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		  });
	}
}
