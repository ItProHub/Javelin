package site.itprohub.javelin.startup;

public enum StartupMode {
    /**
     * Start as a regular website
     */
    WEBSITE,

    /**
     * Start the application as a public service
     */
    PUBLIC_SERVICES,

    /**
     * Start the application as an internal private service
     */
    INTERNAL_SERVICES,

    /**
     * Start the application in console mode
     */
    CONSOLE
}
