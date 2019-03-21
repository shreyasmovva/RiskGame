package Map;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Driver.Main;
import Map.Map.*;
import Player.Player;

/**
 * This is the JUnit Test cases for Map class. this implements all
 * the test related to the units within this class.
 *
 * @author Arun
 * @version 1.0
 * @since   2018-10-01 
 */ 
public class Map_Test {

	public static final String FILE_NAME = "Files\\Canada.map";
	public static final String INFILE_NAME = "Files\\returnMap.map";

	private Territory actualTerritory;
	private Map map;
	private Map mapForContinent;
	/**
	 * The testBefore is setting up of predetermined values as part of the Junit testcase
	 * 
	 */
	@Before
	public void testBefore() {
		Map.listOfAllTerritories = new ArrayList<>();
		Map.listOfAllContinents = new ArrayList<>();
		actualTerritory =new Territory("Japan");
		map=new Map();
		map.addContinent("North America",5);
		mapForContinent=new Map("Africa", 13);
	}
	/**
	* This method is used to test the Map.findTerritory operation
	*
	* return the instance of the territory found 
	*/
	@Test
	public void testFindTerritory() { 
		//actualTerritory=Map.findTerritory("Japan"); //Territory constructor is already adding it
		assertEquals("Japan", actualTerritory.name);
	}
	/**
	* This method is used to test the Map.findTerritory operation
	*
	* return the null as its instance is not found 
	*/
	@Test
	public void testFindTerritoryNull() { 
		actualTerritory=Map.findTerritory("India");
		assertNull(actualTerritory);
	}
	/**
	* This method is used to test if all territories are owner by a single user
	* that signals end of the game. 
	* 
	* return True if all the territories are owned by a user
	* 
	* return False if not all the territories are owned by the user
	*/
	@Test
	public void testAllTerritoriesOwnBySinglePlayer() { 
		Map.listOfAllTerritories = new ArrayList<Territory>();
		Main.activeMap = new Map();
		Main.activeMap.territories = new ArrayList<Territory>();
		Main.players = new ArrayList<Player>(); 
		Player player = new Player("Player1");
		Main.players.add(player);
		Territory t1 = new Territory("Africa");
		t1.addNeighbours("India,Pakistan");// add neighbour and add player to owner of neighbour and finally create
		t1.neighbours.get(0).owner = player; 
		t1.neighbours.get(1).owner = player;
		t1.numberOfArmies = 1;
		t1.owner=player;
		Main.activeMap.territories.add(t1); 
		t1 = new Territory("Asia");
		t1.addNeighbours("Nepal,Bangal");// add neighbour and add player to owner of neighbour and finally create 
		t1.neighbours.get(0).owner = player; 
		t1.neighbours.get(1).owner = player;
		t1.numberOfArmies = 1;
		t1.owner=player;
		Main.activeMap.territories.add(t1);
		
		System.out.println("All for one "+Main.activeMap.allTerritoriesOwnBySinglePlayer(false));
		assertTrue(Main.activeMap.allTerritoriesOwnBySinglePlayer(false));
	}
	/**
	* This method is used to test if all territories are owner by a single user
	* that signals end of the game. 
	* 
	* return True if all the territories are owned by a user
	* 
	* return False if not all the territories are owned by the user
	*/
	@Test
	public void testAllTerritoriesNotOwnBySinglePlayer() { 
		Map.listOfAllTerritories = new ArrayList<Territory>();
		Main.activeMap = new Map();
		Main.activeMap.territories = new ArrayList<Territory>();
		Main.players = new ArrayList<Player>();
		// removed this becas its assigned in randim so giving only ione player and
		// seeing
		Player player = new Player("Player1");
		Main.players.add(player);
		Territory t1 = new Territory("Africa");
		t1.addNeighbours("India,Pakistan");// add neighbour and add player to owner of neighbour and finally create
		// assignedterritories list since player is assighes
		t1.neighbours.get(0).owner = player;
		//Player player2 = new Player("Player2");// assigning owner to neighbour
		t1.neighbours.get(1).owner = player;
		t1.numberOfArmies = 1;
		t1.owner=player;
		Main.activeMap.territories.add(t1);
		
		t1 = new Territory("Asia");
		t1.addNeighbours("Nepal,Bangal");// add neighbour and add player to owner of neighbour and finally create
		// assignedterritories list since player is assighes
		t1.neighbours.get(0).owner = player;
		Player player2 = new Player("Player2");// assigning owner to neighbour
		t1.neighbours.get(1).owner = player;
		t1.numberOfArmies = 1;
		t1.owner=player2;
		Main.activeMap.territories.add(t1);
		
		System.out.println("All for one "+Main.activeMap.allTerritoriesOwnBySinglePlayer(false));
		assertFalse(Main.activeMap.allTerritoriesOwnBySinglePlayer(false));
	}
	
