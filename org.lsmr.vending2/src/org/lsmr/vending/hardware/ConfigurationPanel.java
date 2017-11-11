package org.lsmr.vending.hardware;

/**
 * A panel on the interior of the machine that a maintenance person can, in
 * principle, use to set the product costs. The logic for interpreting button
 * presses is not built into the device.
 * <p>
 * The panel has 37 buttons for data entry (which can be used to code a small
 * set of numbers and characters, plus a "shift" key). It has an additional
 * designated button to indicate "Enter". It also has a small display to provide feedback
 * to the maintainer.
 * <p>
 * Note that no listeners and no code interpreter are added automatically.
 * Different code interpreters will be needed for different kinds of vending
 * machine.
 */
public class ConfigurationPanel {
    private PushButton[] buttons = new PushButton[37];
    private PushButton enterButton = new PushButton();
    private Display internalDisplay = new Display();

    /**
     * Accesses a particular button on the panel.
     * 
     * @param index
     *            The index of the button to access, between 0 and 37.
     * @return The button at the indicated index.
     * @throws ArrayIndexOutOfBoundsException
     *             if the index is &lt;0 or &gt;37.
     */
    public PushButton getButton(int index) {
	return buttons[index];
    }

    /**
     * Accesses the specialized button representing "enter".
     * 
     * @return The enter button.
     */
    public PushButton getEnterButton() {
	return enterButton;
    }

    /**
     * Accesses the internal display of the panel.
     * 
     * @return The internal display.
     */
    public Display getDisplay() {
	return internalDisplay;
    }
}
