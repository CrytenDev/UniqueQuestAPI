package de.cryten.tool.mysql;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * This class is responsible for the connection. HikariCP plays an important role in this, as it provides a connection
 * pool. More informations about HikariCP can be found here https://github.com/brettwooldridge/HikariCP.
 *
 * @author Florian Heitzmann
 */
public class ConnectionPool {
    private final String hostname, database, username, password;
    private final int port;
    private int maxPoolSize = 5;
    private HikariDataSource source = null;
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    /**
     * Initialize the mysql connection data. To connect to the database, the {@link #open()} method must be run.
     * <br>
     * <b>TIPP:</b> Use the {@link #setMaxPoolSize(int)} method.
     *
     * @param hostname Name of the server, for example <b>127.0.0.1, localhost or www.scarpex.biz</b>.
     * @param port Port of the mysql server, for example <b>3306</b>.
     * @param database Name of the database.
     * @param username Username from database account.
     * @param password Password from database account.
     */
    public ConnectionPool(String hostname, int port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    /**
     * Set the maximum size that the pool is allowed to reach, including both idle and in-use connections. The default
     * size the pool is allowed to reach is 5.
     *
     * @param size Size of the maximum allowed connections.
     */
    public void setMaxPoolSize(int size) {
        this.maxPoolSize = size;
    }

    /**
     * Check the connection and get a result of the current state.
     *
     * @return true if the connection is closed, false otherwise.
     */
    public boolean isClosed() {
        return this.source == null || this.source.isClosed();
    }

    /**
     * Connects to the database and creates the connection pool.
     *
     * @return true if successfully created a connection to database.
     */
    public synchronized boolean open() {
        final HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setMaximumPoolSize(this.maxPoolSize);

        final ThreadFactoryBuilder factory = new ThreadFactoryBuilder();
        factory.setNameFormat("HikariThread-%d");
        config.setThreadFactory(factory.build());

        try {
            this.source = new HikariDataSource(config);
        } catch(Exception e) {
        	console.sendMessage("§cHikariCP: Wrong login data or mysql server configurations.");
            e.printStackTrace();
        }

        if(!this.source.isClosed()) {
        	console.sendMessage("§2HikariCP: Successfully connected to the database.");
            return true;
        }
        console.sendMessage("§2HikariCP: Could not connect to the database.");
        return false;
    }

    /**
     * Closes the connection pool.
     */
    public synchronized void close() {
        if(this.source != null) {
            this.source.close();
            this.source = null;
        }
    }

    /**
     * Returns a {@linkplain Connection} from connection pool.
     * <br>
     * <b>IMPORTANT:</b> Do not forget to close the connection after usage.
     *
     * @return a connection.
     */
    public Connection getConnection() {
        Connection connection = null;

        if(isClosed()) {
            open();
        }

        try {
            connection = this.source.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }


    /**
     * For security the connection pool is closed, if someone forgets it.
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
