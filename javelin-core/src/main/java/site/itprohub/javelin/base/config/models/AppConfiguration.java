package site.itprohub.javelin.base.config.models;

import jakarta.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import site.itprohub.javelin.base.xml.*;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public final class AppConfiguration {

    @XmlElementWrapper(name = "appSettings")
    @XmlElement(name = "add")
    public List<AppSetting> appSettings = new ArrayList<>();

    @XmlElementWrapper(name = "connectionStrings")
    @XmlElement(name = "add")
    public List<ConnectionStringSetting> connectionStrings = new ArrayList<>();

    @XmlElementWrapper(name = "dbConfigs")
    @XmlElement(name = "add")
    public List<XmlDbConfig> dbConfigs = new ArrayList<>();


    public void correctData() {
        if (appSettings == null) appSettings = new ArrayList<>();
        if (connectionStrings == null) connectionStrings = new ArrayList<>();
        if (dbConfigs == null) dbConfigs = new ArrayList<>();

        for (ConnectionStringSetting x : connectionStrings) {
            if (x.providerName == null || x.providerName.isEmpty()) {
                x.providerName ="SqlClient";  // 替换成你实际使用的 provider 名称
            }
        }
    }

    public static AppConfiguration loadFromFile(String filePath, boolean checkExist) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("filePath is required");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            if (checkExist)
                throw new IllegalArgumentException("配置文件没有找到，filePath: " + filePath);
            else
                return null;
        }

        return XmlHelper.deserializeFromFile(AppConfiguration.class, filePath);
    }

    public static AppConfiguration loadFromXml(String xml) throws Exception {
        if (xml == null || xml.trim().isEmpty()) {
            throw new IllegalArgumentException("xml is required");
        }

        return XmlHelper.deserializeFromXml(AppConfiguration.class, xml);
    }

    public static AppConfiguration loadFromSysConfiguration() {
        // Java 不存在 System.Configuration，替代方式可以从 System.getenv() 或 java.util.Properties 获取
        return null;
    }
}
