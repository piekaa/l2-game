package net.sf.l2j.gameserver.customnpc;

import com.google.common.collect.ImmutableList;
import net.sf.l2j.gameserver.datatables.NpcTable;
import net.sf.l2j.gameserver.model.actor.instance.L2NpcInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.templates.L2NpcTemplate;
import net.sf.l2j.gameserver.templates.StatsSet;

import java.util.*;

public abstract class CustomNpc {
    private static int idCounter = 999999;

    private static List<CustomNpc> customNPCs = new ArrayList<>();
    private static Map<Integer, CustomNpc> customNPCsMap = new HashMap<>();

    private final L2NpcTemplate template;
    private final int id = idCounter--;
    private final List<Spawn> spawns;

    private List<String> htmlContents = new ArrayList<>();
    private List<NpcAction> actions = new ArrayList<>();

    public static List<CustomNpc> getCustomNPCs() {
        return ImmutableList.copyOf(customNPCs);
    }

    public static Optional<CustomNpc> customNpcById(int id) {
        return Optional.ofNullable(customNPCsMap.get(id));
    }

    public static void doAction(int customNpcId, int actionId, L2NpcInstance npc, L2PcInstance player) {
        customNpcById(customNpcId).ifPresentOrElse(n -> n.doAction(actionId, npc, player),
                () -> {
                    throw new RuntimeException("Custom npc with id" + customNpcId + " does not exist");
                });
    }


    /**
     *
     * @param idTemplate find it in NPC table, use only those with type=L2Npc
     */
    public CustomNpc(String name, String title, int idTemplate, List<Spawn> spawns) {
        StatsSet stats = NpcTable.getInstance().getTemplate(idTemplate).getStatsSet();

        stats.set("serverSideName", true);
        stats.set("serverSideTitle", true);
        stats.set("name", name);
        stats.set("title", title);
        stats.set("npcId", id);

        template = new L2NpcTemplate(stats);
        this.spawns = spawns;
        customNPCs.add(this);
        customNPCsMap.put(id, this);
    }


    public L2NpcTemplate getTemplate() {
        return template;
    }

    public int getId() {
        return id;
    }

    public List<Spawn> getSpawns() {
        return ImmutableList.copyOf(spawns);
    }

    public String initialHtml(int objectId) {
        StringBuilder html = new StringBuilder("<html><body>" + template.name + ": <br>");
        for (String htmlContent : htmlContents) {
            html.append(htmlContent).append("<br>");
        }
        html.append("</body></html>");
        return html.toString().replaceAll("OBJECT_ID", objectId + "");
    }

    protected void addHtmlText(String text) {
        htmlContents.add(text);
    }

    protected void addHtmlAction(String text, NpcAction action) {
        htmlContents.add(String.format("<a action=\"bypass -h custom_npc_%d_%d_OBJECT_ID\">%s</a>", id, actions.size(), text));
        actions.add(action);
    }

    private void doAction(int actionId, L2NpcInstance npc, L2PcInstance player) {
        if (actionId >= actions.size()) {
            throw new RuntimeException("Action id grater than actions size");
        }
        actions.get(actionId).perform(npc, player);
    }
}
