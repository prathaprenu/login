import com.prathap.web.service.Accounts;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ChangePassword extends HttpServlet {

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String emailId = req.getParameter("email");
        String Password = req.getParameter("password");
        String newPassword = req.getParameter("new_password");


        List<HashMap<String, Object>> result = null;
        try {
            result = Accounts.loginService(emailId, Password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        List<HashMap<String, Object>> rs = null;
        if (result.size() > 0) {
            try {
                rs = Accounts.checkPasswordService(Password, (int) result.get(0).get("id"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();

            JSONObject json = new JSONObject();

            json.put("message", "login failed");

            out.println(json);
        }


        List<HashMap<String, Object>> result2 =null;
        if (rs.size() > 0) {
            try {
                result2 = Accounts.changePasswordService(newPassword, (int) rs.get(0).get("id"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();

            JSONObject json = new JSONObject();

            json.put("message", "Password is not match");
            out.println(json);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        JSONObject json = new JSONObject();

        if (result.size() > 0) {

            json.put("message", "login successfully");
        }

        if (rs.size() > 0) {

            json.put("message", " password is match");
        }

        if (result2.size() > 0) {

            json.put("message", "Change Password successfully");
            out.println(json);
        }
        else {

            json.put("message", "Password unsuccessfully");
            out.println(json);
        }
    }
}
