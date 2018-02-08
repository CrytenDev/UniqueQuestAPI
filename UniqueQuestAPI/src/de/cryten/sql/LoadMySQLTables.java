package de.cryten.sql;

import de.cryten.tool.mysql.common.table.MysqlTable;
import de.cryten.tool.mysql.common.table.column.Column;
import de.cryten.tool.mysql.common.table.column.ColumnType;

public class LoadMySQLTables {
	
	public static MysqlTable questtracker;
	public static MysqlTable quests;
	public static MysqlTable tokens;
	
    /**
     * Load tables from Mysql HikariCP.
     */
	public static void loadTable() {
		questtracker = new MysqlTable(MySQL.connectionPool, "Questtracker",
                 new Column("ID"),
                 new Column("QUESTID", ColumnType.INT, 10),
                 new Column("NAME", ColumnType.VARCHAR, 20),
                 new Column("UUID", ColumnType.VARCHAR, 64),
                 new Column("QUESTTRACKER", ColumnType.INT, 11),
                 new Column("GATHERTRACKER", ColumnType.INT, 50),
                 new Column("KILLTRACKER", ColumnType.INT, 50),
                 new Column("PLAYERTRACKER", ColumnType.INT, 50));
        
        quests = new MysqlTable(MySQL.connectionPool, "Quests",
                 new Column("QUESTID", ColumnType.INT, 11),
                 new Column("QUESTNAME", ColumnType.VARCHAR, 50),
                 new Column("QUESTTEXT", ColumnType.VARCHAR, 200),
                 new Column("ITEMID", ColumnType.VARCHAR, 50),
                 new Column("VALUE", ColumnType.INT, 11),
                 new Column("GATHER", ColumnType.INT, 50),
                 new Column("KILLCOUNTER", ColumnType.INT, 50),
                 new Column("MOBNAME", ColumnType.VARCHAR, 50),
                 new Column("PLAYERTOKILL", ColumnType.INT, 50),
                 new Column("REWARD", ColumnType.INT, 20),
                 new Column("STATE", ColumnType.INT, 11));
        
        tokens = new MysqlTable(MySQL.connectionPool, "Tokens",
                new Column("UUID", ColumnType.VARCHAR, 64),
                new Column("TOKENS", ColumnType.INT, 20));
        
        questtracker.create();
        quests.create();
        tokens.create();
	}
}
