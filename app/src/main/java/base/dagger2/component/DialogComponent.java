package base.dagger2.component;

import com.product.demo.dialog.OneTextBtnChooseDialog;

import base.android.BaseDialog;
import base.dagger2.scope.PerActivity;
import base.module.DialogModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = DialogModule.class)
public interface DialogComponent {
    void inject(BaseDialog dialog);
    void inject(OneTextBtnChooseDialog dialog);
}