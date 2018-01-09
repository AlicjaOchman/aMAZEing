package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.Integer.valueOf;

/**
 * @author Alicja Ochman[r0693935]
 */
public class MazeArray {
    private int Xcoordinate;
    private int Ycoordinate;
    private String northWall;
    private String southWall;
    private String eastWall;
    private String westWall;
    private String Object;
    static int width;

    /**
     * Constructor defining Maze Objects consisting of the following parameters.
     * @param   Xcoordinate
     *          | Parameter describing row of a maze counting from 0.
     * @param   Ycoordinate
     *          | Parameter describing column of a maze counting from 0.
     * @param   northWall
     *          | Parameter describing north wall of the maze square.
     * @param   southWall
     *          | Parameter describing south wall of the maze square.
     * @param   eastWall
     *          | Parameter describing south wall of the maze square.
     * @param westWall
     *          | Parameter describing west wall of the maze square.
     * @param   Object
     *          | Parameter indicating  if the square of a maze is a Start, End or contains a special object.
     */
    public MazeArray(int Xcoordinate, int Ycoordinate, String northWall, String southWall, String eastWall, String westWall, String Object) {
        this.Xcoordinate = Xcoordinate;
        this.Ycoordinate = Ycoordinate;
        this.northWall = northWall;
        this.southWall = southWall;
        this.eastWall = eastWall;
        this.westWall = westWall;
        this.Object = Object;
    }

    public MazeArray(){}

    /**
     * @return  Array List Maze containing Array squares
     * @throws  IOException
     *          | Throws exception when file not found
     *
     */
    public static List<MazeArray> createArray(String filePath) throws IOException {
        //Read data from file, throw an exception if file doesn't exist.
        Scanner file = null;
        try {
            file = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Error" + e + "Please, make sure file exists.");
        }
        // List to collect mazeArray objects
        List<MazeArray> maze = new ArrayList<>();
        //Read file line by line
        String line;
        //Omit first line of a file.
        file.nextLine();

        while (file.hasNext()) {
            line = file.nextLine();
            // Split line to extract individual fields
            String[] data = line.split(",");
            // Create new mazeArray object, i.e. maze square
            MazeArray mazeSquare = new MazeArray(valueOf(data[0]), valueOf(data[1]), data[2], data[3], data[4], data[5], data[6]);
            // Add mazeArray object to maze list
            maze.add(mazeSquare);
        }
        return maze;
    }

    /**
     * Method creating list of all lines of the maze.
     * Assumption for this method is that outside walls are always real walls.
     * @return  List<String> mazeRepresentation
     *          List containing lines of String type for construction of the maze
     */
    public static List presentMaze(List<MazeArray> maze){
        //Variable presenting width of the array
        int width = 0;
        //North wall string
        String northWall = "";
        //South wall string
        String southWall = "";
        //Line string (Starts with "|" as outer west wall should always be real wall. One cannot go out from the maze.)
        String line = "|";
        // List array of lines of the maze.
        List<String> mazeRepresentation = new ArrayList<>();

        //Calculating the width of an array [foreach loop]
        for (MazeArray aMaze : maze) {
            if (aMaze.Xcoordinate == 0)
                width += 1;
        }
        //Using the width of the maze to make the lower bound of the maze.
        for (int i = 0; i < width; i++) {
            if ((maze.get(i).southWall).equals("wall"))
                southWall += "+---";
            else
                southWall += "+---";
        }
        //Adding last sign to southWall
        southWall += "+";
        mazeRepresentation.add(southWall);
        int j = 0;
        //Loop creating 2D console representation of maze from List of constructors
        for (int i = 0; i < maze.size(); i++) {
            if(maze.get(i).Xcoordinate == j) {
                if ((maze.get(i).northWall).equals("wall"))
                    northWall += "+---";
                else if((maze.get(i).northWall).equals("breakable"))
                    northWall += "+-b-";
                else if((maze.get(i).northWall).equals("door"))
                    northWall += "+-d-";
                else
                    northWall += "+   ";

                if ((maze.get(i).Object).equals("S") || (maze.get(i).Object).equals("start"))
                    line += " S ";
                else if ((maze.get(i).Object).equals("E") || (maze.get(i).Object).equals("end"))
                    line += " E ";
                else if ((maze.get(i).Object).equals("E") || (maze.get(i).Object).equals("P"))
                    line += " P ";
                else
                    line += "   ";
                if ((maze.get(i).eastWall.equals("wall")) || (maze.get(i).eastWall.equals("normal")))
                    line += "|";
                else if (maze.get(i).eastWall.equals("breakable"))
                    line += "b";
                else if((maze.get(i).eastWall).equals("door"))
                    line += "d";
                else if (maze.get(i).eastWall.equals("fake"))
                    line += "|";
                else
                    line += " ";
            }else{
                j+=1;
                i -= 1;
                northWall += "+";
                mazeRepresentation.add(line);
                mazeRepresentation.add(northWall);
                line = "|";
                northWall= "";
            }
        }
        northWall += "+";
        mazeRepresentation.add(line);
        mazeRepresentation.add(northWall);

        return mazeRepresentation;
    }

