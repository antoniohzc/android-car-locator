package hzc.antonio.carlocator.login;

public interface LoginInteractor {
    void doSignIn(String email, String password);
    void doSignUp(String email, String password);
}
