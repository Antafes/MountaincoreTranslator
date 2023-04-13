package antafes.mountaincoreTranslator.service;

import antafes.mountaincoreTranslator.entity.TranslationEntity;
import antafes.mountaincoreTranslator.entity.TranslationMap;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FileHandlerService
{
    private CSVReader reader;
    private String filename;

    public FileHandlerService(String path)
    {
        try {
            this.filename = (new File(path)).getName();
            this.reader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public TranslationMap read()
    {
        TranslationMap translationEntities = new TranslationMap(this.getLanguageFromFilename());
        String[] values;
        TranslationEntity entity;
        try {
            while ((values = this.reader.readNext()) != null) {
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
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }

        return translationEntities;
    }

    private String getLanguageFromFilename()
    {
        String[] parts = this.filename.split("\\.");

        return StringUtils.capitalize(parts[0]);
    }
}
