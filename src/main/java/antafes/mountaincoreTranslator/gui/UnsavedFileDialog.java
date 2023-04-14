package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.gui.event.AddEscapeCloseEvent;
import antafes.mountaincoreTranslator.gui.event.CloseProgramEvent;
import antafes.mountaincoreTranslator.gui.event.TriggerSaveFileEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnsavedFileDialog extends JDialog
{
    private JTextArea infoTextArea;
    private JButton saveButton;
    private JButton exitButton;
    private JButton cancelButton;

    public UnsavedFileDialog(Frame owner)
    {
        super(owner, true);

        this.setResizable(false);
        this.setSize(new Dimension(400, 150));

        this.initComponents();
        this.init();
    }

    private void initComponents()
    {
        this.infoTextArea = new JTextArea();
        this.saveButton = new JButton();
        this.exitButton = new JButton();
        this.cancelButton = new JButton();

        this.infoTextArea.setLineWrap(true);
        this.infoTextArea.setWrapStyleWord(true);
        this.infoTextArea.setEditable(false);
        this.infoTextArea.setOpaque(false);

        this.saveButton.setMnemonic('s');
        this.exitButton.setMnemonic('d');
        this.cancelButton.setMnemonic('c');

        this.cancelButton.addActionListener(this::cancelButtonActionPerformed);
        this.exitButton.addActionListener(this::exitButtonActionPerformed);
        this.saveButton.addActionListener(this::saveButtonActionPerformed);

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagLayout contentPaneLayout = new GridBagLayout();
        this.getContentPane().setLayout(contentPaneLayout);

        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 20;
        constraints.fill = GridBagConstraints.BOTH;
        this.getContentPane().add(this.infoTextArea, constraints);

        constraints.gridwidth = 1;
        constraints.gridy = 1;
        constraints.ipady = 0;
        this.getContentPane().add(this.saveButton, constraints);

        constraints.gridx = 1;
        this.getContentPane().add(this.exitButton, constraints);

        constraints.gridx = 2;
        this.getContentPane().add(this.cancelButton, constraints);
    }

    private void init() {
        // Set look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        AddEscapeCloseEvent event = new AddEscapeCloseEvent();
        event.setDialog(this);
        MountaincoreTranslator.getDispatcher().dispatch(event);
        this.setFieldTexts();
    }

    private void setFieldTexts()
    {
        this.setTitle("Unsaved file");
        this.infoTextArea.setText("There are changes on the opened translation file. Do you want to save them now?");
        this.saveButton.setText("Save");
        this.exitButton.setText("Don't save");
        this.cancelButton.setText("Cancel");
    }

    private void cancelButtonActionPerformed(ActionEvent actionEvent)
    {
        this.closeDialog();
    }

    private void exitButtonActionPerformed(ActionEvent actionEvent)
    {
        MountaincoreTranslator.getDispatcher().dispatch(new CloseProgramEvent());
    }

    private void saveButtonActionPerformed(ActionEvent actionEvent)
    {
        this.closeDialog();
        MountaincoreTranslator.getDispatcher().dispatch(new TriggerSaveFileEvent());
    }

    private void closeDialog()
    {
        this.setVisible(false);
        this.dispose();
    }
}
