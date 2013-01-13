package sub.two.WebviewBrowser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import sub.two.Activity.R;

import sub.two.WebviewBrowser.HelpTabAct;
import sub.two.WebviewBrowser.HistoryBean;
import sub.two.WebviewBrowser.HttpData;
import sub.two.WebviewBrowser.SQLiteHelper;
import sub.two.WebviewBrowser.WriteFavoriteXml;

import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.webkit.DownloadListener;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity 
{
	
	private WebView mWebView = null;    
    final Activity context = this;   
    private SQLiteHelper mOpenHelper; 
    public static Cursor myCursor_one;
    public Intent directCall;
    private WriteFavoriteXml writeXml = new WriteFavoriteXml();
    private ImageButton btn = null;
  
    private ImageButton forwardBtn = null;
    private ImageButton backBtn = null;
    private ListView list = null;
    private Button go_back = null;
 
    private GridView menuGrid, toolbarGrid;
    private AlertDialog menuDialog;
    private View menuView;
    private String script ="function load_night(){	document.body.background= 'black';	} ";
    private boolean isMore = false;// menu菜单翻页控制
  
	private final static int HTTP_ITEM = 1;	//关于
	private final static int SHORTCUT_ITEM = 2;	//快捷方式
	
	
	private final static int PREFERENCE_ITEM = 5;		//帮助网页
		//退出
	private final int ITEM_SEARCH = 0;// 搜索
	private final int ITEM_FILE_MANAGER = 1;// 文件管理
	private final int ITEM_DOWN_MANAGER = 2;// 下载管理
	private final int ITEM_FULLSCREEN = 3;// 全屏
	  private final  int HISTORY_ITEM =4;	//历史记录
	  private final  int FAVORITE_ITEM = 5;	//收藏夹
	  private final  int ADD_FAVORITE = 6;	//加入收藏夹
	  private final  int SHARE_PAGE=7; 
	  private final  int EXIT_ITEM = 8;	
	  private final  int DAY_NIGHT=9; 
	  private final  int REFRESH=10;
	private final int ITEM_MORE = 11;// 菜单

	/*-- Toolbar底部菜单选项下标--*/
	private final int TOOLBAR_ITEM_PAGEHOME = 0;// 首页
	private final int TOOLBAR_ITEM_BACK = 1;// 退后
	private final int TOOLBAR_ITEM_FORWARD = 2;// 前进
	private final int TOOLBAR_ITEM_NEW = 3;// 创建
	private final int TOOLBAR_ITEM_MENU = 4;// 菜单
	/** 菜单图片 **/
	int[] menu_image_array = { R.drawable.menu_search,
			R.drawable.menu_filemanager, R.drawable.menu_downmanager,
			R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
			R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
			R.drawable.menu_sharepage, R.drawable.menu_quit,
			R.drawable.menu_nightmode, R.drawable.menu_refresh,
			R.drawable.menu_more };
	/** 菜单文字 **/
	String[] menu_name_array = { "搜索", "文件管理", "下载管理", "全屏", "历史", "书签",
			"加入书签", "分享页面", "退出", "夜间模式", "刷新", "更多" };
	/** 菜单图片2 **/
	int[] menu_image_array2 = { R.drawable.menu_auto_landscape,
			R.drawable.menu_penselectmodel, R.drawable.menu_page_attr,
			R.drawable.menu_novel_mode, R.drawable.menu_page_updown,
			R.drawable.menu_checkupdate, R.drawable.menu_checknet,
			R.drawable.menu_refreshtimer, R.drawable.menu_syssettings,
			R.drawable.menu_help, R.drawable.menu_about, R.drawable.menu_return };
	/** 菜单文字2 **/
	String[] menu_name_array2 = { "自动横屏", "笔选模式", "阅读模式", "浏览模式", "快捷翻页",
			"检查更新", "检查网络", "定时刷新", "设置", "帮助", "关于", "返回" };

	/** 底部菜单图片 **/
	int[] menu_toolbar_image_array = { R.drawable.controlbar_homepage,
			R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable, R.drawable.controlbar_window,
			R.drawable.controlbar_showtype_list };
	/** 底部菜单文字 **/
	String[] menu_toolbar_name_array = { "首页", "后退", "前进", "创建", "菜单" };

	
	
	
	// private String cur_url = "http://192.168.132.50:8080/EBookShelf/";
	// private String first_page = "http://192.168.132.50:8080/EBookShelf/";
	
	
    private String cur_url = "http://apk.hiapk.com/";	
    private String first_page="http://apk.hiapk.com/";	
	List<Map<String, Object>> history_data = new ArrayList<Map<String, Object>>();
	List<HistoryBean> xml_data = new ArrayList<HistoryBean>();	
	String[] dialog_data = new String[]{};
	public int selectId = 0;	
	SharedPreferences sp;
	Drawable drawable;	
	private static String SAVE_KEY = "save-view";
	
	public static Main instance;

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.w("debug.onCreate","onCreate");
	        requestWindowFeature(Window.FEATURE_PROGRESS);//让进度条显示在标题栏上 
	        
	        mOpenHelper = new SQLiteHelper(this);	     
	        onInit();	
	        instance = this;	    
	        
		    if (savedInstanceState == null) 		        
		    {
		    	deleteTable();
	        } 
	        else 
	        {
	            Bundle map = savedInstanceState.getBundle(SAVE_KEY);
	            if (map != null) 
	            {
	                restoreState(map);
	            }
	        }
	 }
	
    private void onInit() {
        setContentView(R.layout.browsermainlayout);
   //	 mWebView.loadUrl(cur_url);
        menuView = View.inflate(this, R.layout.gridview_menu, null);
        menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** 监听menu选项 **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case ITEM_SEARCH:// 搜索

					break;
				case ITEM_FILE_MANAGER:// 文件管理

					break;
				case ITEM_DOWN_MANAGER:// 下载管理

					break;
				case ITEM_FULLSCREEN:// 全屏
					
					break;
				case HISTORY_ITEM:
					menuDialog.dismiss();
				goto_history_view();
				break;
				case FAVORITE_ITEM:
					menuDialog.dismiss();
					open_favorite();
					break;
				case ADD_FAVORITE:
					menuDialog.dismiss();
					add_favorite();
					break;
				case SHARE_PAGE:
					menuDialog.dismiss();
					break;
				case EXIT_ITEM:
					menuDialog.dismiss();
					dialog();
					break;
				case DAY_NIGHT:
					WebSettings webSettings = mWebView.getSettings(); 
					webSettings.setJavaScriptEnabled(true);  
					mWebView.loadUrl("javascript:document.body.background='black'");
					menuDialog.dismiss();
				
				
					
					break;
					
				case REFRESH:
					menuDialog.dismiss();
				    mWebView.reload();
				break;
				case ITEM_MORE:// 翻页
					if (isMore) {
						menuGrid.setAdapter(getMenuAdapter(menu_name_array2,
								menu_image_array2));
						isMore = false;
					} else {// 首页
						menuGrid.setAdapter(getMenuAdapter(menu_name_array,
								menu_image_array));
						isMore = true;
					}
					menuGrid.invalidate();// 更新menu
					menuGrid.setSelection(ITEM_MORE);
					break;
				}
				
				
			}
		});

        	

        	mWebView = (WebView) findViewById(R.id.wv1); 

     
        	// 创建底部菜单 Toolbar
    		toolbarGrid = (GridView) findViewById(R.id.GridView_toolbar);
    		toolbarGrid.setBackgroundResource(R.drawable.channelgallery_bg);// 设置背景
    		toolbarGrid.setNumColumns(5);// 设置每行列数
    		//toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
    		toolbarGrid.setVerticalSpacing(10);// 垂直间隔
    		toolbarGrid.setHorizontalSpacing(10);// 水平间隔
    		toolbarGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array,
    				menu_toolbar_image_array));// 设置菜单Adapter
    		/** 监听底部菜单选项 **/
    		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    					long arg3) {
    				
    				switch (arg2) {
    				case TOOLBAR_ITEM_PAGEHOME:
    					mWebView.loadUrl(first_page);
    					break;
    				case TOOLBAR_ITEM_BACK:
    					if(mWebView.canGoBack())
    	            		mWebView.goBack();
    					break;
    				case TOOLBAR_ITEM_FORWARD:
    					if(mWebView.canGoForward())
    	            		mWebView.goForward();
    					break;
    				case TOOLBAR_ITEM_NEW:
       
    					break;
    				case TOOLBAR_ITEM_MENU:
    					menuDialog.show();
    					break;
    				}
    			}
    		});
    		
