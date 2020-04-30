package com.android.ireport.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class User {
    private String email;
    private String username;

    public User(String email, String username) {
        this.username = username;
        this.email = email;
    }
}
