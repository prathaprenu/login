package com.prathap.web.service;

import com.prathap.web.Database.JDBCConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import  java.util.Date;


public class Mailbox {

    public static List<HashMap<String, Object>> sendMailService(int id, int receiver_id, String message, String subject) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "INSERT INTO MailBox(m_sender,m_receiver,m_message,m_subject,m_status,m_sender_time)VALUES(?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(query);

        pst.setInt(1, id);
        pst.setInt(2, receiver_id);
        pst.setString(3, message);
        pst.setString(4, subject);
        pst.setInt(5, 0);

        Date date = new Date();
        String ts = String.valueOf(date.getTime());
        pst.setString(6, ts);

        int result = pst.executeUpdate();

        List<HashMap<String, Object>> result1 =null;
        if (result > 0) {
            String query1 = "SELECT * FROM MAILBOX WHERE m_sender_time=?";
            PreparedStatement pst1 = conn.prepareStatement(query1);
            pst1.setString(1, ts);

            ResultSet rs = pst1.executeQuery();
            result1 = convertResultSetToList(rs);
        }
        conn.close();
        pst.close();
        return result1;
    }


    public static List<HashMap<String, Object>> sentMail(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * FROM MAILBOX WHERE m_sender=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        conn.close();
        pst.close();
        return result;
    }

    public static List<HashMap<String, Object>> receiveMail(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from MAILBOX WHERE m_receiver=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        conn.close();
        pst.close();
        return result;
    }

    public static int statusUpdate(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "UPDATE MAILBOX SET m_status=1 WHERE m_receiver=?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id);

        int result = pst.executeUpdate();

        conn.close();
        pst.close();

        return result;
    }


    public static List<HashMap<String, Object>> unreadMail(int id) throws SQLException {
        Connection conn = JDBCConnection.getConnect();
        String query = "SELECT * from MAILBOX WHERE m_receiver=? and m_status = 0";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();
        List<HashMap<String, Object>> result = convertResultSetToList(rs);

        conn.close();
        pst.close();
        return result;
    }

    public static List<HashMap<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
}




