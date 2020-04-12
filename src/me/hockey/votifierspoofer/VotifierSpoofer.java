package me.hockey.votifierspoofer;

import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.*;
import java.util.*;
import java.net.*;
import java.io.*;

import javax.crypto.Cipher;

public class VotifierSpoofer
{
    public static byte[] encrypt(byte[] data, PublicKey key) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception
    {
        System.out.print("--------------------------------------------------------------------\n" +
                "Votifier Spoofer by Hockeyfan360. Licensed under GNU GPL v3.\n"+
                "--------------------------------------------------------------------\n\n");

        Scanner userInput = new Scanner(System.in);

        System.out.print("Hostname (example: myserver.com): ");
        String hostname = userInput.nextLine();

        System.out.print("\nPort (default is 8192): ");
        String portString = userInput.nextLine();

        int port = 0;
        try
        {
            port = Integer.parseInt(portString);
            if (port < 0 || port > 65537)
            {
                System.out.println("Invalid port, must be a number between 0 and 65537.");
                System.exit(1);
            }
        }
        catch (Exception e)
        {
            System.out.println("Invalid port, must be a number between 0 and 65537.");
            System.exit(1);
        }

        System.out.print("\nVoting site name to spoof (can be anything): ");
        String siteName = userInput.nextLine();

        System.out.print("\nWho is voting? (put your username here): ");
        String username = userInput.nextLine();

        System.out.print("\nIP to spoof for origin of vote (example: 8.8.8.8, but can be anything): ");
        String ip = userInput.nextLine();

        System.out.print("\nTimestamp to spoof (can be literally anything): ");
        String timestamp = userInput.nextLine();

        System.out.print("\nRSA 2048 BIT Public Key (No spaces, no line breaks, Base64 encoded): ");
        String publicKey = userInput.nextLine();

        System.out.println("\n\nSpoofing vote...");

        spoofVote(hostname, port, siteName, username, ip, timestamp, publicKey);

        System.out.println("DONE!");
    }

    public static void spoofVote(String hostname, int port, String siteName, String username, String ip, String timestamp, String publicKey)
    {
        try
        {
            byte[] publicBytes = Base64.getDecoder().decode(publicKey.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            Socket socket = new Socket(hostname, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write(encrypt(new String("VOTE\n" + siteName + "\n" + username + "\n" + ip + "\n" + timestamp + "\n").getBytes(), pubKey));
        }
        catch (SocketException e)
        {
            System.out.println("ERROR: Connection to server could not be established. Check hostname and port. Printing error: \n" + e.getMessage());
            System.exit(1);
        }
        catch (InvalidKeyException e)
        {
            System.out.println("ERROR: Check that your key was entered correctly. Printing error: \n" + e.getMessage());
            System.exit(1);
        }
        catch (Exception e)
        {
            System.out.println("ERROR: Unknown exception. Check hostname/port and public key. Printing error: \n" + e.getMessage());
            System.exit(1);
        }
    }
}
