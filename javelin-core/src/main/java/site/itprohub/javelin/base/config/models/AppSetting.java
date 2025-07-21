package site.itprohub.javelin.base.config.models;

import jakarta.xml.bind.annotation.*;

public class AppSetting {
    @XmlElement(name = "key")
    public String key;

    @XmlElement(name = "value")
    public String value;
}
