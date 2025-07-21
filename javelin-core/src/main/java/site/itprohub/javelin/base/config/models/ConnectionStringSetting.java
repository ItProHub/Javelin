package site.itprohub.javelin.base.config.models;

import jakarta.xml.bind.annotation.*;

public class ConnectionStringSetting implements Cloneable {
    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "connectionString")
    public String connectionString;

    @XmlElement(name = "providerName")
    public String providerName;

    public String ToString() {
        return String.format("Name=%s\nProviderName=%s\nConnectionString=%s", name, providerName, connectionString);
    }

     @Override
    public ConnectionStringSetting clone() {
        try {
            return (ConnectionStringSetting) super.clone();  // 浅拷贝
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // 不可能发生
        }
    }
}
