package antafes.mountaincoreTranslator.service;

import antafes.mountaincoreTranslator.entity.TranslationEntity;
import antafes.mountaincoreTranslator.entity.TranslationMap;
import antafes.mountaincoreTranslator.utility.SortingUtility;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class FileHandlerService
{
    private final String filename;
    private final String path;

    public FileHandlerService(String path)
    {
        this.path = path;
        this.filename = (new File(path)).getName();
    }

    public TranslationMap read()
    {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(this.path))
                .withCSVParser(new RFC4180ParserBuilder().build())
                .build();
            TranslationMap translationEntities = new TranslationMap(this.getLanguageFromFilename());
            String[] values;
            TranslationEntity entity;
            while ((values = reader.readNext()) != null) {
                if (Objects.equals(values[0], "") || Objects.equals(values[0], "KEY")) {
                    continue;
                }

                entity = TranslationEntity.builder()
                    .setKey(values[0])
                    .setNotice(values[1])
                    .setEnglish(values[2])
                    .setTranslated(values[3])
                    .build();

                if (!translationEntities.containsKey(entity.getGroupElement())) {
                    translationEntities.put(entity.getGroupElement(), new ArrayList<>());
                }

                translationEntities.get(entity.getGroupElement()).add(entity);
            }

            return translationEntities;
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(TranslationMap translationMap)
    {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(this.path), ',', '"', '"', "\r\n");
            String[] headlines = {
                "KEY",
                "NOTES",
                "ENGLISH",
                this.filename.split("\\.")[0].toUpperCase()
            };
            String[] empty = {"", "", "", ""};

            writer.writeNext(headlines, true);
            writer.writeNext(empty, true);

            SortingUtility.sortEntityMap(translationMap).forEach((group, list) -> {
                SortingUtility.sortEntityList(list).forEach(translation -> {
                    String[] line = {
                        translation.getKey(),
                        translation.getNotice(),
                        translation.getEnglish(),
                        translation.getTranslated()
                    };
                    writer.writeNext(line, true);
                });
                writer.writeNext(empty, true);
            });
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getLanguageFromFilename()
    {
        String[] parts = this.filename.split("\\.");

        return StringUtils.capitalize(parts[0]);
    }
}
