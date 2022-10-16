package net.sf.l2j.gameserver.customnpc;

import net.sf.l2j.gameserver.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.serverpackets.Ride;

import java.util.List;

import static net.sf.l2j.gameserver.clientpackets.Say2.ALL;

class WyvernNpc extends CustomNpc{
    public WyvernNpc() {
        super("So high", "I wanna fly", 31075,
                List.of(new Spawn(18660, 145459, -3151)));
        addHtmlAction("fly", (npc, player) -> {
            Ride mount = new Ride(player.getObjectId(), Ride.ACTION_MOUNT, 12621);
            player.sendPacket(mount);
            player.broadcastPacket(mount);
            player.setMountType(mount.getMountType());

            CreatureSay message = new CreatureSay(npc.getObjectId(), ALL, npc.getName(), "Fly safely!");
            player.sendPacket(message);
        });
    }
}
