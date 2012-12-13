package sub.two.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB extends SQLiteOpenHelper {

	final String create_db_sqlString="create table BookDB (id integer primary key," +
			"title varchar(50)," +
			"path varchar(50)," +
			"auther varchar(50)," +
			"intro varchar(255)," +
			"pic varchar(100))";
	public MyDB(Context context, String name ,int version) {
		super(context, name,null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase EbookDb) {
		// TODO Auto-generated method stub
		
		EbookDb.execSQL(create_db_sqlString);
		System.out.println("book has fund sucess!");
    
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		

	}
	public  void insertdata(SQLiteDatabase EbookDb ,int id,String title,String path,
			String auther ,String intro,String pic) {
		System.out.println("insert ....!");
		EbookDb.execSQL("delete from BookDB ");
		EbookDb.execSQL("insert into BookDB values("+id+",?,?,?,?,?)",new String[]{title,path,auther,intro,pic});
	    System.out.println("insert sucess!");
	}

}
