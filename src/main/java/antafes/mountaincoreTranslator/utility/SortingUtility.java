/*
 * This file is part of Vampire Editor.
 *
 * Vampire Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vampire Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Vampire Editor. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package Vampire Editor
 * @author Marian Pollzien <map@wafriv.de>
 * @copyright (c) 2022, Marian Pollzien
 * @license https://www.gnu.org/licenses/lgpl.html LGPLv3
 */

package antafes.mountaincoreTranslator.utility;

import antafes.mountaincoreTranslator.entity.TranslationEntity;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class SortingUtility
{
    public static HashMap<String, ArrayList<TranslationEntity>> sortEntityMap(
        @NonNull HashMap<String, ArrayList<TranslationEntity>> entityMap
    )
    {
        return entityMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                )
            );
    }

    public static @NonNull ArrayList<TranslationEntity> sortEntityList(@NonNull ArrayList<TranslationEntity> entityList)
    {
        entityList.sort(new KeyComparator());

        return  entityList;
    }
}
