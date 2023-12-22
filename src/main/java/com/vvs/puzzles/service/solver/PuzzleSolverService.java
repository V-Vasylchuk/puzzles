package com.vvs.puzzles.service.solver;

import org.springframework.web.multipart.MultipartFile;

public interface PuzzleSolverService {
    String solvePuzzle(MultipartFile[] tiles);
}
