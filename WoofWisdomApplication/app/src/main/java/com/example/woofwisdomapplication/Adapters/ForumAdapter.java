package com.example.woofwisdomapplication.Adapters;

import static android.content.Context.MODE_PRIVATE;

import static com.example.woofwisdomapplication.MainActivity.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.woofwisdomapplication.CommentActivity;
import com.example.woofwisdomapplication.FormActivity;
import com.example.woofwisdomapplication.R;
import com.example.woofwisdomapplication.data.model.FoodCategoryModel;
import com.example.woofwisdomapplication.data.model.ForumModel;
import com.example.woofwisdomapplication.forumsAddQuestion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.Viewholder> {
    List<ForumModel> forum_list;
    List<ForumModel> itemsCopy;
    Context context;
    public ForumAdapter(List<ForumModel> forum_list,List<ForumModel> itemsCopy, Context context) {
        this.forum_list = forum_list;
        this.itemsCopy = itemsCopy;
        this.context = context;
    }

    @NonNull
    @Override
    public ForumAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_list_design,parent,false);
        return new ForumAdapter.Viewholder(view);
    }

    public void filter(String text) {
        forum_list.clear();
        if(text.isEmpty()){
            forum_list.addAll(itemsCopy);
        } else{

            text = text.toLowerCase();
            Log.d("mubi-0-0",itemsCopy.size()+" "+forum_list.size());
            for(ForumModel item: itemsCopy){
                if(item.getQuestionTitle().toLowerCase().contains(text)){
                    forum_list.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ForumAdapter.Viewholder holder, int position) {
        ForumModel model=forum_list.get(position);
        holder.title.setText(model.getQuestionTitle());
        holder.description.setText(model.getQuestionDetails());
        holder.date.setText(model.getDatetime());

        TextView commentCountTextView = holder.itemView.findViewById(R.id.comment_count);

        // Fetch comment count using network request
        int questionId_st = model.getQuestionId();  // Assuming you have a getter method for questionId
        String URL_COUNT = BASE_URL + "dogForums/getCommentCountsForAllQuestions";
        StringRequest countRequest = new StringRequest(Request.Method.GET, URL_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int commentCount = jsonObject.getInt(String.valueOf(questionId_st));
                            commentCountTextView.setText(commentCount + " Comments");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error fetching comment count: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(context).add(countRequest);

        /* Comment Activity :- */
        holder.comment_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                if(sharedPreferences.getString("sessionID","").equals(""))
                {
                    Toast.makeText(context,"You need to login first",Toast.LENGTH_SHORT).show();
                }else{
                    Intent it=new Intent(context, CommentActivity.class);
                    it.putExtra("title",model.getQuestionTitle());
                    it.putExtra("date",model.getDatetime());
                    it.putExtra("questionId",model.getQuestionId());
                    context.startActivity(it);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return forum_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView description;
        private LinearLayout comment_pass;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            date=itemView.findViewById(R.id.date);
            description=itemView.findViewById(R.id.description);
            comment_pass=itemView.findViewById(R.id.comment_pass);

        }
    }
}
