package com.rvega.dreamshops.service.image;

import com.rvega.dreamshops.dto.ImageDto;
import com.rvega.dreamshops.exceptions.ResourceNotFoundException;
import com.rvega.dreamshops.model.Image;
import com.rvega.dreamshops.model.Product;
import com.rvega.dreamshops.repository.ImageRepository;
import com.rvega.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides services related to image management in the e-commerce application.
 * It implements the {@link IImageService} interface and utilizes Spring's {@link Service} annotation.
 *
 * @author rvega
 */
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    /**
     * The {@link ImageRepository} instance used to interact with the database.
     */
    private final ImageRepository imageRepository;

    /**
     * The {@link IProductService} instance used to retrieve product information.
     */
    private final IProductService productService;

    /**
     * Retrieves an image from the database by its ID.
     *
     * @param id The ID of the image to retrieve.
     * @return The {@link Image} object with the specified ID.
     * @throws ResourceNotFoundException If no image is found with the given ID.
     */
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    /**
     * Deletes an image from the database by its ID.
     *
     * @param id The ID of the image to delete.
     * @throws ResourceNotFoundException If no image is found with the given ID.
     */
    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("No image found with id: " + id);
        });
    }

    /**
     * Saves multiple images associated with a product in the database.
     *
     * @param productId The ID of the product to associate the images with.
     * @param files The list of {@link MultipartFile} objects representing the images to save.
     * @return A list of {@link ImageDto} objects representing the saved images.
     * @throws RuntimeException If an error occurs while saving the images.
     */
    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    /**
     * Updates an existing image in the database.
     *
     * @param file The {@link MultipartFile} object representing the updated image.
     * @param imageId The ID of the image to update.
     * @throws RuntimeException If an error occurs while updating the image.
     */
    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
