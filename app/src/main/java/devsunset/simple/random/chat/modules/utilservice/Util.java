package devsunset.simple.random.chat.modules.utilservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(inFile !=null){
                try {
                    inFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
            e.printStackTrace();
        } catch (IOException e) {
            check = false;
            e.printStackTrace();
        } finally{
            if(outFile !=null){
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return check;
    }
}
