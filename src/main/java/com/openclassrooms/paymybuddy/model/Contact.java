package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id", nullable = false)
    private int contact_id;

    @OneToMany(mappedBy = "transaction_id", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Transaction> transactions = new ArrayList<>();

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="user_id")
    private User user;

    /*@Column(name = "buddy_id", nullable = false)
    private int buddy_id;*/

}
