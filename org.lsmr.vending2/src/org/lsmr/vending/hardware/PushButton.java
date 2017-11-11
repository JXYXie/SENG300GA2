package org.lsmr.vending.hardware;

/**
 * Represents a simple push button on the vending machine. It ignores the
 * enabled/disabled state.
 */
public final class PushButton extends
        AbstractHardware<PushButtonListener> {
    /**
     * Simulates the pressing of the button. Notifies its listeners of a
     * "pressed" event.
     */
    public void press() {
	notifyPressed();
    }

    private void notifyPressed() {
	for(PushButtonListener listener : listeners)
	    listener.pressed(this);
    }
}
