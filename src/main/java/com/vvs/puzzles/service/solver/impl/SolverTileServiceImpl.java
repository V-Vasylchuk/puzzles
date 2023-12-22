package com.vvs.puzzles.service.solver.impl;

import com.vvs.puzzles.model.Tile;
import com.vvs.puzzles.model.TileEdge;
import com.vvs.puzzles.service.solver.SolverTileService;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SolverTileServiceImpl implements SolverTileService {
    @Override
    public Tile createSolverTile(BufferedImage image) {
        Tile tile = new Tile(image);
        createEdges(tile);
        setEdgesPixels(tile);
        return tile;
    }

    private void createEdges(Tile tile) {
        List<TileEdge> edgesList = new ArrayList<>();
        for (TileEdge.Side side : TileEdge.Side.values()) {
            edgesList.add(new TileEdge(tile, side));
        }
        tile.setTileEdges(edgesList);
    }

    //fill the array representing pixels(colors) on the edge of the tile
    private void setEdgesPixels(Tile tile) {
        for (TileEdge tileEdge : tile.getTileEdges()) {
            int width = tile.getTileImage().getWidth();
            int height = tile.getTileImage().getHeight();
            int[] pixels;

            switch (tileEdge.getSide()) {
                case TOP -> {
                    pixels = new int[width];
                    for (int i = 0; i < width; i++) {
                        pixels[i] = tile.getTileImage().getRGB(i, 0);
                    }
                    tileEdge.setPixels(pixels);
                }
                case BOTTOM -> {
                    pixels = new int[width];
                    for (int i = 0; i < width; i++) {
                        pixels[i] = tile.getTileImage().getRGB(i, height - 1);
                    }
                    tileEdge.setPixels(pixels);
                }
                case RIGHT -> {
                    pixels = new int[height];
                    for (int i = 0; i < height; i++) {
                        pixels[i] = tile.getTileImage().getRGB(width - 1, i);
                    }
                    tileEdge.setPixels(pixels);
                }
                case LEFT -> {
                    pixels = new int[height];
                    for (int i = 0; i < height; i++) {
                        pixels[i] = tile.getTileImage().getRGB(0, i);
                    }
                    tileEdge.setPixels(pixels);
                }
                default -> { }
            }
        }
    }
}
