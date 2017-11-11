package org.lsmr.vending.hardware;

/**
 * A simple device that displays a string. How it does this is not part of the
 * simulation. A very long string might scroll continuously, for example.
 */
public final class Display extends AbstractHardware<DisplayListener> {
    private String currentMessage = null;

    /**
     * Tells the display to start displaying the indicated message. Announces a
     * "messageChange" event to its listeners.
     * 
     * @param newMessage
     *            The message to be displayed. May be null.
     */
    public void display(String newMessage) {
	String oldMessage = this.currentMessage;
	this.currentMessage = newMessage;
	notifyMessageChange(oldMessage, newMessage);
    }

    private void notifyMessageChange(String oldMessage, String newMessage) {
	for(DisplayListener listener : listeners)
	    listener.messageChange(this, oldMessage, newMessage);
    }
}