//        	forwardBtn = (ImageButton)findViewById(R.id.forward_btn);
//
//        	backBtn = (ImageButton)findViewById(R.id.back_btn);
//        
    
    
        
//        forwardBtn.setOnClickListener( new Button.OnClickListener()
//        {
//            public void onClick( View v )
//            {
//                // TODO Auto-generated method stub
//            	if(mWebView.canGoForward())
//            		mWebView.goForward();
//            }
//        } );
//        backBtn.setOnClickListener( new Button.OnClickListener()
//        {
//            public void onClick( View v )
//            {
//                // TODO Auto-generated method stub
//            	if(mWebView.canGoBack())
//            		mWebView.goBack();
//            }
//        } );
//      
        mWebView.setWebViewClient(new WebViewClient(){     
        	public boolean shouldOverrideUrlLoading(WebView  view, String url) {     
        		
        		cur_url = url;
        		int filesize;
        		try {
        			InputStream inStream=null;
					URL path=new URL(url);
					
					
					HttpURLConnection connection=(HttpURLConnection) path.openConnection();
					connection.setDoInput(true);
					
					if (connection.getContentType().equals("application/x-msdownload")) {
						
						
						filesize=connection.getContentLength();					
						 inStream=connection.getInputStream();	
						 byte[] buffer=new byte[1024];
						BufferedInputStream bufferedInputStream=new BufferedInputStream(inStream);
						bufferedInputStream.read(buffer);
						
						 System.out.println( new String (buffer));
						File localFile=new File("/mnt/sdcard/Ebookdir/test.txt");
							RandomAccessFile file=new RandomAccessFile(localFile, "rw");							
							int hasread=0;							
							while ((hasread=inStream.read(buffer))!=-1) {
								file.write(buffer,0,hasread);
							}
							inStream.close();
						
					}
					else {
						
						mWebView.loadUrl(url);  
					}
				
					
				
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		setTitle();
        		//insertTable(url,1,mWebView.getTitle());
        		return true;     
        	}     
        	}); 
        
        mWebView.setWebChromeClient(new WebChromeClient() {   
            public void onProgressChanged(WebView view, int progress) {   
              //Activity和Webview根据加载程度决定进度条的进度大小   
             //当加载到100%的时候 进度条自动消失   
              context.setProgress(progress * 100); 
              if(progress>=100)
              {
            	  insertTable(cur_url,1,view.getTitle());
              }
              //Log.d("TTTTTTTTT",progress+","+view.getTitle());
            }           
        	});   
        
        
        
        
        //下载事件
        mWebView.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype, long contentLength) {
				// TODO Auto-generated method stub
				Intent downIntent=new Intent();
				downIntent.setClass(context, DownloadActivity.class);
		           
		            startActivity(downIntent);  
			}
		});
        	mWebView.loadUrl(cur_url);        
        	setTitle();

        Log.i("debug.Init",cur_url);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
    	 Log.i("onCreateContextMenu",v.toString());
    	 Log.i("onCreateContextMenu",String.valueOf(v.getId()));
    }   

    
    private void setTitle()
    {
    	Bitmap bitmap = mWebView.getFavicon();
        drawable = new BitmapDrawable(bitmap);     
        //edit.setCompoundDrawables(drawable, null, null, null);
        drawable = this.getResources().getDrawable(R.drawable.history);
        
        //edit.setMaxLines(1);
    }
 
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//    	super.onCreateOptionsMenu(menu);
//    	menu.add(0, HISTORY_ITEM, HISTORY_ITEM, R.string.history).setIcon(R.drawable.history); //setIcon,setText
//    	menu.add(0, HTTP_ITEM, HTTP_ITEM, R.string.http_name).setIcon(R.drawable.about);
//    	
//    	menu.add(0, ADD_FAVORITE, ADD_FAVORITE, R.string.addFavorite).setIcon(R.drawable.add_favorite);
//    	menu.add(0, FAVORITE_ITEM, FAVORITE_ITEM, R.string.favorite).setIcon(R.drawable.favorite);
//    	menu.add(1, PREFERENCE_ITEM, PREFERENCE_ITEM, R.string.preference).setIcon(R.drawable.help);
//    	return true;
//    }
//    
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//    	switch(item.getItemId()){
//    		case HISTORY_ITEM:
//    			goto_history_view();
//    			break;
//    		case HTTP_ITEM:
//    			showDialog(HTTP_ITEM);
//    			break;
//    		
//    		case ADD_FAVORITE:
//    			add_favorite();
//    			break;
//    		case FAVORITE_ITEM:
//    			open_favorite();
//    			break;
//    		case PREFERENCE_ITEM:
//    			goto_help_act();
//    			break;
//    		default:
//    			break;
//    	}
//    	return super.onOptionsItemSelected(item);
//    }
    
    private void goto_history_view()
    {
    	getHistory();
    	
    	setContentView(R.layout.history);
    	list = (ListView)findViewById(R.id.list);
        go_back = (Button)findViewById(R.id.go_back);
        
        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                android.R.layout.simple_list_item_2, new String[] {"网页","网址"},
                new int[] { android.R.id.text1 , android.R.id.text2});

