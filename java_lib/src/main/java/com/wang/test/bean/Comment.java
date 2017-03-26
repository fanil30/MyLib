package com.wang.test.bean;

import com.wang.db.basis.Constraint;
import com.wang.db.basis.ConstraintAnno;
import com.wang.db.basis.FieldType;
import com.wang.db.basis.TypeAnno;

/**
 * 评论类（一条签到对应多条评论）
 */
public class Comment {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int commentId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "User", foreignField = "userId")
    private int userId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "Sign", foreignField = "signId")
    private int signId;

    @TypeAnno(type = FieldType.VARCHAR_10)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String commentDate;

    @TypeAnno(type = FieldType.VARCHAR_10)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String commentTime;

    @TypeAnno(type = FieldType.VARCHAR_500)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String commentText;

    public Comment() {
    }

    public Comment(int userId, int signId, String commentDate, String commentTime, String commentText) {
        this.userId = userId;
        this.signId = signId;
        this.commentDate = commentDate;
        this.commentTime = commentTime;
        this.commentText = commentText;
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

    public int getSignId() {
        return signId;
    }

    public void setSignId(int signId) {
        this.signId = signId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
