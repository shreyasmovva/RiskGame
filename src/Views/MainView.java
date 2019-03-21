package Views;

import java.util.ArrayList;
import java.util.Scanner;

import Driver.Main;
import Map.Map;

/**
 *
 *This class is used to get user input 
 *for different method used in it. 
 *
 *@author shreyas
 *@version 1.0
 *
 *
 */
public class MainView {
	
public Scanner keyboard;
	/**
	 * This method will have a string value
	 * which is a file name.
	 * 
	 * @return expecting to enter file name.
	 */
	public String fileNameToSaveView() {
		System.out.println("Enter the file name");
		return keyboard.nextLine();
	}
	
	/**
	 * This method will display the map whether
	 * to edit or load file.
	 * 
	 * @return expecting a input from the player.
	 */
	public String fileNameToEditOrLoadView() {
		System.out.println("Enter absolute path of the Map file to edit or load.");
		return keyboard.nextLine();
	}
	
	/**
	 * This method will ask whether player wants 
	 * to edit a continent or not
	 * 
	 * @return edit the continent.
	 */
	public boolean wantToEditContinentView() {
		System.out.println("Do you want to edit a continent? Yes or No?");
		return keyboard.nextLine().equalsIgnoreCase("Yes");
	}
	
	/**
	 * This method will ask the player to enter line 
	 * number to edit in game.
	 * 
	 * @return waits for the input.
	 */
	public int lineNumberToEditView() {
		System.out.println("Enter the correct line number to edit");
		return Integer.parseInt(keyboard.nextLine());
	}
	
	/**
	 * This method will display the line to be 
	 * replaced
	 * 
	 * @param editContinent a boolean value.
	 * 
	 * @return wait's for the input value.
	 */
	public String newLineToReplaceWithView(boolean editContinent) {
		System.out.println("Enter the line you want to replace it with");
	    
	    if(editContinent) {
	    	System.out.println("Format of the line to edit a continent should be:\n"
	    			+ "Continent_Name=Continent_Score");
	    }
	    else {
	    	System.out.println("Format of the line to edit a Territory should be:\n"
	    			+ "Territory_Name, X-cord, Y-cord, Continent_Name, Adjacent Territories");
	    }
	    
	    String newLineText = keyboard.nextLine();
	    
	    while(!Main.validateMapLine(editContinent, newLineText)) {
	    	System.out.println("Please enter a valid format as described earlier to edit the line");
	    	newLineText = keyboard.nextLine();
	    }
	    
	    return newLineText;
	}
	
	/**
	 * This method will provide a option to
	 * edit more lines yes or no in it.
	 * 
	 * @return the edit more lines.
	 */
	public boolean wantToEditMorelinesView() {
		System.out.println("Want to edit more lines? \nEnter Yes to edit more lines");
		return keyboard.nextLine().equalsIgnoreCase("Yes");
	}
	
	/**
	 * This method will ask whether to edit 
	 * current map yes or no in it.
	 * 
	 * @return the edit the map.
	 */
	public boolean wantToEditMapView() {
		System.out.println("\nDo you want to edit this map? Answer in Yes or No.");
		return keyboard.nextLine().equalsIgnoreCase("Yes");
	}
	
	/**
	* This method is used to add new Continents to the Map
	* 
	* @return A string output containing lines of new Continents
	*/
	public String addContinentView() {
		System.out.println("Enter a list of continents (Enter 'exit' to stop adding continents)");
	    System.out.println("Format of the line to add continents should be:\n"
	    			+ "Continent Name=Continent Score");
	    
	    String inputFileText = "";
    	String inputLine;
	    
	    while(keyboard.hasNextLine()) {
	    	inputLine = keyboard.nextLine();
	    	if(inputLine.equals("exit")) {
	    		break;
	    	}
	    	while(!Main.validateMapLine(true, inputLine)) {
	    		System.out.println("Enter a correct format for adding a Continent");
	    		inputLine = keyboard.nextLine();
	    	}
	    	Main.userEnteredContinentLines.add(inputLine);
	    	inputFileText += inputLine + "\n";
	    }
	    return inputFileText;
	}
	
