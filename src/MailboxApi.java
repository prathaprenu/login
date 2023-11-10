import com.prathap.web.service.Accounts;
import com.prathap.web.service.Mailbox;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
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

import static com.prathap.web.service.Accounts.checkEmail;

@WebServlet(urlPatterns = {"/mailbox/*"})

public class MailboxApi extends HttpServlet {
    public void service (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String path = req.getPathInfo();

        switch (path) {
            case "/send":
                try {
                    sendMail(req, res);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/inbox":
                try {
                    inbox(req, res);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/unread":
                try {
                    unreadMail(req, res);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/receive":
                try {
                    receiveMail(req, res);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/sent":
                try {
                    sentMail(req, res);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                res.setContentType("application/json");
                PrintWriter out = res.getWriter();
                JSONObject json = new JSONObject();
                json.put("message","not match the path name ");
                json.put("status","failed");
                json.put("status_code","400");
                out.println(json);
        }
    }

    public void sendMail (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String sender = req.getParameter("sender");
        String receiver = req.getParameter("receiver");
        String message = req.getParameter("message");
        String subject = req.getParameter("subject");


        List<HashMap<String, Object>> result1;
        try {
             result1 = Accounts.checkEmail(sender);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<HashMap<String, Object>> result2;
        try {
            result2 = Accounts.checkEmail(receiver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        try{
            int sender_id = (int) result1.get(0).get("id");
            int receiver_id = (int) result2.get(0).get("id");

            JSONObject json = new JSONObject();

            List<HashMap<String, Object>> result3;
            try {
                result3 = Mailbox.sendMailService(sender_id, receiver_id, message, subject);

                if (result3.size() > 0) {
                    json.put("from_mail", result1.get(0).get("email_id"));
                    json.put("to_mail", result2.get(0).get("email_id"));

                    json.put("id", result3.get(0).get("m_id"));
                    json.put("message", result3.get(0).get("m_message"));
                    json.put("subject", result3.get(0).get("m_subject"));
                    json.put("status", result3.get(0).get("m_status"));

                    json.put("message1", "Mail send successfully");
                    json.put("status", "success");
                    json.put("status_code", "200");
                } else {
                    json.put("message", "mail id is not match");
                    json.put("status", "failed");
                    json.put("status_code", "400");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            out.println(json);
        }
        catch (Exception e){
            JSONObject json = new JSONObject();
            json.put("message", "id is not match");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }
    }

    public void inbox (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String emailId = req.getParameter("email");


        List<HashMap<String, Object>> result;
        try {
            result = Accounts.checkEmail(emailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        try {
            int id = (int) result.get(0).get("id");

            List<HashMap<String, Object>> result1;
            try {
                result1 = Mailbox.unreadMail(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            List<HashMap<String, Object>> result2;
            try {
                result2 = Mailbox.receiveMail(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            JSONObject json = new JSONObject();
            json.put("unread_count",result1.size());
            json.put("received_count",result2.size());
            json.put("status", "success");
            json.put("status_code", "200");
            out.println(json);
        }
        catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("message", "mail id is not match");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }
    }

    public void unreadMail (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String emailId = req.getParameter("email");


        List<HashMap<String, Object>> result;
        try {
            result = Accounts.checkEmail(emailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        try {
            int id = (int) result.get(0).get("id");

            List<HashMap<String, Object>> result1;
            try {
                result1 = Mailbox.unreadMail(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JSONArray jsonArray =new JSONArray();
            if (result1.size() > 0) {

                for (int i = 0; i < result1.size(); i++)
                {
                    JSONObject json = new JSONObject();

                    int id1 = (int) result1.get(i).get("m_sender");

                    List<HashMap<String, Object>> result2;
                    try {
                        result2 = Accounts.checkId(id1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                    json.put("from_mail", result2.get(0).get("email_id"));
                    json.put("to_mail", result.get(0).get("email_id"));

                    json.put("id",result1.get(i).get("m_id"));
                    json.put("massages",result1.get(i).get("m_message"));
                    json.put("subject",result1.get(i).get("m_subject"));
                    json.put("status",result1.get(i).get("m_status"));


                    String ap = (String) result1.get(i).get("m_sender_time");
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm ");
                    long milliSeconds = Long.parseLong(ap);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliSeconds);
                    json.put("send_mail_date_and_time",formatter.format(calendar.getTime()));

                    jsonArray.put(json);
                }
                JSONObject json = new JSONObject();
                json.put("status", "success");
                json.put("status_code", "200");
                json.put("unread_mail",jsonArray);
                out.println(json);

                try {
                    Mailbox.statusUpdate(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                JSONObject json = new JSONObject();
                json.put("message","You have 0 unread mails.");
                json.put("status", "failed");
                json.put("status_code", "400");
                out.println(json);
            }

        }
        catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("message", "mail id is not match");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }
    }

    public void receiveMail (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String emailId = req.getParameter("email");


        List<HashMap<String, Object>> result;
        try {
            result = Accounts.checkEmail(emailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        try {
            int id = (int) result.get(0).get("id");

            List<HashMap<String, Object>> result1;
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

                    int id1 = (int) result1.get(i).get("m_sender");

                    List<HashMap<String, Object>> result2;
                    try {
                        result2 = Accounts.checkId(id1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                    json.put("from_mail", result2.get(0).get("email_id"));
                    json.put("to_mail", result.get(0).get("email_id"));

                    json.put("id", result1.get(i).get("m_id"));
                    json.put("massages", result1.get(i).get("m_message"));
                    json.put("subject", result1.get(i).get("m_subject"));
                    json.put("status", result1.get(i).get("m_status"));


                    String ap =(String)result1.get(i).get("m_sender_time");
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm ");
                    long milliSeconds= Long.parseLong(ap);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliSeconds);
                    json.put("send_mail_date_and_time", formatter.format(calendar.getTime()));

                    jsonArray.put(json);
                }
                JSONObject json = new JSONObject();
                json.put("status", "success");
                json.put("status_code", "200");
                json.put("receive_mail",jsonArray);
                out.println(json);
            }
            else
            {
                JSONObject json = new JSONObject();
                json.put("message","You have 0 receive mails");
                json.put("status", "failed");
                json.put("status_code", "400");
                out.println(json);
            }
        }
        catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("message", "mail id is not match");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }
    }


    public void sentMail (HttpServletRequest req, HttpServletResponse res) throws IOException{


        String emailId = req.getParameter("email");


        List<HashMap<String, Object>> result;
        try {
            result = checkEmail(emailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        try {
            int id = (int) result.get(0).get("id");

            List<HashMap<String, Object>>result1;
            try {
                result1 = Mailbox.sentMail(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            JSONArray jsonArray =new JSONArray();
            if (result1.size() > 0) {
                for (int i = 0 ; i < result1.size(); i++) {
                    JSONObject json = new JSONObject();

                    int id1 = (int) result1.get(i).get("m_receiver");

                    List<HashMap<String, Object>> result2;
                    try {
                        result2 = Accounts.checkId(id1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    json.put("from_mail", result.get(0).get("email_id"));
                    json.put("to_mail", result2.get(0).get("email_id"));

                    json.put("id", result1.get(i).get("m_id"));
                    json.put("massages", result1.get(i).get("m_message"));
                    json.put("subject", result1.get(i).get("m_subject"));
                    json.put("status", result1.get(i).get("m_status"));


                    String ap = (String) result1.get(i).get("m_sender_time");
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm ");
                    long milliSeconds = Long.parseLong(ap);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliSeconds);
                    json.put("sent_mail_date_and_time", formatter.format(calendar.getTime()));

                    jsonArray.put(json);
                }
                JSONObject json = new JSONObject();
                json.put("status", "success");
                json.put("status_code", "200");
                json.put("sent_mail",jsonArray);
                out.println(json);
            }
            else {
                JSONObject json = new JSONObject();
                json.put("message","You have 0 sent mails");
                json.put("status", "failed");
                json.put("status_code", "400");
                out.println(json);
            }
        }
        catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("message", "mail id is not match");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }
    }
}
