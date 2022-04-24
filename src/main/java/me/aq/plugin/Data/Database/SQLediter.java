package me.aq.plugin.Data.Database;

import me.aq.plugin.NTIRBungeeChat;
import net.dv8tion.jda.api.entities.Member;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLediter {


    private NTIRBungeeChat plugin;

    public void SQLGetter(NTIRBungeeChat plugin){
        this.plugin = plugin;
    }

    public void createChatSettingsTable(){
        PreparedStatement ps;
        PreparedStatement ps1;
        PreparedStatement ps2;
        try {
            ps = plugin.SQL.getChatConnection().prepareStatement("CREATE TABLE IF NOT EXISTS PlayerChatSettings"
                    + "(Player VARCHAR(100), UUID VARCHAR(100),Prefix VARCHAR(100) , ShowOnlineMSG BOOLEAN, CustomName VARCHAR(100), NameColor VARCHAR(100), PrefixColor VARCHAR(100),PRIMARY KEY(UUID))");
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //玩家初始設定
    public void createPlayer(ProxiedPlayer p){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getChatConnection().prepareStatement("INSERT INTO PlayerChatSettings (Player,UUID,Prefix,ShowOnlineMSG,CustomName,NameColor,PrefixColor) VALUE(?,?,?,?,?,?,?)");
            ps.setString(1,p.getName());
            ps.setString(2,p.getUniqueId().toString());
            ps.setString(3, ChatColor.AQUA + "玩家" + ChatColor.RESET);
            ps.setBoolean(4,true);
            ps.setString(5,null);
            ps.setString(6,"#FFFFFF");
            ps.setString(7,null);

            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean chatSettingExist(ProxiedPlayer p){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getChatConnection().prepareStatement("SELECT UUID FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //玩家聊天設定(玩家發送訊息)
    public String getPrefix(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT Prefix FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("prefix");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getPrefixColor(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT PrefixColor FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("PrefixColor");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getNameColor(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT NameColor FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("NameColor");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getCustomName(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT CustomName FROM PlayerChatSettings WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("CustomName");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //玩家聊天訊息(玩家接收訊息)
    public List<String> getIgnoredPlayer(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT IgnoredPlayerUUID FROM PlayerReceiveSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            ArrayList<String> IgnoredPlayer = new ArrayList<>();
            for (int cur = 0; cur < ignoredPlayerCount(p);cur++ ,rs.next()){
                IgnoredPlayer.add(rs.getString("IgnoredPlayerUUID"));

            }
            return IgnoredPlayer;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int ignoredPlayerCount(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT COUNT(IgnoredPlayerUUID) AS COUNT FROM PlayerReceiveSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt("COUNT");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }


    //玩家聊天訊息(系統訊息)
    public String getOnlineMessageFormat(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT OnlineMessage FROM PlayerSystemSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("OnlineMessage");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean showDeathMessage(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getChatConnection().prepareStatement("SELECT ShowDeathMessage FROM PlayerSystemSetting WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("ShowDeathMessage");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //verify
    public ProxiedPlayer getPlayer(String linkCode){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT PlayerName FROM LinkList WHERE LinkedCode=?");
            ps.setString(1, linkCode);
            ResultSet rs = ps.executeQuery();
            String name = null;
            if(rs.next()){
                name = rs.getString("PlayerNAME");
                ProxiedPlayer p = plugin.getProxy().getPlayer(name);
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void verifydc(Member member, String LinkCode){

        try {
            String DCId = member.getId();


            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE LinkList SET dcName=? ,DiscordID=?, Linked=? WHERE LinkedCode=?");
            ps.setString(1, member.getEffectiveName());
            ps.setString(2, DCId);
            ps.setString(3, "true");
            ps.setString(4, LinkCode);
            ps.executeUpdate();
            return;


        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public String getVerifyCode(UUID uuid){


        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT LinkedCode FROM LinkList WHERE PlayerUUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            String linkCode = null;
            if(rs.next()){
                linkCode = rs.getString("LinkedCode");
                return linkCode;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean verified(UUID uuid){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT Linked FROM LinkList WHERE PlayerUUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if(results.next()){
                return true;
            }
            return false;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }
    public void verify(ProxiedPlayer p , String LinkCode){

        try {
            UUID uuid = p.getUniqueId();

            if(!existsverified(uuid)){

                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT INTO LinkList (PlayerName,PlayerUUID, dcName,DiscordID, LinkedCode) VALUES (?,?,?,?,?)");
                ps.setString(1, p.getDisplayName());
                ps.setString(2, uuid.toString());
                ps.setString(3, null);
                ps.setString(4, null);
                ps.setString(5, LinkCode);
                ps.executeUpdate();

            }


        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public boolean existsverified(UUID uuid){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT PlayerUUID FROM LinkList WHERE PlayerUUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if(results.next()){
                return true;
            }
            return false;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;

    }

    //ban
    public ProxiedPlayer getTempBannedPlayer(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT Player FROM TempBanList WHERE Player=?");
            ps.setString(1, p.getDisplayName());
            ResultSet rs = ps.executeQuery();
            String name = null;
            if(rs.next()){
                name = rs.getString("Player");
                ProxiedPlayer banedplayer = plugin.getProxy().getPlayer(name);
                return banedplayer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void unBan(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("DELETE FROM TempBanList WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean tempBanned(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT UUID FROM TempBanList WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public String getReason(ProxiedPlayer p){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT Reason FROM TempBanList WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            String reason = null;
            if(rs.next()){
                reason = rs.getString("Reason");
                return reason;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public String getUnBanDate(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT unBanDate FROM TempBanList WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            String date = null;
            if(rs.next()){
                date = rs.getString("unBanDate");
                return date;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }
    public long getunBanTime(ProxiedPlayer p){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT Time FROM TempBanList WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            long time = 0;
            if(rs.next()){
                time = rs.getLong("Time");
                return time;
            }
            return 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;

    }
    public boolean Banned(ProxiedPlayer p){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT UUID FROM banlist WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean enablePVP(ProxiedPlayer p){
        try {

            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT pvp FROM PlayerChatControl WHERE UUID=?");
            ps.setString(1,p.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("pvp");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
