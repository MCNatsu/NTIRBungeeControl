package me.aq.plugin.DataBase;

import me.aq.plugin.NTIRBungeeChat;
import net.dv8tion.jda.api.entities.Member;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLediter {


    private NTIRBungeeChat plugin;

    public void SQLGetter(NTIRBungeeChat plugin){
        this.plugin = plugin;

    }

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
}
