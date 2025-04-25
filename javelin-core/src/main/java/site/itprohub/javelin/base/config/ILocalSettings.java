package site.itprohub.javelin.base.config;

public interface ILocalSettings {
    public String getSetting(String name);

    public String getSetting(String name, boolean checkExist);
    
}
