/*
 * @(#)DataVo.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.httpservice;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * SimpleRandomChat DataVo
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class DataVo {


    private final String CALL_FUNCTION;
    private final String RESULT_CODE;
    private final String RESULT_MESSAGE;
    private final List<HashMap<String, Object>> RESULT_DATA;

    /**
     * DataVo
     *
     * @param CALL_FUNCTION
     * @param RESULT_CODE
     * @param RESULT_MESSAGE
     * @param RESULT_DATA
     */
    public DataVo(String CALL_FUNCTION, String RESULT_CODE, String RESULT_MESSAGE, List<HashMap<String, Object>> RESULT_DATA) {
        this.CALL_FUNCTION = CALL_FUNCTION;
        this.RESULT_CODE = RESULT_CODE;
        this.RESULT_MESSAGE = RESULT_MESSAGE;
        this.RESULT_DATA = RESULT_DATA;
    }

    /**
     * call function
     *
     * @return
     */
    public String getCALL_FUNCTION() {
        return CALL_FUNCTION;
    }

    /**
     * result code S:Success , E:Error
     *
     * @return
     */
    public String getRESULT_CODE() {
        return RESULT_CODE;
    }

    /**
     * result message
     *
     * @return
     */
    public String getRESULT_MESSAGE() {
        return RESULT_MESSAGE;
    }

    /**
     * result data List<HashMap<String,Object>>
     *
     * @return
     */
    public List<HashMap<String, Object>> getRESULT_DATA() {
        return RESULT_DATA;
    }
}


