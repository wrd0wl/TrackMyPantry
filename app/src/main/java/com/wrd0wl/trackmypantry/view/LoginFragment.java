package com.wrd0wl.trackmypantry.view;

import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES;
import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES_DATE;
import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES_EMAIL;
import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES_TOKEN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wrd0wl.trackmypantry.R;
import com.wrd0wl.trackmypantry.model.LoginData;
import com.wrd0wl.trackmypantry.utils.Utilities;
import com.wrd0wl.trackmypantry.viewmodel.LoginViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginFragment extends Fragment {
    EditText etEmail, etPass;
    Button btnLogin;
    TextView tvReg;

    LoginViewModel loginViewModel;

    SharedPreferences sharedPreferences;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEmail = view.findViewById(R.id.etLoginEmail);
        etPass = view.findViewById(R.id.etLoginPass);

        btnLogin = view.findViewById(R.id.btnLogin);

        tvReg = view.findViewById(R.id.tvReg);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final NavController navController = Navigation.findNavController(view);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String pass = etPass.getText().toString();
            if(Utilities.checkLoginData(email, pass)){
                LoginData loginData = new LoginData(email, pass);
                loginViewModel.setLoginResponseLiveData(loginData, getContext());
                loginViewModel.getLoginResponseLiveData().observe(getViewLifecycleOwner(), loginResponse -> {
                    if(loginResponse != null){
                        sharedPreferences = this.requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(APP_PREFERENCES_DATE, date.format(new Date()));
                        editor.putString(APP_PREFERENCES_TOKEN, loginResponse.getToken());
                        editor.putString(APP_PREFERENCES_EMAIL, loginData.getEmail());
                        editor.apply();
                        Toast.makeText(getContext(), "You are logged in!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                Toast.makeText(getContext(), "Please, enter correct credentials!", Toast.LENGTH_LONG).show();
            }
        });

        tvReg.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_registerFragment));

    }
}