package com.vvs.puzzles.controller;

import com.vvs.puzzles.service.solver.PuzzleSolverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/solver")
public class PuzzleSolverController {
    private final PuzzleSolverService puzzleSolverService;

    @GetMapping
    public String displaySolverPage() {
        return "solver";
    }

    @PostMapping
    public String uploadTileImages(Model model,
                                   @RequestParam("files") MultipartFile[] files) {
        String solutionUrl = null;
        String message = "Great! You solved it successfully!";
        try {
            solutionUrl = puzzleSolverService.solvePuzzle(files);
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.addAttribute("solutionUrl", solutionUrl);
        model.addAttribute("message", message);
        return "solver";
    }
}
