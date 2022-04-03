package com.springboot.start_app_backend.models;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 20)
    private String username;
    @Version
    private Long version;
    private long creation_date;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(max = 150)
    private String password;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isEnabled = false;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Size(max = 200)
    private String biography;
    @Size(max = 120)
    private String slogan;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Transient
    private Set<Experience> experiences = new HashSet<Experience>();
    @ElementCollection
    private Set<String> skills = new HashSet<String>();
    private String profileUrl;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Community> communities = new HashSet<>();
    @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE})
    @JsonIgnore
    private Set<Community> subscribedCommunities = new HashSet<Community>();
    @OneToMany(mappedBy = "to", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Followers> followers = new HashSet<Followers>();
    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Followers> following = new HashSet<Followers>();

    public Set<Community> getSubscribedCommunities() {
        return subscribedCommunities;
    }

    public Set<Followers> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Followers> followers) {
        this.followers = followers;
    }

    public Set<Followers> getFollowing() {
        return following;
    }

    public void setFollowing(Set<Followers> following) {
        this.following = following;
    }

    public void setSubscribedCommunities(Set<Community> subscirbedCommunities) {
        this.subscribedCommunities = subscirbedCommunities;
    }


    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public User() {

    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.creation_date = System.currentTimeMillis();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Set<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(Set<Experience> experiences) {
        this.experiences = experiences;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(Set<Community> communities) {
        this.communities = communities;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
