/*
 * @(#)Util.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.utilservice;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public static final int EOF = -1;
    public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

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


    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    public static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
