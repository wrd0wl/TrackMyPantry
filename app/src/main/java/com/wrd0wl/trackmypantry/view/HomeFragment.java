package com.wrd0wl.trackmypantry.view;

import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES;
import static com.wrd0wl.trackmypantry.constants.Constants.APP_PREFERENCES_SESSION;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wrd0wl.trackmypantry.R;
import com.wrd0wl.trackmypantry.adapter.ItemAdapter;
import com.wrd0wl.trackmypantry.model.ItemData;
import com.wrd0wl.trackmypantry.model.VoteData;
import com.wrd0wl.trackmypantry.viewmodel.PantryViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment implements ItemAdapter.HandleItemClick {

    private NavController navController;

    private SharedPreferences sharedPreferences;
    private PantryViewModel pantryViewModel;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;
    private String email;
    private SearchView searchView;
    private boolean isLocalSearch = true;
    private View currView;

    private TextView tvScanBarcode;
    private ImageView ivScanBarcode, ivAddItem;

    private String searchValue;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            default:
                return super.onOptionsItemSelected(item);
            case R.id.action_local:
                isLocalSearch = true;
                searchView.setVisibility(View.GONE);
                tvScanBarcode.setVisibility(View.GONE);
                ivScanBarcode.setVisibility(View.GONE);
                ivAddItem.setVisibility(View.GONE);
                itemListInit();
                getLocalItemList();
                return true;
            case R.id.action_search_name:
                isLocalSearch = true;
                searchView.setVisibility(View.VISIBLE);
                searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                itemListInit();
                getLocalItemListByName();
                return true;
            case R.id.action_search_category:
                isLocalSearch = true;
                searchView.setVisibility(View.GONE);
                itemListInit();
                getLocalListByCategory();
                return true;
            case R.id.action_search:
                isLocalSearch = false;
                ivAddItem.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                tvScanBarcode.setVisibility(View.VISIBLE);
                ivScanBarcode.setVisibility(View.VISIBLE);
                itemListInit();
                getRemoteItemList();
                return true;
            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear().apply();
                NavController navController = Navigation.findNavController(currView);
                navController.navigate(R.id.action_homeFragment_to_loginFragment);
                isLocalSearch = true;
                return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currView = view;

        navController = Navigation.findNavController(view);

        pantryViewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        searchView = view.findViewById(R.id.searchView);

        tvScanBarcode = view.findViewById(R.id.tvHomeScanBarcode);
        ivScanBarcode = view.findViewById(R.id.ivHomeScanBarcode);
        ivAddItem = view.findViewById(R.id.ivHomeAddItem);

        sharedPreferences = this.requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", null);
        String date = sharedPreferences.getString("date", null);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        long weekSec = 604800;
        if(date != null){
            try {
                Date loggedDay = dateFormat.parse(date);
                Date today = new Date();
                long diffTimeSec = TimeUnit.MILLISECONDS.toSeconds(today.getTime() - (loggedDay != null ? loggedDay.getTime() : 0));
                if(diffTimeSec > weekSec){
                    navController.navigate(R.id.action_homeFragment_to_loginFragment);
                }
                else{
                    itemListInit();
                    if(!isLocalSearch){
                        searchView.setQuery(searchValue, false);
                        getRemoteItemList();
                    }
                    else {
                        Bundle bundle = getArguments();
                        if(bundle != null){
                            isLocalSearch = false;
                            searchView.setQuery(bundle.getString("scanBarcode"), false);
                            getRemoteItemList();
                        }
                        else {
                            getLocalItemList();
                        }
                    }
                    ivScanBarcode.setOnClickListener(v -> {
                        Bundle fragmentAction = new Bundle();
                        fragmentAction.putString("fragment", "home");
                        navController.navigate(R.id.action_homeFragment_to_barcodeScannerFragment, fragmentAction);
                    });
                    ivAddItem.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_addItemFragment));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else{
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        }
    }

    private void itemListInit(){
        recyclerView = currView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter(getContext(), this);
        recyclerView.setAdapter(itemAdapter);
    }



    private void getRemoteItemList(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchValue = query;

                System.out.println(query);

                String token = sharedPreferences.getString("token", null);
                pantryViewModel.getProductLiveData().observe(getViewLifecycleOwner(), productResponse -> {
                    if(productResponse != null){
                        System.out.println(productResponse.getToken());
                        sharedPreferences = requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(APP_PREFERENCES_SESSION, productResponse.getToken());
                        editor.apply();
                        if(!productResponse.getProducts().isEmpty()){
                            itemAdapter.setItemList(productResponse.getProducts(), isLocalSearch);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        else{
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
                pantryViewModel.getRemoteItemsBarcode(query, token);
                ivAddItem.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                tvScanBarcode.setVisibility(View.GONE);
                ivScanBarcode.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLocalItemList(){
        pantryViewModel.getItemListLiveData().observe(getViewLifecycleOwner(), itemList -> {
            if(itemList != null){
                if(!itemList.isEmpty()){
                    itemAdapter.setItemList(itemList, isLocalSearch);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
            else{
                recyclerView.setVisibility(View.GONE);
            }
        });
        pantryViewModel.getAllItems(email);
    }

    private void getLocalItemListByName(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pantryViewModel.getItemListLiveData().observe(getViewLifecycleOwner(), itemList -> {
                    if(itemList != null){
                        if(!itemList.isEmpty()){
                            itemAdapter.setItemList(itemList, isLocalSearch);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        else{
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                    else {
                        recyclerView.setVisibility(View.GONE);
                    }
                });
                pantryViewModel.getListByName(email, "%" + query.toLowerCase() + "%");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLocalListByCategory(){
        ArrayList<String> selectedCategories = new ArrayList<>();
        String[] categories = requireActivity().getResources().getStringArray(R.array.pantry_category);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose a category");
        builder.setMultiChoiceItems(categories, null, (dialog, which, isChecked) -> {
            if(isChecked){
                selectedCategories.add(categories[which]);
            }
            else {
                selectedCategories.remove(categories[which]);
            }
        });
        builder.setPositiveButton("OK", (dialog, which) -> {
            pantryViewModel.getItemListLiveData().observe(getViewLifecycleOwner(), itemList -> {
                if(itemList != null){
                    if(!itemList.isEmpty()){
                        itemAdapter.setItemList(itemList, isLocalSearch);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                    }
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }
            });
            pantryViewModel.getListByCategories(email, selectedCategories);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    @Override
    public void itemFull(ItemData item) {
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getId());
        navController.navigate(R.id.action_homeFragment_to_productFragment, bundle);

    }

    @Override
    public void itemAdd(ItemData item) {
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getId());
        bundle.putString("name", item.getName());
        bundle.putString("description", item.getDescription());
        bundle.putString("barcode", item.getBarcode());
        navController.navigate(R.id.action_homeFragment_to_addItemFragment, bundle);
    }

    @Override
    public void itemVote(ItemData item, int rating) {
        String token = sharedPreferences.getString("token", null);
        String sessionToken = sharedPreferences.getString("sessionToken", null);
        VoteData voteData = new VoteData(sessionToken, rating, item.getId());
        pantryViewModel.postRemoteVote(voteData, token);
    }
}