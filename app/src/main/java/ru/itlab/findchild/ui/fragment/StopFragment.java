package ru.itlab.findchild.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.itlab.findchild.R;

/**
 * @author Павел Аннин, 08.07.2015.
 */
public class StopFragment extends Fragment {

    public static final String TAG = "StopFragment";

    private static final String ARG_USER_KEY = "userKey";

    private String mUserKey;

    private AppCompatTextView mUserCodeView;
    private AppCompatButton mStopButton;

    private OnStopService mOnStopService;

    public static StopFragment getInstance(String userKey) {
        StopFragment fragment = new StopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_USER_KEY, userKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnStopService = (OnStopService) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnStopService");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_USER_KEY)) {
                mUserKey = getArguments().getString(ARG_USER_KEY);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserCodeView = (AppCompatTextView) view.findViewById(R.id.txt_userCode);
        mStopButton = (AppCompatButton) view.findViewById(R.id.btn_stop);
        if (mStopButton != null) {
            mStopButton.setOnClickListener(mOnClickStopListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUserCodeView != null && !mUserKey.isEmpty()) {
            mUserCodeView.setText(getString(R.string.user_cod_format, mUserKey));
        }
    }

    private View.OnClickListener mOnClickStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnStopService != null) {
                mOnStopService.onStopService();
            }
        }
    };

    public interface OnStopService {
        void onStopService();
    }
}
