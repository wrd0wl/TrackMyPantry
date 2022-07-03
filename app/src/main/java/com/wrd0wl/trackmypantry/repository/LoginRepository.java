package com.wrd0wl.trackmypantry.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wrd0wl.trackmypantry.model.LoginData;
import com.wrd0wl.trackmypantry.model.LoginResponse;
import com.wrd0wl.trackmypantry.remote.RetrofitClient;
import com.wrd0wl.trackmypantry.remote.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private final WebService webService;


    public LoginRepository() {
        this.webService = RetrofitClient.getRetrofitInstance().create(WebService.class);
    }

    public LiveData<LoginResponse> login(LoginData loginData, Context context){
        final MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        webService.loginUser(loginData).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                    Toast.makeText(context, "You are logged in successfully! ", Toast.LENGTH_LONG).show();
                }
                else{
                    data.setValue(null);
                    Toast.makeText(context, "The user with these credentials doesn't exist. Please, register!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                data.setValue(null);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return data;
    }
}
