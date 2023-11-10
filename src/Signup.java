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

public class Signup extends HttpServlet {

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String firstName = req.getParameter("firstname");
        String lastName = req.getParameter("lastname");
        String emailId = req.getParameter("email");
        String password = req.getParameter("password");
        String mobileNumber = req.getParameter("mobile_number");


        List<HashMap<String, Object>> result = null;
        try {
            result = Accounts.signupService(firstName, lastName, emailId, password, mobileNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        JSONObject json = new JSONObject();

        if (result.size() > 0)
        {
            json.put("message", "signup successfully");
        }
        out.println(json);
    }
}


