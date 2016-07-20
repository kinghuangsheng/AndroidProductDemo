package base.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import base.dagger2.component.FragmentComponent;
import base.module.FragmentModule;

import base.util.UIUtil;
import butterknife.ButterKnife;

/**
 * Fragment基类
 * 
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(getLayoutViewID(), null);
        UIUtil.viewScreenAdapter(view);
        ButterKnife.bind(this, view);
        dagger2Inject(App.getInstance().getAppComponent().createFragmentComponent(new FragmentModule()));
        onCreateView(view);
        return view;
    }

    protected abstract void onCreateView(View view);
    public abstract int getLayoutViewID();
    public abstract void dagger2Inject(FragmentComponent fragmentComponent);

}
