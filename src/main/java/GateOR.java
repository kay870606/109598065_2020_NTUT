public class GateOR extends Device {
    @Override
    public boolean getOutput() {
        boolean output = false;
        for (Device iPin : iPins) {
            if (iPin.getOutput())
                output = true;
        }
        return output;
    }
}
