package com.wrd0wl.trackmypantry.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.wrd0wl.trackmypantry.R;

public class BarcodeScannerFragment extends Fragment {

    private NavController navController;
    private Bundle bundle;
    private String fragment;

    public BarcodeScannerFragment(){
        // Required empty public constructor
    }

    private final ActivityResultLauncher<ScanOptions> fragmentLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() != null) {
                    bundle.putString("scanBarcode", result.getContents());
                    if(fragment.equals("add")){
                        navController.navigate(R.id.action_barcodeScannerFragment_to_addItemFragment, bundle);
                    }
                    else if(fragment.equals("home")){
                        navController.navigate(R.id.action_barcodeScannerFragment_to_homeFragment, bundle);
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
        Button scan = view.findViewById(R.id.scan);
        scan.setOnClickListener(v -> scanFromFragment());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        bundle = new Bundle();
        Bundle fragmentBundle = getArguments();
        fragment = fragmentBundle != null ? fragmentBundle.getString("fragment") : null;
    }

    public void scanFromFragment() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
        options.setPrompt("Place a barcode inside the viewfinder rectangle to scan it. Press volume up button to enable flashlight.");
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);
        fragmentLauncher.launch(options);
    }
}