package client;

import ui.EscapeSequences;

public class ConsoleOutput {

    public static void helpPreLogin(){
        String[] commands = {
                "register <USERNAME> <PASSWORD> <EMAIL>",
                "login <USERNAME> <PASSWORD>",
                "quit",
                "help"
        };
        String[] descriptions = {
                " - creates an account",
                " - logs in to an existing account",
                " - exits the program",
                " - displays this menu"
        };

        for(int i = 0; i < commands.length; i++) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print(commands[i]);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.println(descriptions[i]);
        }
    }

    public static void helpPostLogin(){
        String[] commands = {
                "create <NAME>",
                "list",
                "join <ID> [WHITE|BLACK]",
                "observe <ID>",
                "logout",
                "quit",
                "help"
        };
        String[] descriptions = {
                " - creates a new game",
                " - lists existing games",
                " - joins a game as a player",
                " - joins a game as an observer",
                " - logs out",
                " - exits the program",
                " - displays this menu"
        };

        for(int i = 0; i < commands.length; i++) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print(commands[i]);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.println(descriptions[i]);
        }
    }

    public static void printPrompt(Repl.State state){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("[" + state.toString() + "] >>> ");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public static void invalidCommand(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.println("Error: Invalid command. Run help to see a list of commands.");
    }

    public static void printError(String msg){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.println(msg);
    }

    public static void printRegister(String username){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.println("Account " + username + " successfully registered!");
    }

    public static void printLogin(String username){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.println("Login successful. Welcome, " + username);
    }

    public static void printArgumentError(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.println("Error: Invalid arguments. Run help to see command usage.");
    }

    public static void printLogout(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.println("Logged out successfully.");
    }

}
