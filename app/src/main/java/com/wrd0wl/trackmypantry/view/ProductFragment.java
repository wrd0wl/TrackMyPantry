package com.wrd0wl.trackmypantry.view;

import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.wrd0wl.trackmypantry.R;
import com.wrd0wl.trackmypantry.utils.Utilities;
import com.wrd0wl.trackmypantry.viewmodel.PantryViewModel;

public class ProductFragment extends Fragment {

    private TextView tvName, tvDescription, tvCategory, tvBarcode, tvQuantity, tvDate;
    private ImageView ivCategory;
    private NavController navController;
    private SharedPreferences sharedPreferences;
    private PantryViewModel pantryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedPreferences = this.requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        pantryViewModel = new ViewModelProvider(requireActivity()).get(PantryViewModel.class);

        tvName = view.findViewById(R.id.tvProductName);
        tvDescription = view.findViewById(R.id.tvProductDescription);
        tvCategory = view.findViewById(R.id.tvProductCategory);
        tvBarcode = view.findViewById(R.id.tvProductBarcode);
        tvQuantity = view.findViewById(R.id.tvProductQuantity);
        tvDate = view.findViewById(R.id.tvProductExpiryDate);

        ivCategory = view.findViewById(R.id.ivProductCategory);
        ImageView ivDecQuantity = view.findViewById(R.id.ivProductDecItem);
        ImageView ivIncQuantity = view.findViewById(R.id.ivProductIncItem);
        ImageView ivDelete = view.findViewById(R.id.ivProductDeleteItem);

        Bundle bundle = getArguments();
        if(bundle != null){
            pantryViewModel.getItemLivedata().observe(getViewLifecycleOwner(), itemData -> {
                if(itemData != null){
                    Glide.with(view).load(Utilities.getCategoryImageId(itemData.getCategory(), requireContext())).into(ivCategory);
                    tvName.setText(itemData.getName());
                    tvDescription.setText(itemData.getDescription());
                    tvCategory.setText(itemData.getCategory());
                    tvBarcode.setText(itemData.getBarcode());
                    tvQuantity.setText(String.valueOf(itemData.getQuantity()));
                    tvDate.setText(itemData.getExpiryDate());
                }
            });
            pantryViewModel.getItemById(sharedPreferences.getString("email", null), bundle.getString("id"));
        }

        ivDecQuantity.setOnClickListener(v -> pantryViewModel.getItemLivedata().observe(getViewLifecycleOwner(), itemData -> {
            if(itemData != null){
                if(itemData.getQuantity() > 0){
                    itemData.setQuantity(itemData.getQuantity() - 1);
                    pantryViewModel.updateItem(itemData, sharedPreferences.getString("email", null));
                    tvQuantity.setText(String.valueOf(itemData.getQuantity()));
                }
            }
        }));

        ivIncQuantity.setOnClickListener(v -> pantryViewModel.getItemLivedata().observe(getViewLifecycleOwner(), itemData -> {
            if(itemData != null){
                itemData.setQuantity(itemData.getQuantity() + 1);
                pantryViewModel.updateItem(itemData, sharedPreferences.getString("email", null));
                tvQuantity.setText(String.valueOf(itemData.getQuantity()));
            }
        }));


        ivDelete.setOnClickListener(v -> pantryViewModel.getItemLivedata().observe(getViewLifecycleOwner(), itemData -> {
            if(itemData != null){
                pantryViewModel.deleteItem(itemData, sharedPreferences.getString("email", null));
                if(itemData.isRemote()){
                    pantryViewModel.deleteRemoteItem(itemData.getId(), sharedPreferences.getString("email", null));
                }
                navController.popBackStack();
            }
        }));
    }
}