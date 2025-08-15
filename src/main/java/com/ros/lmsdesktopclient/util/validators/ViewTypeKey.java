package com.ros.lmsdesktopclient.util.validators;

import com.ros.lmsdesktopclient.util.enums.ViewType;
import dagger.MapKey;

@MapKey
public @interface ViewTypeKey {
    ViewType value();
}
