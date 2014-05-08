package com.persado.oss.quality.stevia.network;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado Intellectual Property Limited
 * %%
 * Copyright (c) Persado Intellectual Property Limited. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *  
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  
 * * Neither the name of the Persado Intellectual Property Limited nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.Scanner;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.SystemUtils;

public class Hardware {

	public static final String getNetworkIdentifiers() {
		StringBuilder bldBuilder = new StringBuilder();

		Enumeration<NetworkInterface> networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
		}
		bldBuilder.append("ni{");
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface if0 = networkInterfaces.nextElement();
			try {
				if (if0.isLoopback() || if0.isPointToPoint() || if0.isVirtual()) {
					continue;
				}
			} catch (SocketException e) {
				continue;
			}

			byte[] hardwareAddress = null;
			try {
				hardwareAddress = if0.getHardwareAddress();

				if (hardwareAddress != null) {
					bldBuilder.append(if0.getDisplayName()).append("=");
					Formatter formatter = new Formatter(bldBuilder);
					try {
						for (int k = 0; k < hardwareAddress.length; k++) {
							formatter
									.format("%02X%s",
											hardwareAddress[k],
											(k < hardwareAddress.length - 1) ? "-"
													: "");
						}
					} finally {
						formatter.close();
					}
					bldBuilder.append("|");
				}
			} catch (SocketException e) {
			}

		}
		if (bldBuilder.length() > 4)
			bldBuilder.setLength(bldBuilder.length() - 1);

		return bldBuilder.append("}").toString();
	}

	public static final String getSerialNumber() {
		// lets gather what we can
		String osString = System.getProperty("os.name") + "-"
				+ System.getProperty("os.version") + "-"
				+ System.getProperty("os.arch");
		try {
			if (SystemUtils.IS_OS_WINDOWS) {
				return H4W.getSerialNumber() + ":WIN";
			}
			if (SystemUtils.IS_OS_LINUX) {
				return H4N.getSerialNumber() + ":LIN";
			}
			if (SystemUtils.IS_OS_MAC_OSX) {
				return H4M.getSerialNumber() + ":MAC";
			}
			if (SystemUtils.IS_OS_SOLARIS) {
				return H4S.getSerialNumber()
						+ ":S"
						+ (SystemUtils.OS_VERSION == null ? "OL"
								: SystemUtils.OS_VERSION);
			}
		} catch (Exception e) {
			// if we're here, we dont know what this is.
			return osString + ":EXC";
		}
		// if we're here then lets note it's unknown
		return osString + ":UNK";
	}

	public static String hmacDigest(String msg, String keyString, String algo) {
		String digest = null;
		try {
			SecretKeySpec key = new SecretKeySpec(
					(keyString).getBytes("UTF-8"), algo);
			Mac mac = Mac.getInstance(algo);
			mac.init(key);

			byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			digest = hash.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return digest;
	}
}

class H4S {
	private static String sn = null;

	public static final String getSerialNumber() {

		if (sn != null) {
			return sn;
		}
		String[] cmdStrings;
		// check if ipmitool is around
		File f = new File("/usr/local/bin/ipmitool");
		if (f.exists()) {
			cmdStrings = new String[] { "/usr/local/bin/ipmitool", "-I", "bmc",
					"fru" };
		} else {
			cmdStrings = new String[] { "/usr/platform/sun4u/sbin/prtdiag",
					"-v" };
		}
		OutputStream os = null;
		InputStream is = null;

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(cmdStrings);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		os = process.getOutputStream();
		is = process.getInputStream();

		try {
			os.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Scanner sc = new Scanner(is);
		try {
			while (sc.hasNext()) {
				String next = sc.next();
				if ("Chassis Serial".equals(next)) {
					if (next.contains(":")) {
						sn = next.split(":")[1].trim();
					} else {
						sn = sc.next().trim();
					}
					break;
				}
			}
		} finally {
			try {
				is.close();
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if (sn == null) {
			throw new RuntimeException("Cannot find computer SN");
		}

		return sn;
	}
}

class H4W {

	private static String sn = null;

	public static final String getSerialNumber() {

		if (sn != null) {
			return sn;
		}

		OutputStream os = null;
		InputStream is = null;

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(new String[] { "wmic", "bios", "get",
					"serialnumber" });
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		os = process.getOutputStream();
		is = process.getInputStream();

		try {
			os.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Scanner sc = new Scanner(is);
		try {
			while (sc.hasNext()) {
				String next = sc.next();
				if ("SerialNumber".equals(next)) {
					sn = sc.next().trim();
					break;
				}
			}
		} finally {
			try {
				is.close();
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if (sn == null) {
			throw new RuntimeException("Cannot find computer SN");
		}

		return sn;
	}
}

class H4N {

	private static String sn = null;

	public static final String getSerialNumber() {

		if (sn != null) {
			return sn;
		}

		OutputStream os = null;
		InputStream is = null;

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			File f = new File("/usr/sbin/dmidecode");
			if (f.exists()) {
				process = runtime.exec(new String[] { "/usr/sbin/dmidecode",
						"-t", "system" });
			} else {
				f = new File("/sbin/blkid");
				if (f.exists()) {
					process = runtime.exec(new String[] { "/sbin/blkid" });
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (process != null) {
			os = process.getOutputStream();
			is = process.getInputStream();

			try {
				os.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			String marker = "Serial Number:";
			try {
				while ((line = br.readLine()) != null) {
					if (line.indexOf(marker) != -1) {
						sn = line.split(marker)[1].trim();
						break;
					} else if (line.indexOf("UUID") != -1) {
						line = line.substring(line.indexOf("UUID=\"")+6);
						sn = line.split("\"")[0].trim();
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if (sn == null) {
			throw new RuntimeException("Cannot find computer SN");
		}

		return sn;
	}
}

class H4M {

	private static String sn = null;

	public static final String getSerialNumber() {

		if (sn != null) {
			return sn;
		}

		OutputStream os = null;
		InputStream is = null;

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(new String[] { "/usr/sbin/system_profiler",
					"SPHardwareDataType" });
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		os = process.getOutputStream();
		is = process.getInputStream();

		try {
			os.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		String marker = "Serial Number";
		try {
			while ((line = br.readLine()) != null) {
				if (line.indexOf(marker) != -1) {
					sn = line.split(marker)[1].trim();
					if (sn.indexOf(":") > -1) {
						sn = sn.split(":")[1].trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if (sn == null) {
			throw new RuntimeException("Cannot find computer SN");
		}

		return sn;
	}
}
