package talha.com.bd.humaclab;

public class LoginResponse {

boolean error;

    public LoginResponse(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }
}
