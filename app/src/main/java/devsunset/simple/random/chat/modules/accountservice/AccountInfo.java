/*
 * @(#)AccountInfo.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.accountservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <PRE>
 * SimpleRandomChat AccountInfo
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

public class AccountInfo {

    public static final String TAG = AccountInfo.class.getSimpleName();

    /**
     * Get Account Info
     * @param context
     * @return
     */
    public static HashMap<String,Object> getAccountInfo(Context context){

        HashMap<String,Object> accountInfo = new HashMap<String,Object>();
        SharedPreferences pref = context.getSharedPreferences("SRC_PREF", context.MODE_PRIVATE);

        accountInfo.put("APP_ID",pref.getString("APP_ID", "-"));
        accountInfo.put("APP_KEY",pref.getString("APP_KEY", "-"));
        accountInfo.put("APP_NUMBER",pref.getString("APP_NUMBER", "-"));
        accountInfo.put("APP_PHONE",pref.getString("APP_PHONE", "-"));
        accountInfo.put("APP_VER",pref.getString("APP_VER", "1.0"));
        accountInfo.put("COUNTRY",pref.getString("COUNTRY", "-"));
        accountInfo.put("COUNTRY_NAME",pref.getString("COUNTRY_NAME", "-"));
        accountInfo.put("GENDER",pref.getString("GENDER", "W"));
        accountInfo.put("LANG",pref.getString("LANG", "-"));
        accountInfo.put("NOTICE_NUMBER",pref.getString("NOTICE_NUMBER", "0"));
        accountInfo.put("SET_ALARM_YN",pref.getString("SET_ALARM_YN", "Y"));
        accountInfo.put("SET_BYE_CONFIRM_YN",pref.getString("SET_BYE_CONFIRM_YN", "N"));
        accountInfo.put("SET_LOCK_PWD",pref.getString("SET_LOCK_PWD", "-"));
        accountInfo.put("SET_LOCK_YN",pref.getString("SET_LOCK_YN", "N"));
        accountInfo.put("SET_NEW_RECEIVE_YN",pref.getString("SET_NEW_RECEIVE_YN", "Y"));
        accountInfo.put("SET_SEND_COUNTRY",pref.getString("SET_SEND_COUNTRY", "N"));
        accountInfo.put("SET_SEND_GENDER",pref.getString("SET_SEND_GENDER", "A"));
        accountInfo.put("SET_SEND_LIST_HIDE_YN",pref.getString("SET_SEND_LIST_HIDE_YN", "N"));
        accountInfo.put("SET_SOUND_YN",pref.getString("SET_SOUND_YN", "Y"));
        accountInfo.put("SET_VIBRATION_YN",pref.getString("SET_VIBRATION_YN", "Y"));

        return accountInfo;
    }

    /**
     * Set Accunt Info
     * @param context
     * @param accountInfo
     * @return
     */
    public static boolean setAccountInfo(Context context,HashMap<String,Object> accountInfo){

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

            if(accountInfo.containsKey("APP_PHONE") && !"".equals(accountInfo.get("APP_PHONE"))){
                editor.putString("APP_PHONE", accountInfo.get("APP_PHONE").toString());
            }

            if(accountInfo.containsKey("APP_VER") && !"".equals(accountInfo.get("APP_VER"))){
                editor.putString("APP_VER", accountInfo.get("APP_VER").toString());
            }

            if(accountInfo.containsKey("COUNTRY") && !"".equals(accountInfo.get("COUNTRY"))){
                editor.putString("COUNTRY", accountInfo.get("COUNTRY").toString());
                editor.putString("COUNTRY_NAME", getContryName(accountInfo.get("COUNTRY").toString()));
            }

            if(accountInfo.containsKey("GENDER") && !"".equals(accountInfo.get("GENDER"))){
                editor.putString("GENDER", accountInfo.get("GENDER").toString());
            }

            if(accountInfo.containsKey("LANG") && !"".equals(accountInfo.get("LANG"))){
                editor.putString("LANG", accountInfo.get("LANG").toString());
            }

            if(accountInfo.containsKey("NOTICE_NUMBER") && !"".equals(accountInfo.get("NOTICE_NUMBER"))){
                editor.putString("NOTICE_NUMBER", accountInfo.get("NOTICE_NUMBER").toString());
            }

            if(accountInfo.containsKey("SET_ALARM_YN") && !"".equals(accountInfo.get("SET_ALARM_YN"))){
                editor.putString("SET_ALARM_YN", accountInfo.get("SET_ALARM_YN").toString());
            }

            if(accountInfo.containsKey("SET_BYE_CONFIRM_YN") && !"".equals(accountInfo.get("SET_BYE_CONFIRM_YN"))){
                editor.putString("SET_BYE_CONFIRM_YN", accountInfo.get("SET_BYE_CONFIRM_YN").toString());
            }

            if(accountInfo.containsKey("SET_LOCK_PWD") && !"".equals(accountInfo.get("SET_LOCK_PWD"))){
                editor.putString("SET_LOCK_PWD", accountInfo.get("SET_LOCK_PWD").toString());
            }

            if(accountInfo.containsKey("SET_LOCK_YN") && !"".equals(accountInfo.get("SET_LOCK_YN"))){
                editor.putString("SET_LOCK_YN", accountInfo.get("SET_LOCK_YN").toString());
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


            // LOCAL 설정 SERVER와 동기화 처리
            if(!accountInfo.containsKey("INITIALIZE") || !"Y".equals(accountInfo.get("INITIALIZE"))){
                HttpConnectService httpConnctService =  HttpConnectClient.getClient().create(HttpConnectService.class);

                httpConnctService.appInfoUpdate(AccountInfo.getAccountInfo(context)).enqueue(new Callback<DataVo>() {
                    @Override
                    public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                        if (response.isSuccessful()) {
                            DataVo data = response.body();
                            if (data != null) {
                                if("S" == data.getRESULT_CODE()){
                                    Log.i(TAG,"appInfoUpdate Success ");
                                }else{
                                    Log.i(TAG,"appInfoUpdate Fail : "+data.getRESULT_MESSAGE());
                                }
                            }else{
                                Log.e(TAG,"appInfoUpdate response error :" +response.isSuccessful());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                        Log.e(TAG,"appInfoUpdate error :" +t.getMessage());
                    }
                });
            }

            result = true;
        }

        /*
            // Delete example
            SharedPreferences pref =  context.getSharedPreferences("SRC_PREF", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("APP_ID");
            editor.commit();
         */

        return result;
    }

    /**
     * Set Account Info
     * @param country
     * @return
     */
    public static String getContryName (String country){

        String countryName = "OverSea";

        HashMap<String,String> countryMap = new HashMap<String,String>();
        countryMap.put("AF","Afghanistan");
        countryMap.put("AL","Albania");
        countryMap.put("DZ","Algeria");
        countryMap.put("AS","American Samoa");
        countryMap.put("AD","Andorra");
        countryMap.put("AO","Angola");
        countryMap.put("AI","Anguilla");
        countryMap.put("AQ","Antarctica");
        countryMap.put("AG","Antigua and Barbuda");
        countryMap.put("AR","Argentina");
        countryMap.put("AM","Armenia");
        countryMap.put("AW","Aruba");
        countryMap.put("AU","Australia");
        countryMap.put("AT","Austria");
        countryMap.put("AZ","Azerbaijan");
        countryMap.put("BS","Bahamas");
        countryMap.put("BH","Bahrain");
        countryMap.put("BD","Bangladesh");
        countryMap.put("BB","Barbados");
        countryMap.put("BY","Belarus");
        countryMap.put("BE","Belgium");
        countryMap.put("BZ","Belize");
        countryMap.put("BJ","Benin");
        countryMap.put("BM","Bermuda");
        countryMap.put("BT","Bhutan");
        countryMap.put("BO","Bolivia");
        countryMap.put("BA","Bosnia and Herzegovina");
        countryMap.put("BW","Botswana");
        countryMap.put("BR","Brazil");
        countryMap.put("IO","British Indian Ocean Territory");
        countryMap.put("VG","British Virgin Islands");
        countryMap.put("BN","Brunei");
        countryMap.put("BG","Bulgaria");
        countryMap.put("BF","Burkina Faso");
        countryMap.put("BI","Burundi");
        countryMap.put("KH","Cambodia");
        countryMap.put("CM","Cameroon");
        countryMap.put("CA","Canada");
        countryMap.put("CV","Cape Verde");
        countryMap.put("KY","Cayman Islands");
        countryMap.put("CF","Central African Republic");
        countryMap.put("TD","Chad");
        countryMap.put("CL","Chile");
        countryMap.put("CN","China");
        countryMap.put("CX","Christmas Island");
        countryMap.put("CC","Cocos Islands");
        countryMap.put("CO","Colombia");
        countryMap.put("KM","Comoros");
        countryMap.put("CK","Cook Islands");
        countryMap.put("CR","Costa Rica");
        countryMap.put("HR","Croatia");
        countryMap.put("CU","Cuba");
        countryMap.put("CW","Curacao");
        countryMap.put("CY","Cyprus");
        countryMap.put("CZ","Czech Republic");
        countryMap.put("CD","Democratic Republic of the Congo");
        countryMap.put("DK","Denmark");
        countryMap.put("DJ","Djibouti");
        countryMap.put("DM","Dominica");
        countryMap.put("DO","Dominican Republic");
        countryMap.put("TL","East Timor");
        countryMap.put("EC","Ecuador");
        countryMap.put("EG","Egypt");
        countryMap.put("SV","El Salvador");
        countryMap.put("GQ","Equatorial Guinea");
        countryMap.put("ER","Eritrea");
        countryMap.put("EE","Estonia");
        countryMap.put("ET","Ethiopia");
        countryMap.put("FK","Falkland Islands");
        countryMap.put("FO","Faroe Islands");
        countryMap.put("FJ","Fiji");
        countryMap.put("FI","Finland");
        countryMap.put("FR","France");
        countryMap.put("PF","French Polynesia");
        countryMap.put("GA","Gabon");
        countryMap.put("GM","Gambia");
        countryMap.put("GE","Georgia");
        countryMap.put("DE","Germany");
        countryMap.put("GH","Ghana");
        countryMap.put("GI","Gibraltar");
        countryMap.put("GR","Greece");
        countryMap.put("GL","Greenland");
        countryMap.put("GD","Grenada");
        countryMap.put("GU","Guam");
        countryMap.put("GT","Guatemala");
        countryMap.put("GG","Guernsey");
        countryMap.put("GN","Guinea");
        countryMap.put("GW","Guinea-Bissau");
        countryMap.put("GY","Guyana");
        countryMap.put("HT","Haiti");
        countryMap.put("HN","Honduras");
        countryMap.put("HK","Hong Kong");
        countryMap.put("HU","Hungary");
        countryMap.put("IS","Iceland");
        countryMap.put("IN","India");
        countryMap.put("ID","Indonesia");
        countryMap.put("IR","Iran");
        countryMap.put("IQ","Iraq");
        countryMap.put("IE","Ireland");
        countryMap.put("IM","Isle of Man");
        countryMap.put("IL","Israel");
        countryMap.put("IT","Italy");
        countryMap.put("CI","Ivory Coast");
        countryMap.put("JM","Jamaica");
        countryMap.put("JP","Japan");
        countryMap.put("JE","Jersey");
        countryMap.put("JO","Jordan");
        countryMap.put("KZ","Kazakhstan");
        countryMap.put("KE","Kenya");
        countryMap.put("KI","Kiribati");
        countryMap.put("XK","Kosovo");
        countryMap.put("KW","Kuwait");
        countryMap.put("KG","Kyrgyzstan");
        countryMap.put("LA","Laos");
        countryMap.put("LV","Latvia");
        countryMap.put("LB","Lebanon");
        countryMap.put("LS","Lesotho");
        countryMap.put("LR","Liberia");
        countryMap.put("LY","Libya");
        countryMap.put("LI","Liechtenstein");
        countryMap.put("LT","Lithuania");
        countryMap.put("LU","Luxembourg");
        countryMap.put("MO","Macau");
        countryMap.put("MK","Macedonia");
        countryMap.put("MG","Madagascar");
        countryMap.put("MW","Malawi");
        countryMap.put("MY","Malaysia");
        countryMap.put("MV","Maldives");
        countryMap.put("ML","Mali");
        countryMap.put("MT","Malta");
        countryMap.put("MH","Marshall Islands");
        countryMap.put("MR","Mauritania");
        countryMap.put("MU","Mauritius");
        countryMap.put("YT","Mayotte");
        countryMap.put("MX","Mexico");
        countryMap.put("FM","Micronesia");
        countryMap.put("MD","Moldova");
        countryMap.put("MC","Monaco");
        countryMap.put("MN","Mongolia");
        countryMap.put("ME","Montenegro");
        countryMap.put("MS","Montserrat");
        countryMap.put("MA","Morocco");
        countryMap.put("MZ","Mozambique");
        countryMap.put("MM","Myanmar");
        countryMap.put("NA","Namibia");
        countryMap.put("NR","Nauru");
        countryMap.put("NP","Nepal");
        countryMap.put("NL","Netherlands");
        countryMap.put("AN","Netherlands Antilles");
        countryMap.put("NC","New Caledonia");
        countryMap.put("NZ","New Zealand");
        countryMap.put("NI","Nicaragua");
        countryMap.put("NE","Niger");
        countryMap.put("NG","Nigeria");
        countryMap.put("NU","Niue");
        countryMap.put("KP","North Korea");
        countryMap.put("MP","Northern Mariana Islands");
        countryMap.put("NO","Norway");
        countryMap.put("OM","Oman");
        countryMap.put("PK","Pakistan");
        countryMap.put("PW","Palau");
        countryMap.put("PS","Palestine");
        countryMap.put("PA","Panama");
        countryMap.put("PG","Papua New Guinea");
        countryMap.put("PY","Paraguay");
        countryMap.put("PE","Peru");
        countryMap.put("PH","Philippines");
        countryMap.put("PN","Pitcairn");
        countryMap.put("PL","Poland");
        countryMap.put("PT","Portugal");
        countryMap.put("PR","Puerto Rico");
        countryMap.put("QA","Qatar");
        countryMap.put("CG","Republic of the Congo");
        countryMap.put("RE","Reunion");
        countryMap.put("RO","Romania");
        countryMap.put("RU","Russia");
        countryMap.put("RW","Rwanda");
        countryMap.put("BL","Saint Barthelemy");
        countryMap.put("SH","Saint Helena");
        countryMap.put("KN","Saint Kitts and Nevis");
        countryMap.put("LC","Saint Lucia");
        countryMap.put("MF","Saint Martin");
        countryMap.put("PM","Saint Pierre and Miquelon");
        countryMap.put("VC","Saint Vincent and the Grenadines");
        countryMap.put("WS","Samoa");
        countryMap.put("SM","San Marino");
        countryMap.put("ST","Sao Tome and Principe");
        countryMap.put("SA","Saudi Arabia");
        countryMap.put("SN","Senegal");
        countryMap.put("RS","Serbia");
        countryMap.put("SC","Seychelles");
        countryMap.put("SL","Sierra Leone");
        countryMap.put("SG","Singapore");
        countryMap.put("SX","Sint Maarten");
        countryMap.put("SK","Slovakia");
        countryMap.put("SI","Slovenia");
        countryMap.put("SB","Solomon Islands");
        countryMap.put("SO","Somalia");
        countryMap.put("ZA","South Africa");
        countryMap.put("KR","South Korea");
        countryMap.put("SS","South Sudan");
        countryMap.put("ES","Spain");
        countryMap.put("LK","Sri Lanka");
        countryMap.put("SD","Sudan");
        countryMap.put("SR","Suriname");
        countryMap.put("SJ","Svalbard and Jan Mayen");
        countryMap.put("SZ","Swaziland");
        countryMap.put("SE","Sweden");
        countryMap.put("CH","Switzerland");
        countryMap.put("SY","Syria");
        countryMap.put("TW","Taiwan");
        countryMap.put("TJ","Tajikistan");
        countryMap.put("TZ","Tanzania");
        countryMap.put("TH","Thailand");
        countryMap.put("TG","Togo");
        countryMap.put("TK","Tokelau");
        countryMap.put("TO","Tonga");
        countryMap.put("TT","Trinidad and Tobago");
        countryMap.put("TN","Tunisia");
        countryMap.put("TR","Turkey");
        countryMap.put("TM","Turkmenistan");
        countryMap.put("TC","Turks and Caicos Islands");
        countryMap.put("TV","Tuvalu");
        countryMap.put("VI","U.S. Virgin Islands");
        countryMap.put("UG","Uganda");
        countryMap.put("UA","Ukraine");
        countryMap.put("AE","United Arab Emirates");
        countryMap.put("GB","United Kingdom");
        countryMap.put("US","United States");
        countryMap.put("UY","Uruguay");
        countryMap.put("UZ","Uzbekistan");
        countryMap.put("VU","Vanuatu");
        countryMap.put("VA","Vatican");
        countryMap.put("VE","Venezuela");
        countryMap.put("VN","Vietnam");
        countryMap.put("WF","Wallis and Futuna");
        countryMap.put("EH","Western Sahara");
        countryMap.put("YE","Yemen");
        countryMap.put("ZM","Zambia");
        countryMap.put("ZW","Zimbabwe");

        if(countryMap.get(country) !=null){
            countryName = countryMap.get(country);
        }

        return countryName;
    }
}
