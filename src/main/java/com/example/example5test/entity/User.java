package com.example.example5test.entity;

import com.example.example5test.validation.UserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@UserInfo
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(min = 3, max = 30)
    private String name;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private String images;

    private Role role = Role.USER;

    @Transient
    public String getPhotosImagePath(){
        if (images == null || id == null) return null;

        return "/user-photos/" + id + "/" + images;

    }


}
