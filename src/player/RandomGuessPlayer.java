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

    int numRow;
    int numColumn;
    int gridSize;

    int numberOfShipsRemaing;

    // Store all potential coordinates of grid
    ArrayList<World.Coordinate> allCoordinates = new ArrayList<World.Coordinate>();
    // Stack of grid locations
    Stack<World.Coordinate> coordinatesRandomOrder = new Stack<World.Coordinate>();

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

        // number of Initial ships
        this.numberOfShipsRemaing = world.shipLocations.size();

        // storing inforamtion about the board
        this.numRow = world.numRow;
        this.numColumn = world.numColumn;
        this.gridSize = this.numRow * this.numColumn;

        // fill in the arraylist of potential coordinates

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

        // shuffle the array list to randomise
        Collections.shuffle(allCoordinates);

        Iterator allCoordinatesIterator = allCoordinates.iterator();

        // add the random order arraylist to the stack to random guess

        while(allCoordinatesIterator.hasNext()) {

            World.Coordinate cooordinateToAdd = (World.Coordinate) allCoordinatesIterator.next();
            this.coordinatesRandomOrder.push(cooordinateToAdd);
        }

            // now have a stack of all coordinates in a random order
            // use this to create guesses in a random order
            // use of stack prevents same guesses occuring

        System.out.println("\nEnd of initialisePlayer()\n");

    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.

        // construct an answer object

        Answer answer = new Answer();

        // guess object with coordinates
        // we know our ship coordinates
        // if guess coordinates match a ship coordinate -> hit else not hit

        // guess contains row and column not a co-ordinate object

        World.Coordinate coordinatesOfShot = world.new Coordinate();

        coordinatesOfShot.row = guess.row;
        coordinatesOfShot.column = guess.column;

        // check for existance;

        // word contains an arraylist of shipLocations
        // shiplocations is an object of:
        // -- ship
        // -- arraylist<Coordinate>

        for(World.ShipLocation shipLocation : this.world.shipLocations) {

            System.out.println("Ship Location: ");
            System.out.println("Ship: " + shipLocation.ship);
            System.out.println("Location: ");
            // Two ways to handle a hit:
            // Arraylist conatins method --> return hit
            // iterate through and match co-ordinates --> Benefit: get the actual coordinates of the hit

            // remove coordinates from the ship on hit, if empty after hit, ship is sunk
            // Coordinate class has built in isSame() to check equality

            for(World.Coordinate coordinatesOfShip : shipLocation.coordinates) {

                System.out.println(coordinatesOfShip.toString());

                // potential issue with equals method --------- check if if not working as expected

                if(coordinatesOfShot.equals(coordinatesOfShip)) {

                    // hit condition
                    answer.isHit = true;

                    // remove coordinates from the ship to keep track of if ship is sunk
                    // if all coordinates removed --> shipLocations.coordinates will be empty
                    shipLocation.coordinates.remove(coordinatesOfShip);
                }

            }

            if(shipLocation.coordinates.isEmpty()) {

                answer.shipSunk = shipLocation.ship;
            }

            System.out.println();

        }


        return answer;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {

        // Implement some check to ensure stack not empty


        // Have a stack of random coordinates
        // pop one, use object to Generate a guess object
        // return guess object

        World.Coordinate newGuessCoordinate = this.coordinatesRandomOrder.pop();

        Guess newGuess = new Guess();
        newGuess.row = newGuessCoordinate.row;
        newGuess.column = newGuessCoordinate.column;

        return newGuess;

    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.

        // update the board, draw a red x on board
        this.world.drawShot(guess);

        // update number of ships
        if(answer.shipSunk != null) {

            this.numberOfShipsRemaing--;
        }

        System.out.println(answer.toString());
        System.out.println("Number of ships remaining: " + this.numberOfShipsRemaing + "\n");
    } // end of update()


    @Override
    public boolean noRemainingShips() {

        // number of ships = 0, game over
        if(this.numberOfShipsRemaing == 0) {

            return false;
        }

        return true;
    } // end of noRemainingShips()

} // end of class RandomGuessPlayer
