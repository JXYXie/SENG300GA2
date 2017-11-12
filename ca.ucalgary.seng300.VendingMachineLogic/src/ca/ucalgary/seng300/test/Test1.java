/****************************************************
 * SENG 300 Group Assignment 1
 * Authors: 
 * Xin Yan (Jack) Xie
 * Xiangyu (Michael) Han
 * Zachary Metz
 * This class tests the functionality of the VendingMachineLogic class
 * by simulating different possible user interactions
 * An overall 86.2% code coverage is reached
 ****************************************************/
package ca.ucalgary.seng300.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng300.VendingMachineLogic.*;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.DisabledException;
import org.lsmr.vending.hardware.SimulationException;
import org.lsmr.vending.hardware.VendingMachine;

public class Test1 {
	
	private VendingMachine vm;
	private VendingMachineLogic vml;
	@Before
	public void setUp() throws Exception {
		
		int[] coinKinds = {5, 10, 25, 100, 200}; //Nickels, dimes, quarters, loonies, toonies (all values in cents)
		int btnCount = 6; //6 types of pop
		int coinRackCapacity = 15;
		int popRackCapacity = 10;
		int receptacleCapacity = 200;
		int deliveryChuteCapacity = 10;
		int coinReturnCapacity = 10;
		
		vm = new VendingMachine(coinKinds, btnCount, coinRackCapacity, popRackCapacity, receptacleCapacity, deliveryChuteCapacity, coinReturnCapacity);
		vml = new VendingMachineLogic(vm);
		
		List<String> popNames = new ArrayList<String>(); //List of pop names
				
		popNames.add("Water");
		popNames.add("Pepsi");
		popNames.add("Sprite");
		popNames.add("Mountain Dew");
		popNames.add("Orange Crush");
		popNames.add("Gatorade");
		
		List<Integer> costs = new ArrayList<Integer>(); //List of pop costs
		
		for (int i = 0; i < popNames.size(); i++ ) {
			costs.add(250); //everything costs 2.50
		}
		
		vm.configure(popNames, costs);
		vm.loadPopCans(5, 5, 5, 5, 5, 5); //Starts with 5 of each kind of pop
	}

	@Test
	public void buyPopTest() throws DisabledException {
		
		Coin loonie = new Coin(100);
		Coin toonie = new Coin(200);
		
		vm.getCoinSlot().addCoin(loonie); //100
	}
	
	@Test
	public void soldOutTest() throws DisabledException, SimulationException {
		Coin loonie = new Coin(100);
		Coin toonie = new Coin(200);
		
		vm.getCoinSlot().addCoin(toonie); //200*7 = 1400
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(loonie); // 1400 + 100 = 1500
		assertEquals(1500, vml.getCredit()); //1500 cents
		
		vm.getSelectionButton(0).press(); //I want water
		vm.getSelectionButton(0).press(); //I want water
		vm.getSelectionButton(0).press(); //I want water
		vm.getSelectionButton(0).press(); //I want water
		vm.getSelectionButton(0).press(); //I want water
	}

	@Test
	public void maliciousTest() throws DisabledException {
		
		Coin washer = new Coin(1); //Invalid coin
		
		vm.getCoinSlot().addCoin(washer);
		
		vm.getSelectionButton(2).press(); //I want Sprite for free
		
		assertEquals(0, vml.getCredit()); //No credit
	}	
}
