import com.prathap.web.service.Accounts;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

//@WebServlet(urlPatterns = {"/login"})

@WebServlet(urlPatterns = {"/login", "/greeting"})
public class Login extends HttpServlet {
    public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String emailId = req.getParameter("email");
        String password = req.getParameter("password");


        List<HashMap<String, Object>> result = null;
        try {
            result = Accounts.loginService(emailId, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        JSONObject json = new JSONObject();

        if (result.size() > 0) {

                json.put("message","login successfully");
                json.put("id",result.get(0).get("id"));
                json.put("first Name",result.get(0).get("first_name"));
                json.put("last Name",result.get(0).get("last_name"));
                json.put("email Id",result.get(0).get("email_id"));
                json.put("password",result.get(0).get("password"));
                json.put("mobile Number",result.get(0).get("mobile_number"));
            }
        else
        {
            json.put("message","login password not match ");
        }
        out.println(json);
    }
}

