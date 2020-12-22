package com.miguel.retrofit2;

import com.google.gson.annotations.SerializedName;

class Comment {

    int postId;

    @SerializedName("id")
    int commentId;

    @SerializedName("name")
    String title;

    String email;

    @SerializedName("body")
    String text;

    public int getPostId() {
        return postId;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getText() {
        return text;
    }
}
