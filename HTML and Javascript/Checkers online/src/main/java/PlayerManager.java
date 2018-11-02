import java.util.UUID;

public class PlayerManager {
    public static synchronized String getNewID() {
        return UUID.randomUUID().toString();
    }
}
