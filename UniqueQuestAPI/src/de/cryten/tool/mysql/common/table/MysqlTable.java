package de.cryten.tool.mysql.common.table;

import de.cryten.tool.mysql.ConnectionPool;
import de.cryten.tool.mysql.common.StatementResult;
import de.cryten.tool.mysql.common.table.column.Column;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class allows working with the mysql without syntax knowledge.
 *
 * @author Florian Heitzmann
 */
public class MysqlTable {
    private final ConnectionPool connectionPool;
    private final String table;
    private Column[] columns;

    /**
     * Initializes the table information.
     *
     * @param connectionPool The connection pool.
     * @param table The name of the table.
     * @param columns The columns of the table.
     */
    public MysqlTable(ConnectionPool connectionPool, String table, Column ... columns) {
        this.connectionPool = connectionPool;
        this.table = table;
        this.columns = columns;
    }

    /**
     * Creates the table.
     */
    public void create() {
        if(this.table == null) throw new IllegalArgumentException("The table name cannot be null!");
        if(this.columns == null || this.columns.length <= 0) throw new IllegalArgumentException("The variable \"columns\" has no entries or is null.");

        String syntax = "";

        for(int i = 0; i < this.columns.length; i++) {
            Column variable = this.columns[i];
            String column = variable.getField() + " " + variable.getColumnType().name();

            if(variable.getLength() > 0) {
                column += "(" + variable.getLength() + ")";
            }

            if(variable.isAutoIncrement()) {
                column += " NOT NULL AUTO_INCREMENT";
            }

            if(variable.isUnique()) {
                column += " UNIQUE KEY";
            }

            if(variable.isPrimaryKey()) {
                column += " PRIMARY KEY";
            }

            if(i < this.columns.length - 1) {
                syntax += column + ",";
            } else {
                syntax += column;
            }
        }

        syntax = "CREATE TABLE IF NOT EXISTS " + this.table + " (" + syntax + ")";

        Connection connection = this.connectionPool.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement(syntax)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Deletes the table.
     */
    public void delete() {
        final String syntax = "DELETE FROM " + this.table;

        Connection connection = this.connectionPool.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement(syntax)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a record to the table.
     *
     * @param values Specification of all values that are needed.
     */
    public void insert(Object[] values) {
        if(values == null || values.length <= 0) throw new IllegalArgumentException("The variable \"values\" has no entries or is null.");
        if(this.columns.length != values.length) throw new IllegalArgumentException("The variable \"values\" does not have the same number of records as the table has.");

        String[] cache = new String[this.columns.length];
        for(int i = 0; i < this.columns.length; i++) {
            cache[i] = this.columns[i].getField();
        }

        String syntax = "INSERT INTO " + this.table + " (" + buildString(cache) + ") VALUES (" + buildQuestionMarkString(values.length) + ")";

        Connection connection = this.connectionPool.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement(syntax)) {
            for(int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes a record from the table.
     *
     * @param whereNames The table columns.
     * @param whereValues The values of the {@code whereNames}.
     */
    public void remove(String[] whereNames, Object[] whereValues) {
        if(whereNames == null || whereNames.length <= 0) throw new IllegalArgumentException("The variable \"whereNames\" has no entries or is null.");
        if(whereValues == null || whereValues.length <= 0) throw new IllegalArgumentException("The variable \"whereNames\" has no entries or is null.");

        final String syntax = "DELETE FROM " + this.table + " " + buildWhereClause(whereNames, whereValues);

        Connection connection = this.connectionPool.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement(syntax)) {
            for(int i = 0; i < whereNames.length; i++) {
                preparedStatement.setObject(i + 1, whereValues[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the content of a record.
     *
     * @param setNames The table columns to update.
     * @param setValues The new values of the {@code setNames}.
     * @param whereNames The table columns.
     * @param whereValues The values of the {@code whereNames}.
     */
    public void update(String[] setNames, Object[] setValues, String[] whereNames, Object[] whereValues) {
        if(setNames == null || setNames.length <= 0) throw new IllegalArgumentException("The variable \"setNames\" has no entries or is null.");
        if(setValues == null || setValues.length <= 0) throw new IllegalArgumentException("The variable \"setValues\" has no entries or is null.");
        if(setNames.length != setValues.length) throw new IllegalArgumentException("The variable \"setNames\" does not have the same number of records as \"setValues\".");
        if(whereNames == null || whereNames.length <= 0) throw new IllegalArgumentException("The variable \"whereNames\" has no entries or is null.");
        if(whereValues == null || whereValues.length <= 0) throw new IllegalArgumentException("The variable \"whereNames\" has no entries or is null.");

        final String syntax = "UPDATE " + this.table + " " + buildSetClause(setNames) + " " + buildWhereClause(whereNames, whereValues);

        Connection connection = this.connectionPool.getConnection();

        try(PreparedStatement preparedStatement = connection.prepareStatement(syntax)) {
            int index = 1;
            for(Object object : setValues) {
                preparedStatement.setObject(index, object);
                index++;
            }
            for(Object object : whereValues) {
                preparedStatement.setObject(index, object);
                index++;
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns true if the record is in the table.
     *
     * @param whereNames The table columns.
     * @param whereValues The values of the {@code whereNames}.
     *
     * @return true if the record is in the table.
     */
    public boolean contains(String[] whereNames, Object[] whereValues) {
        return getAmount(whereNames, whereValues) > 0;
    }

    /**
     * Returns the amount of all found records.
     *
     * @param whereNames The table columns.
     * @param whereValues The values of the {@code whereNames}.
     *
     * @return the amount of all found records.
     */
    public int getAmount(String[] whereNames, Object[] whereValues) {
        int amount = 0;

        try(StatementResult statementResult = get(new String[] {"COUNT(*)"}, whereNames, whereValues)) {
            ResultSet resultSet = statementResult.receive();

            while(resultSet.next()) {
                amount = resultSet.getInt("COUNT(*)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return amount;
    }

    /**
     * Returns a {@link StatementResult}.
     *
     * @param getValues The table columns that are searched.
     * @param whereNames The table columns.
     * @param whereValues The values of the {@code whereNames}.
     *
     * @return a {@link StatementResult}.
     */
    public StatementResult get(String[] getValues, String[] whereNames, Object[] whereValues) {
        if(getValues == null || getValues.length <= 0) {
            throw new IllegalArgumentException("The variable \"getValues\" has no entries or is null.");
        }

        final String syntax = "SELECT " + buildString(getValues) + " FROM " + this.table + " " + buildWhereClause(whereNames, whereValues);

        Connection connection = this.connectionPool.getConnection();
        StatementResult result = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(syntax);
            for(int i = 0; i < whereNames.length; i++) {
                preparedStatement.setObject(i + 1, whereValues[i]);
            }
            preparedStatement.executeQuery();
            result = new StatementResult(connection, preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Returns a generated part of the sql syntax.
     *
     * @param names The table columns which are used.
     * @param values The content of the used table columns.
     *
     * @return a generated part of the sql syntax.
     */
    private String buildWhereClause(String[] names, Object[] values) {
        String clause = "WHERE";

        for(int i = 0; i < names.length; i++) {
            if(values[i] != null) {
                if(i <= 0) {
                    clause += " " + names[i] + "=?";
                } else {
                    clause += " AND " + names[i] + "=?";
                }
            } else {
                if(i <= 0) {
                    clause += " " + names[i] + " IS NULL";
                } else {
                    clause += " AND " + names[i] + " IS NULL";
                }
            }
        }

        return clause;
    }

    /**
     * Returns a generated part of the sql syntax.
     *
     * @param names The table columns which are used.
     *
     * @return a generated part of the sql syntax.
     */
    private String buildSetClause(String[] names) {
        String clause = "SET";

        for(int i = 0; i < names.length; i++) {
            if(i <= 0) {
                clause += " " + names[i] + "=?";
            } else {
                clause += "," + names[i] + "=?";
            }
        }

        return clause;
    }

    private String buildString(String[] strings) {
        String string = "";

        for(int i = 0; i < strings.length; i++) {
            if(i < strings.length - 1) {
                string += strings[i] + ",";
            } else {
                string += strings[i];
            }
        }

        return string;
    }

    private String buildQuestionMarkString(int length) {
        final String[] strings = new String[length];

        for(int i = 0; i < length; i++) {
            strings[i] = "?";
        }

        return buildString(strings);
    }
}
