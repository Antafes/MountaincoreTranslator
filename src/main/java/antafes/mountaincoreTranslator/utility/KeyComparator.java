package antafes.mountaincoreTranslator.utility;

import antafes.mountaincoreTranslator.entity.TranslationEntity;
import lombok.NonNull;

import java.util.Comparator;

/**
 *
 * @author Marian Pollzien
 */
public class KeyComparator implements Comparator<TranslationEntity> {

    /**
     * Compare two objects by name.
     *
     * @param o1 First object
     * @param o2 Second object
     *
     * @return The value {@code 0} if the argument string is equal to this string; a value less than {@code 0} if this
     *         string is lexicographically less than the string argument; and a value greater than {@code 0} if this
     *         string is lexicographically greater than the string argument.
     */
    @Override
    public int compare(@NonNull TranslationEntity o1, @NonNull TranslationEntity o2) {
        String s1 = o1.getKey();
        String s2 = o2.getKey();

        return s1.compareTo(s2);
    }
}
