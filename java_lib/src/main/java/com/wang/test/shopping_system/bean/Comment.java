package com.wang.test.shopping_system.bean;

/**
 * by wangrongjun on 2016/12/15.
 * 评论
 */
public class Comment {

    private int commentId;
    private int userId;
    private int goodId;
    private String content;

    public Comment(int userId, int goodId, String content) {
        this.userId = userId;
        this.goodId = goodId;
        this.content = content;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
