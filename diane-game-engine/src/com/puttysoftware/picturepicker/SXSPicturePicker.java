package com.puttysoftware.picturepicker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.gui.GameImage;
import com.puttysoftware.diane.gui.MainContent;
import com.puttysoftware.diane.internal.BufferedImageIcon;

public final class SXSPicturePicker {
  /**
   * @version 1.0.0
   */
  // Fields
  private GameImage[] choices;
  private JLabel[] choiceArray;
  private final MainContent pickerMainContent;
  private final MainContent choiceMainContent;
  private final MainContent radioMainContent;
  private final MainContent choiceRadioMainContent;
  private final ButtonGroup radioGroup;
  private JRadioButton[] radioButtons;
  private final JScrollPane scrollPane;
  int index;
  private final EventHandler handler;
  private final int stackCount;

  // Constructor
  public SXSPicturePicker(final GameImage[] pictures, final boolean[] enabled,
      final int newStackCount) {
    this.stackCount = newStackCount;
    this.handler = new EventHandler();
    this.pickerMainContent = new MainContent();
    this.pickerMainContent.setLayout(new BorderLayout());
    this.choiceMainContent = new MainContent();
    this.radioMainContent = new MainContent();
    this.radioGroup = new ButtonGroup();
    this.choiceRadioMainContent = new MainContent();
    this.choiceRadioMainContent.setLayout(new BorderLayout());
    this.choiceRadioMainContent.add(this.radioMainContent, BorderLayout.WEST);
    this.choiceRadioMainContent.add(this.choiceMainContent,
        BorderLayout.CENTER);
    this.scrollPane = new JScrollPane(this.choiceRadioMainContent);
    this.scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    this.scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.pickerMainContent.add(this.scrollPane, BorderLayout.CENTER);
    this.updatePicker(pictures, enabled);
    this.index = 0;
  }

  // Methods
  public MainContent getPicker() {
    return this.pickerMainContent;
  }

  public void disablePicker() {
    this.pickerMainContent.setEnabled(false);
    for (final JRadioButton radioButton : this.radioButtons) {
      radioButton.setEnabled(false);
    }
  }

  public void enablePicker() {
    this.pickerMainContent.setEnabled(true);
    for (final JRadioButton radioButton : this.radioButtons) {
      radioButton.setEnabled(true);
    }
  }

  public void updatePicker(final GameImage[] newImages,
      final boolean[] enabled) {
    this.choices = newImages;
    this.choiceMainContent.removeAll();
    this.radioMainContent.removeAll();
    this.radioButtons = new JRadioButton[this.choices.length];
    int rows = this.choices.length / this.stackCount;
    final int extra = this.choices.length % this.stackCount;
    if (extra != 0) {
      rows++;
    }
    this.choiceMainContent.setLayout(new GridLayout(rows, this.stackCount));
    this.radioMainContent.setLayout(new GridLayout(rows, this.stackCount));
    this.choiceArray = new JLabel[this.choices.length];
    for (int x = 0; x < this.choices.length; x++) {
      this.choiceArray[x] = new JLabel("", //$NON-NLS-1$
          new BufferedImageIcon(this.choices[x]), SwingConstants.LEFT);
      this.choiceArray[x].setOpaque(true);
      this.choiceMainContent.add(this.choiceArray[x]);
      this.radioButtons[x] = new JRadioButton();
      this.radioButtons[x].setHorizontalAlignment(SwingConstants.CENTER);
      this.radioButtons[x].setOpaque(true);
      this.radioButtons[x].setActionCommand(Integer.valueOf(x).toString());
      this.radioGroup.add(this.radioButtons[x]);
      this.radioButtons[x].addActionListener(this.handler);
      this.radioButtons[x].setEnabled(enabled[x]);
      this.radioMainContent.add(this.radioButtons[x]);
    }
    for (int x = 0; x < this.choices.length; x++) {
      if (enabled[x]) {
        this.radioButtons[x].setSelected(true);
        this.index = x;
        break;
      }
    }
  }

  public void updatePickerLayout(final int maxHeight) {
    final int newPreferredWidth = this.pickerMainContent.getLayout()
        .preferredLayoutSize(this.pickerMainContent).width
        + this.scrollPane.getVerticalScrollBar().getWidth();
    final int newPreferredHeight = Math.min(maxHeight, this.pickerMainContent
        .getLayout().preferredLayoutSize(this.pickerMainContent).height);
    this.pickerMainContent
        .setPreferredSize(new Dimension(newPreferredWidth, newPreferredHeight));
  }

  public void selectLastPickedChoice(final int lastPicked) {
    this.radioButtons[lastPicked].setSelected(true);
    this.index = lastPicked;
  }

  /**
   *
   * @return the index of the picture picked
   */
  public int getPicked() {
    return this.index;
  }

  private class EventHandler implements ActionListener {
    public EventHandler() {
      // Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
      final String cmd = e.getActionCommand();
      // A radio button
      SXSPicturePicker.this.index = Integer.parseInt(cmd);
    }
  }
}
