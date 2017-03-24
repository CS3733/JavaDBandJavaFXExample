package sample;

import org.apache.derby.jdbc.EmbeddedDriver;

import java.sql.*;

/**
 * Created by Kyle on 3/24/2017.
 */
public class Database {

    private String dbName;
    private Connection connection;
    private boolean connected = false;
    private Statement statement;

    public Database(String dbName) {
        this.dbName = dbName;
    }

    public boolean connect() {
        try {
            DriverManager.registerDriver(new EmbeddedDriver());
            connection = DriverManager.getConnection("jdbc:derby:" + dbName + ";create=true");
            statement = connection.createStatement();
            connected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean dropTable(String tableName) {
        if (!connected)
            return false;

        try {
            statement.execute("DROP TABLE " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public boolean createTable(String tableName, TableEntity... entities) {
        if (!connected)
            return false;

        String entityString = "";

        for (TableEntity entity : entities) {
            entityString += entity.getName() + " " + entity.getType() + ",\n";
        }

        entityString = entityString.substring(0, entityString.length() - 2);
        try {
            statement.execute("CREATE TABLE " + tableName + "(\n" + entityString + "\n)");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean insert(String values, String table) {
        if (!connected)
            return false;
        try {
            statement.execute("INSERT INTO " + table + " VALUES ( " + values + " )");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Select values from the table.
     *
     * @param values The SQL statement for which values to select. Ex: "*"
     * @param table  The SQL table to run the select on.
     * @return The {@link ResultSet} from the select query.
     */
    public ResultSet select(String values, String table) {
        if (!connected) {
            System.err.println("Database was not connected to.");
            return null;
        }
        try {
            return statement.executeQuery("SELECT " + values + " FROM " + table);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Select values from the table.
     *
     * @param values The SQL statement for which values to select. Ex: "*"
     * @param table  The SQL table to run the select on.
     * @param where  The where clause to the SQL select query.
     * @return The {@link ResultSet} from the select query.
     */
    public ResultSet select(String values, String table, String where) {
        if (!connected) {
            System.err.println("Database was not connected to.");
            return null;
        }
        try {
            return statement.executeQuery("SELECT " + values + " FROM " + table + " WHERE " + where);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean close() {
        try {
            statement.close();
            connection.close();
            connected = false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static class TableEntity {
        private String name;
        private String type;

        public TableEntity(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

}