//    	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                		android.R.layout.simple_list_item_1, history_data);
        
    	list.setAdapter(adapter);
    	go_back.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	onInit();
            }
        } );
    	list.setOnItemClickListener(new OnItemClickListener() 
    	{
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
            {
                cur_url = history_data.get(position).get("网址").toString();
                onInit();
            }
        });
    }
    public void copyHistoryData(WebBackForwardList mylist)
    {
    	int i;  	
        for (i=0;i<mylist.getSize();i++)
        {
        	Map<String, Object> item = new HashMap<String, Object>();
        	item.put("网页", mylist.getItemAtIndex(i).getTitle());
            item.put("网址", mylist.getItemAtIndex(i).getUrl());
            history_data.add(item);

            //history_data.add(mylist.getItemAtIndex(i).getUrl().toString()); //查看浏览器历史
        }
    }
    private List<Map<String, Object>> getData()
    { 
    	return history_data; 
    } 
    
    protected Dialog onCreateDialog(int id) //只在第一次创建时调用
	{	
		if(id == FAVORITE_ITEM)
		{
    		return new AlertDialog.Builder(Main.this)
            .setTitle(R.string.fav_name)
             .setSingleChoiceItems(dialog_data, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	selectId = whichButton;
                    }
                })
            .setNeutralButton(R.string.open_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	if(selectId>=0)
                    	{
                    		cur_url = xml_data.get(selectId).getURL();
                    		
                    		mWebView.loadUrl(cur_url);
                    	}
                    }
                })
            .setPositiveButton(R.string.del_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	if(selectId>=0)
                    	writeXml.Write(context,"history.xml",writeXml.deleteElement(dialog_data[selectId]));
                		selectId = 0;
                		removeDialog(FAVORITE_ITEM);
                    }
                })
            .setNegativeButton(R.string.close_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	removeDialog(FAVORITE_ITEM);
                    }
                })
            .create();
		}
		if(id == HTTP_ITEM)
		{
            return new AlertDialog.Builder(Main.this)
                .setTitle(R.string.http_name)
                .setItems(R.array.http_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();   
                            intent.setClass(Main.this, HttpData.class);   
                            Bundle b = new Bundle();   
                            b.putInt("id", which+1);
                            b.putString("url", cur_url);
                            intent.putExtras(b);   
                            startActivity(intent);   
                      
                    }
                })
                .create();
		}
		
		if(id == EXIT_ITEM)
		{
	            return new AlertDialog.Builder(Main.this)
	                .setIcon(R.drawable.icon)
	                .setTitle(R.string.exit_title)
	                .setMessage(R.string.exit_message)
	                .setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	finish();
	                    }
	                })
	                .setNegativeButton(R.string.no_btn, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	
	                    }
	                })
	                .create();
		}
		return null;
	}
    
    /* 往表中插入数据 */
	private void insertTable(String url, int time, String title) 
	{
		time = (int)Math.floor(System.currentTimeMillis() / 1000 );
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		myCursor_one = db.rawQuery("SELECT * FROM "+SQLiteHelper.TB_NAME + " where name=?" , new String[]{String.valueOf(title)});
		String sql;
		String tip;
		if (myCursor_one.moveToFirst()) 
		{
			sql = "update " + SQLiteHelper.TB_NAME 
			+ " set " + HistoryBean.TIME + "=" + time + " where " + HistoryBean.NAME + "='" + title + "'";
			Log.i("update",title);
			tip = "更新";
		}
		else
		{
		 sql = "insert into " + SQLiteHelper.TB_NAME 
			+ " (" + HistoryBean.URL + ", " + HistoryBean.TIME + ", " + HistoryBean.NAME + ") " 
			+ "values('"+url+"','"+time+"','"+title+"');";
		 	Log.i("insert",title);
		 	tip = "插入";
		}
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Toast.makeText(Main.this, tip+"记录出错", Toast.LENGTH_LONG).show();
			return;
		}
				
		
	}
	/* 删除过时的历史记录 */
	private void deleteTable() 
	{
		int time = (int)Math.floor(System.currentTimeMillis() / 1000 ) - 86400;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String sql = "delete from " + SQLiteHelper.TB_NAME 
			+ " where "+ time + ">" + HistoryBean.TIME;
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			Toast.makeText(Main.this, "删除记录出错", Toast.LENGTH_LONG).show();
		}	
	}
	
	private void getHistory() 
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		myCursor_one = db.rawQuery("SELECT * FROM "+SQLiteHelper.TB_NAME, null);
		int url = myCursor_one.getColumnIndex(HistoryBean.URL);
		int name = myCursor_one.getColumnIndex(HistoryBean.NAME);
		history_data.clear();
		if (myCursor_one.moveToFirst()) 
		{
			do {
				Map<String, Object> item = new HashMap<String, Object>();
	        	item.put("网页", myCursor_one.getString(name));
	            item.put("网址", myCursor_one.getString(url));
	            history_data.add(item);
				//history_data.add(myCursor_one.getString(url));
			} while (myCursor_one.moveToNext());
		}
		myCursor_one.close();  	
	}
	
	
	
	private void add_favorite()
	{
		String name = mWebView.getTitle();
    	String url = mWebView.getUrl();
    	if(name!=""&&url!="")
    	{     
    		writeXml.Write(context,"history.xml",writeXml.insertElement(name,url));
    		writeXml.onReadXml();
    		dialog_data = writeXml.getDialogData();
    		xml_data = writeXml.getXmlData();
    		showDialog(FAVORITE_ITEM);
    	}
	}
	
	private void open_favorite()
	{
		writeXml.onReadXml();
		dialog_data = writeXml.getDialogData();
		xml_data = writeXml.getXmlData();
		showDialog(FAVORITE_ITEM);
	}
	
	private void goto_help_act()
	{
		Intent intent = new Intent();
		intent.setClass(context, HelpTabAct.class);
		startActivity(intent);
	}
	
	public void setBlockImage(boolean flag)
	{
		Log.e("setBlockImage",flag==true?"true":"false");
		WebSettings webSettings = mWebView.getSettings(); 
		webSettings.setBlockNetworkImage(flag);  
	}	
	public void setCacheMode(boolean flag)
	{
		Log.e("setCacheMode",flag==true?"true":"false");
		WebSettings webSettings = mWebView.getSettings(); 
		if(flag)
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
		else
			webSettings.setCacheMode(webSettings.LOAD_NO_CACHE);
	}
	public void setJavaScript(boolean flag)
	{
		Log.e("setJavaScript",flag==true?"true":"false");
		WebSettings webSettings = mWebView.getSettings(); 
		webSettings.setJavaScriptEnabled(flag);  
	}
	
	@Override protected void onResume() {
        super.onResume();
        Log.w("debug.onResume","onResume");
    }
    @Override protected void onSaveInstanceState(Bundle outState) {
    	outState.putBundle(SAVE_KEY, saveState());
    	Log.w("debug.onSaveInstanceState","onSaveInstanceState");
    }
    @Override protected void onPause() {
        super.onPause();
        Log.w("debug.onPause","onPause");
    }	
    @Override protected void onStart() {
        super.onStart();
        Log.w("debug.onStart","onStart");
    }
    @Override protected void onRestart() {
        super.onRestart();
        Log.w("debug.onRestart","onRestart");
    }
    @Override protected void onStop() {
        super.onStop();
        Log.w("debug.onStop","onStop");
    }
    @Override protected void onDestroy() {
    	showDialog(EXIT_ITEM);
        super.onDestroy();
        Log.w("debug.onDestroy","onDestroy");
    } 
    
    public void restoreState(Bundle icicle) 
    {
    	cur_url = icicle.getString("URL");
    	mWebView.loadUrl(cur_url);
    	setTitle();
    }
    
    public Bundle saveState()
    {
    	Bundle map = new Bundle();
    	map.putString("URL", cur_url);
        return map;
    }
    
    @Override  
    public void onBackPressed() 
    {  
        dialog();  
    } 
    protected void dialog() 
    {  
        AlertDialog.Builder builder = new Builder(Main.this);  
        builder.setIcon(R.drawable.exit);
        builder.setTitle(R.string.exit_title);
        builder.setMessage(R.string.exit_message);
        builder.setPositiveButton(R.string.ok_btn,  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
               finish(); 
            }  
        });  
        builder.setNegativeButton(R.string.no_btn,  
        new android.content.DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        });  
        builder.create().show();  
    }  
    
    /**
	 * 构造菜单Adapter
	 * 
	 * @param menuNameArray
	 *            名称
	 * @param imageResourceArray
	 *            图片
	 * @return SimpleAdapter
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}



}
