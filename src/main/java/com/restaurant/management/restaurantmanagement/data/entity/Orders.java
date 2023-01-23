package com.restaurant.management.restaurantmanagement.data.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_order")
public final class Orders
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Foods> foods;

    @ManyToOne
    @JoinColumn(name = "delivery_user_id", referencedColumnName = "id")
    private Users delivery;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Orders()
    {
    }

    public long getId()
    {
        return id;
    }

    public Users getUser()
    {
        return user;
    }

    public List<Foods> getFoods()
    {
        return foods;
    }

    public Users getDelivery()
    {
        return delivery;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setUser(Users user)
    {
        this.user = user;
    }

    public void setFoods(List<Foods> foods)
    {
        this.foods = foods;
    }

    public void setDelivery(Users delivery)
    {
        this.delivery = delivery;
    }
}
