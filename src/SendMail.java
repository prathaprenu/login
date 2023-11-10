import com.prathap.web.service.Accounts;
import com.prathap.web.service.Mailbox;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;

import java.util.HashMap;
import java.util.List;



public class SendMail extends HttpServlet {
    public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {


        String sender = req.getParameter("sender");
        String receiver = req.getParameter("receiver");
        String message = req.getParameter("message");
        String subject = req.getParameter("subject");


        List<HashMap<String, Object>> result1=null;
        try {
            result1 = Accounts.checkEmail(sender);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<HashMap<String, Object>> result2=null;
        try {
            result2 = Accounts.checkEmail(receiver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();



        int sender_id = (int) result1.get(0).get("id");
        int receiver_id = (int) result2.get(0).get("id");

        List<HashMap<String, Object>> result3;
            try {
                 result3 = Mailbox.sendMailService(sender_id, receiver_id, message, subject);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        JSONObject json = new JSONObject();
        if(result3.size() > 0)
        {

            json.put("from", result1.get(0).get("email_id"));
            json.put("to", result2.get(0).get("email_id"));
            json.put("message", result3.get(0).get("m_message"));
            json.put("subject", result3.get(0).get("m_subject"));

            json.put("message1","Mail sent successfully");
        }
        else {
            json.put("message", "mail id is not match");
        }
        out.println(json);
    }
}
