package ca.ucalgary.seng300.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng300.VendingMachineLogic.*;
import org.lsmr.vending.Coin;
import org.lsmr.vending.hardware.*;

import org.junit.Test;

public class VMLogicTester {
	private VendingMachine vm;
	private VendingMachineLogic vml;
	@Before
	public void setUp() throws Exception {
		
		int[] coinKinds = {5, 10, 25, 100, 200}; //Nickels, dimes, quarters, loonies, toonies (all values in cents)
		int btnCount = 6; //6 types of pop
		int coinRackCapacity = 15;
		int popRackCapacity = 10;
		int receptacleCapacity = 4;
		int deliveryChuteCapacity = 10;
		int coinReturnCapacity = 8;
		
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
		
		//load the coin racks 
		for(int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
			Coin value;
			if (i == 0) 
				value = new Coin(5);
			else if (i == 1)
				value = new Coin(10);
			else if (i == 2)
				value = new Coin(25);
			else if (i == 3)
				value = new Coin(100);
			else
				value = new Coin(200);
			
			vm.getCoinRack(i).load(value, value, value, value);	
		
	}
		
}
	//Purchase normally
	@Test
	public void test1() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		assertEquals(vml.getCredit(), 400);
		vm.getSelectionButton(1).press();
		System.out.println(vml.getEvent());
		assertEquals(vml.getEvent(), "Delivery chute door opened");
	//	assertEquals(vm.getDeliveryChute().size(), 1);
	
	}
	
	//Invalid coin insertion
	@Test
	public void test2() throws Exception {
		Coin fiver = new Coin(500);
		vm.getCoinSlot().addCoin(fiver);
		assertEquals(vml.getCredit(), 0);
	}
	
	//Test to check when you attempt to purchase a something that is not in stock
	@Test
	public void test3() throws DisabledException, EmptyException {
		
		Coin toonie = new Coin(200);
		
		vm.getPopCanRack(3).unload();
		
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		
		vm.getSelectionButton(3).press();
		assertEquals(vm.getDeliveryChute().size(), 0);
	}
	
	//Testing for when the coin receptacle becomes full
	@Test
	public void test4() throws DisabledException, CapacityExceededException {
		Coin loonie = new Coin(100);		
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		assertTrue(!(vm.getCoinReceptacle().hasSpace()));
		try {
			vm.getCoinSlot().addCoin(loonie);
		}
		catch (DisabledException e){
			fail("purposely failed test to make sure catch block ran");
		} 
	}
	
	//attempting to purchase with insufficient credits
	@Test
	public void test5() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(3).press();
		assertEquals(vm.getDeliveryChute().size(), 0);
	}
	
	//checking to see if change is returned after making a purchase with excess credit
	@Test 
	public void testReturnedChange() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(1).press();
		assertTrue(vm.getCoinReturn().size() > 0);
	}
	
}
