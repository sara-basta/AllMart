package com.sara.allmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "saved_addresses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavedAddress {
    @Id
    @SequenceGenerator(
            name = "saved_address_id_seq",
            sequenceName = "saved_address_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "saved_address_id_seq"
    )
    private Long id;

    private String street;
    private String city;
    private String zipCode;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
