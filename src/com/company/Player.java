package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Alicja Ochman[r0693935]
 */
public class Player{
    //finishedGame - flag indicating if the player reached the end field
    static boolean finishedGame = false;
    //Initial position of the player equal to the start field
    static int initialPosition;
    //No of moves made by the player traversing the maze
    static int noOfMoves;
    //List of items held by the plater
    static List<String> playerPossessions = new ArrayList<>();
    //Width of the maze indicated by X coordinate
    int width;

    public Player() throws IOException {
    }

    /**
     * Method of playing game
     * @throws IOException
     *          Throws an exception if method createArray throws an exception (file not found)
     */
    public void playGame(String filePath) throws IOException{
        //Create new array
        List<MazeArray> mazeRepresentation = MazeArray.createArray(filePath);
        //Get width of the maze
        width = MazeArray.getWidth(filePath);
        //InitialPosition - sets player position on start field
        initialPosition = setInitialPosition(filePath);
        Scanner userInput = new Scanner(System.in);
        //While loop if the player did not reach end field
        while(!finishedGame){
            //Printing statement to show player's statistics
            System.out.println("So far you made " + getNoOfMoves() + " move(s) and you have following items in your possession:" + getPlayerPossessions());
            // Asking for players input
            System.out.println("Please enter next move. You can type: up, down, left, right or w,s,a,d.");
            //Reading players input
            String input = userInput.nextLine();
            //Invoking methods corresponding to player's move
            if(input.equals("down") || input.equals("s"))
                moveDown(mazeRepresentation);
            else if(input.equals("up") || input.equals("w"))
                moveUp(mazeRepresentation);
            else if(input.equals("left") || input.equals("a"))
                moveLeft(mazeRepresentation);
            else if(input.equals("right") || input.equals("d"))
                moveRight(mazeRepresentation);
            else
                System.out.println("Invalid input. Try again.");
        }

        //Finished game procedure
        if(finishedGame){
            //Trophies collected by the player
            int noOfTrophies = 0;
            //Final score of the game
            int finalScore;
            System.out.println("CONGRATULATIONS! You finished the game!");
            //Asking for player's name
            String name = getPlayerName();
            //Calculating no of trophies collected by the player
            for(String element: playerPossessions){
                if(element.equals("trophy")){
                    noOfTrophies += 1;
                    System.out.println("You get bonus 10% for every trophy you collected.");}
            }
            //Calculating final score of the game
            finalScore = (int) (getNoOfMoves() - (0.1 * noOfTrophies) * getNoOfMoves());
            //Printing of the statistics of the game
            System.out.println(name + ", you made " + getNoOfMoves() + " moves and collected " + noOfTrophies + " trophies.");
            System.out.println("Your final score is: " + finalScore + " and will be saved.");

            //Form a new file if non existent, write player's score to the file
            try(FileWriter fw = new FileWriter("HighScore.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(name + "," + filePath + "," + finalScore + " moves");

            } catch (IOException e) {
                throw new IOException("Something went wrong!");
            }
        }
    }

    /**
     * Method of moving down
     * @param mazeRepresentation
     *         Representation of a maze
     * @throws IOException
     *          Throws a exception if file not found
     */
    public void moveDown(List<MazeArray> mazeRepresentation) throws IOException{
        //If wall is fake or there's no wall player can walk to the chosen field
        if (("fake".equals(mazeRepresentation.get(getPlayerPosition()).getSouthWall()))
                || ("no".equals(mazeRepresentation.get(getPlayerPosition()).getSouthWall()))) {
            //Change no of player's moves
            setNoOfMoves(1);
            //If this is the end field finish the game
            if("end".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject())
                    || "E".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject()))
                isFinished();
            //If there is an object on the field
            if(!"no".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject())
                    && !"start".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject()))
                //If not end field, check if field contains object. Add to possessions if object exists
                addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() - width).getObject());
            // Change player position
            mazeRepresentation.get(getPlayerPosition() - width).setObject(getPlayerPosition() - width,"P", mazeRepresentation);
            mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
            //Set new position of a player
            setPlayerPosition(-width);
            //Print out new layout of the maze
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));
        }
        //In case of a breakable wall
        else if ("breakable".equals(mazeRepresentation.get(getPlayerPosition()).getSouthWall())){
            // Checking for hammer in possessions
            if(playerPossessions.contains("hammer")){
                //Change no of player's moves
                setNoOfMoves(1);
                //Remove breakable wall
                mazeRepresentation.get(getPlayerPosition()).setSouthWall(getPlayerPosition(), "no", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition() - width).setNorthWall(getPlayerPosition() - width, "no", mazeRepresentation);
                //If field on which player steps is the end field; the game ends.
                if("end".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject())
                        || "E".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject()))
                    isFinished();
                //If not end field, check if field contains object. Add to possessions if object exists
                if(!"no".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject()))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() - width).getObject());
                mazeRepresentation.get(getPlayerPosition() - width).setObject(getPlayerPosition() - width,"P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                //Set new position of a player
                setPlayerPosition(-width);
                //Print out new layout of the maze
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            }else{
                System.out.println("You cannot walk through this wall. You have no hammer to break it");
                //Print out layout of the maze
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        //In case of door
        else if("door".equals(mazeRepresentation.get(getPlayerPosition()).getSouthWall())){
            //Check if player's possessions contain key
            if(playerPossessions.contains("key")){
                //Change no of player's moves
                setNoOfMoves(1);
                if("end".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject())
                        || "E".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject()))
                    isFinished();
                if(!"no".equals(mazeRepresentation.get(getPlayerPosition() - width).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() - width)))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() - width).getObject());
                mazeRepresentation.get(getPlayerPosition() - width).setObject(getPlayerPosition() - width,"P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                setPlayerPosition(-width);
                //Displaying maze with new position of a player.
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            }
            //If player does not have a key, he/she cannot pass the door
            else{
                System.out.println("You cannot open this door without a key");
                //Print out layout of the maze
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        //If this is just a normal wall. Player cannot pass it.
        else{
            System.out.println("You cannot walk through the wall");
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));}
    }

    /**
     * Method of moving up
     * @param mazeRepresentation
     *        Representation of a maze
     * @throws IOException
     *          Throws an exception if file not found
     */
    public void moveUp(List<MazeArray> mazeRepresentation) throws IOException{
        if ("fake".equals(mazeRepresentation.get(getPlayerPosition()).getNorthWall())
                || "no".equals(mazeRepresentation.get(getPlayerPosition()).getNorthWall())){
            //Change no of player's moves
            setNoOfMoves(1);
            //If this is the end field finish the game
            if("end".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject())
                    || "E".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject()))
                isFinished();
            if(!"no".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject())
                    && !"start".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject()))
                //If not end field, check if field contains object. Add to possessions if object exists
                addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() + width).getObject());
            // Change player position
            mazeRepresentation.get(getPlayerPosition() + width).setObject(getPlayerPosition() + width,"P", mazeRepresentation);
            mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
            //Set new position of a player
            setPlayerPosition(width);
            //Displaying maze with new position of a player
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));
        }
        else if ("breakable".equals(mazeRepresentation.get(getPlayerPosition()).getNorthWall())) {
            // Checking for hammer in possessions
            if (playerPossessions.contains("hammer")) {
                //Change no of player's moves
                setNoOfMoves(1);
                //Remove breakable wall;
                mazeRepresentation.get(getPlayerPosition()).setNorthWall(getPlayerPosition(), "no", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition() + width).setSouthWall(getPlayerPosition() + width, "no", mazeRepresentation);
                //If field on which player steps is the end field; the game ends.
                if ("end".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject())
                        || "E".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject()))
                    isFinished();
                //If not end field, check if field contains object. Add to possessions if object exists
                if (!"no".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject()))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() + width).getObject());
                mazeRepresentation.get(getPlayerPosition() + width).setObject(getPlayerPosition() + width, "P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                //Set new position of a player
                setPlayerPosition(width);
                //Displaying a maze with new position of a player
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            } else{
                System.out.println("You cannot walk through this wall. You have no hammer to break it");
                //Displaying a maze layout
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        else if("door".equals(mazeRepresentation.get(getPlayerPosition()).getNorthWall())) {
            //Check if player's possessions contain key
            if (playerPossessions.contains("key")) {
                //Change no of player's moves
                setNoOfMoves(1);
                if ("end".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject())
                        || "E".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject()))
                    isFinished();
                if (!"no".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() + width).getObject()))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() + width).getObject());
                mazeRepresentation.get(getPlayerPosition() + width).setObject(getPlayerPosition() + width, "P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                // Setting new position of a player
                setPlayerPosition(width);
                //Displaying maze with new position of a player
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            }
            //If player does not have a key, he/she cannot pass the door
            else{
                System.out.println("You cannot open this door without a key");
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        else{
            System.out.println("You cannot walk through the wall");
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));}
    }

    /**
     * Method of moving left
     * @param mazeRepresentation
     *        Representation of a maze
     * @throws IOException
     *          Throws an exception if file not found
     */
    public void moveLeft(List<MazeArray> mazeRepresentation)throws IOException{
        if ("fake".equals(mazeRepresentation.get(getPlayerPosition()).getWestWall())
                || "no".equals(mazeRepresentation.get(getPlayerPosition()).getWestWall())){
            //Change no of player's moves
            setNoOfMoves(1);
            //If this is the end field finish the game
            if("end".equals(mazeRepresentation.get(getPlayerPosition() - 1).getObject())
                    || "E".equals(mazeRepresentation.get(getPlayerPosition() -1).getObject()))
                isFinished();
            if(!"no".equals(mazeRepresentation.get(getPlayerPosition() - 1).getObject())
                    && !"start".equals(mazeRepresentation.get(getPlayerPosition() - 1).getObject()) )
                //If not end field, check if field contains object. Add to possessions if object exists
                addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() - 1).getObject());
            // Change player position
            mazeRepresentation.get(getPlayerPosition()- 1).setObject(getPlayerPosition() -1,"P", mazeRepresentation);
            mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
            //Set new position of a player
            setPlayerPosition(-1);
            //Displaying maze with new position of a player
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));
        }
        else if ("breakable".equals(mazeRepresentation.get(getPlayerPosition()).getWestWall())){
            // Checking for hammer in possessions
            if (playerPossessions.contains("hammer")) {
                //Change no of player's moves
                setNoOfMoves(1);
                //Remove breakable wall
                mazeRepresentation.get(getPlayerPosition()).setWestWall(getPlayerPosition(), "no", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition() - 1).setEastWall(getPlayerPosition() -1, "no", mazeRepresentation);
                //If field on which player steps is the end field; the game ends.
                if ("end".equals(mazeRepresentation.get(getPlayerPosition() -1).getObject())
                        || "E".equals(mazeRepresentation.get(getPlayerPosition() -1).getObject()) )
                    isFinished();
                //If not end field, check if field contains object. Add to possessions if object exists
                if (!"no".equals(mazeRepresentation.get(getPlayerPosition() -1).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() -1).getObject()))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() -1).getObject());
                mazeRepresentation.get(getPlayerPosition() - 1).setObject(getPlayerPosition() - 1, "P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                //Set new position of a player
                setPlayerPosition(-1);
                //Displaying maze with new position of a player
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            } else{
                System.out.println("You cannot walk through this wall. You have no hammer to break it");
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        else if("door".equals(mazeRepresentation.get(getPlayerPosition()).getWestWall())){
            //Check if player's possessions contain key
            if (playerPossessions.contains("key")) {
                //Change no of player's moves
                setNoOfMoves(1);
                if ("end".equals(mazeRepresentation.get(getPlayerPosition() - 1).getObject()) || "E".equals(mazeRepresentation.get(getPlayerPosition() - 1).getObject()))
                    isFinished();
                if (!"no".equals(mazeRepresentation.get(getPlayerPosition() - 1).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() -1).getObject()))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() -1).getObject());
                mazeRepresentation.get(getPlayerPosition() -1).setObject(getPlayerPosition() - 1, "P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                //Setting new player position
                setPlayerPosition(-1);
                //Displaying maze with new position of a player
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            }
            //If player does not have a key, he/she cannot pass the door
            else{
                System.out.println("You cannot open this door without a key");
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        else{
            System.out.println("You can no walk through the wall");
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));}
    }

    /**
     * Method of moving right
     * @param mazeRepresentation
     *        Representation of a maze
     * @throws IOException
     *          Throws an exception if file not found
     */
    public void moveRight(List<MazeArray> mazeRepresentation) throws IOException{
        if ("fake".equals(mazeRepresentation.get(getPlayerPosition()).getEastWall()) || "no".equals(mazeRepresentation.get(getPlayerPosition()).getEastWall())){
            //Change no of player's moves
            setNoOfMoves(1);
            //If this is the end field finish the game
            if("end".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject())
                    || "E".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject()))
                isFinished();
            if(!"no".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject())
                    && ((!"start".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject()))
                    ||!"S".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject())))
                //If not end field, check if field contains object. Add to possessions if object exists
                addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() + 1).getObject());
            // Change player position
            mazeRepresentation.get(getPlayerPosition()+ 1).setObject(getPlayerPosition() +1,"P", mazeRepresentation);
            mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
            //Set new position of a player
            setPlayerPosition(1);
            //Displaying maze with new position of a player
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));
        }
        else if ("breakable".equals(mazeRepresentation.get(getPlayerPosition()).getEastWall())){
            // Checking for hammer in possessions
            if (playerPossessions.contains("hammer")) {
                //Change no of player's moves
                setNoOfMoves(1);
                //Remove breakable wall
                mazeRepresentation.get(getPlayerPosition()).setEastWall(getPlayerPosition(), "no", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition() + 1).setWestWall(getPlayerPosition() + 1, "no", mazeRepresentation);;
                //If field on which player steps is the end field; the game ends.
                if ("end".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject())
                        || "E".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject()))
                    isFinished();
                //If not end field, check if field contains object. Add to possessions if object exists
                if (!"no".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject()))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() + 1).getObject());
                mazeRepresentation.get(getPlayerPosition() + 1).setObject(getPlayerPosition() + 1, "P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                //Set new position of a player
                setPlayerPosition(1);
                //Displaying maze with new position of a player
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            } else{
                System.out.println("You cannot walk through this wall. You have no hammer to break it");
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        else if("door".equals(mazeRepresentation.get(getPlayerPosition()).getEastWall())){
            //Check if player's possessions contain key
            if (playerPossessions.contains("key")) {
                //Change no of player's moves
                setNoOfMoves(1);
                if ("end".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject())
                        || "E".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject()) )
                    isFinished();
                if (!"no".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject())
                        && !"start".equals(mazeRepresentation.get(getPlayerPosition() + 1).getObject()))
                    addObjectToPossessions(mazeRepresentation.get(getPlayerPosition() + 1).getObject());
                mazeRepresentation.get(getPlayerPosition() +1).setObject(getPlayerPosition() + 1, "P", mazeRepresentation);
                mazeRepresentation.get(getPlayerPosition()).setObject(getPlayerPosition(),"no", mazeRepresentation);
                //Setting new position of a player
                setPlayerPosition(1);
                //Displaying maze with new position of a player
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));
            }
            //If player does not have a key, he/she cannot pass the door
            else{
                System.out.println("You cannot open this door without a key");
                List<String> maze = MazeArray.presentMaze(mazeRepresentation);
                for(int k = maze.size() - 1; k >= 0; k--)
                    System.out.println(maze.get(k));}
        }
        else{
            System.out.println("You cannot walk through the wall");
            List<String> maze = MazeArray.presentMaze(mazeRepresentation);
            for(int k = maze.size() - 1; k >= 0; k--)
                System.out.println(maze.get(k));}
    }

    /**
     * Getter of player's position
     * @return  initialPosition;
     */
    public static int getPlayerPosition(){
        return initialPosition;
    }

    /**
     * Sets position of a player to a start field
     * @param filePath
     *        Path to a maze file
     * @return  initialPosition
     * @throws IOException
     *         Throws an exception if file not found
     */
    public int setInitialPosition(String filePath) throws IOException{
        List<MazeArray> mazeRepresentation = MazeArray.createArray(filePath);
        for(MazeArray element: mazeRepresentation){
            if(element.getObject().equals("start") || element.getObject().equals("S"))
                initialPosition = mazeRepresentation.indexOf(element);
        }
        return initialPosition;
    }

    /**
     * Sets player position to a new position
     * @param newPosition
     *        New position of a player
     */
    public static void setPlayerPosition(int newPosition){
        initialPosition = getPlayerPosition() + newPosition;
    }

    /**
     * Gets number of moves made by the player
     * @return noOfMoves
     */
    public int getNoOfMoves(){return noOfMoves;}

    /**
     * Sets no of moves made by the player to a new value
     * @param move
     *        No of moves made by the player
     */
    public void setNoOfMoves(int move){
        noOfMoves += move;
    }

    /**
     * Gets a list of items held be the player
     * @return  playerPossessions
     */
    public String getPlayerPossessions(){
        return playerPossessions.toString();
    }

    /**
     * Adds object from a filed to player's possessions
     * @param object
     *        Object to be added to the list
     */
    public void addObjectToPossessions(String object){
        playerPossessions.add(object);
    }

    /**
     * Sets isFinished flag to true
     */
    public static void isFinished(){
        finishedGame = true;
    }

    /**
     * Gets player name
     * @return name
     *
     */
    public static String getPlayerName(){
        Scanner userName = new Scanner(System.in);
        //Asks for payer's name
        System.out.println("What is your name?");
        //Stores players name in a variable
        String Name = userName.nextLine();
        return Name;}
}