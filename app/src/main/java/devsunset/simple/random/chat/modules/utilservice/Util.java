/*
 * @(#)Util.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.utilservice;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * <PRE>
 * SimpleRandomChat Util
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

public class Util {

    /**
     * Service Background 여부 판단
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    /**
     * encoder
     * @return base64
     */
    public static String encoder(String path,String fileName) {
        String base64 = "";
        File file = new File(path+File.separator+fileName);
        FileInputStream inFile = null;
        try {
            inFile = new FileInputStream(file);
            byte data[] = new byte[(int) file.length()];
            inFile.read(data);
            base64 = null;//Base64.getEncoder().encodeToString(data);
        } catch (FileNotFoundException e) {
            Logger.e("encoder Error : "+e.getMessage());
        } catch (IOException e) {
            Logger.e("encoder Error : "+e.getMessage());
        } finally{
            if(inFile !=null){
                try {
                    inFile.close();
                } catch (IOException e) {
                    Logger.e("encoder Error : "+e.getMessage());
                }
            }
        }
        return base64;
    }

    /**
     * decoder
     * @param base64
     * @return boolean
     */
    public static boolean decoder(String path,String fileName,String base64) {
        boolean check = true;
        FileOutputStream outFile = null;
        try  {
            outFile = new FileOutputStream(path+File.separator+fileName);
            byte[] btyeArray = null;//Base64.getUrlDecoder().decode(base64.getBytes());
            outFile.write(btyeArray);
            outFile.flush();
        } catch (FileNotFoundException e) {
            check = false;
            Logger.e("decoder Error : "+e.getMessage());
        } catch (IOException e) {
            check = false;
            Logger.e("decoder Error : "+e.getMessage());
        } finally{
            if(outFile !=null){
                try {
                    outFile.close();
                } catch (IOException e) {
                    Logger.e("decoder Error : "+e.getMessage());
                }
            }
        }
        return check;
    }
}
