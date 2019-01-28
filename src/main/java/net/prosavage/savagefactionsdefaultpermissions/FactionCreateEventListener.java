package net.prosavage.savagefactionsdefaultpermissions;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class FactionCreateEventListener implements Listener {

    @EventHandler
    public void onFactionCreate(FactionCreateEvent event) {
        DefaultPermissions plugin = DefaultPermissions.instance;

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugin.getLogger().info("Setting Default Permissions for faction: " + event.getFactionTag());


            Configuration configuration = plugin.getConfig();
            ConfigurationSection defaultPermsAllow = plugin.getConfig().getConfigurationSection("default-perms-allow");
            ConfigurationSection defailtPermsDeny = plugin.getConfig().getConfigurationSection("default-perms-deny");
            Faction faction = Factions.getInstance().getByTag(event.getFactionTag());
            for (String sectionAllow : defaultPermsAllow.getKeys(false)) {
                for (String sectionDeny : defailtPermsDeny.getKeys(false)) {
                    List<String> permissionsAllow = defaultPermsAllow.getStringList(sectionAllow);
                    List<String> permissionsDeny = defailtPermsDeny.getStringList(sectionDeny);
                    Role roleallow = getRoleFromString(sectionAllow);
                    Role roledeny = getRoleFromString(sectionDeny);
                    Relation relationAllow = Relation.fromString(sectionAllow);
                    Relation relationDeny = Relation.fromString(sectionDeny);
                    if (roleallow == Role.LEADER) {
                        for (String permission : permissionsAllow) {
                            faction.setPermission(relationAllow, PermissableAction.fromString(permission), Access.ALLOW);
                        }
                        for(String permission : permissionsDeny){
                            faction.setPermission(relationDeny, PermissableAction.fromString(permission), Access.DENY);
                        }
                    } else {
                        for (String permission : permissionsAllow) {
                            faction.setPermission(roleallow, PermissableAction.fromString(permission), Access.ALLOW);
                        }
                        for (String permission : permissionsDeny) {
                            faction.setPermission(roledeny, PermissableAction.fromString(permission), Access.DENY);
                        }
                    }

                }
            }

        }, 20L);



    }


    private Role getRoleFromString(String role) {
        switch (role) {
            case "recruit":
                return Role.RECRUIT;
            case "normal":
                return Role.NORMAL;
            case "mod":
                return Role.MODERATOR;
            case "coleader":
                return Role.COLEADER;
            default:
                return Role.LEADER;
        }
    }
}
