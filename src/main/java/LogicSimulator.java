import java.io.*;
import java.util.Vector;

public class LogicSimulator {
    private Vector<Device> circuits;
    private Vector<Device> iPins;
    private Vector<Device> oPins;

    public boolean load(String path) throws IOException {
        boolean isLoad = false;
        dd();
        File file = new File(path);
        if (file.canRead()) {
            double[][] fileContent = getFileContent(file);
            int[] searchOutput = new int[circuits.size()];
            for (int i = 0; i < circuits.size(); i++) {
                searchOutput[i] = 1;
            }
            connectCircuits(fileContent, searchOutput);
            for (int i = 0; i < circuits.size(); i++) {
                if (searchOutput[i] == 1) {
                    oPins.add(circuits.get(i));
                }
            }
            isLoad = true;
        }
        return isLoad;
    }

    public String getSimulationResult(Vector<Boolean> booleans) {
        StringBuilder simulationResult = new StringBuilder("Simulation Result:\n" + getTableTopString());
        for (int i = 0; i < iPins.size(); i++) {
            simulationResult.append(booleans.get(i) ? 1 : 0).append(" ");
            iPins.get(i).setInput(booleans.get(i));
        }
        simulationResult.append("| ").append(oPins.get(0).getOutput() ? 1 : 0).append("\n");
        return simulationResult.toString();
    }

    public String getTruthTable() {
        StringBuilder truthTable = new StringBuilder("Truth table:\n" + getTableTopString());
        for (int i = 0; i < (1 << iPins.size()); i++) {
            int k = 0;
            for (int j = (1 << (iPins.size() - 1)); j > 0; j /= 2) {
                int bit = ((i & j) > 0) ? 1 : 0;
                iPins.get(k++).setInput(bit > 0);
                truthTable.append(bit).append(" ");
            }
            truthTable.append("|");
            for (Device oPin : oPins) {
                truthTable.append(" ").append(oPin.getOutput() ? 1 : 0);
            }
            truthTable.append("\n");
        }
        return truthTable.toString();
    }

    public int getCircuitsSize() {
        return circuits.size();
    }
    
    public int getInputPinsSize() {
        return iPins.size();
    }

    public int getOutputPinsSize() {
        return oPins.size();
    }

    private void dd(){
        circuits = new Vector<>();
        iPins = new Vector<>();
        oPins = new Vector<>();
    }

    private String getTableTopString() {
        StringBuilder tableTopString = new StringBuilder();
        for (int i = 0; i < iPins.size(); i++) {
            tableTopString.append("i ");
        }
        tableTopString.append("|");
        for (int i = 0; i < oPins.size(); i++) {
            tableTopString.append(" o");
        }
        tableTopString.append("\n");
        for (int i = 1; i <= iPins.size(); i++) {
            tableTopString.append(i).append(" ");
        }
        tableTopString.append("|");
        for (int i = 1; i <= oPins.size(); i++) {
            tableTopString.append(" ").append(i);
        }
        tableTopString.append("\n");
        for (int i = 0; i < iPins.size(); i++) {
            tableTopString.append("--");
        }
        tableTopString.append("+");
        for (int i = 0; i < oPins.size(); i++) {
            tableTopString.append("--");
        }
        tableTopString.append("\n");
        return tableTopString.toString();
    }

    private double[][] getFileContent(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        int iPinsNumber = Integer.parseInt(line);
        for (int i = 0; i < iPinsNumber; i++) {
            iPins.add(new IPin());
        }

        line = br.readLine();
        int gatesNumber = Integer.parseInt(line);
        double[][] doubles = new double[gatesNumber][1024];
        for (int i = 0; i < gatesNumber; i++) {
            line = br.readLine();
            String[] tokens = line.split(" ");
            boolean isFirst = true;
            int j = 0;
            for (String token : tokens) {
                if (isFirst) {
                    int gateType = Integer.parseInt(token);
                    accordingToGateTypeToJoinCircuits(gateType);
                    isFirst = false;
                } else {
                    double value = Double.parseDouble(token);
                    doubles[i][j++] = value;
                    if (value == 0) break;
                }
            }
        }
        return doubles;
    }

    private void connectCircuits(double[][] doubles, int[] searchOutput) {
        for (int i = 0; i < circuits.size(); i++) {
            int j = 0;
            while (true) {
                double value = doubles[i][j++];
                if (value == 0.0) {
                    break;
                } else if (value < 0.0) {
                    int index = (int) Math.abs(value) - 1;
                    circuits.get(i).addInputPin(iPins.get(index));
                } else {
                    int index = (int) Math.floor(value) - 1;
                    circuits.get(i).addInputPin(circuits.get(index));
                    searchOutput[index] = 0;
                }
            }
        }
    }

    private void accordingToGateTypeToJoinCircuits(int gateType) {
        if (gateType == 1) circuits.add(new GateAND());
        else if (gateType == 2) circuits.add(new GateOR());
        else if (gateType == 3) circuits.add(new GateNOT());
    }
}