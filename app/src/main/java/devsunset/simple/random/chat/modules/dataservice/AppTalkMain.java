/*
 * @(#)AppTalkMain.java
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
 * SimpleRandomChat AppTalkMain
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
@Entity(tableName = "APP_TALK_MAIN")
public class AppTalkMain implements Serializable {

    @PrimaryKey
    @NonNull
    private String ATX_ID;
    private String ATX_LOCAL_TIME;
    private String ATX_STATUS;
    private String FROM_APP_ID;
    private String FROM_APP_KEY;
    private String FROM_COUNTRY;
    private String FROM_COUNTRY_NAME;
    private String FROM_GENDER;
    private String FROM_LANG;
    private String TALK_APP_ID;
    private String TALK_TEXT;
    private String TALK_TYPE;
    private String TO_APP_ID;
    private String TO_APP_KEY;
    private String TO_COUNTRY;
    private String TO_COUNTRY_NAME;
    private String TO_GENDER;
    private String TO_LANG;

    public String getATX_ID() {
        return ATX_ID;
    }
    public void setATX_ID(String aTX_ID) {
        ATX_ID = aTX_ID;
    }
    public String getATX_LOCAL_TIME() {
        return ATX_LOCAL_TIME;
    }
    public void setATX_LOCAL_TIME(String aATX_LOCAL_TIME) {
        ATX_LOCAL_TIME = aATX_LOCAL_TIME;
    }
    public String getATX_STATUS() {
        return ATX_STATUS;
    }
    public void setATX_STATUS(String aTX_STATUS) {
        ATX_STATUS = aTX_STATUS;
    }
    public String getFROM_APP_ID() {
        return FROM_APP_ID;
    }
    public void setFROM_APP_ID(String fROM_APP_ID) {
        FROM_APP_ID = fROM_APP_ID;
    }
    public String getFROM_APP_KEY() {
        return FROM_APP_KEY;
    }
    public void setFROM_APP_KEY(String fROM_APP_KEY) {
        FROM_APP_KEY = fROM_APP_KEY;
    }
    public String getFROM_COUNTRY() {
        return FROM_COUNTRY;
    }
    public void setFROM_COUNTRY(String fROM_COUNTRY) {
        FROM_COUNTRY = fROM_COUNTRY;
    }
    public String getFROM_COUNTRY_NAME() {
        return FROM_COUNTRY_NAME;
    }
    public void setFROM_COUNTRY_NAME(String fROM_COUNTRY_NAME) {
        FROM_COUNTRY_NAME = fROM_COUNTRY_NAME;
    }
    public String getFROM_GENDER() {
        return FROM_GENDER;
    }
    public void setFROM_GENDER(String fROM_GENDER) {
        FROM_GENDER = fROM_GENDER;
    }
    public String getFROM_LANG() {
        return FROM_LANG;
    }
    public void setFROM_LANG(String fROM_LANG) {
        FROM_LANG = fROM_LANG;
    }
    public String getTALK_APP_ID() {
        return TALK_APP_ID;
    }
    public void setTALK_APP_ID(String tALK_APP_ID) {TALK_APP_ID = tALK_APP_ID; }
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
    public String getTO_APP_ID() {
        return TO_APP_ID;
    }
    public void setTO_APP_ID(String tO_APP_ID) {
        TO_APP_ID = tO_APP_ID;
    }
    public String getTO_APP_KEY() {
        return TO_APP_KEY;
    }
    public void setTO_APP_KEY(String tO_APP_KEY) {
        TO_APP_KEY = tO_APP_KEY;
    }
    public String getTO_COUNTRY() {
        return TO_COUNTRY;
    }
    public void setTO_COUNTRY(String tO_COUNTRY) {
        TO_COUNTRY = tO_COUNTRY;
    }
    public String getTO_COUNTRY_NAME() {
        return TO_COUNTRY_NAME;
    }
    public void setTO_COUNTRY_NAME(String tO_COUNTRY_NAME) {
        TO_COUNTRY_NAME = tO_COUNTRY_NAME;
    }
    public String getTO_GENDER() {
        return TO_GENDER;
    }
    public void setTO_GENDER(String tO_GENDER) {
        TO_GENDER = tO_GENDER;
    }
    public String getTO_LANG() {
        return TO_LANG;
    }
    public void setTO_LANG(String tO_LANG) {
        TO_LANG = tO_LANG;
    }
}