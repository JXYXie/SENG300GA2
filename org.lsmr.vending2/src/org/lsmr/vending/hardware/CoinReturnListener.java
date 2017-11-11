package org.lsmr.vending.hardware;

import org.lsmr.vending.Coin;

/**
 * Listens for events emanating from a delivery chute.
 */
public interface CoinReturnListener extends AbstractHardwareListener {
    /**
     * Indicates that coins have been delivered to the coin return.
     * 
     * @param coinReturn
     *            The device on which the event occurred.
     * @param coins
     *            The coins that are being delivered.
     */
    void coinsDelivered(CoinReturn coinReturn, Coin[] coins);

    /**
     * Indicates that the coin return will not be able to hold any more
     * coins.
     * 
     * @param coinReturn
     *            The device on which the event occurred.
     */
    void returnIsFull(CoinReturn coinReturn);
}
