package com.company;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @author Alicja Ochman[r0693935]
 */
public class Maze_experiment {

    public static void main(String[] args) throws IOException {
        //filePath - path to the .txt file containing the maze
        String FILEPATH = "MidiMaze.txt";
        MazeArray array = new MazeArray();
        Player playGame = new Player();
        //Printing of the rules of the game
        System.out.println("WELCOME TO THE GREAT MAZE GAME!");
        System.out.println();
        System.out.println("At the start of the game you are standing on the field with letter S(Start field)!");
        System.out.println("Rules:");
        System.out.println("1.You can move one field at a time: up, down, right or left.");
        System.out.println("2.Remember to make as few steps as possible.");
        System.out.println("3. There are fake walls in the maze- if you try you can pass through them.");
        System.out.println("4. You can grab items, such as keys or hammers that will help you to get through the maze faster...");
        System.out.println("... You can use them many times.");
        System.out.println("5. Items mentioned above are not shown on the map. You have to find them on your own.");
        System.out.println();
        System.out.println("Signs used to mark the walls: normal wall:'|' or '---'; breakable wall: 'b' or '-b-'; door:'-d-' or 'd'. ");
        System.out.println();

        System.out.println("This is your maze:");
        System.out.println();

        //Create list of square objects of the maze
        List<MazeArray> maze = array.createArray(FILEPATH);
        //Create maze representation
        List<String> mazeRepresentation = array.presentMaze(maze);
        //Display initial layout of the maze to the player
        for(int k = mazeRepresentation.size() - 1; k >= 0; k--)
            System.out.println(mazeRepresentation.get(k));

        //Asking the player if he/she wants to start the game.
        Scanner userInput = new Scanner(System.in);
        System.out.println("Are you ready? Enter yes or no");
        //Getting player's input
        String start= userInput.nextLine();
        if(start.equals("yes") || start.equals("Yes") || start.equals("YES"))
            //Starting the game
            playGame.playGame(FILEPATH);
        else
            System.out.println("That's a pity you don't want to play.");
    }
}