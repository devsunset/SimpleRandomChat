/*
 * @(#)MessageItem.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.etcservice;

/**
 * <PRE>
 * SimpleRandomChat MessageItem
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class MessageItem {

    public int drawableId;
    private String ATX_ID;
    private String ATX_LOCAL_TIME;
    private String ATX_STATUS;
    private String COUNTRY_NAME;
    private String FROM_LANG;
    private String GENDER;
    private String REPLY_APP_KEY;
    private String TALK_LANG;
    private String TALK_TEXT;
    private String TALK_TARGET;
    private String TO_LANG;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getATX_ID() {
        return ATX_ID;
    }

    public void setATX_ID(String ATX_ID) {
        this.ATX_ID = ATX_ID;
    }

    public String getATX_LOCAL_TIME() {
        return ATX_LOCAL_TIME;
    }

    public void setATX_LOCAL_TIME(String ATX_LOCAL_TIME) {
        this.ATX_LOCAL_TIME = ATX_LOCAL_TIME;
    }

    public String getATX_STATUS() {
        return ATX_STATUS;
    }

    public void setATX_STATUS(String ATX_STATUS) {
        this.ATX_STATUS = ATX_STATUS;
    }

    public String getCOUNTRY_NAME() {
        return COUNTRY_NAME;
    }

    public void setCOUNTRY_NAME(String COUNTRY_NAME) {
        this.COUNTRY_NAME = COUNTRY_NAME;
    }

    public String getFROM_LANG() {
        return FROM_LANG;
    }

    public void setFROM_LANG(String FROM_LANG) {
        this.FROM_LANG = FROM_LANG;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getREPLY_APP_KEY() {
        return REPLY_APP_KEY;
    }

    public void setREPLY_APP_KEY(String REPLY_APP_KEY) {
        this.REPLY_APP_KEY = REPLY_APP_KEY;
    }

    public String getTALK_LANG() {
        return TALK_LANG;
    }

    public void setTALK_LANG(String TALK_LANG) {
        this.TALK_LANG = TALK_LANG;
    }

    public String getTALK_TEXT() {
        return TALK_TEXT;
    }

    public void setTALK_TEXT(String TALK_TEXT) {
        this.TALK_TEXT = TALK_TEXT;
    }

    public String getTALK_TARGET() {
        return TALK_TARGET;
    }

    public void setTALK_TARGET(String TALK_TARGET) {
        this.TALK_TARGET = TALK_TARGET;
    }

    public String getTO_LANG() {
        return TO_LANG;
    }

    public void setTO_LANG(String TO_LANG) {
        this.TO_LANG = TO_LANG;
    }
}
