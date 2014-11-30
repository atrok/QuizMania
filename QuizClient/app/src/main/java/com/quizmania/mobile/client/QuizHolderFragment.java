package com.quizmania.mobile.client;

/**
 * Created by atrok on 11/29/2014.
 */

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quizmania.client.Game;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizHolderFragment extends Fragment {
    OnSelectedListener mListener;
    OnRatingChangeListener mRatingListener;

    private Game g;

    public static QuizHolderFragment newInstance(Game g) {
        QuizHolderFragment f = new QuizHolderFragment();
        f.g=g;
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putStringArray("radioBtnValues", new String[]{g.getVar1(), g.getVar2(), g.getVar3(), g.getVar4()});
        f.setArguments(args);

        return f;
    }

    public String[] getAttachedValues() {
        return getArguments().getStringArray("radioBtnValues");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        ((RadioButton)rootView.findViewById(R.id.radioVar1)).setText(getAttachedValues()[0]);
        ((RadioButton)rootView.findViewById(R.id.radioVar2)).setText(getAttachedValues()[1]);
        ((RadioButton)rootView.findViewById(R.id.radioVar3)).setText(getAttachedValues()[2]);
        ((RadioButton)rootView.findViewById(R.id.radioVar4)).setText(getAttachedValues()[3]);

        return rootView;
    }

public void onActivityCreated(Bundle savedInstanceState){
    super.onActivityCreated(savedInstanceState);

    RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
    {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // checkedId is the RadioButton selected
            for(int i = 0; i < group.getChildCount(); i++)
                ((RadioButton)group.getChildAt(i)).setEnabled(false);

            onRadioButtonClicked(getActivity().findViewById(checkedId));
        }
    });


    RatingBar ratingBar=(RatingBar) getActivity().findViewById(R.id.ratingBar);
    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
    {
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser){
            if(fromUser){
                mRatingListener.onRatingChange(rating);
            }
        }
                                           }
    );

}
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        int id=view.getId();

        // let Activity know of right answer
        if (g.getAnswer()!=id) {
            view.setBackgroundColor(Color.RED);

            mListener.onSelected(false);
        }
        else {
            view.setBackgroundColor(Color.GREEN);
            mListener.onSelected(true);
        }

        RadioGroup rg=(RadioGroup)getActivity().findViewById(R.id.radioGroup);

        // place correct answer

        TextView text = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        text.setText(g.getDescription());
    }

    public interface OnSelectedListener {
        public void onSelected(boolean success);
    }


    public interface OnRatingChangeListener {
        public void onRatingChange(float rating);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mListener = (OnSelectedListener) activity;
            mRatingListener=(OnRatingChangeListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSelectedListener");
        }
    }

}

