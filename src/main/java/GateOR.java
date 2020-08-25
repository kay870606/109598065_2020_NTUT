public class GateOR extends Device {
    @Override
    public boolean getOutput() {
        boolean output = false;
        for (int i = 0; i < iPins.size(); i++) {
            if (iPins.get(i).getOutput() == true)
                output = true;
        }
        return output;
    }
}
