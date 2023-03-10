package com.mk.manageuserpro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name")
    @Length(min = 5, message = "*User name must have at least 5 characters")
    @NotEmpty(message = "*Please provide a user name")
    private String username;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "group_id")
    @Valid
    private Group group;
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @Column(name = "password")
    @Length(min = 5, message = "*Password must have at least 5 characters")
    @NotEmpty(message = "*Please provide password")
    private String password;
    @Column(name = "name")
    @NotEmpty(message = "*Please provide name")
    private String name;
    @Column(name = "birthday")
    @NotNull(message = "*Please provide birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @Column(name = "active")
    private Boolean active;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Valid
    private Set<Role> roles;
    @Valid
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserDetailJapan userDetailJapan;

}
