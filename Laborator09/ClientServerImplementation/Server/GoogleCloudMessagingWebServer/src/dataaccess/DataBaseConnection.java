package dataaccess;

import general.Constants;
import general.Utilities;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBaseConnection {
    public static Connection        dbConnection;
    public static DatabaseMetaData  dbMetaData;
    public static Statement         stmt;

    public DataBaseConnection() { }

    public static void openConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
        } catch (ClassNotFoundException exception) {
            System.out.println ("exceptie: "+exception.getMessage());
            if (Constants.DEBUG)
                exception.printStackTrace();
        }
        dbConnection    = DriverManager.getConnection(general.Constants.DATABASE_CONNECTION, general.Constants.DATABASE_USER, general.Constants.DATABASE_PASSWORD);
        dbMetaData      = dbConnection.getMetaData();
        stmt            = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public static void closeConnection() throws SQLException {
        stmt.close();
        dbConnection.close();
    }
    
    public static int getTableNumberOfColumns(String tableName) throws SQLException {
        int result = 0;
        openConnection();
        ResultSet RS = dbMetaData.getColumns(Constants.DATABASE_NAME, null, tableName, null);
        while (RS.next()) 
            result++;
        closeConnection();
        return result;
    }

    public static String getTablePrimaryKey(String tableName) throws SQLException {
        String result = new String();
        openConnection();
        ResultSet RS = dbMetaData.getPrimaryKeys(Constants.DATABASE_NAME, null, tableName);
        while (RS.next())
            result += RS.getString("COLUMN_NAME")+" ";
        closeConnection();
        return result != null ? result.trim() : result;
    }         
    
    public static ArrayList<String> getTableAttributes(String tableName) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        openConnection();
        ResultSet RS = dbMetaData.getColumns(Constants.DATABASE_NAME, null, tableName, null);
        while (RS.next())
            result.add(RS.getString("COLUMN_NAME"));
        closeConnection();
        return result;
    }

    public static ArrayList<ArrayList<Object>> getTableContent(String tableName, ArrayList<String> attributes, String whereClause, String orderByClause, String groupByClause) throws SQLException {
        String expression = "SELECT ";
        int numberOfColumns = -1;
        if (attributes == null) {
            numberOfColumns = getTableNumberOfColumns(tableName);
            expression += "*";            
        }
        else {
            numberOfColumns = attributes.size();
            for (String attribute:attributes) {
                expression += attribute+", ";
            }
            expression = expression.substring(0,expression.length()-2);            
        }
        expression += " FROM "+tableName;
        if (whereClause != null) {
            expression += " WHERE "+whereClause;
        }
        if (groupByClause != null) {
            expression += " GROUP BY "+groupByClause;
        }
        if (orderByClause != null) {
            expression += " ORDER BY "+orderByClause;
        }
        if (general.Constants.DEBUG) {
            System.out.println("query: "+expression);
        }
        if (numberOfColumns == -1) {
            return null;
        }      
        openConnection();
        ArrayList<ArrayList<Object>> dataBaseContent = new ArrayList<>();
        ResultSet result = stmt.executeQuery(expression);  
        int currentRow = 0;
        while (result.next()) {
            dataBaseContent.add(new ArrayList<>());
            for (int currentColumn = 0; currentColumn < numberOfColumns; currentColumn++) {
                dataBaseContent.get(currentRow).add(result.getString(currentColumn+1));
            }
            currentRow++;
        }
        closeConnection();
        return dataBaseContent;
    }

    public static void insertValuesIntoTable(String tableName, ArrayList<String> attributes, ArrayList<String> values, boolean skipPrimaryKey) throws Exception {
        String expression = "INSERT INTO "+tableName+" (";
        if (attributes == null) {
            attributes = getTableAttributes(tableName);
            if (skipPrimaryKey) {
                attributes.remove(0);
            }
        }
        if (attributes.size() != values.size()) {
            throw new Exception ("Attributes size does not match values size !"+attributes.size()+" "+values.size());
        }
        for (String attribute:attributes) {
            expression += attribute + ", ";
        }      
        expression = expression.substring(0,expression.length()-2);
        expression += ") VALUES (";
        for (String currentValue: values) {
           if (Utilities.isSystemFunction(currentValue))
                expression += currentValue+",";
            else
                expression += "\'"+currentValue+"\',";
        }
        expression = expression.substring(0,expression.length()-1);
        expression += ")";
        if (general.Constants.DEBUG) {
            System.out.println("query: "+expression);
        }
        openConnection();
        stmt.execute(expression);
        closeConnection();
    }

    public static void updateRecordsIntoTable(String tableName, ArrayList<String> attributes, ArrayList<String> values, String whereClause) throws Exception {
        String expression = "UPDATE "+tableName+" SET ";
        if (attributes == null) {
            attributes = getTableAttributes(tableName);
        }
        if (attributes.size() != values.size()) {
            throw new Exception ("Attributes size does not match values size.");
        }
        for (int currentIndex = 0; currentIndex < values.size(); currentIndex++)
            expression += attributes.get(currentIndex)+"=\'"+values.get(currentIndex)+"\', ";
        expression = expression.substring(0,expression.length()-2);
        expression += " WHERE ";
        if (whereClause != null ) {
            expression += whereClause;
        } else {
            expression += getTablePrimaryKey(tableName)+"=\'"+values.get(0)+"\'";
        }
        if (general.Constants.DEBUG) {
            System.out.println("query: "+expression);
        }
        openConnection();
        stmt.execute(expression);
        closeConnection();
    }

    public static void deleteRecordsFromTable(String tableName, ArrayList<String> attributes, ArrayList<String> values, String whereClause) throws Exception {
        String expression = "DELETE FROM "+tableName+" WHERE ";
        if (whereClause != null) {
            expression += whereClause;
        } else {
            if (attributes.size() != values.size()) {
                if (Constants.DEBUG) {
                    System.out.print("attributes: ");
                    for (String attribute:attributes)
                        System.out.print(attribute+" ");
                    System.out.println();
                    System.out.print("values: ");
                    for (String value:values)
                        System.out.print(value+" ");
                    System.out.println();               
                }
                throw new Exception ("Attributes size does not match values size !");
            }
            for (int currentIndex = 0; currentIndex < values.size(); currentIndex++) {
                expression += attributes.get(currentIndex)+"=\'"+values.get(currentIndex)+"\' AND";
            }
            expression = expression.substring(0,expression.length()-4);
        }
        if (general.Constants.DEBUG) {
            System.out.println("query: "+expression);
        }
        openConnection();        
        stmt.execute(expression);
        closeConnection();
    }
} 