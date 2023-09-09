package com.sameeh.springit.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table
@RequiredArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Role { // Authorization side

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @ManyToMany( mappedBy = "roles")
    private Collection<User> users;

}