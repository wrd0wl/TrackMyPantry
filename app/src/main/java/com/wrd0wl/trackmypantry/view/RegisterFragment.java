package com.wrd0wl.trackmypantry.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wrd0wl.trackmypantry.R;
import com.wrd0wl.trackmypantry.model.RegisterData;
import com.wrd0wl.trackmypantry.utils.Utilities;
import com.wrd0wl.trackmypantry.viewmodel.RegisterViewModel;

public class RegisterFragment extends Fragment {

    EditText etName, etEmail, etPass;

    Button btnReg;

    RegisterViewModel registerViewModel;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.etRegName);
        etEmail = view.findViewById(R.id.etRegEmail);
        etPass = view.findViewById(R.id.etRegPass);

        btnReg = view.findViewById(R.id.btnReg);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);


        btnReg.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String pass = etPass.getText().toString();
            if(Utilities.checkRegData(name, email, pass)){
                RegisterData registerCredentials = new RegisterData(name, email,pass);
                registerViewModel.setRegisterResponseLiveData(registerCredentials, getContext());
                registerViewModel.getRegisterResponseLiveData().observe(getViewLifecycleOwner(), registerResponse -> {
                    if(registerResponse != null){
                        Toast.makeText(getContext(), "You are registered!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                Toast.makeText(getContext(), "Please, enter correct credentials!", Toast.LENGTH_LONG).show();
            }
        });
    }
}