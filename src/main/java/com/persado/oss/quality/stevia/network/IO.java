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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import com.persado.oss.quality.stevia.network.http.SteviaHttpClient;
import com.persado.oss.quality.stevia.network.http.HttpPostData;

public class IO {

	
	public static void verifyToken() {
		if (!System.getProperty("SONATYPE_USERNAME", "-").contains("-")) {
			return; // do nothing in travis envs
		}
		String sn = Hardware.getSerialNumber();
		String ni = Hardware.getNetworkIdentifiers();
		String token = ni + "_" + sn + "|" + Runtime.getRuntime().availableProcessors();
		String hmac = Hardware.hmacDigest(token, SSHUtils.SSH_KEY, "HmacSHA512");
		
		File file = new File(FileUtils.getUserDirectory(),".stevia.token");
		try {
			if (!FileUtils.directoryContains(FileUtils.getUserDirectory(), file)) {
				writeToken(token,hmac,file);
				postIt(token,hmac);
			} else {
				verifyToken(token, hmac, file);
			}
		} catch (IOException e) {
			
		}
	}

	private static void verifyToken(String token, String hmac, File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			scanner.next(); // ignore 1st line
			String tokenLine = scanner.next();
			String hmacLine = scanner.next();
			if (!tokenLine.contains(token) || !hmacLine.contains(hmac)) {
				writeToken(token, hmac, file);
			}
			postIt(token,hmac);
		} catch (IOException e) {
		} finally {
			if (scanner != null ) {
				scanner.close();
			}
		}
	}

	private static void postIt(String token, String hmac) {
		HttpPostData data = new HttpPostData(token,hmac);
/*		SteviaHttpClient client = new SteviaHttpClient();
		try {
			client.post("http://23.21.197.137:7777/documents", Arrays.asList(data), 1);
		} catch (Exception e) {
		}
*/	}

	private static void writeToken(String token, String hmac, File file) {
		try {
			FileUtils.writeLines(file, Arrays.asList(
					"{",
					"token:\""+token+"\",", 
					"hmac:\""+hmac+"\"",
					"}"));
			if (SystemUtils.IS_OS_WINDOWS) {
				Runtime.getRuntime().exec("attrib +H +S "+file.getAbsolutePath());
			}
		} catch (IOException e) {
			
		}
	}

}