	/**
	* This method is used to add new Territory to the Map
	* 
	* @return A string output containing lines of new Territories
	*/
	public String addTerritoryView() {		
		System.out.println("Enter a list of Territories (Enter 'exit' to stop adding territories)");
		System.out.println("Format of the line to add a Territory should be:\n"
    			+ "Territory Name, X-cord, Y-cord, Continent Name, Adjacent Territories");;

    	String inputFileText = "";
    	String inputLine;
    	
	    while(keyboard.hasNextLine()) {
	    	inputLine = keyboard.nextLine();
	    	if(inputLine.equals("exit")) {
	    		break;
	    	}
	    	while(!Main.validateMapLine(false, inputLine)) {
	    		System.out.println("Enter a correct format for adding a Territory");
	    		inputLine = keyboard.nextLine();
	    	}
	    	Main.userEnteredTerritoryLines.add(inputLine);
	    	inputFileText += inputLine + "\n"; 
	    }
	    return inputFileText;
	}
	/**
	 * This method will provide two option to place
	 * armies manually or automatically
	 * 
	 * @return manual assigning of armies.
	 */
	public boolean wantToPlaceArmiesManually() {
		System.out.println("Write 'm' to place armies manually or 'a' to place armies automatically");
		return keyboard.nextLine().equalsIgnoreCase("m");
	}
	/**
	 * This is the default constructor of MainView
	 * which takes input value.
	 */
	public MainView() {
		this.keyboard = new Scanner(System.in);
	}
	
	/**
	 * This method will display three options to select
	 * 1.create a new map,2.to edit a map,3.load a previous map.
	 * 
	 * @return waits for the input.
	 */
	public int mapSelectionView() {
		System.out.println("Select one of the following options: \n"
				+ "1. Create a new Map. \n"
				+ "2. Edit a Map. \n"
				+ "3. Load a previous Map.");
		return Integer.parseInt(keyboard.nextLine().trim());
	}
	
	/**
	 * This method contains the startup phase of
	 * the game which includes map selection,
	 * building a map, if the map is valid then
	 * assigning countries to players randomly and
	 * letting the user place armies in the assigned countries
	 * 
	 */
	public void startupPhaseView() {	
		System.out.println("\n***********************STARTUP PHASE BEGINS*****************************\n");
		Main.startUp();
		System.out.println("\n***********************STARTUP PHASE ENDS*****************************\n");
	}
	
	/**
	 * this method will display total number of player's
	 * and also size of the territories
	 * 
	 * @return the number of count of players.
	 */
	public int playerCountView() {
		int playersCount;
		do {
			System.out.println("\nEnter the number of players"
					+ "\n(Note: the value should less than " + Map.listOfAllTerritories.size() + " i.e. the number of territories");
			playersCount = Integer.parseInt(keyboard.nextLine());
		} while(playersCount >= Main.activeMap.territories.size());
		return playersCount;
	}
	
	/**
	 * this method will display ask user to select 
	 * one of the strategies for the player
	 * 
	 * @return the number of count of players.
	 */
	public String playerStrategyView() {
		String playerStrategy = null;
		ArrayList<String> validStrategies = new ArrayList<String>();
		if(!Main.gameMode.equalsIgnoreCase("tournament mode")) {
			validStrategies.add("Human");
			validStrategies.add("human");
		}
		validStrategies.add("Aggressive");
		validStrategies.add("Benevolent");
		validStrategies.add("Random");
		validStrategies.add("Cheater");
		validStrategies.add("aggressive");
		validStrategies.add("benevolent");
		validStrategies.add("random");
		validStrategies.add("cheater");
		while(playerStrategy == null || !validStrategies.contains(playerStrategy)) {
			if(!Main.gameMode.equalsIgnoreCase("tournament mode")) {
				System.out.println("\nEnter the strategy for this player."
						+ "\n(Note: the valid options are: Human, Aggressive, Benevolent"
						+ ", Random, Cheater)");
			}
			else {
				System.out.println("\nEnter the strategy for this player."
						+ "\n(Note: the valid options are: Aggressive, Benevolent"
						+ ", Random, Cheater)");
			}
			playerStrategy = keyboard.nextLine();
		}
		return playerStrategy;
	}

