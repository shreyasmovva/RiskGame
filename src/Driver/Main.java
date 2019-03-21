package Driver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import Card.Card;
import Card.Card.CardType;

import java.nio.file.Path;

import Map.Map;
import Map.Map.Territory;
import Player.Aggressive;
import Player.Benevolent;
import Player.Cheater;
import Player.Human;
import Player.Player;
import Player.Player.GamePhase;
import Player.RandomStrategy;
import Views.MainView;
import Views.PhaseView;
import Views.WorldDominationView;

/**
* This class is the main driver class which
* interacts with the user and drives the program
* according to users input 
* 
* @author Mandeep Ahlawat
* @version 1.0
* @since   2018-09-27 
*/
public class Main {
	/**
	 * object of map is created which is activeMap.
	 */
	public static Map activeMap;
	/**
	 * string of Arraylist userEnteredContinentLines.
	 */
	public static ArrayList<String> userEnteredContinentLines;
	/**
	 * string of Arraylist userEnteredTerritoryLines.
	 */
	public static ArrayList<String> userEnteredTerritoryLines;
	/**
	* list of cards in the game.
	*/
	public static ArrayList<Card> cards;
	 /**
	* string of Arraylist userEnteredPlayerLines.
	*/
	public static ArrayList<String> userEnteredPlayerLines;
	/**
	* string of Arraylist userEnteredCardLines.
	*/
	public static ArrayList<String> userEnteredCardLines;
	/**
	 * list of players in the game.
	 */
	public static ArrayList<Player> players;
	/**
	 * Integer value type which as total number of armies intialy assigned
	 */
	public static int totalInitialArmies;
	/**
	 * object of Mainview created.
	 */
	public static MainView mainView;
	/**
	 * game mode being played
	 */
	public static String gameMode;
	/**
	 * maximum number of game turns in tournament mode
	 */
	public static int maxGameTurns;
	/**
	 * boolean value if game is finished or not
	 */
	public static boolean gameFinished;
	/**
	 * String value of map file path to load in case of tournament mode
	 */
	public static String mapFilePath;
	/**
	 * number of maps being used in the tournament mode
	 */
	public static int numberOfMaps;
	/**
	 * file paths of the maps in tournament mode
	 */
	public static ArrayList<String> mapFilePaths;
	/**
	 * number of game for each map
	 */
	public static ArrayList<Integer> numberOfGame;
	/**
	 * number of turns to be played before draw for each game
	 * in the map
	 */
	public static ArrayList<ArrayList<Integer>> numberOfTurns;
	/**
	 * tournament result containing player name or draw for each
	 * map and each game
	 */
	public static ArrayList<ArrayList<String>> tournamentResult;
	
