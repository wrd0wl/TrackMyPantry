package com.wrd0wl.trackmypantry.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.wrd0wl.trackmypantry.model.ItemData;
import com.wrd0wl.trackmypantry.model.ProductData;
import com.wrd0wl.trackmypantry.model.ProductResponse;
import com.wrd0wl.trackmypantry.model.VoteData;
import com.wrd0wl.trackmypantry.repository.PantryRepository;

import java.util.ArrayList;
import java.util.List;

public class PantryViewModel extends AndroidViewModel {
    private final PantryRepository pantryRepository;

    public PantryViewModel(@NonNull Application application) {
        super(application);
        pantryRepository = new PantryRepository(getApplication().getApplicationContext());
    }

    public MutableLiveData<ItemData> getItemLivedata(){
        return pantryRepository.getItemLiveData();
    }

    public MutableLiveData<List<ItemData>> getItemListLiveData(){
        return pantryRepository.getListItemLiveData();
    }

    public MutableLiveData<ProductResponse> getProductLiveData(){
        return pantryRepository.getProductLiveData();
    }

    public void postRemoteProduct(ProductData product, String token){
        pantryRepository.postNewProduct(product, token);
    }

    public void getRemoteItemsBarcode(String barcode, String token){
        pantryRepository.getProductByBarcode(barcode, token);
    }

    public void deleteRemoteItem(String id, String token){
        pantryRepository.deleteProduct(id, token);
    }

    public void postRemoteVote(VoteData voteData, String token){
        pantryRepository.voteProduct(voteData, token);
    }

    public void getAllItems(String userEmail){
        pantryRepository.getItemList(userEmail);
    }

    public void getItemById(String userEmail, String id){
        pantryRepository.getItemById(userEmail, id);
    }

    public void getListByName(String userEmail, String name){
        pantryRepository.getItemListByName(userEmail, name);
    }

    public void getListByCategories(String userEmail, ArrayList<String> categories){
        pantryRepository.getItemListByCategories(userEmail, categories);
    }

    public void insertItem(ItemData item, String userEmail){
        pantryRepository.insertItem(item, userEmail);
    }

    public void deleteItem(ItemData item, String userEmail){
        pantryRepository.deleteItem(item, userEmail);
    }

    public void updateItem(ItemData item, String userEmail){
        pantryRepository.updateItem(item, userEmail);
    }
}
