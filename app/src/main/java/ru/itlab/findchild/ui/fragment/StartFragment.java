package ru.itlab.findchild.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.itlab.findchild.R;

/**
 * @author Павел Аннин, 08.07.2015.
 */
public class StartFragment extends Fragment {

    public static final String TAG = "StartFragment";

    private static final String ARG_USER_KEY = "userKey";

    private String mUserKey;

    private AppCompatEditText mCodeEdit;
    private AppCompatButton mStartButton;
    private TextInputLayout mCodeTIL;

    private OnStartService mOnStartService;

    public static StartFragment getInstance(String userKey) {
        StartFragment fragment = new StartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_USER_KEY, userKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnStartService = (OnStartService) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnInputUserKey");
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
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCodeEdit = (AppCompatEditText) view.findViewById(R.id.edt_numberCode);
        mCodeTIL = (TextInputLayout) view.findViewById(R.id.til_numberCode);
        mStartButton = (AppCompatButton) view.findViewById(R.id.btn_start);
        if (mStartButton != null) {
            mStartButton.setOnClickListener(mOnClickOkListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCodeEdit != null && mUserKey != null) {
            mCodeEdit.setText(mUserKey);
        }
    }

    private boolean isValidate(String userKey) {
        boolean validate = true;
        if (userKey == null || userKey.isEmpty()) {
            validate = false;
            if (mCodeTIL != null) {
                mCodeTIL.setError(getString(R.string.err_edt_empty));
            }
        }
        return validate;
    }

    private View.OnClickListener mOnClickOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mCodeEdit != null) {
                String userKey = mCodeEdit.getText().toString();
                if (isValidate(userKey)) {
                    if (mOnStartService != null) {
                        mOnStartService.onStartService(userKey);
                    }
                }
            }
        }
    };

    public interface OnStartService {
        void onStartService(String userKey);
    }

}
