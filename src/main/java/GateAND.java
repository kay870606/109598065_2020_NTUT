public class GateAND extends Device {
    @Override
    public boolean getOutput() {
        boolean output = true;
        for (Device iPin : iPins) {
            if (!iPin.getOutput())
                output = false;
        }
        return output;
    }
}
