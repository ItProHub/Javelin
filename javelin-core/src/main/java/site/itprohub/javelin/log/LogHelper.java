package site.itprohub.javelin.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

public class LogHelper {

    private static final String LOG_DIR = "logs"; // 日志存储的根目录
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Gson gson = new Gson();

    public static void Write(OprLog log) {
        String logFileName = getLogFileName(); // 获取日志文件名
        File logFile = new File(LOG_DIR, logFileName); // 构建日志文件路径

        // 确保日志目录存在
        if (logFile.getParentFile().exists() == false) {
            logFile.getParentFile().mkdirs();
        }

         // 将日志内容写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(gson.toJson(log)); // 写入日志内容
            writer.newLine(); // 换行
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getLogFileName() {
        return DATE_FORMAT.format(new Date()) + ".log";
    }

}