	/**
	 * This method will display the mode of the game
	 * which a player wants to play.
	 * 
	 * @return string value as gamemode
	 */
	public String getGameModeView() {
		String gameMode = null;
		ArrayList<String> validGameModes = new ArrayList<String>();
		validGameModes.add("Single mode");
		validGameModes.add("tournament mode");
		validGameModes.add("Single mode".toLowerCase());
		validGameModes.add("Tournament mode".toLowerCase());
		while(gameMode == null || !validGameModes.contains(gameMode)) {
			System.out.println("\nEnter the game mode you want to play"
					+ "\n(Note: the valid options are: Single mode and Tournament mode)");
			gameMode = keyboard.nextLine();
		}
		return gameMode;
	}
	
	/**
	 * 
	 * This method will display the number
	 * of maps in the game.
	 *  
	 * @return integer value of the number of maps 
	 * in the game.
	 *
	 */
	public int chooseNumberOfMapsView() {
		int numberOfMaps = 0;
		while(numberOfMaps <= 0 || numberOfMaps > 5) {
			System.out.println("\nEnter the number of Maps"
					+ "\n(Note: the valid value is 1 to 5)");
			numberOfMaps = Integer.parseInt(keyboard.nextLine());
		}
		return numberOfMaps;
	}
	
	/**
	 *This method will print the number of games 
	 *for the particular map in the game under
	 *the given conditions.
	 * 
	 * @return The number of games
	 */
	public int chooseNumberOfGamesView(int index) {
		int numberOfGames = 0;
		while(numberOfGames <= 0 || numberOfGames > 5) {
			System.out.println("\nEnter the number of games for Map - " + index
					+ "\n(Note: the valid value is 1 to 5)");
			numberOfGames = Integer.parseInt(keyboard.nextLine());
		}
		return numberOfGames;
	}
	/**
	 * 
	 * This method will print the number of turns 
	 * under given condition in the game
	 * 
	 * @return the number of turns in the game
	 * 
	 */
	public int chooseNumberOfTurnsForEachGameView(int mapIndex, int gameIndex) {
		int numberOfTurns = 0;
		while(numberOfTurns < 10 || numberOfTurns > 50) {
			System.out.println("\nEnter the number of turns for Map - " + mapIndex + " and game - " + gameIndex
					+ "\n(Note: the valid value is 10 to 50)");
			numberOfTurns = Integer.parseInt(keyboard.nextLine());
		}
		return numberOfTurns;
	}
	/**
	 * 
	 * This method will ask the player to save 
	 * the game or not
	 * 
	 * @return the option which matches the word yes.
	 *
	 */
	public boolean wantToSaveGame() {
		System.out.println("\nDo you want to save this game? Enter yes or no");
		return keyboard.nextLine().equalsIgnoreCase("yes");
	}
	/**
	 *
	 * This method will load data from saved previous one.
	 * 
	 * @return the option which matches the word yes. 
	 *
	 */
	public boolean loadFromSaveData() {
		System.out.println("\nDo you want to load game from previous Data? Enter yes or no");
		return keyboard.nextLine().equalsIgnoreCase("yes");
	}
	/**
	 * This method will get the tournment maps paths view 
	 * 
	 * @param index a integer vaule
	 * 
	 * @return Asks for user's input
	 *
	 */
	public String getTournamentMapPathsView(int index) {
		System.out.println("Enter the map file path for map - " + index);
		return keyboard.nextLine();
	}
}
