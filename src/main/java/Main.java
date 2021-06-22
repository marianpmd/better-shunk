import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class Main {
    public static void main(String[] args)  {
        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(new OAuth2Credential("twitch","twitch token here"))
                .build();

        twitchClient.getChat().joinChannel("CreativeMonkeyz");

        twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class , (event)->{
            if (SystemTray.isSupported()){
                try {
                    displayTray();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private static void displayTray() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().createImage("src/main/resources/3.png");
        TrayIcon trayIcon = new TrayIcon(image, "Twitch Channel Live");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Twitch notification from Java App");
        tray.add(trayIcon);

        trayIcon.displayMessage("Twitch", "CreativeMonkeyz e live!", TrayIcon.MessageType.INFO);

        trayIcon.addActionListener(e -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.twitch.tv/creativemonkeyz?twitch5=0"));
                } catch (IOException | URISyntaxException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
