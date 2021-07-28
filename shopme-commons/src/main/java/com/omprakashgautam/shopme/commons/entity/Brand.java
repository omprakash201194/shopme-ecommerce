package com.omprakashgautam.shopme.commons.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author gautam
 * Created on 28-Jul-21 at 4:13 PM.
 */
@Entity
@Table(name = "brands")
@Data
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 45, nullable = false)
    private String logo;

    @ManyToMany
    @JoinTable(name = "brands_categories", joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();
}
