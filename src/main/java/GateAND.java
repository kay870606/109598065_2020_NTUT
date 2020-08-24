public class GateAND extends Device
{
    @Override
    public boolean getOutput()
    {
        boolean output = true;
        for (int i = 0; i < iPins.size(); i++) {
            if(iPins.get(i).getOutput()==false)
                output = false;
        }
        return output;
    }
}
