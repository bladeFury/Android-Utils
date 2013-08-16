package com.example.root;

import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.IOException;  
import java.util.ArrayList;  
import java.util.List;  
  
import android.app.Activity;  
import android.os.Bundle;  
import android.util.Log;  
import android.view.View;  
import android.widget.AdapterView;  
import android.widget.AdapterView.OnItemSelectedListener;  
import android.widget.ArrayAdapter;  
import android.widget.Spinner;  
import android.widget.TextView;  
import android.widget.Toast;  
  
public class MainActivity extends Activity {  
      
    private final String TAG = "SetCPU";  
    private List<String> governors;  
      
    private Spinner spinner;  
    private ArrayAdapter<String> adapter;  
    private TextView tv;  
      
    private int curCpuGovernor;  
    private final String cpuFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq";  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
          
        governors = readCpuGovernors();  
        curCpuGovernor = governors.indexOf(readCurCpuGovernor());  
        spinner = (Spinner) findViewById(R.id.spinner1);  
        tv = (TextView) findViewById(R.id.textView1);  
          
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, governors);  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spinner.setAdapter(adapter);  
        spinner.setPrompt("CPU Governors");  
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {  
  
            @Override  
            public void onItemSelected(AdapterView<?> parent, View view,  
                    int position, long id) {  
                Log.i(TAG, "set CPU Governor " + readCurCpuGovernor() + "-> " + governors.get(position));  
                  
                writeCpuGovernor(governors.get(position));  
                if(governors.get(position).equals(readCurCpuGovernor())){  
                    Toast.makeText(MainActivity.this, "write CPU Governor success!", Toast.LENGTH_LONG).show();  
                    curCpuGovernor = governors.indexOf(readCurCpuGovernor());  
                }  
                else{  
                    Toast.makeText(MainActivity.this, "write CPU Governor failed!", Toast.LENGTH_LONG).show();  
                    spinner.setSelection(curCpuGovernor);  
                }  
            }  
  
            @Override  
            public void onNothingSelected(AdapterView<?> parent) {  
                  
            }  
        });  
          
          
    }  
      
    private boolean writeCpuGovernor(String governor)  
    {  
        DataOutputStream os = null;  
        byte[] buffer = new byte[256];  
        String command = "echo " + governor  + " > " + cpuFreqPath + "/scaling_governor";  
        Log.i(TAG, "command: " + command);  
        try {  
            Process process = Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes(command + "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
            Log.i(TAG, "exit value = " + process.exitValue());  
        } catch (IOException e) {  
            Log.i(TAG, "writeCpuGovernor: write CPU Governor(" + governor + ") failed!");  
            return false;  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        return true;  
    }  
      
    private String readCurCpuGovernor()  
    {  
        String governor = null;  
        DataInputStream is = null;  
        try {  
            Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_governor");  
            is = new DataInputStream(process.getInputStream());  
            governor = is.readLine();  
        } catch (IOException e) {  
            Log.i(TAG, "readCurCpuGovernor: read CPU Governor failed!");  
            return null;  
        }  
        return governor;  
    }  
      
    private List<String> readCpuGovernors()  
    {  
        List<String> governors = new ArrayList<String>();  
        DataInputStream is = null;  
        try {  
            Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_available_governors");  
            is = new DataInputStream(process.getInputStream());  
            String line = is.readLine();  
      
            String[] strs = line.split(" ");  
            for(int i = 0; i < strs.length; i++)  
                governors.add(strs[i]);  
        } catch (IOException e) {  
            Log.i(TAG, "readCpuGovernors: read CPU Governors failed!");  
        }  
        return governors;  
    }  
}  
