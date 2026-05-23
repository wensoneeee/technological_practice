package com.technokratos.bookingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDateTime timestamp;
    private String location;
    private BigDecimal price;

    @Column(name="available_tickets")
    @ColumnDefault("0")
    @Check(constraints = "available_tickets>=0")
    private Integer availableTickets;

    @ManyToMany
    @JoinTable(name="event_category",
        joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "eventId"),
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "categoryId"))
    private List<Category> categories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "eventCartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;


    @OneToMany(mappedBy = "eventFeedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId != null && eventId.equals(event.eventId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return new StringBuilder("Event(")
                .append("eventId=").append(eventId)
                .append(", title=").append(title)
                .append(", description= ").append(description)
                .append(", timestamp= ").append(timestamp)
                .append(", location= ").append(location)
                .append(", price= ").append(price)
                .append(", availableTickets= ").append(availableTickets)
                .append(')').toString();
    }
}
