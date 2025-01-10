package com.rvega.dreamshops.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing an image.
 * This class is used to transfer image data between different layers of the application.
 */
@Data
public class ImageDto {
    /**
     * The unique identifier of the image.
     */
    private Long id;

    /**
     * The name of the image file.
     */
    private String fileName;

    /**
     * The URL from which the image can be downloaded.
     */
    private String downloadUrl;
}
