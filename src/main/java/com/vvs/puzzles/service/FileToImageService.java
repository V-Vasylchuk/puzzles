package com.vvs.puzzles.service;

import java.awt.image.BufferedImage;
import org.springframework.web.multipart.MultipartFile;

public interface FileToImageService {
    BufferedImage fileToImage(MultipartFile file);
}
