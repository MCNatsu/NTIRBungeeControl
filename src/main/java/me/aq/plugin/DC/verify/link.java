package me.aq.plugin.DC.verify;

import me.aq.plugin.NTIRBungeeChat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class link extends ListenerAdapter {

    private NTIRBungeeChat plugin;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {

        plugin = NTIRBungeeChat.getPlugin();

        User user = e.getAuthor();


        if(user.isBot() || e.isWebhookMessage()) return;

        if(!e.getChannel().getId().equals("935867246956412948")){return;}

        String[] args = e.getMessage().getContentRaw().split(" ");


        if(args[0].equalsIgnoreCase("!link")){

            if ((args.length != 2)){
                e.getMessage().reply("請輸入驗證碼!").queue();
                return;
            }

            if(plugin.data.getPlayer(args[1]) == null){
                e.getMessage().reply("該玩家不存在或尚未產生驗證碼").queue();
                return;
            }

            plugin.data.verifydc(e.getMember() , args[1]);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor("你已成功綁定Minecraft帳號",null,"https://crafatar.com/avatars/" + plugin.data.getPlayer(args[1]).getUniqueId().toString());
            eb.setColor(Color.GREEN);
            eb.setFooter("UUID:"  + plugin.data.getPlayer(args[1]).getUniqueId().toString());
            eb.setTimestamp(e.getMessage().getTimeCreated());

            e.getMessage().replyEmbeds(eb.build()).queue();
            Role linkedPlayer = plugin.jda.getRoleById("939336944339546172");
            Role Player = plugin.jda.getRoleById("893472407350808650");
            Role wait = plugin.jda.getRoleById("893472407350808649");
            e.getGuild().addRoleToMember(e.getMember(),linkedPlayer).queue();
            e.getGuild().addRoleToMember(e.getMember(),Player).queue();
            e.getGuild().removeRoleFromMember(e.getMember(),wait).queue();
            eb.clear();


        }
    }

}
