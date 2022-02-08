package me.aq.plugin;

import me.aq.plugin.DataBase.MySQL;
import me.aq.plugin.DataBase.SQLediter;
import me.aq.plugin.DataBase.TempBanMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public final class NTIRBungeeChat extends Plugin {

    private static NTIRBungeeChat plugin;

    public MySQL SQL;
    public SQLediter data;
    public JDA jda;
    public TempBanMessage tempBanMessage;
    String token = "OTI3MDAxOTM2MjE2MDkyNzQy.YdD31A.nkdTXIqv7Y_dLPy-VsR8SKS5xsg";

    public static NTIRBungeeChat getPlugin(){return plugin;}

    @Override
    public void onEnable() {
        plugin = this;

        this.tempBanMessage = new TempBanMessage();
        this.SQL = new MySQL();
        this.data = new SQLediter();
        data.SQLGetter(this);

        try {
            SQL.connect();

        } catch (ClassNotFoundException | SQLException e) {
            getProxy().getLogger().info("DB not connected");
            getProxy().getLogger().info("資料庫是該插件的必要功能");
        }
        try {
            jda = JDABuilder.createDefault(token).build().awaitReady();
            plugin.jda.getTextChannelById("893472407485038622").sendMessage( ":small_blue_diamond:連線端已開啟").queue();
        }catch (LoginException | InterruptedException e){
            e.printStackTrace();
        }
        jda.getPresence().setActivity(Activity.playing("上線人數:" + getProxy().getOnlineCount() + "人"));
        jda.addEventListener(new Verify());
        getProxy().getPluginManager().registerListener(this, new Join());
        getProxy().getPluginManager().registerListener(this, new Leave());


    }

    @Override
    public void onDisable() {
        plugin.jda.getTextChannelById("893472407485038622").sendMessage( ":red_circle:連線端已關閉").queue();
        jda.shutdownNow();
        SQL.disconnect();
    }
}
