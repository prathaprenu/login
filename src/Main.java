import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.prathap.web.service.Accounts;
import com.prathap.web.service.Mailbox;
import com.prathap.web.service.Shop;

import static com.prathap.web.service.Accounts.checkEmail;
import static com.prathap.web.service.Mailbox.statusUpdate;
import static com.prathap.web.service.Shop.*;


public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("WELCOME USERS");
        System.out.println("LOGIN TYPE IN 1");
        System.out.println("SIGNUP TYPE IN 2");

        int n = sc.nextInt();
        switch (n) {
            case 1:
                login();
                break;
            case 2:
                signup();
                break;
            default:
                System.out.println("Invalid syntax error");
        }
    }

    public static void login() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the Email id or Mobile Number : ");
        String verify = sc.next();
        System.out.print("Enter the password : ");
        String password = sc.next();

        System.out.println("Email Id or Mobile Number : " + verify);
        System.out.println("Password                  : " + password);

        List<HashMap<String, Object>> result = Accounts.loginService(verify, password);

        if (result.size() > 0) {
            System.out.println("login successfully");
            System.out.println("WELCOME TO GMAIL " + result.get(0).get("first_name"));
            System.out.println("change password TYPE IN 1");
            System.out.println("send mail TYPE IN 2");
            System.out.println("inbox TYPE IN 3");
            System.out.println("sent mail TYPE IN 4");
            System.out.println("profile details TYPE IN 5");
            System.out.println("logout TYPE IN 6");
            System.out.println("shop details TYPE IN 7");


            int update = sc.nextInt();
            switch (update) {
                case 1:
                    checkPassword((int) result.get(0).get("id"));
                    break;
                case 2:
                    sendMail((int) result.get(0).get("id"));
                    break;
                case 3:
                    inbox((int) result.get(0).get("id"));
                    break;
                case 4:
                    sentMail((int) result.get(0).get("id"));
                    break;
                case 5:
                    profileDetails((int) result.get(0).get("id"));
                    break;
                case 6:
                    System.out.println("logout successfully");
                    break;
                case 7:
                    shopDetails((int) result.get(0).get("id"));
                    break;
            }
        } else {
            System.out.println("login password not match");
            signup();
        }
    }

    public static void signup() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the First Name    : ");
        String firstName = sc.next();
        System.out.print("Enter the Last Name     : ");
        String lastName = sc.next();
        System.out.print("Enter the Email Id      : ");
        String emailId = sc.next();
        System.out.print("Enter the Password      : ");
        String password = sc.next();
        System.out.print("Enter the Mobile Number : ");
        String mobileNumber = sc.next();

        System.out.println("FIRST NAME    = " + firstName);
        System.out.println("LAST NAME     = " + lastName);
        System.out.println("EMAIL ID      = " + emailId);
        System.out.println("PASSWORD      = " + password);
        System.out.println("MOBILE NUMBER = " + mobileNumber);

        List<HashMap<String, Object>> result1 = Accounts.signupService(firstName, lastName, emailId, password, mobileNumber);
        if (result1.size() > 0) {
            System.out.println("Signup successfully");
        }
        System.out.println("Enter Login TYPE IN 1 : ");
        System.out.println("Enter Exit TYPE IN 2 : ");
        int users = sc.nextInt();
        switch (users) {
            case 1:
                login();
                break;
            case 2:
                System.out.println("your account is exits");
                System.out.println("Thank You");
                break;
            default:
                System.out.println("syntax error");
        }
        System.out.println("closed database successfully");
    }

    public static void checkPassword(int id) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the Old Password : ");
        String oldPassword = sc.next();

        List<HashMap<String, Object>> result = Accounts.checkPasswordService(oldPassword, id);

        if (result.size() > 0) {
            System.out.println("Old password is match");
            changePassword(id);
        } else {
            System.out.println("Old Password is not match");
        }
    }

    public static void changePassword(int id) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the New Password     : ");
        String newPassword = sc.next();
        System.out.print("Enter the Confirm Password : ");
        String confirmPassword = sc.next();

        System.out.println("NEM PASSWORD     = " + newPassword);
        System.out.println("CONFIRM PASSWORD = " + confirmPassword);

        if (newPassword.equals(confirmPassword)) {
            System.out.println("Password match");

            List<HashMap<String, Object>> result = Accounts.changePasswordService(confirmPassword, id);

            if (result.size() > 0) {
                System.out.println("Change Password successfully");
            } else {
                System.out.println("Password unsuccessfully");
            }
        } else {
            System.out.println("Password not match");
        }
    }

    public static void sendMail(int id) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the Receiver Email Id : ");
        String receiver = sc.next();


        List<HashMap<String, Object>> result = checkEmail(receiver);
        int receiver_id = (int) result.get(0).get("id");
        System.out.println("Receiver Email Id is = " + result.get(0).get("id"));


        System.out.print("Message is : ");
        String message = sc.next();
        System.out.print("Enter the Subject is : ");
        String subject = sc.next();


        System.out.println("Message is      = " + message);
        System.out.println("Subject is      = " + subject);

        List<HashMap<String, Object>> result1 = Mailbox.sendMailService(id, receiver_id, message, subject);

        if (result1.size() > 0) {
            System.out.println("send mail successfully");
        } else {
            System.out.println("mail id is not match");
        }
    }

    public static void inbox(int id) throws SQLException {
        Scanner sc = new Scanner(System.in);

        List<HashMap<String, Object>> result = Mailbox.unreadMail(id);
        System.out.println("You have " + result.size() + " unread mails. To view TYPE IN 1");

        List<HashMap<String, Object>> result1 = Mailbox.receiveMail(id);
        System.out.println("You have " + result1.size() + " receive Mails. To view TYPE IN 2");

        int b = sc.nextInt();
        switch (b) {
            case 1:
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        System.out.println("massages : " + result.get(i).get("m_message"));
                        System.out.println("subject  : " + result.get(i).get("m_subject"));


                        String ap = (String) result.get(i).get("m_sender_time");
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm ");
                        long milliSeconds = Long.parseLong(ap);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(milliSeconds);
                        System.out.println("you send mail date and time : " + formatter.format(calendar.getTime()));
                        System.out.println();
                    }
                    System.out.println("show unread mail");
                    statusUpdate(id);
                } else {
                    System.out.println("You have 0 unread mails.");
                }
                break;

            case 2:
                if (result1.size() > 0) {
                    for (int i = 0; i < result1.size(); i++) {
                        System.out.println("massages : " + result1.get(i).get("m_message"));
                        System.out.println("subject  : " + result1.get(i).get("m_subject"));


                        String ap = (String) result1.get(i).get("m_sender_time");
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm ");
                        long milliSeconds = Long.parseLong(ap);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(milliSeconds);
                        System.out.println("you send mail date and time : " + formatter.format(calendar.getTime()));
                        System.out.println();
                    }
                    System.out.println("show receive mail");
                } else {
                    System.out.println("You have 0 receive mails");
                }
                break;
        }
    }

    public static void sentMail(int id) throws SQLException {

        List<HashMap<String, Object>> result = Mailbox.sentMail(id);
        System.out.println("You have " + result.size() + " sent mails");

        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {

                System.out.println("massages : " + result.get(i).get("m_message"));
                System.out.println("subject  : " + result.get(i).get("m_subject"));

                String ap = (String) result.get(i).get("m_sender_time");
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm ");
                long milliSeconds = Long.parseLong(ap);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(milliSeconds);
                System.out.println("you sent mail date and time : " + formatter.format(calendar.getTime()));
                System.out.println();
            }
            System.out.println("show sent mail");
        } else {
            System.out.println("You have 0 sent mails");
        }

    }

    public static void profileDetails(int id) throws SQLException {

        List<HashMap<String, Object>> result = Accounts.checkId(id);

        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {

                System.out.println("first Name    : " + result.get(i).get("first_name"));
                System.out.println("last Name     : " + result.get(i).get("last_name"));
                System.out.println("email Id      : " + result.get(i).get("email_id"));
                System.out.println("password      : " + result.get(i).get("password"));
                System.out.println("mobile Number : " + result.get(i).get("mobile_number"));
                System.out.println();
            }
            System.out.println("show profile all details");
        } else {
            System.out.println("You have no details");
        }
    }

    public static void shopDetails(int id) throws SQLException {
        Scanner sc = new Scanner(System.in);

        List<HashMap<String, Object>> result = Accounts.checkId(id);
        System.out.println("welcome to shop " + result.get(0).get("first_name"));
        System.out.println("Shop register TYPE IN 1");
        System.out.println("items add TYPE IN 2");
        System.out.println("Find Shop TYPE IN 3");
        System.out.println("logout TYPE IN 4");
        int n = sc.nextInt();
        switch (n) {
            case 1:
                List<HashMap<String, Object>> result1 = category_list();
                for (int i = 0; i < result1.size(); i++) {
                    System.out.println("id            : " + result1.get(i).get("id"));
                    System.out.println("shop category : " + result1.get(i).get("name"));
                    System.out.println();
                }

                System.out.print("Enter the shop Name       : ");
                String shop_name = sc.next();
                System.out.print("Enter the category id     : ");
                int category = sc.nextInt();
                System.out.print("Enter the address         : ");
                String address = sc.next();
                System.out.print("Enter the contact number  : ");
                String contact = sc.next();
                System.out.print("Enter the shop available  : ");
                boolean shop_available = sc.nextBoolean();

                System.out.println("Shop Name      = " + shop_name);
                System.out.println("Category Id    = " + category);
                System.out.println("Address        = " + address);
                System.out.println("Contact        = " + contact);
                System.out.println("Shop Available = " + shop_available);

                List<HashMap<String, Object>> verify_shop = shop_details(id);
                if (verify_shop.size() == 0) {

                    List<HashMap<String, Object>> result2 = shopDetailsService(id, shop_name, category, address, contact, shop_available);
                    if (result2.size() > 0) {
                        System.out.println("shop register successfully");
                    }
                } else {
                    System.out.println("shop already register");
                }
                break;

            case 2:
                List<HashMap<String, Object>> result3 = shop_details(id);
                int shop_id = (int) result3.get(0).get("id");


                List<HashMap<String, Object>> result4 = Shop.item_list();
                if (result4.size() > 0) {
                    for (int i = 0; i < result4.size(); i++) {
                        System.out.println("id : " + result4.get(i).get("id"));
                        System.out.println("name : " + result4.get(i).get("name"));
                        System.out.println("category of item : " + result4.get(i).get("category_of_item"));
                        System.out.println();
                    }
                    System.out.println("category id :" + result3.get(0).get("category_id"));
                    System.out.print("Enter the item id : ");
                    int item_id = sc.nextInt();

                    List<HashMap<String, Object>> same_item = check_same_item(shop_id, item_id);
                    if (same_item.size() > 0) {
                        System.out.println("This item has already added to your store");
                    }
                    else {
                        List<HashMap<String, Object>> category_user = find_item_id(item_id);
                        int item_id1 = (int) category_user.get(0).get("category_of_item");
                        List<HashMap<String, Object>> check_cate = check_shop_item(shop_id,item_id1);

                        if (check_cate.size() > 0) {


                            System.out.print("Enter the cost            : ");
                            int cost = sc.nextInt();
                            System.out.print("Enter the items available : ");
                            boolean items_available = sc.nextBoolean();


                            System.out.println("Shop Id         = " + shop_id);
                            System.out.println("Item Id         = " + item_id);
                            System.out.println("Cost            = " + cost);
                            System.out.println("Items Available = " + items_available);

                            List<HashMap<String, Object>> result5 = itemsTableService(shop_id, item_id, cost, items_available);
                            if (result5.size() > 0) {
                                System.out.println("insert successfully");
                            } else {
                                System.out.println("insert failed");
                            }
                        }
                        else {
                            System.out.println("category not match");
                        }
                    }
                }
                break;
            case 3:
                System.out.print("Enter the item name  : ");
                String item = sc.next();
                System.out.print("Enter the city name  : ");
                String city = sc.next();

                List<HashMap<String, Object>> item_list = Shop.find_item(item);
                if (item_list.size() > 0) {
                    int item_id1 = (int) item_list.get(0).get("id");

                    List<HashMap<String, Object>> city_list = Shop.find_city(city);
                    if (city_list.size() > 0) {
                        for (int i = 0; i < city_list.size(); i++) {
                            int shop_id2 = (int) city_list.get(i).get("id");

                            List<HashMap<String, Object>> shop_list = Shop.find(shop_id2, item_id1);
                            if (shop_list.size() > 0) {
                                int shop_id1 = (int) shop_list.get(0).get("shop_id");

                                List<HashMap<String, Object>> shop = find_shop(shop_id1);
                                if (shop.size() > 0) {

                                    System.out.println("shop name       : " + shop.get(0).get("shop_name"));
                                    System.out.println("address         : " + shop.get(0).get("address"));
                                    System.out.println("contact         : " + shop.get(0).get("contact"));
                                    System.out.println("open time       : " + shop.get(0).get("open_time"));
                                    System.out.println("close time      : " + shop.get(0).get("close_time"));
                                    System.out.println("shop_available  : " + shop.get(0).get("shop_available"));
                                    System.out.println("cost            : " + shop_list.get(0).get("cost"));
                                    System.out.println();
                                }
                            }
                        }
                    } else {
                        System.out.println("city is not match");
                    }
                } else {
                    System.out.println("item is not match");
                }
                break;
            case 4:
                System.out.println("logout successfully");
                break;
        }
    }

}



