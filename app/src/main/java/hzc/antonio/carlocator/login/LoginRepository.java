package hzc.antonio.carlocator.login;

public interface LoginRepository {
    void signIn(String email, String password);
    void signUp(String email, String password);
}
