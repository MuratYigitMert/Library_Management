package com.example.library_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.ToString;

import java.util.List;

@Entity
@ToString(exclude = {"loans", "lossRecords", "role"}) // add other lazy fields too!
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Column(name = "loss_count", nullable = false)
    private int lossCount = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Loan> loans;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LossRecord> lossRecords;
}
