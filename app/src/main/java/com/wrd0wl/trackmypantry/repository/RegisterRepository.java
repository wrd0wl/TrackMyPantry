package com.wrd0wl.trackmypantry.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wrd0wl.trackmypantry.model.RegisterData;
import com.wrd0wl.trackmypantry.model.RegisterResponse;
import com.wrd0wl.trackmypantry.remote.RetrofitClient;
import com.wrd0wl.trackmypantry.remote.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRepository {
    private final WebService webService;

    public RegisterRepository(){
        this.webService = RetrofitClient.getRetrofitInstance().create(WebService.class);
    }

    public LiveData<RegisterResponse> register(RegisterData registerData, Context context){
        MutableLiveData<RegisterResponse> data = new MutableLiveData<>();
        webService.registerUser(registerData).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                    Toast.makeText(context, "Your registration was successfully done! Now you can login.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "This user already exists. Please, enter another credentials!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return data;
    }
}
