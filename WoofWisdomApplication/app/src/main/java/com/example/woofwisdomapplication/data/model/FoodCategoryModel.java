package com.example.woofwisdomapplication.data.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.woofwisdomapplication.R;

import java.io.Serializable;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class FoodCategoryModel implements Serializable {

    private transient String foodCategory;
    private transient Context context;
    public FoodCategoryModel(Context ctx,String foodCategory) {
        this.foodCategory = foodCategory;
        this.context = ctx;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public Drawable getImage(){
        if(foodCategory.contains("drinks"))
        {
           return  context.getResources().getDrawable(R.drawable.drinks);
        }else if(foodCategory.contains("fruits"))
        {
            return  context.getResources().getDrawable(R.drawable.fruits);
        }else if(foodCategory.contains("vegetables"))
        {
            return  context.getResources().getDrawable(R.drawable.vegetables);
        }else if(foodCategory.contains("chicken, meat and fish"))
        {
            return  context.getResources().getDrawable(R.drawable.chicken_meat_and_fish);
        }else if(foodCategory.contains("sweet and spicy, nuts"))
        {
            return  context.getResources().getDrawable(R.drawable.sweet_and_spicy_nuts);
        }else if(foodCategory.contains("dairy products and eggs"))
        {
            return  context.getResources().getDrawable(R.drawable.dairy_products_and_eggs);
        }else
        {
            return  context.getResources().getDrawable(R.drawable.grain_and_starches);
        }
    }
}
