package com.vvs.puzzles.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameBoard {
    private String originalImageUrl;
    private List<Puzzle> tilesList;
    private List<List<Puzzle>> tilesTable;
}
