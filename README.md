# Puzzle Solver
This software is developed to break down and subsequently assemble images in the form of puzzles.
It includes both manual and automatic puzzle assembly.

## Features and Endpoints:
#### 1. `/` Home Page
* Display all uploaded pictures with links to play with them or download.
#### 2. `/upload` Algorithm for Breaking Down an Image into Puzzles
* You can upload your images up to 5MB, and choose from `2x2` to `5x5` sizes
* For each puzzle, information is stored, such as coordinates, size, and side.
#### 3. `/game` Manual Puzzle Assembly
* A Java application is implemented that allows for manual puzzle assembly.
* Supports actions such as swapping puzzles, rotations, and checking the correctness of assembly.
#### 4. `/solver` Algorithm for Automatic Puzzle Assembly
* An algorithm is created that automatically assembles puzzles without prior information about the original image.
* Upload your puzzle-tile (rectangular JPG images that are part of one sliced image). 
Puzzle tiles can be of any size, aspect-ratio and not necessarily all equal (the only - JPG format, upright position, 5MB combined size). 

## Used Technologies:
* Java `17`
* Spring Boot `3.2.0`
* Thymeleaf
* Lombok
* Docker

## How to run:
* Ensure that you have Docker installed and running;
* Clone this repository;
* Build the project with command: `mvn clean package`;
* Press `Run`;
* Go to http://localhost:8080/ and enjoy.

### Author
_[Vasyl Vasylchuk](https://www.linkedin.com/in/vasyl-vasylchuk-632303273/)_