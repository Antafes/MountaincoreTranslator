package antafes.mountaincoreTranslator.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class TranslationMap extends HashMap<String, ArrayList<TranslationEntity>>
{
    @Getter
    private String language;

    public TranslationMap(String language)
    {
        this.language = language;
    }

    @Override
    public Object clone()
    {
        TranslationMap map = (TranslationMap) super.clone();
        map.language = this.getLanguage();

        return map;
    }
}
