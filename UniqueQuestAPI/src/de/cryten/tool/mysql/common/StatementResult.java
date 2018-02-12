package de.cryten.tool.mysql.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class returns the result of the statement. It is important to use the {@link #close()} method at the end.
 *
 */
public class StatementResult implements AutoCloseable {
    private final Connection connection;
    private final PreparedStatement preparedStatement;
    private final ResultSet resultSet;

    /**
     * Initialize the required data.
     *
     * @param connection The connection to the mysql database.
     * @param preparedStatement The used statement.
     */
    public StatementResult(Connection connection, PreparedStatement preparedStatement) throws SQLException {
        this.connection = connection;
        this.preparedStatement = preparedStatement;
        this.resultSet = preparedStatement.getResultSet();
    }

    /**
     * Returns the result of the statement.
     *
     * @return the result of the statement.
     */
    public ResultSet receive() {
        return this.resultSet;
    }

    /**
     * Closes all connections after usage.
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        this.resultSet.close();
        this.preparedStatement.close();
        this.connection.close();
    }
}
