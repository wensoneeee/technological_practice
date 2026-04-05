package com.technokratos.bookingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="listing_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Listing listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="guest_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User guest;

    @Column(nullable=false)
    private LocalDate checkIn;

    @Column(nullable=false)
    private LocalDate checkOut;

    @Column(nullable=false)
    private BookingStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
