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

            double[][] vector = getDoubleVectorFromFileContent(file);
            int[] searchOutput = new int[circuits.size()];
            for(int i = 0;i< circuits.size();i++){
                searchOutput[i] = 1;
            }

            for (int i = 0; i < circuits.size(); i++) {
                int j = 0;
                while (true) {
                    double value = vector[i][j++];
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

            for (int i = 0; i < circuits.size(); i++) {
                if (searchOutput[i] == 1) {
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
            simulationResult += (booleans.get(i)== true ? 1:0) + " ";
            iPins.get(i).setInput(booleans.get(i));
        }
        simulationResult += "| " + (oPins.get(0).getOutput()== true ? 1: 0) + "\n";
        return simulationResult;
    }

    public String getTruthTable()
    {
        String truthTable = "Truth table:\n" + getTableTopString();
        for (int i = 0; i < (1 << iPins.size()); i++) {
            int k = 0;
            for (int j = (1 << (iPins.size() - 1)); j > 0; j /= 2) {
                int bit = ((i & j) > 0) ? 1 : 0;
                iPins.get(k++).setInput(bit > 0  ? true : false);
                truthTable += bit + " ";
            }
            truthTable += "| " + (oPins.get(0).getOutput()== true ? 1: 0) + "\n";
        }
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

    private double[][] getDoubleVectorFromFileContent(File file) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        int iPinsNumber = Integer.parseInt(line);
        for (int i = 0; i < iPinsNumber; i++) {
            iPins.add(new IPin());
        }

        line = br.readLine();
        int gatesNumber = Integer.parseInt(line);

        double[][] vectors = new double[gatesNumber][1024];

        for (int i = 0; i < gatesNumber; i++) {
            line = br.readLine();
            String[] tokens = line.split(" ");
            boolean isFirst = true;
            int j = 0;
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
                    vectors[i][j++] = value;
                    if (value == 0)break;
                }
            }
        }
        return vectors;
    }
}