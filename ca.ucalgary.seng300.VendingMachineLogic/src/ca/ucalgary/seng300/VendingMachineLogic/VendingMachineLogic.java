/****************************************************
 * SENG 300 Group Assignment 2
 * This class handles all the logic operations that occur in a vending machine,
 * specifically, this class handles the calling of methods when money is inserted
 * and buttons are pressed as well as the listening of their respective events
 ****************************************************/
package ca.ucalgary.seng300.VendingMachineLogic;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.vending.*;
import org.lsmr.vending.hardware.*;

public class VendingMachineLogic implements CoinSlotListener, PopCanRackListener, PushButtonListener {

	private VendingMachine vm;
	private int userCredit;
	private List<PushButton> buttonList = new ArrayList<>();
	private String event;

	/********************
	 * Constructor
	 * instantiates and initializes all the relevant hardware
	 * and registers listeners
	 *******************/
	public VendingMachineLogic(VendingMachine vm) {

		this.vm = vm;
		userCredit = 0;
		//For loop to iterate through all the available buttons
		for (int i = 0; i < vm.getNumberOfSelectionButtons(); i++) {
				PushButton sb = vm.getSelectionButton(i); //Instantiates the hardware
				sb.register(this); //And registers the relevant listeners
				buttonList.add(sb); //Stores into an ArrayList for later use
		}
		//Iterate through all available pop can racks
		for (int i = 0; i < vm.getNumberOfPopCanRacks(); i++) {
				PopCanRack pcr = vm.getPopCanRack(i); //Instantiates the hardware
				pcr.register(this); //Registers the relevant listeners
		}

		vm.getCoinSlot().register(this);

	}

	/**
	 * @return the current event
	 * Useful for printing things to a log file
	 */
	public String getEvent() {
			return event;
	}
	/**
	 *
	 * @return how much credit is in vending machine
	 */
	public int getCredit() {
			return userCredit;
	}

	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		//Leave Empty
	}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		//Leave Empty
	}

	/*************************
	 * Handles the logic of selection buttons
	 *************************/
	@Override
	public void pressed(PushButton button) {

		int btnIndex = buttonList.indexOf(button) ; //Which button did the user press

		if (btnIndex == -1) { //Unregistered button is pressed
			//Nothing happens for now
		}

		int cost = vm.getPopKindCost(btnIndex);

		if (cost > userCredit) { //Not enough money!!!
			//Nothing happens for now
		} else {
			PopCanRack pr = vm.getPopCanRack(btnIndex); //Matches the button with the pop rack
			try {
					pr.dispensePopCan(); //Dispenses the relevant pop
					vm.getCoinReceptacle().storeCoins(); //Stores the change
					userCredit -= cost; //Deduct the pay from the available credit
			} catch (DisabledException | CapacityExceededException e) {
					throw new SimulationException(e);
			} catch (EmptyException e2) {
					event = "Pop is sold out!"; //Set the event for sold-out
			}
		}
	}

	@Override
	public void popCanAdded(PopCanRack popCanRack, PopCan popCan) {
		event = "Added " + popCan.getName();
	}

	@Override
	public void popCanRemoved(PopCanRack popCanRack, PopCan popCan) {
		event = "Removed a " + popCan.getName();
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
		//Leave empty
	}

	@Override
	public void popCansEmpty(PopCanRack popCanRack) {
		//Leave empty
	}

	@Override
	public void validCoinInserted(CoinSlot slot, Coin coin) {
		userCredit += coin.getValue(); //Increment the credit when valid coins are inserted
		event = "Inserted $" + coin.getValue(); //Updates event
	}

	@Override
	public void coinRejected(CoinSlot slot, Coin coin) {
		event = "Invalid coin inserted";
	}


}
