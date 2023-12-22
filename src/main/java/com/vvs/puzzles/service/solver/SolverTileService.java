package com.vvs.puzzles.service.solver;

import com.vvs.puzzles.model.Tile;
import java.awt.image.BufferedImage;

public interface SolverTileService {
    Tile createSolverTile(BufferedImage image);
}
