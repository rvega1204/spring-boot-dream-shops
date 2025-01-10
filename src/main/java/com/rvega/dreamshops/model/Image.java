package com.rvega.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

/**
 * Represents an image entity in the Dream Shops application.
 * This class is used to store information about images, such as their file name, type, binary data, download URL, and the product they belong to.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    /**
     * Unique identifier for the image.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the image file.
     */
    private String fileName;

    /**
     * The type of the image file (e.g., "image/jpeg", "image/png").
     */
    private String fileType;

    /**
     * The binary data of the image.
     * This field is annotated with {@link Lob} to indicate that it can store large objects.
     */
    @Lob
    private Blob image;

    /**
     * The download URL of the image.
     * This URL can be used to retrieve the image from a remote server.
     */
    private String downloadUrl;

    /**
     * The product that the image belongs to.
     * This field is annotated with {@link ManyToOne} and {@link JoinColumn} to establish a relationship between the Image and Product entities.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
