package com.vvs.puzzles.service.solver.impl;

import com.vvs.puzzles.model.Tile;
import com.vvs.puzzles.model.TileEdge;
import com.vvs.puzzles.service.solver.TilesCompatibilityService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TilesCompatibilityServiceImpl implements TilesCompatibilityService {
    private static final double PIXEL_MAX_VALUE = 16777216;

    @Override
    public List<Tile> setTilesCompatibilityBySides(List<Tile> tilesList,
                                                   TileEdge.Side firstSide,
                                                   TileEdge.Side secondSide) {
        for (Tile firstTile : tilesList) {
            TileEdge firstTileEdge = firstTile.getTileEdges().stream()
                    .filter(e -> e.getSide() == firstSide)
                    .findFirst()
                    .orElseThrow();
            for (Tile secondTile : tilesList) {
                if (secondTile != firstTile) {
                    TileEdge secondTileEdge = secondTile.getTileEdges().stream()
                            .filter(e -> e.getSide() == secondSide)
                            .findFirst()
                            .orElseThrow();
                    double compatibility = calculateEdgesCompatability(firstTileEdge,
                                                                       secondTileEdge);

                    firstTileEdge.getCompatibilityMap().put(secondTileEdge, compatibility);
                    secondTileEdge.getCompatibilityMap().put(firstTileEdge, compatibility);
                }
            }
        }
        return tilesList;
    }

    private double calculateEdgesCompatability(TileEdge firstTileEdge, TileEdge secondTileEdge) {
        if (firstTileEdge.getPixels().length != secondTileEdge.getPixels().length) {
            return 0;
        }
        int[] firstEdgePixels = firstTileEdge.getPixels();
        int[] secondEdgePixels = secondTileEdge.getPixels();
        double sum = 0;
        for (int i = 0; i < firstEdgePixels.length; i++) {
            sum += (PIXEL_MAX_VALUE - Math.abs(firstEdgePixels[i] - secondEdgePixels[i]))
                        / PIXEL_MAX_VALUE;
        }
        return sum / firstEdgePixels.length;
    }
}
