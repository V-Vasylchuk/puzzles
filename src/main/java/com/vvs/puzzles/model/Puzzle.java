package com.vvs.puzzles.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Puzzle {
    private final Integer correctPosition;
    private Integer currentPosition;
    private Integer rotation;
    private String imageUrl;
}
