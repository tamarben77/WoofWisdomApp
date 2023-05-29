package com.example.woofwisdomapplication.data.model;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CommentsModel {

    private String  commentTitle;
    private String commentDesc;
    private String userName;
    private int userID;
    private int questionID;

    Context context;

    public CommentsModel(Context context,String commentTitle, String commentDesc, String userName, int userID, int questionID, String dateandTime, int commentId) {
        this.commentTitle = commentTitle;
        this.commentDesc = commentDesc;
        this.userName = userName;
        this.userID = userID;
        this.questionID = questionID;
        this.dateandTime = dateandTime;
        this.context = context;
        this.commentId = commentId;
    }

    public String getDateandTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format1.format(format.parse(dateandTime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDateandTime(String dateandTime) {
        this.dateandTime = dateandTime;
    }

    private String dateandTime;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    private int commentId;

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getCommentDesc() {
        return commentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        this.commentDesc = commentDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }
}