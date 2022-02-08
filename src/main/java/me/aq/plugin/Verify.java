package me.aq.plugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class Verify extends ListenerAdapter {

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
            eb.setAuthor("你已成功綁定Minecraft帳號");
            eb.setColor(Color.GREEN);
            eb.setFooter("玩家UUID:"  + plugin.data.getPlayer(args[1]).getUniqueId().toString());
            eb.setTimestamp(e.getMessage().getTimeCreated());
            eb.setDescription("你已成功將將你的dc帳號連結至" + plugin.data.getPlayer(args[1]).getDisplayName());

            e.getMessage().replyEmbeds(eb.build()).queue();
            Role role = plugin.jda.getRoleById("939336944339546172");
            e.getGuild().addRoleToMember(e.getMember(),role).queue();
            eb.clear();




        }
    }

}
