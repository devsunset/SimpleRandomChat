package devsunset.simple.random.chat.modules;

import java.util.HashMap;
import java.util.List;

public class DataVo {


    private final String CALL_FUNCTION;
    private final String RESULT_CODE;
    private final String RESULT_MESSAGE;
    private final List<HashMap<String,Object>> RESULT_DATA;

    public DataVo(String CALL_FUNCTION, String RESULT_CODE, String RESULT_MESSAGE, List<HashMap<String,Object>> RESULT_DATA) {
        this.CALL_FUNCTION = CALL_FUNCTION;
        this.RESULT_CODE = RESULT_CODE;
        this.RESULT_MESSAGE = RESULT_MESSAGE;
        this.RESULT_DATA = RESULT_DATA;
    }

    public String getCALL_FUNCTION() {
        return CALL_FUNCTION;
    }

    public String getRESULT_CODE() {
        return RESULT_CODE;
    }

    public String getRESULT_MESSAGE() {
        return RESULT_MESSAGE;
    }

    public List<HashMap<String,Object>> getRESULT_DATA() {
        return RESULT_DATA;
    }
}


