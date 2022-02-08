package me.aq.plugin;

import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Leave implements Listener {

    private NTIRBungeeChat plugin;

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e){
        plugin = NTIRBungeeChat.getPlugin();
        ProxiedPlayer p = e.getPlayer();
        if(plugin.data.tempBanned(p)){
            return;
        }

        plugin.jda.getPresence().setActivity(Activity.playing("上線人數:" + plugin.getProxy().getOnlineCount() + "人"));
        plugin.jda.getTextChannelById("893472407485038622").sendMessage( ":small_red_triangle_down:"+ "`" + p.getDisplayName() + "`下線了").queue();
    }
}
