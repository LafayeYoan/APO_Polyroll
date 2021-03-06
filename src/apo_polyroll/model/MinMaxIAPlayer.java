
package apo_polyroll.model;

import static apo_polyroll.model.Plateau.Jeton.BLACK;
import static apo_polyroll.model.Plateau.Jeton.WHITE;
import static apo_polyroll.model.Plateau.PLATEAU_SIZE;
import apo_polyroll.utils.Position;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Min Max IA
 * Play at the spot get by a min max method
 * @author Yoan LAFAYE DE MICHEAUX - Sacha LHOPITAL
 */
public class MinMaxIAPlayer extends IAPlayer {
    
    public final int MAX_VALUE = 1000;
    public final int MIN_VALUE = -1000;
    
    private int max_value;
    private int min_value;
    private int currentValue;
    
    public MinMaxIAPlayer() { 
        super("MinMax");
    }

    @Override
    public HashMap<Position, Integer> getPlayableSpots(Plateau othellier) {
        
        HashMap allPositions = new HashMap<Position, Integer>();
        
        for(int i = 0; i < PLATEAU_SIZE; i++) {
            for(int j = 0; j < PLATEAU_SIZE; j++) {
                
                //Si l'IA possède un pion sur cette case 
                if((othellier.getToken(i, j)).equals(getToken())) {
                    
                    max_value = MIN_VALUE;
                    currentValue = 0;
                    
                    allPositions.put(findBestPosition(othellier, i, j, 5), currentValue); 
                }
            }
        }
        
        return allPositions;
    }
    
    /***
     * Min Max function
     * @param othellier the current othellier
     * @param i 
     * @param j
     * @param profondeur the deapth of the minmax
     * @return The Best move for a selected position
     */
    private Position findBestPosition(Plateau othellier, int i, int j, int profondeur) {
        
        ArrayList<Position> allPositionsUnclean = new ArrayList<Position>();
        Plateau othellierSimulated = new Plateau(othellier);
        Position position = new Position(i, j);
        Position best_position = null; 
        
        allPositionsUnclean.add(getSpotIfExist(othellier, position, 0, -1));
        allPositionsUnclean.add(getSpotIfExist(othellier, position, 0, 1));
        allPositionsUnclean.add(getSpotIfExist(othellier, position, -1, 0));
        allPositionsUnclean.add(getSpotIfExist(othellier, position, 1, 0));
        allPositionsUnclean.add(getSpotIfExist(othellier, position, 1, -1));
        allPositionsUnclean.add(getSpotIfExist(othellier, position, -1, -1));
        allPositionsUnclean.add(getSpotIfExist(othellier, position, 1, 1));
        allPositionsUnclean.add(getSpotIfExist(othellier, position, -1, 1));
        
        for(Position pos : allPositionsUnclean) {
            if(pos == null) {
                //do nothing
            } else {
                
                //Première initialisation si le coup à jouer n'est pas très pertinant
                if(best_position == null) {
                    best_position = pos;
                }
                
                //Simulation du coup sur un autre plateau !
                othellierSimulated.addAndReverse(pos, WHITE);
                currentValue = Min(othellierSimulated, profondeur);
                if(currentValue > max_value) {
                    max_value = currentValue;
                    best_position = pos;
                }
                //suppression du coup précédent.
                othellierSimulated = new Plateau(othellier);
            }
        }
        
        return best_position;
    }
    
    /***
     * Return the "maximum child value" for a selected a simulation
     * @param othellierSimulated the othellier simulated
     * @param profondeur the deapth
     * @return The max value for the IA in this configuration 
     */
    private int Max(Plateau othellierSimulated, int profondeur) {
        
        if(profondeur == 0 || othellierSimulated.isFull()) {
            return eval(othellierSimulated);
        }
        
        max_value = MIN_VALUE;
        
        //on refait l'algo pour tous les coups "fils"
        for(int i = 0; i < PLATEAU_SIZE; i++) {
            for(int j = 0; j < PLATEAU_SIZE; j++) {
                if((othellierSimulated.getToken(i, j)).equals(getToken())) {
                    ArrayList<Position> allPositionsUnclean = new ArrayList<Position>();
                    Position position = new Position(i, j);
                    Plateau othellierSimulated2 = new Plateau(othellierSimulated);
                    
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 0, -1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 0, 1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, -1, 0));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 1, 0));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 1, -1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, -1, -1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 1, 1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, -1, 1));

                    for(Position pos : allPositionsUnclean) {
                        if(pos == null) {
                            //do nothing
                        } else {
                            othellierSimulated2.addAndReverse(pos, WHITE);
                            currentValue = Min(othellierSimulated2, profondeur - 1);
                            if(currentValue > max_value) {
                                max_value = currentValue;
                            }
                            othellierSimulated2 = new Plateau(othellierSimulated);
                        }
                    }
                    
                }
            }
        }
        return max_value;
    }

    /***
     * Return the "minimum child value" for a selected a simulation
     * @param othellierSimulated the othellier simulated
     * @param profondeur the deapth
     * @return The min value for the IA in this configuration 
     */
    private int Min(Plateau othellierSimulated, int profondeur) {
        
        if(profondeur == 0 || othellierSimulated.isFull()) {
            return eval(othellierSimulated);
        }
        
        min_value = MAX_VALUE;
        
        //on refait l'algo pour tous les coups "fils"
        for(int i = 0; i < PLATEAU_SIZE; i++) {
            for(int j = 0; j < PLATEAU_SIZE; j++) {
                if((othellierSimulated.getToken(i, j)).equals(getToken())) {
                    
                    ArrayList<Position> allPositionsUnclean = new ArrayList<Position>();
                    Position position = new Position(i, j);
                    Plateau othellierSimulated2 = new Plateau(othellierSimulated);
                    
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 0, -1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 0, 1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, -1, 0));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 1, 0));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 1, -1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, -1, -1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, 1, 1));
                    allPositionsUnclean.add(getSpotIfExist(othellierSimulated, position, -1, 1));

                    for(Position pos : allPositionsUnclean) {
                        if(pos == null) {
                            //do nothing
                        } else {
                            othellierSimulated2.addAndReverse(pos, WHITE);
                            currentValue = Max(othellierSimulated2, profondeur - 1);
                            if(currentValue < min_value) {
                                min_value = currentValue;
                            }
                            othellierSimulated2 = new Plateau(othellierSimulated);
                        }
                    }
                    
                }
            }
        }
        return min_value;
    }

    /***
     * Evaluate if it is a good choice for the IA for the configured othellier.
     * @param othellierSimulated
     * @return 
     */
    private int eval(Plateau othellierSimulated) {
        
        int iAScore = othellierSimulated.getNumberOfToken(WHITE);
        int playerScore = othellierSimulated.getNumberOfToken(BLACK);
        
        if(othellierSimulated.isFull()) {
            
            if(othellierSimulated.getWinner() == WHITE) {
                //IA win
                return MAX_VALUE - (iAScore - playerScore);
                
            } else if(othellierSimulated.getWinner() == BLACK){
                //Player win
                return MIN_VALUE + (playerScore - iAScore);
            } else {
                //equality
                return 0;
            }
        }
        
        return iAScore - playerScore;
    }
    

    
           
}