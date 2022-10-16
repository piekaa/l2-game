package net.sf.l2j.gameserver.customnpc;

import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

import java.util.List;

class BufferNpc extends CustomNpc {

    public BufferNpc() {
        super("Piekoszek", "Buffer", 35656,
                // Use /loc command to find current location of character
                List.of(new Spawn(18860, 145459, -3151), // Dion center
                        new Spawn(-84500, 243200, -3729) // Talking island center
                ));
        addHtmlText("Hello");

        addHtmlAction("Wind walk", (npc, player) -> {
            buff(player, 1204, 2);
        });

        addHtmlAction("Few dances", (npc, player) -> {
            buff(player, 271, 1);
            buff(player, 272, 1);
            buff(player, 273, 1);
            buff(player, 274, 1);
            buff(player, 275, 1);
        });

        addHtmlAction("Cancel", (npc, player) -> {
            L2Effect[] effects = player.getAllEffects();
            if (effects == null) {
                return;
            }

            for (L2Effect e : effects) {
                if (e != null) {
                    e.exit(true);
                }
            }
            player.updateAndBroadcastStatus(2);
        });
    }

    /**
     * Find skillId and skillLevel in skill_trees table or skill_trees.sql in datapack project
     */
    private void buff(L2PcInstance player, int skillId, int skillLevel) {
        L2Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
        skill.getEffects(player, player); // method also applies effects
    }
}
