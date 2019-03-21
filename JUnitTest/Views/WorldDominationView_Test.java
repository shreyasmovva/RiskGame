package Views;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import Driver.Main;
import Map.Map;
import Map.Map.Territory;
import Player.Player;

/**
* This class is used to display to the GUI the world view details
* 
*/
public class WorldDominationView_Test {
	private WorldDominationView worldView;
	private Player player;
	/**
	* This method runs before each test case functions
	* 
	*/
	@Before
	public void testBefore() {
		worldView = new WorldDominationView();
		player = new Player("Player1");
		Main.players = new ArrayList<>();
	}
	/**
	* This method is used to display to the GUI in formatted way
	* 
	*/
	@Test
	public void testCountryNameWithContinentAndArmy() {
		Map.listOfAllTerritories = new ArrayList<Territory>();
		Territory t1 = new Territory("Africa");
		t1.addNeighbours("India,Pakistan");
		t1.numberOfArmies=5;
		t1.continent=new Map();
		t1.continent.name="Asia";
		worldView = new WorldDominationView();
		worldView.countryNameWithContinentAndArmy(t1, true);
		assertNotNull(t1.name);
	}
	/**
	* This method is used to display to the GUI in formatted way
	* 
	*/
	@Test
	public void testCountryNameWithContinentAndArmyFalse() {
		Map.listOfAllTerritories = new ArrayList<Territory>();
		Main.activeMap = new Map();
		Main.activeMap.territories = new ArrayList<Territory>();
		Main.players = new ArrayList<Player>(); 
		player = new Player("Player1");
		Main.players.add(player);
		Territory t1 = new Territory("Africa");
		t1.addNeighbours("India,Pakistan");
		t1.numberOfArmies=5;
		t1.continent=new Map();
		t1.continent.name="Asia";
		worldView = new WorldDominationView();
		worldView.countryNameWithContinentAndArmy(t1, false);
		assertNotNull(t1.name);
	}
	 
}