	/**
	* This method is used to test the Map.findContinent operation
	*
	* return the instance of the Continent found 
	*/
	@Test
	public void testFindContinent() { 
		Map mapContinent=Map.findContinent("North America"); 
		assertEquals("North America", mapContinent.name);
	}
	/**
	* This method is used to test the Map.findContinent operation
	*
	* return null if instance of the Continent not found 
	*/
	@Test
	public void testFindContinentAsNull() {   
		assertNull(Map.findContinent("Asia"));
	}
	 
	/**
	* This is a Map constructor that accept the parm Continent name and the score and list it in the continent Arraylist to be connected
	* it tests if the returned Continent name equated the expected value
	* 
	* return the instance of newly created continent and score 
	*/
	@Test
	public void testMapStringIntNewContinentName() {
		assertEquals("Africa", mapForContinent.name);
	}

	/**
	* This is a Map constructor that accept the parm Continent name and the score and list it in the continent Arraylist to be connected
	* it tests if the returned Continent score equated the expected value
	* 
	* return the instance of newly created continent and score 
	*/
	@Test
	public void testMapStringIntNewContinentScore() {
		assertEquals(13, mapForContinent.score);
	}
	/**
	* This is to add the continent with its score. since we have set this value in the @before its not re initiated here again
	* it tests if the returned Continent name equated the expected value
	* 
	* return the instance of newly created continent and score 
	*/
	@Test
	public void testAddContinentName() {
		assertEquals("North America", map.continents.get(0).name);
	}
	/**
	* This is to add the continent with its score. since we have set this value in the @before its not re initiated here again
	* it tests if the returned Continent score equated the expected value
	* 
	* return the instance of newly created continent and score 
	*/
	@Test
	public void testAddContinentScore() {
		//map.addContinent("North America",5);
		assertEquals(5, map.continents.get(0).score);
	}
	
	/**
	* This method is used to test if Map is a valid one. 
	* 
	* return True if all the territories are owned by a user
	* 
	* return False if not all the territories are owned by the user
	*/
	@Test
	public void testValidateMapForValidMap() throws IOException {
		Main.userEnteredContinentLines = new ArrayList<>(); // need to initialize it here as we are initializing static variable inside the main method
		Main.userEnteredTerritoryLines = new ArrayList<>();
		String path = FILE_NAME;
		Path expPath = Paths.get(path);
		List<String> linesBeforeRunningMethod =Files.readAllLines(expPath, StandardCharsets.UTF_8);
		Main.populateUserEnteredContinentLines(linesBeforeRunningMethod); 
		Main.populateUserEnteredTerritoryLines(linesBeforeRunningMethod);
		 
		
		Main.userEnteredContinentLines.removeAll(Arrays.asList("", null));
		Main.userEnteredTerritoryLines.removeAll(Arrays.asList("", null));
		
		Main.activeMap = new Map();
		for(String continentLines : Main.userEnteredContinentLines) {
			String continentName = continentLines.split("=")[0].trim();
			int continentScore = Integer.parseInt(continentLines.split("=")[1].trim());
			
			if(!Main.activeMap.addContinent(continentName, continentScore)) {
				System.out.println("Couldn't add continent because format is invalid"
						+ "as the continent name already exists");
				System.exit(0);
			}
			
		}
		
		for(String territoryLines : Main.userEnteredTerritoryLines) {
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
		System.out.println(Main.activeMap.validateMap(false));
		assertTrue(Main.activeMap.validateMap(false));
	}
	 
	/**
	* This method is used to test if Map is a valid one. 
	* 
	* return True if all the territories are owned by a user
	* 
	* return False if not all the territories are owned by the user
	*/
	@Test
	public void testValidateMapForInValidMap() throws IOException {
		Main.userEnteredContinentLines = new ArrayList<>(); // need to initialize it here as we are initializing static variable inside the main method
		Main.userEnteredTerritoryLines = new ArrayList<>();
		String path = INFILE_NAME;
		Path expPath = Paths.get(path);
		List<String> linesBeforeRunningMethod =Files.readAllLines(expPath, StandardCharsets.UTF_8);
		Main.populateUserEnteredContinentLines(linesBeforeRunningMethod); 
		Main.populateUserEnteredTerritoryLines(linesBeforeRunningMethod);
		 
		
		Main.userEnteredContinentLines.removeAll(Arrays.asList("", null));
		Main.userEnteredTerritoryLines.removeAll(Arrays.asList("", null));
		
		Main.activeMap = new Map();
		for(String continentLines : Main.userEnteredContinentLines) {
			String continentName = continentLines.split("=")[0].trim();
			int continentScore = Integer.parseInt(continentLines.split("=")[1].trim());
			
			if(!Main.activeMap.addContinent(continentName, continentScore)) {
				System.out.println("Couldn't add continent because format is invalid"
						+ "as the continent name already exists");
				System.exit(0);
			}
			
		}
		
		for(String territoryLines : Main.userEnteredTerritoryLines) {
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
		System.out.println(Main.activeMap.validateMap(false));
		assertFalse(Main.activeMap.validateMap(false));
	}

}
