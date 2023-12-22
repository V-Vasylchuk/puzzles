package com.vvs.puzzles.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class TileEdge {
    private final Tile tile;
    private final Side side;
    private final Map<TileEdge, Double> compatibilityMap = new HashMap<>();
    private int[] pixels;
    private boolean available = true;

    public enum Side {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
