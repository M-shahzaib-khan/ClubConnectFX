package app.clubconnectfx;

import BL.SystemAdminBL;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SystemAdminBroadcastController {

    @FXML private TextArea messageArea;

    private final SystemAdminBL controller = new SystemAdminBL();

    @FXML
    private void sendBroadcast() {
        String msg = messageArea.getText().trim();

        if (msg.isEmpty()) {
            alert("Message cannot be empty.");
            return;
        }

        controller.broadcastToAll(msg);
        alert("Broadcast sent successfully!");
        messageArea.clear();
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
