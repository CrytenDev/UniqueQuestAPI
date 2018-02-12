package de.cryten.sql;

import de.cryten.tool.mysql.ConnectionPool;
import de.cryten.tool.mysql.common.table.MysqlTable;
import de.cryten.tool.mysql.common.table.column.Column;
import de.cryten.tool.mysql.common.table.column.ColumnType;

public class MySQLTables {
	
	public static MysqlTable questtracker;
	public static MysqlTable quests;
	public static MysqlTable coins;
	public static MysqlTable itemreward;
	public static MysqlTable playtime;
	public static ConnectionPool connectionPool;
	static MySQLData data = new MySQLData();
    /**
     * Load tables from Mysql HikariCP.
     */
	public static void connectMySQL() {
		connectionPool = new ConnectionPool(
				data.getHost(), 
				data.getPort(), 
				data.getDatabase(), 
				data.getUsername(), 
				data.getPassword()
		);
        connectionPool.setMaxPoolSize(data.getPoolsize());
        connectionPool.open();
	}
	
	public static void loadTable() {
		questtracker = new MysqlTable(connectionPool, "Questtracker",
                 new Column("ID"),
                 new Column("QUESTID", ColumnType.INT, 10),
                 new Column("NAME", ColumnType.VARCHAR, 20),
                 new Column("UUID", ColumnType.VARCHAR, 64),
                 new Column("QUESTTRACKER", ColumnType.INT, 11),
                 new Column("GATHERTRACKER", ColumnType.INT, 50),
                 new Column("KILLTRACKER", ColumnType.INT, 50),
                 new Column("PLAYERTRACKER", ColumnType.INT, 50));
        
        quests = new MysqlTable(connectionPool, "Quests",
                 new Column("QUESTID", ColumnType.INT, 11),
                 new Column("QUESTNAME", ColumnType.VARCHAR, 50),
                 new Column("QUESTTEXT", ColumnType.VARCHAR, 200),
                 new Column("ITEM", ColumnType.VARCHAR, 50),
                 new Column("VALUE", ColumnType.INT, 11),
                 new Column("GATHER", ColumnType.INT, 50),
                 new Column("KILLCOUNTER", ColumnType.INT, 50),
                 new Column("MOBNAME", ColumnType.VARCHAR, 50),
                 new Column("PLAYERTOKILL", ColumnType.INT, 50),
                 new Column("REWARD", ColumnType.INT, 20),
                 new Column("STATE", ColumnType.INT, 11));
        
        coins = new MysqlTable(connectionPool, "Coins",
                new Column("UUID", ColumnType.VARCHAR, 64),
                new Column("Coins", ColumnType.INT, 20));
        
        itemreward = new MysqlTable(connectionPool, "Itemreward",
                new Column("UUID", ColumnType.VARCHAR, 64),
                new Column("STATE", ColumnType.INT, 1),
        		new Column("ITEMS", ColumnType.LONGTEXT));
        
        playtime = new MysqlTable(connectionPool, "Citytime", 
        		new Column("UUID", ColumnType.VARCHAR, 64),
        		new Column("DAY", ColumnType.INT, 11),
        		new Column("HOURS", ColumnType.INT, 11),
        		new Column("MINUTES", ColumnType.INT, 11));
        		
        questtracker.create();
        itemreward.create();
        quests.create();
        coins.create();
        playtime.create();
	}
}
