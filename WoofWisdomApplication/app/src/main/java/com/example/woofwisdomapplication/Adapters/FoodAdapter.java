package com.example.woofwisdomapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woofwisdomapplication.CommentActivity;
import com.example.woofwisdomapplication.R;
import com.example.woofwisdomapplication.data.model.FoodCategoryModel;
import com.example.woofwisdomapplication.views.FoodDetailsActivity;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    List<FoodCategoryModel> food_list;
    Context context;

    public FoodAdapter(List<FoodCategoryModel> food_list, Context context) {
        this.food_list = food_list;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_fragment_design,parent,false);
        return new FoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {
        FoodCategoryModel foods=food_list.get(position);
        holder.setdata(foods);
    }

    @Override
    public int getItemCount() {
        return food_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         private CardView food_menu_list;
         private TextView textView;
         private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            food_menu_list=itemView.findViewById(R.id.food_menu_list);
            textView=itemView.findViewById(R.id.foods_name);
            image=itemView.findViewById(R.id.image);


        }
        public void setdata(FoodCategoryModel foods) {
            textView.setText(foods.getFoodCategory().toUpperCase());
            image.setImageDrawable(foods.getImage());
            /* Food Details Activity :- */
            food_menu_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it=new Intent(context,FoodDetailsActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra("category",foods.getFoodCategory());
                    context.startActivity(it);
                }
            });
        }
    }
}
