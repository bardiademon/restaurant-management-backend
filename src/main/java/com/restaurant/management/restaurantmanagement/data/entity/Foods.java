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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 1000)
    private String name;

    @Column(nullable = false)
    private int price = 0;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Categories category;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "order_image", length = 1000)
    private String orderImage;

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

    public Categories getCategory()
    {
        return category;
    }

    public void setCategory(Categories category)
    {
        this.category = category;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public String getOrderImage()
    {
        return orderImage;
    }

    public void setOrderImage(String orderImage)
    {
        this.orderImage = orderImage;
    }
}
