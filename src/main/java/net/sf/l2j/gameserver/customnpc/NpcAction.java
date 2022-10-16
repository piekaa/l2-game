package net.sf.l2j.gameserver.customnpc;

import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

public interface NpcAction {

    void perform(L2NpcInstance npc, L2PcInstance player);
}
