CascadingComparator
===================

##Description
CascadingComparator gives you the ability to generate a decent java.util.Comparator for your class, enables sorting with multiple attributes, priority of executing comparators, and null value handling.

##Compile
You need JDK 1.7 and Maven to compile the project.
```shell
    mvn clean
    mvn compile
```

###Using Annotations
**Example**
```Java
public class ComparableEntry implements Comparable<ComparableEntry>, Serializable  {

    private static final long serialVersionUID = 8685640734247873209L;

    private CascadingComparator<ComparableEntry> comparator = new CascadingComparator<ComparableEntry>(ComparableEntry.class);

    @Criteria(priority = 3, order = SortOrder.ASC)
    private String field1;

    @Criteria(priority = 2, order = SortOrder.ASC)
    private String field2;

    @Criteria(priority = 1, order = SortOrder.ASC, nvl = "0000")
    private Integer field3; // eg. year

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(ComparableEntry that) {
        return comparator.compare(this, that);
    }
}
```

#### Comparator factory
```Java
private CascadingComparator<ComparableEntry> comparator = new CascadingComparator<ComparableEntry>(ComparableEntry.class);
```

#### Fields annotation
Following code will annotate fields that will be used by separate comparators in cascade, with defined priority (order of execution), sort order (direction of comparison) and (optional) null value handling.

```Java
@Criteria(priority = 3, order = SortOrder.ASC)
private String field1;

@Criteria(priority = 2, order = SortOrder.ASC)
private String field2;

@Criteria(priority = 1, order = SortOrder.ASC, nvl = "0000")
private Integer field3;
```

#### Usage in 'compareTo' method
Following code will 'fire' created comparator cascade.

```Java
@SuppressWarnings("unchecked")
@Override
public int compareTo(ComparableEntry that) {
  return comparator.compare(this, that);
}
```

### 'Criteria' annotation
You can annotate every field with `@Criteria` annotation. This will tell comparator factory to create an comparator and use it in an cascade way in the `compare` method

```Java
@Criteria(priority = ???, order = ??? [, nvl = ???])
```

**Note:** if you have multiple values to compare you have to specify the priority of these values. The higher the value the more important is it to compare. 

#### 'priority' annotation attribute (required)
This attribute determines priority of comparator in cascade, defined by numeric order. Takes `integer` as an argument.

```Java
@Criteria(priority = 1, order = ??? [, nvl = ???])
```

Defined as above - instruments comparator factory to make comparator for this field to be a first one in sequence (cascade). 

**Note:** Make sure that priority numbers are unique - only the first field annotated with given priority will eventually end up as an comparator - all other fields annotated with already defined priority will be ignored.

#### 'order' annotation attribute (required)
This attribute determines cardinality of generated comparator for a field. Takes value defined by `SortOrder` enum as an argument.

Cardinality (ordering direction) | Value in SortOrder
------- | ---------
ascending | SortOrder.ASC 
descending     | SortOrder.DESC

```Java
@Criteria(priority = ???, order = SortOrder.ASC [, nvl = ???])
```

Defined as above - instruments comparator factory to make comparator for this field to explicitly compare elements in ascending orders. 

### 'nvl' annotation attribute (optional)
This attribute defines an substitution value for `null` value for a field. It instruments comparator factory to create a comparator which substitutes `null` values of compared objects with an `String` value (that has been cast to a value of field type) provided as an argument. Takes `String` as an argument. 

**Note: ** This attribute is optional.

**Note: ** This has no side effects - substitution only takes place upon comparison, the original value of field is intact.

```Java
@Criteria(priority = ???, order = ???, nvl = "0000")
```

Defined as above - instruments comparator factory to make comparator for this field with a substitution for `nvl` argument in mind, when `null` value is provided for comparator.

## Credits
***Special thanks to Andrzej Masłowski for asking the right question at the right time.***

It all started by 'scratching own itch' - universal solution created solely for my own consumption. Then I've decided to share it with JAVA community.

## History

2014-04-25

* Initial version.

## Problems?

[Submit an issue](https://github.com/michalkolodziejski/CascadingComparator/issues).
