package net.teamhollow.direbats;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Direbats implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "direbats";
    public static final String MOD_NAME = "Direbats";

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");

        // TODO initializer

        log(Level.INFO, "Initialized");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }
}
