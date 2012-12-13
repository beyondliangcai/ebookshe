package sub.two.searchlocalfile;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import sub.two.Activity.EBookShelfActivity;
import sub.two.Activity.R;
import sub.two.Activity.login;
import sub.two.Service.SearchLocalFile;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyFile extends ListActivity  implements OnItemLongClickListener {
  
  private List<String> items = null;   //items：存放显示的名称
  private List<String> paths = null;   //paths：存放文件路径
  private List<String> sizes = null;   //sizes：文件大小
  private String rootPath="/";         //rootPath：起始文件夹
  private TextView path_edit;
  private View myView;
  private TextView new_textView;
  private EditText myEditText;
  private RadioGroup radioGroup;
  private RadioButton rb_file;
  private RadioButton rb_dir;
  private ImageButton rb_qry;
  private int add_book=-9;
  private int [] flag=new int[100];
  protected final static int MENU_ADD =    Menu.FIRST;           //新建文件/文件夹
  protected final static int MENU_SET =    Menu.FIRST + 1;       //设置
  protected final static int MENU_ABOUT =  Menu.FIRST + 2;       //关于  
  protected final static int MENU_ADDFILE=  Menu.FIRST + 3;
  
  private CheckBox cb_open;
  private CheckBox cb_zoom;
  
  private int id = 0;
  private int isZoom = 0;
  private int isOpen = 0;
  public static String addpath="";
  public String localfile=new  String();
  private String str=new String();
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu){
    super.onCreateOptionsMenu(menu);
    /* 添加MENU */
    
    menu.add(Menu.NONE, MENU_ADD, 0, R.string.dirAddButton).setIcon(R.drawable.file);
    menu.add(Menu.NONE, MENU_SET, 0, R.string.dirSetButton).setIcon(R.drawable.set);
    menu.add(Menu.NONE, MENU_ADDFILE, 0, R.string.dirAddfileButton).setIcon(R.drawable.add);//还差一张添加图片    ！！！
    menu.add(Menu.NONE, MENU_ABOUT,0, R.string.dirAboutButton).setIcon(R.drawable.info);
  
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    super.onOptionsItemSelected(item);
    switch (item.getItemId()){
      case MENU_ADD:
        newDirOrFile();
        break;
      case MENU_SET:
        set();
        break;
      case MENU_ABOUT:
        about();
        break;
       case MENU_ADDFILE:
        	addfile();
        	break;
        
    }
    return true;
  }
  
  
