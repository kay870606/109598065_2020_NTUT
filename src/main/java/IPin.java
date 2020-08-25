public class IPin extends Device {
    boolean input;

    @Override
    public void setInput(boolean value) {
        input = value;
    }

    @Override
    public boolean getOutput() {
        return input;
    }
}
