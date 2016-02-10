package de.odinoxin.negyek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public class InputTab extends NegyekTab
{
    private View rootView;
    private HorizontalNumberPicker year_hnp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(this.rootView == null)
        {
            this.rootView = inflater.inflate(R.layout.fragment_input_tab, container, false);

            this.year_hnp = (HorizontalNumberPicker) this.rootView.findViewById(R.id.input_year_hnp);
            this.year_hnp.min = 1970;
            this.year_hnp.max = Calendar.getInstance().get(Calendar.YEAR) + 10;
            this.year_hnp.value = Calendar.getInstance().get(Calendar.YEAR);
            this.year_hnp.updateControls();
        }
        return  rootView;
    }

    @Override
    public int getTabNameResourceId()
    {
        return R.string.input;
    }
}
