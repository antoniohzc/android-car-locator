package hzc.antonio.carlocator.main;

public class SessionInteractorImpl implements SessionInteractor {
    private MainRepository repository;

    public SessionInteractorImpl(MainRepository repository) {
        this.repository = repository;
    }

    @Override
    public void logout() {
        repository.logout();
    }
}