	/**
	*
	* This method is used to validate the new map line
	* entered by the user.
	* 
	* @param isContinent A boolean value to denote if we
	* are editing a continent, if false then it means we are
	* editing a territory.
	* 
	* @param newLine A String entered by the user to replace the old
	* line in map.
	* 
	* @return True if new line is valid otherwise False.
	*
	*/
	public static boolean validateMapLine(boolean isContinent, String newLine) {
		if(isContinent) {
			String continentParts[] = newLine.split("=");
			if(continentParts.length != 2) {
				return false;
			}
			else {
				if(!continentParts[1].trim().matches("-?\\d+(\\.\\d+)?")) {
					return false;
				}
			}
		}
		else {
			String territoryParts[] = newLine.split(",");
			if(territoryParts.length < 5 ) {
				return false;
			}
			else {
				if(!territoryParts[1].trim().matches("-?\\d+(\\.\\d+)?") || !territoryParts[2].trim().matches("-?\\d+(\\.\\d+)?")) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	*
	* This method lets user to build
	* a new map and then displays the full map to the user
	* on the screen.
	* 
	*/
	public static void createNewMap() {
		String newMapText = "";
		newMapText += "[Map]\n"
				+ "image=world.bmp\n"
				+ "wrap=yes\n"
				+ "scroll=horizontal\n"
				+ "author=Your Name\n"
				+ "warn=yes";
		newMapText += "\n\n[Continents]\n" + mainView.addContinentView();
		newMapText += "\n\n[Territories]\n" + mainView.addTerritoryView();
		
		String fileName = mainView.fileNameToSaveView();
		
		try {  
            Writer w = new FileWriter(fileName);  
            w.write(newMapText);  
            w.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	/**
	*
	* This method replaces the content of a line number in a
	* file with the new content.
	* 
	* @param lineNumber The line number where we want to place the new content.
	* 
	* @param newData The new content which we want to replace the old data.
	* 
	* @param filePath The path of the file to be edited.
	* 
	* @throws java.io.IOException in some circumstance.
	*
	*/
	public static void setLineText(int lineNumber, String newData, String filePath) throws IOException {
	    Path path = Paths.get(filePath);
	    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	    lines.set(lineNumber - 1, newData);
	    Files.write(path, lines, StandardCharsets.UTF_8);
	}
	
	/**
	* 
	* This method lets user to edit an existing map based on the
	* line number selected and then displays the new map
	* 
	* @param filePath The path of the file to be edited, if the path is null
	* then user will be asked to input the file path.
	*
	*/
	public static void editMap(String filePath) {
		if(filePath == null) {
			filePath = mainView.fileNameToEditOrLoadView();
			mapFilePath = filePath;
		}
		
		try {
			
			int lineNumber = 1;
			int continentBegin = -1;
			int territoryBegin = -1;
			List<String> allLines = Files.readAllLines(Paths.get(filePath));
			for (String line : allLines) {
				System.out.println(lineNumber + ". " + line);
				if(line.equals("[Continents]")) {
					continentBegin = lineNumber;
				}
				if(line.equals("[Territories]")) {
					territoryBegin = lineNumber;
				}
				lineNumber++;
			}
		    			
			boolean editContinent = mainView.wantToEditContinentView();
			
		    int lineNumberToEdit;
		    while(true) {
			    lineNumberToEdit = mainView.lineNumberToEditView();
			    if(lineNumberToEdit < 1 || lineNumberToEdit >= lineNumber) {
			    	System.out.println("INVALID line number entered");
			    	continue;
			    }
			    else if(editContinent && (lineNumberToEdit <= continentBegin || lineNumberToEdit >= territoryBegin)) {
			    	System.out.println("INVALID continent line number entered");
			    	continue;
			    }	
			    else if(!editContinent && (lineNumberToEdit <= territoryBegin || lineNumberToEdit >= lineNumber)) {
			    	System.out.println("INVALID territory line number entered");
			    	continue;
			    }
			    else {
			    	break;
			    }
		    }
		    
		    String newLineText = mainView.newLineToReplaceWithView(editContinent);
		    
		    setLineText(lineNumberToEdit, newLineText, filePath);
		    allLines.set(lineNumberToEdit - 1, newLineText);
		    
		    while(mainView.wantToEditMorelinesView()) {
		    	editMap(filePath);
		    }
	    	populateUserEnteredContinentLines(allLines);
	    	populateUserEnteredTerritoryLines(allLines);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	* This method is used for populating the data
	* of userEnteredContinentLines array list.
	* 
	* @param allLines A list of strings which contains
	* all the lines inside map file.
	*/
	public static void populateUserEnteredContinentLines(List<String> allLines) {
		Integer continentLineStartIndex = null;
		Integer continentLineEndIndex = null;
		int lineCount = 0;
		for(String line : allLines) {
			if(line.matches("\\[Continents\\]")) {
				continentLineStartIndex = lineCount + 1;
			}
			if(line.matches("\\[Territories\\]")) {
				continentLineEndIndex = lineCount;
			}
			lineCount++;
		}
		
		if(continentLineStartIndex != null && continentLineEndIndex != null) {
			userEnteredContinentLines.addAll(allLines.subList(continentLineStartIndex, continentLineEndIndex));
		}
	}
	
	/**
	* This method is used for populating the data
	* of userEnteredTerritoryLines array list.
	* 
	* @param allLines A list of strings which contains
	* all the lines inside map file.
	*/
	public static void populateUserEnteredTerritoryLines(List<String> allLines) {
		Integer territoryLineStartIndex = null;
		Integer territoryLineEndIndex = allLines.size();
		int lineCount = 0;
		for(String line : allLines) {
			if(line.matches("\\[Territories\\]")) {
				territoryLineStartIndex = lineCount + 1;
			}
			if(line.matches("\\[Player\\]")) {
				territoryLineEndIndex = lineCount;
			}
			lineCount++;
		}

		if(territoryLineStartIndex != null) {
			userEnteredTerritoryLines.addAll(allLines.subList(territoryLineStartIndex, territoryLineEndIndex));
		}
	}
	
    /**
	* 
	* This method is used for populating the data
	* of userEnteredPlayerLines array list.
	* 
	* @param allLines A list of strings which contains
	* all the lines inside map file.
	*
	*/
	public static void populateUserEnteredPlayerLines(List<String> allLines) {
		Integer playerLineStartIndex = null;
		Integer playerLineEndIndex = null;
		int lineCount = 0;
		for(String line : allLines) {
			if(line.matches("\\[Player\\]")) {
				playerLineStartIndex = lineCount + 1;
			}
			if(line.matches("\\[Card\\]")) {
				playerLineEndIndex = lineCount;
			}
			lineCount++;
		}
		
		if(playerLineStartIndex != null && playerLineStartIndex != null) {
			userEnteredPlayerLines.addAll(allLines.subList(playerLineStartIndex, playerLineEndIndex));
		}
	}
	
	/**
	* 
	* This method is used for populating the data
	* of userEnteredCardLines array list.
	* 
	* @param allLines A list of strings which contains
	* all the lines inside map file.
	* 
	*/
	public static void populateUserEnteredCardLines(List<String> allLines) {
		Integer cardLineStartIndex = null;
		Integer cardLineEndIndex = allLines.size();
		int lineCount = 0;
		for(String line : allLines) {
			if(line.matches("\\[Card\\]")) {
				cardLineStartIndex = lineCount + 1;
			}
			lineCount++;
		}
		
		if(cardLineStartIndex != null) {
			userEnteredCardLines.addAll(allLines.subList(cardLineStartIndex, cardLineEndIndex));
		}
	}

	/**
	 * This method will save the date entered in the game.
	 * 
	 * 
	 * @param fileName holds the string value in it.
	 * 
	 * @param playerIndex 
	 *
	 */
	public static void saveGameData(String fileName, int playerIndex) {
		String gameData = null;

		gameData = "\n\n[Continents]"; 

		for(Map continent : Map.listOfAllContinents) {
			gameData += "\n" + continent.name + '=' + continent.score;
		}

		gameData += "\n\n[Territories]";
		for(Territory territory : Map.listOfAllTerritories) {
			gameData += "\n" + territory.name + ", 1, 2, " + territory.continent.name;
			for(Territory neighbour : territory.neighbours) {
				gameData += ", " + neighbour.name;
			}
		}

		gameData += "\n\n[Player]";
		// format = name, strategy, totalArmiesCount, assignedTerritories 
		for(Player player : Main.players) {
			gameData += "\n" + player.getName() + ", " + player.playerStrategy + ", " + player.totalArmiesCount;
			for(Territory territory : player.assignedTerritories) {
				gameData += ", " + territory.name + "(" + territory.numberOfArmies +")";
			}
		}

		gameData += "\n\n[Card]";
		// cardExchangeValue
		// format = type, territory, owner, previousOwners
		gameData += "\n" + Card.cardExchangeValue + "\n";
		for(Card card : Main.cards) {
			gameData += "\n" + card + ", " + card.territory.name + ", ";
			if(card.owner != null) {
				gameData += card.owner.getName();

				for(Player owner : card.previousOwners) {
					gameData += ", " + owner.getName();
				}
			}
		}

		try {  
            Writer w = new FileWriter(fileName);  
            w.write(gameData);  
            w.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	/**
	 * 
	 * This method will build the players 
	 * and also the cards in the game.
	 * 
	 */
	public static void buildPlayersAndCards() {
		userEnteredPlayerLines.removeAll(Arrays.asList("", null));
		userEnteredCardLines.removeAll(Arrays.asList("", null));

		for(String playerLine : userEnteredPlayerLines) {
			ArrayList<String> playerData = new ArrayList<String>(Arrays.asList(playerLine.split(",")));

			Player player = new Player(playerData.get(0).trim());

			switch(playerData.get(1).trim().toLowerCase()) {
				case "human":
					player.setPlayerStrategy(new Human(player));
					break;
				case "aggressive":
					player.setPlayerStrategy(new Aggressive(player));
					break;
				case "benevolent":
					player.setPlayerStrategy(new Benevolent(player));
					break;
				case "random":
					player.setPlayerStrategy(new RandomStrategy(player));
					break;
				case "cheater":
					player.setPlayerStrategy(new Cheater(player));
					break;
			}

			player.totalArmiesCount = Integer.parseInt(playerData.get(2).trim());

			for(String territoryNameAndArmy : playerData.subList(3, playerData.size())) {
				String territoryName = territoryNameAndArmy.split("\\(")[0];
				String armies = territoryNameAndArmy.split("\\(")[1].split("\\)")[0];

				Territory territory = Map.findTerritory(territoryName.trim());
				player.assignedTerritories.add(territory);
				territory.owner = player;
				territory.numberOfArmies = Integer.parseInt(armies);
			}

			PhaseView view = new PhaseView();
			player.addObserver(view);
			players.add(player);
		}
		
		// needs to clear as each territory generates a card automatically
		// remove all those cards and assign proper card to each territory
		Main.cards.clear();

		int i = 0;
		for(String cardLine : userEnteredCardLines) {
			if(i == 0) {
				Card.cardExchangeValue = Integer.parseInt(cardLine);
			}
			else {
				ArrayList<String> cardData = new ArrayList<String>(Arrays.asList(cardLine.split(",")));
				cardData.removeAll(Arrays.asList("", null, " "));

				Territory territory = Map.findTerritory(cardData.get(1).trim());
				Card card = new Card(territory, CardType.valueOf(cardData.get(0).trim()));

				if(cardData.size() >= 3) {
					Player player = Main.findPlayer(cardData.get(2));

					card.owner = player;
					player.cards.add(card);
					territory.card = card;
					for(String previousOwner : cardData.subList(3, cardData.size())) {
						Player previousPlayer = Main.findPlayer(previousOwner);
						card.previousOwners.add(previousPlayer);
					}
				}

			}
			++i;
		}
	}
/**
 * 
 * This method will find the particular player
 * in the game.
 * 
 * @param name holds the string value in it.
 *
 * @return players name if the conditions passed 
 * else it will return null. 
 *
 */
	public static Player findPlayer(String name) {
		for(Player player : players) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 *
	 *This method will build map from 
	 *saved data in the game.
	 * 
	 * @param filePath it will have file's path.
	 *
	 */
	public static void buildMapFromSaveData(String filePath) {
		try {
			List<String> allLines = Files.readAllLines(Paths.get(filePath));
			for (String line : allLines) {
				System.out.println(line);
			}
			populateUserEnteredContinentLines(allLines);
			populateUserEnteredTerritoryLines(allLines);
			populateUserEnteredPlayerLines(allLines);
			populateUserEnteredCardLines(allLines);
		}
		catch(Exception e) {
			System.out.println(e);
		}

		buildMap();
		activeMap.territories.addAll(Map.listOfAllTerritories);
		buildPlayersAndCards();
	}
	/**
	 *
	 *This method will check all the stages 
	 *It will only continue if all the 
	 *conditions are passed.
	 * 
	 */
	 public static void playGame() {
		 while(!activeMap.allTerritoriesOwnBySinglePlayer(true)) {
			 for(Player player : players) {
				 if(player.assignedTerritories.size() > 0) {
					 player.setCurrentGamePhase(GamePhase.REINFORCEMENT);
					 if(player.playerStrategy.toString().equalsIgnoreCase("human") && !Main.gameFinished) {
						 if(mainView.wantToSaveGame()) {
							 String fileName = mainView.fileNameToSaveView();
							 saveGameData(fileName, players.indexOf(player));
						 }
					 }
				 }
			 }
		 }
		 System.out.println("======== Game finished ========");
	 }
	 
	 
	/**
	* This method loads the Map and displays its content.
	* 
	*/
	public static void loadMap(){
		String filePath;
		if(mapFilePath == null) {
			filePath = mainView.fileNameToEditOrLoadView();
			mapFilePath = filePath;
		}
		else {
			filePath = mapFilePath;
		}
		
		try {
			List<String> allLines = Files.readAllLines(Paths.get(filePath));
			for (String line : allLines) {
				System.out.println(line);
			}
			populateUserEnteredContinentLines(allLines);
			populateUserEnteredTerritoryLines(allLines);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	* This method asks user about map selections. Depending on the
	* choice entered user can create a new map, edit an existing map
	* or load an existing map.
	* 
	*/
	public static void mapSelection() {
		int selectedOption = mainView.mapSelectionView();
		
		switch(selectedOption) {
			case 1:
				createNewMap();
				break;
			case 2:
				editMap(null);
				break;
			case 3:
				loadMap();
				if(mainView.wantToEditMapView()) {
					userEnteredContinentLines.clear();
					userEnteredTerritoryLines.clear();
					editMap(null);
				}
				break;
			default:
				System.out.println("Invalid Option.");
				mapSelection();
				break;
		}
		
	}
	
	/**
	 * This method checks if there are any
	 * players with no territories assigned
	 * @return null if all players has territories otherwise returns player
	 */
	public static Player playerWithNoTerritory() {
		for(Player player : players) {
			if(player.assignedTerritories.size() == 0) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * This method Assigns the initial territories randomly
	 * to the players in the game.
	 */
	public static void assignInitialTerritories() {
		for (Territory territory : Main.activeMap.territories) {
			if (territory.owner == null) {
				Player player = playerWithNoTerritory();
				if(player == null) {
					Random rand = new Random();
					player = Main.players.get(rand.nextInt(Main.players.size()));
				}
				territory.owner = player;
				player.assignedTerritories.add(territory);
			}
		}
	}
	
	/**
	 * This method defines Assigning the initial number of armies
	 *  to each player who gets in the game.
	 */
	public static void assignInitialArmies() {
		int playersSize = players.size();
		totalInitialArmies = 0;
		if (playersSize == 2) {
			totalInitialArmies = 80;
		} else if (playersSize == 3) {
			totalInitialArmies = 105;
		} else if (playersSize == 4) {
			totalInitialArmies = 120;
		} else if (playersSize == 5) {
			totalInitialArmies = 125;
		} else if (playersSize == 6) {
			totalInitialArmies = 120;
		}
		else {
			System.out.println("Number of players should be less than 6");
			System.exit(0);
		}
		
		for (int i = 0; i < playersSize; i++) {
			players.get(i).setInitialArmyCount(totalInitialArmies/playersSize);
		}
	}
	
		
	/**
	 * This method builds the map from the text lines
	 * which are read from the file selected to load
	 * the map.
	 */
	public static void buildMap() {
		userEnteredContinentLines.removeAll(Arrays.asList("", null));
		userEnteredTerritoryLines.removeAll(Arrays.asList("", null));
		
		for(String continentLines : userEnteredContinentLines) {
			String continentName = continentLines.split("=")[0].trim();
			int continentScore = Integer.parseInt(continentLines.split("=")[1].trim());
			
			if(!activeMap.addContinent(continentName, continentScore)) {
				System.out.println("Couldn't add continent because format is invalid"
						+ "as the continent name already exists");
				System.exit(0);
			}
			
		}
		
		for(String territoryLines : userEnteredTerritoryLines) {
			List<String> territoryLineArray = new ArrayList<String>(); 
			for(String territoryLine : territoryLines.split(",")) {
				territoryLineArray.add(territoryLine.trim());
			}
			
			Territory territory = Map.findTerritory(territoryLineArray.get(0));
			if(territory == null) {
				territory = new Territory(territoryLineArray.get(0));
			}
			territory.addNeighbours(String.join(",", territoryLineArray.subList(4, territoryLineArray.size())));
			
			if(!territory.assignContinent(territoryLineArray.get(3))) {
				System.out.println("Couldn't assign continent because format is invalid as"
						+ "the continent is already assigned to territory or doesn't exists.");
				System.exit(0);
			}
			
		}
	}
	
	/**
	 * This method is a part of startup phase and assign
	 * initial territories to players and gives them a set of
	 * initial armies and lets them place those armies in the
	 * assigned countries
	 */
	public static void assignTerritoriesAndArmies() {
		assignInitialTerritories();
		assignInitialArmies();
		int playersCount = players.size();
		
		if(Main.gameMode.equalsIgnoreCase("tournament mode")) {
			for(int i = 0; i < playersCount; ++i) {
				players.get(i).placeArmiesAutomatically();
			}
			return;
		}
		
		if (mainView.wantToPlaceArmiesManually()) {
			boolean armiesLeftToPlace = true;
			while(armiesLeftToPlace) {
				for(int i = 0; i < playersCount; ++i) {
					players.get(i).placeArmies();
				}
				
				int playersCountWithNoArmiesLeft = 0;
				
				for(int i = 0; i < playersCount; ++i) {
					if(players.get(i).armiesLeft == 0) {
						playersCountWithNoArmiesLeft++;
					}
				}
				
				if(playersCountWithNoArmiesLeft == playersCount) {
					armiesLeftToPlace = false;
				}
			}
		}
		else {
			for(int i = 0; i < playersCount; ++i) {
				players.get(i).placeArmiesAutomatically();
			}
		}
	}
	
	/**
	 * 
	 * Public method startUp will assign territories and 
	 * armies to the player's in the game  if it satisfies
	 * the condition only otherwise it will display invalid
	 * map and call's this method again.
	 * 
	 */
	public static void startUp() {
		if(mapFilePath == null) {
			mapSelection();
		}
		else {
			loadMap();
		}
		
		buildMap();
		activeMap.territories.addAll(Map.listOfAllTerritories);
		if(activeMap.validateMap(false)) {
			
			if((gameMode.equalsIgnoreCase("tournament mode") && Main.players.isEmpty()) 
					|| gameMode.equalsIgnoreCase("single mode")){
					
					int playersCount = mainView.playerCountView();		
					
					for(int i = 0; i < playersCount; ++i) {
						Player player = new Player("Player" + i);
						String stratgey = mainView.playerStrategyView();
						
						switch(stratgey.toLowerCase()) {
							case "human":
								player.setPlayerStrategy(new Human(player));
								break;
							case "aggressive":
								player.setPlayerStrategy(new Aggressive(player));
								break;
							case "benevolent":
								player.setPlayerStrategy(new Benevolent(player));
								break;
							case "random":
								player.setPlayerStrategy(new RandomStrategy(player));
								break;
							case "cheater":
								player.setPlayerStrategy(new Cheater(player));
								break;
						}
						
						PhaseView view = new PhaseView();
						player.addObserver(view);
						players.add(player);
					
				}
			}
			
			assignTerritoriesAndArmies();
		}
		else {
			System.out.println("INVALID MAP!");
			startUp();
		}
	}
	
	/**
	* This is the main method which runs the program
	* 
	* @param args command line arguments
	*/
	public static void main(String[] args) {
		userEnteredContinentLines = new ArrayList<String>();
		userEnteredTerritoryLines = new ArrayList<String>();
		userEnteredPlayerLines = new ArrayList<String>();
		userEnteredCardLines = new ArrayList<String>();
		players = new ArrayList<Player>();
		cards = new ArrayList<Card>();
		numberOfTurns = new ArrayList<ArrayList<Integer>>();
		tournamentResult = new ArrayList<ArrayList<String>>();
		mapFilePaths = new ArrayList<String>();
		numberOfGame = new ArrayList<Integer>();

		gameFinished = false;

		mainView = new MainView();

		if(mainView.loadFromSaveData()) {
			activeMap = new Map();
			WorldDominationView worldDominationView = new WorldDominationView();
			activeMap.addObserver(worldDominationView);

			String fileName = mainView.fileNameToSaveView();
			buildMapFromSaveData(fileName);
			playGame();

		}
		else {
			gameMode = mainView.getGameModeView();
			if(gameMode.equalsIgnoreCase("tournament mode")) {
				numberOfMaps = mainView.chooseNumberOfMapsView();
				for(int i = 0; i < numberOfMaps; ++i) {	
					Main.mapFilePaths.add(mainView.getTournamentMapPathsView(i+1));
					Main.numberOfGame.add(mainView.chooseNumberOfGamesView(i+1));
					
					ArrayList<Integer> turnCount = new ArrayList<Integer>();
					for(int j = 0; j < numberOfGame.get(i); ++j) {
						turnCount.add(mainView.chooseNumberOfTurnsForEachGameView(i+1, j+1));
					}
					Main.numberOfTurns.add(turnCount);
				}
				
				for(int i = 0; i < numberOfMaps; ++i) {	
					mapFilePath = Main.mapFilePaths.get(i);
					ArrayList<String> gameResult = new ArrayList<String>();
					
					for(int j = 0; j < numberOfGame.get(i); ++j) {
						userEnteredContinentLines.clear();
						userEnteredTerritoryLines.clear();
						totalInitialArmies = 0;
						
						activeMap = new Map();
						Map.listOfAllTerritories.clear();
						Map.listOfAllContinents.clear();

						WorldDominationView worldDominationView = new WorldDominationView();
						activeMap.addObserver(worldDominationView);
						
						int numberOfTurnsForThisGame = Main.numberOfTurns.get(i).get(j);
						
						Player.resetPlayersData();
						
						mainView.startupPhaseView();
						
						int currentTurnCount = 0;
						
						while(!activeMap.allTerritoriesOwnBySinglePlayer(true) && currentTurnCount < numberOfTurnsForThisGame) {
							currentTurnCount++;
							System.out.println("---------------------------------------------------------");
							System.out.println("----------Turn : " + currentTurnCount + " ---------------");
							System.out.println("---------------------------------------------------------");
							for(Player player : players) {
								if(player.assignedTerritories.size() > 0) {
									player.setCurrentGamePhase(GamePhase.REINFORCEMENT);
									if(gameFinished) {
										gameResult.add(player.getName());
										break;
									}
								}
							}
						}
						if(currentTurnCount >= numberOfTurnsForThisGame) {
							gameResult.add("Draw");
							System.out.println("Number of Turns exhausted");
						}
						System.out.println("======== Map - " + (i+1) + " Game - " + (j+1) + " is finished ========");
						gameFinished = false;
					}
					tournamentResult.add(gameResult);
				}
				
				System.out.println("====================*********** Tournament Results ****************=================================");
				for(int i = 0; i < tournamentResult.size(); ++i) {
					for(int j = 0; j < tournamentResult.get(i).size(); ++ j) {
						System.out.println("======== Map - " + (i+1) + " Game - " + (j+1) + " result - "+ tournamentResult.get(i).get(j));
					}
				}
				System.out.print("====================***********************************************=================================");
			}
			else {
				activeMap = new Map();
				WorldDominationView worldDominationView = new WorldDominationView();
				activeMap.addObserver(worldDominationView);

				mainView.startupPhaseView();
				playGame();
			}

		}

	}
}
