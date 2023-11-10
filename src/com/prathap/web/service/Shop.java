package com.prathap.web.service;

import com.prathap.web.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static com.prathap.web.service.Accounts.convertResultSetToList;



public class Shop {
    public static List<HashMap<String, Object>> category_list() throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  category_list ";
        PreparedStatement pst = conn.prepareStatement(query);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> shop_details() throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  shopdetails";
        PreparedStatement pst = conn.prepareStatement(query);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> shop_details(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query ="SELECT * from  shopdetails where users1_id=?";
        PreparedStatement pst=conn.prepareStatement(query);
        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> check_same_item(int shop_id,int item_id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query ="SELECT * from  items_table where shop_id=? and item_id=?";
        PreparedStatement pst=conn.prepareStatement(query);
        pst.setInt(1, shop_id);
        pst.setInt(2, item_id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static  List<HashMap<String, Object>> find_item_id(int user_req) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query ="SELECT * from  item_list where id=?";
        PreparedStatement pst=conn.prepareStatement(query);
        pst.setInt(1, user_req);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> check_shop_item(int shop_id,int item_id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query ="SELECT * from  shopdetails where id=? and category_id=?";
        PreparedStatement pst=conn.prepareStatement(query);
        pst.setInt(1,shop_id);
        pst.setInt(2, item_id);


        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> shopDetailsService(int id, String shop_name, int category, String address, String contact, boolean shop_available) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "INSERT INTO shopdetails(users1_id,shop_name,category_id,address,contact,open_time,close_time,shop_available)VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(query);

        pst.setInt(1, id);
        pst.setString(2, shop_name);
        pst.setInt(3, category);
        pst.setString(4, address);
        pst.setString(5, contact);
        pst.setString(6, String.valueOf(1690082801));
        pst.setString(7, String.valueOf(1690039802));
        pst.setBoolean(8, shop_available);

        int result = pst.executeUpdate();

        List<HashMap<String, Object>> result1 = null;
        if(result > 0 ) {
            String query1 = "SELECT * FROM shopdetails WHERE users1_id=?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setInt(1,id);

            ResultSet rs = pst1.executeQuery();
            result1 = convertResultSetToList(rs);
        }

        conn.close();
        pst.close();
        return result1;
    }

    public static List<HashMap<String, Object>> item_list() throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  item_list ";
        PreparedStatement pst = conn.prepareStatement(query);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static   List<HashMap<String, Object>> add_item(String name, int category_of_item) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "INSERT INTO item_list(name,category_of_item)VALUES(?,?)";
        PreparedStatement pst = conn.prepareStatement(query);

        pst.setString(1, name);
        pst.setInt(2, category_of_item);

        int result = pst.executeUpdate();

        List<HashMap<String, Object>> result1 =null;
        if (result > 0) {
            String query1 = "SELECT * FROM item_list WHERE name=?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setString(1,name);
            ResultSet rs = pst1.executeQuery();
            result1 = convertResultSetToList(rs);
        }
        conn.close();
        pst.close();
        return result1;

    }

    public static List<HashMap<String, Object>> add_category(String name) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "INSERT INTO category_list(name)VALUES(?)";
        PreparedStatement pst = conn.prepareStatement(query);

        pst.setString(1, name);
        int result = pst.executeUpdate();

        List<HashMap<String, Object>> result1 =null;
        if (result > 0) {
            String query1 = "SELECT * FROM category_list WHERE name=?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setString(1,name);
            ResultSet rs = pst1.executeQuery();
            result1 = convertResultSetToList(rs);
        }

        conn.close();
        pst.close();
        return result1;
    }

    public static List<HashMap<String, Object>> find_item(String item) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  item_list WHERE name=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1,item);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> find_category(String name) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  category_list WHERE name=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1,name);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> find_city(String address) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  shopdetails WHERE address=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1,address);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> find(int shop_id,int item_id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  items_table WHERE shop_id=? and item_id=? and items_available=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1,shop_id);
        pst.setInt(2,item_id);
        pst.setBoolean(3,true);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> find_shop(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from  shopdetails WHERE id=? and shop_available=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1,id);
        pst.setBoolean(2,true);


        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        pst.close();
        conn.close();
        return result;
    }

    public static List<HashMap<String, Object>> itemsTableService(int shop_id, int item_id, int cost, boolean items_available) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "INSERT INTO items_table(shop_id,item_id,cost,items_available)VALUES(?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(query);

        pst.setInt(1, shop_id);
        pst.setInt(2, item_id);
        pst.setInt(3, cost);
        pst.setBoolean(4, items_available);

        int result = pst.executeUpdate();

        List<HashMap<String, Object>> result1 = null;
        if(result > 0) {
            String query1 = "SELECT * FROM items_table WHERE shop_id=?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setInt(1,shop_id);

            ResultSet rs = pst1.executeQuery();
            result1 = convertResultSetToList(rs);
        }

        conn.close();
        pst.close();
        return result1;

    }

}
