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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReceiveMail extends HttpServlet {

    public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {

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
             result1 = Mailbox.receiveMail(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JSONArray jsonArray =new JSONArray();
        if (result1.size() > 0)
        {

            for (int i = 0; i < result1.size(); i++)
            {
                JSONObject json = new JSONObject();


//                json.put("from", result1.get(i).get("m_sender"));

                int id1 = (int) result1.get(i).get("m_receiver");

                List<HashMap<String, Object>> result2;
                try {
                    result2 = Accounts.checkId(id1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                json.put("from", result2.get(0).get("email_id"));
                json.put("to", result.get(0).get("email_id"));

                json.put("massages", result1.get(i).get("m_message"));
                json.put("subject ", result1.get(i).get("m_subject"));


                String ap =(String)result1.get(i).get("m_sender_time");
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm ");
                long milliSeconds= Long.parseLong(ap);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(milliSeconds);
                json.put("send mail date and time", formatter.format(calendar.getTime()));

                jsonArray.put(json);
            }
            out.println(jsonArray);
        }
        else
        {
            JSONObject json = new JSONObject();
            json.put("message","You have 0 receive mails");
            out.println(json);
        }

    }
}
