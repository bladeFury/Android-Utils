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
				Toast.makeText(Main.this,"��װ�ɹ���", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(Main.this,"��װʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
		url=Environment.getExternalStorageDirectory()+"/QQBrowser/��װ��/MyCards.apk";
		Log.d("213124124124", url);
	}

	public boolean isRooted() {
    	//����Ƿ�ROOT��
    	DataInputStream stream;
        boolean flag=false;
		try {
			stream = Terminal("ls /data/");
			//Ŀ¼�Ķ��У���һ��Ҫ��ҪROOTȨ�޵�
			if(stream.readLine()!=null)flag=true;
			//�����Ƿ��з������ж��Ƿ���rootȨ��
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
 
		}
 
    	return flag;
    }
	
	public DataInputStream Terminal(String command) throws Exception
    {
        Process process = Runtime.getRuntime().exec("su");
        //ִ�е��⣬Superuser����������ѡ���Ƿ������ȡ���Ȩ��
        OutputStream outstream = process.getOutputStream();
        DataOutputStream DOPS = new DataOutputStream(outstream);
        InputStream instream = process.getInputStream();
        DataInputStream DIPS = new DataInputStream(instream);
        String temp = command + "\n";
        //�ӻس�
        DOPS.writeBytes(temp);
        //ִ��
        DOPS.flush();
        //ˢ�£�ȷ�������͵�outputstream
        DOPS.writeBytes("exit\n");
        //�˳�
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
	
	
	
	

