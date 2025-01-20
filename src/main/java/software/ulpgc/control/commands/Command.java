package software.ulpgc.control.commands;


public interface Command {
    void execute();


    interface Input {
        interface API extends Input{
            String token();
        }
    }



}
