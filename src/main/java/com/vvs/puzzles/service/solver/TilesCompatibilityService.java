package com.vvs.puzzles.service.solver;

import com.vvs.puzzles.model.Tile;
import com.vvs.puzzles.model.TileEdge;
import java.util.List;

public interface TilesCompatibilityService {
    List<Tile> setTilesCompatibilityBySides(List<Tile> tilesList,
                                            TileEdge.Side side1,
                                            TileEdge.Side side2);
}
