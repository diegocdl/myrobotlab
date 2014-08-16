package org.myrobotlab.control;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * source modified from:
 * http://bryanesmith.com/docs/drag-and-drop-java-5/DragAndDropPanelsDemo.java
 */
/**
 *
 * @author LunDev (github), Ma. Vo. (MyRobotlab)
 */
public class ServoOrchestratorGUI_middlemiddle_main {

	// References to the panels
	private final ServoOrchestratorGUI_middlemiddle_panel[][] panels;

	// This is the panel that will hold everything.
	private final ServoOrchestratorGUI_middlemiddle_rootpanel rootPanel;

	// "border"-panels
	public final ServoOrchestratorGUI_middlemiddle_panel[] prep;

	// "main"-panel
	private final JPanel middlemiddle;

	/**
	 * <p>
	 * This represents the data that is transmitted in drag and drop.
	 * </p>
	 * <p>
	 * In our limited case with only 1 type of dropped item, it will be a panel
	 * object!
	 * </p>
	 * <p>
	 * Note DataFlavor can represent more than classes -- easily text, images,
	 * etc.
	 * </p>
	 */
	private static DataFlavor dragAndDropPanelDataFlavor = null;

	public ServoOrchestratorGUI_middlemiddle_main(
			final ServoOrchestratorGUI so_ref) {

		middlemiddle = new JPanel();

		// Create the root panel and add to the main panel
		rootPanel = new ServoOrchestratorGUI_middlemiddle_rootpanel(
				ServoOrchestratorGUI_middlemiddle_main.this);
		rootPanel.setLayout(new GridBagLayout());
		middlemiddle.add(rootPanel);

		// Create a list to hold all the panels
		panels = new ServoOrchestratorGUI_middlemiddle_panel[so_ref.sizex][so_ref.sizey];

		// "border"-panels
		prep = new ServoOrchestratorGUI_middlemiddle_panel[getRandomDragAndDropPanels().length
				+ getRandomDragAndDropPanels()[0].length];
		for (int i = 0; i < prep.length; i++) {
			if (i < getRandomDragAndDropPanels()[0].length) {
				final int fi = i;
				prep[i] = new ServoOrchestratorGUI_middlemiddle_panel("channel");
				prep[i].channel_id.setText(prep[i].id + "");
				prep[i].channel_name.setText("Channel " + (i + 1));
				prep[i].channel_settings
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent ae) {
								so_ref.externalcall_loadsettings(fi);
							}
						});
				prep[i].setBackground(Color.green);
			} else {
				prep[i] = new ServoOrchestratorGUI_middlemiddle_panel(
						"timesection");
				prep[i].timesection_id.setText(prep[i].id + "");
				prep[i].timesection_headline.setText("TIMEUNIT "
						+ (i - getRandomDragAndDropPanels().length + 1));
				prep[i].setBackground(Color.green);
			}
		}
		prep[getRandomDragAndDropPanels()[0].length].setBackground(Color.red);

		// refresh the gui
		relayout();
	}

	// Button
	public void externalcall_addPanel() {
		// Add the new panel to the array (on the first free space)
		// after that - relayout!
		boolean found = false;
		for (int i1 = 0; i1 < getRandomDragAndDropPanels().length; i1++) {
			if (found) {
				break;
			}
			for (int i2 = 0; i2 < getRandomDragAndDropPanels()[i1].length; i2++) {
				if (found) {
					break;
				}
				if (getRandomDragAndDropPanels()[i1][i2] == null) {
					ServoOrchestratorGUI_middlemiddle_panel p = new ServoOrchestratorGUI_middlemiddle_panel(
							"servo");
					p.servo_id.setText(p.id + "");
					p.servo_channelid.setText("CH" + (i2 + 1));
					// TODO - add remaining attributes
					// TODO - make the channelid independent of the y-position
					// (i2)
					p.setBackground(Color.yellow);
					getRandomDragAndDropPanels()[i1][i2] = p;
					found = true;
				}
			}
		}

		// Relayout the panels.
		relayout();
	}

	public JPanel externalcall_getmiddlemiddle() {
		return middlemiddle;
	}

	/**
	 * <p>
	 * Removes all components from our root panel and re-adds them.
	 * </p>
	 * <p>
	 * This is important for two things:
	 * </p>
	 * <ul>
	 * <li>Adding a new panel (user clicks on button)</li>
	 * <li>Re-ordering panels (user drags and drops a panel to acceptable drop
	 * target region)</li>
	 * </ul>
	 */
	public void relayout() {

		// Create the constraints, and go ahead and set those
		// that don't change for components
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 0, 0);

		// Clear out all previously added items
		rootPanel.removeAll();

		// Add the panels, if any & the "border"-panels
		for (int i1 = 0; i1 < getRandomDragAndDropPanels().length + 1; i1++) {
			for (int i2 = 0; i2 < getRandomDragAndDropPanels()[0].length + 1; i2++) {
				if (i1 == 0 || i2 == 0) {
					if (i1 != 0 || i2 != 0) {
						gbc.gridx = i1;
						gbc.gridy = i2;
						int num = 0;
						if (i1 == 0) {
							num = i2 - 1;
						} else {
							num = getRandomDragAndDropPanels().length - 1 + i1;
						}
						rootPanel.add(prep[num], gbc);
					}
				} else {
					ServoOrchestratorGUI_middlemiddle_panel p = getRandomDragAndDropPanels()[i1 - 1][i2 - 1];
					gbc.gridx = i1;
					gbc.gridy = i2;
					if (p != null) {
						rootPanel.add(p, gbc);
					}
				}
			}
		}

		middlemiddle.validate();
		middlemiddle.repaint();
	}

	/**
	 * <p>
	 * Returns (creating, if necessary) the DataFlavor representing
	 * ServoOrchestratorGUI_middlemiddle_panel
	 * </p>
	 *
	 * @return
	 */
	public static DataFlavor getDragAndDropPanelDataFlavor() throws Exception {
		// the commented (first one) is original and first it wotrked, then not
		// (???)
		// the second (uncommented) is my repair - don't know (???)
		// FIXME - maybe (???)
		// TODO - maybe a fix (???)

		// Lazy load/create the flavor
		if (dragAndDropPanelDataFlavor == null) {
			// dragAndDropPanelDataFlavor = new
			// DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
			// ";class=RandomDragAndDropPanel");
			dragAndDropPanelDataFlavor = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType);
		}

		return dragAndDropPanelDataFlavor;
	}

	/**
	 * <p>
	 * Returns the Array of user-added panels.
	 * </p>
	 * <p>
	 * Note that for drag and drop, these will be cleared, and the panels will
	 * be added back in the correct order!
	 * </p>
	 *
	 * @return
	 */
	public ServoOrchestratorGUI_middlemiddle_panel[][] getRandomDragAndDropPanels() {
		return panels;
	}
}