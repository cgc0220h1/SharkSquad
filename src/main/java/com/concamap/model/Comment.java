package com.concamap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comments", schema = "shark_squad")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Basic
    @Column(name = "status", nullable = false)
    @JsonIgnore
    private int status;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_date", nullable = false)
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "posts_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    @JsonIgnore
    private Users users;
}
