package com.vvs.puzzles.service.solver.impl;

import com.vvs.puzzles.model.Tile;
import com.vvs.puzzles.model.TileEdge;
import com.vvs.puzzles.repository.PuzzleRepository;
import com.vvs.puzzles.service.FileToImageService;
import com.vvs.puzzles.service.solver.PuzzleSolverService;
import com.vvs.puzzles.service.solver.SolutionDrawingService;
import com.vvs.puzzles.service.solver.SolverTileService;
import com.vvs.puzzles.service.solver.TilesCompatibilityService;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PuzzleSolverServiceImpl implements PuzzleSolverService {
    private final PuzzleRepository puzzleRepository;
    private final SolverTileService solverTileService;
    private final TilesCompatibilityService tilesCompatibilityService;
    private final SolutionDrawingService solutionDrawer;
    private final FileToImageService fileToImageReader;

    @Override
    public String solvePuzzle(MultipartFile[] files) {
        List<Tile> tilesList = Arrays.stream(files)
                .map(fileToImageReader::fileToImage)
                .map(solverTileService::createSolverTile)
                .collect(Collectors.toList());
        tilesCompatibilityService.setTilesCompatibilityBySides(tilesList,
                TileEdge.Side.TOP, TileEdge.Side.BOTTOM);
        tilesCompatibilityService.setTilesCompatibilityBySides(tilesList,
                TileEdge.Side.RIGHT, TileEdge.Side.LEFT);

        //Table for solution
        Tile[][] solutionTable = new Tile[tilesList.size() * 2][tilesList.size() * 2];
        placeInitialTileOnTable(tilesList, solutionTable);
        for (int i = 0; i < tilesList.size() - 1; i++) {
            placeMostCompatibleOnTable(tilesList, solutionTable);
        }
        BufferedImage solutionImage = solutionDrawer.drawSolutionFromTable(solutionTable);
        return puzzleRepository.saveSolution(solutionImage);
    }

    //Putting tile with the best compatability in the center of the table
    private void placeInitialTileOnTable(List<Tile> tilesList, Tile[][] table) {
        TileEdge tileEdge = tilesList.stream()
                .flatMap(t -> t.getTileEdges().stream())
                .max(Comparator.comparingDouble(e ->
                        e.getCompatibilityMap().values().stream()
                                .max(Double::compareTo)
                                .orElseThrow()))
                .orElseThrow();
        Tile tile1 = tileEdge.getTile();
        table[table.length / 2][table.length / 2] = tile1;
        tile1.setTableX(table.length / 2);
        tile1.setTableY(table.length / 2);
        tile1.setPlaced(true);
    }

    private void placeMostCompatibleOnTable(List<Tile> tilesList, Tile[][] table) {
        // Find tile placed on table that has the highest compatibility with unplaced tile
        TileEdge firstTileEdge = tilesList.stream()
                .filter(Tile::isPlaced)
                .flatMap(t -> t.getTileEdges().stream())
                .filter(TileEdge::isAvailable)
                .max(Comparator.comparingDouble(edge ->
                        edge.getCompatibilityMap().entrySet().stream()
                                .filter(e -> !e.getKey().getTile().isPlaced())
                                .map(Map.Entry::getValue)
                                .max(Double::compareTo)
                                .orElseThrow()))
                .orElseThrow();
        // Find opposite edge
        TileEdge secondTileEdge = firstTileEdge.getCompatibilityMap().entrySet().stream()
                .filter(e -> !e.getKey().getTile().isPlaced())
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .orElseThrow().getKey();

        placeTileOnTable(table, firstTileEdge, secondTileEdge);
    }

    private void placeTileOnTable(Tile[][] table,
                                  TileEdge tileEdgePlaced,
                                  TileEdge tileEdgeNew) {
        tileEdgePlaced.setAvailable(false);
        tileEdgeNew.setAvailable(false);
        Tile tilePlaced = tileEdgePlaced.getTile();
        Tile tileNew = tileEdgeNew.getTile();
        tileNew.setPlaced(true);

        switch (tileEdgeNew.getSide()) {
            case TOP -> {
                tileNew.setTableX(tilePlaced.getTableX());
                tileNew.setTableY(tilePlaced.getTableY() + 1);
            }
            case BOTTOM -> {
                tileNew.setTableX(tilePlaced.getTableX());
                tileNew.setTableY(tilePlaced.getTableY() - 1);
            }
            case RIGHT -> {
                tileNew.setTableX(tilePlaced.getTableX() - 1);
                tileNew.setTableY(tilePlaced.getTableY());
            }
            case LEFT -> {
                tileNew.setTableX(tilePlaced.getTableX() + 1);
                tileNew.setTableY(tilePlaced.getTableY());
            }
            default -> { }
        }
        table[tileNew.getTableY()][tileNew.getTableX()] = tileNew;
        closeAdjacentEdges(table, tileNew.getTableX(), tileNew.getTableY());
    }

    //make common edges of given tile and all neighbouring tiles unavailable for use
    private void closeAdjacentEdges(Tile[][] table, int x, int y) {
        if (table[y - 1][x] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.TOP)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y - 1][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.BOTTOM)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
        if (table[y + 1][x] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.BOTTOM)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y + 1][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.TOP)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
        if (table[y][x - 1] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.LEFT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y][x - 1].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.RIGHT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
        if (table[y][x + 1] != null) {
            table[y][x].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.RIGHT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
            table[y][x + 1].getTileEdges().stream()
                    .filter(e -> e.getSide() == TileEdge.Side.LEFT)
                    .findFirst()
                    .ifPresent(e -> e.setAvailable(false));
        }
    }
}
