package antafes.mountaincoreTranslator.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(setterPrefix = "set", toBuilder = true)
public class TranslationEntity
{
    private String key;
    private String notice;
    private String english;
    private String translated;
    @Builder.Default
    private String groupElement = null;

    public String getGroupElement()
    {
        if (this.groupElement != null) {
            return this.groupElement;
        }

        String[] parts = this.key.split("\\.");

        return this.groupElement = parts[0];
    }
}
