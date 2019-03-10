package devsunset.simple.random.chat.modules;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class AccountInfo {

    public HashMap<String,Object> getAccountInfo(Context context){

        HashMap<String,Object> accountInfo = new HashMap<String,Object>();
        SharedPreferences pref = context.getSharedPreferences("SRC_PREF", context.MODE_PRIVATE);
        accountInfo.put("APP_ID",pref.getString("APP_ID", "-"));
        accountInfo.put("APP_KEY",pref.getString("APP_KEY", "-"));
        accountInfo.put("APP_NUMBER",pref.getString("APP_NUMBER", "-"));
        accountInfo.put("APP_VER",pref.getString("APP_VER", "1.0"));
        accountInfo.put("COUNTRY",pref.getString("COUNTRY", "-"));
        accountInfo.put("GENDER",pref.getString("GENDER", "M"));
        accountInfo.put("LANG",pref.getString("LANG", "-"));
        accountInfo.put("SET_BYE_CONFIRM_YN",pref.getString("SET_BYE_CONFIRM_YN", "N"));
        accountInfo.put("SET_NEW_RECEIVE_YN",pref.getString("SET_NEW_RECEIVE_YN", "Y"));
        accountInfo.put("SET_SEND_GENDER",pref.getString("SET_SEND_GENDER", "A"));
        accountInfo.put("SET_SEND_LIST_HIDE_YN",pref.getString("SET_SEND_LIST_HIDE_YN", "N"));
        accountInfo.put("SET_SOUND_YN",pref.getString("SET_SOUND_YN", "Y"));
        accountInfo.put("SET_VIBRATION_YN",pref.getString("SET_VIBRATION_YN", "Y"));

        return accountInfo;
    }

    public boolean setAccountInfo(Context context,HashMap<String,Object> accountInfo){

        boolean result = false;

        if(accountInfo !=null && !accountInfo.isEmpty() && accountInfo.size() > 0){

            SharedPreferences pref = context.getSharedPreferences("SRC_PREF", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            if(accountInfo.containsKey("APP_ID") && !"".equals(accountInfo.get("APP_ID"))){
                editor.putString("APP_ID", accountInfo.get("APP_ID").toString());
            }

            if(accountInfo.containsKey("APP_KEY") && !"".equals(accountInfo.get("APP_KEY"))){
                editor.putString("APP_KEY", accountInfo.get("APP_KEY").toString());
            }

            if(accountInfo.containsKey("APP_NUMBER") && !"".equals(accountInfo.get("APP_NUMBER"))){
                editor.putString("APP_NUMBER", accountInfo.get("APP_NUMBER").toString());
            }

            if(accountInfo.containsKey("APP_VER") && !"".equals(accountInfo.get("APP_VER"))){
                editor.putString("APP_VER", accountInfo.get("APP_VER").toString());
            }

            if(accountInfo.containsKey("COUNTRY") && !"".equals(accountInfo.get("COUNTRY"))){
                editor.putString("COUNTRY", accountInfo.get("COUNTRY").toString());
            }

            if(accountInfo.containsKey("GENDER") && !"".equals(accountInfo.get("GENDER"))){
                editor.putString("GENDER", accountInfo.get("GENDER").toString());
            }

            if(accountInfo.containsKey("LANG") && !"".equals(accountInfo.get("LANG"))){
                editor.putString("LANG", accountInfo.get("LANG").toString());
            }

            if(accountInfo.containsKey("SET_BYE_CONFIRM_YN") && !"".equals(accountInfo.get("SET_BYE_CONFIRM_YN"))){
                editor.putString("SET_BYE_CONFIRM_YN", accountInfo.get("SET_BYE_CONFIRM_YN").toString());
            }

            if(accountInfo.containsKey("SET_NEW_RECEIVE_YN") && !"".equals(accountInfo.get("SET_NEW_RECEIVE_YN"))){
                editor.putString("SET_NEW_RECEIVE_YN", accountInfo.get("SET_NEW_RECEIVE_YN").toString());
            }

            if(accountInfo.containsKey("SET_SEND_COUNTRY") && !"".equals(accountInfo.get("SET_SEND_COUNTRY"))){
                editor.putString("SET_SEND_COUNTRY", accountInfo.get("SET_SEND_COUNTRY").toString());
            }

            if(accountInfo.containsKey("SET_SEND_GENDER") && !"".equals(accountInfo.get("SET_SEND_GENDER"))){
                editor.putString("SET_SEND_GENDER", accountInfo.get("SET_SEND_GENDER").toString());
            }

            if(accountInfo.containsKey("SET_SEND_LIST_HIDE_YN") && !"".equals(accountInfo.get("SET_SEND_LIST_HIDE_YN"))){
                editor.putString("SET_SEND_LIST_HIDE_YN", accountInfo.get("SET_SEND_LIST_HIDE_YN").toString());
            }

            if(accountInfo.containsKey("SET_SOUND_YN") && !"".equals(accountInfo.get("SET_SOUND_YN"))){
                editor.putString("SET_SOUND_YN", accountInfo.get("SET_SOUND_YN").toString());
            }

            if(accountInfo.containsKey("SET_VIBRATION_YN") && !"".equals(accountInfo.get("SET_VIBRATION_YN"))){
                editor.putString("SET_VIBRATION_YN", accountInfo.get("SET_VIBRATION_YN").toString());
            }

            editor.commit();

            result = true;
        }

        /*
            // Delete example
            SharedPreferences pref = getSharedPreferences("SRC_PREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("APP_ID");
            editor.commit();
         */

        return result;
    }
}
