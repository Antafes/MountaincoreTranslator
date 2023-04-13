package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.Configuration;
import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.gui.event.FileOpenedEvent;
import antafes.mountaincoreTranslator.gui.event.FileOpenedListener;
import antafes.mountaincoreTranslator.gui.event.OpenActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseWindow extends JFrame
{
    private final Configuration configuration;
    private TranslationPanel panel;

    public BaseWindow() throws HeadlessException
    {
        this.configuration = Configuration.getInstance();

        this.initComponents();
        this.init();
    }

    private void init()
    {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(BaseWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setLocation(this.configuration.getWindowLocation());
        this.setExtendedState(this.configuration.getExtendedState());
    }

    private void closeProgramme()
    {
        this.configuration.setWindowLocation(this.getLocationOnScreen());
        this.configuration.setExtendedState(this.getExtendedState());
        this.configuration.saveProperties();
        System.exit(0);
    }

    private void initComponents()
    {
        this.setTitle("Mountaincore Translator");

        this.addMenu();
        this.panel = new TranslationPanel();
        this.registerEvents();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(this.panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(this.panel, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
        );

        pack();
    }

    private void addMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        JMenuItem newMenuItem = new JMenuItem();
        JMenuItem openMenuItem = new JMenuItem();
        JMenuItem saveMenuItem = new JMenuItem();
        JMenuItem closeMenuItem = new JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fileMenu.setText("File");

//        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
//        newMenuItem.setText("New");
//        newMenuItem.addActionListener(this::newMenuItemActionPerformed);
//        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new OpenActionListener(this));
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setText("Save");
//        saveMenuItem.addActionListener(this::saveMenuItemActionPerformed);
        saveMenuItem.setEnabled(false);
        fileMenu.add(saveMenuItem);

        closeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        closeMenuItem.setText("Quit");
        closeMenuItem.addActionListener(this::closeMenuItemActionPerformed);
        fileMenu.add(closeMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Action performed event for the close menu entry.
     *
     * @param evt Event object
     */
    private void closeMenuItemActionPerformed(ActionEvent evt) {
//        if (this.checkForUnsavedCharacters()) {
//            this.showUnsavedCharactersDialog();
//        } else {
            this.closeProgramme();
//        }
    }

    private void registerEvents()
    {
        MountaincoreTranslator.getDispatcher().addListener(
            FileOpenedEvent.class,
            new FileOpenedListener(this::fileOpened)
        );
    }

    private void fileOpened(FileOpenedEvent fileOpenedEvent)
    {
        ShowWaitAction waitAction = new ShowWaitAction(this);
        waitAction.show(aVoid -> {
            this.panel.setTranslations(fileOpenedEvent.getTranslationMap());

            return null;
        });
    }
}
