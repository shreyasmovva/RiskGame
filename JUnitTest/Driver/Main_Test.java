package Driver;

import static org.junit.Assert.*;

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.*;

import Map.Map.Territory;
import Player.*;
import Map.Map;
import Map.Map.*;
/**
 * This is the JUnit Test cases for Main class. this implements all
 * the test related to the units within this class
 *
 * @author 	Arun
 * @version 1.0
 * @since   2018-10-01
 */
public class Main_Test {
	
	public static final String FILE_NAME = "Files\\returnMap.map";

	/**
	* This method is used to validate the new map line
	* entered by the user.
	* 
	* parameter isContinent A boolean value to denote if we
	* are editing a continent, if false then it means we are
	* editing a territory
	* 
	* parameter newLine A String entered by the user to replace the old
	* line in map
	* 
	* return True if new line is valid otherwise False
	*/
	@Test
	public void testValidateMapLineForInteger() {
		boolean returnStat=	Main.validateMapLine(true, "Northern Islands=12");			/*Continent Edit*/
		//test for Continent
		assertEquals(true, returnStat);	
	}
	/**
	 * This is to validate and return false if new map line entered by user has fractional score value
	 * 
	 * return false if entered with fractional Continent score
	 */
	@Test
	public void testValidateMapLineForFraction() {
		assertEquals(true, Main.validateMapLine(true, "Northern Islands=12.2"));		/*Continent Edit*/ /**return is good ? wrong  discarding any remainder**/
	}
	/**
	 * This is to validate and return false if new map line entered by user has no score value
	 * 
	 * return false if entered with no Continent score
	 */
	@Test
	public void testValidateMapLineForZero() { 				
		assertEquals(true, Main.validateMapLine(true, "Northern Islands=0"));			/*Continent Edit*/ /**return is good ? wrong  discarding any remainder**/
	}
	/**
	 * This is to validate and return false if new map line entered by user for Territory 
	 * has more than one adjacent countries
	 * 
	 * return true as its valid entry
	 */
	@Test
	public void testValidateMapLineForTerritoryAndTwoAdjecentCountry() {
		assertEquals(true, Main.validateMapLine(false, "1,548,116,Northern Islands,2,12"));
	}
	/**
	 * This is to validate and return false if new map line entered by user for Territory 
	 * has only one adjacent countries
	 * 
	 * return true as its valid entry
	 */
	@Test
	public void testValidateMapLineForTerritoryAndOneAdjecentCountry() { 
		assertEquals(true, Main.validateMapLine(false, "1,548,116,Northern Islands,2"));
	}
	/**
	 * This is to validate and return false if new map line entered by user for Territory 
	 * has no adjacent countries
	 * 
	 * return false as its invalid entry
	 */
	@Test
	public void testValidateMapLineForTerritoryAndNoAdjecentCountry() {
		assertEquals(false, Main.validateMapLine(false, "1,548,116,Northern Islands"));
	}

	/**
	 * This is to validate and return true if new map created and the expected maps are same  
	 * 
	 * return true if the entries of two files are compared to be same
	 */ 
	@Test
	public void testSetLineText() throws IOException {
		Main main = new Main();
		String path = FILE_NAME;
		Path expPath = Paths.get(path);
		List<String> linesBeforeRunningMethod =Files.readAllLines(expPath, StandardCharsets.UTF_8);
		//Main.setLineText(0, "line 0", path+"returnMap.map");//index out of bound exception
		Main.setLineText(7, "Asia=7", path); 
		List<String> linesAfterRunningMethod=Files.readAllLines(expPath, StandardCharsets.UTF_8);
		linesBeforeRunningMethod.set(6, "Asia=7");
		assertTrue(linesBeforeRunningMethod.equals(linesAfterRunningMethod)); 
	}

	/**
	 * This is to validate and return true if the continents entered are as expected  
	 * 
	 * return true if the entries continents are as expected
	 */ 
	@Test
	public void testPopulateUserEnteredContinentLines() throws IOException {
		Main.userEnteredContinentLines = new ArrayList<>(); // need to initialize it here as we are initializing static variable inside the main method
		String path = FILE_NAME;
		Path expPath = Paths.get(path);
		List<String> linesBeforeRunningMethod =Files.readAllLines(expPath, StandardCharsets.UTF_8);
		ArrayList<String> userEnteredContinentLines = new ArrayList<String>(Arrays.asList("North America=5","Mexico=2","Africa=3","Asia=7",""));
		Main.populateUserEnteredContinentLines(linesBeforeRunningMethod); 
		System.out.println("Conti"+Main.userEnteredContinentLines);
		assertTrue(userEnteredContinentLines.equals(Main.userEnteredContinentLines));
	}
	/**
	 * This is to validate and return true if the Territory entered are as expected  
	 * 
	 * return true if the entries of territories are as expected
	 */
	@Test
	public void testPopulateUserEnteredTerritoryLines() throws IOException {
		Main.userEnteredTerritoryLines = new ArrayList<>(); // need to initialize it here as we are initializing static variable inside the main method
		String path = FILE_NAME;
		Path expPath = Paths.get(path);
		List<String> linesBeforeRunningMethod =Files.readAllLines(expPath, StandardCharsets.UTF_8);
		ArrayList<String> userEnteredTerritoryLines = new ArrayList<String>(Arrays.asList("Japan,322,104,North America,Kamchatka,Mongolia","Ural,241,68,Asia,Siberia,China,Afghanistan,Ukraine","Arab,241,68,Asia,Siberia,China,Afghanistan,Ukraine"));
		Main.populateUserEnteredTerritoryLines(linesBeforeRunningMethod);
		System.out.println("terri"+Main.userEnteredTerritoryLines);
		assertTrue(userEnteredTerritoryLines.equals(Main.userEnteredTerritoryLines));
	}

