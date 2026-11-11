package client;

import java.util.Scanner;

public class Repl {

    private State state;
    private Scanner scanner;
    private CommandHandler handler;

    public enum State{
        QUIT,
        SIGNED_OUT,
        SIGNED_IN,
    }

    public Repl(String serverUrl){
        scanner = new Scanner(System.in);
        handler = new CommandHandler(serverUrl);
        this.state = State.SIGNED_OUT;
    }

    public void run(){
        System.out.println("Welcome to Chess. Type help to get started.");
        while (!state.equals(State.QUIT)){
            ConsoleOutput.printPrompt(state);
            String input = scanner.nextLine();
            if(state == State.SIGNED_OUT){
                state = handler.handlePreLogin(input);
            }
            else if(state == State.SIGNED_IN){
                state = handler.handlePostLogin(input);
            }
        }
        System.exit(0);
    }

}
