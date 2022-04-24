package me.aq.plugin;

import me.aq.plugin.Command.msg;
import me.aq.plugin.Command.ping;
import me.aq.plugin.DC.DCtoMC;
import me.aq.plugin.DC.DiscordWebhook;
import me.aq.plugin.DC.verify.link;
import me.aq.plugin.DC.verify.verify;
import me.aq.plugin.Data.utils.BanMessage;
import me.aq.plugin.Data.Database.MySQL;
import me.aq.plugin.Data.Database.SQLediter;
import me.aq.plugin.Data.utils.TempBanMessage;
import me.aq.plugin.Event.Join;
import me.aq.plugin.Event.Leave;
import me.aq.plugin.Event.onChatBridgeCall;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.md_5.bungee.api.plugin.Plugin;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.TimeZone;

public final class NTIRBungeeChat extends Plugin {

    private static NTIRBungeeChat plugin;

    public MySQL SQL;
    public SQLediter data;
    public JDA jda;
    public TempBanMessage tempBanMessage;
    public BanMessage BanMessage;
    public DiscordWebhook webhook;
    String token = "OTI3MDAxOTM2MjE2MDkyNzQy.YdD31A.0eq_ZcNgLQkOG43nix_UgRUT-l4";
    String url = "https://discordapp.com/api/webhooks/956882137141899265/EmWao3-wZvtljIH9opRznh5JFDfsLtEiyolw-8dV5GW-0n0rAhhVyfHSCArXRm1IAXql";

    public static NTIRBungeeChat getPlugin(){return plugin;}

    @Override
    public void onEnable() {
        plugin = this;
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

        this.tempBanMessage = new TempBanMessage();
        this.BanMessage = new BanMessage();

        this.SQL = new MySQL();
        this.data = new SQLediter();
        data.SQLGetter(this);

        this.webhook = new DiscordWebhook(url);

        try {
            SQL.connect();
            data.createChatSettingsTable();

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
        jda.addEventListener(new link());
        jda.addEventListener(new DCtoMC());
        getProxy().getPluginManager().registerListener(this, new Join());
        getProxy().getPluginManager().registerListener(this, new Leave());
        getProxy().getPluginManager().registerCommand(this,new ping("ping"));
        getProxy().getPluginManager().registerCommand(this,new verify("verify"));
        getProxy().getPluginManager().registerCommand(this,new msg("msg"));
        getProxy().getPluginManager().registerListener(this,new onChatBridgeCall());
        getProxy().registerChannel("ntir:chat");

    }

    @Override
    public void onDisable() {
        plugin.jda.getTextChannelById("893472407485038622").sendMessage( ":red_circle:連線端已關閉").queue();
        jda.shutdownNow();
        SQL.disconnect();
    }
}
