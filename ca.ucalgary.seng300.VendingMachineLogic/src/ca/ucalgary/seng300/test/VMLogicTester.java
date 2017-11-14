package ca.ucalgary.seng300.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.ucalgary.seng300.VendingMachineLogic.*;
import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.*;

import org.junit.Test;

public class VMLogicTester {
	private VendingMachine vm;
	private VendingMachineLogic vml;
	private VendingListener vl;
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
		vl = vml.getListener();
		
		
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
	}
	
	//Invalid coin insertion
	/*
	@Test
	public void test2() throws Exception {
		Coin fiver = new Coin(500);
		
		vm.getCoinSlot().addCoin(fiver);
	}*/
	
	//Test to check when you attempt to purchase a something that is not in stock
	@Test
	public void test3() throws DisabledException, EmptyException {
		
		Coin toonie = new Coin(200);
		
		vm.getPopCanRack(3).unload();
		
		vm.getCoinSlot().addCoin(toonie);
		vm.getCoinSlot().addCoin(toonie);
		
		vm.getSelectionButton(3).press();
	}
	
	//Testing for when the coin receptacle becomes full
	/*
	@Test
	public void test4() throws DisabledException, CapacityExceededException {
		Coin loonie = new Coin(100);		
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
		vm.getCoinSlot().addCoin(loonie);
	}*/
	
	//attempting to purchase with insufficient credits
	@Test
	public void test5() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(3).press();
	}
	
	/***************************************************
	 * Test initialization 
	 **************************************************/
	@Test
	public void testNumOfPopCanRacks() {
		assertEquals(6,vm.getNumberOfPopCanRacks()); 
	}
	@Test
	public void testNumOfButtons() {
		assertEquals(6,vm.getNumberOfSelectionButtons());
	}
	
	@Test
	public void testNamesOfPop() {
		assertEquals("Water",vm.getPopKindName(0));
	}
	
	@Test
	public void testcoinRackCapacity() {
		assertEquals(4,vm.getCoinRack(0).getCapacity());
	}
	
	@Test
	public void testreceptacleCapacity() {
		assertEquals(4,vm.getCoinReceptacle().getCapacity());
	}
