package me.aq.plugin;

import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ClientConnectEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Join implements Listener {

    private NTIRBungeeChat plugin;

    @EventHandler
    public void onJoin(PostLoginEvent e) throws ParseException {
        plugin = NTIRBungeeChat.getPlugin();
        ProxiedPlayer p = e.getPlayer();


        if(plugin.data.tempBanned(p)) {
            long current = new Date().getTime();
            long unban = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss.SSS").parse(plugin.data.getUnBanDate(p)).getTime();
            if (current >= unban) {
                plugin.data.unBan(p);
            }
        }

        if (plugin.data.tempBanned(p)) {
            p.disconnect(plugin.tempBanMessage.banMessage(plugin.data.getReason(p), plugin.data.getUnBanDate(p)));
            return;
        }
        plugin.jda.getPresence().setActivity(Activity.playing("上線人數:" + plugin.getProxy().getOnlineCount() + "人"));
        plugin.jda.getTextChannelById("893472407485038622").sendMessage(":arrow_forward:" + "`" + p.getDisplayName() + "`上線了").queue();
    }


}
