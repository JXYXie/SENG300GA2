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

import org.junit.Test;

public class VMLogicTester {
	private VendingMachine vm;
	private VendingMachineLogic vml;
	@Before
	public void setUp() throws Exception {
		
		int[] coinKinds = {5, 10, 25, 100, 200}; //Nickels, dimes, quarters, loonies, toonies (all values in cents)
		int btnCount = 6; //6 types of pop
		int coinRackCapacity = 5;
		int popRackCapacity = 10;
		int receptacleCapacity = 8;
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
		vm.loadPopCans(5, 4, 0, 0, 5, 5); //Starts with 5 of each kind of pop
	}
	//Test to check when you attempt to purchase a something that is not in stock
	@Test
	public void notInstock() throws DisabledException, SimulationException {

		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		
		vm.getSelectionButton(3).press();
	}
	@Test
	public void popcanUnload() throws DisabledException, SimulationException {

		
	}
	
	//Testing for coinrackFull
	@Test
	public void nonValidCoin() throws DisabledException, SimulationException {
		Coin loonie = new Coin(100);		
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);

	}
	//attempting to purchase with sufficient credits
	@Test
	public void insufficientC() throws DisabledException, SimulationException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(3).press();
		
	}
	
	//removing coins 
	@Test
	public void invalidCoin() throws DisabledException, SimulationException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinReturn();
		
	}
	//Adding popCanFull
	@Test
	public void addCan() throws DisabledException, SimulationException{
		vm.getPopCanRack(0).unload();
		vm.getPopCanRack(1).unload();
		vm.getPopCanRack(2).unload();
		vm.getPopCanRack(3).unload();
		vm.getPopCanRack(4).unload();
		vm.getPopCanRack(5).unload();
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(3).press();
		vm.getSelectionButton(3).press();
		vm.getSelectionButton(3).press();
		vm.getSelectionButton(3).press();
		vm.getSelectionButton(3).press();
		vm.getSelectionButton(3).press();
		
	}
	//purchase normally
	@Test
	public void normPurchase() throws DisabledException, SimulationException{
		vm.getPopCanRack(0).load(popCans);
	}
	
	//max insertCoins

}
