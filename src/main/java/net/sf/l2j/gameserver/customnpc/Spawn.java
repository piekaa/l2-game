package net.sf.l2j.gameserver.customnpc;

public class Spawn {

    private static int idCounter = 999999;

    private final int x;
    private final int y;
    private final int z;

    private final int id = idCounter--;

    public Spawn(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getId() {
        return id;
    }
}
