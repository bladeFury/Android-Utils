package com.bladefury.us;

import java.util.ArrayList;
import java.util.Scanner;

import com.bladefury.us.ShellUtils.CommandResult;


public class FileSystemUtils {
	
	public static String[] EXCLUDED_APPENDIX = {"asec", "obb", "dev"};
	public static String[] EXCLUDED_PATH = {"/system", "/data", "/dev"};
	
	public static String[] listAllExternalFS() {
		CommandResult result = ShellUtils.execCommand("df", false, true);
		ArrayList<String> resultList = new ArrayList<String>();
		if (result.successMsg != null) {
			Scanner scanner = new Scanner(result.successMsg);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] items = line.split(" +");
				String path = items[0];
				boolean notTarget = false;
				if (!line.startsWith("/")) {
					notTarget = true;
					continue;
				}
				for (String exclude : EXCLUDED_PATH) {
					if (exclude.equals(path)) {
						notTarget = true;
						break;
					}
				}
				for (String exclude_tail: EXCLUDED_APPENDIX) {
					if (path.endsWith(exclude_tail)) {
						notTarget = true;
						break;
					}
				}
				String fsSize = items[1];
				if (fsSize.length() > 1) {
					char unit = fsSize.charAt(fsSize.length() - 1);
					int number = Integer.valueOf(fsSize.substring(0, fsSize.length() - 1));
					if ((unit == 'G' || unit == 'g') && number >= 1) {
						//ok
					}
					else {
						notTarget = true;
					}
				}
				
				if (notTarget) {
					continue;
				}
				else {
					resultList.add(line);
				}
			}
		}
		
		return resultList.toArray(new String[] {});
	}
}
