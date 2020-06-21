package banking;

import java.sql.*;

public class MyDatabase {
    private static MyDatabase db = null;
    private static String file = "";

    private MyDatabase() {
    }

    public static MyDatabase getInstance() {
        if (db == null) {
            db = new MyDatabase();
        }
        return db;
    }

    private static Connection getCon() {
        Connection con = null;

        String url = "jdbc:sqlite:" + file;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }

    public static void createTable(String filename) {
        String sql = "CREATE TABLE IF NOT EXISTS card(\n"
                + "id INTEGER,\n"
                + "number TEXT,\n"
                + "pin TEXT,\n"
                + "balance INTEGER DEFAULT 0\n"
                + ");";
        file = filename;
        try (Connection c = getCon()) {
            Statement stm = c.createStatement();
            stm.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insert(int id, String number, String pin, int balance) {
        String sql = "INSERT INTO card(id,number,pin,balance) VALUES (?,?,?,?);";
        try (Connection c = getCon()) {
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, number);
            statement.setString(3, pin);
            statement.setInt(4, balance);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static Bank result(String card) {
        String sql = "SELECT * FROM card WHERE number=?";
        Bank bank = null;
        try (Connection c = getCon()) {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setString(1, card);
            ResultSet rs = pstm.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("Wrong card number or PIN!\n");
                System.out.println();
            } else {
                bank = new Bank(rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bank;
    }

    public static void update(String card, int balance) {
        String sql = "UPDATE balance FROM card VALUES(?) WHERE number=?";
        try (Connection c = getCon()) {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setInt(1, balance);
            pstm.setString(2, card);
            pstm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void delete(String card) {
        String sql = "DELETE FROM card WHERE number=?";
        try (Connection c = getCon()) {
            PreparedStatement pstm = c.prepareStatement(sql);
            pstm.setString(1, card);
            pstm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
