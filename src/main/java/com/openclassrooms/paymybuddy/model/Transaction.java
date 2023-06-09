package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private int transaction_id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @OneToOne(mappedBy = "transaction")
    private Fee fee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="contact_id", referencedColumnName = "contact_id")
    private Contact contact;
}
