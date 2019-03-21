/*
 * @(#)Util.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.utilservice;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
