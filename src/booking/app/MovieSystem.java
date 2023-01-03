package booking.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import booking.bean.Business;
import booking.bean.Customer;
import booking.bean.Movie;
import booking.bean.User;

public class MovieSystem {

    /*
     * 定义系统的数据容器存储数据
     * 1.存储多用户（客人对象，片商对象）
     */
    // 用户final修饰把其变为常量(constant),地址不变，不能把这个容器变为其他容器（比如null），但是可以添加数据，
    public static final List<User> ALL_USERS = new ArrayList<>();

    /*
     * 2.存储商家信息和其排片信息
     */
    public static final Map<Business, List<Movie>> ALL_MOVIES = new HashMap<>();

    /*
     * Scanner
     */
    public static final Scanner SYS_SC = new Scanner(System.in);

    /*
     * 定义一个静态的User类型的变量记住当前登录成功的用户
     */
    public static User loginUser;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger("MovieSystem.class");
    /*
     * 3.准备一些测试数据
     */
    static {
        Customer c = new Customer();
        c.setLoginName("c1");
        c.setPassWord("123456");
        c.setUserName("C-1");
        c.setGender('M');
        c.setMoney(10000);
        c.setPhone("110110");
        ALL_USERS.add(c);

        Customer c1 = new Customer();
        c1.setLoginName("c2");
        c1.setPassWord("123456");
        c1.setUserName("C-2");
        c1.setGender('F');
        c1.setMoney(2000);
        c1.setPhone("111111");
        ALL_USERS.add(c1);

        Business b = new Business();
        b.setLoginName("b1");
        b.setPassWord("123456");
        b.setUserName("B-3");
        b.setMoney(0);
        b.setGender('M');
        b.setPhone("110110");
        b.setAddress("BLUD-1");
        b.setShopName("ROOM-@@@");
        ALL_USERS.add(b);
        // 注意，商家一定需要加入到店铺排片信息中去
        List<Movie> movies = new ArrayList<>();
        ALL_MOVIES.put(b, movies); // b = []

        Business b2 = new Business();
        b2.setLoginName("b2");
        b2.setPassWord("123456");
        b2.setUserName("B-2");
        b2.setMoney(0);
        b2.setGender('F');
        b2.setPhone("110110");
        b2.setAddress("BLUD-2");
        b2.setShopName("ROOM-$$$");
        ALL_USERS.add(b2);
        // 注意，商家一定需要加入到店铺排片信息中去
        List<Movie> movies3 = new ArrayList<>();
        ALL_MOVIES.put(b2, movies3); // b2 = []
    }

    // -------------------------------------------------------------------------------
    public static void main(String[] args) {
        showMain();
    }

