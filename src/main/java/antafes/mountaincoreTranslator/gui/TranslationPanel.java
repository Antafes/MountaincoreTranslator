package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.MountaincoreTranslator;
import antafes.mountaincoreTranslator.entity.TranslationEntity;
import antafes.mountaincoreTranslator.entity.TranslationMap;
import antafes.mountaincoreTranslator.gui.element.PDControlScrollPane;
import antafes.mountaincoreTranslator.gui.event.SaveFileEvent;
import antafes.mountaincoreTranslator.gui.event.SaveFileListener;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TranslationPanel extends JTabbedPane
{
    private TranslationMap translations;
    private boolean evenRow = true;
    private final HashMap<String, JTextPane> translationElements = new HashMap<>();

    public void setTranslations(TranslationMap translations)
    {
        if (this.translations != null && this.translations.equals(translations)) {
            return;
        }

        this.clearTabs();
        this.translations = translations;

        this.addAllTab();
        this.addUntranslatedTab();

        this.addEventListener();
    }

    private void addEventListener()
    {
        MountaincoreTranslator.getDispatcher().addListener(
            SaveFileEvent.class,
            new SaveFileListener(this::saveFile)
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = this.setupConstraints();

        this.createHeader(panel, constraints);

        this.translations.forEach(
            (group, translationList) -> this.createGroup(panel, constraints, group, translationList)
        );

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportView(panel);

        this.addTab("All translations", scrollPane);
    }

    private void addUntranslatedTab()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = this.setupConstraints();

        this.createHeader(panel, constraints);

        this.translations.forEach(
            (group, translationList) -> this.createGroup(panel, constraints, group, translationList, true)
        );

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1100, 600));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportView(panel);

        this.addTab("Not translated", scrollPane);
    }

    private GridBagConstraints setupConstraints()
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

    private void createHeader(JPanel panel, GridBagConstraints constraints)
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
        translationList.forEach(translation -> {
            if (!skipEmpty || translation.getTranslated().isEmpty()) {
                this.createRow(panel, constraints, translation);
            }
        });
    }

    private boolean checkGroupAllTranslated(ArrayList<TranslationEntity> translationList)
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

        JTextPane textArea = new JTextPane();
        textArea.setPreferredSize(new Dimension(300, 50));
        textArea.setText(translation.getTranslated());
        JScrollPane translationScrollPane = new PDControlScrollPane();
        translationScrollPane.setPreferredSize(new Dimension(300, 50));
        translationScrollPane.setViewportView(textArea);
        panel.add(translationScrollPane, constraints);
        this.translationElements.put(translation.getKey(), textArea);
        constraints.gridx = 0;
        constraints.gridy++;
    }

    private JLabel createGroupLabel(String group)
    {
        String[] parts = group.toLowerCase().split("_");
        parts[0] = StringUtils.capitalize(parts[0]);

        return new JLabel(StringUtils.joinWith(" ", (Object[]) parts));
    }

    private void saveFile(SaveFileEvent saveFileEvent)
    {
        TranslationMap map = new TranslationMap(this.translations.getLanguage());

        this.translations.forEach((group, list) -> {
            if (!map.containsKey(group)) {
                map.put(group, new ArrayList<>());
            }

            list.forEach(
                entity -> map.get(group).add(
                    entity.toBuilder()
                        .setTranslated(this.translationElements.get(entity.getKey()).getText())
                        .build()
                )
            );
        });

        saveFileEvent.setTranslationMap(map);
    }
}
