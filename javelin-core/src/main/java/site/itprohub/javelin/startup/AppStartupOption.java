package site.itprohub.javelin.startup;

import site.itprohub.javelin.base.config.LocalSettings;
import site.itprohub.javelin.common.Const;

public class AppStartupOption {

    public boolean enableConfigService = false; // 是否启用配置服务，默认不启用

    public static AppStartupOption create(AppStartupOption option) throws Exception { // ✅ 初始化AppStartupOption参数
        AppStartupOption appStartupOption = option == null ? new AppStartupOption() : (AppStartupOption) option.clone();;
        
        // 如果没有制定配置服务地址，就认为不启用配置服务
        if( appStartupOption.enableConfigService == true && LocalSettings.getSetting(Const.Names.CONFIG_SERVICE_URL).isEmpty()) {
            System.out.println("Not found ConfigServiceUrl, set EnableConfigService = fasle");
            appStartupOption.enableConfigService = false;
        }

        return appStartupOption;
    }
}
