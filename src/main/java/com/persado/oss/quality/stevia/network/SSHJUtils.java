package com.persado.oss.quality.stevia.network;

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
     * @return
     * @throws Exception
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
