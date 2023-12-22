package com.vvs.puzzles.service.impl;

import com.vvs.puzzles.exeption.GameException;
import com.vvs.puzzles.model.GameBoard;
import com.vvs.puzzles.model.Puzzle;
import com.vvs.puzzles.repository.PuzzleRepository;
import com.vvs.puzzles.service.PuzzleGameService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuzzleGameServiceImpl implements PuzzleGameService {
    public static final List<Integer> ROTATION_DEGREES = List.of(0, 90, 180, 270);
    private final PuzzleRepository repository;

    @Override
    public GameBoard createInitTable(String imageName) {
        List<String> tileImagesList = repository.getTilesUrlsByImageName(imageName);
        String originalImg = repository.getOriginalImageByName(imageName);

        //Randomizer for puzzles
        List<Integer> initPositions = IntStream.range(0, tileImagesList.size())
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(initPositions);

        //List for puzzles
        List<Puzzle> tilesList = new ArrayList<>();
        for (int i = 0; i < tileImagesList.size(); i++) {
            int randomIndex = new Random().nextInt(ROTATION_DEGREES.size());
            int rotationDegrees = ROTATION_DEGREES.get(randomIndex);
            tilesList.add(new Puzzle(i, initPositions.get(i),
                    rotationDegrees, tileImagesList.get(i)));
        }

        List<List<Puzzle>> tilesTable = sortAndCreateSquareTable(tilesList);
        return new GameBoard(originalImg, tilesList, tilesTable);
    }

    @Override
    public GameBoard swapPuzzles(GameBoard gameBoard,
                                 Integer firstTilePos,
                                 Integer secondTilePos) {
        List<Puzzle> tilesList = gameBoard.getTilesList();
        Puzzle firstPuzzle = tilesList.stream()
                .filter(t -> t.getCurrentPosition().equals(firstTilePos))
                .findFirst()
                .orElseThrow(() -> new GameException("No such position " + firstTilePos));
        Puzzle secondPuzzle = tilesList.stream()
                .filter(t -> t.getCurrentPosition().equals(secondTilePos))
                .findFirst()
                .orElseThrow(() -> new GameException("No such position " + secondTilePos));

        firstPuzzle.setCurrentPosition(secondTilePos);
        secondPuzzle.setCurrentPosition(firstTilePos);

        List<List<Puzzle>> tilesTable = sortAndCreateSquareTable(tilesList);
        gameBoard.setTilesTable(tilesTable);
        return gameBoard;
    }

    @Override
    public GameBoard rotateRight(GameBoard gameBoard, Integer tilePos) {
        List<Puzzle> tilesList = gameBoard.getTilesList();
        Puzzle puzzle = tilesList.stream()
                .filter(t -> t.getCurrentPosition().equals(tilePos))
                .findFirst()
                .orElseThrow(() -> new GameException("No such position " + tilePos));
        int rotationIndex = ROTATION_DEGREES.indexOf(puzzle.getRotation());
        if (rotationIndex == ROTATION_DEGREES.size() - 1) {
            puzzle.setRotation(ROTATION_DEGREES.get(0));
        } else {
            puzzle.setRotation(ROTATION_DEGREES.get(rotationIndex + 1));
        }
        List<List<Puzzle>> tilesTable = sortAndCreateSquareTable(tilesList);
        gameBoard.setTilesTable(tilesTable);
        return gameBoard;
    }

    @Override
    public boolean checkSolution(GameBoard gameBoard) {
        boolean result = true;
        for (Puzzle puzzle : gameBoard.getTilesList()) {
            if (!puzzle.getCurrentPosition().equals(puzzle.getCorrectPosition())
                    || puzzle.getRotation() != 0) {
                result = false;
                break;
            }
        }
        return result;
    }

    private List<List<Puzzle>> sortAndCreateSquareTable(List<Puzzle> tilesList) {
        tilesList.sort(Comparator.comparingInt(Puzzle::getCurrentPosition));
        int gridSideLength = (int) Math.sqrt(tilesList.size());
        List<List<Puzzle>> tilesTable = new ArrayList<>();
        for (int i = 0; i < gridSideLength; i++) {
            tilesTable.add(new ArrayList<>());
            for (int j = 0; j < gridSideLength; j++) {
                int pos = i * gridSideLength + j;
                tilesTable.get(i).add(tilesList.get(pos));
            }
        }
        return tilesTable;
    }
}