/**
   * 重写返回键功能:返回上一级文件夹
   */
  @Override  
  public boolean onKeyDown(int keyCode,KeyEvent event) {   
      // 是否触发按键为back键   
      if (keyCode == KeyEvent.KEYCODE_BACK) {
          path_edit=(EditText)findViewById(R.id.path_edit);  
          File file = new File(path_edit.getText().toString());
          if(rootPath.equals(path_edit.getText().toString())){
            return super.onKeyDown(keyCode,event); 
          }else{
            getFileDir(file.getParent());
            return true; 
          }
      // 如果不是back键正常响应 
      } else{  
          return super.onKeyDown(keyCode,event); 
      }
  }  

  @Override
  public void onCreate(Bundle savedInstanceState){
	  
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
  //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getWindow().setBackgroundDrawableResource(R.drawable.background);
   
    setContentView(R.layout.filelistlayout);  
    setTheme(R.drawable.background);
    
    for (int i = 0; i < 100; i++) {
		flag[i]=0;
	}
    
    
    path_edit = (EditText)findViewById(R.id.path_edit);
    
    rb_qry = (ImageButton)findViewById(R.id.qry_button);
    rb_qry.setOnClickListener(listener_qry);
    
    getListView().setOnItemLongClickListener(this);
   
    
    getFileDir(rootPath);
  }
  private void getFileName2(File[] files) {      
      if (files != null) {
      	 
          for (File file : files) {  
              if (file.isDirectory()) {         

                  getFileName2(file.listFiles());    
                  
              } 
              else {  
                  String fileName = file.getName();  
                  String localpathString=file.getAbsolutePath();
                  if (fileName.substring(0, fileName.lastIndexOf(".")).equals(str)) {               
                   
                      localfile=localpathString;
                  }  
                  else {
					localfile="";
				}
              }  
          }  
      }  
  }  
  Button.OnClickListener listener_qry = new Button.OnClickListener(){
    public void onClick(View arg0) {
    	File path = Environment.getExternalStorageDirectory();// 获得SD卡路径  
        //  File path = new File("/mnt/sdcard/");  
         File[] files = path.listFiles();// 读取  
        
         str=path_edit.getText().toString();              
        getFileName2(files);     
		System.out.println(localfile);    
		 path_edit.setText(localfile);
		    items = new ArrayList<String>();
		    paths = new ArrayList<String>();
		    sizes = new ArrayList<String>();
		    File f = new File(localfile);  
		  
		  
		    /* 使用自定义的MyAdapter来将数据传入ListActivity */
		   // getFileDir(localfile);
		    File f2=f.getParentFile();
		    if (localfile=="") {
		  //   Toast.makeText(MyFile.this,"找不到该文件,请确定文件名是否正确!",Toast.LENGTH_SHORT).show();
			Builder nofileBuilder=new AlertDialog.Builder(MyFile.this);
			nofileBuilder.setTitle("警告");
			nofileBuilder.setMessage("找不到该文件,请确定文件名是否正确,重新输入！!");
			nofileBuilder.setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			nofileBuilder.show();
		    
		    }
		    else {
		    	getFileDir(f2.getPath());
		    	
			}
		    
    
      
    }      
  };

  /**
   * 设置ListItem被点击时要做的动作
   */
  
  @Override
  
  protected void onListItemClick(ListView l,View v,int position,long id){
//	  if (Math.abs(lastClickTime-System.currentTimeMillis()) < 1000))) {
//		
//	}
	  //l.setCacheColorHint(Color.TRANSPARENT); 	 
	  for (int i = 0; i < 100; i++) {
		  if (i!=position) {
			  flag[i]=0;		
			 
		}			
		}
	 
	  for(int i = 0; i <l.getChildCount(); i++){ 
		 
		 l.getChildAt(i).setSelected(false);
		 l.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
	       //  view.setBackgroundColor(Color.RED);
	     //   view.setBackgroundResource(R.drawable.background);
	  }
	flag[position]++;
	if (flag[position]%2==0) {
		  File file = new File(paths.get(position));
		    fileOrDirHandle(file,"short");
	}
	else {
		v.setSelected(true);
	
		//v.setBackgroundResource(R.drawable.background);
		v.setBackgroundColor(Color.YELLOW);
       addpath=paths.get(position);
	}
  
  }

  /**
   * 设置ListItem被长按时要做的动作
   */
  @Override
  public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3){
	  
    File file = new File(paths.get(arg2));
    fileOrDirHandle(file,"long");
    return true;
  }

  /**
   * 处理文件或者目录的方法
   * @param file
   * @param flag
   */
  private void fileOrDirHandle(final File file,String flag){
    /* 点击文件时的OnClickListener */
    OnClickListener listener_list=new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog,int which){
                if(which==0){
                   copyFileOrDir(file);
                }else if(which==1){
                   moveFileOrDir(file);
                }else if(which==2){
                   modifyFileOrDir(file);
                } else if(which==3){
                   delFileOrDir(file);
                }
    }};
    
    if(flag.equals("long")){
      String[] list_file = {"复制","移动","重命名","删除"};        //file操作
        /* 选择一个文件或者目录时，跳出要如何处理文件的ListDialog */
        new AlertDialog.Builder(MyFile.this)
            .setTitle(file.getName()).setIcon(R.drawable.list)
            .setItems(list_file,listener_list)
            .setPositiveButton("取消",
             new DialogInterface.OnClickListener(){
              public void onClick(DialogInterface dialog, int which){
              }
            }).show();
  }else{
    if(file.isDirectory()){
      getFileDir(file.getPath());
    }else{
      openFile(file);
    }
  }
}  
  
  /**
   * 取得文件结构的方法
   * @param filePath
   */
  private void getFileDir(String filePath){
    /* 设置目前所在路径 */
    path_edit.setText(filePath);
    items = new ArrayList<String>();
    paths = new ArrayList<String>();
    sizes = new ArrayList<String>();
    File f = new File(filePath);  
    File[] files = f.listFiles();
    if(files!=null){
    /* 将所有文件添加ArrayList中 */
        for(int i=0;i<files.length;i++){
          if(files[i].isDirectory()){
            items.add(files[i].getName());
            paths.add(files[i].getPath());
            sizes.add("");
          }
        }
        for(int i=0;i<files.length;i++){
          if(files[i].isFile()){
            items.add(files[i].getName());
            paths.add(files[i].getPath());
            sizes.add(MyUtil.fileSizeMsg(files[i]));
          }
        }
    }
    /* 使用自定义的MyAdapter来将数据传入ListActivity */
    setListAdapter(new FileListAdapter(this,items,paths,sizes,isZoom));
  }
  
  /**
   *  新建文件夹或者文件
   */
  private void newDirOrFile(){
    AlertDialog nameDialog = new AlertDialog.Builder(MyFile.this).create();
    nameDialog.setTitle("新建文件或文件夹");
    LayoutInflater factory = LayoutInflater.from(MyFile.this);
    /* 初始化myChoiceView，使用new_alert为layout */
    myView = factory.inflate(R.layout.new_alert,null);
    new_textView = (TextView)myView.findViewById(R.id.new_view);
    rb_dir=(RadioButton)myView.findViewById(R.id.newdir_radio);
    rb_file =(RadioButton)myView.findViewById(R.id.newfile_radio);
    radioGroup = (RadioGroup)myView.findViewById(R.id.new_radio);
    myEditText=(EditText)myView.findViewById(R.id.new_edit);
    path_edit =(EditText)findViewById(R.id.path_edit);     //当前所在路径
    /* 将原始文件名先放入EditText中 */
    nameDialog.setView(myView);
    //单选按扭选择
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId){
          if(checkedId == rb_file.getId()){
            new_textView.setText("新建文件:");
          }else if(checkedId == rb_dir.getId()){
            new_textView.setText("新建文件夹:");
          }
        }
    });
    
    /* 新建文件夹的确认提示 */
    nameDialog.setButton("确定", 
      new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
        final int checkedId = radioGroup.getCheckedRadioButtonId();               //选择的文件夹或者文件标记
        final String newName = myEditText.getText().toString();                   //取得创建的文件夹或者文件名
        final String newPath = path_edit.getText().toString()+"/"+newName;        //新的文件夹或者文件路径
        final File f_new =new File(newPath);
          if(f_new.exists()){
            Toast.makeText(MyFile.this,"指定文件'"+newName+"'与现有文件重名,请指定另一名称!",Toast.LENGTH_LONG).show();
            return;
          }else{
                  new AlertDialog.Builder(MyFile.this)
                  .setTitle("注意").setIcon(R.drawable.alert)
                  .setMessage("确定创建"+((checkedId==rb_dir.getId())?"文件夹":"文件")+"'"+newName+"' 吗?")
                  .setPositiveButton("确定",
                   new DialogInterface.OnClickListener(){
                      public void onClick(DialogInterface dialog,int which){
                        if(checkedId == rb_dir.getId()){
                            if(MyUtil.checkDirPath(newPath)){
                              if(f_new.mkdirs()){
                                Toast.makeText(MyFile.this,"已创建!",Toast.LENGTH_SHORT).show();
                                getFileDir(f_new.getParent());
                              }else{
                                Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                              }
                            }else{
                              Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                          if(MyUtil.checkFilePath(newPath)){
                              if(newFile(f_new)){
                                Toast.makeText(MyFile.this,"已创建!",Toast.LENGTH_SHORT).show();
                                getFileDir(f_new.getParent());
                              }else{
                                Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                              }
                          }else{
                            Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
                          }
                        }
                      }
                  })
                  .setNegativeButton("取消",
                   new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                    }
                  }).show();
          }
      }});
      nameDialog.setButton2("取消",
          new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
            }
          });
      nameDialog.show();
   }
  
  /**
   * 修改文件名或者文件夹名
   * @param f
   */
  private void modifyFileOrDir(File f) {
    final File f_old = f;
    LayoutInflater factory=LayoutInflater.from(MyFile.this);
    myView=factory.inflate(R.layout.rename_alert,null);
    myEditText=(EditText)myView.findViewById(R.id.rename_edit);
    myEditText.setText(f_old.getName());
    OnClickListener listenerFileEdit = new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
        /* 取得修改后的文件路径 */
        final String modName = myEditText.getText().toString();             //取得修改的文件名
        final String pFile = f_old.getParentFile().getPath()+"/";           //取得该文件路径
        final String newPath = pFile+modName;                               //新的文件路径+文件名
        final File f_new = new File(newPath);
        if(f_new.exists()){
            if(!modName.equals(f_old.getName())){
                 Toast.makeText(MyFile.this,"指定文件'"+modName+"'与现有文件重名,请指定另一名称!",Toast.LENGTH_SHORT).show();
            }else{
                 Toast.makeText(MyFile.this,"名称未修改!",Toast.LENGTH_SHORT).show();
            }
        }else{
              new AlertDialog.Builder(MyFile.this)
              .setTitle("注意").setIcon(R.drawable.alert)
              .setMessage("确定要修改"+(f_old.isDirectory()?"文件夹'":"文件'")+f_old.getName()+"'名称为'"+modName+"'吗?")
              .setPositiveButton("确定",
              new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                  if(f_old.isDirectory()){
                      if(MyUtil.checkDirPath(newPath)){
                          if(f_old.renameTo(f_new)){
                             Toast.makeText(MyFile.this,"已修改!",Toast.LENGTH_SHORT).show();
                             /* 重新产生文件列表的ListView即重新显示上一级文件夹下文件 */
                             getFileDir(pFile);
                          }else{
                             Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                          }
                      }else{
                        Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
                      }
                  }else{
                    if(MyUtil.checkFilePath(newPath)){
                        if(f_old.renameTo(f_new)){
                          Toast.makeText(MyFile.this,"已修改!",Toast.LENGTH_SHORT).show();
                          getFileDir(pFile);
                        }else{
                          Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                      Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
                    }
                  }
                }
              })
              .setNegativeButton("取消",
               new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                }
              }).show();
      }};
    };
    
    /* 设置更改文件名点击确定后的Listener */
    AlertDialog renameDialog = new AlertDialog.Builder(MyFile.this).create();
    renameDialog.setView(myView);
    renameDialog.setButton("确定",listenerFileEdit);
    renameDialog.setButton2("取消",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    renameDialog.show();
  }
   
  /**
   * 复制文件或者文件夹
   * @param file
   */
  public void copyFileOrDir(File f) {
    final File f_old = f;
    LayoutInflater factory = LayoutInflater.from(MyFile.this);
    myView=factory.inflate(R.layout.copy_alert,null);
    myEditText=(EditText)myView.findViewById(R.id.copy_edit);
    myEditText.setText(f.getParent());
    OnClickListener listenerCopy = new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
        String new_path = myEditText.getText().toString();
        if(new_path.endsWith(File.separator)){
          new_path = new_path+f_old.getName();
        } else{
          new_path = new_path+File.separator+f_old.getName();
        }
        final File f_new = new File(new_path);
        if(f_new.exists()){
          Toast.makeText(MyFile.this,"指定文件'"+f_new.getName()+"'与现有文件重名,请指定另一名称!",Toast.LENGTH_SHORT).show();
        }else{
          new AlertDialog.Builder(MyFile.this)
          .setTitle("注意").setIcon(R.drawable.alert)
          .setMessage("确定要把"+(f_old.isDirectory()?"文件夹":"文件")+"'"+f_old.getName()+"'复制到'"+f_new.getParent()+"'吗?")
          .setPositiveButton("确定",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
              if(f_old.isDirectory()){
                if(MyUtil.checkDirPath(f_new.getPath())){
                    if(copyDir(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"已复制!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
                }
              }else{
                if(MyUtil.checkFilePath(f_new.getPath())){
                    if(copyFile(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"已复制!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                    }
               }else{
                 Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
               }
            }
          }
          })
          .setNegativeButton("取消",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
            }
          }).show();
          
        }
      }
    };
    
    //设置复制点击确定后的Dialog 
    AlertDialog copyDialog = new AlertDialog.Builder(MyFile.this).create();
    copyDialog.setView(myView);
    copyDialog.setButton("确定",listenerCopy);
    copyDialog.setButton2("取消",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    copyDialog.show();
  }
  
  /**
   * 移动文件或者文件夹
   * @param file
   */
 
  public void moveFileOrDir(File f) {
    final File f_old = f;
    LayoutInflater factory=LayoutInflater.from(MyFile.this);
   
    //  File path = new File("/mnt/sdcard/");  
   
   
     
    myView=factory.inflate(R.layout.move_alert,null);
    myEditText=(EditText)myView.findViewById(R.id.move_edit);
    myEditText.setText(f_old.getParent());
    OnClickListener listenerMove = new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
        String new_path = myEditText.getText().toString();
        
        if(new_path.endsWith(File.separator)){
          new_path = new_path+f_old.getName();
        } else{
          new_path = new_path+File.separator+f_old.getName();
        }
        final File f_new = new File(new_path);
        if(f_new.exists()){
          Toast.makeText(MyFile.this,"指定文件'"+f_new.getName()+"'与现有文件重名,请指定另一名称!",Toast.LENGTH_SHORT).show();
        }else{
          new AlertDialog.Builder(MyFile.this)
          .setTitle("注意").setIcon(R.drawable.alert)
          .setMessage("确定要把"+(f_old.isDirectory()?"文件夹":"文件")+"'"+f_old.getName()+"'移动到'"+f_new.getParent()+"'吗?")
          .setPositiveButton("确定",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
              if(f_old.isDirectory()){
                if(MyUtil.checkDirPath(f_new.getPath())){
                    if(moveDir(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"已移动!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                  Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
                }
              }else{
                if(MyUtil.checkDirPath(f_new.getPath())){
                    if(moveFile(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"已移动!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                  Toast.makeText(MyFile.this,"请输入正确的格式(不包含//)!",Toast.LENGTH_SHORT).show();
                }
              }
            }
          })
          .setNegativeButton("取消",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
            }
          }).show();
        }
      }
    };
    
    //设置移动点击确定后的Dialog
    AlertDialog moveDialog = new AlertDialog.Builder(MyFile.this).create();
    moveDialog.setView(myView);
    moveDialog.setButton("确定",listenerMove);
    moveDialog.setButton2("取消",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    moveDialog.show();
}
  
  /**
   * 删除文件或者文件夹
   * @param f
   */
  private void delFileOrDir(File f){
     final File f_del = f;
     new AlertDialog.Builder(MyFile.this).setTitle("注意").setIcon(R.drawable.alert)
    .setMessage("确定要删除"+(f_del.isDirectory()?"文件夹'":"文件'")+f_del.getName()+"'吗?")
    .setPositiveButton("确定",
     new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog,int which){          
        /* 删除文件或者文件夹 */
        if(f_del.isDirectory()){
          if(delDir(f_del)){
            Toast.makeText(MyFile.this,"已删除!",Toast.LENGTH_SHORT).show();
            getFileDir(f_del.getParent());
          }else{
            Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
          }
        }else{
          if(delFile(f_del)){
            Toast.makeText(MyFile.this,"已删除!",Toast.LENGTH_SHORT).show();
            getFileDir(f_del.getParent());
          }else{
            Toast.makeText(MyFile.this,"出错!",Toast.LENGTH_SHORT).show();
          }
        }
      }
    })
    .setNegativeButton("取消",
     new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    }).show();
  }
  
  /**
   * 设置
   */
  public void set() {
    LayoutInflater factory=LayoutInflater.from(MyFile.this);
    myView = factory.inflate(R.layout.setview,null);
    cb_open = (CheckBox)myView.findViewById(R.id.checkOpen);
    cb_zoom = (CheckBox)myView.findViewById(R.id.checkZoom);
    if(isZoom == 1){
      cb_zoom.setChecked(true);
    }
    if(isOpen == 1){
      cb_open.setChecked(true);
    }
    OnClickListener listenerSet = new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
          new AlertDialog.Builder(MyFile.this)
          .setTitle("注意").setIcon(R.drawable.alert)
          .setMessage("确定要保存设置吗?")
          .setPositiveButton("确定",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
              if(cb_zoom.isChecked()){
                isZoom = 1;
              }else{
                isZoom = 0;
              }
              if(cb_open.isChecked()){
                isOpen = 1;
              }else{
                isOpen = 0;
              }
              
            }
          })
          .setNegativeButton("取消",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
            }
          }).show();
        }
    };
    
    AlertDialog setDialog = new AlertDialog.Builder(MyFile.this).create();
    setDialog.setView(myView);
    setDialog.setButton("确定",listenerSet);
    setDialog.setButton2("取消",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    setDialog.show();
}
  
  /**
   * 新建文件
   * @param file
   * @return
   */
  public boolean newFile(File f) {
    try {
      f.createNewFile();
    }catch (Exception e){
      return false;
    }
    return true;
  }
  
  /**
   * 复制单个文件
   * @param oldPath String 原文件路径 如：/xx
   * @param newPath String 复制后路径 如：/xx/ss
   * @return boolean
   */
  public boolean copyFile(String oldPath, String newPath) {
    try {
      int bytesum = 0;
      int byteread = 0;
      String f_new = "";
      File f_old = new File(oldPath);
      if(newPath.endsWith(File.separator)){
        f_new = newPath+f_old.getName();
      } else{
        f_new = newPath+File.separator+f_old.getName();
      }
      new File(newPath).mkdirs();              //如果文件夹不存在 则建立新文件夹
      new File(f_new).createNewFile();         //如果文件不存在 则建立新文件
      //文件存在时
      if (f_old.exists()) { 
        InputStream inStream = new FileInputStream(oldPath); //读入原文件
        FileOutputStream fs = new FileOutputStream(f_new);
        byte[] buffer = new byte[1444];
        while ( (byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread; //字节数 文件大小
          fs.write(buffer, 0, byteread);
        }
        inStream.close();
      }
    }catch (Exception e) {
      return false;

    }
     return true;
  }

  /**
   * 复制文件夹
   * @param oldPath String 原文件路径 如：/aa/bb   11,22
   * @param newPath String 复制后路径 如：/ss/cc
   * @return boolean
   */
  public boolean copyDir(String oldPath, String newPath) {
    try {                                                       //要复制的文件夹  /aa/bb---[1.txt,rr]
      File f_old = new File(oldPath);
      String d_old = "";                                          
      String d_new = newPath+File.separator+f_old.getName();    //新文件夹路径    传入/cc/dd    转为/cc/dd/bb
      new File(d_new).mkdirs();                                 //如果文件夹不存在 则建立新文件夹     //建立/cc/dd/bb文件夹
      File [] files = f_old.listFiles();
      for (int i = 0; i < files.length; i++) {
        d_old = oldPath+File.separator+files[i].getName();      //要复制的文件夹下的文件：/aa/bb/1.txt,文件夹：/aa/bb/rr
        if(files[i].isFile()){
          copyFile(d_old,d_new);
        }else {            
          copyDir(d_old,d_new);
        }
      }
    }catch (Exception e) {
      return false;
    }
   return true;
  }

  /**
   * 移动文件到指定目录
   * @param oldPath String 如：/fqf.txt
   * @param newPath String 如：/xx/fqf.txt
   */
  public boolean moveFile(String oldPath, String newPath) {
    boolean ret = false;
    try{
        if(copyFile(oldPath, newPath)){
          new File(oldPath).delete();
          ret = true;
        }
      } catch (Exception e){
        return false;
      }
      return ret;
  }
  
  /**
   * 移动文件夹到指定目录
   * @param oldPath String 如：/xx
   * @param newPath String 如：/cc/xx
   */
  public boolean moveDir(String oldPath, String newPath) {
    boolean ret = false;
    try{
      if(copyDir(oldPath, newPath)){
        if(delDir(new File(oldPath))){
          ret = true;
        }
      }
    } catch (Exception e){
      return false;
    }
    return ret;
 }

  /**
   * 删除单个文件
   * @param file
   * @return
   */
  public boolean delFile(File f) {
    boolean ret  = false;
    try {
      if (f.exists()) {
        f.delete(); 
        ret = true ;
      }
    }
    catch (Exception e) {
      return false;
    }
    return ret;
  }
  
  /**
   * 删除文件夹
   * @param file
   * @return
   */
  public boolean delDir(File f) {
    boolean ret  = false;
    try {
      if (f.exists()) {
        File [] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
          if (files[i].isDirectory()) {
            if(!delDir(files[i])){
               return false;
            }
           }else { 
            files[i].delete();
          }
        }
      f.delete();    //删除空文件夹
      ret = true; 
      }
    }
    catch (Exception e) {
      return false;
    }
    return ret;
  }
  
  /**
   * 打开文件
   * @param f
   */
  private void openFile(File f){
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(android.content.Intent.ACTION_VIEW);
    //跳出列表供选择 
    String type = "*/*";
    if(isOpen==0){
      type = MyUtil.getMIMEType(f,true); 
    }
    //设置intent的file与MimeType
    intent.setDataAndType(Uri.fromFile(f),type);
    startActivity(intent); 
  }
  
  /**
   * 关于
   */
  private void about(){
    new AlertDialog.Builder(MyFile.this).setTitle("关于")
    .setMessage("本软件有beyondliangcai完成 谢谢支持。")
    .setPositiveButton("确定",
     new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog,int which){          
      }
    }).show();
  }
  /**
   * 在电子书架上添加书籍
   */
  private void addfile() {
		// TODO Auto-generated method stub
	  
		Builder addfileBuilder=new AlertDialog.Builder(MyFile.this);
		addfileBuilder.setTitle("添加书籍");
		
	  TextView addfileTextView=new TextView(this);
	 File addFile=new File(addpath);
	 if (addFile.isDirectory()) {
		 addfileTextView.setText("您确定要添加"+addpath+"里的所有文件吗?");
	}
	else if (addFile.isFile()) {
		addfileTextView.setText("您确定要添加"+addpath+"文件吗?");
	}
	else if (addpath=="") {
		
		addfileTextView.setText("请选择您要添加的文件夹或文件");
	}
	  addfileTextView.setPadding(5, 5, 5, 5);
	  addfileTextView.setTextSize(20);
	 
		addfileBuilder.setView(addfileTextView);
	final	Intent intenttoEbookshell=new Intent();
		intenttoEbookshell.setClass(MyFile.this, EBookShelfActivity.class);
		addfileBuilder.setPositiveButton("确定", new OnClickListener() {
		final	Builder errorBuilder=new AlertDialog.Builder(MyFile.this);
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 添加本地书籍

				try {
					//SearchLocalFile.count=0;	
					
					Intent intent1=new Intent();
					intent1.setClass(MyFile.this, SearchLocalFile.class);
					startService(intent1);
					final Builder confirmBuilder=new AlertDialog.Builder(MyFile.this);
					confirmBuilder.setTitle("确认信息");	
					TextView confirmTextView=new TextView(MyFile.this);
				//	System.out.println(SearchLocalFile.count);
					confirmTextView.setText("您已经完成添加了书籍了是否返回书架?");
					confirmBuilder.setView(confirmTextView);
					
					confirmBuilder.setPositiveButton("确定", new OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub			
							finish();
						}
					});
					confirmBuilder.setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					confirmBuilder.show();
				} catch (Exception e) {
					// TODO: handle exception
					
				
					errorBuilder.setTitle("警告:");
					TextView errormessage=new TextView(MyFile.this);
					errormessage.setText("请确定您对这个文件或文件夹有权限！！");
					errorBuilder.setView(errormessage);
					errorBuilder.setPositiveButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					errorBuilder.show();
				}
				
				
				
			}
		});
		addfileBuilder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		addfileBuilder.show();
	}

  
}
