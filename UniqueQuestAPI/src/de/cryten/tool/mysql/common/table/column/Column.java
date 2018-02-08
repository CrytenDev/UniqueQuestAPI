package de.cryten.tool.mysql.common.table.column;

/**
 * This class forms a table column.
 *
 * @author Florian Heitzmann
 */
public class Column {
    private final String field;
    private final ColumnType columnType;
    private int length;
    private boolean unique, primaryKey, autoIncrement;

    /**
     * Creates a column with a auto increment.
     * <pre>
     *     CREATE TABLE persons (id INT NOT NULL AUTO_INCREMENT, ...);
     * </pre>
     *
     * @param field Name of the column.
     */
    public Column(String field) {
        this.field = field;
        this.columnType = ColumnType.INT;
        this.autoIncrement = true;
    }

    /**
     * Creates a default column.
     *
     * @param field Name of the column.
     * @param columnType The data type, for example INT, VARCHAR or LONG.
     */
    public Column(String field, ColumnType columnType) {
        this.field = field;
        this.columnType = columnType;
    }

    /**
     * Creates a column with a length.
     *  <pre>
     *     CREATE TABLE persons (name VARCHAR(16), ...);
     * </pre>
     *
     * @param field Name of the column.
     * @param columnType The data type, for example INT, VARCHAR or LONG.
     * @param length Length of the value.
     */
    public Column(String field, ColumnType columnType, int length) {
        this.field = field;
        this.columnType = columnType;
        this.length = length;
    }

    /**
     * Creates a column with a length which can be unique.
     * <pre>
     *     CREATE TABLE persons (name VARCHAR(16) UNIQUE KEY, ...);
     * </pre>
     *
     * @param field Name of the column.
     * @param columnType The data type, for example INT, VARCHAR or LONG.
     * @param length Length of the value.
     * @param unique Should the column be unique?
     */
    public Column(String field, ColumnType columnType, int length, boolean unique) {
        this.field = field;
        this.columnType = columnType;
        this.length = length;
        this.unique = unique;
    }

    /**
     * Creates a column with a length which can be unique and has a primary key.
     * <pre>
     *     CREATE TABLE persons (name VARCHAR(16) [UNIQUE KEY | PRIMARY KEY], ...);
     * </pre>
     *
     * @param field Name of the column.
     * @param columnType The data type, for example INT, VARCHAR or LONG.
     * @param length Length of the value.
     * @param unique Should the column be unique?
     * @param primaryKey Should the column has a primary key?
     */
    public Column(String field, ColumnType columnType, int length, boolean unique, boolean primaryKey) {
        this.field = field;
        this.columnType = columnType;
        this.length = length;
        this.unique = unique;
        this.primaryKey = primaryKey;
    }


    /**
     * Returns the name of the table column.
     *
     * @return the name of the table column.
     */
    public String getField() {
        return this.field;
    }

    /**
     * Returns the data type of table column.
     *
     * @return the data type of table column
     */
    public ColumnType getColumnType() {
        return this.columnType;
    }

    /**
     * Returns the max length of the column value.
     *
     * @return the max length of the column value.
     *
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Returns true if the column is unique.
     *
     * @return true if the column is unique.
     */
    public boolean isUnique() {
        return this.unique;
    }

    /**
     * Returns true if the column has a primary key.
     *
     * @return true if the column has a primary key.
     */
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }

    /**
     * Returns true if the column has auto increment.
     *
     * @return true if the column has auto increment.
     */
    public boolean isAutoIncrement() {
        return this.autoIncrement;
    }
}
