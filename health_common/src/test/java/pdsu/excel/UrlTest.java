package pdsu.excel;

public class UrlTest {

    public static void main(String[] args) {
        String url = "http://192.168.109.131:8080/healthmobile_web/pages/setmeal_detail.html?id=12&a=123&name=zhangsan";

        String value = getValueByKey(url, "name");

        System.out.println(value);
    }

    //id=12&a=123&name=zhangsan

    //id=12
    //a=123
    //name=zhangsan


    public static String getValueByKey(String  url,String key1){
        String[] split = url.split("\\?");
        String s1 = split[1];
        String[] split1 = s1.split("&");

        for (String s : split1) {
            String key = s.split("=")[0];
            String value = s.split("=")[1];
            if(key1.equals(key)){
                return value;
            }
        }
        return null;
    }
}
