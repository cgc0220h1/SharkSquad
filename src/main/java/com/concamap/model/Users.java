package com.concamap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.sql.Timestamp;

@Entity
@Table(name = "users", schema = "shark_squad")
@Getter
@Setter
@NoArgsConstructor
public class Users {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "first_name", nullable = false, length = 100)
    @NotNull(message = "First Name cannot be empty")
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ\" +\n" +
            "    \"ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ\" +\n" +
            "    \"ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\\\s]+$")
    private String firstName;
  
    @Basic
    @Column(name = "last_name", nullable = false, length = 100)
    @NotNull(message = "Last Name cannot be empty")
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ\\\" +\\n\" +\n" +
            "            \"    \\\"ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ\\\" +\\n\" +\n" +
            "            \"    \\\"ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\\\\\\\s]+$")

    private String lastName;

    @NotNull(message = "Phone cannot be emty")
    @Size(min = 10, max = 11)
    @Pattern(regexp = "^0\\d*")
    @Pattern(regexp = "(^$|[0-9]*$)")
    @Basic
    @Column(name = "phone", nullable = false, length = 50)
    @JsonIgnore
    private String phone;

    @NotNull(message = "Email cannot be empty")
    @Size(min = 8, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Basic
    @Column(name = "email", nullable = false, length = 225)
    @JsonIgnore
    private String email;

    @NotNull(message = "username cannot be empty")
    @Size(min = 5, max = 30)
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Basic
    @Column(name = "username", nullable = false, length = 100)
    @JsonIgnore
    private String username;

    @NotNull(message = "password cannot be empty")
    @Size(min = 8 , max = 30)
    @Basic
    @Column(name = "password", nullable = false, length = 100)
    @JsonIgnore
    private String password;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_time", nullable = false)
    @JsonIgnore
    private Timestamp updatedTime;

    @Basic
    @Column(name = "status", nullable = false)
    @JsonIgnore
    private int status;

    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    @JsonIgnore
    @ToString.Exclude
    @HashCodeExclude
    private Roles roles;
}
