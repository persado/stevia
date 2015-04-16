package com.persado.oss.quality.stevia.network;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.connection.ChannelState;
import com.sshtools.j2ssh.session.SessionChannelClient;
import com.sshtools.j2ssh.sftp.SftpFile;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;



/**
 * The Class SSHUtils.
 */
public class SSHUtils {

	/** The Constant SSH_LOG. */
	private static final Logger SSH_LOG = LoggerFactory.getLogger(SSHUtils.class);

	/** The port. */
	private static final int SSH_PORT = 22;
	
	/** The key */
	public static final String SSH_KEY = "STEVIA_KEY_ebf46d5a1508759f09313c59552f5afbf9587776157d8f727998e1669ca4e837ec8";
	
	/**
	 * Connect.
	 * 
	 * @param host	 the host
	 * @param username the username
	 * @param password the password
	 * @return the ssh client
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SshClient connect(String host, String username,String password) throws IOException {
	     return connect(host, SSH_PORT, username, password);
	}
	
	/** Disconnect
	 * @param ssh
	 */
	public static void disConnect(SshClient ssh) {
		if(ssh.isConnected()){
			ssh.disconnect();
		}
	}
	
	/**
	 * Connect. This action simulates all the actions required for a SSH connection
	 *
	 * @param host  the hostname of the host you want to connect to
	 * @param port the port of the host you want to connect to
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @return the ssh client you connected to
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SshClient connect(String host, int port,String username,String password) throws IOException {
		SSH_LOG.info("Connecting to " + host);
		SshClient ssh = new SshClient();
		ssh.connect(host, port, new IgnoreHostKeyVerification());
		PasswordAuthenticationClient passwordAuthenticationClient = new PasswordAuthenticationClient();
		passwordAuthenticationClient.setUsername(username);
		passwordAuthenticationClient.setPassword(password);
		int result = ssh.authenticate(passwordAuthenticationClient);
		if (result != AuthenticationProtocolState.COMPLETE) {
			throw new IOException("Login to " + host + ":" + port + " "+ username + "/" + password + " failed");
		}
		SSH_LOG.info("Connected " + host);
		return ssh;
	}
	
	/**
	 * Create an SFTP connection to a client
	 * 
	 * @param host the hostname of the Client.The default port used for connection is 22
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @return the client 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SftpClient getSftpClient(String host, String username,String password) throws IOException {
		return getSftpClient(host, SSH_PORT, username, password);
	}

	/**
	 * Create an SFTP connection to a client
	 *
	 * @param host the hostname of the client
	 * @param port the port the user want to use for connection
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @return the client
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SftpClient getSftpClient(String host,int port, String username,String password) throws IOException {
		SshClient ssh = connect(host, port,username, password);
		return ssh.openSftpClient();
	}
	
	/**
	 * Puts the file to remote host.
	 *
	 * @param host the hostname of the client. The port number is by default 22
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param localfilePath the localfile path where the file is situated
	 * @param remotePath the remote path where the file is going to be put
	 * @param fileName the file name you want to put
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	
	public static void putFileToRemoteHost(String host, String username,String password, String localfilePath,String remotePath,String fileName)throws IOException {
		putFileToRemoteHost(host, SSH_PORT, username, password,localfilePath,remotePath,fileName);
	}
	
	/**
	 * Put file to remote host.
	 *
	 * @param host the hostname of the client
	 * @param port the port number the user want to use for connection
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param localfilePath the localfile path where the file is situated
	 * @param remotePath the remote path where the file is going to be put
	 * @param fileName the file name you want to put
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public static void putFileToRemoteHost(String host, int port,String username,String password, String localfilePath,String remotePath,String fileName)throws IOException {
		SftpClient sftp = getSftpClient(host,port, username, password);
		sftp.lcd(localfilePath);
		sftp.cd(remotePath);
		sftp.put(fileName);
		List<SftpFile> dirContents = sftp.ls(remotePath);
	    Iterator<SftpFile> it = dirContents.iterator();
	    while(it.hasNext()){    		  
	    	if(it.next().getFilename().equals(fileName)){
	    		 SSH_LOG.info("The file " + fileName + " was tranferred successfully to " + remotePath);
	    		 return;
	    	}
	    }
	    SSH_LOG.error("The file " + fileName + " was not tranferred  to " + remotePath);	
	}
	
	/**
	 * Gets the file from remote host.
	 *
	 * @param hostthe hostname of the client. The port number is by default 22
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param remoteFilePath remotePath the remote path from where the file is going to be get
	 * @param localfilePath the localfile path where the file is going to be put
	 * @param fileName the name of the file you want to transfer
	 * @return the file from remote host
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getFileFromRemoteHost(String host, String username,String password, String remoteFilePath,String localPath,String fileName) throws IOException {
			getFileFromRemoteHost(host, SSH_PORT, username, password, remoteFilePath, localPath,fileName);	
	}
	
	/**
	 * Gets the file from remote host.
	 *
	 * @param host the hostname of the client
	 * @param port the port number the user want to use for connection
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param remoteFilePath remotePath the remote path from where the file is going to be get
	 * @param localfilePath the local file path where the file is going to be put
	 * @param fileName the name of the file you want to transfer
	 * @return the file from the remote host
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getFileFromRemoteHost(String host, int port,String username,String password, String remoteFilePath,String localPath,String fileName) throws IOException {
		SftpClient sftp = getSftpClient(host, port,username, password);
		sftp.cd(remoteFilePath);
		sftp.lcd(localPath);
		sftp.get(fileName);
		File f = new File(localPath+System.getProperty("file.separator")+fileName);
		if (!f.exists()){
			SSH_LOG.error("The file " + fileName + " was not tranferred  to " + localPath);	
		}
		else{
			SSH_LOG.info("The file " + fileName + " was tranferred successfully to " + localPath);
		}		
	}
	
	/**
	 * Gets a server file.
	 *
	 * @param prefix the prefix
	 * @param fileLocation the file location where the file is situated
	 * @param host the host name of the server Default port number is 22
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @return the server file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getServerFile(String prefix, String fileLocation ,String host, String username, String password) throws IOException{
	    return getServerFile(prefix, fileLocation, host, SSH_PORT, username, password);
	}

	/**
	 * Gets a server file.
	 *
	 * @param prefix the prefix
	 * @param fileLocation the file location
	 * @param host the host name of the server
	 * @param port the port number the user want to use for connection
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @return the server file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getServerFile(String prefix, String fileLocation ,String host, int port,String username, String password) throws IOException{
		SftpFile file;
		List<String> fileNames = new ArrayList<String>();
		String localdir = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
		SftpClient sftp = getSftpClient(host, port,username, password);
		sftp.cd(fileLocation);
		sftp.lcd(localdir);
		@SuppressWarnings("rawtypes")
		List listinbox = sftp.ls();
		for (Iterator<?> it = listinbox.iterator(); it.hasNext();) {
			file = (SftpFile) it.next();
			if(file.getFilename().contains(prefix)){
				fileNames.add(file.getFilename());
				break;
			}
		}
		Collections.sort(fileNames, Collections.reverseOrder());
		SSH_LOG.info(fileNames.get(0));
		String outputFileName = fileNames.get(0);
		SSH_LOG.info("The output file is " + outputFileName);
		FileOutputStream fos = new FileOutputStream(outputFileName);
		try {
			SSH_LOG.info("Downloading the file: " + outputFileName);
			sftp.get(outputFileName, outputFileName);
			SSH_LOG.info("Download completed");
		} 
		catch (IOException e) {
			SSH_LOG.error("Could not download file");
		} 
		finally {
			try {
				fos.close();
			} 
			catch (IOException e1) {
				SSH_LOG.error("Cant do anything here... just ignore it");
			}
		}
		return localdir + outputFileName;
	}

	/**
	 * Execute command in a remote host.
	 * 
	 * @param host the host name of the server Default port number is 22
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param cmd the commands you want to execute remotely
	 * @return the string with the parameters you entered
	 * @throws IOException, InterruptedException
	 */
	public static String executeCommand(String host, String username,String password, String cmd) throws IOException, InterruptedException {
		return executeCommand(host, SSH_PORT, username, password, cmd);
	}

