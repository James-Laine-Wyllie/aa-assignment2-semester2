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

public class ProbabilisticGuessPlayer  implements Player{
  /*
   * Internal class to represent the liklihood of a coordinate containing a shipCount
   * cell, useful for making the next guess, or for stroing targettedSectors in
   * TARGETING mode
   */
  public class ShotProb {
    public int liklihood;
    public World.Coordinate shot;

    public ShotProb(int liklihood, World.Coordinate shot) {
      this.liklihood = liklihood;
      this.shot = shot;
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

    int aircraftCarrierConfigurations = 24;
    int cruiserConfigurations = 12;
    int frigateConfigurations = 8;
    int submarineConfigurations  = 8;
    int patrolCraftConfiguraions = 6;

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

        // probability is for each combination of a ship that can exist
        // in the coordinate , assign a value 1, total this to get the value
        // of the coordinate

        int pr = 0;

        // TODO calculate pr
        if(row == 0 || row == 9) {

            pr = (int) ((0.5) * (aircraftCarrierConfigurations + cruiserConfigurations + frigateConfigurations
                + submarineConfigurations + patrolCraftConfiguraions));

            if(column == 1 || column == 9) {
                pr = (int) ((.40) * pr);
            }

            if(column == 2 || column == 8) {
                pr = (int) ((.65) * pr);
            }

            if(column == 3 || column == 7) {
                pr = (int) ((.90) * pr);
            }
        }

        if(row == 1 || row == 8) {

            pr = (int) ((0.70) * (aircraftCarrierConfigurations + cruiserConfigurations + frigateConfigurations
                + submarineConfigurations + patrolCraftConfiguraions));

            if(column == 1 || column == 9) {
                pr = (int) ((.40) * pr);
            }

            if(column == 2 || column == 8) {
                pr = (int) ((.65) * pr);
            }

            if(column == 3 || column == 7) {
                pr = (int) ((.90) * pr);
            }
        }

        if(row == 3 || row == 7) {

            pr = (int) ((0.90) * (aircraftCarrierConfigurations + cruiserConfigurations + frigateConfigurations
                + submarineConfigurations + patrolCraftConfiguraions));

            if(column == 1 || column == 9) {
                pr = (int) ((.40) * pr);
            }

            if(column == 2 || column == 8) {
                pr = (int) ((.65) * pr);
            }

            if(column == 3 || column == 7) {
                pr = (int) ((.90) * pr);
            }
        }


        if(row == 4 || row == 5 || row == 6) {

            pr = (int) (aircraftCarrierConfigurations + cruiserConfigurations + frigateConfigurations
                + submarineConfigurations + patrolCraftConfiguraions);

            if(column == 1 || column == 9) {
                pr = (int) ((.40) * pr);
            }

            if(column == 2 || column == 8) {
                pr = (int) ((.65) * pr);
            }

            if(column == 3 || column == 7) {
                pr = (int) ((.90) * pr);
            }
        }

        ShotProb shot = new ShotProb(pr, newCoordinate);
        possibleShots.add(shot);

      }
  }

    System.out.println("Possible Shots");
    System.out.println("Size: " + possibleShots.size());
    for(int index = 0; index < possibleShots.size(); index++) {

        System.out.println("Shot: " + index);
        System.out.printf("Coordinates:\n\t Row: %d Column: %d", possibleShots.get(index).shot.row, possibleShots.get(index).shot.column);
        System.out.printf("probability: %d\n", possibleShots.get(index).liklihood);
        System.out.println("-------------------------\n");


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

    if(this.targetingMode == Mode.HUNTING) {
      // Do some target selection based off of highest pr
      // TODO make an 'edcated' guess as to which of the shots is most valid

      Iterator possibleShotsIterator = possibleShots.iterator();
      ShotProb currentSelected = (ShotProb) possibleShotsIterator.next();

      // interate through probablisitc, select highest one
      while(possibleShotsIterator.hasNext()) {

          ShotProb shotCheck = (ShotProb) possibleShotsIterator.next();

          if(shotCheck.liklihood > currentSelected.liklihood) {

              currentSelected = shotCheck;
          }
      }

      // our guess will be the coordinate of current selected shot
      newGuessCoordinate = currentSelected.shot;

    } else if(this.targetingMode == Mode.TARGETING){
      // We're in targetting mode and have knowledge of a ship's location
      // Therefore we choose a coord within range of 1 of the pervious shot
      // Base this off of the pr contained in targettedSectors

      // TODO make an 'edcated' guess as to which of the targettedSectors is most valid

      Iterator targettedSectorsIterator = targettedSectors.iterator();
      ShotProb currentSelectedSector = (ShotProb) targettedSectorsIterator.next();

      // interate through probablisitc, select highest one
      while(targettedSectorsIterator.hasNext()) {

          ShotProb sectorCheck = (ShotProb)targettedSectorsIterator.next();

          if(sectorCheck.liklihood > currentSelectedSector.liklihood) {

              currentSelectedSector = sectorCheck;
          }
      }

      newGuessCoordinate = currentSelectedSector.shot;

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
    this.shotsFired.add(shot);

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
        if(!this.shotsFired.contains(sector) && this.allCoordinates.contains(sector)) {
          // again, calculate the pr of the neighbour being a valid shot before adding
          int pr = 0;
          // TODO calc pr

          this.targettedSectors.add(new ShotProb(pr, sector));
        }
      }
    }

    if(answer.shipSunk != null) {
      // switch back to hunting mode only after a ship is sunk
      this.targetingMode = Mode.HUNTING;
      this.targettedSectors.clear();
      this.shipCount--;
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
