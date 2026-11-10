package client;

public class CommandHandler {

    public Repl.State handlePreLogin(String commandInput){
        String[] splitCommand = split(commandInput);
        switch (splitCommand[0]){
            case "help":
                ConsoleOutput.helpPreLogin();
                return Repl.State.SIGNED_OUT;
            case "register":
                return Repl.State.SIGNED_IN;
            case "login":
                return Repl.State.SIGNED_IN;
            case "quit":
                return Repl.State.QUIT;
        }
        return Repl.State.QUIT;
    }

    public Repl.State handlePostLogin(String commandInput){
        return Repl.State.QUIT;
    }

    private String[] split(String input){
        return input.split("\\s+");
    }

}
