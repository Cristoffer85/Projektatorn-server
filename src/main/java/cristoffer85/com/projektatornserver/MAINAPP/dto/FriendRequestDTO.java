package cristoffer85.com.projektatornserver.MAINAPP.dto;

public class FriendRequestDTO {
    private String requestId;
    private String username;

    public FriendRequestDTO(String requestId, String username) {
        this.requestId = requestId;
        this.username = username;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUsername() {
        return username;
    }
}
