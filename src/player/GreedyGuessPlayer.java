package player;

import java.util.Scanner;
import world.World;

/**
 * Greedy guess player (task B).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public enum Mode {

    TARGETING,
    HUNTING
}

public class GreedyGuessPlayer  implements Player{

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

        // keep track of which mode the player is in, defaut is TARGETING
        Mode whichMode = TARGETING;

        // want to keep track of all co-ordinates, parity co-ordinates, co-ordinates guessed
        // neighbour co-ordinate of a correct guess will be in allCoordinates
        // unless already guessed -- prevent repeat guesses

        // Store all potential coordinates of grid
        ArrayList<World.Coordinate> allCoordinates = new ArrayList<World.Coordinate>();
        // A subset of allCoordinates, taking every second element
        ArrayList<World.Coordinate> parityCoordinates = new ArrayList<World.Coordinate>();
        Stack<World.Coordinate> coordinatesRandomOrder = new Stack<World.Coordinate>();
        // Of a hit coordinate, the 4 potential neighbour coordinates N, E, S, W, max 4, min 0
        // clear when ship is sunk
        ArrayList<World.Coordinate> huntingCoordinates = new ArrayList<World.Coordinate>();

    @Override
    public void initialisePlayer(World world) {

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

        // number of Initial ships
        this.numberOfShipsRemaing = world.shipLocations.size();

        // storing inforamtion about the board
        this.numRow = world.numRow;
        this.numColumn = world.numColumn;
        this.gridSize = this.numRow * this.numColumn;

        for(int row = 0; row < numRow; row++) {

            for(int column = 0; column < numColumn; column++) {
                // !!!!!!!!!! ---- ISSUE: how to create a co-ordinate outside world class ---->

                // this works, not sure why
                World.Coordinate newCoordinate = world.new Coordinate();

                newCoordinate.row = row;
                newCoordinate.column = column;

                allCoordinates.add(newCoordinate);
            }
        }



        for(int index = 0; index < allCoordinates.size(); index++) {

            System.out.println("allCoordinates: element: " + index);
            System.out.println(allCoordinates.get(index).toString());
            System.out.println("Type of: " + allCoordinates.get(index).getClass().getName() + "\n");
        }

        // need to popuate parity, every second element from all co-ordinates

        Iterator allCoordinatesIterator = allCoordinates.iterator();

        // add the random order arraylist to the stack to random guess

        while(allCoordinatesIterator.hasNext()) {

            World.Coordinate cooordinateToAdd = (World.Coordinate) allCoordinatesIterator.next();
            this.parityCoordinates.add(cooordinateToAdd);

            if(allCoordinatesIterator.hasNext())) {

                allCoordinatesIterator.next();
            }
        }

        // randomise
        Collections.shuffle(parityCoordinates);
        Iterator parityCoordinatesIterator = parityCoordinates.iterator();

        while(parityCoordinatesIterator.hasNext()) {

            World.Coordinate parityCooordinateToAdd = (World.Coordinate) parityCoordinatesIterator.next();
            this.coordinatesRandomOrder.push(parityCooordinateToAdd);
        }

        System.out.println("\nEnd of initialisePlayer()\n");

        // have allCoordinates, parityCoordinates, and random order of parityCoordinates

    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.

        // dummy return
        return null;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // depends which mode it is in --> which co-ordinates it picks from

        Guess guess = new Guess();

        // if mode == TARGETING (will be on first guess)
        // take from parity
        // else take from huntingCoordinates

        if(this.whichMode == TARGETING) {




            // explicit else if for clarity / not required
        } else if(this.whichMode == HUNTING) {

        }
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

} // end of class GreedyGuessPlayer
