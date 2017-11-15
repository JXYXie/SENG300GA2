/**********************************************************
 * SENG 300 Group Assignment 2
 * Vending machine listener class
 * This class contains all the listeners for the various hardware
 * It handles events and listens for them when events are announced
 * This is important not only for logic reasons but also for the displaying
 * and logging of events
 **********************************************************/
package ca.ucalgary.seng300.VendingMachineLogic;

import java.util.Arrays;
import org.lsmr.vending.Coin;
import org.lsmr.vending.PopCan;
import org.lsmr.vending.hardware.*;

public class VendingListener implements CoinSlotListener, CoinRackListener, CoinReceptacleListener, CoinReturnListener,  
PushButtonListener, PopCanRackListener, DeliveryChuteListener, IndicatorLightListener, DisplayListener{
	
	private VendingMachineLogic vml;
	private VendingMachine vm;
	
	private String event;
	
	/**
	 * Constructor which passes in VendingMachine and VendingMachingLogic objects to be initialized
	 * @param vm
	 * @param vml
	 */
	public VendingListener(VendingMachine vm, VendingMachineLogic vml) {
		this.vm = vm; //passes vending machine object
		this.vml = vml; //and vending machine logic object
	}
	
	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
	
	
	/*******************************Start CoinSlot Listener*************************************/
	//When a valid coin is inserted into the vending machine
	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		vml.addCredit(coin.getValue()); //Increment the credit when valid coins are inserted
		event = ("Inserted a " + vml.getCurrency() + coin.getValue() + " cent coin"); //Updates event for coin insertion
		vml.log(event); //Now logs that event
		event = "Credit: " + vml.getCredit(); //Updates event for the display
		vml.display(event); //Displays the current credit for and logs it to file
	}
	//Rejects an invalid coin
	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		event = "Invalid " + coin.getValue() + " cent coin inserted";
		vml.log(event);
	}
	/*******************************End CoinSlot Listener*************************************/
	
	
	/***********************************Start CoinRack Listener**************************************/
	/**
	 * When a coin rack is full
	 */
	@Override
	public void coinsFull(CoinRack rack) {
		event = "A coin rack is full";
		vml.log(event);
	}
	/**
	 * When a coin rack is empty
	 */
	@Override
	public void coinsEmpty(CoinRack rack) {
		event = "A coin rack is empty";
		vml.log(event);
	}

	/**
	 * A coin gets added to its coin rack
	 */
	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		event = coin.getValue() + " cent coin has been added to its rack";
		vml.log(event);
	}

	/**
	 * A coin gets removed from its coin rack
	 */
	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		event = coin.getValue() + " coin has been removed from its rack";
		vml.log(event);
	}

	@Override
	public void coinsLoaded(CoinRack rack, Coin... coins) {}

	@Override
	public void coinsUnloaded(CoinRack rack, Coin... coins) {}
	/***********************************End CoinRack Listener**************************************/
	
	
	/******************************Start CoinReceptacle Listener******************************/
	@Override
	public void coinAdded(CoinReceptacle receptacle, Coin coin) {}
	
	/**
	 * event when the coin receptacle is emptied
	 */
	@Override
	public void coinsRemoved(CoinReceptacle receptacle) {
		event = "The coin receptacle is emptied";
		vml.log(event);
	}
	
	/**
	 * event when the coin receptacle is full
	 * enables the safety
	 */
	@Override
	public void coinsFull(CoinReceptacle receptacle) {
		event = "Coin receptacle full";
		vml.log(event);
		vml.enableSafety(); //enable the safety
	}

	@Override
	public void coinsLoaded(CoinReceptacle receptacle, Coin... coins) {}

	@Override
	public void coinsUnloaded(CoinReceptacle receptacle, Coin... coins) {}
	/******************************End CoinReceptacle Listener******************************/
	
	
	/*****************************Start CoinReturn Listener*********************************/
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins) {
		event = Arrays.toString(coins) + " cent coin is returned";
		vml.log(event);
	}

	@Override
	public void returnIsFull(CoinReturn coinReturn) {
		event = "Coin return is full";
		vml.log(event);
		vml.enableSafety(); //enables safety if there is no more space in the coin return receptacle
	}
	/*****************************End CoinReturn Listener*********************************/
	
	
	/*********************************Start Button Listener*****************************************/
	/***************************************************
	 * Handles the logic of push buttons
	 * If a valid button is pressed, then the buy method in vending machine logic is called
	 **************************************************/
	@Override
	public void pressed(PushButton button) {

		int btnIndex = vml.getButtonList().indexOf(button) ; //Which button did the user press
		
		event = vm.getPopKindName(btnIndex) + " button at button index " + btnIndex + " pressed";
		vml.log(event); //now log the button press event to file
		
		if (btnIndex == -1) { //Unregistered button is pressed
			//Nothing happens for now
		} else { //Valid button gets pressed
			try {
				vml.buy(btnIndex); //Calls the buy transaction method
			} catch (DisabledException e) { //Vending machine is disabled
				event = "The vending machine is currently disabled!";
				vml.display(event);
				vml.enableSafety();
			} catch (CapacityExceededException e) { //Chute becomes full
				event = "The delivery chute is full!";
				vml.display(event);
				vml.enableSafety();
			} catch (EmptyException e) { //Or pop is sold out
				try {
					vml.returnChange(); //still tries to return the change
				} catch (DisabledException e1) {
					event = "The vending machine is currently disabled!";
					vml.display(event);
					vml.enableSafety();
				} catch (CapacityExceededException e1) {
					e1.printStackTrace();
				} catch (EmptyException e1) {
					e1.printStackTrace();
				};
				event = vm.getPopKindName(btnIndex)+ " is sold out!";
				vml.display(event);
			}
		}
	}
	
	/**********************************End Button Listener*****************************************/
	
	
	/********************************Start PopCanRack Listener*************************************/
	//Adding PopCan to PopCanRack
	@Override
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {
		event = "Added " + popCan.getName();
		vml.log(event);
	}
	//Removing PopCan from PopCanRack
	@Override
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		event = "Removed a " + popCan.getName() + " from its rack";
		vml.log(event);
	}

	@Override
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {	}

	@Override
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {}
	
	/**
	 * When a pop can rack is full
	 * enables safety
	 */
	@Override
	public void popCansFull(PopCanRack popCanRack) {
		event = "Pop can rack full";
		vml.log(event);
		vml.enableSafety();
	}
	
	/**
	 * When a pop can rack becomes empty
	 */
	@Override
	public void popCansEmpty(PopCanRack popCanRack) {
		event = "Pop can rack empty";
		vml.log(event);
	}
	
	/*******************************End PopCanRack Listener*************************************/
	
	/********************************Start DeliveryChute Listener********************************/
	@Override
	public void itemDelivered(DeliveryChute chute) {
		//Simulates opening the chute for the customer
		PopCan[] popcans = chute.removeItems();
		event = "Delivered " + Arrays.toString(popcans);
		vml.log(event);
	}

	@Override
	public void doorOpened(DeliveryChute chute) {
		event = "Delivery chute door opened";
		vml.log(event);
	}

	@Override
	public void doorClosed(DeliveryChute chute) {
		event = "Delivery chute door closed";
		vml.log(event);
	}

	@Override
	public void chuteFull(DeliveryChute chute) {
		event = "Delivery chute door full";
		vml.log(event);
		vml.enableSafety(); //enables the safety
		
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
		vml.log(event);
	}

	@Override
	public void deactivated(IndicatorLight light) {
		if (light == vm.getExactChangeLight()) {
			event = "Exact change only light turned off";
		}
		else if (light == vm.getOutOfOrderLight()) {
			event = "Out of order light turned off";
		}
		vml.log(event);		
	}
	/******************************End IndicatorLight Listener********************************/
	
	
	/******************************Start Display Listener********************************/
	@Override
	public void messageChange(Display display, String oldMessage, String newMessage) {
		event = newMessage;
	}
	/******************************End Display Listener********************************/
}
