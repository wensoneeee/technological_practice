package com.technokratos.bookingservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long imageId;

    private String originalFileName;
    private String storageFileName;
    private String type;
    private Long size;

    @OneToMany(mappedBy = "image")
    private List<Event> eventsImage;

    @OneToMany(mappedBy = "image")
    private List<User> usersImage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return imageId != null && imageId.equals(image.imageId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder("Image(")
                .append("imageId=").append(imageId)
                .append(", originalFileName=").append(originalFileName)
                .append(", storageFileName=").append(storageFileName)
                .append(", type=").append(type)
                .append(", size=").append(size)
                .append(')').toString();
    }
}
