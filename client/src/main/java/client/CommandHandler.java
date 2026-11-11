package client;

import java.util.Arrays;

public class CommandHandler {

    private ServerFacade serverFacade;

    public CommandHandler(String serverUrl){
        serverFacade = new ServerFacade(serverUrl);
    }

    public Repl.State handlePreLogin(String commandInput){
        String[] splitCommand = split(commandInput);
        switch (splitCommand[0]){
            case "help":
                ConsoleOutput.helpPreLogin();
                return Repl.State.SIGNED_OUT;
            case "register":
                return register(Arrays.copyOfRange(splitCommand, 1, splitCommand.length));
            case "login":
                return Repl.State.SIGNED_IN;
            case "quit":
                return Repl.State.QUIT;
            default:
                return Repl.State.SIGNED_OUT;
        }
    }

    public Repl.State handlePostLogin(String commandInput){
        return Repl.State.QUIT;
    }

    private String[] split(String input){
        return input.split("\\s+");
    }

    private Repl.State register(String[] args){
        if(args.length != 3){
            ConsoleOutput.printArgumentError();
            return Repl.State.SIGNED_OUT;
        }
        try{
            String username = serverFacade.register(args[0], args[1], args[2]);
            ConsoleOutput.printRegister(username);
            ConsoleOutput.printLogin(username);
            return Repl.State.SIGNED_IN;
        } catch(Exception e){
            ConsoleOutput.printError(e.getMessage());
            return Repl.State.SIGNED_OUT;
        }
    }
}
