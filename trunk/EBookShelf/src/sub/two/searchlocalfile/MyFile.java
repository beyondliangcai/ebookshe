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
  
  private List<String> items = null;   //items�������ʾ������
  private List<String> paths = null;   //paths������ļ�·��
  private List<String> sizes = null;   //sizes���ļ���С
  private String rootPath="/";         //rootPath����ʼ�ļ���
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
  protected final static int MENU_ADD =    Menu.FIRST;           //�½��ļ�/�ļ���
  protected final static int MENU_SET =    Menu.FIRST + 1;       //����
  protected final static int MENU_ABOUT =  Menu.FIRST + 2;       //����  
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
    /* ���MENU */
    
    menu.add(Menu.NONE, MENU_ADD, 0, R.string.dirAddButton).setIcon(R.drawable.file);
    menu.add(Menu.NONE, MENU_SET, 0, R.string.dirSetButton).setIcon(R.drawable.set);
    menu.add(Menu.NONE, MENU_ADDFILE, 0, R.string.dirAddfileButton).setIcon(R.drawable.add);//����һ�����ͼƬ    ������
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
   * ��д���ؼ�����:������һ���ļ���
   */
  @Override  
  public boolean onKeyDown(int keyCode,KeyEvent event) {   
      // �Ƿ񴥷�����Ϊback��   
      if (keyCode == KeyEvent.KEYCODE_BACK) {
          path_edit=(EditText)findViewById(R.id.path_edit);  
          File file = new File(path_edit.getText().toString());
          if(rootPath.equals(path_edit.getText().toString())){
            return super.onKeyDown(keyCode,event); 
          }else{
            getFileDir(file.getParent());
            return true; 
          }
      // �������back��������Ӧ 
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
    	File path = Environment.getExternalStorageDirectory();// ���SD��·��  
        //  File path = new File("/mnt/sdcard/");  
         File[] files = path.listFiles();// ��ȡ  
        
         str=path_edit.getText().toString();              
        getFileName2(files);     
		System.out.println(localfile);    
		 path_edit.setText(localfile);
		    items = new ArrayList<String>();
		    paths = new ArrayList<String>();
		    sizes = new ArrayList<String>();
		    File f = new File(localfile);  
		  
		  
		    /* ʹ���Զ����MyAdapter�������ݴ���ListActivity */
		   // getFileDir(localfile);
		    File f2=f.getParentFile();
		    if (localfile=="") {
		  //   Toast.makeText(MyFile.this,"�Ҳ������ļ�,��ȷ���ļ����Ƿ���ȷ!",Toast.LENGTH_SHORT).show();
			Builder nofileBuilder=new AlertDialog.Builder(MyFile.this);
			nofileBuilder.setTitle("����");
			nofileBuilder.setMessage("�Ҳ������ļ�,��ȷ���ļ����Ƿ���ȷ,�������룡!");
			nofileBuilder.setPositiveButton("ȷ��", new OnClickListener() {
				
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
   * ����ListItem�����ʱҪ���Ķ���
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
   * ����ListItem������ʱҪ���Ķ���
   */
  @Override
  public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3){
	  
    File file = new File(paths.get(arg2));
    fileOrDirHandle(file,"long");
    return true;
  }

  /**
   * �����ļ�����Ŀ¼�ķ���
   * @param file
   * @param flag
   */
  private void fileOrDirHandle(final File file,String flag){
    /* ����ļ�ʱ��OnClickListener */
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
      String[] list_file = {"����","�ƶ�","������","ɾ��"};        //file����
        /* ѡ��һ���ļ�����Ŀ¼ʱ������Ҫ��δ����ļ���ListDialog */
        new AlertDialog.Builder(MyFile.this)
            .setTitle(file.getName()).setIcon(R.drawable.list)
            .setItems(list_file,listener_list)
            .setPositiveButton("ȡ��",
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
   * ȡ���ļ��ṹ�ķ���
   * @param filePath
   */
  private void getFileDir(String filePath){
    /* ����Ŀǰ����·�� */
    path_edit.setText(filePath);
    items = new ArrayList<String>();
    paths = new ArrayList<String>();
    sizes = new ArrayList<String>();
    File f = new File(filePath);  
    File[] files = f.listFiles();
    if(files!=null){
    /* �������ļ����ArrayList�� */
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
    /* ʹ���Զ����MyAdapter�������ݴ���ListActivity */
    setListAdapter(new FileListAdapter(this,items,paths,sizes,isZoom));
  }
  
  /**
   *  �½��ļ��л����ļ�
   */
  private void newDirOrFile(){
    AlertDialog nameDialog = new AlertDialog.Builder(MyFile.this).create();
    nameDialog.setTitle("�½��ļ����ļ���");
    LayoutInflater factory = LayoutInflater.from(MyFile.this);
    /* ��ʼ��myChoiceView��ʹ��new_alertΪlayout */
    myView = factory.inflate(R.layout.new_alert,null);
    new_textView = (TextView)myView.findViewById(R.id.new_view);
    rb_dir=(RadioButton)myView.findViewById(R.id.newdir_radio);
    rb_file =(RadioButton)myView.findViewById(R.id.newfile_radio);
    radioGroup = (RadioGroup)myView.findViewById(R.id.new_radio);
    myEditText=(EditText)myView.findViewById(R.id.new_edit);
    path_edit =(EditText)findViewById(R.id.path_edit);     //��ǰ����·��
    /* ��ԭʼ�ļ����ȷ���EditText�� */
    nameDialog.setView(myView);
    //��ѡ��Ťѡ��
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId){
          if(checkedId == rb_file.getId()){
            new_textView.setText("�½��ļ�:");
          }else if(checkedId == rb_dir.getId()){
            new_textView.setText("�½��ļ���:");
          }
        }
    });
    
    /* �½��ļ��е�ȷ����ʾ */
    nameDialog.setButton("ȷ��", 
      new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
        final int checkedId = radioGroup.getCheckedRadioButtonId();               //ѡ����ļ��л����ļ����
        final String newName = myEditText.getText().toString();                   //ȡ�ô������ļ��л����ļ���
        final String newPath = path_edit.getText().toString()+"/"+newName;        //�µ��ļ��л����ļ�·��
        final File f_new =new File(newPath);
          if(f_new.exists()){
            Toast.makeText(MyFile.this,"ָ���ļ�'"+newName+"'�������ļ�����,��ָ����һ����!",Toast.LENGTH_LONG).show();
            return;
          }else{
                  new AlertDialog.Builder(MyFile.this)
                  .setTitle("ע��").setIcon(R.drawable.alert)
                  .setMessage("ȷ������"+((checkedId==rb_dir.getId())?"�ļ���":"�ļ�")+"'"+newName+"' ��?")
                  .setPositiveButton("ȷ��",
                   new DialogInterface.OnClickListener(){
                      public void onClick(DialogInterface dialog,int which){
                        if(checkedId == rb_dir.getId()){
                            if(MyUtil.checkDirPath(newPath)){
                              if(f_new.mkdirs()){
                                Toast.makeText(MyFile.this,"�Ѵ���!",Toast.LENGTH_SHORT).show();
                                getFileDir(f_new.getParent());
                              }else{
                                Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                              }
                            }else{
                              Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                          if(MyUtil.checkFilePath(newPath)){
                              if(newFile(f_new)){
                                Toast.makeText(MyFile.this,"�Ѵ���!",Toast.LENGTH_SHORT).show();
                                getFileDir(f_new.getParent());
                              }else{
                                Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                              }
                          }else{
                            Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
                          }
                        }
                      }
                  })
                  .setNegativeButton("ȡ��",
                   new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                    }
                  }).show();
          }
      }});
      nameDialog.setButton2("ȡ��",
          new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
            }
          });
      nameDialog.show();
   }
  
  /**
   * �޸��ļ��������ļ�����
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
        /* ȡ���޸ĺ���ļ�·�� */
        final String modName = myEditText.getText().toString();             //ȡ���޸ĵ��ļ���
        final String pFile = f_old.getParentFile().getPath()+"/";           //ȡ�ø��ļ�·��
        final String newPath = pFile+modName;                               //�µ��ļ�·��+�ļ���
        final File f_new = new File(newPath);
        if(f_new.exists()){
            if(!modName.equals(f_old.getName())){
                 Toast.makeText(MyFile.this,"ָ���ļ�'"+modName+"'�������ļ�����,��ָ����һ����!",Toast.LENGTH_SHORT).show();
            }else{
                 Toast.makeText(MyFile.this,"����δ�޸�!",Toast.LENGTH_SHORT).show();
            }
        }else{
              new AlertDialog.Builder(MyFile.this)
              .setTitle("ע��").setIcon(R.drawable.alert)
              .setMessage("ȷ��Ҫ�޸�"+(f_old.isDirectory()?"�ļ���'":"�ļ�'")+f_old.getName()+"'����Ϊ'"+modName+"'��?")
              .setPositiveButton("ȷ��",
              new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                  if(f_old.isDirectory()){
                      if(MyUtil.checkDirPath(newPath)){
                          if(f_old.renameTo(f_new)){
                             Toast.makeText(MyFile.this,"���޸�!",Toast.LENGTH_SHORT).show();
                             /* ���²����ļ��б��ListView��������ʾ��һ���ļ������ļ� */
                             getFileDir(pFile);
                          }else{
                             Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                          }
                      }else{
                        Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
                      }
                  }else{
                    if(MyUtil.checkFilePath(newPath)){
                        if(f_old.renameTo(f_new)){
                          Toast.makeText(MyFile.this,"���޸�!",Toast.LENGTH_SHORT).show();
                          getFileDir(pFile);
                        }else{
                          Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                      Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
                    }
                  }
                }
              })
              .setNegativeButton("ȡ��",
               new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                }
              }).show();
      }};
    };
    
    /* ���ø����ļ������ȷ�����Listener */
    AlertDialog renameDialog = new AlertDialog.Builder(MyFile.this).create();
    renameDialog.setView(myView);
    renameDialog.setButton("ȷ��",listenerFileEdit);
    renameDialog.setButton2("ȡ��",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    renameDialog.show();
  }
   
  /**
   * �����ļ������ļ���
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
          Toast.makeText(MyFile.this,"ָ���ļ�'"+f_new.getName()+"'�������ļ�����,��ָ����һ����!",Toast.LENGTH_SHORT).show();
        }else{
          new AlertDialog.Builder(MyFile.this)
          .setTitle("ע��").setIcon(R.drawable.alert)
          .setMessage("ȷ��Ҫ��"+(f_old.isDirectory()?"�ļ���":"�ļ�")+"'"+f_old.getName()+"'���Ƶ�'"+f_new.getParent()+"'��?")
          .setPositiveButton("ȷ��",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
              if(f_old.isDirectory()){
                if(MyUtil.checkDirPath(f_new.getPath())){
                    if(copyDir(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"�Ѹ���!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
                }
              }else{
                if(MyUtil.checkFilePath(f_new.getPath())){
                    if(copyFile(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"�Ѹ���!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                    }
               }else{
                 Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
               }
            }
          }
          })
          .setNegativeButton("ȡ��",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
            }
          }).show();
          
        }
      }
    };
    
    //���ø��Ƶ��ȷ�����Dialog 
    AlertDialog copyDialog = new AlertDialog.Builder(MyFile.this).create();
    copyDialog.setView(myView);
    copyDialog.setButton("ȷ��",listenerCopy);
    copyDialog.setButton2("ȡ��",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    copyDialog.show();
  }
  
  /**
   * �ƶ��ļ������ļ���
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
          Toast.makeText(MyFile.this,"ָ���ļ�'"+f_new.getName()+"'�������ļ�����,��ָ����һ����!",Toast.LENGTH_SHORT).show();
        }else{
          new AlertDialog.Builder(MyFile.this)
          .setTitle("ע��").setIcon(R.drawable.alert)
          .setMessage("ȷ��Ҫ��"+(f_old.isDirectory()?"�ļ���":"�ļ�")+"'"+f_old.getName()+"'�ƶ���'"+f_new.getParent()+"'��?")
          .setPositiveButton("ȷ��",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
              if(f_old.isDirectory()){
                if(MyUtil.checkDirPath(f_new.getPath())){
                    if(moveDir(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"���ƶ�!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                  Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
                }
              }else{
                if(MyUtil.checkDirPath(f_new.getPath())){
                    if(moveFile(f_old.getPath(),f_new.getParent())){
                      Toast.makeText(MyFile.this,"���ƶ�!",Toast.LENGTH_SHORT).show();
                      getFileDir(f_new.getParent());
                    }else{
                      Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                  Toast.makeText(MyFile.this,"��������ȷ�ĸ�ʽ(������//)!",Toast.LENGTH_SHORT).show();
                }
              }
            }
          })
          .setNegativeButton("ȡ��",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
            }
          }).show();
        }
      }
    };
    
    //�����ƶ����ȷ�����Dialog
    AlertDialog moveDialog = new AlertDialog.Builder(MyFile.this).create();
    moveDialog.setView(myView);
    moveDialog.setButton("ȷ��",listenerMove);
    moveDialog.setButton2("ȡ��",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    moveDialog.show();
}
  
  /**
   * ɾ���ļ������ļ���
   * @param f
   */
  private void delFileOrDir(File f){
     final File f_del = f;
     new AlertDialog.Builder(MyFile.this).setTitle("ע��").setIcon(R.drawable.alert)
    .setMessage("ȷ��Ҫɾ��"+(f_del.isDirectory()?"�ļ���'":"�ļ�'")+f_del.getName()+"'��?")
    .setPositiveButton("ȷ��",
     new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog,int which){          
        /* ɾ���ļ������ļ��� */
        if(f_del.isDirectory()){
          if(delDir(f_del)){
            Toast.makeText(MyFile.this,"��ɾ��!",Toast.LENGTH_SHORT).show();
            getFileDir(f_del.getParent());
          }else{
            Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
          }
        }else{
          if(delFile(f_del)){
            Toast.makeText(MyFile.this,"��ɾ��!",Toast.LENGTH_SHORT).show();
            getFileDir(f_del.getParent());
          }else{
            Toast.makeText(MyFile.this,"����!",Toast.LENGTH_SHORT).show();
          }
        }
      }
    })
    .setNegativeButton("ȡ��",
     new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    }).show();
  }
  
  /**
   * ����
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
          .setTitle("ע��").setIcon(R.drawable.alert)
          .setMessage("ȷ��Ҫ����������?")
          .setPositiveButton("ȷ��",
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
          .setNegativeButton("ȡ��",
           new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
            }
          }).show();
        }
    };
    
    AlertDialog setDialog = new AlertDialog.Builder(MyFile.this).create();
    setDialog.setView(myView);
    setDialog.setButton("ȷ��",listenerSet);
    setDialog.setButton2("ȡ��",
    new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int which){
      }
    });
    setDialog.show();
}
  
  /**
   * �½��ļ�
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
   * ���Ƶ����ļ�
   * @param oldPath String ԭ�ļ�·�� �磺/xx
   * @param newPath String ���ƺ�·�� �磺/xx/ss
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
      new File(newPath).mkdirs();              //����ļ��в����� �������ļ���
      new File(f_new).createNewFile();         //����ļ������� �������ļ�
      //�ļ�����ʱ
      if (f_old.exists()) { 
        InputStream inStream = new FileInputStream(oldPath); //����ԭ�ļ�
        FileOutputStream fs = new FileOutputStream(f_new);
        byte[] buffer = new byte[1444];
        while ( (byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread; //�ֽ��� �ļ���С
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
   * �����ļ���
   * @param oldPath String ԭ�ļ�·�� �磺/aa/bb   11,22
   * @param newPath String ���ƺ�·�� �磺/ss/cc
   * @return boolean
   */
  public boolean copyDir(String oldPath, String newPath) {
    try {                                                       //Ҫ���Ƶ��ļ���  /aa/bb---[1.txt,rr]
      File f_old = new File(oldPath);
      String d_old = "";                                          
      String d_new = newPath+File.separator+f_old.getName();    //���ļ���·��    ����/cc/dd    תΪ/cc/dd/bb
      new File(d_new).mkdirs();                                 //����ļ��в����� �������ļ���     //����/cc/dd/bb�ļ���
      File [] files = f_old.listFiles();
      for (int i = 0; i < files.length; i++) {
        d_old = oldPath+File.separator+files[i].getName();      //Ҫ���Ƶ��ļ����µ��ļ���/aa/bb/1.txt,�ļ��У�/aa/bb/rr
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
   * �ƶ��ļ���ָ��Ŀ¼
   * @param oldPath String �磺/fqf.txt
   * @param newPath String �磺/xx/fqf.txt
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
   * �ƶ��ļ��е�ָ��Ŀ¼
   * @param oldPath String �磺/xx
   * @param newPath String �磺/cc/xx
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
   * ɾ�������ļ�
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
   * ɾ���ļ���
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
      f.delete();    //ɾ�����ļ���
      ret = true; 
      }
    }
    catch (Exception e) {
      return false;
    }
    return ret;
  }
  
  /**
   * ���ļ�
   * @param f
   */
  private void openFile(File f){
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(android.content.Intent.ACTION_VIEW);
    //�����б�ѡ�� 
    String type = "*/*";
    if(isOpen==0){
      type = MyUtil.getMIMEType(f,true); 
    }
    //����intent��file��MimeType
    intent.setDataAndType(Uri.fromFile(f),type);
    startActivity(intent); 
  }
  
  /**
   * ����
   */
  private void about(){
    new AlertDialog.Builder(MyFile.this).setTitle("����")
    .setMessage("�������beyondliangcai��� лл֧�֡�")
    .setPositiveButton("ȷ��",
     new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog,int which){          
      }
    }).show();
  }
  /**
   * �ڵ������������鼮
   */
  private void addfile() {
		// TODO Auto-generated method stub
	  
		Builder addfileBuilder=new AlertDialog.Builder(MyFile.this);
		addfileBuilder.setTitle("����鼮");
		
	  TextView addfileTextView=new TextView(this);
	 File addFile=new File(addpath);
	 if (addFile.isDirectory()) {
		 addfileTextView.setText("��ȷ��Ҫ���"+addpath+"��������ļ���?");
	}
	else if (addFile.isFile()) {
		addfileTextView.setText("��ȷ��Ҫ���"+addpath+"�ļ���?");
	}
	else if (addpath=="") {
		
		addfileTextView.setText("��ѡ����Ҫ��ӵ��ļ��л��ļ�");
	}
	  addfileTextView.setPadding(5, 5, 5, 5);
	  addfileTextView.setTextSize(20);
	 
		addfileBuilder.setView(addfileTextView);
	final	Intent intenttoEbookshell=new Intent();
		intenttoEbookshell.setClass(MyFile.this, EBookShelfActivity.class);
		addfileBuilder.setPositiveButton("ȷ��", new OnClickListener() {
		final	Builder errorBuilder=new AlertDialog.Builder(MyFile.this);
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO ��ӱ����鼮

				try {
					//SearchLocalFile.count=0;	
					
					Intent intent1=new Intent();
					intent1.setClass(MyFile.this, SearchLocalFile.class);
					startService(intent1);
					final Builder confirmBuilder=new AlertDialog.Builder(MyFile.this);
					confirmBuilder.setTitle("ȷ����Ϣ");	
					TextView confirmTextView=new TextView(MyFile.this);
				//	System.out.println(SearchLocalFile.count);
					confirmTextView.setText("���Ѿ����������鼮���Ƿ񷵻����?");
					confirmBuilder.setView(confirmTextView);
					
					confirmBuilder.setPositiveButton("ȷ��", new OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub			
							finish();
						}
					});
					confirmBuilder.setNegativeButton("ȡ��", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					confirmBuilder.show();
				} catch (Exception e) {
					// TODO: handle exception
					
				
					errorBuilder.setTitle("����:");
					TextView errormessage=new TextView(MyFile.this);
					errormessage.setText("��ȷ����������ļ����ļ�����Ȩ�ޣ���");
					errorBuilder.setView(errormessage);
					errorBuilder.setPositiveButton("ȷ��", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					errorBuilder.show();
				}
				
				
				
			}
		});
		addfileBuilder.setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		addfileBuilder.show();
	}

  
}
