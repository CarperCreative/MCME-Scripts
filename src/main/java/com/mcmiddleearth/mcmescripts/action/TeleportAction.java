package com.mcmiddleearth.mcmescripts.action;

import com.craftmend.thirdparty.iosocket.global.Global;
import com.mcmiddleearth.entities.entities.RealPlayer;
import com.mcmiddleearth.mcmescripts.action.targeted.EntityTargetedAction;
import com.mcmiddleearth.mcmescripts.event.position.EventPosition;
import com.mcmiddleearth.mcmescripts.event.rotation.EventRotation;
import com.mcmiddleearth.mcmescripts.event.rotation.Rotation;
import com.mcmiddleearth.mcmescripts.event.target.EntityEventTarget;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.logging.Logger;

public class TeleportAction extends EntityTargetedAction {

    private static final Random random = new Random();

    public TeleportAction(EntityEventTarget target, EventPosition eventPosition, EventRotation eventRotation, double spread) {
        super(target, (entity,context) -> {

            Vector position = eventPosition.getPosition(context);
            Rotation rotation = eventRotation.getRotation(context);

            Location loc = null;

            if(position != null) loc = new Location(context.getWorld(), position.getX(), position.getY(), position.getZ());
            if(rotation != null && loc != null) {
                loc.setPitch(rotation.getPitch());
                loc.setYaw(rotation.getYaw());
            }
            if(loc==null) return;

            if(spread == 0) {
                if(entity instanceof RealPlayer){
                    entity.teleport(loc);
                } else {
                    Logger.getGlobal().warning(loc.toString());
                    entity.setLocation(loc);
                }
            } else {
                Location randomLoc = randomClose(loc, spread);
                if(entity instanceof RealPlayer){
                    entity.teleport(randomLoc);
                } else {
                    entity.setRotation(0,0,0); // Reset location in case the entity has roll from winged flight (Probably rethink this)
                    entity.setLocation(randomLoc);
                }
                context.getDescriptor().addLine("Target location: "+randomLoc);
            }
        });
        getDescriptor().indent()
                .addLine("Target position: "+eventPosition)
                .addLine("Target rotation: "+eventRotation)
                .addLine("Spread: "+spread).outdent();
    }

    private static Location randomClose(Location location, double spread) {
        double span = 2*spread;
        Location loc = location.clone().add(random.nextDouble()*span-spread,0,random.nextDouble()*span-spread);
        while(!(loc.getBlock().isPassable() && loc.getBlock().getRelative(BlockFace.UP).isPassable())) {
            loc = loc.add(0,1,0);
        }
        return loc;
    }

}
