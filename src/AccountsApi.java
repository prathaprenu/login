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


@WebServlet(urlPatterns = {"/accounts/*"})

public class AccountsApi extends HttpServlet {
      public void service (HttpServletRequest req, HttpServletResponse res) throws IOException {

          String path = req.getPathInfo();

          switch (path) {
              case "/login":
                  try {
                      login(req, res);
                  } catch (IOException e) {
                      throw new RuntimeException(e);
                  }
                  break;
              case "/signup":
                      try {
                          signup(req,res);
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                      break;
              case "/change":
                  try {
                      changePassword(req,res);
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

      public void login(HttpServletRequest req, HttpServletResponse res) throws IOException {

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

              json.put("message", "login successfully");
              json.put("id", result.get(0).get("id"));
              json.put("first_Name", result.get(0).get("first_name"));
              json.put("last_Name", result.get(0).get("last_name"));
              json.put("email_Id", result.get(0).get("email_id"));
              json.put("password", result.get(0).get("password"));
              json.put("mobile_Number", result.get(0).get("mobile_number"));

              json.put("status", "success");
              json.put("status_code", "200");
          }
          else
          {
              json.put("message", "login password not match ");
              json.put("status", "failed");
              json.put("status_code", "400");
          }
          out.println(json);
      }

    public void signup (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String firstName = req.getParameter("firstname");
        String lastName = req.getParameter("lastname");
        String emailId = req.getParameter("email");
        String password = req.getParameter("password");
        String mobileNumber = req.getParameter("mobile_number");

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        JSONObject json = new JSONObject();

        try {
            List<HashMap<String, Object>> result1 = Accounts.signupService(firstName, lastName, emailId, password, mobileNumber);
            if (result1.size() > 0)
            {
                json.put("message", "signup successfully");
                json.put("id",result1.get(0).get("id"));
                json.put("status", "success");
                json.put("status_code", "200");
            }
            else {
                json.put("message", "signup failed");
                json.put("status", "failed");
                json.put("status_code", "400");
            }
        } catch (SQLException e) {
            json.put("message", "already signup");
            json.put("status", "failed");
            json.put("status_code", "403");
        }
        out.println(json);


    }

    public void changePassword (HttpServletRequest req, HttpServletResponse res) throws IOException {

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
            json.put("status", "failed");
            json.put("status_code", "403");

            out.println(json);
        }


        List<HashMap<String, Object>> result2 = null;
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
            json.put("status", "failed");
            json.put("status_code", "400");

            out.println(json);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        JSONObject json = new JSONObject();

        if (result2.size() > 0) {
            json.put("message", "Change Password successfully");
            json.put("status", "success");
            json.put("status_code", "200");
            out.println(json);
        }
        else {
            json.put("message", "Password unsuccessfully");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }
    }
}
