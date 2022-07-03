package com.wrd0wl.trackmypantry.utils;

import android.content.Context;

public class Utilities {
    public static boolean checkLoginData(String email, String password){
        boolean flag = true;
        if(email.isEmpty()){
            flag = false;
        }
        if(password.isEmpty()){
            flag = false;
        }
        return flag;
    }

    public static boolean checkRegData(String name, String email, String password){
        boolean flag = true;
        if(name.isEmpty()){
            flag = false;
        }
        if(email.isEmpty()){
            flag = false;
        }
        if(password.isEmpty()){
            flag = false;
        }
        return flag;
    }

    public static boolean checkItemData(String name, String description, String barcode, String category, String expiryDate){
        boolean flag = true;
        if(name.isEmpty()){
            flag = false;
        }
        if(description.isEmpty()){
            flag = false;
        }
        if(barcode.isEmpty()){
            flag = false;
        }
        if(category.isEmpty()){
            flag = false;
        }
        if(expiryDate == null){
            flag = false;
        }
        return flag;
    }


    public static int getCategoryImageId(String spinnerItem, Context context){
        switch (spinnerItem){
            case "Fruits":
                return context.getResources().getIdentifier("fruits", "drawable", context.getPackageName());
            case "Vegetables":
                return context.getResources().getIdentifier("vegetables", "drawable", context.getPackageName());
            case "Dairy":
                return context.getResources().getIdentifier("dairy", "drawable", context.getPackageName());
            case "Bread and Bakery":
                return context.getResources().getIdentifier("bread", "drawable", context.getPackageName());
            case "Canned Goods":
                return context.getResources().getIdentifier("canned", "drawable", context.getPackageName());
            case "Meat":
                return context.getResources().getIdentifier("meat", "drawable", context.getPackageName());
            case "Fish":
                return context.getResources().getIdentifier("fish", "drawable", context.getPackageName());
            case "Cereal":
                return context.getResources().getIdentifier("cereal", "drawable", context.getPackageName());
            case "Pasta and Rice":
                return context.getResources().getIdentifier("pasta", "drawable", context.getPackageName());
            case "Beverages":
                return context.getResources().getIdentifier("beverages", "drawable", context.getPackageName());
            case "Coffee and Tea":
                return context.getResources().getIdentifier("coffee", "drawable", context.getPackageName());
            case "Oils and Condiments":
                return context.getResources().getIdentifier("oils", "drawable", context.getPackageName());
            case "Snacks":
                return context.getResources().getIdentifier("snacks", "drawable", context.getPackageName());
            default:
                return context.getResources().getIdentifier("other", "drawable", context.getPackageName());
        }
    }
}
