import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class TextUI {
    public void displayMenu() {
        System.out.println("1. Load logic circuit file");
        System.out.println("2. Simulation");
        System.out.println("3. Display truth table");
        System.out.println("4. Exit");
        System.out.println("Command :");
    }

    public void processCommand() {
        try {
            LogicSimulator ls = new LogicSimulator();
            String str;
            int cmd;

            while (true) {
                displayMenu();

                Scanner scanner = new Scanner(System.in);
                str = scanner.next();
                cmd = Integer.parseInt(str);

                if (cmd == 1) {
                    processLoad(ls);
                } else if (cmd == 2) {
                    processSimulation(ls);
                } else if (cmd == 3) {
                    processDisplayTruthTable(ls);
                } else if (cmd == 4) {
                    processExit();
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error.");
        }
    }

    void processLoad(LogicSimulator ls) throws IOException {
        System.out.println("Please key in a file path: ");

        String path;
        Scanner scanner = new Scanner(System.in);
        path = scanner.next();

        if (ls.load(path) == false) {
            System.out.println("File not found or file format error!!");
            System.out.println();
        } else {
            System.out.println("Circuit: " + ls.getIPinsSize() + " input pins, " + ls.getOPinsSize()
                    + " output pins and " + ls.getCircuitSize());
            System.out.println();
        }
    }

    void processSimulation(LogicSimulator ls) {
        if (ls.getIPinsSize() == 0) {
            System.out.println("Please load an lcf file, before using this operation.");
            System.out.println();
        } else {
            Vector<Integer> simulationInput = new Vector<>();

            for (int i = 1; i <= ls.getIPinsSize(); i++) {
                System.out.println("Please key in the value of input pin " + i + ": ");

                String pinStr;
                Scanner scanner = new Scanner(System.in);
                pinStr = scanner.next();
                int pinValue;

                pinValue = Integer.parseInt(pinStr);
                if (pinValue != 0 && pinValue != 1) {
                    System.out.println("The value of input pin must be 0/1");
                    i--;
                    continue;
                } else {
                    simulationInput.add(pinValue);
                }
            }
            System.out.println(ls.getSimulationResult(simulationInput));
        }
    }

    void processDisplayTruthTable(LogicSimulator ls) {
        if (ls.getIPinsSize() == 0) {
            System.out.println("Please load an lcf file, before using this operation.");
            System.out.println();
        } else {
            System.out.println(ls.getTruthTable());
        }
    }

    void processExit() {
        System.out.println("Goodbye, thanks for using LS. ");
    }
}
