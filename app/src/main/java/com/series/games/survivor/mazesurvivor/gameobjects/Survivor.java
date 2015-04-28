package com.series.games.survivor.mazesurvivor.gameobjects;

/**
 * Created by Luke on 4/20/2015.
 * Survivor class that implements all the player's actions
 */
public class Survivor {

    //player's X and Y indices in maze
    private int indexX;
    private int indexY;
    //Survivor got a sword for attacking
    public Sword sword;
    //Record the player's orientation
    private String orientation;
    //Survive status of the player;
    public boolean isAlive;
    //Record the previous type of player's current cell
    private char prevType;
    //Record the number of bonus time delayers that player have
    private int numOfTimeDelayer;

    public Survivor(int row, int col) {

        //Survivor is alive
        isAlive = true;
        //Randomly set a start point in maze
        this.indexX = (int)(Math.random() * (row - 1));
        this.indexY = (int) (Math.random() * (col - 1));
        //Create the sword
        sword = new Sword(indexX, indexY);
        //Initially set the player's orientation as DOWN
        orientation = "DOWN";
        //Initially se the lastType as path
        prevType = 'p';
        //Initially have 0 time delayer
        numOfTimeDelayer = 0;
    }

    /**Update the survivor position according to the touch event
     *
     * @param move
     */
    public boolean updateSurvivor(String move, MazeWorld.Cell[][] maze, boolean inChange) {
        int row = maze.length;
        int col = maze[0].length;
        boolean findExit = false;
        switch(move) {

            case "LEFT":
                int moveLeft = Dir.LEFT.moveY(indexY);
                if(isAlive && !inChange && moveLeft >= 0 && maze[indexX][moveLeft].Type != 'w') {
                    //change the player's orientation to left
                    if(orientation != "LEFT") {
                        orientation = "LEFT";
                        return false;
                    }
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = prevType;
                    prevType = maze[indexX][moveLeft].Type;
                    updateY(moveLeft);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    } else {
                        if(maze[indexX][indexY].Type == 'b') {//get a time delayer
                            numOfTimeDelayer = numOfTimeDelayer < 3 ? numOfTimeDelayer + 1 : numOfTimeDelayer;
                            prevType = 'p';
                        }
                        maze[indexX][indexY].Type = 's';
                    }
                    //Also update the position of the sword
                    sword.updateY(Dir.LEFT.moveY(sword.getY()));
                }
                break;
            case "RIGHT":
                int moveRight = Dir.RIGHT.moveY(indexY);
                if(isAlive && !inChange && moveRight < row && maze[indexX][moveRight].Type != 'w') {
                    //change the player's orientation to right
                    if(orientation != "RIGHT") {
                        orientation = "RIGHT";
                        return false;
                    }
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = prevType;
                    prevType = maze[indexX][moveRight].Type;
                    updateY(moveRight);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    } else {
                        if(maze[indexX][indexY].Type == 'b') {//get a time delayer
                            numOfTimeDelayer = numOfTimeDelayer < 3 ? numOfTimeDelayer + 1 : numOfTimeDelayer;
                            prevType = 'p';
                        }
                        maze[indexX][indexY].Type = 's';
                    }
                    //Also update the position of the sword
                    sword.updateY(Dir.RIGHT.moveY(sword.getY()));
                }
                break;
            case "UP":
                int moveUp = Dir.UP.moveX(indexX);
                if(isAlive && !inChange && moveUp >= 0 && maze[moveUp][indexY].Type != 'w') {
                    //change the player's orientation to up
                    if(orientation != "UP") {
                        orientation = "UP";
                        return false;
                    }
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = prevType;
                    prevType = maze[moveUp][indexY].Type;
                    updateX(moveUp);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    } else {
                        if(maze[indexX][indexY].Type == 'b') {//get a time delayer
                            numOfTimeDelayer = numOfTimeDelayer < 3 ? numOfTimeDelayer + 1 : numOfTimeDelayer;
                            prevType = 'p';
                        }
                        maze[indexX][indexY].Type = 's';
                    }
                    //Also update the position of the sword
                    sword.updateX(Dir.UP.moveX(sword.getX()));
                }
                break;
            case "DOWN":
                int moveDown = Dir.DOWN.moveX(indexX);
                if(isAlive && !inChange && moveDown < col && maze[moveDown][indexY].Type != 'w') {
                    //change the player's orientation to down
                    if(orientation != "DOWN") {
                        orientation = "DOWN";
                        return false;
                    }
                    //maze is not in change, and can move to the new cell
                    maze[indexX][indexY].Type = prevType;
                    prevType = maze[moveDown][indexY].Type;
                    updateX(moveDown);
                    if(maze[indexX][indexY].Type == 'e') {
                        findExit = true;
                    } else {
                        if(maze[indexX][indexY].Type == 'b') {//get a time delayer
                            numOfTimeDelayer = numOfTimeDelayer < 3 ? numOfTimeDelayer + 1 : numOfTimeDelayer;
                            prevType = 'p';
                        }
                        maze[indexX][indexY].Type = 's';
                    }
                    //Also update the position of the sword
                    sword.updateX(Dir.DOWN.moveX(sword.getX()));
                }
                break;
        }

        return findExit;
    }

    public int attack(MazeWorld.Cell[][] maze, boolean inChange, Monster[] monsters) {
        //Return the number of monsters be killed in this time's attack
        int row = maze.length;
        int col = maze[0].length;
        int beKilledMonsters = 0;

        switch(orientation) {

            case "LEFT":
                int attackLeft = Dir.LEFT.moveY(indexY);
                if(isAlive && !inChange && attackLeft >= 0 && (maze[indexX][attackLeft].Type == 'p' || maze[indexX][attackLeft].Type == 'm')) {
                    //maze is not in change, and can attack
                    beKilledMonsters = sword.attackMonster(indexX, attackLeft, maze, monsters);
                    //withdraw the sword
                    sword.updateX(indexX);
                    sword.updateY(indexY);
                }
                break;
            case "RIGHT":
                int attackRight = Dir.RIGHT.moveY(indexY);
                if(isAlive && !inChange && attackRight < row && (maze[indexX][attackRight].Type == 'p' || maze[indexX][attackRight].Type == 'm')) {
                    //maze is not in change, and can attack
                    beKilledMonsters = sword.attackMonster(indexX, attackRight, maze, monsters);
                    //withdraw the sword
                    sword.updateX(indexX);
                    sword.updateY(indexY);
                }
                break;
            case "UP":
                int attackUp = Dir.UP.moveX(indexX);
                if(isAlive && !inChange && attackUp >= 0 && (maze[attackUp][indexY].Type == 'p' || maze[attackUp][indexY].Type == 'm')) {
                    //maze is not in change, and can attack
                    beKilledMonsters = sword.attackMonster(attackUp, indexY, maze, monsters);
                    //withdraw the sword
                    sword.updateX(indexX);
                    sword.updateY(indexY);
                }
                break;
            case "DOWN":
                int attackDown = Dir.DOWN.moveX(indexX);
                if(isAlive && !inChange && attackDown < col && (maze[attackDown][indexY].Type == 'p' || maze[attackDown][indexY].Type == 'm')) {
                    //maze is not in change, and can attack
                    beKilledMonsters = sword.attackMonster(attackDown, indexY, maze, monsters);
                    //withdraw the sword
                    sword.updateX(indexX);
                    sword.updateY(indexY);
                }
                break;
        }
        return beKilledMonsters;
    }

    public int getX() {

        return indexX;
    }

    public int getY() {

        return indexY;
    }

    public int getNumOfTimeDelayer() {

        return numOfTimeDelayer;
    }

    public void decreaseNumOfTimeDelayer() {

        numOfTimeDelayer = numOfTimeDelayer > 0 ? numOfTimeDelayer - 1 : 0;
    }

    public void updateX(int newX) {

        indexX = newX;
    }

    public void updateY(int newY) {

        indexY = newY;
    }
}
