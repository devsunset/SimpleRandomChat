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
public class AppTalkThread implements Serializable {

    private String ATX_ID;
    private String TALK_APP_ID;
    private String TALK_LOCAL_TIME;
    @PrimaryKey
    @NonNull
    private String TALK_ID;
    private String TALK_COUNTRY;
    private String TALK_COUNTRY_NAME;
    private String TALK_GENDER;
    private String TALK_LANG;
    private String TALK_TEXT;
    private String TALK_TEXT_IMAGE;
    private String TALK_TEXT_VOICE;
    private String TALK_TYPE;

    public String getATX_ID() {
        return ATX_ID;
    }

    public void setATX_ID(String aTX_ID) {
        ATX_ID = aTX_ID;
    }

    public String getTALK_APP_ID() {
        return TALK_APP_ID;
    }

    public void setTALK_APP_ID(String tALK_APP_ID) {
        TALK_APP_ID = tALK_APP_ID;
    }

    public String getTALK_LOCAL_TIME() {
        return TALK_LOCAL_TIME;
    }

    public void setTALK_LOCAL_TIME(String tALK_LOCAL_TIME) {
        TALK_LOCAL_TIME = tALK_LOCAL_TIME;
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

    public String getTALK_LANG() {
        return TALK_LANG;
    }

    public void setTALK_LANG(String tALK_LANG) {
        TALK_LANG = tALK_LANG;
    }

    public String getTALK_TEXT() {
        return TALK_TEXT;
    }

    public void setTALK_TEXT(String tALK_TEXT) {
        TALK_TEXT = tALK_TEXT;
    }

    public String getTALK_TEXT_IMAGE() {
        return TALK_TEXT_IMAGE;
    }

    public void setTALK_TEXT_IMAGE(String tALK_TEXT_IMAGE) {
        TALK_TEXT_IMAGE = tALK_TEXT_IMAGE;
    }

    public String getTALK_TEXT_VOICE() {
        return TALK_TEXT_VOICE;
    }

    public void setTALK_TEXT_VOICE(String tALK_TEXT_VOICE) {
        TALK_TEXT_VOICE = tALK_TEXT_VOICE;
    }

    public String getTALK_TYPE() {
        return TALK_TYPE;
    }

    public void setTALK_TYPE(String tALK_TYPE) {
        TALK_TYPE = tALK_TYPE;
    }
}