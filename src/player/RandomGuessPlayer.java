package player;

import java.util.Scanner;
import world.World;
import java.util.*;

/**
 * Random guess player (task A).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class RandomGuessPlayer implements Player{

    // attributes to keep track of ships, co-ordinates, and remaining ships

    World world;

    // these can be accesed via the world object: this.world.numRow
    // convient varaible to keep track inside player

    // to keep track of guesses made on board add combination of rows x column
    // to a queue and pop
    // ensures guess only made once, and random

    int numRow;
    int numColumn;
    int gridSize;


    int numberOfShipsRemaing;

    // queue of grid locations

    // initialisePlayer will set up the player ships, locations, and board
    @Override
    public void initialisePlayer(World world) {
        // To be implemented.

        this.world = world;

        System.out.println("initialisePlayer Test");
        System.out.println("initialisePlayer: ");
        System.out.println("World Information: ");
        System.out.println("Number of ships: " + world.shipLocations.size());
        // ship in ship co-ordinates has an associated ship and coordinate

        for(int i = 0; i < world.shipLocations.size(); i++) {

            System.out.println();
            System.out.println("Ship information: ");
            System.out.println("Ship: " + world.shipLocations.get(i).ship);
            System.out.println("coordinates: " + world.shipLocations.get(i).coordinates.get(0));
            System.out.println("------------------------------");

        }


        System.out.println("Size of grid: ");
        System.out.println("rows: " + world.numRow);
        System.out.println("colums: " + world.numColumn);


        System.out.println("\nEnd of initialisePlayer()\n");

    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.

        // dummy return
        return null;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // To be implemented.

        // dummy return
        return null;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.

        // dummy return
        return true;
    } // end of noRemainingShips()

} // end of class RandomGuessPlayer
