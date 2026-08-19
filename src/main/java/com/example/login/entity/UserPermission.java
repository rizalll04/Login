package com.example.login.entity;

import javax.persistence.*;

@Entity
@Table(
    name = "USER_PERMISSION",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_USER_PERMISSION",
            columnNames = {"USER_ID", "FEATURE"}
        )
    }
)
public class UserPermission {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_permission_seq"
    )
    @SequenceGenerator(
        name = "user_permission_seq",
        sequenceName = "USER_PERMISSION_SEQ",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "FEATURE", nullable = false, length = 50)
    private String feature;

    @Column(name = "ENABLED", nullable = false)
    private Integer enabled = 0;

    public UserPermission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public boolean isAllowed() {
        return enabled != null && enabled == 1;
    }
}