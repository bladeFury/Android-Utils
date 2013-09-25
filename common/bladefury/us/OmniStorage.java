package common.bladefury.us;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class OmniStorage {
	
	private static final String TAG = "OmniStorage";
	private Context mContext;
	
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	
	
	public OmniStorage(Context context) {
		mContext = context;
	}
	
	public void init() {
		checkExternalStorage();
	}
	
	/*
	 * I want a place to save my file and I need other apps can access it
	 * to get FileOutputStream, please use StorageManager.get...Stream
	 * @param desiredDirName: relative dir name
	 */
	public File getPublicStorage(String desiredDirName) {
		File publicStorage;
		if (mExternalStorageWriteable) {
			publicStorage = getExternalStorage(desiredDirName);
		} 
		else {
			publicStorage = mContext.getFilesDir();
		}
		
		return publicStorage;
	}
	
	/*
	 * I want a place to save my file and I don't give a shit about access
	 * to get FileOutputStream, please use StorageManager.get...Stream
	 * @param desiredDirName: relative dir name
	 */
	public File getWhateverStorage(String desiredDirName) {
		File weStorage;
		if (mExternalStorageWriteable) {
			weStorage = getExternalStorage(desiredDirName);
		}
		else {
			weStorage = mContext.getDir(desiredDirName, 0);
		}
		return weStorage;
	}
	
	public File getCacheDir() {
		if (mExternalStorageWriteable) {
			return mContext.getExternalCacheDir();
		}
		else {
			return mContext.getCacheDir();
		}
	}
	
	public FileOutputStream getPublicFileOutputStream(File dir, String filename)
			throws FileNotFoundException {
		if (mExternalStorageWriteable) {
			return new FileOutputStream(new File(dir, filename));
		}
		try {
			if (dir.getCanonicalPath().equals(mContext.getFilesDir().getCanonicalPath())) {
				return mContext.openFileOutput(filename, Context.MODE_WORLD_READABLE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new FileOutputStream(new File(dir, filename));
	}
	
	public FileOutputStream getWhateverFileOutputStream(File dir, String filename) 
			throws FileNotFoundException {
		return new FileOutputStream(new File(dir, filename));
	}
	
	private File getExternalStorage(String desiredDirName) {
		File appRoot = mContext.getExternalFilesDir(null);
		File desired_dir = appRoot;
		if (desiredDirName != null) {
			desired_dir = new File(appRoot, desiredDirName);
		}
		if (!desired_dir.isDirectory()) {
			desired_dir.mkdirs();
		}
		if (desired_dir.isDirectory()) {
			return desired_dir;
		}
		return appRoot;
	}
	
	private void checkExternalStorage() {
		
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		if (mExternalStorageWriteable) {
			File appRoot = mContext.getExternalFilesDir(null);
			if (appRoot == null) {
				mExternalStorageWriteable = mExternalStorageAvailable = false;
			}
		}
		Log.v(TAG, String.format("external storage available: %s, external storage writeable: %s ", mExternalStorageAvailable, mExternalStorageWriteable ));
	}
	
	
}
