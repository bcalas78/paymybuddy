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
@Table(name = "fee")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id", nullable = false)
    private int fee_id;

    @Column(name = "fee_amount", nullable = false)
    private Float fee_amount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tansaction_id", referencedColumnName = "transaction_id")
    private Transaction transaction;
}
