package com.wrd0wl.trackmypantry.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wrd0wl.trackmypantry.model.RegisterData;
import com.wrd0wl.trackmypantry.model.RegisterResponse;
import com.wrd0wl.trackmypantry.repository.RegisterRepository;

public class RegisterViewModel extends AndroidViewModel {
    RegisterRepository registerRepository;
    LiveData<RegisterResponse> registerResponseLiveData;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.registerRepository = new RegisterRepository();
    }

    public void setRegisterResponseLiveData(RegisterData registerData, Context context) {
        this.registerResponseLiveData = registerRepository.register(registerData, context);
    }

    public LiveData<RegisterResponse> getRegisterResponseLiveData() {
        return registerResponseLiveData;
    }
}
