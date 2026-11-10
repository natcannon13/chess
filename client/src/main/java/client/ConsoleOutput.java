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
                " - Creates an account",
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
                "register <USERNAME> <PASSWORD> <EMAIL>",
                "login <USERNAME> <PASSWORD>",
                "quit",
                "help"
        };
        String[] descriptions = {
                " - Creates an account",
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

    public static void printPrompt(Repl.State state){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("[" + state.toString() + "] >>> ");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

}
