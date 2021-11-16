package Tests;

public class SignUpContent {

    public String _firstName;
    public String _lastName;
    public String _email;
    public String _password;
    public String _confirmPassword;
    public String _heard;

    public SignUpContent(String firstName, String lastName, String email, String password, String confirmPassword, String heard)
    {
        _firstName=firstName;
        _lastName=lastName;
        _email=email;
        _password=password;
        _confirmPassword=confirmPassword;
        _heard=heard;
    }

    public String get_firstName() {
        return _firstName;
    }

    public String get_lastName() {
        return _lastName;
    }

    public String get_email() {
        return _email;
    }

    public String get_password() {
        return _password;
    }

    public String get_confirmPassword() {
        return _confirmPassword;
    }

    public String get_heard() {
        return _heard;
    }
}
