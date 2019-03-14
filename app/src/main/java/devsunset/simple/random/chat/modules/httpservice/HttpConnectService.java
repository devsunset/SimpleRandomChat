package devsunset.simple.random.chat.modules.httpservice;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HttpConnectService {

    String URL = "https://src-server.firebaseapp.com/";

    /**
     * App Notice
     * @param param
     * @return
     */
    @POST("appNotice")
    Call<DataVo> appNotice();

    /**
     * App Info Init
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("appInfoInit")
    Call<DataVo> appInfoInit(@FieldMap HashMap<String, Object> param);

    /**
     * App Info Update
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("appInfoUpdate")
    Call<DataVo> appInfoUpdate(@FieldMap HashMap<String, Object> param);

    /**
     * Send Message
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("sendMessage")
    Call<DataVo> sendMessage(@FieldMap HashMap<String, Object> param);

    /**
     * Reply Message
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("replyMessage")
    Call<DataVo> replyMessage(@FieldMap HashMap<String, Object> param);

    /**
     * Retry Message
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("retryMessage")
    Call<DataVo> retryMessage(@FieldMap HashMap<String, Object> param);

    /**
     * Good Bye Message
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("goodbyeMessage")
    Call<DataVo> goodbyeMessage(@FieldMap HashMap<String, Object> param);

    /**
     * Get Image Data
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("getImageData")
    Call<DataVo> getImageData(@FieldMap HashMap<String, Object> param);

    /**
     * Get Voice Data
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("getVoiceData")
    Call<DataVo> getVoiceData(@FieldMap HashMap<String, Object> param);

    /**
     * Request Black List
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("requstBlackList")
    Call<DataVo> requstBlackList(@FieldMap HashMap<String, Object> param);

    /**
     * Request Voc
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("requestVoc")
    Call<DataVo> requestVoc(@FieldMap HashMap<String, Object> param);

    /**
     * Error Stack Trace
     * @param param
     * @return
     */
    @FormUrlEncoded
    @POST("errorStackTrace")
    Call<DataVo> errorStackTrace(@FieldMap HashMap<String, Object> param);


    /**
     * GET 방식
     * URL/posts/{userId} 호출.
     * @Path("userId")
     * "http://jsonplaceholder.typicode.com/posts/1"
     * @param userId 요청에 필요한 userId
     * @return DataVo
     *
     * @GET("/posts/{userId}")
     * Call<DataVo> getData(@Path("userId") String userId);
     */

    /**
     * GET 방식
     * @Query("userId")
     * "http://jsonplaceholder.typicode.com/posts?userId=1"
     * @param userId 요청에 필요한 userId
     * @return DataVo
     *
     * @GET("/posts")
     * Call<List < DataVo>> getDataSub(@Query("userId") String userId);
     */

    /**
     * POST 방식
     * @FieldMap HashMap<String, Object> param :
     * Field 형식을 통해 넘겨주는 값들이 여러 개일 때 FieldMap을 사용함.
     * Retrofit에서는 Map 보다는 HashMap 권장.
     * @FormUrlEncoded Field 형식 사용 시 Form이 Encoding 되어야 하기 때문에 사용하는 어노테이션
     * Field 형식은 POST 방식에서만 사용가능.
     * @param param 요청에 필요한 값들.
     * @return DataVo 객체를 JSON 형태로 반환.
     */

    /**
     * PUT 방식
     * @Body DataVo param : 통신을 통해 전달하는 값이 특정 JSON 형식일 경우
     * 매번 JSON 으로 변환하지 않고, 객체를 통해서 넘겨주는 방식.
     * PUT 뿐만 아니라 다른 방식에서도 사용가능.
     * @param param 전달 데이터
     * @return DataVo 객체를 JSON 형태로 반환.
     *
     * @PUT("/posts/1")
     * Call<DataVo> putData(@Body DataVo param);
     */

    /**
     * PATCH 방식
     * @FIeld("title") String title : patch 방식을 통해 title 에 해당하는 값을 넘기기 위해 사용.
     * @FormUrlEncoded Field 형식 사용 시 Form이 Encoding 되어야 하기 때문에 사용하는 어노테이션
     * @param title
     * @return
     *
     * @FormUrlEncoded
     * @PATCH("/posts/1")
     * Call<DataVo> patchData(@Field("title") String title);
     */

    /**
     * DELETE 방식
     * Call<ResponseBody> : ResponseBody는 통신을 통해 되돌려 받는 값이 없을 경우 사용.
     * @return
     *
     * @DELETE("/posts/1")
     * Call<ResponseBody> deleteData();
     *
     * DELETE 방식에서 @Body를 사용하기 위해서는 아래처럼 해야함.
     * @HTTP(method = "DELETE", path = "/Arahant/Modification/Profile/Image/User", hasBody = true)
     * Call<ResponseBody> delete(@Body RequestGet parameters);
     */
}