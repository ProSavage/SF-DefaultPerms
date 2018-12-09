package net.prosavage.savagefactionsdefaultpermissions;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.Permissable;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class FactionCreateEventListener implements Listener {

    @EventHandler
    public void onFactionCreate(FactionCreateEvent event) {
        DefaultPermissions plugin = DefaultPermissions.instance;

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugin.getLogger().info("Setting Default Permissions for faction: " + event.getFactionTag());


            Configuration configuration = plugin.getConfig();
            ConfigurationSection defaultPerms = plugin.getConfig().getConfigurationSection("default-perms");
            Faction faction = Factions.getInstance().getByTag(event.getFactionTag());
            for (String section : defaultPerms.getKeys(false)) {
                List<String> permissions = defaultPerms.getStringList(section);
                Role role = getRoleFromString(section);
                Relation relation = Relation.fromString(section);
                if (role == Role.LEADER) {
                    for (String permission : permissions) {
                        faction.setPermission(relation, PermissableAction.fromString(permission), Access.ALLOW);
                    }
                } else {
                    for (String permission : permissions) {
                        faction.setPermission(role, PermissableAction.fromString(permission), Access.ALLOW);
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
