import java.util.Vector;

public class Device {
    protected Vector<Device> iPins;

    public Device() {
        iPins = new Vector<>();
    }

    public void addInputPin(Device iPin) {
        iPins.add(iPin);
    }

    public void setInput(boolean value) {
        throw new RuntimeException("This method doesn't allow to call.");
    }

    public boolean getOutput() {
        throw new RuntimeException("This method doesn't allow to call.");
    }
}
