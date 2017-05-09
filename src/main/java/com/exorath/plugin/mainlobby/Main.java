/*
 * Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.plugin.mainlobby;

import com.exorath.plugin.base.ExoBaseAPI;
import com.exorath.plugin.mainlobby.scoreboard.ScoreboardManager;
import com.exorath.service.connector.res.BasicServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.UnknownHostException;

/**
 * Created by toonsev on 3/11/2017.
 */
public class Main extends JavaPlugin implements Listener {
    private ExoBaseAPI exoBaseAPI;
    private FileConfiguration configuration;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        exoBaseAPI = ExoBaseAPI.getInstance();
        this.configuration = getConfig();
        try {
            exoBaseAPI.setupGame(new BasicServer(getGameId(), "default", "lobby"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Main.terminate();
        }
        getServer().getPluginManager().registerEvents(this, this);

        this.scoreboardManager = new ScoreboardManager();
        Bukkit.getPluginManager().registerEvents(scoreboardManager, this);
    }

    private String getGameId() {
        if (!configuration.contains("connector.gameId")) {
            System.out.println("MainLobbyPlugin config does not contain connector.gameId, exiting");
            Main.terminate();
        }
        return configuration.getString("connector.gameId");
    }


    public static void terminate() {
        System.out.println("1v1Plugin is terminating...");
        Bukkit.shutdown();
        System.out.println("Termination failed, force exiting system...");
        System.exit(1);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        exoBaseAPI.onGameJoin(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        exoBaseAPI.onGameLeave(event.getPlayer());
    }

}
