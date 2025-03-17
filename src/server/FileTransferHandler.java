package server;

import java.io.*;
import java.net.Socket;

public class FileTransferHandler implements Runnable {

    private Socket clientSocket;

    public FileTransferHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream())) {

            String command = dataInputStream.readUTF();
            if (!"FILE_TRANSFER".equals(command)) {
                System.out.println("Invalid file transfer command.");
                return;
            }

            String fileName = dataInputStream.readUTF();
            long fileSize = dataInputStream.readLong();

            if (!isValidFileType(fileName)) {
                System.out.println("Rejected file: " + fileName + " (Invalid file type)");
                dataOutputStream.writeUTF("ERROR: Invalid file type.");
                return;
            }

            File receivedFile = new File("received_files/" + fileName);
            receivedFile.getParentFile().mkdirs();  // Ensure directory exists

            try (FileOutputStream fileOutputStream = new FileOutputStream(receivedFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                long remaining = fileSize;

                while ((bytesRead = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    remaining -= bytesRead;
                }

                System.out.println("File received successfully: " + fileName);
                dataOutputStream.writeUTF("SUCCESS: File received successfully.");

            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
                dataOutputStream.writeUTF("ERROR: Failed to write file.");
            }

        } catch (IOException e) {
            System.out.println("Error receiving file: " + e.getMessage());
        }
    }

    private static boolean isValidFileType(String fileName) {
        return fileName.endsWith(".docx") || fileName.endsWith(".pdf") || fileName.endsWith(".jpeg");
    }
}

