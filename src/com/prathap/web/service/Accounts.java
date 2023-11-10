package com.prathap.web.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import  com.prathap.web.Database.JDBCConnection;

public class Accounts {

    public static List<HashMap<String,Object>> loginService(String verify, String password)throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * FROM USERS1 WHERE (email_id=? or mobile_number=?) AND password=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, verify);
        pst.setString(2, verify);
        pst.setString(3, password);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String,Object>> result= convertResultSetToList(rs);

        conn.close();
        pst.close();
        return result;
    }

    public static List<HashMap<String, Object>> signupService(String first_name, String last_name, String email_id, String password, String mobile_number) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "INSERT INTO USERS1(first_name,last_name,email_id,password,mobile_number)VALUES(?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(query);

        pst.setString(1, first_name);
        pst.setString(2, last_name);
        pst.setString(3, email_id);
        pst.setString(4, password);
        pst.setString(5, mobile_number);

        int result=pst.executeUpdate();

        List<HashMap<String, Object>> result1 =null;
        if (result > 0) {
            String query1 = "SELECT * FROM USERS1 WHERE email_id=?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setString(1,email_id);

            ResultSet rs = pst1.executeQuery();
            result1 = convertResultSetToList(rs);
        }

        conn.close();
        pst.close();
        return result1;
    }

    public static List<HashMap<String,Object>> checkPasswordService(String oldPassword,int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * FROM  USERS1 WHERE password=? AND id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, oldPassword);
        pst.setInt(2, id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String,Object>> result= convertResultSetToList(rs);

        conn.close();
        pst.close();
        return result;
    }

    public static List<HashMap<String, Object>> changePasswordService(String confirmPassword, int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "UPDATE USERS1 SET password=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, confirmPassword);
        pst.setInt(2, id);

        int result=pst.executeUpdate();

        List<HashMap<String, Object>> result1 =null;
        if (result > 0) {
            String query1 = "SELECT * FROM USERS1 WHERE id=?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setInt(1,id);

            ResultSet rs = pst1.executeQuery();
            result1 = convertResultSetToList(rs);
        }

        conn.close();
        pst.close();
        return result1;
    }
    public static List<HashMap<String, Object>> checkEmail(String email_id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * FROM  USERS1 WHERE  email_id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, email_id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        conn.close();
        pst.close();
        return result;
    }
    public static List<HashMap<String, Object>> checkId(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * FROM  USERS1 WHERE  id=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1,id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        conn.close();
        pst.close();
        return result;
    }

    public static List<HashMap<String,Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        while (rs.next())
        {
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i)
            {
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
}

