package me.eastrane.utilities;

import me.eastrane.EastWhitelist;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugProvider {
    private ConfigProvider configProvider;
    private final EastWhitelist plugin;
    private final File debugFile;

    public DebugProvider(EastWhitelist plugin) {
        this.plugin = plugin;
        configProvider = plugin.getConfigProvider();
        debugFile = new File(plugin.getDataFolder(), "debug.yml");
        try {
            debugFile.createNewFile();
        } catch (IOException e) {
            sendException(e);
        }
    }

    /**
     * Logs a message to the debug file with the specified log level.
     *
     * @param logLevel The log level (INFO, WARNING, SEVERE).
     * @param message  The message to log.
     */
    private void logToFile(String logLevel, String message) {
        String realTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = String.format("[%s] [%s] %s%n", logLevel, realTime, message);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(debugFile, true))) {
            writer.write(logMessage);
        } catch (IOException e) {
            sendException(e);
        }
    }

    /**
     * Sends an INFO level message to the console and debug file.
     *
     * @param message The message to send.
     */
    public void sendInfo(String message) {
        if (configProvider.isDebugConsole()) {
            plugin.getLogger().info(message);
        }
        if (configProvider.isDebugFile()) {
            logToFile("INFO", message);
        }
    }

    /**
     * Sends an INFO level message to the console and debug file.
     *
     * @param message             The message to send.
     * @param isRequiredInConsole Whether the message should be sent to the console regardless of disabled status in configuration
     */
    public void sendInfo(String message, boolean isRequiredInConsole) {
        if (configProvider.isDebugConsole() || isRequiredInConsole) {
            plugin.getLogger().info(message);
        }
        if (configProvider.isDebugFile()) {
            logToFile("INFO", message);
        }
    }

    /**
     * Sends a WARNING level message to the console and debug file.
     *
     * @param message The message to send.
     */
    public void sendWarning(String message) {
        plugin.getLogger().warning(message);
        if (configProvider.isDebugFile()) {
            logToFile("WARNING", message);
        }
    }

    /**
     * Sends a SEVERE level message to the console and debug file.
     *
     * @param message The message to send.
     */
    public void sendSevere(String message) {
        plugin.getLogger().severe(message);
        if (configProvider.isDebugFile()) {
            logToFile("SEVERE", message);
        }
    }

    /**
     * Sends an exception to the console and debug file.
     *
     * @param e The exception to send.
     */
    public void sendException(Throwable e) {
        plugin.getLogger().severe(e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            plugin.getLogger().severe("at " + element.toString());
        }
        if (configProvider.isDebugFile()) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logToFile("SEVERE", sw.toString());
        }
        if (configProvider.isMysqlShutdownOnException()) {
            if (e instanceof SQLException) {
                sendSevere("SQL exception occurred. Shutting down server...");
                plugin.getServer().shutdown();
            }
        }
    }
}
