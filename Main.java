package com.example.root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener{

	private Button button;
	private Button root;
	private Button open_no;
	private PackageUtils pack;
	private String url;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(Main.this,"安装成功！", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(Main.this,"安装失败！", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button=(Button) findViewById(R.id.button1);
		root=(Button) findViewById(R.id.button2);
		open_no=(Button) findViewById(R.id.button3);
		button.setOnClickListener(this);
		root.setOnClickListener(this);
		open_no.setOnClickListener(this);
		pack=new PackageUtils();
		url=Environment.getExternalStorageDirectory()+"/QQBrowser/安装包/MyCards.apk";
		Log.d("213124124124", url);
	}

	public boolean isRooted() {
    	//检测是否ROOT过
    	DataInputStream stream;
        boolean flag=false;
		try {
			stream = Terminal("ls /data/");
			//目录哪都行，不一定要需要ROOT权限的
			if(stream.readLine()!=null)flag=true;
			//根据是否有返回来判断是否有root权限
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
 
		}
 
    	return flag;
    }
	
	public DataInputStream Terminal(String command) throws Exception
    {
        Process process = Runtime.getRuntime().exec("su");
        //执行到这，Superuser会跳出来，选择是否允许获取最高权限
        OutputStream outstream = process.getOutputStream();
        DataOutputStream DOPS = new DataOutputStream(outstream);
        InputStream instream = process.getInputStream();
        DataInputStream DIPS = new DataInputStream(instream);
        String temp = command + "\n";
        //加回车
        DOPS.writeBytes(temp);
        //执行
        DOPS.flush();
        //刷新，确保都发送到outputstream
        DOPS.writeBytes("exit\n");
        //退出
        DOPS.flush();
        process.waitFor();
        return DIPS;
    }
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			refershView();
			break;
		case R.id.button2:
			PackageUtils.root(this);	
			break;
		default:
			
			Log.d("dfadfsafsdfadf", ""+isRooted());
			break;
		}
	}
	private void refershView() {
			new Thread() {
				public void run() {
					handler.sendEmptyMessage(PackageUtils.install(Main.this, url));
				};
			}.start();
		}
	}
	
	
	
	

