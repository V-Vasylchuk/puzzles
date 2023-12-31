package com.vvs.puzzles.service.solver.impl;

import com.vvs.puzzles.model.Tile;
import com.vvs.puzzles.service.solver.SolutionDrawingService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class SolutionDrawingServiceImpl implements SolutionDrawingService {
    @Override
    public BufferedImage drawSolutionFromTable(Tile[][] table) {
        Tile[][] croppedTable = cropTableToImageSize(table);

        int[] heights = getRowsHeights(croppedTable);
        int[] widths = getColumnsWidths(croppedTable);
        BufferedImage solutionImage = new BufferedImage(Arrays.stream(widths).sum(),
                Arrays.stream(heights).sum(), BufferedImage.TYPE_INT_RGB);
        Graphics2D imgCreator = solutionImage.createGraphics();
        int currXPixel = 0;
        int currYPixel = 0;
        for (int y = 0; y < croppedTable.length; y++) {
            currXPixel = 0;
            for (int x = 0; x < croppedTable[y].length; x++) {
                Tile tile = croppedTable[y][x];
                if (tile != null) {
                    imgCreator.drawImage(tile.getTileImage(), currXPixel, currYPixel, null);
                }
                currXPixel += widths[x];
            }
            currYPixel += heights[y];
        }
        return solutionImage;
    }

    private Tile[][] cropTableToImageSize(Tile[][] table) {
        Integer topTileY = null;
        Integer bottomTileY = null;
        Integer leftMostTileX = null;
        Integer rightMostTileX = null;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                Tile tile = table[i][j];
                if (tile != null) {
                    if (topTileY == null) {
                        topTileY = tile.getTableY();
                    }
                    if (bottomTileY == null || i > bottomTileY) {
                        bottomTileY = tile.getTableY();
                    }
                    if (leftMostTileX == null || j < leftMostTileX) {
                        leftMostTileX = tile.getTableX();
                    }
                    if (rightMostTileX == null || j > rightMostTileX) {
                        rightMostTileX = tile.getTableX();
                    }
                }
            }
        }

        Tile[][] croppedTable =
                new Tile[bottomTileY - topTileY + 1][rightMostTileX - leftMostTileX + 1];
        for (int i = 0; i < croppedTable.length; i++) {
            System.arraycopy(table[i + topTileY], leftMostTileX, croppedTable[i],
                        0, croppedTable[i].length);
        }
        return croppedTable;
    }

    private int[] getRowsHeights(Tile[][] table) {
        int[] heights = new int[table.length];
        for (int y = 0; y < table.length; y++) {
            for (int x = 0; x < table[y].length; x++) {
                Tile tile = table[y][x];
                if (tile != null && tile.getTileImage().getHeight() > heights[y]) {
                    heights[y] = tile.getTileImage().getHeight();
                }
            }
        }
        return heights;
    }

    private int[] getColumnsWidths(Tile[][] table) {
        int[] widths = new int[table[0].length];
        for (Tile[] tiles : table) {
            for (int x = 0; x < tiles.length; x++) {
                Tile tile = tiles[x];
                if (tile != null && tile.getTileImage().getWidth() > widths[x]) {
                    widths[x] = tile.getTileImage().getWidth();
                }
            }
        }
        return widths;
    }
}
