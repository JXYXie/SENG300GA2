package org.lsmr.vending.hardware;

import org.lsmr.vending.PopCan;

/**
 * A simple interface to allow a device to communicate with another device that
 * accepts pop cans.
 */
public interface PopCanAcceptor {
    /**
     * Instructs the device to take the pop can as input.
     * 
     * @param popCan
     *            The pop can to be taken as input.
     * @throws CapacityExceededException
     *             if the device does not have enough space for the pop can.
     * @throws DisabledException
     *             if the device is currently disabled.
     */
    public void acceptPopCan(PopCan popCan) throws CapacityExceededException, DisabledException;

    /**
     * Checks whether the device has enough space to expect one more item. If
     * this method returns true, an immediate call to acceptPopCan should not
     * throw CapacityExceededException, unless an asynchronous addition has
     * occurred in the meantime.
     * 
     * @return true if there is space, false if there is not space
     */
    public boolean hasSpace();
}
