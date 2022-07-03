package com.wrd0wl.trackmypantry.remote;

import com.wrd0wl.trackmypantry.model.ItemData;
import com.wrd0wl.trackmypantry.model.LoginData;
import com.wrd0wl.trackmypantry.model.LoginResponse;
import com.wrd0wl.trackmypantry.model.ProductData;
import com.wrd0wl.trackmypantry.model.ProductResponse;
import com.wrd0wl.trackmypantry.model.RegisterData;
import com.wrd0wl.trackmypantry.model.RegisterResponse;
import com.wrd0wl.trackmypantry.model.VoteData;
import com.wrd0wl.trackmypantry.model.VoteResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebService {
    @POST("users")
    Call<RegisterResponse> registerUser(@Body RegisterData registerCredentials);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginData loginData);

    @GET("products")
    Call<ProductResponse> getProduct(@Query("barcode") String barcode, @Header("Authorization") String token);

    @POST("products")
    Call<ItemData> postProduct(@Header("Authorization") String token, @Body ProductData productData);

    @DELETE("products/{id}")
    Call<ItemData> deleteProduct(@Path("id") String id, @Header("Authorization") String token);

    @POST("votes")
    Call<VoteResponse> voteProduct(@Header("Authorization") String token, @Body VoteData voteData);
}
