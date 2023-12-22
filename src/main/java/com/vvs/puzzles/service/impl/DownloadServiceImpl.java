package com.vvs.puzzles.service.impl;

import com.vvs.puzzles.exeption.DownloadException;
import com.vvs.puzzles.repository.PuzzleRepository;
import com.vvs.puzzles.service.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@RequiredArgsConstructor
@Service
public class DownloadServiceImpl implements DownloadService {
    private final PuzzleRepository puzzleRepository;

    @Override
    public void streamZipToResponse(HttpServletResponse response, String imageName) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");
        List<String> filesList = puzzleRepository.getPuzzleFilesByImageName(imageName);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for (String fileName : filesList) {
                FileSystemResource fileSystemResource = new FileSystemResource(fileName);
                ZipEntry zipEntry =
                        new ZipEntry(Objects.requireNonNull(fileSystemResource.getFilename()));
                zipEntry.setSize(fileSystemResource.contentLength());
                zipEntry.setTime(System.currentTimeMillis());
                zipOutputStream.putNextEntry(zipEntry);
                StreamUtils.copy(fileSystemResource.getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
        } catch (IOException e) {
            throw new DownloadException("Failed to output file in ZipOutputStream", e);
        }
    }
}
