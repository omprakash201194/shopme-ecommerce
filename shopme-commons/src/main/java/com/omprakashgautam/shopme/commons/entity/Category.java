package com.omprakashgautam.shopme.commons.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 5:47 PM.
 */
//Do not use lombok with entity mapped to self -- lombok's hashcode functions keeps calling itself and gives error
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @Fetch(value= FetchMode.SELECT)
    private Set<Category> children = new HashSet<>();

    public Category(Long id) {
        this.id = id;
    }

    public Category(String name, String alias, String image) {
        this.name = name;
        this.alias = alias;
        this.image = image;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
        this.alias = name;
        this.image = name;
    }

    public Category(String name, String alias, String image, Category parent) {
        this(name,alias,image);
        this.parent = parent;
    }

    public Category() {
    }

    public static Category copyFull(Category category){
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setImage(category.getImage());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setName(category.getName());
        return copyCategory;
    }

    public static Category copyFull(Category category, String name){
        Category copyCategory = copyFull(category);
        copyCategory.setName(name);
        return copyCategory;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", image='" + image + '\'' +
                ", enabled=" + enabled +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    @Transient
    public String getPhotosImagePath() {
        if (id == null || image == null || image.equalsIgnoreCase("default.png")) {
            return "/category-images/0/default.png";
        }
        return "/category-images/" + this.id + "/" + this.image;
    }
}
