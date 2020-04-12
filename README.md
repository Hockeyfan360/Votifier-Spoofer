# Votifier Spoofer

## What is this?
This program takes a compromised Votifier public key and sends forged votes to a Minecraft server, allowing users to potentially earn unlimited rewards for doing nothing beneficial.

## How does this even work?
Votifier uses public key cryptography to receive votes from voting sites. The intended use is that players vote to increase a server's ranking on server listings, and the server rewards them. To achieve this, Votifier generates a 2048 bit RSA keypair. Server owners then post their server on voting websites, and they provide these voting websites with the Votifier public key. When a player votes, the voting site encrypts the vote data with the Votifier public key and sends it back to the server (so that the player can be rewarded).

A typical vote, before being encrypted, might look like this:

    VOTE\nMyVotingSite\nVoterUsername\nVoterIPAddress\nTimestamp\n

Here's the kicker: Votifier has no way to verify that votes sent are from a legitimate voting site. If the public key is compromised, anyone with access to it can send an unlimited number of spoofed votes to the server. 

Best practice for this type of scenario would have been for voting sites to ALSO have a keypair used to sign votes. In this case, Votifier would have prompted server owners for the voting sites' public keys. Armed with the new public keys, Votifier could then verify that all votes received are cryptographically signed and from legitimate sites.

## Okay, but how can I use this?
Firstly, I don't condone using this program with malicious intent. It was meant to demonstrate that Votifier is inherently vulnerable because of its poor cryptography structure. Secondly, this program only works if you have access to the server's public key. For anyone wishing to test this program, though, you can download the jar from the releases page and run it with the command:

    java -jar VotifierSpoofer.jar

## License
Votifier Spoofer is licensed under the GNU General Public License v3. A copy of it is included with this program.
