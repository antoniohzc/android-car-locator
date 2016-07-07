package hzc.antonio.carlocator.login;

public class LoginInteractorImpl implements LoginInteractor {
    private LoginRepository repository;

    public LoginInteractorImpl(LoginRepository repository) {
        this.repository = repository;
    }

    @Override
    public void doSignIn(String email, String password) {
        repository.signIn(email, password);
    }

    @Override
    public void doSignUp(String email, String password) {
        repository.signUp(email, password);
    }
}
