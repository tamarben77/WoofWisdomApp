package com.example.woofwisdomapplication.data.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.woofwisdomapplication.R;

public class FoodModel {

    String foodName,foodInfo,effectsIfDangerous,effectsIfHealthy,foodCategory;
    int ifDangerous,ifHealthy;
    Context context;

    public FoodModel(Context context,String foodName, String foodInfo, String effectsIfDangerous, String effectsIfHealthy, String foodCategory, int ifDangerous, int ifHealthy) {
        this.context=context;
        this.foodName = foodName;
        this.foodInfo = foodInfo;
        this.effectsIfDangerous = effectsIfDangerous;
        this.effectsIfHealthy = effectsIfHealthy;
        this.foodCategory = foodCategory;
        this.ifDangerous = ifDangerous;
        this.ifHealthy = ifHealthy;
    }

    public Drawable getImage(){
        if(foodName.toLowerCase().contains("avocado"))
        {
            return  context.getResources().getDrawable(R.drawable.avocado);
        }else if(foodName.toLowerCase().contains("banana"))
        {
            return  context.getResources().getDrawable(R.drawable.banana);
        }else if(foodName.toLowerCase().contains("citrus"))
        {
            return  context.getResources().getDrawable(R.drawable.citrus_fruits);
        }else if(foodName.toLowerCase().contains("coconut"))
        {
            return  context.getResources().getDrawable(R.drawable.coconut);
        }else if(foodName.toLowerCase().contains("grapes"))
        {
            return  context.getResources().getDrawable(R.drawable.grapes_and_raisins);
        }else if(foodName.toLowerCase().contains("mango"))
        {
            return  context.getResources().getDrawable(R.drawable.mango);
        }else if(foodName.toLowerCase().contains("orange"))
        {
            return  context.getResources().getDrawable(R.drawable.orange);
        }else if(foodName.toLowerCase().contains("peach"))
        {
            return  context.getResources().getDrawable(R.drawable.peach);
        }else if(foodName.toLowerCase().contains("pineapple"))
        {
            return  context.getResources().getDrawable(R.drawable.pineapple);
        }else if(foodName.toLowerCase().contains("straw"))
        {
            return  context.getResources().getDrawable(R.drawable.strawberries_and_berries);
        }else if(foodName.toLowerCase().contains("watermelon"))
        {
            return  context.getResources().getDrawable(R.drawable.watermelon_and_melon);
        }else if(foodName.toLowerCase().contains("alcohol"))
        {
            return  context.getResources().getDrawable(R.drawable.alcohol);
        }else if(foodName.toLowerCase().contains("caffeine"))
        {
            return  context.getResources().getDrawable(R.drawable.caffeine);
        }else if(foodName.toLowerCase().contains("bean"))
        {
            return  context.getResources().getDrawable(R.drawable.bean);
        }else if(foodName.toLowerCase().contains("broccoli"))
        {
            return  context.getResources().getDrawable(R.drawable.broccoli);
        }else if(foodName.toLowerCase().contains("carrot"))
        {
            return  context.getResources().getDrawable(R.drawable.carrot);
        }else if(foodName.toLowerCase().contains("celery"))
        {
            return  context.getResources().getDrawable(R.drawable.celery);
        }else if(foodName.toLowerCase().contains("corn"))
        {
            return  context.getResources().getDrawable(R.drawable.corn);
        }else if(foodName.toLowerCase().contains("cucumber"))
        {
            return  context.getResources().getDrawable(R.drawable.cucumber);
        }else if(foodName.toLowerCase().contains("garlic"))
        {
            return  context.getResources().getDrawable(R.drawable.garlic);
        }else if(foodName.toLowerCase().contains("green"))
        {
            return  context.getResources().getDrawable(R.drawable.green_beans);
        }else if(foodName.toLowerCase().contains("onion"))
        {
            return  context.getResources().getDrawable(R.drawable.onion);
        }else if(foodName.toLowerCase().contains("sweet"))
        {
            return  context.getResources().getDrawable(R.drawable.sweet_potato);
        }else if(foodName.toLowerCase().contains("tomato"))
        {
            return  context.getResources().getDrawable(R.drawable.tomato);
        }else if(foodName.toLowerCase().contains("boiled"))
        {
            return  context.getResources().getDrawable(R.drawable.boiled_meat);
        }else if(foodName.toLowerCase().contains("bones"))
        {
            return  context.getResources().getDrawable(R.drawable.bones);
        }else if(foodName.toLowerCase().contains("salmon"))
        {
            return  context.getResources().getDrawable(R.drawable.salmon);
        }else if(foodName.toLowerCase().contains("raw"))
        {
            return  context.getResources().getDrawable(R.drawable.raw_fish);
        }else if(foodName.toLowerCase().contains("tuna"))
        {
            return  context.getResources().getDrawable(R.drawable.tuna);
        }else if(foodName.toLowerCase().contains("candy"))
        {
            return  context.getResources().getDrawable(R.drawable.candy_and_gum);
        }else if(foodName.toLowerCase().contains("chocolate"))
        {
            return  context.getResources().getDrawable(R.drawable.chocolate);
        }else if(foodName.toLowerCase().contains("nuts"))
        {
            return  context.getResources().getDrawable(R.drawable.nuts);
        }else if(foodName.toLowerCase().contains("peanut"))
        {
            return  context.getResources().getDrawable(R.drawable.peanut_butter);
        }else if(foodName.toLowerCase().contains("salty"))
        {
            return  context.getResources().getDrawable(R.drawable.salty_food);
        }else if(foodName.toLowerCase().contains("dairy"))
        {
            return  context.getResources().getDrawable(R.drawable.dairy_products);
        }else if(foodName.toLowerCase().contains("eggs"))
        {
            return  context.getResources().getDrawable(R.drawable.eggs);
        }else if(foodName.toLowerCase().contains("corn"))
        {
            return  context.getResources().getDrawable(R.drawable.corn);
        }else if(foodName.toLowerCase().contains("oats"))
        {
            return  context.getResources().getDrawable(R.drawable.oats);
        }else if(foodName.toLowerCase().contains("kernels"))
        {
            return  context.getResources().getDrawable(R.drawable.popcorn);
        }else if(foodName.toLowerCase().contains("potato"))
        {
            return  context.getResources().getDrawable(R.drawable.potato);
        }else if(foodName.toLowerCase().contains("rice"))
        {
            return  context.getResources().getDrawable(R.drawable.rice);
        }else if(foodName.toLowerCase().contains("yeast"))
        {
            return  context.getResources().getDrawable(R.drawable.yeast);
        }else if(foodName.toLowerCase().contains("pear"))
        {
            return  context.getResources().getDrawable(R.drawable.pear);
        }else
        {
            return  context.getResources().getDrawable(R.drawable.broccoli);
        }
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodInfo() {
        return foodInfo;
    }

    public String getEffectsIfDangerous() {
        return effectsIfDangerous;
    }

    public String getEffectsIfHealthy() {
        return effectsIfHealthy;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public int getIfDangerous() {
        return ifDangerous;
    }

    public int getIfHealthy() {
        return ifHealthy;
    }
}