    /**
     * Getter for X coordinate of given square
     * @return  Xcoordinate
     */
    public int getXcoordinate(){
        return this.Xcoordinate;
    }

    /**
     * Getter for Y coordinate of given square
     * @return Ycoordinate
     */
    public int getYcoordinate(){
        return this.Ycoordinate;
    }

    /**
     * Getter for east wall of given square
     * @return  eastWall
     */
    public String getEastWall() {
        return this.eastWall;
    }

    /**
     * Sets eastWall to new value
     * @param element
     *        Index of maze field
     * @param newEastWall
     *        New east wall
     * @param mazeRepresentation
     *        Representation of the maze
     */
    public void setEastWall(int element, String newEastWall, List<MazeArray> mazeRepresentation){
        mazeRepresentation.get(element).eastWall = newEastWall;
    }
    /**
     * Getter for north wall of given square
     * @return  northWall
     */
    public String getNorthWall() {
        return this.northWall;
    }

    /**
     * Sets northWall to new value
     * @param element
     *        Index of maze field
     * @param newNorthWall
     *        New east wall
     * @param mazeRepresentation
     *        Representation of the maze
     */
    public void setNorthWall(int element, String newNorthWall, List<MazeArray> mazeRepresentation){
        mazeRepresentation.get(element).northWall = newNorthWall;
    }

    /**
     * Getter for object in a given square
     * @return  Object
     */
    public String getObject() {
        return this.Object;
    }

    /**
     * Getter for south wall of given square
     * @return  southWall
     */
    public String getSouthWall() {
        return this.southWall;
    }

    /**
     * Sets southWall to new value
     * @param element
     *        Index of maze field
     * @param newSouthWall
     *        New south wall
     * @param mazeRepresentation
     *        Representation of a maze
     */
    public void setSouthWall(int element, String newSouthWall, List<MazeArray> mazeRepresentation) {
        mazeRepresentation.get(element).southWall = newSouthWall;
    }

    /**
     * Getter for south wall of given square
     * @return  westWall
     */
    public String getWestWall() {
        return this.westWall;
    }

    /**
     * Sets westWall to new value
     * @param element
     *        Index of maze field
     * @param newWestWall
     *        New west wall
     * @param mazeRepresentation
     *        Representation of a maze
     */
    public void setWestWall(int element, String newWestWall, List<MazeArray> mazeRepresentation){
        mazeRepresentation.get(element).westWall = newWestWall;
    }
    /**
     * Setter of an object to different object
     * @param element
     *        Index of square, it's position in the file
     * @param newObject
     *        Object to be set as new in the square
     */
    public static void setObject(int element, String newObject, List<MazeArray> mazeRepresentation){
        mazeRepresentation.get(element).Object = newObject;
    }

    /**
     * Getter for the width of the maze
     * @return  width
     * @throws IOException
     *          Throws an exception when method createArray() does not find the file specified
     */
    public static int getWidth(String filePath) throws IOException{
        List<MazeArray> maze = createArray(filePath);
        for (MazeArray element: maze) {
            if (element.Xcoordinate == 0)
                width += 1;
        }
        return width;
    }
}