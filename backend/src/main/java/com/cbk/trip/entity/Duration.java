package com.cbk.trip.entity;

import javax.persistence.*;
import com.cbk.trip.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "duration")
public class Duration extends BaseEntity {
   
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;
}