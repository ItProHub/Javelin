package site.itprohub.javelin.http.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class NHttpModuleFactory {
    private static List<Class<?>> MODULE_LIST = new ArrayList<>();

    private static volatile boolean IS_INITIALIZED = false;

    public static synchronized void registerModule(Class<?> module) {
        if(module == null) {
            throw new IllegalArgumentException("module cannot be null"); 
        }

        if(!NHttpModule.class.isAssignableFrom(module)) {
            throw new IllegalArgumentException("将要注册的插件 "+ module.getName() +" 不符合基类要求。");
        }

        try {
            if(module.getDeclaredConstructor() == null)  {
                throw new IllegalArgumentException("将要注册的插件 "+ module.getName() +" 没有公开无参的构造方法。");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("将要注册的插件 "+ module.getName() +" 没有公开无参的构造方法。");
        }

        if(IS_INITIALIZED) {
            throw new IllegalStateException("RegisterModule方法只允许在程序初始化时调用");
        }

        if (!MODULE_LIST.contains(module)) {
            MODULE_LIST.add(module);
        }
    }


    static List<NHttpModule> createModules() {
       if (IS_INITIALIZED == false) {
           IS_INITIALIZED = true;
       } 

       List<NHttpModule> list = new ArrayList<>();

       for (Class<?> moduleClass : MODULE_LIST) {
        	try {
            	NHttpModule plugin = (NHttpModule) moduleClass.getDeclaredConstructor().newInstance();
				list.add(plugin);
        	} catch (Exception e) {
        		throw new RuntimeException("创建插件实例时发生错误: " + moduleClass.getName(), e); 
        	}
       }

	   list.sort((a, b) -> Integer.compare(a.getOrder(), b.getOrder()));

	   return list;
    }

}
