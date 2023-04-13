package antafes.mountaincoreTranslator.gui;

import antafes.mountaincoreTranslator.entity.TranslationEntity;
import antafes.mountaincoreTranslator.entity.TranslationMap;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class TranslationPanel extends JTabbedPane
{
    private TranslationMap translations;
    private boolean evenRow = true;

    public void setTranslations(TranslationMap translations)
    {
        if (this.translations != null && this.translations.equals(translations)) {
            return;
        }

        this.clearTabs();
        this.translations = translations;

        this.addAllTab();
        this.addUntranslatedTab();
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
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 5;
        constraints.insets.set(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        panel.add(new JLabel("Key"), constraints);
        constraints.gridx++;
        panel.add(new JLabel("Notice"), constraints);
        constraints.gridx++;
        panel.add(new JLabel("English"), constraints);
        constraints.gridx++;
        panel.add(new JLabel(this.translations.getLanguage()), constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        this.translations.forEach((group, translationList) -> {
            constraints.gridwidth = 4;
            panel.add(this.createGroupLabel(group), constraints);
            constraints.gridwidth = 1;
            constraints.gridy++;
            translationList.forEach(translation -> createRow(panel, constraints, translation));
        });

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
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.ipady = 5;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets.set(2, 2, 2, 2);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        panel.add(new JLabel("Key"), constraints);
        constraints.gridx++;
        panel.add(new JLabel("Notice"), constraints);
        constraints.gridx++;
        panel.add(new JLabel("English"), constraints);
        constraints.gridx++;
        panel.add(new JLabel(this.translations.getLanguage()), constraints);
        constraints.gridx = 0;
        constraints.gridy++;

        this.translations.forEach((group, translationList) -> {
            constraints.gridwidth = 4;
            panel.add(this.createGroupLabel(group), constraints);
            constraints.gridwidth = 1;
            constraints.gridy++;
            translationList.forEach(translation -> {
                if (translation.getTranslated().isEmpty()) {
                    this.createRow(panel, constraints, translation);
                }
            });
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportView(panel);

        this.addTab("Not translated", scrollPane);
    }

    private void createRow(JPanel panel, GridBagConstraints constraints, TranslationEntity translation)
    {
        Color backgroundColor = Color.LIGHT_GRAY;

        if (this.evenRow) {
            backgroundColor = Color.WHITE;
            this.evenRow = false;
        } else {
            this.evenRow = true;
        }

        JTextPane keyLabel = new JTextPane();
        keyLabel.setText(translation.getKey());
        keyLabel.setBackground(backgroundColor);
        keyLabel.setMaximumSize(new Dimension(50, 150));
        keyLabel.setPreferredSize(new Dimension(50, 20));
        panel.add(keyLabel, constraints);
        constraints.gridx++;

        JTextPane noticeLabel = new JTextPane();
        noticeLabel.setText(translation.getNotice());
        noticeLabel.setBackground(backgroundColor);
        noticeLabel.setMaximumSize(new Dimension(50, 150));
        noticeLabel.setPreferredSize(new Dimension(50, 20));
        panel.add(noticeLabel, constraints);
        constraints.gridx++;

        JTextPane englishLabel = new JTextPane();
        englishLabel.setBackground(backgroundColor);
        englishLabel.setText(translation.getEnglish());
        englishLabel.setEditable(false);
        englishLabel.setPreferredSize(new Dimension(100, 50));
        panel.add(englishLabel, constraints);
        constraints.gridx++;

        JTextPane textArea = new JTextPane();
        textArea.setBackground(backgroundColor);
        textArea.setPreferredSize(new Dimension(100, 50));
        textArea.setText(translation.getTranslated());
        panel.add(textArea, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
    }

    private JLabel createGroupLabel(String group)
    {
        String[] parts = group.toLowerCase().split("_");
        parts[0] = StringUtils.capitalize(parts[0]);

        return new JLabel(StringUtils.joinWith(" ", (Object[]) parts));
    }
}
