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
                return login(Arrays.copyOfRange(splitCommand, 1, splitCommand.length));
            case "quit":
                return Repl.State.QUIT;
            default:
                ConsoleOutput.invalidCommand();
                return Repl.State.SIGNED_OUT;
        }
    }

    public Repl.State handlePostLogin(String commandInput){
        String[] splitCommand = split(commandInput);
        switch (splitCommand[0]){
            case "help":
                ConsoleOutput.helpPostLogin();
                return Repl.State.SIGNED_IN;
            case "create":
                return Repl.State.SIGNED_IN;
            case "list":
                return Repl.State.SIGNED_IN;
            case "join":
                return Repl.State.SIGNED_IN;
            case "observe":
                return Repl.State.SIGNED_IN;
            case "logout":
                return logout();
            case "quit":
                return Repl.State.QUIT;
            default:
                ConsoleOutput.invalidCommand();
                return Repl.State.SIGNED_IN;
        }
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

    private Repl.State login(String[] args){
        if(args.length != 2){
            ConsoleOutput.printArgumentError();
            return Repl.State.SIGNED_OUT;
        }
        try{
            String username = serverFacade.login(args[0], args[1]);
            ConsoleOutput.printLogin(username);
            return Repl.State.SIGNED_IN;
        } catch (Exception e) {
            ConsoleOutput.printError(e.getMessage());
            return Repl.State.SIGNED_OUT;
        }
    }

    private Repl.State logout(){
        try{
            serverFacade.logout();
            ConsoleOutput.printLogout();
            return Repl.State.SIGNED_OUT;
        } catch (Exception e) {
            ConsoleOutput.printError(e.getMessage());
            return Repl.State.SIGNED_IN;
        }
    }
}
