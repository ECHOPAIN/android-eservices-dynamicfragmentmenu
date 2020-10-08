package android.eservices.dynamicfragmentmenu;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    public static final String COUNTER_STATE_KEY = "CounterState";
    private View rootView;
    private NavigationInterface navigationInterface;
    private Integer currentCounter;
    private Button addButton;
    private Button removeButton;
    private TextView counterTextView;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavigationInterface) {
            this.navigationInterface = (NavigationInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("On activity created " + this);
        setupButtons();


        //if available, restore the state of the current counter
        if (savedInstanceState != null) {
            currentCounter = savedInstanceState.getInt("FAVORITES_COUNTER_KEY");
        }else{
            //if there is no value to restore, set the counter to default value 4
            currentCounter = 4;
        }
        //finally call refreshCounter to update the display
        refreshCounter();

        MainActivity main = (MainActivity) getActivity();
        if(!main.getFragmentCache().containsKey(R.id.favorites)){
            main.getFragmentCache().put(R.id.favorites,this);
        }

    }

    private void setupButtons() {
        addButton = rootView.findViewById(R.id.add_button);
        removeButton = rootView.findViewById(R.id.remove_button);
        counterTextView = rootView.findViewById(R.id.counter_textview);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCounter(true);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCounter(false);
            }
        });
    }

    private void updateCounter(boolean increment) {
        if ((currentCounter > 0) || (currentCounter == 0 && increment)) {
            currentCounter = increment ? currentCounter + 1 : currentCounter - 1;
            refreshCounter();
        }
    }

    private void refreshCounter() {
        counterTextView.setText(Integer.toString(currentCounter));
        removeButton.setEnabled(currentCounter != 0);
        navigationInterface.updateFavoriteCounter(currentCounter);
    }


    //save the state of the counter i.e. the current counter number
    //in order to restore it later
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("FAVORITES_COUNTER_KEY", currentCounter);
    }
}
