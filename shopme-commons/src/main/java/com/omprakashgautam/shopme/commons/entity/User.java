package com.omprakashgautam.shopme.commons.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author omprakash gautam
 * Created on 11-Jul-21 at 5:55 PM.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String firstName;

    @Column(length = 54, nullable = false)
    private String lastName;

    @Column(length = 64)
    private String photos;

    private boolean enabled = true;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addRole(Role role){
        this.getRoles().add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photos='" + photos + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }

    @Transient
    public String getPhotosImagePath() {
        if (id == null || photos == null) {
            return "/user-photos/0/default-user.png";
        }
        return "/user-photos/" + this.id + "/" + this.photos;
    }

    @Transient
    public String getFullName(){
        return firstName + " " + lastName;
    }
}
