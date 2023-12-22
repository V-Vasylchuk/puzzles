package com.vvs.puzzles.service.impl;

import com.vvs.puzzles.repository.PuzzleRepository;
import com.vvs.puzzles.service.PuzzleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PuzzleServiceImpl implements PuzzleService {
    private final PuzzleRepository puzzleRepository;

    @Override
    public List<String> getAllPuzzles() {
        return puzzleRepository.getAllPuzzles();
    }
}
