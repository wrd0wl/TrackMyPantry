package com.wrd0wl.trackmypantry.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.wrd0wl.trackmypantry.database.PantryDatabase;
import com.wrd0wl.trackmypantry.model.ItemData;
import com.wrd0wl.trackmypantry.model.ProductData;
import com.wrd0wl.trackmypantry.model.ProductResponse;
import com.wrd0wl.trackmypantry.model.VoteData;
import com.wrd0wl.trackmypantry.model.VoteResponse;
import com.wrd0wl.trackmypantry.remote.RetrofitClient;
import com.wrd0wl.trackmypantry.remote.WebService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantryRepository {
    private final PantryDatabase pantryDatabase;
    private final WebService webService;
    private final MutableLiveData<ItemData> item;
    private final MutableLiveData<List<ItemData>> listItem;
    private final MutableLiveData<ProductResponse> product;
    private final MutableLiveData<VoteResponse> vote;
    private final Executor executor;
    private final Context context;


    public PantryRepository(Context context) {
        this.pantryDatabase = PantryDatabase.getDBInstance(context);
        this.webService = RetrofitClient.getRetrofitInstance().create(WebService.class);
        this.item = new MutableLiveData<>();
        this.listItem = new MutableLiveData<>();
        this.product = new MutableLiveData<>();
        this.vote = new MutableLiveData<>();
        this.executor = Executors.newFixedThreadPool(2);
        this.context = context;
    }

    public MutableLiveData<ItemData> getItemLiveData() { return item; }

    public MutableLiveData<List<ItemData>> getListItemLiveData() {
        return listItem;
    }

    public MutableLiveData<ProductResponse> getProductLiveData(){
        return product;
    }

    public void getProductByBarcode(String barcode, String token){
        webService.getProduct(barcode, "Bearer " + token).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if(response.isSuccessful()){
                    product.postValue(response.body());
                }
                else{
                    product.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                product.postValue(null);
            }
        });
    }

    public void postNewProduct(ProductData productData, String token){
        webService.postProduct("Bearer " + token, productData).enqueue(new Callback<ItemData>() {
            @Override
            public void onResponse(@NonNull Call<ItemData> call, @NonNull Response<ItemData> response) {
                if(response.isSuccessful()){
                    item.postValue(response.body());
                }
                else{
                    item.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemData> call, @NonNull Throwable t) {
                item.postValue(null);
            }
        });
    }

    public void deleteProduct(String id, String token){
        webService.deleteProduct(id, "Bearer " + token).enqueue(new Callback<ItemData>() {
            @Override
            public void onResponse(@NonNull Call<ItemData> call, @NonNull Response<ItemData> response) {
                if(response.isSuccessful()){
                    item.postValue(response.body());
                }
                else {
                    item.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemData> call, @NonNull Throwable t) {
                item.postValue(null);
            }
        });
    }

    public void voteProduct(VoteData voteData, String token){
        webService.voteProduct("Bearer " + token, voteData).enqueue(new Callback<VoteResponse>() {
            @Override
            public void onResponse(@NonNull Call<VoteResponse> call, @NonNull Response<VoteResponse> response) {
                if(response.isSuccessful()){
                    vote.postValue(response.body());
                }
                else{
                    vote.postValue(null);
                }
                System.out.println(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<VoteResponse> call, @NonNull Throwable t) {
                vote.postValue(null);
                System.out.println(t.getMessage());
            }
        });
    }

    public void getItemList(String userEmail){
        executor.execute(() -> {
            List<ItemData> itemList = pantryDatabase.pantryDAO().get(userEmail);
            if(itemList.size() > 0){
                listItem.postValue(itemList);
            }
            else{
                listItem.postValue(null);
            }
        });
    }

    public void getItemById(String userEmail, String id){
        executor.execute(() -> {
            ItemData newItem = pantryDatabase.pantryDAO().getById(userEmail, id);
            item.postValue(newItem);
        });
    }

    public void getItemListByName(String userEmail, String name){
        executor.execute(() -> {
            List<ItemData> itemList = pantryDatabase.pantryDAO().getByName(userEmail, name);
            if(itemList.size() > 0){
                listItem.postValue(itemList);
            }
            else{
                listItem.postValue(null);
            }
        });
    }

    public void getItemListByCategories(String userEmail, ArrayList<String> categories){
        executor.execute(() -> {
            List<ItemData> itemList = pantryDatabase.pantryDAO().getByCategories(userEmail, categories);
            if (itemList.size() > 0){
                listItem.postValue(itemList);
            }
            else{
                listItem.postValue(null);
            }
        });
    }


    public void insertItem(ItemData itemData, String userEmail){
        executor.execute(() -> {
            pantryDatabase.pantryDAO().insert(itemData);
            getItemList(userEmail);
        });
    }

    public void deleteItem(ItemData item, String userEmail){
        executor.execute(() -> {
            pantryDatabase.pantryDAO().delete(item);
            getItemList(userEmail);
        });
    }

    public void updateItem(ItemData item, String userEmail){
        executor.execute(() -> {
            pantryDatabase.pantryDAO().update(item);
            getItemList(userEmail);
        });
    }

    public List<ItemData> getItemListRanOut(String userEmail){
        PantryDatabase mainThreadDatabase = Room.databaseBuilder(context.getApplicationContext(), PantryDatabase.class, "PantryDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        return new ArrayList<>(mainThreadDatabase.pantryDAO().getRanOut(userEmail));
    }

    public List<ItemData> getItemListExpired(String userEmail, String date){
        PantryDatabase mainThreadDatabase = Room.databaseBuilder(context.getApplicationContext(), PantryDatabase.class, "PantryDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        return new ArrayList<>(mainThreadDatabase.pantryDAO().getExpired(userEmail, date));
    }

    public List<ItemData> getItemListExpiring(String userEmail, String today, String week){
        PantryDatabase mainThreadDatabase = Room.databaseBuilder(context.getApplicationContext(), PantryDatabase.class, "PantryDatabase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        return new ArrayList<>(mainThreadDatabase.pantryDAO().getExpiring(userEmail, today, week));
    }
}
