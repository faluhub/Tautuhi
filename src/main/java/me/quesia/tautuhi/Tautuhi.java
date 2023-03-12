package me.quesia.tautuhi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import org.apache.logging.log4j.*;

public class Tautuhi implements ClientModInitializer {
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("tautuhi").orElseThrow(RuntimeException::new);
    public static final String MOD_NAME = MOD_CONTAINER.getMetadata().getName();
    public static final String MOD_VERSION = String.valueOf(MOD_CONTAINER.getMetadata().getVersion());
    public static Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static boolean GLINT = false;

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    @Override
    public void onInitializeClient() {
        log("Using " + MOD_NAME + " v" + MOD_VERSION);
    }
}