	/**
	 * Execute command.
	 *
	 * @param host the host name of the server
	 * @param port the port number the user want to use for connection
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param cmd the commands you want to execute remotely
	 * @return the string with the parameters you entered
	 * @throws IOException, InterruptedException
	 */
	public static String executeCommand(String host, int port,String username,String password, String cmd) throws IOException, InterruptedException {
		return executeCommands(host, port,username, password, new String[] { cmd });
	}
	
	/**
	 * Execute commands.
	 *
	 * @param host he host name of the server
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param cmds the commands you want to execute remotely
	 * @return the string with the parameters you entered 
	 * @throws IOException, InterruptedException
	 */
	public static String executeCommands(String host, String username,String password, String[] cmds) throws IOException, InterruptedException {
	  return executeCommands(host, SSH_PORT, username, password, cmds);
	}
	
	/**
	 * Execute commands.
	 *
	 * @param host the host name of the server
	 * @param port the port number the user want to use for connection
	 * @param username the username required for authentication
	 * @param password the password required for authentication
	 * @param cmds the commands you want to execute remotely
	 * @return the string with the parameters you entered 
	 * @throws InterruptedException 
	 * @throws IOException, InterruptedException
	 */
	public static String executeCommands(String host, int port,String username,String password, String[] cmds) throws IOException, InterruptedException {
		String commandOutput = "";
		SshClient ssh = connect(host, port,username, password);
		SessionChannelClient sessionChannel = ssh.openSessionChannel();

		// make a script out of all the commands
		StringBuilder cmdToExecute = new StringBuilder();
		for (String cmd : cmds) {
			cmdToExecute.append(cmd).append(";");
		}
		// execute the whole thing
		if (sessionChannel.executeCommand(cmdToExecute.toString())) {
			/**
			 * Reading from the session InputStream
			 */
			InputStream in = sessionChannel.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in)); // read to buffer from the stream
		    StringBuffer buffer = new StringBuffer();
	        String line;
			   while (((line = br.readLine()) !=  null)){ // read from the buffer of the stream the line
                   buffer.append(line); // append the result line to the String buffer.
                   buffer.append("\n");
			   }
			   commandOutput = buffer.toString(); 
			sessionChannel.getState().waitForState(ChannelState.CHANNEL_CLOSED);
			br.close();
			ssh.disconnect();
			return commandOutput;

		} 
		else {
			SSH_LOG.error("The command did not execute");
			ssh.disconnect();
			return commandOutput;
		}
	}
}
