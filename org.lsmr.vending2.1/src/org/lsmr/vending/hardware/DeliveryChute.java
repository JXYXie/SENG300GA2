package org.lsmr.vending.hardware;

import java.util.ArrayList;

import org.lsmr.vending.PopCan;

/**
 * Represents a simple delivery chute device. The delivery chute has a finite
 * capacity of objects (pop cans or coins) that it can hold. This is obviously
 * not a realistic element of the simulation, but sufficient here.
 */
public final class DeliveryChute extends AbstractHardware<DeliveryChuteListener> implements PopCanAcceptor {
    private ArrayList<PopCan> chute = new ArrayList<PopCan>();
    private int maxCapacity;

    /**
     * Creates a delivery cute with the indicated maximum capacity of pop cans.
     * 
     * @param capacity
     *            The maximum number of items that the delivery chute can
     *            contain. Must be positive.
     * @throws SimulationException
     *             If the capacity is not a positive integer.
     */
    public DeliveryChute(int capacity) {
	if(capacity <= 0)
	    throw new SimulationException("Capacity must be a positive value: " + capacity);

	this.maxCapacity = capacity;
    }

    /**
     * Accesses the current number of items in the chute.
     * 
     * @return The current number of items. Cannot be negative.
     */
    public int size() {
	return chute.size();
    }

    /**
     * Returns the maximum capacity of this delivery chute in number of pop cans
     * that it can hold. Causes no events.
     * 
     * @return The maximum number of items that can be in the chute. Cannot be
     *         negative.
     */
    public int getCapacity() {
	return maxCapacity;
    }

    /**
     * Tells this delivery chute to deliver the indicated pop can. If the
     * delivery is successful, an "itemDelivered" event is announced to its
     * listeners. If the successful delivery causes the chute to become full, a
     * "chuteFull" event is announced to its listeners.
     * 
     * @throws CapacityExceededException
     *             if the chute is already full and the pop can cannot be
     *             delivered.
     * @throws DisabledException
     *             if the chute is currently disabled.
     */
    @Override
    public void acceptPopCan(PopCan popCan) throws CapacityExceededException, DisabledException {
	if(isDisabled())
	    throw new DisabledException();

	if(chute.size() >= maxCapacity)
	    throw new CapacityExceededException();

	chute.add(popCan);

	notifyItemDelivered();

	if(chute.size() >= maxCapacity)
	    notifyChuteFull();
    }

    /**
     * Simulates the opening of the door of the delivery chute and the removal
     * of all items therein. Announces a "doorOpened" event to its listeners
     * before the items are removed, and a "doorClosed" event after the items
     * are removed. Disabling the chute does not prevent this.
     * 
     * @return The items that were in the delivery chute.
     */
    public PopCan[] removeItems() {
	notifyDoorOpened();
	PopCan[] items = new PopCan[chute.size()];
	chute.toArray(items);
	chute.clear();
	notifyDoorClosed();
	return items;
    }

    /**
     * Determines whether this delivery chute has space for at least one more
     * item. Causes no events.
     */
    @Override
    public boolean hasSpace() {
	return chute.size() < maxCapacity;
    }

    private void notifyItemDelivered() {
	for(DeliveryChuteListener listener : listeners)
	    listener.itemDelivered(this);
    }

    private void notifyDoorOpened() {
	for(DeliveryChuteListener listener : listeners)
	    listener.doorOpened(this);
    }

    private void notifyDoorClosed() {
	for(DeliveryChuteListener listener : listeners)
	    listener.doorClosed(this);
    }

    private void notifyChuteFull() {
	for(DeliveryChuteListener listener : listeners)
	    listener.chuteFull(this);
    }
}
