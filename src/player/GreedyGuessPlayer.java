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
  Mode targetingMode;

  // Store all potential coordinates of grid
  ArrayList<World.Coordinate> allCoordinates = new ArrayList<World.Coordinate>();
  // Stack of grid locations
  Stack<World.Coordinate> parityCoordinates = new Stack<World.Coordinate>();
  // The stack to be used for targetting if we're in targetting mode
  Stack<World.Coordinate> targettedSectors = new Stack<World.Coordinate>();

  @Override
  public void initialisePlayer(World world) {
    // Keep track of the board and ships
    this.targetingMode = Mode.TARGETING;
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
          parityCoordinates.push(newCoordinate);
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

    // enter TARGETING mode if we get a hit against the other player's ship

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
    Guess newGuess = new Guess();
    World.Coordinate newGuessCoordinate = world.new Coordinate();

    // Before we have secured a hit, target based off of our parityCoordinates
    if(this.targetingMode == Mode.TARGETING) {
      // Only attempt to pop from the stack if not empty
      if(!this.parityCoordinates.empty()) {
        newGuessCoordinate = this.parityCoordinates.pop();
        System.out.println(newGuessCoordinate);
      }
    } else if(this.targetingMode == Mode.HUNTING){
      // We're in targetting mode and have knowledge of a ship's location
      // Therefore we choose a coord within range of 1 of the pervious shot
      // otherwise known as popping the next targettedSectors element

      // targettedSectors will be emptied if we successfully hit on a consequent shot
      // and it will be repopulated with the neighbours of that subsequent hit
      if(!this.targettedSectors.empty()) {
        // Try to eliminate these sectors first, ie guess here first
        newGuessCoordinate = this.targettedSectors.pop();
        System.out.println(newGuessCoordinate);
      }
    }

    newGuess.row = newGuessCoordinate.row;
    newGuess.column = newGuessCoordinate.column;

    return newGuess;
  } // end of makeGuess()

  @Override
  public void update(Guess guess, Answer answer) {
    // update the board, draw a red x on board
    this.world.drawShot(guess);

    // Decrement ships remaining if answer contains sunk as a ship
    // if(answer.shipSunk != null) {
    //   this.numberOfShipsRemaing--;
    // }
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
