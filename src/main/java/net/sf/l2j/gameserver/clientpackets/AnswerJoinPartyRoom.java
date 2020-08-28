/*
 * Copyright © 2004-2020 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.clientpackets;

/**
 * Format: (ch) d
 * @author -Wooden-
 */
public final class AnswerJoinPartyRoom extends L2GameClientPacket
{
	private static final String _C__D0_15_ANSWERJOINPARTYROOM = "[C] D0:15 AnswerJoinPartyRoom";
	@SuppressWarnings("unused")
	private int _requesterID; // not tested, just guessed
	
	@Override
	protected void readImpl()
	{
		_requesterID = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// TODO
		// System.out.println("C5:AnswerJoinPartyRoom: d: "+_requesterID);
	}
	
	@Override
	public String getType()
	{
		return _C__D0_15_ANSWERJOINPARTYROOM;
	}
}