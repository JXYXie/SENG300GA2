package org.lsmr.vending.hardware;

import java.util.ArrayList;
import java.util.List;

import org.lsmr.vending.Coin;

/**
 * A temporary storage device for coins. A coin receptacle can be disabled to
 * prevent more coins from being placed within it. A coin receptacle has a
 * maximum capacity of coins that can be stored within it. A coin receptacle can
 * be connected to specialized channels depending on the denomination of each
 * coin (usually used for storing to coin racks) and another for the coin
 * return.
 */
public final class CoinReturn extends AbstractHardware<CoinReturnListener> implements CoinAcceptor {
    private ArrayList<Coin> coinsReturned = new ArrayList<>();
    private int maxCapacity;

    /**
     * Creates a coin return with the indicated capacity.
     * 
     * @param capacity
     *            The maximum number of coins that can be stored. Must be
     *            positive.
     * @throws SimulationException
     *             if the capacity is not a positive integer.
     */
    public CoinReturn(int capacity) {
	if(capacity <= 0)
	    throw new SimulationException("Capacity must be positive: " + capacity);

	maxCapacity = capacity;
    }

    /**
     * Returns the maximum capacity of this coin receptacle. Causes no events.
     * 
     * @return The maximum number of items that this device can store.
     */
    public int getCapacity() {
	return maxCapacity;
    }

    /**
     * The current number of coins stored.
     * 
     * @return The current count. Will be non-negative.
     */
    public int size() {
	return coinsReturned.size();
    }


    /**
     * Unloads coins from the coin return, to simulate direct, physical
     * unloading. Causes a "coinsUnloaded" event to be announced.
     * 
     * @return A list of coins unloaded. None will be null. The list can be
     *         empty.
     */
    public List<Coin> unload() {
	List<Coin> result = new ArrayList<>(coinsReturned);
	coinsReturned.clear();
	return result;
    }

    /**
     * Causes the indicated coin to be added to the coin return if it has space.
     * A successful addition causes a "coinAdded" event to be announced to its
     * listeners. If a successful addition causes the receptacle to become full,
     * it will also announce a "returnIsFull" event to its listeners.
     * 
     * @throws CapacityExceededException
     *             if the receptacle has no space.
     * @throws DisabledException
     *             if the receptacle is disabled.
     */
    public void acceptCoin(Coin coin) throws CapacityExceededException, DisabledException {
	if(isDisabled())
	    throw new DisabledException();

	if(coinsReturned.size() >= maxCapacity)
	    throw new CapacityExceededException();

	coinsReturned.add(coin);

	notifyCoinsDelivered(coin);

	if(coinsReturned.size() >= maxCapacity)
	    notifyReturnIsFull();
    }

    /**
     * Returns whether this coin receptacle has enough space to accept at least
     * one more coin. Causes no events.
     */
    @Override
    public boolean hasSpace() {
	return coinsReturned.size() < maxCapacity;
    }

    private void notifyCoinsDelivered(Coin... coins) {
	for(CoinReturnListener listener : listeners)
	    listener.coinsDelivered(this, coins);
    }

    private void notifyReturnIsFull() {
	for(CoinReturnListener listener : listeners)
	    listener.returnIsFull(this);
    }
}
