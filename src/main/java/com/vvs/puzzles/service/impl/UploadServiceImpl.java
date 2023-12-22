package com.vvs.puzzles.service.impl;

import com.vvs.puzzles.repository.PuzzleRepository;
import com.vvs.puzzles.service.FileToImageService;
import com.vvs.puzzles.service.UploadService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {
    private final PuzzleRepository puzzleRepository;
    private final FileToImageService fileToImageService;

    @Override
    public String uploadAndCutImage(String imageName,
                                    Integer sideLength,
                                    MultipartFile file) {
        BufferedImage inputImg = fileToImageService.fileToImage(file);

        // Array for sub images
        int rows = sideLength;
        int columns = sideLength;
        BufferedImage[] tileImages = new BufferedImage[rows * columns];

        // Dividing original image to sub images
        int subImageWidth = inputImg.getWidth() / columns;
        int subImageHeight = inputImg.getHeight() / rows;

        int currentImg = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                tileImages[currentImg] = new BufferedImage(subImageWidth,
                        subImageHeight, inputImg.getType());
                Graphics2D imgCreator = tileImages[currentImg].createGraphics();

                // Calculating coordinates for drawing
                int srcFirstX = subImageWidth * j;
                int srcFirstY = subImageHeight * i;
                int dstCornerX = subImageWidth * j + subImageWidth;
                int dstCornerY = subImageHeight * i + subImageHeight;

                imgCreator.drawImage(inputImg, 0, 0, subImageWidth, subImageHeight,
                        srcFirstX, srcFirstY, dstCornerX, dstCornerY, null);
                currentImg++;
            }
        }
        return puzzleRepository.savePuzzle(imageName, inputImg, tileImages);
    }
}
