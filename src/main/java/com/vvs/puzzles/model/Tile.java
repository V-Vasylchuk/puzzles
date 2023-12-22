package com.vvs.puzzles.model;

import java.awt.image.BufferedImage;
import java.util.List;
import lombok.Data;

@Data
public class Tile {
    private final BufferedImage tileImage;
    private List<TileEdge> tileEdges;
    private boolean placed = false;
    private int tableX;
    private int tableY;
}
