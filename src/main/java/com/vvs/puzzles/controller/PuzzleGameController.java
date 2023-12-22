package com.vvs.puzzles.controller;

import com.vvs.puzzles.model.GameBoard;
import com.vvs.puzzles.service.PuzzleGameService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game")
public class PuzzleGameController {
    private final PuzzleGameService puzzleGameService;

    @GetMapping
    public String loadGame(Model model,
                           HttpSession session,
                           @RequestParam String imageName) {
        GameBoard gameBoard = puzzleGameService.createInitTable(imageName);
        session.setAttribute("game-board", gameBoard);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/swap")
    public String swapPuzzles(Model model,
                              HttpSession session,
                              @RequestParam Integer firstTilePosition,
                              @RequestParam Integer secondTilePosition) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        String message = null;
        try {
            gameBoard = puzzleGameService.swapPuzzles(gameBoard,
                                                      firstTilePosition,
                                                      secondTilePosition);
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.addAttribute("message", message);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/rotate")
    public String rotateRight(Model model,
                              HttpSession session,
                              @RequestParam Integer tilePos) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        String message = null;
        try {
            gameBoard = puzzleGameService.rotateRight(gameBoard, tilePos);
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.addAttribute("message", message);
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }

    @PostMapping("/check")
    public String checkSolution(Model model,
                                HttpSession session) {
        GameBoard gameBoard = (GameBoard) session.getAttribute("game-board");
        boolean correctSolution = puzzleGameService.checkSolution(gameBoard);
        String message;
        if (correctSolution) {
            message = "Good job";
        } else {
            message = "Incorrect! You can see correct image below.";
        }
        model.addAttribute("message", message);
        model.addAttribute("originalImg", gameBoard.getOriginalImageUrl());
        model.addAttribute("tilesTable", gameBoard.getTilesTable());
        return "game";
    }
}
