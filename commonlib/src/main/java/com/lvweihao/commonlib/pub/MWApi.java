package com.lvweihao.commonlib.pub;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvweihao.commonlib.net.AuthResult;
import com.lvweihao.commonlib.net.HttpResult;
import com.lvweihao.commonlib.net.HttpResultUAP;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by lv.weihao on 2018/1/15.
 *
 * @GET 表明这是get请求
 * @POST 表明这是post请求
 * @PUT 表明这是put请求
 * @DELETE 表明这是delete请求
 * @PATCH 表明这是一个patch请求，该请求是对put请求的补充，用于更新局部资源
 * @HEAD 表明这是一个head请求
 * @OPTIONS 表明这是一个option请求
 * @HTTP 通用注解, 可以替换以上所有的注解，其拥有三个属性：method，path，hasBody
 * @Headers 用于添加固定请求头，可以同时添加多个。通过该注解添加的请求头不会相互覆盖，而是共同存在
 * @Header 作为方法的参数传入，用于添加不固定值的Header，该注解会更新已有的请求头
 * @Body 多用于post请求发送非表单数据, 比如想要以post方式传递json格式数据
 * @Filed 多用于post请求中表单字段, Filed和FieldMap需要FormUrlEncoded结合使用
 * @PartMap 用于表单字段, 默认接受的类型是Map<String,REquestBody>，可用于实现多文件上传
 * <p>
 * Part标志上文的内容可以是富媒体形势，比如上传一张图片，上传一段音乐，即它多用于字节流传输。
 * 而Filed则相对简单些，通常是字符串键值对。
 * </p>
 * Part标志上文的内容可以是富媒体形势，比如上传一张图片，上传一段音乐，即它多用于字节流传输。
 * 而Filed则相对简单些，通常是字符串键值对。
 * @Path 用于url中的占位符,{占位符}和PATH只用在URL的path部分，url中的参数使用Query和QueryMap代替，保证接口定义的简洁
 * @Query 用于Get中指定参数
 * @QueryMap 和Query使用类似
 * @FieldMap 和File使用类似
 * @Url 指定请求路径
 */
public interface MWApi {
    //开发
//    String uapUrl = "http://192.168.22.221:8001/authorization/";

    //测试
//    String uapUrl = "http://192.168.22.223:2205/auth/authorization/";

    /**
     * 正式
     */
    String uapUrl = "https://www.hzwsjds.com/auth/authorization/";

    /**
     * 平台认证接口
     * Username: pukp*r$r#J0w4fAe
     * Password: lbgVF&LheDiqNpTNjZm@z95C3l83!Gv#
     * Authorization:Basic cHVrcCpyJHIjSjB3NGZBZTpsYmdWRiZMaGVEaXFOcFROalptQHo5NUMzbDgzIUd2Iw== 通过用户名和密码生成的消息头
     *
     * @param map
     * @return
     */
    @Headers("Authorization:Basic cHVrcCpyJHIjSjB3NGZBZTpsYmdWRiZMaGVEaXFOcFROalptQHo5NUMzbDgzIUd2Iw==")
    @POST(uapUrl + "oauth/token")
    Observable<AuthResult> authentication(@QueryMap Map<String, String> map);

    /**
     * 平台注销接口
     *
     * @param token
     * @return
     */
    @Headers("Authorization:Basic cHVrcCpyJHIjSjB3NGZBZTpsYmdWRiZMaGVEaXFOcFROalptQHo5NUMzbDgzIUd2Iw==")
    @DELETE(uapUrl + "oauth/token")
    Observable<HttpResult<Object>> logout(@QueryMap Map<String, String> token);

    /**
     * 平台短信发送接口
     *
     * @param phoneNum 手机号码
     * @return
     */
    @Headers("Authorization:Basic cHVrcCpyJHIjSjB3NGZBZTpsYmdWRiZMaGVEaXFOcFROalptQHo5NUMzbDgzIUd2Iw==")
    @POST(uapUrl + "sms")
    Observable<HttpResultUAP<Object>> sendSMS(@Body Map<String, String> phoneNum);

    /**
     * 平台注册
     *
     * @param
     * @return
     */
    @Headers("Authorization:Basic cHVrcCpyJHIjSjB3NGZBZTpsYmdWRiZMaGVEaXFOcFROalptQHo5NUMzbDgzIUd2Iw==")
    @POST(uapUrl + "user/regist")
    Observable<HttpResultUAP<Object>> regist(@Body Map<String, String> map);

    /**
     * 平台密码找回
     *
     * @param
     * @return
     */
    @Headers("Authorization:Basic cHVrcCpyJHIjSjB3NGZBZTpsYmdWRiZMaGVEaXFOcFROalptQHo5NUMzbDgzIUd2Iw==")
    @PUT(uapUrl + "user/password")
    Observable<HttpResultUAP<Object>> revampPassword(@Body Map<String, String> map);

    /**
     * 平台密码修改
     *
     * @param
     * @return
     */
    @Headers("Authorization:Basic cHVrcCpyJHIjSjB3NGZBZTpsYmdWRiZMaGVEaXFOcFROalptQHo5NUMzbDgzIUd2Iw==")
    @PUT(uapUrl + "user/password2")
    Observable<HttpResultUAP<Object>> resetPassword(@Body Map<String, String> map);

    /**
     * 登陆接口
     *
     * @param token 放在请求头里
     * @return
     */
    @GET("user/login")
    Observable<HttpResult<MWUser>> login(@Header("Authorization") String token);

    /**
     * 通用Get请求
     *
     * @param path  接口的url路径
     * @param token 放在请求头里
     * @param map
     * @return JsonArray
     */
    @GET
    Observable<HttpResult<JSONArray>> getCommonArrayService(@Url String path, @Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 通用Get请求
     *
     * @param path  接口的url路径
     * @param token 放在请求头里
     * @param map
     * @return JsonObject
     */
    @GET
    Observable<HttpResult<JSONObject>> getCommonObjectService(@Url String path, @Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 通用Get请求
     *
     * @param path  接口的url路径
     * @param token 放在请求头里
     * @param map
     * @return Object
     */
    @GET
    Observable<HttpResult<Object>> getCommonService(@Url String path, @Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 通用Put请求
     *
     * @param path  接口的url路径
     * @param token 放在请求头里
     * @param map
     * @return Object
     */
    @PUT
    Observable<HttpResult<Object>> putCommonObjectService(@Url String path, @Header("Authorization") String token, @Body Map<String, String> map);

    /**
     * Post
     * 通用Pst请求
     *
     * @param path  接口的url路径
     * @param token 放在请求头里
     * @param map
     * @return Object
     */
    @POST
    Observable<HttpResult<Object>> postBodyObjectService(@Url String path, @Header("Authorization") String token, @Body Map<String, String> map);

    /**
     * 获取统计分析查询结果
     *
     * @param token
     * @param map
     * @return
     */

    @GET("glz/typetj")
    Observable<HttpResult<JSONObject>> getAnalyseResult(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 获取统计分析查询详细（含暂存点明细分页）
     *
     * @param token
     * @param map
     * @return
     */
    @GET("glz/jsdj")
    Observable<HttpResult<JSONArray>> getAnalyseDetailResult(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 头像上传
     *
     * @param token
     * @param id
     * @param file
     * @return
     */
    @Multipart
    @POST("user/uploadRYZP")
    Observable<HttpResult<JSONObject>> upLoad(@Header("Authorization") String token, @Query("id") String id, @Part MultipartBody.Part file);
}