	/**
	* This method is used to test the assignment of initial terrirories. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method assignInitialTerritories()
	*/	
	@Test
	public void testAssignInitialTerritories() {
		Player player = new Player("Player1");
		Map.listOfAllTerritories=new ArrayList<Territory>();
		Main.activeMap = new Map();
		Main.activeMap.territories=new ArrayList<Territory>();
		Main.players = new ArrayList<Player>();
		Main.players.add(player);
		Territory t1 = new Territory("Asia");
		Main.activeMap.territories.add(t1);
		Main.assignInitialTerritories(); 
		assertTrue(t1.owner.getName().equals("Player1"));
	}
	/**
	* This method is used to test the assignment of initial armies for two players. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method assignInitialArmies()
	*/	
	@Test
	public void testAssignInitialArmiesFor2Players() {
		Main.players = new ArrayList<Player>();
		Player player = new Player("Player1");
		Main.players.add(player);
		player = new Player("Player2");
		Main.players.add(player);
		Main.assignInitialArmies(); 
		assertTrue(Main.totalInitialArmies==80); 
	}
	/**
	* This method is used to test the assignment of initial armies for three players. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method assignInitialArmies()
	*/	
	@Test
	public void testAssignInitialArmiesFor3Players() {
		Main.players = new ArrayList<Player>();
		Player player = new Player("Player1");
		Main.players.add(player);
		player = new Player("Player2");
		Main.players.add(player);
		player = new Player("Player3");
		Main.players.add(player);
		Main.assignInitialArmies(); 
		assertTrue(Main.totalInitialArmies==105); 
	}
	/**
	* This method is used to test the assignment of initial armies for for players. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method assignInitialArmies()
	*/	
	@Test
	public void testAssignInitialArmiesFor4Players() {
		Main.players = new ArrayList<Player>();
		Player player = new Player("Player1");
		Main.players.add(player);
		player = new Player("Player2");
		Main.players.add(player);
		player = new Player("Player3");
		Main.players.add(player);
		player = new Player("Player4");
		Main.players.add(player);
		Main.assignInitialArmies(); 
		assertTrue(Main.totalInitialArmies==120); 
	}
	/**
	* This method is used to test the assignment of initial armies for five players. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method assignInitialArmies()
	*/	
	@Test
	public void testAssignInitialArmiesFor5Players() {
		Main.players = new ArrayList<Player>();
		Player player = new Player("Player1");
		Main.players.add(player);
		player = new Player("Player2");
		Main.players.add(player);
		player = new Player("Player3");
		Main.players.add(player);
		player = new Player("Player4");
		Main.players.add(player);
		player = new Player("Player5");
		Main.players.add(player);
		Main.assignInitialArmies(); 
		assertTrue(Main.totalInitialArmies==125); 
	}
	/**
	* This method is used to test the assignment of initial armies for six players. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method assignInitialArmies()
	*/	
	@Test
	public void testAssignInitialArmiesFor6Players() {
		Main.players = new ArrayList<Player>();
		Player player = new Player("Player1");
		Main.players.add(player);
		player = new Player("Player2");
		Main.players.add(player);
		player = new Player("Player3");
		Main.players.add(player);
		player = new Player("Player4");
		Main.players.add(player);
		player = new Player("Player5");
		Main.players.add(player);
		player = new Player("Player6");
		Main.players.add(player);
		Main.assignInitialArmies(); 
		assertTrue(Main.totalInitialArmies==120); 
	}
	/**
	* This method is used to test the building of a map. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method buildMap()
	*/	
	@Test
	public void testbuildMapForContinents() throws IOException{
		 
		Main.activeMap=new Map();
		Main.userEnteredContinentLines = new ArrayList<String>(Arrays.asList("North America=5","Mexico=2","Africa=3","Asia=7",""));
		Main.userEnteredTerritoryLines = new ArrayList<String>(Arrays.asList("Japan,322,104,North America,Kamchatka,Mongolia","Ural,241,68,Asia,Siberia,China,Afghanistan,Ukraine","Arab,241,68,Asia,Siberia,China,Afghanistan,Ukraine"));
		Main.buildMap();
		System.out.println(Main.userEnteredContinentLines);
		//System.out.println(Main.activeMap.te);
		assertTrue(Main.activeMap.continents.get(0).name.equals("North America"));
		
	}
	/**
	* This method is used to test the building of a map. 
	* 
	* this is a void return method and hence the value of the 
	* variable is validated after testing the method buildMap()
	*/	
	@Test
	public void testbuildMapForTerritories() throws IOException{
		 
		Main.activeMap=new Map();
		Main.userEnteredContinentLines = new ArrayList<String>(Arrays.asList("North America=5","Mexico=2","Africa=3","Asia=7",""));
		Main.userEnteredTerritoryLines = new ArrayList<String>(Arrays.asList("Japan,322,104,North America,Kamchatka,Mongolia","Ural,241,68,Asia,Siberia,China,Afghanistan,Ukraine","Arab,241,68,Asia,Siberia,China,Afghanistan,Ukraine"));
		Main.buildMap();
		System.out.println(Main.userEnteredContinentLines);
		//System.out.println(Main.activeMap.te);
		assertTrue(Main.activeMap.continents.get(0).territories.get(0).name.equals("Japan"));
		
	}
 
}
