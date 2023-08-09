package com.example.woofwisdomapplication.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.woofwisdomapplication.R;
import com.example.woofwisdomapplication.data.model.FoodCategoryModel;
import com.example.woofwisdomapplication.data.model.FoodModel;
import com.example.woofwisdomapplication.views.FoodDetailsActivity;

import java.util.List;

public class FoodDetailsAdapter extends RecyclerView.Adapter<FoodDetailsAdapter.ViewHolder> {
    List<FoodModel> food_details_list;
    Context context;

    public FoodDetailsAdapter(List<FoodModel> food_details_list, Context context) {
        this.food_details_list = food_details_list;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_details_design,parent,false);
        return new FoodDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDetailsAdapter.ViewHolder holder, int position) {
        FoodModel foods=food_details_list.get(position);
        holder.setdata(foods);
    }

    @Override
    public int getItemCount() {
        return food_details_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView title;
        private ImageView image;
        private Dialog dialog;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.food_details_list);
            title=itemView.findViewById(R.id.title);
            image=itemView.findViewById(R.id.image);

        }

        public void setdata(FoodModel foods) {

            title.setText(foods.getFoodName().toUpperCase());
            image.setImageDrawable(foods.getImage());

            /* Dialog Open Button :- */
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog();
                }

                private void openDialog() {
                    dialog=new Dialog(context);
                    dialog.setContentView(R.layout.food_details_dialog);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);

                    ImageView cancel_dialog;
                    TextView healthy_dang,healthy_dang_details,details,title;

                    cancel_dialog=dialog.findViewById(R.id.cancel_dialog);
                    details=dialog.findViewById(R.id.details);
                    title=dialog.findViewById(R.id.title);
                    healthy_dang=dialog.findViewById(R.id.healthy_dang);
                    healthy_dang_details=dialog.findViewById(R.id.healthy_details);

                    title.setText(foods.getFoodName().toUpperCase());
                    details.setText(foods.getFoodInfo());

                   if(foods.getIfDangerous()==1){
                       healthy_dang.setText("Dangerous");
                       healthy_dang.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                       healthy_dang_details.setVisibility(View.VISIBLE);
                       healthy_dang_details.setText(foods.getEffectsIfHealthy());

                   }else{
                       healthy_dang.setText("Healthy");
                       healthy_dang.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                       healthy_dang_details.setVisibility(View.VISIBLE);
                       healthy_dang_details.setText(foods.getEffectsIfDangerous());
                   }




                    dialog.show();

                    /* Dialog Dismiss :- */
                    cancel_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}
