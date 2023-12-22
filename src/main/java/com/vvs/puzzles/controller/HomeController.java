package com.vvs.puzzles.controller;

import com.vvs.puzzles.dto.ImageInfoDto;
import com.vvs.puzzles.service.PuzzleService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {
    private final PuzzleService puzzleService;

    @GetMapping
    public String displayHomePage(Model model) {
        List<ImageInfoDto> imageInfosList = puzzleService.getAllPuzzles().stream()
                .map(s -> new ImageInfoDto(s,
                        "/game?imageName=" + s,
                        "/download?imageName=" + s))
                .collect(Collectors.toList());
        model.addAttribute("imageInfosList", imageInfosList);
        return "home";
    }
}
