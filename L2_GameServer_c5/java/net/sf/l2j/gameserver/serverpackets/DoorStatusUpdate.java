/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.serverpackets;

import net.sf.l2j.gameserver.model.actor.instance.L2DoorInstance;

/**
 * 61 
 * d6 6d c0 4b		door id 
 * 8f 14 00 00 		x
 * b7 f1 00 00 		y
 * 60 f2 ff ff 		z
 * 00 00 00 00 		??
 *  
 * format  dddd    rev 377  ID:%d X:%d Y:%d Z:%d
 *         ddddd   rev 419
 *  
 * @version $Revision: 1.3.2.2.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public class DoorStatusUpdate extends ServerBasePacket
{
	private static final String _S__61_DOORSTATUSUPDATE = "[S] 4d DoorStatusUpdate";
	private L2DoorInstance _door;

	public DoorStatusUpdate(L2DoorInstance door)
	{
		_door=door;
	}
	
	final void runImpl()
	{
		// no long-running tasks
	}
	
	final void writeImpl()
	{
		writeC(0x4d);
		writeD(_door.getObjectId());
		writeD(_door.getOpen());
		writeD(_door.getDamage());
		writeD(_door.isEnemyOf(getClient().getActiveChar()) ? 1 : 0);
		writeD(_door.getDoorId());
	}
	
	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	public String getType()
	{
		return _S__61_DOORSTATUSUPDATE;
	}

}