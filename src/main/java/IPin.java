public class IPin extends Device
{
    boolean inputValue;

    @Override
    public void setInput(boolean value)
    {
        inputValue = value;
    }

    @Override
    public boolean getOutput()
    {
        return inputValue;
    }
}
