/*
 * @(#)AppTalkThread.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.dataservice;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * <PRE>
 * SimpleRandomChat AppTalkThread
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

@Entity(tableName = "APP_TALK_THREAD")
public class AppTalkThread  implements Serializable {

    private String ATX_ID;
    private String TALK_ACCESS_TIME;
    private String TALK_ACCESS_LOCAL_TIME;
    private String TALK_APP_ID;
    @PrimaryKey
    @NonNull
    private String TALK_ID;
    private String TALK_COUNTRY;
    private String TALK_COUNTRY_NAME;
    private String TALK_GENDER;
    private String TALK_TEXT;
    private String TALK_TYPE;


    public String getATX_ID() {
        return ATX_ID;
    }
    public void setATX_ID(String aTX_ID) {
        ATX_ID = aTX_ID;
    }
    public String getTALK_ACCESS_TIME() {
        return TALK_ACCESS_TIME;
    }
    public void setTALK_ACCESS_TIME(String tALK_ACCESS_TIME) {
        TALK_ACCESS_TIME = tALK_ACCESS_TIME;
    }
    public String getTALK_ACCESS_LOCAL_TIME() {
        return TALK_ACCESS_LOCAL_TIME;
    }
    public void setTALK_ACCESS_LOCAL_TIME(String tALK_ACCESS_LOCAL_TIME) {
        TALK_ACCESS_LOCAL_TIME = tALK_ACCESS_LOCAL_TIME;
    }
    public String getTALK_APP_ID() {
        return TALK_APP_ID;
    }
    public void setTALK_APP_ID(String tALK_APP_ID) {
        TALK_APP_ID = tALK_APP_ID;
    }
    public String getTALK_ID() {
        return TALK_ID;
    }
    public void setTALK_ID(String tALK_ID) {
        TALK_ID = tALK_ID;
    }
    public String getTALK_COUNTRY() {
        return TALK_COUNTRY;
    }
    public void setTALK_COUNTRY(String tALK_COUNTRY) {
        TALK_COUNTRY = tALK_COUNTRY;
    }
    public String getTALK_COUNTRY_NAME() {
        return TALK_COUNTRY_NAME;
    }
    public void setTALK_COUNTRY_NAME(String tALK_COUNTRY_NAME) {
        TALK_COUNTRY_NAME = tALK_COUNTRY_NAME;
    }
    public String getTALK_GENDER() {
        return TALK_GENDER;
    }
    public void setTALK_GENDER(String tALK_GENDER) {
        TALK_GENDER = tALK_GENDER;
    }
    public String getTALK_TEXT() {
        return TALK_TEXT;
    }
    public void setTALK_TEXT(String tALK_TEXT) {
        TALK_TEXT = tALK_TEXT;
    }
    public String getTALK_TYPE() {
        return TALK_TYPE;
    }
    public void setTALK_TYPE(String tALK_TYPE) {
        TALK_TYPE = tALK_TYPE;
    }
}