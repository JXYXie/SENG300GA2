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
	
	public VendingListener(VendingMachine vm, VendingMachineLogic vml) {
		this.vm = vm;
		this.vml = vml;
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
		event = "Invalid coin inserted";
		vml.log(event);
	}
	/*******************************End CoinSlot Listener*************************************/
	
	
	/***********************************Start CoinRack Listener**************************************/
	@Override
	public void coinsFull(CoinRack rack) {
		event = "A coin rack is full";
		vml.log(event);
	}

	@Override
	public void coinsEmpty(CoinRack rack) {
		event = "A coin rack is empty";
		vml.log(event);
	}

	@Override
	public void coinAdded(CoinRack rack, Coin coin) {
		event = coin.getValue() + " cent coin has been added to its rack";
		vml.log(event);
	}

	@Override
	public void coinRemoved(CoinRack rack, Coin coin) {
		event = coin.getValue() + "coin has been removed from its rack";
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

	@Override
	public void coinsRemoved(CoinReceptacle receptacle) {
		event = "The coin receptacle is emptied";
		vml.log(event);
	}

	@Override
	public void coinsFull(CoinReceptacle receptacle) {
		event = "Coin receptacle full";
		vml.log(event);
		vml.enableSafety();
	}

	@Override
	public void coinsLoaded(CoinReceptacle receptacle, Coin... coins) {}

	@Override
	public void coinsUnloaded(CoinReceptacle receptacle, Coin... coins) {}
	/******************************End CoinReceptacle Listener******************************/
	
	
	/*****************************Start CoinReturn Listener*********************************/
	@Override
	public void coinsDelivered(CoinReturn coinReturn, Coin[] coins) {
		event = Arrays.toString(coins) + " coin(s) delivered to coin return";
		vml.log(event);
	}

	@Override
	public void returnIsFull(CoinReturn coinReturn) {
		event = "Coin return is full";
		vml.log(event);
		vml.enableSafety();
	}
	/*****************************End CoinReturn Listener*********************************/
	/*********************************Start Button Listener*****************************************/
	
	/***************************************************
	 * Handles the logic of selection buttons
	 **************************************************/
	@Override
	public void pressed(PushButton button) {

		int btnIndex = vml.getButtonList().indexOf(button) ; //Which button did the user press
		
		event = vm.getPopKindName(btnIndex) + " button at button index " + btnIndex + " pressed";
		vml.log(event);
		
		if (btnIndex == -1) { //Unregistered button is pressed
			//Nothing happens for now
		} else {
			try {
				vml.buy(btnIndex);
			} catch (DisabledException e) {
				vml.display("The vending machine is currently disabled!");
				vml.enableSafety();
			} catch (CapacityExceededException e) {
				vml.display("The delivery chute is full!");
				vml.enableSafety();
			} catch (EmptyException e) {
				vml.display(vm.getPopKindName(btnIndex)+ " is sold out!");
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
		event = "Removed a " + popCan.getName() + "from its rack";
		vml.log(event);
	}

	@Override
	public void popCansLoaded(PopCanRack rack, PopCan... popCans) {	}

	@Override
	public void popCansUnloaded(PopCanRack rack, PopCan... popCans) {}

	@Override
	public void popCansFull(PopCanRack popCanRack) {
		event = "Pop can rack full";
		vml.log(event);
		vml.enableSafety();
	}

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
		event = "Item Delivered";
		vml.log(event);
		chute.removeItems();
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
