package net.teamhollow.direbats;

import net.fabricmc.api.ClientModInitializer;
import net.teamhollow.direbats.init.DBEntities;

public class DirebatsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DBEntities.registerRenderers();
    }
}
