package com.vvs.puzzles.service.impl;

import com.vvs.puzzles.exeption.InputException;
import com.vvs.puzzles.service.FileToImageService;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileToImageServiceImpl implements FileToImageService {
    @Override
    public BufferedImage fileToImage(MultipartFile file) {
        if (!Objects.equals(file.getContentType(), "image/jpeg")) {
            throw new InputException(file.getOriginalFilename()
                    + " is not a jpeg file!");
        }
        BufferedImage inputImage;
        try (InputStream is = file.getInputStream()) {
            inputImage = ImageIO.read(is);
        } catch (Exception e) {
            throw new InputException("Can't read input file: "
                    + file.getOriginalFilename(), e);
        }
        if (inputImage == null) {
            throw new InputException("Can't read image from input file: "
                    + file.getOriginalFilename());
        }
        return inputImage;
    }
}
