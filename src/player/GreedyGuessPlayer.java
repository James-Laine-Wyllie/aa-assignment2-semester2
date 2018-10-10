package player;

import java.util.Scanner;
import world.World;
import java.util.*;

/**
* Greedy guess player (task B).
* Please implement this class.
*
* @author Youhan Xia, Jeffrey Chan
*/

enum Mode {
  TARGETING,
  HUNTING
}

public class GreedyGuessPlayer implements Player{
  World world;
  int ships, numberOfShipsRemaing;

  // Store all potential coordinates of grid
  ArrayList<World.Coordinate> allCoordinates = new ArrayList<World.Coordinate>();
  // Stack of grid locations
  ArrayList<World.Coordinate> parityCoordinates = new ArrayList<World.Coordinate>();

  @Override
  public void initialisePlayer(World world) {
    // Keep track of the board and ships
    this.world = world;
    this.ships = world.shipLocations.size();
    this.numberOfShipsRemaing = this.ships;
    // iterate through all of the world grid coords and store parityCoordinates
    for(int row = 0; row < world.numRow; row++) {
      for(int column = 0; column < world.numColumn; column++) {
        World.Coordinate newCoordinate = world.new Coordinate();
        newCoordinate.row = row;
        newCoordinate.column = column;

        allCoordinates.add(newCoordinate);
        // on every odd row, use odd column, else use even col when on even row
        if(((row % 2 != 0) && (column % 2 != 0)) || ((row % 2 == 0) && (column % 2 == 0))) {
          parityCoordinates.add(newCoordinate);
        }
      }
    }
  } // end of initialisePlayer()

  @Override
  public Answer getAnswer(Guess guess) {
    // Create a new answer with hit = false, and sunk = null
    // Answer answer;
    // World.Coordinate shot = world.new Coordinate();
    // shot.row = guess.row;
    // shot.column = guess.column;
    // if the guess' row and column match a ship, flag it as hit
    // ArrayList<ShipLocation> shipLocs = this.world.shipLocations;

    // for(ShipLocation shipLoc : shipLocs) {
    //   if(shot.equals(shipLoc)) {
    //     answer.isHit = true;
    //     // if the hit was a killing blow, set this to the scuttled ship object
    //     if() {
    //       /* value of the sunk ship */
    //       answer.shipSunk = shipLoc.ship;
    //     }
    //   }
    // }
    // return the answer, if miss or hit, and whether or not the ship is sunk
    return null;
  } // end of getAnswer()

  @Override
  public Guess makeGuess() {

    return null;
  } // end of makeGuess()

  @Override
  public void update(Guess guess, Answer answer) {
    // update the board, draw a red x on board
    this.world.drawShot(guess);

    // Decrement ships remaining if answer contains sunk as a ship
    if(answer.shipSunk != null) {
      this.numberOfShipsRemaing--;
    }
  } // end of update()

  @Override
  public boolean noRemainingShips() {
    // number of ships == 0 => game over
    if(this.numberOfShipsRemaing == 0){
      return true;
    }
    return false;
  } // end of noRemainingShips()
} // end of class GreedyGuessPlayer
