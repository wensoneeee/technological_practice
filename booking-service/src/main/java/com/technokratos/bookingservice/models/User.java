package com.technokratos.bookingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private Role role;

    @CreationTimestamp
    private LocalDate creationDate;

    private Role role;

    private String confirmed;
    private String confirmCode;

    @ManyToOne
    private Image image;

    @OneToMany(mappedBy = "userCartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "userFeedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "userPurchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId != null && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("User(")
                .append("userId=").append(userId)
                .append(", name=").append(name)
                .append(", createdDate=").append(creationDate)
                .append(')').toString();
    }
}