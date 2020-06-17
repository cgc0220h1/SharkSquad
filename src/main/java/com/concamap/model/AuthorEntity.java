package com.concamap.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "author", schema = "blog")
public class AuthorEntity {
    private long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String imageLink;
    private String bio;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "dateOfBirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Basic
    @Column(name = "imageLink")
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Basic
    @Column(name = "bio")
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorEntity that = (AuthorEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(imageLink, that.imageLink) &&
                Objects.equals(bio, that.bio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phoneNumber, dateOfBirth, imageLink, bio);
    }
}
