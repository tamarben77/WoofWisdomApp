package com.example.woofwisdomapplication.views;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.woofwisdomapplication.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class FoodMeasureActivity extends AppCompatActivity {
    MaterialSpinner materialSpinner;
    Button button;
    String selected_food="Select Food";
    EditText edittext,edittext1;
    TextView textView;
    String dangerous_message="Your food is Dangerous";
    String normal_message="Your food is Normal";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_measure);
        button=(Button) findViewById(R.id.calculate_foods_result);
        edittext=(EditText) findViewById(R.id.dog_weight);
        edittext1=(EditText) findViewById(R.id.food_weight);
        textView=(TextView) findViewById(R.id.final_result);

        /* Food Spinner :- */
        materialSpinner = (MaterialSpinner) findViewById(R.id.food_spinner);
        materialSpinner.setItems("Select Food", "Milk Chocolate", "Dark Chocolate", "Onion", "Grapes and Raisins", "Macadamia Nuts");
        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                selected_food=item;
              /* SnackBar.make(view, "Clicked " + item, SnackBar.LENGTH_INDEFINITE).show(); */
            }
        });

       /* Calculate Result Button :- */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dog_weight=edittext.getText().toString();
                String food_weight=edittext1.getText().toString();

                if (selected_food.equals("Select Food")) {
                    materialSpinner.setError("Please Select Value");
                }else if (dog_weight.equals("0") || dog_weight.equals("")) {
                    edittext.setError("Please Enter Value");
                }else if (food_weight.equals("0") || food_weight.equals("")) {
                    edittext1.setError("Please Enter Value");
                }
                else {
                    float result1=0;
                    float get_food_weight = Integer.parseInt(food_weight);

                    if (selected_food.equals("Milk Chocolate")) {
                        int value = 30;
                        float total_weight = Integer.parseInt(dog_weight);
                        result1=total_weight * value;

                    } else if (selected_food.equals("Dark Chocolate")) {
                        int value = 3;
                        float total_weight = Integer.parseInt(dog_weight);
                        result1=total_weight * value;
                    } else if (selected_food.equals("Onion")) {
                        float value = 12.5f;
                        float total_weight = Integer.parseInt(dog_weight);
                        result1=total_weight * value;
                    } else if (selected_food.equals("Grapes and Raisins")) {
                        int value = 10;
                        float total_weight = Integer.parseInt(dog_weight);
                        result1=total_weight * value;
                    } else if (selected_food.equals("Macadamia Nuts")) {
                        int value = 2;
                        float total_weight = Integer.parseInt(dog_weight);
                        result1=total_weight * value;
                    }

                    if (result1 <= get_food_weight){
                        textView.setText(dangerous_message);
                        textView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }else {
                        textView.setText(normal_message);
                        textView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    }
                }
            }
        });

    }
}