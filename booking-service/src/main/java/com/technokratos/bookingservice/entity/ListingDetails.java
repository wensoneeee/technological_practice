package com.technokratos.bookingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ListingDetails {
    @Id
    private Long listingDetailsId;

    @MapsId
    @OneToOne
    @JoinColumn(name="listing_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Listing listing;

    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private PropertyType propertyType;

    @Column(nullable = false)
    private Integer roomCount;
    @Column(nullable = false)
    private Integer bedCount;
    @Column(nullable = false)
    private Integer bathCount;
    @Column(nullable = false)
    private Integer peopleCount;
    private Integer areaSqm;
}
