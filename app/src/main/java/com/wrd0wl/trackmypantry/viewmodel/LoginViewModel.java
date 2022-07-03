package com.wrd0wl.trackmypantry.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wrd0wl.trackmypantry.model.LoginData;
import com.wrd0wl.trackmypantry.model.LoginResponse;
import com.wrd0wl.trackmypantry.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {
    LoginRepository loginRepository;
    LiveData<LoginResponse> loginResponseLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.loginRepository = new LoginRepository();
    }

    public void setLoginResponseLiveData(LoginData loginData, Context context) {
        loginResponseLiveData = loginRepository.login(loginData, context);
    }

    public LiveData<LoginResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }
}
