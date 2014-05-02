package org.mkdev.collections;

import org.mkdev.collections.annotations.SortOrder;
import org.mkdev.collections.exceptions.FieldCompareFailedException;
import org.mkdev.collections.exceptions.MajorCompareException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-04-25
 */
public class FieldComparator<T> implements Comparator<T>, Serializable {

    private static final long serialVersionUID = -769472805963788559L;

    private final Logger logger = LoggerFactory.getLogger("org.mkdev.collections");

    private String fieldName;
    private SortOrder sortOrder;
    private Class<T> aClass;
    private String nvl;

    public FieldComparator(Class<T> xClass, String aFieldName, SortOrder aSortOrder, String aNvl) {
        this.fieldName = aFieldName;
        this.sortOrder = aSortOrder;
        this.aClass = xClass;
        this.nvl = aNvl;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final int compare(Object o1, Object o2) {
        Field fieldToCompare;
        try {
            fieldToCompare = aClass.getDeclaredField(fieldName);
            fieldToCompare.setAccessible(true);

            logger.debug("fieldName = [%s] (%s) %s", fieldToCompare.getType().getCanonicalName(), nvl, nvl.getClass().getCanonicalName());

            Object v1 = fieldToCompare.get(o1);
            v1 = queryNVL(fieldToCompare, v1, nvl);

            Object v2 = fieldToCompare.get(o2);
            v2 = queryNVL(fieldToCompare, v2, nvl);

            if (v1 instanceof Comparable<?> && v2 instanceof Comparable<?>) {
                Comparable c1 = (Comparable) v1;
                Comparable c2 = (Comparable) v2;

                switch (sortOrder) {
                    case ASC:
                        return c1.compareTo(c2);
                    case DESC:
                        return c2.compareTo(c1);
                }
            } else {
                throw new FieldCompareFailedException("Couldn't compare by field " + fieldName);
            }
        } catch (Exception e) {
            throw new MajorCompareException("Major comparison exception", e);
        }

        return 0;
    }

    private Object queryNVL(Field fieldToCompare, Object objectToCheck, String aNvl) {
        Object returnValue = null;

        if (objectToCheck == null) {
            // FIXME: have mercy and refactor this :)
            if (fieldToCompare.getType().isAssignableFrom(String.class)) {
                returnValue = (String) aNvl;
            }
            if (fieldToCompare.getType().isAssignableFrom(Integer.class)) {
                returnValue = Integer.parseInt(aNvl);
            }
            if (fieldToCompare.getType().isAssignableFrom(Double.class)) {
                returnValue = Double.parseDouble(aNvl);
            }
        }

        return returnValue;
    }
}