package com.example.woofwisdomapplication.data.model;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ForumModel {

    private int ifNewQuery;
    private String questionTitle;
    private String datetime;
    private int questionId;
    private String questionDetails;
    private int userID;
    private String userType;
    private int upvotes;
    private int views;
    Context context;
    private String category;

    public ForumModel(Context context,int ifNewQuery, String questionTitle, String questionDetails, int userID, String userType, int upvotes, int views, String category,int questionId,String datetime) {
        this.ifNewQuery = ifNewQuery;
        this.questionTitle = questionTitle;
        this.questionDetails = questionDetails;
        this.userID = userID;
        this.userType = userType;
        this.upvotes = upvotes;
        this.views = views;
        this.context = context;
        this.category = category;
        this.datetime = datetime;
        this.questionId = questionId;
    }

    public int getIfNewQuery() {
        return ifNewQuery;
    }

    public void setIfNewQuery(int ifNewQuery) {
        this.ifNewQuery = ifNewQuery;
    }


    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDetails() {
        return questionDetails;
    }

    public void setQuestionDetails(String questionDetails) {
        this.questionDetails = questionDetails;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDatetime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format1.format(format.parse(datetime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getQuestionId() {
        return questionId;
    }
}