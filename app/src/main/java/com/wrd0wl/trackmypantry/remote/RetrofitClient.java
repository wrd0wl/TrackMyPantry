package com.wrd0wl.trackmypantry.remote;

import static com.wrd0wl.trackmypantry.constants.Constants.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit RETROFIT;
    public static Retrofit getRetrofitInstance() {
        if (RETROFIT == null) {
            RETROFIT = new retrofit2.Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return RETROFIT;
    }
}
