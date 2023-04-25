package me.quesia.tautuhi.handler;

import me.quesia.tautuhi.Tautuhi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

public class PlayerHandler {
    private static final Map<UUID, PlayerHandler> INSTANCES = new HashMap<>();
    private PlayerEntity player;
    private final UUID uuid;
    private Identifier texture;

    public PlayerHandler(PlayerEntity player) {
        this.player = player;
        this.uuid = this.player.getUuid();
        INSTANCES.put(this.uuid, this);
    }

    public static PlayerHandler fromPlayer(PlayerEntity player) {
        PlayerHandler handler = INSTANCES.get(player.getUuid());
        return handler != null ? handler : new PlayerHandler(player);
    }

    public static void onPlayerJoin(PlayerEntity player) {
        ForkJoinPool.commonPool().submit(() -> fromPlayer(player).setCape());
    }

    public void setCape() {
        try {
            File capeFile = FabricLoader.getInstance().getConfigDir().resolve("cape.png").toFile();
            if (capeFile.exists()) {
                FileInputStream stream = new FileInputStream(capeFile);
                NativeImage image = NativeImage.read(stream);
                MinecraftClient client = MinecraftClient.getInstance();
                client.submit(() -> {
                    this.texture = client.getTextureManager().registerDynamicTexture(
                            this.uuid.toString().replace("-", ""),
                            new NativeImageBackedTexture(this.parseCape(image))
                    );
                });
            }
        } catch (IOException ignored) {}
    }

    private NativeImage parseCape(NativeImage image) {
        int imgWidth = 64;
        int imgHeight = 32;
        int srcWidth = image.getWidth();
        int srcHeight = image.getHeight();
        while (imgWidth < srcWidth || imgHeight < srcHeight) {
            imgWidth *= 2;
            imgHeight *= 2;
        }
        NativeImage cape = new NativeImage(imgWidth, imgHeight, true);
        for (int x = 0; x < srcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                cape.setPixelColor(x, y, image.getPixelColor(x, y));
            }
        }
        image.close();
        return cape;
    }

    public Identifier getTexture(AbstractClientPlayerEntity acpe) {
        Identifier tempTexture;
        if (acpe == MinecraftClient.getInstance().player) {
            tempTexture = this.getTexture();
            if (tempTexture == null) {
                tempTexture = acpe.getCapeTexture();
            }
        } else {
            tempTexture = acpe.getCapeTexture();
            if (tempTexture == null) {
                tempTexture = this.getTexture();
            }
        }
        return tempTexture;
    }

    public Identifier getTexture() {
        return this.texture;
    }
}
