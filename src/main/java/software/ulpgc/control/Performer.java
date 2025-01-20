package software.ulpgc.control;

public interface Performer {
    void perform(Action action);

    @FunctionalInterface
    interface Action {
        void act();
    }
}
