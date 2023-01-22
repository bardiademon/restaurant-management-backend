package com.restaurant.management.restaurantmanagement.data.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "foods")
public final class Foods
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private long id;

    @Column(nullable = false, length = 1000)
    private String name;

    @Column(nullable = false)
    private int price = 0;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Categories> categories;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Foods()
    {
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public List<Categories> getCategories()
    {
        return categories;
    }

    public void setCategories(List<Categories> categories)
    {
        this.categories = categories;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
}
