package dev.alexa.store.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "msg")
public class Message {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;
    private String message;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User destination;
    @ManyToOne
    private Item item;

}
