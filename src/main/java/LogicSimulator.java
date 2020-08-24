import java.io.*;
import java.util.Vector;

public class LogicSimulator
{
    private Vector<Device> circuits;
    private Vector<Device> iPins;
    private Vector<Device> oPins;

    public LogicSimulator()
    {
        circuits = new Vector<>();
        iPins = new Vector<>();
        oPins = new Vector<>();
    }

    public boolean load(String path) throws IOException
    {
        boolean isLoad = false;

        File file = new File(path);
        if (file.canRead()) {

            Vector<Vector<Double>> vector = getDoubleVectorFromFileContent(file);
            Vector<Integer> searchOutput = new Vector<Integer>(circuits.size(), 1);

            for (int i = 0; i < circuits.size(); i++) {
                int j = 0;
                while (true) {
                    double value = vector.get(i).get(j++);
                    if (value == 0) {
                        break;
                    } else if (value < 0) {
                        int index = (int) Math.abs(value) - 1;
                        circuits.get(i).addInputPin(iPins.get(index));
                    } else {
                        int index = (int) Math.floor(value) - 1;
                        circuits.get(i).addInputPin(circuits.get(index));
                        searchOutput.set(index, 0);
                    }
                }
            }

            for (int i = 0; i < circuits.size(); i++) {
                if (searchOutput.get(i) == 1) {
                    oPins.add(circuits.get(i));
                    break;
                }
            }

            isLoad = true;
        }
        return isLoad;
    }

    public String getSimulationResult(Vector<Boolean> booleans)
    {
        String simulationResult = "Simulation Result:\n" + getTableTopString();
        for (int i = 0; i < iPins.size(); i++) {
            simulationResult += booleans.get(i) + " ";
            iPins.get(i).setInput(booleans.get(i));
        }
        simulationResult += "| " + oPins.get(0).getOutput() + "\n";
        return simulationResult;
    }

    public String getTruthTable()
    {
        String truthTable = "Truth table:\n" + getTableTopString();
        /*for (int i = 0; i < (1 << iPins.size()); i++) {
            int k = 0;
            for (int j = (1 << (iPins.size() - 1)); j > 0; j /= 2) {
                // boolean bit = (i & j ? true : false);
                iPins.get(k++).setInput(bit);
                truthTable += bit + " ";
            }
            truthTable += "| " + oPins.get(0).getOutput() + "\n";
        }*/
        return truthTable;
    }

    private String getTableTopString()
    {
        String tableTopString = "";
        for (int i = 0; i < iPins.size(); i++) {
            tableTopString += "i ";
        }
        tableTopString += "| o\n";
        for (int i = 1; i <= iPins.size(); i++) {
            tableTopString += i + " ";
        }
        tableTopString += "| 1\n";
        for (int i = 0; i < iPins.size(); i++) {
            tableTopString += "--";
        }
        tableTopString += "+--\n";
        return tableTopString;
    }

    private Vector<Vector<Double>> getDoubleVectorFromFileContent(File file) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        int iPinsNumber = Integer.parseInt(line);
        for (int i = 0; i < iPinsNumber; i++) {
            iPins.add(new IPin());
        }

        line = br.readLine();
        int gatesNumber = Integer.parseInt(line);
        Vector<Vector<Double>> vectors = new Vector<Vector<Double>>(gatesNumber);
        for (int i = 0; i < gatesNumber; i++) {
            line = br.readLine();
            String[] tokens = line.split(" ");
            boolean isFirst = true;
            for (String token:tokens) {
                if(isFirst){
                    int gateType = Integer.parseInt(token);
                    if (gateType == 1) circuits.add(new GateAND());
                    else if (gateType == 2) circuits.add(new GateOR());
                    else if (gateType == 3) circuits.add(new GateNOT());
                    isFirst = false;
                }
                else {
                    double value = Double.parseDouble(token);
                    vectors.get(i).add(value);
                    if (value == 0)break;
                }
            }
        }
        return vectors;
    }
}