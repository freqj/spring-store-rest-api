package dev.alexa.store.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;




}
