package client;

import java.io.BufferedReader;
import java.io.IOException;

public class MessageListener implements Runnable {

    private BufferedReader serverInputStream;

    public MessageListener(BufferedReader inputStream) {
        this.serverInputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            String serverMessage;
            while ((serverMessage = serverInputStream.readLine()) != null) {
                System.out.println(serverMessage);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
