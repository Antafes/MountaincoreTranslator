package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.entity.TranslationEntity;
import antafes.mountaincoreTranslator.entity.TranslationMap;
import antafes.mountaincoreTranslator.gui.element.PDControlScrollPane;
import antafes.mountaincoreTranslator.gui.event.*;
import antafes.mountaincoreTranslator.gui.policy.TranslationFocusTraversalPolicy;
import antafes.mountaincoreTranslator.utility.SortingUtility;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class TranslationPanel extends JTabbedPane
{
    private TranslationMap translations;
    private TranslationMap originalTranslations;
    private boolean evenRow = true;
    private final HashMap<String, HashMap<String, JTextArea>> translationElements = new HashMap<>();
    private JPanel allPanel;
    private boolean fileUnsaved;
    private final HashMap<String, TranslationEntity> changedTranslations = new HashMap<>();
    private JPanel untranslatedPanel;
    private final Vector<Component> order;

    public TranslationPanel()
    {
        this.order = new Vector<>();
    }

    public void setTranslations(TranslationMap translations)
    {
        if (this.translations != null && this.translations.equals(translations)) {
            return;
        }

        this.clearTabs();
        this.fileUnsaved = false;
        this.translations = translations;

        this.addAllTab();
        this.addUntranslatedTab();

        this.addChangeTabListener();
        this.addFieldListener();

        this.addEventListener();
    }

    private void addChangeTabListener()
    {
        this.addChangeListener(e -> {
            JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
            int index = sourceTabbedPane.getSelectedIndex();

            if (index == 0) {
                translationElements.get("all").forEach(
                    (key, element) -> element.setText(changedTranslations.get(key).getTranslated())
                );
            } else {
                translationElements.get("untranslated").forEach(
                    (key, element) -> element.setText(changedTranslations.get(key).getTranslated())
                );
            }
        });
    }

    private void addFieldListener()
    {
        this.translationElements.forEach(
            (tab, fieldList) -> fieldList.forEach(
                (key, element) -> element.getDocument().addDocumentListener(this.createDocumentListener(element))
            )
        );
        this.setFocusTraversalPolicy(new TranslationFocusTraversalPolicy(this.order));
        this.setFocusTraversalPolicyProvider(true);
    }

    private void addEventListener()
    {
        MountaincoreTranslator.getDispatcher().addListener(
            SaveFileEvent.class,
            new SaveFileListener(this::saveFile)
        );
        MountaincoreTranslator.getDispatcher().addListener(
            SearchEvent.class,
            new SearchListener(this::search)
        );
        MountaincoreTranslator.getDispatcher().addListener(
            UnsavedFileCheckEvent.class,
            new UnsavedFileCheckListener((event) -> event.setFileUnsaved(this.fileUnsaved))
        );
    }

    private void clearTabs()
    {
        if (this.translations == null) {
            return;
        }

        this.removeTabAt(1);
        this.removeTabAt(0);
    }

    private void addAllTab()
    {
        this.allPanel = new JPanel();
        this.allPanel.setLayout(new GridBagLayout());
        this.allPanel.setName("all");

        this.fillAllTab();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportView(this.allPanel);

        this.addTab("All translations", scrollPane);
    }

    private void fillAllTab()
    {
        GridBagConstraints constraints = this.setupConstraints();

        this.createHeader(this.allPanel, constraints);

        SortingUtility.sortEntityMap(this.translations).forEach(
            (group, translationList) ->  {
                this.createGroup(this.allPanel, constraints, group, translationList);
                translationList.forEach(entity -> this.changedTranslations.put(entity.getKey(), entity));
            }
        );
    }

    private void addUntranslatedTab()
    {
        this.untranslatedPanel = new JPanel();
        this.untranslatedPanel.setLayout(new GridBagLayout());
        this.untranslatedPanel.setName("untranslated");
        GridBagConstraints constraints = this.setupConstraints();

        this.createHeader(this.untranslatedPanel, constraints);

        SortingUtility.sortEntityMap(this.translations).forEach(
            (group, translationList) -> this.createGroup(this.untranslatedPanel, constraints, group, translationList, true)
        );

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1100, 600));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportView(this.untranslatedPanel);

        this.addTab("Not translated", scrollPane);
    }

    private @NonNull GridBagConstraints setupConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 2;
        constraints.insets.set(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        return constraints;
    }

    private void createHeader(@NonNull JPanel panel, GridBagConstraints constraints)
    {
        panel.add(new JLabel("Key"), constraints);
        constraints.gridx++;
        panel.add(new JLabel("Notice"), constraints);
        constraints.gridx++;
        panel.add(new JLabel("English"), constraints);
        constraints.gridx++;
        panel.add(new JLabel(this.translations.getLanguage()), constraints);
        constraints.gridx = 0;
        constraints.gridy++;
    }

    private void createGroup(
        JPanel panel,
        GridBagConstraints constraints,
        String group,
        ArrayList<TranslationEntity> translationList
    ) {
        this.createGroup(panel, constraints, group, translationList, false);
    }

    private void createGroup(
        JPanel panel,
        GridBagConstraints constraints,
        String group,
        ArrayList<TranslationEntity> translationList,
        boolean skipEmpty
    ) {
        if (skipEmpty && this.checkGroupAllTranslated(translationList)) {
            return;
        }

        constraints.gridwidth = 4;
        panel.add(this.createGroupLabel(group), constraints);
        constraints.gridwidth = 1;
        constraints.gridy++;
        SortingUtility.sortEntityList(translationList).forEach(translation -> {
            if (!skipEmpty || translation.getTranslated().isEmpty()) {
                this.createRow(panel, constraints, translation);
            }
        });
    }

    private boolean checkGroupAllTranslated(@NonNull ArrayList<TranslationEntity> translationList)
    {
        for (TranslationEntity translation : translationList) {
            if (translation.getTranslated().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void createRow(JPanel panel, GridBagConstraints constraints, TranslationEntity translation)
    {
        Color backgroundColor = Color.LIGHT_GRAY;

        if (this.evenRow) {
            backgroundColor = new Color(210, 210, 210);
            this.evenRow = false;
        } else {
            this.evenRow = true;
        }

        JTextPane keyLabel = new JTextPane();
        keyLabel.setText(translation.getKey());
        keyLabel.setBackground(backgroundColor);
        keyLabel.setEditable(false);
        keyLabel.setPreferredSize(new Dimension(200, 50));
        JScrollPane keyScrollPane = new PDControlScrollPane();
        keyScrollPane.setPreferredSize(new Dimension(200, 50));
        keyScrollPane.setViewportView(keyLabel);
        panel.add(keyScrollPane, constraints);
        constraints.gridx++;

        JTextPane noticeLabel = new JTextPane();
        noticeLabel.setText(translation.getNotice());
        noticeLabel.setBackground(backgroundColor);
        noticeLabel.setEditable(false);
        noticeLabel.setPreferredSize(new Dimension(300, 50));
        JScrollPane noticeScrollPane = new PDControlScrollPane();
        noticeScrollPane.setPreferredSize(new Dimension(300, 50));
        noticeScrollPane.setViewportView(noticeLabel);
        panel.add(noticeScrollPane, constraints);
        constraints.gridx++;

        JTextPane englishLabel = new JTextPane();
        englishLabel.setBackground(backgroundColor);
        englishLabel.setText(translation.getEnglish());
        englishLabel.setEditable(false);
        englishLabel.setPreferredSize(new Dimension(300, 50));
        JScrollPane englishScrollPane = new PDControlScrollPane();
        englishScrollPane.setPreferredSize(new Dimension(300, 50));
        englishScrollPane.setViewportView(englishLabel);
        panel.add(englishScrollPane, constraints);
        constraints.gridx++;

        JTextArea textArea = new JTextArea();
        textArea.setRows(3);
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(translation.getTranslated());
        textArea.setName(translation.getKey());
        textArea.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (e.getModifiersEx() > 0) {
                        ((JTextArea) e.getSource()).transferFocusBackward();
                    } else {
                        ((JTextArea) e.getSource()).transferFocus();
                    }
                    e.consume();
                }
            }
        });
        JScrollPane translationScrollPane = new PDControlScrollPane();
        translationScrollPane.setPreferredSize(new Dimension(300, 50));
        translationScrollPane.setViewportView(textArea);
        panel.add(translationScrollPane, constraints);
        this.order.add(textArea);

        if (!this.translationElements.containsKey(panel.getName())) {
            this.translationElements.put(panel.getName(), new HashMap<>());
        }

        this.translationElements.get(panel.getName()).put(translation.getKey(), textArea);
        constraints.gridx = 0;
        constraints.gridy++;
    }

    private @NonNull JLabel createGroupLabel(@NonNull String group)
    {
        String[] parts = group.toLowerCase().split("_");
        parts[0] = StringUtils.capitalize(parts[0]);

        return new JLabel(StringUtils.joinWith(" ", (Object[]) parts));
    }

    private void saveFile(@NonNull SaveFileEvent saveFileEvent)
    {
        TranslationMap map = new TranslationMap(this.translations.getLanguage());

        this.changedTranslations.forEach((key, translation) -> {
            if (!map.containsKey(translation.getGroupElement())) {
                map.put(translation.getGroupElement(), new ArrayList<>());
            }

            map.get(translation.getGroupElement()).add(translation);
        });

        this.fileUnsaved = false;
        saveFileEvent.setTranslationMap(map);
    }

    private void search(@NonNull SearchEvent searchEvent)
    {
        if (searchEvent.getSearchValue().isEmpty()) {
            ShowWaitAction waitAction = new ShowWaitAction(SwingUtilities.windowForComponent(this));
            waitAction.setMessage("Loading translations...");
            waitAction.show(aVoid -> {
                this.translations = this.createTranslationMapFromMap(this.originalTranslations);
                this.allPanel.removeAll();
                this.order.clear();
                this.fillAllTab();
                this.addFieldListener();
                this.originalTranslations = null;

                return null;
            });

            return;
        }

        if (this.originalTranslations == null || this.originalTranslations.isEmpty()) {
            this.originalTranslations = (TranslationMap) this.translations.clone();
        }

        this.translations.clear();

        this.originalTranslations.forEach((group, list) -> {
            if (!this.translations.containsKey(group)) {
                this.translations.put(group, new ArrayList<>());
            }

            list.stream().filter(
                entity -> StringUtils.containsIgnoreCase(entity.getKey(), searchEvent.getSearchValue())
                    || StringUtils.containsIgnoreCase(entity.getNotice(), searchEvent.getSearchValue())
                    || StringUtils.containsIgnoreCase(entity.getEnglish(), searchEvent.getSearchValue())
                    || StringUtils.containsIgnoreCase(entity.getTranslated(), searchEvent.getSearchValue())
            ).forEachOrdered(entity -> this.translations.get(group).add(entity));
        });
        this.translations.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        this.translations = this.createTranslationMapFromMap(this.translations);
        this.allPanel.removeAll();
        this.order.clear();
        this.fillAllTab();
        this.addFieldListener();
    }

    private TranslationMap createTranslationMapFromMap(TranslationMap translationMap)
    {
        TranslationMap map = new TranslationMap(translationMap.getLanguage());
        translationMap.forEach((group, list) -> {
            if (!map.containsKey(group)) {
                map.put(group, new ArrayList<>());
            }

            list.forEach(translation -> map.get(group).add(
                translation.toBuilder()
                    .setTranslated(this.changedTranslations.get(translation.getKey()).getTranslated())
                    .build()
            ));
        });

        return map;
    }

    private DocumentListener createDocumentListener(JTextArea textArea)
    {
        ComponentDocumentListener listener = new ComponentDocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                this.changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                this.changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                this.changed();
            }

            private void changed()
            {
                fileUnsaved = true;
                JTextArea component = ((JTextArea) this.getComponent());
                String key = component.getName();
                String value = component.getText();
                changedTranslations.put(
                    component.getName(),
                    changedTranslations.get(key).toBuilder()
                        .setTranslated(value)
                        .build()
                );
            }
        };
        listener.setComponent(textArea);

        return listener;
    }
}
