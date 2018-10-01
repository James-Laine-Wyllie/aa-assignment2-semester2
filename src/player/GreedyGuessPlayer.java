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
        PriorityQueue<World.Coordinate> huntingCoordinates = new PriorityQueue<World.Coordinate>();

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

        Answer answer = new Answer();

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
        // depends which mode it is in --> which co-ordinates it picks from

        Guess guess = new Guess();

        // if mode == TARGETING (will be on first guess)
        // take from parity
        // else take from huntingCoordinates

        if(this.whichMode == TARGETING) {

            World.Coordinate newGuessCoordinate = this.coordinatesRandomOrder.pop();

            newGuess.row = newGuessCoordinate.row;
            newGuess.column = newGuessCoordinate.column;
            return newGuess;

            // explicit else if for clarity / not required
        } else if(this.whichMode == HUNTING) {

            // select top PriorityQueue element

            World.Coordinate newGuessCoordinate = this.huntingCoordinates.poll();

            newGuess.row = newGuessCoordinate.row;
            newGuess.column = newGuessCoordinate.column;

            return newGuess;
        }

    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.

        // update will need to change the mode in this player if a hit
        // will also need to add neighbours to huntingCoordinates
        
    } // end of update()


    @Override
    public boolean noRemainingShips() {

        // number of ships = 0, game over
        if(this.numberOfShipsRemaing == 0) {

            return false;
        }

        return true;

    } // end of noRemainingShips()

} // end of class GreedyGuessPlayer
