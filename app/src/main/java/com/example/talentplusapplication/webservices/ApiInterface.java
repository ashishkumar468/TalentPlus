package com.example.talentplusapplication.webservices;



import com.example.talentplusapplication.Proxy.BaseResponse;
import com.example.talentplusapplication.Proxy.LoginInput;
import com.example.talentplusapplication.Proxy.ResponseEditProfile;
import com.example.talentplusapplication.Proxy.ResponseFollowers;
import com.example.talentplusapplication.Proxy.ResponseFollowing;
import com.example.talentplusapplication.Proxy.ResponseGetAllComments;
import com.example.talentplusapplication.Proxy.ResponseGetAllPost;
import com.example.talentplusapplication.Proxy.ResponseGetUserProfile;
import com.example.talentplusapplication.Proxy.ResponseIsUserExist;
import com.example.talentplusapplication.Proxy.ResponseLogin;
import com.example.talentplusapplication.Proxy.ResponseRegistration;
import com.example.talentplusapplication.Proxy.SearchProxy;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    String STATUS_CODE_SUCCESSFUL ="200";
    String STATUS_CODE_FAILUREL ="404";
    String SUCCESS="Success";

    /*//http://qa.shyaamadverts.com/qa-adverts/rest-app-services/get-adv-app?publisherId=7&host=&websiteId=2&endUserIp=sdf&state=Maharashtra
    @GET("get-adv-app")
    Call<List<Adv_Result.Result>> getAllAds(@Query("publisherId") String publisherId,
                                            @Query("host") String host,
                                            @Query("websiteId") String websiteId,
                                            @Query("endUserIp") String endUserId,
                                            @Query("state") String state);*/



    // http://talentplus.drline.in/talentplus-webapp/user-api/register-user

    @POST("user-api/user-login")
    Call<ResponseLogin> login(@Body JsonObject jObj);

    @POST("user-api/register-user")
    Call<ResponseRegistration> registration(@Body JsonObject jObj);

    @POST("user-api/update-password")
    Call<ResponseRegistration> forgotPassword(@Body JsonObject jObj);

    @GET("user-api/is-user-exists")
    Call<ResponseIsUserExist> isUserExist(@Query("contactNo") String contact);

    @POST("post-api/save-post")
    Call<ResponseRegistration> savePost(@Body JsonObject jObj);

    @GET("post-api/get-all-posts")
    Call<ResponseGetAllPost> getAllPost(@Query("userId") String userId);

    @GET("user-api/get-user-profile")
    Call<ResponseGetUserProfile> getUserProfile(@Query("userId") String userId);

    @POST("user-api/edit-profilepic-username")
    Call<ResponseEditProfile> saveEditProfile(@Body JsonObject jObj);

//    @GET("followers-api/get-all-followers")
//    Call<ResponseFollowers> getAllFollowers(@Query("userId") String userId);

    @GET("followers-api/get-all-followers")
    Call<ResponseFollowing> getAllFollowers(@Query("userId") String userId);

    @GET("following-api/get-all-following")
    Call<ResponseFollowing> getAllFollowing(@Query("userId") String userId);

    @GET("comments-api/get-all-comments")
    Call<ResponseGetAllComments> getAllComments(@Query("postsId") String postsId);

    @POST("comments-api/save-comments")
    Call<BaseResponse> saveComment(@Body JsonObject jObj);

    @POST("thumbs-api/save-thumbs-up")
    Call<BaseResponse> saveThumbsUp(@Body JsonObject jObj);

    @GET("post-api/update-share-count")
    Call<BaseResponse> updateShareCount(@Query("postsId") String postsId);

    @GET("user-api/get-other-user-profile")
    Call<ResponseGetUserProfile> getOtherUserProfile(@Query("loginUserId") String loginUserId,
                                                     @Query("otherUserId") String otherUserId);

    @POST("following-api/save-following")
    Call<BaseResponse> saveFollowing(@Body JsonObject jObj);

    @GET("user-api/search-user-by-name")
    Call<List<SearchProxy>> searchUserByName(@Query("userName") String userName);


}