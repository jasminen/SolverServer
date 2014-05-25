package model.minimaxAB.dataobjects;

import common.Keys;

/**
 * Direction enum.
 * 
 * @author Tzelon Machluf and Jasmine Nouriel based on Vasilis Vryniotis <bbriniotis at datumbox.com> minimax algorithm
 */
public enum Direction {
    /**
     * Move Up
     */
    UP(Keys.UP,"Up"), 
    
    /**
     * Move Right
     */
    RIGHT(Keys.RIGHT,"Right"), 
    
    /**
     * Move Down
     */
    DOWN(Keys.DOWN,"Down"), 
    
    /**
     * Move Left
     */
    LEFT(Keys.LEFT,"Left");
    
    
    /**
     * The numeric code of the status
     */
    private final int code;
    
    /**
     * The description of the status
     */
    private final String description;
    
    /**
     * Constructor
     * 
     * @param code
     * @param description 
     */
    private Direction(final int code, final String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * Getter for code.
     * 
     * @return 
     */
    public int getCode() {
        return code;
    }
 
    /**
     * Getter for description.
     * 
     * @return 
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Overloads the toString and returns the description of the move.
     * @return 
     */
    @Override
    public String toString() { 
        return description;
    }
}
