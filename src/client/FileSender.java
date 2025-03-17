package client;

import java.io.*;
import java.net.Socket;

public class FileSender {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int FILE_TRANSFER_PORT = 54321;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java FileSender <file_path>");
            return;
        }

        String filePath = args[0];
        sendFile(filePath);
    }

    public static void sendFile(String filePath) {
        File file = new File(filePath);
        
        if (!file.exists()) {
            System.out.println("Error: The file does not exist.");
            return;
        }

        String fileName = file.getName();
        if (!isValidFileType(fileName)) {
            System.out.println("Error: Invalid file type. Only .docx, .pdf, and .jpeg are allowed.");
            return;
        }

        try (Socket socket = new Socket(SERVER_ADDRESS, 54321);
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             FileInputStream fileInputStream = new FileInputStream(file)) {

            // Comando inicial para el servidor
            dataOutputStream.writeUTF("FILE_TRANSFER");
            dataOutputStream.writeUTF(fileName);
            dataOutputStream.writeLong(file.length());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully: " + fileName);

        } catch (IOException e) {
            System.out.println("Error sending file: " + e.getMessage());
        }
    }

    private static boolean isValidFileType(String fileName) {
        return fileName.endsWith(".docx") || fileName.endsWith(".pdf") || fileName.endsWith(".jpeg");
    }
}
