package com.concamap.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
@Table(name = "comment", schema = "blog")
public class CommentEntity {
    private long id;
    private String commentAuthorEmail;
    private String commentAuthorName;
    private String content;
    private Long postId;
    private Timestamp createTime;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "commentAuthorEmail")
    public String getCommentAuthorEmail() {
        return commentAuthorEmail;
    }

    public void setCommentAuthorEmail(String commentAuthorEmail) {
        this.commentAuthorEmail = commentAuthorEmail;
    }

    @Basic
    @Column(name = "commentAuthorName")
    public String getCommentAuthorName() {
        return commentAuthorName;
    }

    public void setCommentAuthorName(String commentAuthorName) {
        this.commentAuthorName = commentAuthorName;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "post_id")
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "createTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return id == that.id &&
                Objects.equals(commentAuthorEmail, that.commentAuthorEmail) &&
                Objects.equals(commentAuthorName, that.commentAuthorName) &&
                Objects.equals(content, that.content) &&
                Objects.equals(postId, that.postId) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, commentAuthorEmail, commentAuthorName, content, postId, createTime);
    }
}
