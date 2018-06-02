// Generated code from Butter Knife. Do not modify!
package com.bonvoyage.admin;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddcarActivity_ViewBinding implements Unbinder {
  private AddcarActivity target;

  @UiThread
  public AddcarActivity_ViewBinding(AddcarActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddcarActivity_ViewBinding(AddcarActivity target, View source) {
    this.target = target;

    target._destinationText = Utils.findRequiredViewAsType(source, R.id.destination, "field '_destinationText'", EditText.class);
    target._carText = Utils.findRequiredViewAsType(source, R.id.car_name, "field '_carText'", EditText.class);
    target._driverText = Utils.findRequiredViewAsType(source, R.id.driver_name, "field '_driverText'", EditText.class);
    target._mobileText = Utils.findRequiredViewAsType(source, R.id.driver_number, "field '_mobileText'", EditText.class);
    target._numberText = Utils.findRequiredViewAsType(source, R.id.car_number, "field '_numberText'", EditText.class);
    target._orgText = Utils.findRequiredViewAsType(source, R.id.org_name, "field '_orgText'", EditText.class);
    target._addcarButton = Utils.findRequiredViewAsType(source, R.id.btn_addcar, "field '_addcarButton'", Button.class);
    target._loginLink = Utils.findRequiredViewAsType(source, R.id.link_login, "field '_loginLink'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AddcarActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._destinationText = null;
    target._carText = null;
    target._driverText = null;
    target._mobileText = null;
    target._numberText = null;
    target._orgText = null;
    target._addcarButton = null;
    target._loginLink = null;
  }
}
