package site.itprohub.javelin.data.config;

public class EnvConfig {

    public static String getDbUrl() {
        return  System.getenv().getOrDefault("CONFIG_DB_URL", "jdbc:mysql://localhost:3306/config?user=root&password=lj8023wh") ; 
    }
    

}
