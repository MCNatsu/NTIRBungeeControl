package me.aq.plugin.Event;

import me.aq.plugin.NTIRBungeeChat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ClientConnectEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Join implements Listener {

    private NTIRBungeeChat plugin;

    @EventHandler
    public void onJoin(PostLoginEvent e) throws ParseException {
        plugin = NTIRBungeeChat.getPlugin();
        ProxiedPlayer p = e.getPlayer();

        if(plugin.data.Banned(p)) {
            p.disconnect(new TextComponent(plugin.BanMessage.banMessage(plugin.data.getReason(p))));
            return;
        }

        if(plugin.data.tempBanned(p)) {
            long current = new Date().getTime();
            long unban = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss.SSS").parse(plugin.data.getUnBanDate(p)).getTime();
            if (current >= unban) {
                plugin.data.unBan(p);
            }
            p.disconnect(plugin.tempBanMessage.banMessage(plugin.data.getReason(p), plugin.data.getUnBanDate(p)));
            return;
        }

        for(ProxiedPlayer player : plugin.getProxy().getPlayers()){

            player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent( ChatColor.LIGHT_PURPLE + p.getDisplayName() + ChatColor.GREEN + "上線了"));
            if(!plugin.data.chatSettingExist(p)){
                plugin.data.createPlayer(p);
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("歡迎新玩家" + p.getDisplayName() + "首次加入NTIR伺服器");
                eb.setColor(Color.PINK);
                eb.setAuthor("","https://crafatar.com/avatars/" + p.getUniqueId().toString());
                plugin.jda.getTextChannelById("893472407485038622").sendMessageEmbeds(eb.build()).queue();
                eb.clear();
                p.sendMessage(ChatMessageType.CHAT, new TextComponent(ChatColor.LIGHT_PURPLE + "嘿! 你似乎是第一次加入本伺服!" + ChatColor.RED + "歡迎你加入NTIR伺服器!"));
                p.sendMessage(ChatMessageType.CHAT, new TextComponent(ChatColor.GOLD + "請務必詳閱本伺服的規範" + ChatColor.GREEN + "此外我們幫你準備了新手指南! 也請閱讀完畢喔!"));
                p.sendMessage(ChatMessageType.CHAT, new TextComponent(ChatColor.AQUA + "另外 本伺服設有" + ChatColor.DARK_AQUA + "DC" + ChatColor.AQUA + "群" + ChatColor.BLUE + "誠摯邀請您加入!"));
                TextComponent DC = new TextComponent(ChatColor.RED + "DC連結" + ChatColor.GRAY + "https://discord.gg/bv5emFs4eM");
                TextComponent Website = new TextComponent(ChatColor.RED + "新手指南" + ChatColor.GRAY + "https://firstjoin.natsuiro.xyz");
                DC.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,"https://discord.gg/bv5emFs4eM"));
                DC.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "點我加入DC群\n").italic(false).create()));
                Website.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,"https://firstjoin.natsuiro.xyz"));
                Website.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "點我查看新手指南\n").italic(false).create()));
                p.sendMessage(ChatMessageType.CHAT, DC);
                p.sendMessage(ChatMessageType.CHAT, Website);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "歡迎新玩家" + ChatColor.AQUA + p.getDisplayName() + ChatColor.LIGHT_PURPLE + "首次加入NTIR伺服器");
            }
        }


        plugin.jda.getPresence().setActivity(Activity.playing("上線人數:" + plugin.getProxy().getOnlineCount() + "人"));
        plugin.jda.getTextChannelById("939811023278452756").sendMessage(":arrow_forward:" + "`" + p.getDisplayName() + "`上線了").queue();
    }


}
