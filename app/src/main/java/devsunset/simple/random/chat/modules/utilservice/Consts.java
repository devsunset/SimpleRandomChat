/*
 * @(#)Consts.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.utilservice;

/**
 * <PRE>
 * SimpleRandomChat Consts
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class Consts {

    // screen capture disable
    public static final boolean SCREEN_CAPTURE_DISABLE = false;

    // MESSAGE STATUS
    // F : First
    public static final String MESSAGE_STATUS_FIRST = "F";
    // P : Procceeding
    public static final String MESSAGE_STATUS_PROCEDDING = "P";
    // R : Reply
    public static final String MESSAGE_STATUS_REPLY = "R";
    // H : Hide
    public static final String MESSAGE_STATUS_HIDE = "H";
    // D : Delete
    public static final String MESSAGE_STATUS_DELETE = "D";

    // MESSAGE TYPE
    // T : TEXT
    public static final String MESSAGE_TYPE_TEXT = "T";
    // V : VOICE
    public static final String MESSAGE_TYPE_VOICE = "V";
    // I : IMAGE
    public static final String MESSAGE_TYPE_IMAGE = "I";


    // IDS PREFIX
    // UID
    public static final String IDS_PRIEFIX_UID = "UID_";
    // ATX
    public static final String IDS_PRIEFIX_ATX = "ATX_";
    // TTD
    public static final String IDS_PRIEFIX_TTD = "TTD_";
    // BLACK LIST
    public static final String IDS_PRIEFIX_BLA = "BLA_";

    // Data Clean Day
    public static final int DATA_CLEAN_DAY = 15;

    // App Access Period
    public static final int APP_ACCESS_PERIOD = 3;

    // Attach File Download Period
    public static final int ATTACH_FILE_MAX_PERIOD = 2;
}
