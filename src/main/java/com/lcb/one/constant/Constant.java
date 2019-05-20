package com.lcb.one.constant;

import com.lcb.one.util.TimeUtil;

/**
 * Description: 常量类
 * AUTHOR: Champion Dragon
 * created at 2017/8/23
 **/

public class Constant {
    /* 时间格式 */
    public final static String cformatDay = "yyyy年MM月dd日";
    public final static String cformatD = "M月d日";
    public final static String cformatsecond = "yyyy年MM月dd日HH时mm分ss秒";
    public final static String formatminute = "HH:mm";
    public final static String formatsecond = "yyyy-MM-dd HH:mm:ss";

    /* 文件夹 */
    public final static String fileRoot = "百度地图功能调试";//此APP的根文件夹


    /*后台交互的网址*/
    public final static String TcbUrl = "http://s.tingchebao.com/zld/";//停车宝官网服务器
    public final static String TcyUrl = "http://120.203.0.218:51010/zld/";//公司自己的服务器
    public final static String Tcb_local = "http://192.168.199.240/zld/";
    public final static String Tcb_zld_local = "http://192.168.199.251/zldlocal/";
    public final static String Tcb_zld_line = "http://192.168.199.251/zldline/";
    public final static String Tcb_beta = "http://180.150.188.224:8080/zld/";
    public final static String TcbMoible = "18296127347";


    /*支付*/
    public final static String aliSign = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMv8f1EqwRN4Zx5WO3lVze6WKWdQQWmOWAnynGHIlsR184UEll1V4859p3CCc9b7rwZaOruj2KnPTUJ/VqQ+xPf7Q8o61PFllfzJgQ1KjNw+GJTJ1ZUTDEf443Xeoa6ieSbZjWcR6uNtQ5ffDFjx62gj3UITIjxTCpRcPumUgBwHAgMBAAECgYBtqshAOP4om5jE5JOA/jKCzNRhqPIh79dBMeAFajQ0Vz2fDAJTF7Qr9b4pbNkegZ1tiuD8tG/ti3f8Aj3we5akzgd5EJJGHLprKMitrlgo/3vkKxaJOgQJUv9nmOCPDNoHz9lFK3js0tB5LoC3XN4i87PKAYrkfzW6sErOUkC90QJBAOWz9eCKdPCArHGbyXOhl9Tu8NRwslsSGAPEme+LGBdUPubQEoJNVAkWxSErNbiPiSF0VICP2cJlgoMrAYUyS5sCQQDjVtgHYSZ0AQsXp0GM+ENaQvEzUmY9D7aRrT+HvVnkx9eOwWu3+6nXI2Rw77L1FFHaNekLKVoFEaJ6oJDvrQYFAkEAsKmr3TofnikYd3f9g/UwNRBgIMNcKTbNSXiXe+haavbcOeClm5mlnCfrDQuSkZOzQAucQhRgwmYX7pHQ5YQ9KQJBAIye+T2HUFvNEWluIdPq9O5uHfha7bazc4Cko3l5HJOxMZqx9cl2N9ZFpClfe1ixWvgZBK/MwkwEXnZvv3chlWkCQH2i1OJi0YBqOWvGti0vkZjNSlb3hxyPdNQSr325vo8t4IOfhN2F2wvJt3lc/Qa1FgCezg1j4d9YW7xYiJHuJkE=";

    /* +++++++++++++++++++++++ 第三方KEY ++++++++++++++++++++++++++++++*/
    /**
     * app_id是从微信官网申请到的合法APPid
     */
    public static final String APP_ID_WX = "wx1a03d6d2abbc5b6a";

    /**
     * 微信AppSecret值
     */
    public static final String APP_SECRET_WX = "831ec926826acb588984ef1071ab902e";


    /*测试图片的网址*/
    public static final String img1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557139560606&di=3ad2d429ab7b32fc8a1a20fa979c9da2&imgtype=0&src=http%3A%2F%2Fpic24.nipic.com%2F20120920%2F4499633_231520508000_2.jpg";
    public static final String img2 = "http://i03.pictn.sogoucdn.com/3c28af542f2d49f7-fe9c78d2ff4ac332-73d7732e20e2fcfaa954979d623bcbe9_qq";
    public static final String img3 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1516322206,3963323316&fm=26&gp=0.jpg";
    public static final String img4 = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=211572776,1295513701&fm=11&gp=0.jpg";
    /*测试的时间戳*/
    public static final String time = TimeUtil.long2time(System.currentTimeMillis(), cformatsecond);
    public static final String time1 = TimeUtil.long2time(System.currentTimeMillis() - 3333, cformatsecond);
    public static final String time2 = TimeUtil.long2time(System.currentTimeMillis() - 4444, cformatsecond);
    public static final String time3 = TimeUtil.long2time(System.currentTimeMillis() - 5555, cformatsecond);
    public static final String time4 = TimeUtil.long2time(System.currentTimeMillis() - 6666, cformatsecond);
    public static final String time5 = TimeUtil.long2time(System.currentTimeMillis() - 7777, cformatsecond);


}
