package com.sameeh.springit.Domain;

import com.sameeh.springit.Domain.Validator.PasswordsMatch;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table
@RequiredArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
@PasswordsMatch
public class User implements UserDetails { // Authentication side
    @Id
    @GeneratedValue
    private Long id;

    private Date registrationDate;

    @NonNull
    @Size(min = 8, max = 30)
    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    @Column(length = 100)
    private String password;

    @NonNull
    @Column(nullable = false)
    private Boolean enabled;

    @NonNull
    @NotEmpty(message = "listen to me !! Enter a First name")
    private String firstName;

    @NonNull
    @NotEmpty(message = "listen to me !! Enter a last name")
    private String lastName;

    @Transient
    @Setter(AccessLevel.NONE)
    private String fullName;

    @Transient
    @NotEmpty(message = "Please enter Password Confirmation.")
    private String confirmPassword;

    @NonNull
    @NotEmpty(message = "Please enter alias.")
    @Column(nullable = false, unique = true)
    private String alias;

    private String activationCode;
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Link> links = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addRole(Role userRole) {
        roles.add(userRole);
    }

    public void addRoles(Set<Role> hashSet) {
        roles.addAll(hashSet);
    }

    public void addLink(Link link) {links.add(link);}

    public void addComment(Comment comment) {comments.add(comment);}

}
