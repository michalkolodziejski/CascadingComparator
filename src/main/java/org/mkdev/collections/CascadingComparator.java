package org.mkdev.collections;

import org.mkdev.collections.annotations.Criteria;
import org.mkdev.collections.annotations.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 *
 * @author Michał Kołodziejski &lt;<I><A href="mailto:michal.kolodziejski@gmail.com">michal.kolodziejski@gmail.com</A></I>&gt;
 * @version 1.0
 * @license: GPLv3 (http://www.gnu.org/licenses/gpl-3.0.txt)
 * @since: 2014-04-25
 */
public class CascadingComparator<T> implements Comparator<T>, Serializable {

    private static final long serialVersionUID = -3872384457429106720L;

    private final Logger logger = LoggerFactory.getLogger("org.mkdev.collections");

    private List<Comparator<T>> comparators;

    public final void registerComparator(Comparator<T> comparator) {
        this.comparators.add(comparator);
    }

    private List<Comparator<T>> createComparatorsForClass(Class<T> aClass) {
        List<Comparator<T>> localComparators = new ArrayList<>();

        Set<VO> voAnnotations = new TreeSet<VO>();

        for (Field field : aClass.getDeclaredFields()) {

            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Criteria) {
                    Criteria criteriaAnnotation = (Criteria) annotation;

                    logger.debug("field = %s, priority = %s, order = %s, nvl = %s", field.getName(), criteriaAnnotation.priority(), criteriaAnnotation.order(), criteriaAnnotation.nvl());

                    voAnnotations.add(new VO(field.getName(), criteriaAnnotation));
                }
            }
        }

        for(VO vo : voAnnotations) {

            FieldComparator<T> fieldComparator = new FieldComparator<T>(aClass, vo.fieldName, vo.sortOrder, vo.nvl);
            localComparators.add(fieldComparator);
        }

        return localComparators;
    }

    public CascadingComparator(Class<T> aClass) {
        this.comparators = createComparatorsForClass(aClass);
    }

    public CascadingComparator(List<Comparator<T>> aComparators) {
        if (aComparators == null || aComparators.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.comparators = aComparators;
    }

    public CascadingComparator() {
        this.comparators = new ArrayList<>();
    }

    @Override
    public final int compare(T e1, T e2) {
        // Note: compares identity
        if (e1 == e2) {
            return 0;
        }

        int result = 0;
        for (final Comparator<T> comparator : comparators) {
            result = comparator.compare(e1, e2);
            if (result != 0) {
                break;
            }
        }
        return result;
    }

    class VO implements Comparable<VO> {
        private int priority;
        private SortOrder sortOrder;
        private String nvl;
        private String fieldName;

        public VO(String aFieldName, Criteria annotation) {
            this.fieldName = aFieldName;
            this.priority = annotation.priority();
            this.sortOrder = annotation.order();
            this.nvl = annotation.nvl();
        }

        @Override
        public int compareTo(VO that) {
            return this.priority - that.priority;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (this == o) {return true;}
            if (!(o instanceof CascadingComparator.VO)) {return false;}

            VO vo = (VO) o;

            return priority == vo.priority;
        }

        @Override
        public int hashCode() {
            return priority;
        }

        @Override
        public String toString() {
            return "VO{" +
                "priority=" + priority +
                ", order=" + sortOrder +
                ", nvl='" + nvl + '\'' +
                ", fieldName=" + fieldName +
                '}';
        }
    }
}