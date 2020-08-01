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
package net.sf.l2j.gameserver.gameserverpackets;

import java.util.List;

/**
 * Auth Request packet.
 * 
 * <pre>
 * Format: CCCCHDDBD[S]
 * C protocol
 * C desired Id
 * C accept alternative Id
 * C reserve Host
 * H port
 * D max players
 * D hex Id size
 * B hex Id
 * D subnet size
 * [S] subnets
 * </pre>
 * 
 * @author Zoey76
 */
public class AuthRequest extends GameServerBasePacket {
	
	private static final int VERSION = 6;
	
	public AuthRequest(int id, boolean acceptAlternate, byte[] hexid, int port, boolean reserveHost, int maxplayer, List<String> subnets, List<String> hosts) {
		writeC(0x01);
		writeC(VERSION);
		writeC(id);
		writeC(acceptAlternate ? 0x01 : 0x00);
		writeC(reserveHost ? 0x01 : 0x00);
		writeH(port);
		writeD(maxplayer);
		writeD(hexid.length);
		writeB(hexid);
		writeD(subnets.size());
		for (int i = 0; i < subnets.size(); i++) {
			writeS(subnets.get(i));
			writeS(hosts.get(i));
		}
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}