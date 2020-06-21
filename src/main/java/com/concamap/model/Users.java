package com.concamap.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "shark_squad")
@Data
public class Users {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ\" +\n" +
            "    \"ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ\" +\n" +
            "    \"ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\\\s]+$")
    @Basic
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;


    @NotNull(message = "Last Name cannot be empty")
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ\\\" +\\n\" +\n" +
            "            \"    \\\"ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ\\\" +\\n\" +\n" +
            "            \"    \\\"ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\\\\\\\s]+$")
    @Basic
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;


    @NotNull(message = "Phone cannot be emty")
    @Size(min = 10, max = 11)
    @Pattern(regexp = "^0\\d*")
    @Pattern(regexp = "(^$|[0-9]*$)")
    @Basic
    @Column(name = "phone", nullable = false, length = 50)
    private String phone;


    @NotNull(message = "Email cannot be empty")
    @Size(min = 8, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Basic
    @Column(name = "email", nullable = false, length = 225)
    private String email;


    @NotNull(message = "username cannot be empty")
    @Size(min = 5, max = 30)
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Basic
    @Column(name = "username", nullable = false, length = 100)
    private String username;


    @NotNull(message = "password cannot be empty")
    @Size(min = 8 , max = 30)
    @Basic
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_time", nullable = false)
    private Timestamp updatedTime;

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @OneToMany(mappedBy = "users")
    private Collection<Comment> commentsById;

    @OneToMany(mappedBy = "users")
    private Collection<Post> postsById;

    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id", nullable = false)
    private Roles rolesByRolesId;
}
