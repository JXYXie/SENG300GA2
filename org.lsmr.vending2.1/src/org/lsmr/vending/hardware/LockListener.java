package org.lsmr.vending.hardware;

/**
 * Listens for events emanating from a lock.
 */
public interface LockListener extends AbstractHardwareListener {
    /**
     * An event that announces that the indicated lock has been locked.
     * 
     * @param lock
     *            The device on which the event occurred.
     */
    void locked(Lock lock);

    /**
     * An event that announces that the indicated lock has been unlocked.
     * 
     * @param lock
     *            The device on which the event occurred.
     */
    void unlocked(Lock lock);
}
