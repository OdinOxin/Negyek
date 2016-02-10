package de.odinoxin.negyek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OutputTab extends NegyekTab
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_output_tab, container, false);

        return  rootView;
    }

    @Override
    public int getTabNameResourceId()
    {
        return R.string.output;
    }
}
