package com.persado.oss.quality.stevia.network;

/*-
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2021 Persado Intellectual Property Limited
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

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSHJUtils {

    private static final Logger SSHJ_LOG = LoggerFactory.getLogger(SSHJUtils.class);


    /**
     * Put file to remote host
     * @param host
     * @param username
     * @param password
     * @param localfilePath
     * @param remotePath
     * @param fileName
     * @throws Exception
     */
    public static void putFileToRemoteHost(String host, String username, String password, String localfilePath, String remotePath, String fileName) throws Exception {
        SFTPClient sftp = getSftpClient(host, username, password);
        sftp.put(localfilePath + "/" + fileName, remotePath);
        SSHJ_LOG.info("The file " + fileName + " was transferred successfully to " + remotePath);
    }

    /**
     * Get client
     * @param host
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public static SFTPClient getSftpClient(String host, String username, String password) throws Exception {
        SSHClient ssh = connect(host, username, password);
        return ssh.newSFTPClient();
    }

    /**
     * Connect
     * @param host
     * @param username
     * @param password
     * @return SSHClient
     * @throws Exception Exception
     */
    public static SSHClient connect(String host, String username, String password) throws Exception {
        SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(host);

        try {
            ssh.authPassword(username, password);
            SSHJ_LOG.info("Login to " + host + ":22" + username + "/" + password + " was done successfully");
        } catch (Exception var5) {
            SSHJ_LOG.error("Login to " + host + ":22" + username + "/" + password + " failed");
        }

        return ssh;
    }

}