// Hasn't been implemented yet
//	@Test
//	public void testcoinReturnCapacity() {
//		assertEquals(200,vm.getCoinReturn().getCapacity());
//	}
	
	@Test
	public void testpopCanRackCapacity() {
		assertEquals(10,vm.getPopCanRack(0).getCapacity());
	}
	
	@Test
	public void testdeliveryChuteCapacity() {
		assertEquals(4,vm.getDeliveryChute().getCapacity());
	}
	
	
	
	/***************************************************
	 * Test events
	 **************************************************/
	
	
	/*******************************Start CoinSlot Listener*************************************/
	
	
	@Test
	public void testEventValidCoinInserted() throws DisabledException {
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		assertEquals("Credit: 200",vl.getEvent());
		
	}

	class acceptInvalidCoin implements CoinAcceptor
	{

		@Override
		public void acceptCoin(Coin coin) throws CapacityExceededException, DisabledException {
		}

		@Override
		public boolean hasSpace() {
			return true;
		}
		
	}
	@Test
	public void testEventCoinRejected() throws DisabledException {
		Coin ivCoin = new Coin(1);
		CoinAcceptor ca = new acceptInvalidCoin();
		CoinChannel invalid = new CoinChannel(ca);
		vm.getCoinSlot().connect(null, invalid);
		vm.getCoinSlot().addCoin(ivCoin);
		assertEquals("Invalid coin inserted",vl.getEvent());
	}
	/*******************************End CoinSlot Listener*******************************************/
	/**********************************CoinReceptacle Listener**************************************/
	/*
	 * new CoinReceptacleListener
	 */
	
	class fullCoinReceptacle implements CoinAcceptor
	{

		@Override
		public void acceptCoin(Coin coin) throws CapacityExceededException, DisabledException {
		}

		@Override
		public boolean hasSpace() {
			return false;
		}
		
	}
	
	@Test(expected = CapacityExceededException.class)
	public void testEventCoinsFull() throws CapacityExceededException, DisabledException {
		Coin Coin = new Coin(200);

		vm.getCoinReceptacle().load(Coin,Coin,Coin,Coin);
		vm.getCoinReceptacle().acceptCoin(Coin);
		assertEquals("Coin receptacle full",vl.getEvent());
		
	}
	
	
	@Test
	public void testEventButtonPressedInsufficentFunds() throws DisabledException {
		
		Coin toonie = new Coin(200);
		vm.getCoinSlot().addCoin(toonie);
		vm.getSelectionButton(0).press();
		assertEquals("Water button at button index 0 pressed",vl.getEvent());
		//Finish going through the motion of dispensing the product
	}
	
	@Test
	public void testEventButtonPressedSoldOut() throws DisabledException {
		
		
		vml.addCredit(1250);
		
		vm.getSelectionButton(0).press();
		vm.getSelectionButton(0).press();
		vm.getSelectionButton(0).press();
		vm.getSelectionButton(0).press();
		
		vm.getSelectionButton(0).press();//no water left
		
		assertEquals("Pop is sold out!",vl.getEvent());
	}
	/**********************************CoinReceptacle Listener**************************************/
	
	/********************************Start PopCanRack Listener
	 * @throws DisabledException 
	 * @throws CapacityExceededException *************************************/
	@Test
	public void testEventpopCanAdded() throws CapacityExceededException, DisabledException {
		PopCan popCan = new PopCan("Water");
		vm.getPopCanRack(0).acceptPopCan(popCan);
		assertEquals("Added Water",vl.getEvent());
	}
	
	@Test
	public void testEventpopCanRemoved() throws DisabledException, EmptyException, CapacityExceededException {
		vm.getPopCanRack(0).dispensePopCan();
		//order
		assertEquals("Delivery chute door closed",vl.getEvent());
	}
	
	@Test
	public void testEventpopCansFull() throws CapacityExceededException, DisabledException {
		PopCan popCan = new PopCan("Water");
		
		vm.getPopCanRack(0).acceptPopCan(popCan);
		vm.getPopCanRack(0).acceptPopCan(popCan);
		vm.getPopCanRack(0).acceptPopCan(popCan);
		vm.getPopCanRack(0).acceptPopCan(popCan);
		vm.getPopCanRack(0).acceptPopCan(popCan);//Rack is full now (started with 5, capacity is 10)
		
		assertEquals("Pop can rack full",vl.getEvent());
	}
	
	@Test
	public void testEventpopCansEmpty() throws DisabledException, EmptyException, CapacityExceededException {
		vm.getPopCanRack(0).dispensePopCan();
		vm.getPopCanRack(0).dispensePopCan();
		vm.getPopCanRack(0).dispensePopCan();
		vm.getPopCanRack(0).dispensePopCan();
		vm.getPopCanRack(0).dispensePopCan();//Rack is empty now (started with 5, now 0)		
	
		assertEquals("Pop can rack empty",vl.getEvent());
	}
	/*******************************End PopCanRack Listener*************************************/
	
	/********************************Start DeliveryChute Listener********************************/
	
	@Test
	public void testEventdoorClosed() {
		vm.getDeliveryChute().removeItems();
		assertEquals("Delivery chute door closed",vl.getEvent());
	}
	
	
	@Test
	public void testEventpopcanchuteFull() throws CapacityExceededException, DisabledException {
		
		PopCan popCan = new PopCan("Water");
		for(int i =0; i < 8; i++)
		{
			vm.getDeliveryChute().acceptPopCan(popCan);
		}
		
		vm.getDeliveryChute().acceptPopCan(popCan);
		//"Delivery chute door full" is logged then "Safety enabled! then Delivery chute door closed"
		assertEquals("Delivery chute door closed",vl.getEvent());
	}
	
	/******************************End DeliveryChute Listener***********************************/
	
	/******************************Start IndicatorLight Listener********************************/
	
	@Test
	public void testEventactivatedIndicatorLightExact() {
		vm.getExactChangeLight().activate();
		assertEquals("Exact change only light turned on",vl.getEvent());
	}
	
	@Test
	public void testEventactivatedIndicatorLightOutOfOrder() {
		vm.getOutOfOrderLight().activate();
		assertEquals("Out of order light turned on",vl.getEvent());
	}
	
	@Test
	public void testEventdeactivatedIndicatorLightExact() {
		vm.getExactChangeLight().deactivate();
		assertEquals("Exact change only light turned off",vl.getEvent());
	}
	
	@Test
	public void testEventdeactivatedIndicatorLightOutOfOrder() {
		vm.getOutOfOrderLight().deactivate();
		assertEquals("Out of order light turned off",vl.getEvent());
	}
	/********************************End IndicatorLight Listener*********************************/
	
	
	@Test
	public void testEventenableSafety() {
		vml.enableSafety();
		assertEquals("Out of order light turned on",vl.getEvent());
	}
	
	 
	@Test
	public void testEventdisableSafety() {
		vml.disableSafety();
		assertEquals("Out of order light turned off",vl.getEvent());
	}
	
	/***************************************************
	 * Test VendingMachineLogic.java public void methods
	 **************************************************/
	@Test
	public void testAddCredit() {		
		vml.addCredit(72);
		assertEquals(72, vml.getCredit());
	}
	
	class vmlDisplayListener implements DisplayListener{
		
		public String oldMessage = "";
		public String newMessage = "";
		
		@Override
		public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}

		@Override
		public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}

		@Override
		public void messageChange(Display display, String oldMessage, String newMessage) {
			this.oldMessage = oldMessage;
			this.newMessage = newMessage;
			}
		}
	
	//TODO vml.displayHi();
	@Test
	public void testDisplayHi()
	{
		vmlDisplayListener vDL = new vmlDisplayListener();
		vm.getDisplay().register(vDL);
		String event = "zero";
		assertEquals(0,vml.getCredit());
		//vml.displayHi();
		
		assertEquals("Hi there!",vDL.oldMessage);
		
	}
	
	//TODO vml.display(credit);
	@Test
	public void testDisplayWithMoreThanZeroCredit()
	{
		vmlDisplayListener vDL = new vmlDisplayListener();
		vm.getDisplay().register(vDL);
		String event = "NotZero";
		vml.addCredit(50);
		assertEquals(50,vml.getCredit());
		vml.displayCredit();
		assertEquals(event,vDL.oldMessage);
	}
	
	//TODO vml.display(credit);
	@Test
	public void testDisplayWithZeroCredit()
	{
		vmlDisplayListener vDL = new vmlDisplayListener();
		vm.getDisplay().register(vDL);
		vml.addCredit(50);
		assertEquals(50,vml.getCredit());
		vml.displayCredit();
		assertEquals("Hi there!",vDL.newMessage);
	}
	
	//TODO vml.display(credit);
	@Test
	public void testDisplayEvent()
	{
		vmlDisplayListener vDL = new vmlDisplayListener();
		vm.getDisplay().register(vDL);
		String event = "NotZero";
		vml.addCredit(50);
		assertEquals(50,vml.getCredit());
		vml.display(event);
		assertEquals(event,vDL.newMessage);
	}
	
	//Completely skips the branch to notify all
	@Test
	public void testEventdoorOpen() {
		
		vmlDisplayListener vDL = new vmlDisplayListener();
		vm.getDisplay().register(vDL);
		vml.addCredit(50);
		vm.getDeliveryChute().removeItems();
		vm.getDeliveryChute().removeItems();
		//notifyDoorOpened(); is old message
		//notifyDoorClosed(); is new message
		assertEquals("Delivery chute door opened",vl.getEvent());
	}
	
	@Test
	public void testRemoveFromRack() throws CapacityExceededException, EmptyException, DisabledException
	{
		Coin coin = new Coin(5);
		vm.getCoinRack(0).acceptCoin(coin);
		vm.getCoinRack(0).releaseCoin();
		//Need to hook up channels
		assertEquals("5 coin has been removed from its rack",vl.getEvent());
	}
	
	@Test
	public void testFullRackEvent() throws CapacityExceededException, EmptyException, DisabledException
	{
		Coin coin = new Coin(5);
		vm.getCoinRack(0).acceptCoin(coin);
		vm.getCoinRack(0).acceptCoin(coin);
		vm.getCoinRack(0).acceptCoin(coin);
		vm.getCoinRack(0).acceptCoin(coin);
		assertEquals("A coin rack is full",vl.getEvent());
	}
	
	@Test(expected = EmptyException.class)
	public void testEmptyRackEvent() throws CapacityExceededException, EmptyException, DisabledException
	{
		vm.getCoinRack(0).releaseCoin();
		assertEquals("A coin rack is empty",vl.getEvent());
	}
	

	
	//TODO Test delivery chute full and coin receptacle full - test disabledException and capacityExceededException
	// For pressed button in Listener Class
	
	
}
	