    // -------------------------------------------------------------------------------
    /*
     * mian page
     */
    private static void showMain() {
        while (true) {
            System.out.println("==============Home Page==============");
            System.out.println("1.Login");
            System.out.println("2.User Register");
            System.out.println("3.Merchant Register");
            System.out.println("Please enter number for command");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    // login
                    login();
                    break;
                case "2":
                    // User Register
                    break;
                case "3":
                    // Merchant Register
                    break;
                default:
                    System.out.println("Wrong command number!");
            }
        }
    }

    /*
     * login function
     */
    private static void login() {
        while (true) {
            System.out.println("Please enter your login name");
            String loginName = SYS_SC.nextLine();
            System.out.println("Please enter your Password");
            String passWord = SYS_SC.nextLine();

            // 1.根据登录名称查询用户对象（做成方法）
            User u = getUserByLoginName(loginName);
            // 2.判断用户名是否存在，存在说明登录名称正确
            if (u != null) {
                // 3.比对密码是否正确
                if (u.getPassWord().equals(passWord)) {
                    // 登录成功
                    loginUser = u;// 记住登录成功的用户
                    // 判断是用户还是商家在登录
                    if (u instanceof Customer) {
                        // 普通用户登录
                        showCustomerMAin();
                    } else {
                        // 商家用户登录
                        showBusinessMain();
                    }
                } else {
                    System.out.println("Password is werong!");
                }
            } else {
                System.out.println("Login name is wrong!!");
            }
        }
    }

    /* 商家操作界面 */
    private static void showBusinessMain() {
        while (true) {
            System.out.println("============Merchant===================");
            System.out
                    .println(loginUser.getUserName() + (loginUser.getGender() == 'M' ? " Mr." : " Mrs" + " Welcome! "));
            System.out.println("1.Show details:");
            System.out.println("2.On line:");
            System.out.println("3.Off line:");
            System.out.println("4.Edit movie:");
            System.out.println("5.Log-out:");

            System.out.println("Please enter number for command: ");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    // 展示全部排片信息
                    showBusinessInfos();
                    break;
                case "2":
                    // 上架电影信息
                    addMovie();
                    break;
                case "3":
                    // 下架电影信息
                    // deleteMovie();
                    break;
                case "4":
                    // 修改电影信息
                    // updateMovie();
                    break;
                case "5":
                    System.out.println(loginUser.getUserName() + "Bye-Bye!!");
                    return; // 干掉方法
                default:
                    System.out.println("Erro command!");
                    break;
            }
        }
    }

    /*
     * 商家进行影片上架
     */
    private static void addMovie() {
        System.out.println("================Online Movie====================");
        // 根据商家对象(就是登录的用户loginUser)，作为Map集合的键 提取对应的值就是其排片信息 ：Map<Business , List<Movie>> ALL_MOVIES
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        System.out.println("Please enter a new title: ");
        String name  = SYS_SC.nextLine();
        System.out.println("Please enter the actor:");
        String actor  = SYS_SC.nextLine();
        System.out.println("Please enter the duration:");
        String time  = SYS_SC.nextLine();
        System.out.println("Please enter your fare:");
        String price  = SYS_SC.nextLine();
        System.out.println("Please enter the number of tickets:");
        String totalNumber  = SYS_SC.nextLine(); // 200\n
        while (true) {
            try {
                System.out.println("Please enter the showtime:");
                String stime  = SYS_SC.nextLine();
            // public Movie(String name, String actor, double time, double price, int number, Date startTime)        // 封装成电影对象 ，加入集合movices中去
                Movie movie = new Movie(name, actor ,Double.valueOf(time) , Double.valueOf(price)
                        , Integer.valueOf(totalNumber), sdf.parse(stime));
                movies.add(movie);
                System.out.println("You have successfully listed:<" + movie.getName() + ">");
                return; // 直接退出去
            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.error("time erro!");
            }
        }
    }

    // 展示当前商家信息
    private static void showBusinessInfos() {
        System.out.println("================Merchant Detial=================");
        LOGGER.info(loginUser.getUserName() +"Merchant,checking thire account....");
        // 根据商家对象(loginUser)，作为Map集合的键 提取对应的值就是其排片信息： Map<Business, List<Movie>>
        // ALL_MOVIES
        /*
         * 向下转型是指将一个父类或接口的引用转换为子类的引用。在这段代码中，将一个loginUser引用转换为Business类型的引用。
         * 
         * 在Java中，向下转型前需要进行强制类型转换。这意味着需要在类型之前加上小括号，并在括号内填写要转换的类型。
         * 
         * 在这段代码中，loginUser是一个Object类型的引用，它被强制转换为Business类型的引用，并赋值给business变量。
         * 
         * 在向下转型之前，应确保loginUser的实际类型是Business或其子类，否则会导致运行时错误。
         * 
         * 例如，如果loginUser的实际类型是Person（假设Person是Business的父类），则会导致ClassCastException异常。
         */
        Business business = (Business) loginUser;
        System.out.println(
                business.getShopName() + "\t\tPhone: " + business.getPhone() + "\t\tAddress: " + business.getAddress());
        List<Movie> movies = ALL_MOVIES.get(business);
        if (movies.size() > 0) {
            System.out.println("Title\t\t\tStarring\t\tDuration\t\tScore\t\tFare\t\tNumber of Remaining Tickets\t\tShowtime");
            for (Movie movie : movies) {

                System.out.println(movie.getName() + "\t\t" + movie.getActor() + "\t\t" + movie.getTime()
                        + "\t\t" + movie.getScore() + "\t\t" + movie.getPrice() + "\t\t" + movie.getNumber() + "\t\t"
                        + sdf.format(movie.getStartTime()));
            }
        } else {
            System.out.println("There are currently no films showing in your store~~~~");
        }

    }

    /* 用户操作界面 */
    private static void showCustomerMAin() {
        while (true) {
            System.out.println("============Customer===================");
            System.out.println(loginUser.getUserName() + (loginUser.getGender() == 'M' ? " Mr."
                    : " Mrs" + " Welcome! " +
                            "\tBalance: $" + loginUser.getMoney()));
            System.out.println("Please select the function you want to operate:");
            System.out.println("1.Display all video information:");
            System.out.println("2.Query movie information by movie name:");
            System.out.println("3.Rate:");
            System.out.println("4.Buy tickets:");
            System.out.println("5.Logout:");
            System.out.println("Please enter number for command:");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    // 展示全部排片信息
                    // showAllMovies();
                    break;
                case "2":
                    break;
                case "3":
                    // 评分功能
                    // scoreMovie();
                    // showAllMovies();
                    break;
                case "4":
                    // 购票功能
                    // buyMovie();
                    break;
                case "5":
                    return; // 干掉方法
                default:
                    System.out.println("Erro command!");
                    break;
            }
        }
    }

    public static User getUserByLoginName(String loginName) {
        for (User user : ALL_USERS) {
            // 判断用户名称是不是我们想要的
            if (user.getLoginName().equals(loginName)) {
                return user;
            }
        }
        return null;// 查无此用户
    }
}
