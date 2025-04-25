package site.itprohub.javelin.startup;

import java.util.Date;

import site.itprohub.javelin.http.Pipeline.NHttpApplication;
import site.itprohub.javelin.http.Pipeline.NHttpModuleFactory;
import site.itprohub.javelin.web.modules.*;

public class JavelinHost {

    static void initNHttpApplication() {
        Date start = new Date();

        loadModules();

        NHttpApplication.start();

        NHttpApplication.INSTANCE.showModules();

        System.out.println("initNHttpApplication OK , Execution time: " + (new Date().getTime() - start.getTime()) + " ms");

    }

    private static void loadModules() {
        NHttpModuleFactory.registerModule(OprLogModule.class);

        NHttpModuleFactory.registerModule(AuthenticateModule.class);
        NHttpModuleFactory.registerModule(AuthorizeModule.class);
    }
    
}
