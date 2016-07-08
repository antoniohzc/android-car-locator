package hzc.antonio.carlocator.locationslist.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hzc.antonio.carlocator.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationsListFragment extends Fragment {


    public LocationsListFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locations_list, container, false);
    }

}
