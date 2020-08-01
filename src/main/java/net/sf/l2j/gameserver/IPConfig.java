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
package net.sf.l2j.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * IP Config.
 * 
 * @author Zoey76
 * @version 2.6.1.0
 */
public class IPConfig {
	
	private static final Logger LOG = Logger.getLogger(IPConfig.class.getName());
	
	private final List<String> _subnets = new ArrayList<>(5);
	
	private final List<String> _hosts = new ArrayList<>(5);
	
	protected IPConfig() {
		LOG.info("Using automatic network configuration.");
		
		String externalIp = "127.0.0.1";
		try {
			final URL autoIp = new URL("http://ip1.dynupdate.no-ip.com:8245/");
			try (BufferedReader in = new BufferedReader(new InputStreamReader(autoIp.openStream()))) {
				externalIp = in.readLine();
			}
		}
		catch (IOException e) {
			LOG.warning("Failed to connect to api.externalip.net please check your internet connection using 127.0.0.1!");
			externalIp = "127.0.0.1";
		}
		
		try {
			Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
			
			while (niList.hasMoreElements()) {
				NetworkInterface ni = niList.nextElement();
				
				if (!ni.isUp() || ni.isVirtual()) {
					continue;
				}
				
				if (!ni.isLoopback() && ((ni.getHardwareAddress() == null) || (ni.getHardwareAddress().length != 6))) {
					continue;
				}
				
				for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
					if (ia.getAddress() instanceof Inet6Address) {
						continue;
					}
					
					final String hostAddress = ia.getAddress().getHostAddress();
					final int subnetPrefixLength = ia.getNetworkPrefixLength();
					final int subnetMaskInt = IntStream.rangeClosed(1, subnetPrefixLength).reduce((r, e) -> (r << 1) + 1).orElse(0) << (32 - subnetPrefixLength);
					final int hostAddressInt = Arrays.stream(hostAddress.split("\\.")).mapToInt(Integer::parseInt).reduce((r, e) -> (r << 8) + e).orElse(0);
					final int subnetAddressInt = hostAddressInt & subnetMaskInt;
					final String subnetAddress = ((subnetAddressInt >> 24) & 0xFF) + "." + ((subnetAddressInt >> 16) & 0xFF) + "." + ((subnetAddressInt >> 8) & 0xFF) + "." + (subnetAddressInt & 0xFF);
					final String subnet = subnetAddress + '/' + subnetPrefixLength;
					if (!_subnets.contains(subnet) && !subnet.equals("0.0.0.0/0")) {
						_subnets.add(subnet);
						_hosts.add(hostAddress);
						LOG.info("Adding new subnet: " + subnet + " address: " + hostAddress);
					}
				}
			}
			
			// External host and subnet
			_hosts.add(externalIp);
			_subnets.add("0.0.0.0/0");
			LOG.info("Adding new subnet: 0.0.0.0/0 address: " + externalIp);
		}
		catch (SocketException ex) {
			LOG.severe("Configuration failed please manually configure ipconfig.xml!");
			System.exit(0);
		}
	}
	
	public List<String> getSubnets() {
		if (_subnets.isEmpty()) {
			return List.of("0.0.0.0/0");
		}
		return _subnets;
	}
	
	public List<String> getHosts() {
		if (_hosts.isEmpty()) {
			return List.of("127.0.0.1");
		}
		return _hosts;
	}
	
	public static IPConfig getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final IPConfig INSTANCE = new IPConfig();
	}
}