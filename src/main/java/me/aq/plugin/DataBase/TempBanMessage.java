package me.aq.plugin.DataBase;

import net.md_5.bungee.api.ChatColor;

public class TempBanMessage {

    public String banMessage(String reason, String unbanDate){

        String msg = ChatColor.RED + "你已被暫時封鎖!\n\n" + ChatColor.LIGHT_PURPLE + "原因: " + ChatColor.GREEN + reason + "\n"
                + ChatColor.YELLOW + "解除封鎖日期:" + ChatColor.RESET + unbanDate + "\n\n" + ChatColor.GOLD + "如你覺得這是誤判或是你想知道如何解除封鎖...請至DC申訴\n" +
                ChatColor.AQUA + "DC連結: " + ChatColor.RED + "https://discord.gg/bv5emFs4eM";
        return msg;
    }
}
