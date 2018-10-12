package player;

import java.util.Scanner;
import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import world.World;

/**
* Probabilistic guess player (task C).
* Please implement this class.
*
* @author Youhan Xia, Jeffrey Chan
*/
enum Mode {
  TARGETING,
  HUNTING
}

public class ProbabilisticGuessPlayer  implements Player{
  /*
   * Internal class to represent the liklihood of a coordinate containing a shipCount
   * cell, useful for making the next guess, or for stroing targettedSectors in
   * TARGETING mode
   */
  public class ShotProb {
    public int liklihood;
    public World.Coordinate shot;

    public void ShotProb(int lik, World.Coordinate shot) {
      this.shot = shot;
      this.liklihood = lik;
    }
  }

  World world;
  int shipCount;
  Mode targetingMode;
  /* determine the nextShot from the next available element in a data struct
  * which contains both a Coordinate and the liklihood of  aship being there
  */
  ArrayList<ShotProb> possibleShots = new ArrayList<ShotProb>();
  // Store all potential coordinates of grid
  ArrayList<World.Coordinate> allCoordinates = new ArrayList<World.Coordinate>();
  ArrayList<World.Coordinate> shotsFired = new ArrayList<World.Coordinate>();
  // The stack to be used for targetting if we're in targetting mode
  ArrayList<ShotProb> targettedSectors = new ArrayList<ShotProb>();

  @Override
  public void initialisePlayer(World world) {
    this.targetingMode = Mode.HUNTING;
    this.world = world;
    this.shipCount = world.shipLocations.size();

    // iterate through all of the world grid coords and store parityCoordinates
    for(int row = 0; row < world.numRow; row++) {
      for(int column = 0; column < world.numColumn; column++) {
        World.Coordinate newCoordinate = world.new Coordinate();
        newCoordinate.row = row;
        newCoordinate.column = column;

        if(!this.allCoordinates.contains(newCoordinate)) {
          this.allCoordinates.add(newCoordinate);
        }

        // Update the liklihood for each coord and store it in possibleShots
        // Calc probability from the row/col
        ShotProb shot = new ShotProb(newCoordinate, 1);
        possibleShots.add(shot);
      }
    }
  } // end of initialisePlayer()

  @Override
  public Answer getAnswer(Guess guess) {
    // Create a new answer with hit = false, and sunk = null
    Answer answer = new Answer();

    // if the guess' row and column match a ship, flag it as hit
    ArrayList<World.ShipLocation> shipLocations = this.world.shipLocations;
    World.Coordinate shot = world.new Coordinate();
    shot.row = guess.row;
    shot.column = guess.column;

    Iterator locsIterator = shipLocations.iterator();
    // enter TARGETING mode if we get a hit against the other player's ship
    while(locsIterator.hasNext()) {
      World.ShipLocation shipLocation = (World.ShipLocation) locsIterator.next();

      Iterator coordsIterator = shipLocation.coordinates.iterator();
      while(coordsIterator.hasNext()) {
        World.Coordinate shipCoord = (World.Coordinate) coordsIterator.next();

        // hit condition
        if(shipCoord.equals(shot)) {
          answer.isHit = true;

          // remove coordinates from the ship to keep track of if ship is sunk
          // if all coordinates removed --> shipLocations.coordinates will be empty
          coordsIterator.remove();
        }
      }

      if(shipLocation.coordinates.isEmpty()) {
        answer.shipSunk = shipLocation.ship;
      }
    }
    // return the answer, if miss or hit, and whether or not the ship is sunk
    return answer;
  } // end of getAnswer()


  @Override
  public Guess makeGuess() {
    Guess newGuess = new Guess();
    World.Coordinate newGuessCoordinate = world.new Coordinate();

    // Before we have secured a hit, target based off of our parityCoordinates
    if(this.targetingMode == Mode.HUNTING) {
      // Only attempt to pop from the stack if not empty
      if(!this.parityCoordinates.empty()) {
        newGuessCoordinate = this.parityCoordinates.pop();
      }
    } else if(this.targetingMode == Mode.TARGETING){
      // We're in targetting mode and have knowledge of a ship's location
      // Therefore we choose a coord within range of 1 of the pervious shot
      // otherwise known as popping the next targettedSectors element

      // targettedSectors will be emptied if we successfully hit on a consequent shot
      // and it will be repopulated with the neighbours of that subsequent hit
      if(!this.targettedSectors.empty()) {
        // Try to eliminate these sectors first, ie guess here first
        newGuessCoordinate = this.targettedSectors.pop();
        parityCoordinates.remove(newGuessCoordinate);
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

    World.Coordinate shot = world.new Coordinate();
    shot.row = guess.row;
    shot.column = guess.column;

    // Keep track of our previous shots
    this.shots.add(shot);

    // if our shot just hit a ship then we should switch firing modes
    if(answer.isHit) {
      this.targetingMode = Mode.TARGETING;
      // Empty out the targetting sectors and then add the adjacent sectors
      // this.targettedSectors.clear();

      // Keep track of the neighbours of the shot, for calculating future shots
      ArrayList<World.Coordinate> neighbours = new ArrayList<World.Coordinate>();
      // Add each of the shot's neighbours
      World.Coordinate north = world.new Coordinate();
      World.Coordinate south = world.new Coordinate();
      World.Coordinate east = world.new Coordinate();
      World.Coordinate west = world.new Coordinate();

      north.row = shot.row + 1;
      north.column = shot.column;
      south.row = shot.row - 1;
      south.column = shot.column;
      east.row = shot.row;
      east.column = shot.column + 1;
      west.row = shot.row;
      west.column = shot.column - 1;

      neighbours.add(north);
      neighbours.add(south);
      neighbours.add(east);
      neighbours.add(west);

      for(World.Coordinate sector : neighbours) {
        // Add each of the adjacent sectors if they arent already fired upon
        if(!this.shots.contains(sector) && this.allCoordinates.contains(sector)) {
          this.targettedSectors.push(sector);
        }
      }
    }

    if(answer.shipSunk != null) {
      // switch back to hunting mode only after a ship is sunk
      this.targetingMode = Mode.HUNTING;
      this.targettedSectors.clear();
      this.numberOfShipsRemaing--;
    }
  } // end of update()


  @Override
  public boolean noRemainingShips() {
    if(this.shipCount == 0) {
      return true;
    }
    return false;
  } // end of noRemainingShips()

} // end of class ProbabilisticGuessPlayer
