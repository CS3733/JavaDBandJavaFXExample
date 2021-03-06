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

    /**
     * A class representing a database with the given name.
     *
     * @param dbName The name of the database.
     */
    public Database(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Connect to the database.
     *
     * @return True if the database was successfully connected to.
     */
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

    /**
     * Drop the table specified.
     *
     * @param tableName The table name
     * @return True if it was able to drop the table without error.
     */
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


    /**
     * Create a table with the columns given.
     *
     * @param tableName The name of the table.
     * @param entities  The entities of the table (columns)
     * @return True if the table was created without error.
     */
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


    /**
     * Insert values into the table.
     *
     * @param values    The values in order of their respective columns where strings have '' around them.
     * @param tableName The name of the table.
     * @return True if the values were inserted without error.
     */
    public boolean insert(String values, String tableName) {
        if (!connected)
            return false;
        try {
            statement.execute("INSERT INTO " + tableName + " VALUES ( " + values + " )");
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

    /**
     * Close the database.
     *
     * @return True if the database was successfully closed.
     */
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
