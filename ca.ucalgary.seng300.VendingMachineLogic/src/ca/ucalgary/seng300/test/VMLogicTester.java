package ca.ucalgary.seng300.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
		int coinRackCapacity = 4;
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
	}
	
	//Purchase normally
	@Test
	public void test1() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(1).press();
		assertEquals(vml.getEvent(), "DISPLAY: Credit: 150");

	}
	
	//Invalid coin insertion
	@Test
	public void test2() throws Exception {
		Coin fiver = new Coin(500);
		vm.getCoinSlot().addCoin(fiver);
		assertEquals(vml.getEvent(), "Invalid coin inserted");
	}
	
	//Test to check when you attempt to purchase a something that is not in stock
	@Test
	public void test3() throws DisabledException, EmptyException {
		
		Coin toonie = new Coin(200);
		
		vm.getPopCanRack(3).unload();
		
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		
		vm.getSelectionButton(3).press();
		
		assertEquals(vml.getEvent(), "DISPLAY: Mountain Dew is sold out!");
	}
	
	//Testing for when the coin receptacle becomes full
	@Test
	public void test4() throws DisabledException, CapacityExceededException {
		Coin loonie = new Coin(100);		
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		assertEquals(vml.getEvent(), "DISPLAY: Credit: 400");

	}
	
	//attempting to purchase with insufficient credits
	@Test
	public void test5() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(3).press();
		assertEquals(vm.getPopCanRack(3).size(), 5);
		assertEquals(vml.getEvent(), "DISPLAY: Insufficient credit: 50 cents short");
	}
	
    //paying exact price with no change return
    @Test
    public void test6() throws DisabledException {
        Coin toonie = new Coin(200);
        Coin quarter = new Coin(25);
        vm.getCoinSlot().addCoin(quarter);
        vm.getCoinSlot().addCoin(quarter);
        vm.getCoinSlot().addCoin(toonie);
        vm.getSelectionButton(3).press();
        if(vml.getEvent() =="Exact change only light turned off" ){
    		assertEquals(vml.getEvent(), "Exact change only light turned off");
        }else {
        	assertEquals(vml.getEvent(), "DISPLAY: Hi there!");
        }
    }
    
	//This test makes sure pop is not vended when the machine is disabled, and change is not returned
	@Test 
	public void test7() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		
		vm.enableSafety();
		vm.getSelectionButton(4).press();
		assertEquals(vm.getPopCanRack(4).size(), 5);
		assertEquals(vm.getCoinReturn().size(), 0);
	}
	
    //Enabling and the disabling safety
    @Test
    public void test8() throws DisabledException {
    	vml.enableSafety();
    	vml.disableSafety();
        Coin toonie = new Coin(200);
        Coin quarter = new Coin(25);
        vm.getCoinSlot().addCoin(quarter);
        vm.getCoinSlot().addCoin(quarter);
        vm.getCoinSlot().addCoin(toonie);
        vm.getSelectionButton(3).press();
        if(vml.getEvent() =="Exact change only light turned off" ){
    		assertEquals(vml.getEvent(), "Exact change only light turned off");
        }else {
        	assertEquals(vml.getEvent(), "DISPLAY: Hi there!");
        }
    }
}
