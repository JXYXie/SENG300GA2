/****************************************************
 * SENG 300 Group Assignment 2
 * This class handles all the logic operations that occur in a vending machine,
 * specifically, this class handles the calling of methods when money is inserted
 * and buttons are pressed as well as the listening of their respective events
 ****************************************************/
package ca.ucalgary.seng300.VendingMachineLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class VendingMachineLogic implements CoinSlotListener, CoinRackListener, CoinReturnListener, CoinReceptacleListener, 
	PushButtonListener, PopCanRackListener, DeliveryChuteListener, IndicatorLightListener {

	private VendingMachine vm;
	private EventLogger logger;
	private Timer timer = new Timer();
	private int userCredit;
	private List<PushButton> buttonList = new ArrayList<>();
	private String event;
	private String currency;

	/********************
	 * Constructor
	 * instantiates and initializes all the relevant hardware
	 * and registers listeners
	 *******************/
	public VendingMachineLogic(VendingMachine vm) {
		
		this.vm = vm;
		logger = new EventLogger();
		
		userCredit = 0;
		currency = "CAD";
		
		//For loop to iterate through all the available buttons
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
			PushButton sb = vm.getSelectionButton(i); //Instantiates the hardware
			sb.register(this); //And registers the relevant listeners
			buttonList.add(sb); //Stores into an ArrayList for later use
		}
		//Iterate through all available pop can racks
		for (int i = 0; i < vm.getNumberOfPopCanRacks(); i++) {
			PopCanRack pcr = vm.getPopCanRack(i);
			pcr.register(this); //Registers the relevant listeners
		}
		//Iterate through all available coin racks
		for (int i = 0; i < vm.getNumberOfCoinRacks(); i++) {
			vm.getCoinRack(i).register(this); //Registers the relevant listeners
		}
		
		//Now register all the rest of the listeners
		vm.getCoinSlot().register(this);
		vm.getDeliveryChute().register(this);
		vm.getCoinReceptacle().register(this);
		//TODO implement methods first before registering or else program wont run properly
		//vm.getCoinReturn().register(this);
		//vm.getOutOfOrderLight().register(this);
		//vm.getExactChangeLight().register(this);
		//TODO Display Listener
		display(""); //Display looping message at beginning
	}

	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		//TODO
		//Probably empty here
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		//TODO
		//Probably empty here
	}
	
	/*******************************Start CoinSlot Listener*************************************/
	//When a valid coin is inserted into the vending machine
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		addCredit(coin.getValue()); //Increment the credit when valid coins are inserted
		event = "Inserted " + currency + " " + coin.getValue() + " cents"; //Updates event for coin insertion
		logger.log(event); //Now logs that event
		event = "Credit: " + userCredit; //Updates event for the display
		display(event); //Displays the current credit for and logs it to file
	}
	//Rejects an invalid coin
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		event = "Invalid coin inserted";
		logger.log(event);
	}
	/*******************************End CoinSlot Listener*************************************/
	
	/***********************************Start CoinRack Listener**************************************/
	@Override
	public void coinsFull(CoinRack rack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsEmpty(CoinRack rack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {
		// TODO Auto-generated method stub
		
	}
	/***********************************End CoinRack Listener**************************************/
	
	/********************************Start CoinReturn Listener******************************/
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnIsFull(CoinReturn coinReturn) {
		// TODO Auto-generated method stub
		
	}
	/************************************End CoinReturn Listener******************************/
	
	/******************************Start CoinReceptacle Listener******************************/
	@Override
	public void coinAdded(CoinReceptacle receptacle, Coin coin) {}

	@Override
	public void coinsRemoved(CoinReceptacle receptacle) {}

	@Override
	public void coinsFull(CoinReceptacle receptacle) {
		event = "Coin receptacle full";
		logger.log(event);
		enableSafety();
	}

	@Override
	public void coinsLoaded(CoinReceptacle receptacle, Coin... coins) {}

	@Override
	public void coinsUnloaded(CoinReceptacle receptacle, Coin... coins) {}
	/******************************End CoinReceptacle Listener******************************/
	
	/*********************************Start Button Listener*****************************************/
	
	/***************************************************
	 * Handles the logic of selection buttons
	 **************************************************/
	@Override
	public void pressed(PushButton button) {

		int btnIndex = buttonList.indexOf(button) ; //Which button did the user press
		
		event = vm.getPopKindName(btnIndex) + " button at button index " + btnIndex + " pressed";
		logger.log(event);
		if (btnIndex == -1) { //Unregistered button is pressed
			//Nothing happens for now
		}

		int cost = vm.getPopKindCost(btnIndex);

		if (cost > userCredit) { //Not enough money!!!
			event = "Insufficent credit for purchase"; //Updates message
			display(event);
		} else {
			PopCanRack pr = vm.getPopCanRack(btnIndex); //Matches the button with the corresponding pop rack
			try {
				pr.dispensePopCan(); //Dispenses the relevant pop
				vm.getCoinReceptacle().storeCoins(); //Stores the change
				userCredit -= cost; //Deduct the pay from the available credit
				vm.getCoinReceptacle().returnCoins();
				display("");
			} catch (DisabledException | CapacityExceededException e) {
				throw new SimulationException(e);
			} catch (EmptyException e2) {
				event = "Pop is sold out!"; //Set the event for sold-out
				logger.log(event);
			}
		}
	}
	
	/**********************************End Button Listener*****************************************/
	
	/********************************Start PopCanRack Listener*************************************/
	//Adding PopCan to PopCanRack
	@Override
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {
		event = "Added " + popCan.getName();
		logger.log(event);
	}
	//Removing PopCan from PopCanRack
	@Override
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		event = "Removed a " + popCan.getName();
		logger.log(event);
	}

	@Override
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {
		//Leave empty
	}

	@Override
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {
		//Leave empty
	}

	@Override
	public void popCansFull(PopCanRack popCanRack) {
		event = "Pop can rack full";
		logger.log(event);
	}

	@Override
	public void popCansEmpty(PopCanRack popCanRack) {
		event = "Pop can rack empty";
		logger.log(event);
	}
	
	/*******************************End PopCanRack Listener*************************************/
	
	/********************************Start DeliveryChute Listener********************************/
	@Override
	public void itemDelivered(DeliveryChute chute) {
		// TODO maybe?
		
	}

	@Override
	public void doorOpened(DeliveryChute chute) {
		event = "Delivery chute door opened";
		logger.log(event);
	}

	@Override
	public void doorClosed(DeliveryChute chute) {
		event = "Delivery chute door closed";
		logger.log(event);
	}

	@Override
	public void chuteFull(DeliveryChute chute) {
		event = "Delivery chute door full";
		logger.log(event);
		enableSafety(); //enables the safety
		
	}
	/******************************End DeliveryChute Listener********************************/
	
	/******************************Start IndicatorLight Listener********************************/
	/**
	 * Indicator light behaviour
	 * Toggles the lights and logs the relevant actions
	 */
	@Override
	public void activated(IndicatorLight light) {
		if (light == vm.getExactChangeLight()) {
			event = "Exact change only light turned on";
		}
		else if (light == vm.getOutOfOrderLight()) {
			event = "Out of order light turned on";
		}
		logger.log(event);
	}

	@Override
	public void deactivated(IndicatorLight light) {
		if (light == vm.getExactChangeLight()) {
			event = "Exact change only light turned off";
		}
		else if (light == vm.getOutOfOrderLight()) {
			event = "Out of order light turned off";
		}
		logger.log(event);		
	}
	/********************************End IndicatorLight Listener*********************************/
	
	//TODO More testing somewhat broken
	public void display(String event) {
		
		if (userCredit > 0) { //if there is still credit in the vending machine
			resetTimer();
			vm.getDisplay().display(event);
			logger.log("DISPLAY: " + event);
		}
		else { //credit is 0
			String noCreditEvent = "Hi there!";
			resetTimer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					vm.getDisplay().display(noCreditEvent);
					logger.log("DISPLAY: " + noCreditEvent);
				}
			}, 0, 5000);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					vm.getDisplay().display("");
				}
			}, 0, 15000);
		}
	}
	
	private void resetTimer() {
		timer.cancel();
		timer = new Timer();
	}
	
	/**
	 * Enables the safety of the vending machine
	 */
	void enableSafety() {
		vm.enableSafety();
		event = "Safety enabled!";
		logger.log(event);
	}
	
	/**
	 * Disables the safety of the vending machine
	 */
	void disableSafety() {
		vm.disableSafety();
		event = "Safety disabled!";
		logger.log(event);
	}
	
	/**
	 * @param amount of credit to be added
	 */
	public void addCredit(int amount) {
		userCredit += amount;
	}
	
	/**
	 * @return the current event
	 * Useful for printing things to a log file or do display
	 */
	public String getEvent() {
		return event;
	}
	
	/**
	 * @return how much credit is in vending machine
	 */
	public int getCredit() {
		return userCredit;
	}
	
}
