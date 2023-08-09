package com.example.woofwisdomapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.woofwisdomapplication.R;
import com.example.woofwisdomapplication.data.model.CommentsModel;
import com.example.woofwisdomapplication.data.model.FoodCategoryModel;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.Viewholder> {
    List<CommentsModel> comments_list;
    Context context;

    public CommentsAdapter(List<CommentsModel> comments_list, Context context) {
        this.comments_list = comments_list;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_design,parent,false);
        return new CommentsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.Viewholder holder, int position) {
        CommentsModel model=comments_list.get(position);
        holder.user_comment.setText(model.getCommentTitle());
        holder.user_name.setText(model.getUserName());
        holder.date.setText(model.getDateandTime());
    }

    @Override
    public int getItemCount() {
        return comments_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView user_image;
        private TextView user_name;
        private TextView user_comment;
        private TextView date;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            user_image=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_comment=itemView.findViewById(R.id.user_comment);
            date=itemView.findViewById(R.id.date);
        }

    }
}
