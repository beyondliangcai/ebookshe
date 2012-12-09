package sub.two.Service;

import sub.two.Activity.R;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class AdaptScreenSize extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent,int startId){
		DisplayMetrics dm=new DisplayMetrics();
		dm=getApplication().getApplicationContext().getResources().getDisplayMetrics();
		float density=dm.density;
		LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout=(LinearLayout)inflater.inflate(R.layout.himageviewcoll, null);
		if(density>240)
			linearLayout.setPadding(5,20,0,25);
		if(density<=160&&density>120)
			linearLayout.setPadding(5,20,0,20);
		Log.v("qq", "from adaptscreensize.....");
	}
}
