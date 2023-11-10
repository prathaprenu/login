import com.prathap.web.service.Accounts;
import com.prathap.web.service.Mailbox;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Inbox extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String emailId = req.getParameter("email");


        List<HashMap<String, Object>> result = null;
        try {
            result = Accounts.checkEmail(emailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        int id = (int) result.get(0).get("id");


        List<HashMap<String, Object>> result1 = null;
        try {
            result1 = Mailbox.unreadMail(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<HashMap<String, Object>> result2 = null;
        try {
            result2 = Mailbox.receiveMail(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JSONObject json = new JSONObject();
        json.put("unread_count",result1.size());
        json.put("received_count",result2.size());
        out.println(json);
    }
}

