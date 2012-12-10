package sub.two.Activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import sub.two.PersonalView.PView;
import android.view.View;
import android.widget.ScrollView;

public class ManageBook {
	static List<PView> pview_vec=new ArrayList<PView>();
		
	public ManageBook(ScrollView sv){
		init(sv);
	}
	public void init(ScrollView sv){
		//shelf1
		View shelf=sv.findViewById(R.id.shelf1);
		View hr1=shelf.findViewById(R.id.orgin_hline);
		PView view=(PView)hr1.findViewById(R.id.IV1);
		pview_vec.add(view);
		PView view2=(PView)hr1.findViewById(R.id.IV2);
		pview_vec.add(view2);
		PView view3=(PView)hr1.findViewById(R.id.IV3);
		pview_vec.add(view3);
		//shelf2
		View shelf2=sv.findViewById(R.id.shelf2);
		View hr2=shelf2.findViewById(R.id.orgin_hline);
		PView view4=(PView)hr2.findViewById(R.id.IV1);
		pview_vec.add(view4);
		PView view5=(PView)hr2.findViewById(R.id.IV2);
		pview_vec.add(view5);
		PView view6=(PView)hr2.findViewById(R.id.IV3);
		pview_vec.add(view6);

		for (int i = 0; i < pview_vec.size(); i++) {
			pview_vec.get(i).setVisibility(View.INVISIBLE);
			pview_vec.get(i).setoccupy(false);
		}
	}
	
	public void addbook(ArrayList<String> name,ArrayList<String> path){
		for (int i = 0; i < name.size(); i++) {
			PView view=pview_vec.get(i);
			if (i==pview_vec.size())
				break;
			if(!view.getoccupy()){
				view.setVisibility(View.VISIBLE);
				view.setTitle(name.get(i));
				view.setoccupy(true);
			}
		}
	}
}
