package okhttp.jack.com.okhttp;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogTool {
	
		public static boolean DEBUG = true;
		
		private LogTool() {
		}
		
		/**
		 * 
		 * @param obj
		 * @param msg
		 */
		public static void ee(Object obj, String msg) {
			if (DEBUG) {
				Log.e(obj.getClass().getSimpleName(), msg);
			}
		}
		
		/**
		 * 
		 * @param obj
		 * @param msg
		 */
		public static void ii(String obj, String msg) {
			if (DEBUG) {
				Log.i(obj, msg);
			}
		}
		
		/**
		 * 
		 * 
		 * @param obj
		 * @param msg
		 */
		public static void dd(Object obj, String msg) {
			if (DEBUG) {
				Log.d(obj.getClass().getSimpleName(), msg);
			}
		}
		
		/**
		 * 
		 * 
		 * @param obj
		 * @param msg
		 */
		public static void ww(Object obj, String msg) {
			if (DEBUG) {
				Log.w(obj.getClass().getSimpleName(), msg);
			}
		}
		
		/**
		 * 
		 * 
		 * @param obj
		 * @param msg
		 */
		public static void vv(Object obj, String msg) {
			if (DEBUG) {
				Log.v(obj.getClass().getSimpleName(), msg);
			}
		}
		
		/**
		 * 打印log到文件中
		 * 
		 * @param logPath 日志文件路径
		 * @param logData 日志内容
		 * @param override 是否覆盖文件内容
		 */
		public static void ff(String logPath, String logData, boolean override) {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(logPath, !override));
				if (!override) {
					writer.append(logData);
				} else {
					writer.write(logData);
				}
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			 finally {
				try {
					if (writer != null) {
						writer.flush();
						writer.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();;
				}
			}
		}
	

}
