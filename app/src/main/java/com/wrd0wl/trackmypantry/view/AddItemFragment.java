package com.wrd0wl.trackmypantry.view;

import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wrd0wl.trackmypantry.R;
import com.wrd0wl.trackmypantry.model.ItemData;
import com.wrd0wl.trackmypantry.model.ProductData;
import com.wrd0wl.trackmypantry.utils.Utilities;
import com.wrd0wl.trackmypantry.viewmodel.PantryViewModel;

import java.util.Calendar;

public class AddItemFragment extends Fragment {

    private Context context;

    private SharedPreferences sharedPreferences;

    private NavController navController;

    private EditText etName, etDescription, etBarcode;

    private TextView tvSetExpiryDate;

    private PantryViewModel pantryViewModel;

    private String id;

    private String name;

    private String description;

    private String barcode;

    private boolean isRemote;

    private String category;

    private String expiryDate;

    private DatePickerDialog.OnDateSetListener listener;

    public AddItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = requireContext();

        navController = Navigation.findNavController(view);

        Bundle bundle = getArguments();
        Bundle fragmentBundle = new Bundle();
        pantryViewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        sharedPreferences = this.requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Spinner spinner = view.findViewById(R.id.spinner);

        etName = view.findViewById(R.id.enterNameInput);
        etDescription = view.findViewById(R.id.enterDescriptionInput);
        etBarcode = view.findViewById(R.id.enterBarcodeInput);
        TextView tvScanBarcodeText = view.findViewById(R.id.tvScanBarcode);
        ImageView ivScanBarcode = view.findViewById(R.id.ivScanBarcode);
        tvSetExpiryDate = view.findViewById(R.id.tvSetExpiryDate);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        tvSetExpiryDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, listener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        listener = (dateView, selYear, selMonth, selDay) -> {
            if(selMonth < 9){
                expiryDate = selYear + "-0" + (selMonth + 1);
            }
            else{
                expiryDate = selYear + "-" + (selMonth + 1);
            }
            if(selDay < 10){
                expiryDate = expiryDate + "-0" + selDay;
            }
            else{
                expiryDate = expiryDate + "-" + selDay;
            }

            tvSetExpiryDate.setText(expiryDate);
        };

        if(bundle != null && bundle.getString("scanBarcode") == null){
            id = bundle.getString("id");
            name = bundle.getString("name");
            etName.setText(name);
            etName.setEnabled(false);
            description = bundle.getString("description");
            etDescription.setText(description);
            etDescription.setEnabled(false);
            barcode = bundle.getString("barcode");
            etBarcode.setText(barcode);
            etBarcode.setEnabled(false);
            isRemote = true;
        }
        else if(bundle != null && bundle.getString("scanBarcode") != null){
            barcode = bundle.getString("scanBarcode");
            etBarcode.setText(barcode);
            etBarcode.setEnabled(false);
        }else{
            tvScanBarcodeText.setVisibility(View.VISIBLE);
            ivScanBarcode.setVisibility(View.VISIBLE);
        }

        fragmentBundle.putString("fragment", "add");
        ivScanBarcode.setOnClickListener(v -> navController.navigate(R.id.action_addItemFragment_to_barcodeScannerFragment, fragmentBundle));

        Button btnAdd = view.findViewById(R.id.buttonAdd);

        btnAdd.setOnClickListener(v -> {
            category = spinner.getSelectedItem().toString();
            if(!isRemote){
                String token = sharedPreferences.getString("token", null);
                String sessionToken = sharedPreferences.getString("sessionToken", null);
                name = etName.getText().toString();
                description = etDescription.getText().toString();
                if(barcode == null){
                    barcode = etBarcode.getText().toString();
                }
                ProductData newProduct = new ProductData(sessionToken, name, description, barcode,false);
                postNewProduct(newProduct, token);
            }
            else{
                insertNewProduct();
            }
        });
    }

    private void postNewProduct(ProductData productData, String token){
        pantryViewModel.getItemLivedata().observe((LifecycleOwner) requireContext(), itemDB -> {
            if(itemDB != null){
                id = itemDB.getId();
                insertNewProduct();
            }
        });
        pantryViewModel.postRemoteProduct(productData, token);
    }

    private void insertNewProduct(){
        if(Utilities.checkItemData(name, description, barcode, category, expiryDate)){
            String email = sharedPreferences.getString("email", null);
            ItemData newItem = new ItemData(id, name, description, barcode, 1, category, expiryDate, isRemote, email);
            pantryViewModel.insertItem(newItem, email);
        }
        else {
            Toast.makeText(context, "Cannot insert this product. Please, try again!", Toast.LENGTH_LONG).show();
        }
    }
}