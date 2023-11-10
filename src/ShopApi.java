import com.prathap.web.service.Accounts;
import com.prathap.web.service.Shop;
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

import static com.prathap.web.service.Shop.*;


@WebServlet(urlPatterns = {"/shop/*"})

public class ShopApi extends HttpServlet {
    public void service (HttpServletRequest req, HttpServletResponse res) throws IOException {

        String path = req.getPathInfo();

        switch (path) {
            case "/category_list":
                try {
                    categoryList(req, res);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/shop_register":
                try {
                    shopRegister(req, res);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/item_list":
                try {
                    itemList(req, res);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/items_table":
                try {
                    itemsTable(req, res);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/search_shop":
                try {
                    searchShop(req, res);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/add_item":
                try {
                    addItem(req, res);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/add_category":
                try {
                    addCategoryList(req, res);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "/shop_details":
                try {
                    shopDetails(req, res);
                } catch (IOException | SQLException e) {
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


    public void categoryList(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<HashMap<String, Object>> result = category_list();
        JSONArray jsonArray =new JSONArray();

        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                JSONObject json = new JSONObject();

                json.put("id", result.get(i).get("id"));
                json.put("shop_category", result.get(i).get("name"));

                jsonArray.put(json);
            }
            JSONObject json = new JSONObject();
            json.put("status", "success");
            json.put("status_code", "200");
            json.put("shop_category_list",jsonArray);
            out.println(json);
        }
    }


    public void shopRegister(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {

        String emailId = req.getParameter("email");

        String shop_name = req.getParameter("shop_name");
        int category = Integer.parseInt(req.getParameter("category_id"));
        String address = req.getParameter("address");
        String contact = req.getParameter("contact");
        boolean shop_available = Boolean.parseBoolean(req.getParameter("shop_available"));


        List<HashMap<String, Object>> result;
        try {
            result = Accounts.checkEmail(emailId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        try{
            int id = (int) result.get(0).get("id");

            List<HashMap<String, Object>> verify_shop = shop_details(id);
            JSONObject json = new JSONObject();
            if (verify_shop.size() == 0) {

                List<HashMap<String, Object>> result2 = shopDetailsService(id, shop_name, category, address, contact, shop_available);

                if (result2.size() > 0) {

                    List<HashMap<String, Object>> result1 = shop_details(id);


                    json.put("users1_id", result.get(0).get("email_id"));
                    json.put("shop_name", result1.get(0).get("shop_name"));
                    json.put("category_id", result1.get(0).get("category_id"));
                    json.put("address", result1.get(0).get("address"));
                    json.put("contact", result1.get(0).get("contact"));

                    String ap = (String) result1.get(0).get("open_time");
                    DateFormat formatter = new SimpleDateFormat("HH:mm a");
                    long milliSeconds = Long.parseLong(ap);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(milliSeconds);
                    json.put("open_time",formatter.format(calendar.getTime()));

                    String aps = (String) result1.get(0).get("close_time");
                    DateFormat formatter1 = new SimpleDateFormat("HH:mm a");
                    long milliSeconds1 = Long.parseLong(aps);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(milliSeconds1);
                    json.put("close_time",formatter1.format(calendar1.getTime()));

                    json.put("shop_available", result1.get(0).get("shop_available"));

                    json.put("message", "shop register successfully");
                    json.put("status", "success");
                    json.put("status_code", "200");
                } else {
                    json.put("message", "shop register failed");
                    json.put("status", "failed");
                    json.put("status_code", "400");
                }
            }
            else {
                json.put("message", "shop already register");
                json.put("status", "failed");
                json.put("status_code", "403");
            }
            out.println(json);
        }
        catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("message", "id is not match");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }

    }


   public void itemList(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {

       res.setContentType("application/json");
       PrintWriter out = res.getWriter();

       List<HashMap<String, Object>> result = Shop.item_list();
       JSONArray jsonArray =new JSONArray();

       if (result.size() > 0) {
           for (int i = 0; i < result.size(); i++) {
               JSONObject json = new JSONObject();

               json.put("id", result.get(i).get("id"));
               json.put("name", result.get(i).get("name"));
               json.put("category_of_item", result.get(i).get("category_of_item"));

               jsonArray.put(json);
           }
           JSONObject json = new JSONObject();
           json.put("status", "success");
           json.put("status_code", "200");
           json.put("shop_item_list",jsonArray);
           out.println(json);
       }
   }


    public void itemsTable(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {


        int shop_id = Integer.parseInt(req.getParameter("shop_id"));
        int item_id = Integer.parseInt(req.getParameter("item_id"));
        int cost = Integer.parseInt(req.getParameter("cost"));
        boolean items_available = Boolean.parseBoolean(req.getParameter("items_available"));


        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<HashMap<String, Object>> same_item = check_same_item(shop_id, item_id);
        JSONObject json = new JSONObject();
        if (same_item.size() > 0) {
            json.put("message", "This item has already added to your store");
            json.put("status", "failed");
            json.put("status_code", "403");
        }
        else {
            List<HashMap<String, Object>> category_user = find_item_id(item_id);
            int item_id1 = (int) category_user.get(0).get("category_of_item");

            List<HashMap<String, Object>> check_cate = check_shop_item(shop_id,item_id1);

            if (check_cate.size() > 0) {
                List<HashMap<String, Object>> result1 = itemsTableService(shop_id, item_id, cost, items_available);
                if (result1.size() > 0) {
                    json.put("message", "insert item successfully");
                    json.put("status", "success");
                    json.put("status_code", "200");
                } else {
                    json.put("message", "insert failed");
                    json.put("status", "failed");
                    json.put("status_code", "400");
                }
            }
            else {
                json.put("message", "category not match");
                json.put("status", "failed");
                json.put("status_code", "400");
            }
        }
        out.println(json);
    }


    public void searchShop (HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {

        String item = req.getParameter("item");
        String city = req.getParameter("city");

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<HashMap<String, Object>> result6 = Shop.find_item(item);
        JSONArray jsonArray = new JSONArray();

        if (result6.size() > 0) {
            int item_id = (int) result6.get(0).get("id");

            List<HashMap<String, Object>> result7 = Shop.find_city(city);
            if (result7.size() > 0) {
                for (int i = 0; i < result7.size(); i++) {
                    int shop_id = (int) result7.get(i).get("id");

                    List<HashMap<String, Object>> result8 = Shop.find(shop_id, item_id);

                    if (result8.size() > 0) {
                        int shop_id1 = (int) result8.get(0).get("shop_id");

                        List<HashMap<String, Object>> shop = find_shop(shop_id1);
                        if (shop.size() > 0) {
                            for (int j = 0; j < shop.size(); j++) {

                                JSONObject json = new JSONObject();

                                json.put("shop_name", shop.get(j).get("shop_name"));
                                json.put("address", shop.get(j).get("address"));
                                json.put("contact", shop.get(j).get("contact"));

                                String ap = (String) shop.get(i).get("open_time");
                                DateFormat formatter = new SimpleDateFormat("HH:mm a");
                                long milliSeconds = Long.parseLong(ap);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(milliSeconds);
                                json.put("open_time",formatter.format(calendar.getTime()));

                                String aps = (String) shop.get(i).get("close_time");
                                DateFormat formatter1 = new SimpleDateFormat("HH:mm a");
                                long milliSeconds1 = Long.parseLong(aps);
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.setTimeInMillis(milliSeconds1);
                                json.put("close_time",formatter1.format(calendar1.getTime()));

                                json.put("shop_available", shop.get(j).get("shop_available"));
                                json.put("cost", result8.get(j).get("cost"));

                                jsonArray.put(json);
                            }
                        }
                    }
                }
                if (jsonArray.length() <= 0) {
                    JSONObject json = new JSONObject();
                    json.put("massage", "this item and this city not here");
                    json.put("status", "failed");
                    json.put("status_code", "400");
                    jsonArray.put(json);
                }
                JSONObject json = new JSONObject();
                json.put("status", "success");
                json.put("status_code", "200");
                json.put("shop",jsonArray);
                out.println(json);
            } else {
                JSONObject json = new JSONObject();
                json.put("message", "city is not match");
                json.put("status", "failed");
                json.put("status_code", "400");
                out.println(json);
            }
        } else {
            JSONObject json = new JSONObject();
            json.put("message", "item is not match");
            json.put("status", "failed");
            json.put("status_code", "400");
            out.println(json);
        }
    }


    public void addItem (HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {

        String name = req.getParameter("item_name");
        int category_of_item = Integer.parseInt(req.getParameter("item_category"));

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<HashMap<String, Object>> check_item = find_item(name);

        JSONObject json = new JSONObject();
        if (check_item.size() == 0) {
            List<HashMap<String, Object>> result = Shop.add_item(name, category_of_item);
            if (result.size() > 0) {
                json.put("item_id", result.get(0).get("id"));
                json.put("status", "success");
                json.put("status_code", "200");
            }
            else {
                json.put("massage", "not insert your item");
                json.put("status", "failed");
                json.put("status_code", "400");
            }
        }
        else {
            json.put("massage", "this item already inserted");
            json.put("status", "failed");
            json.put("status_code", "403");
        }
        out.println(json);
    }


    public void addCategoryList (HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String name = req.getParameter("category_name");

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<HashMap<String, Object>> check_category = find_category(name);
        JSONObject json = new JSONObject();
        if (check_category.size() == 0) {
            List<HashMap<String, Object>> result = Shop.add_category(name);
            if (result.size() > 0) {
                json.put("category_id", result.get(0).get("id"));
                json.put("status", "success");
                json.put("status_code", "200");
            }
            else {
                json.put("message", "not insert your category");
                json.put("status", "failed");
                json.put("status_code", "400");
            }
        }
        else {
            json.put("message","this category already inserted ");
            json.put("status", "failed");
            json.put("status_code", "403");
        }
        out.println(json);
    }


    public void shopDetails (HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<HashMap<String, Object>> shop = shop_details();
        JSONArray jsonArray = new JSONArray();
        if (shop.size() > 0) {
            for (int i = 0; i < shop.size(); i++) {
                JSONObject json = new JSONObject();

                json.put("id", shop.get(i).get("id"));
                json.put("users1_id", shop.get(i).get("users1_id"));
                json.put("shop_name", shop.get(i).get("shop_name"));
                json.put("address", shop.get(i).get("address"));
                json.put("contact", shop.get(i).get("contact"));

                String ap = (String) shop.get(i).get("open_time");
                DateFormat formatter = new SimpleDateFormat("HH:mm a");
                long milliSeconds = Long.parseLong(ap);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(milliSeconds);
                json.put("open_time",formatter.format(calendar.getTime()));

                String aps = (String) shop.get(i).get("close_time");
                DateFormat formatter1 = new SimpleDateFormat("HH:mm a");
                long milliSeconds1 = Long.parseLong(aps);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(milliSeconds1);
                json.put("close_time",formatter1.format(calendar1.getTime()));

                json.put("shop_available", shop.get(i).get("shop_available"));

                jsonArray.put(json);
            }
            JSONObject json = new JSONObject();
            json.put("status", "success");
            json.put("status_code", "200");
            json.put("shop_details",jsonArray);
            out.println(json);
        }
    }

}
