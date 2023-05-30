package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.Configuration;
import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.gui.event.*;
import antafes.mountaincoreTranslator.service.FileHandlerService;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseWindow extends JFrame
{
    private final Configuration configuration;
    private TranslationPanel panel;
    private String filename;
    private JMenuItem saveMenuItem;
    private JMenuItem searchMenuItem;
    private String searchValue;
    private JMenu recentMenu;

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

    private void closeProgram()
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
                .addComponent(this.panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
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
        JMenu editMenu = new JMenu();
        JMenuItem newMenuItem = new JMenuItem();
        JMenuItem openMenuItem = new JMenuItem();
        recentMenu = new JMenu();
        this.saveMenuItem = new JMenuItem();
        JMenuItem closeMenuItem = new JMenuItem();
        this.searchMenuItem = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fileMenu.setText("File");
        fileMenu.setMnemonic('f');

        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new NewActionListener(this));
        newMenuItem.setMnemonic('n');
        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new OpenActionListener(this));
        openMenuItem.setMnemonic('o');
        fileMenu.add(openMenuItem);

        this.recentMenu.setText("Last opened");
        this.recentMenu.setMnemonic('l');

        if (this.configuration.getLastOpenedFiles().size() == 0) {
            this.recentMenu.setEnabled(false);
        } else {
            this.addLastOpenedFileEntries();
        }

        fileMenu.add(this.recentMenu);

        this.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        this.saveMenuItem.setText("Save");
        this.saveMenuItem.addActionListener(new SaveActionListener(this));
        this.saveMenuItem.setEnabled(false);
        this.saveMenuItem.setMnemonic('s');
        fileMenu.add(this.saveMenuItem);

        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        closeMenuItem.setText("Quit");
        closeMenuItem.addActionListener(this::closeMenuItemActionPerformed);
        closeMenuItem.setMnemonic('q');
        fileMenu.add(closeMenuItem);

        editMenu.setText("Edit");
        editMenu.setMnemonic('e');

        this.searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        this.searchMenuItem.setText("Search");
        this.searchMenuItem.addActionListener(this::searchMenuItemActionPerformed);
        this.searchMenuItem.setMnemonic('s');
        this.searchMenuItem.setEnabled(false);
        editMenu.add(this.searchMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }

    private void searchMenuItemActionPerformed(ActionEvent actionEvent)
    {
        this.searchValue = JOptionPane.showInputDialog(this, "Search:", this.searchValue);
        SearchEvent event = new SearchEvent();
        event.setSearchValue(this.searchValue);
        MountaincoreTranslator.getDispatcher().dispatch(event);
    }

    /**
     * Action performed event for the close menu entry.
     *
     * @param evt Event object
     */
    private void closeMenuItemActionPerformed(ActionEvent evt) {
        UnsavedFileCheckEvent event = new UnsavedFileCheckEvent();
        MountaincoreTranslator.getDispatcher().dispatch(event);

        if (event.isFileUnsaved()) {
            this.showUnsavedFileDialog();
        } else {
            MountaincoreTranslator.getDispatcher().dispatch(new CloseProgramEvent());
        }
    }

    private void registerEvents()
    {
        MountaincoreTranslator.getDispatcher().addListener(
            CloseProgramEvent.class,
            new CloseProgramListener((event) -> this.closeProgram())
        );
        MountaincoreTranslator.getDispatcher().addListener(
            FileOpenedEvent.class,
            new FileOpenedListener(this::fileOpened)
        );
        MountaincoreTranslator.getDispatcher().addListener(
            SaveFileEvent.class,
            new SaveFileListener(this::saveFile)
        );
        MountaincoreTranslator.getDispatcher().addListener(
            TriggerSaveFileEvent.class,
            new TriggerSaveFileListener((event) -> this.saveMenuItem.doClick())
        );
        MountaincoreTranslator.getDispatcher().addListener(
            AddEscapeCloseEvent.class,
            new AddEscapeCloseListener((event) -> this.installEscapeCloseOperation(event.getDialog()))
        );
    }

    private void fileOpened(@NonNull FileOpenedEvent fileOpenedEvent)
    {
        this.filename = fileOpenedEvent.getFilename();
        ShowWaitAction waitAction = new ShowWaitAction(this);
        waitAction.show(aVoid -> {
            this.panel.setTranslations(fileOpenedEvent.getTranslationMap());
            this.saveMenuItem.setEnabled(true);
            this.searchMenuItem.setEnabled(true);

            return null;
        });
        this.addLastOpenedFileEntries();
    }

    private void saveFile(@NonNull SaveFileEvent saveFileEvent)
    {
        saveFileEvent.setFilename(this.filename);
    }

    private void showUnsavedFileDialog() {
        UnsavedFileDialog dialog = new UnsavedFileDialog(this);
        int x,
            y,
            width = dialog.getWidth(),
            height = dialog.getHeight();

        x = this.configuration.getWindowLocation().x + (this.getWidth() / 2 - width / 2);
        y = this.configuration.getWindowLocation().y + (this.getHeight() / 2 - height / 2);

        dialog.setBounds(x, y, width, height);
        dialog.setVisible(true);
    }

    /**
     * Install a dialog wide escape handler.
     *
     * @param dialog The dialog element to close
     */
    public void installEscapeCloseOperation(@NonNull JDialog dialog) {
        Action dispatchClosing = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dialog.dispatchEvent(new WindowEvent(
                    dialog, WindowEvent.WINDOW_CLOSING
                ));
            }
        };
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            escapeStroke, dispatchWindowClosingActionMapKey
        );
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
    }

    private void addLastOpenedFileEntries()
    {
        this.recentMenu.setEnabled(true);
        this.recentMenu.removeAll();
        ArrayList<String> files = new ArrayList<>(this.configuration.getLastOpenedFiles());
        Collections.reverse(files);
        files.forEach((file) -> {
            JMenuItem lastOpened = new JMenuItem();
            File fileObj = new File(file);

            lastOpened.setText(fileObj.getName());
            lastOpened.addActionListener(e -> {
                FileHandlerService service = new FileHandlerService(fileObj.getPath());
                FileOpenedEvent event = new FileOpenedEvent();
                event.setTranslationMap(service.read())
                    .setFilename(fileObj.getName());
                MountaincoreTranslator.getDispatcher().dispatch(event);
            });
            this.recentMenu.add(lastOpened);
        });
    }
}
