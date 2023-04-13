package antafes.mountaincoreTranslator.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class TranslationMap extends HashMap<String, ArrayList<TranslationEntity>>
{
    @Getter
    private final String language;

    public TranslationMap(String language)
    {
        this.language = language;
    }
}
