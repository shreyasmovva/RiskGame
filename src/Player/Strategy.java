package Player;
/**
 * The interface which defines attack, reinforcement
 * and fotify methods.
 * 
 * The classes will implement this interface.
 * 
 * @author mandeepahlawat
 * @version 1.0
 * @since 24-11-2018
 *
 */
public interface Strategy {
	/**
	 * Calculation of reinforcement armies,
	 * which will vary depending on the strategy.
	 * 
	 * @return int value of the reinforcement armies
	 * 
	 */
	public int calculateReinforcementArmies();
	/**
	 * Placement of reinforcement armies,
	 * which will vary depending on the strategy.
	 * 
	 * @param reinforcements - number of reinforcements
	 * to be place.
	 */
	public void placeReinforcements(int reinforcements);
	/**
	 * Fortification phase, whose implementation will
	 * vary depending on the strategy.
	 * 
	 */
	public void fortification();
	/**
	 * Attack phase, whose implementation will
	 * vary depending on the strategy.
	 */
	public void attack();
}
