package com.vvs.puzzles.service;

import jakarta.servlet.http.HttpServletResponse;

public interface DownloadService {
    void streamZipToResponse(HttpServletResponse response, String imageName);
}
