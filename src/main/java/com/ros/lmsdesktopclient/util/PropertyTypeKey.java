package com.ros.lmsdesktopclient.util;

import com.ros.lmsdesktopclient.util.enums.PropertyType;
import dagger.MapKey;

@MapKey
public @interface PropertyTypeKey {
    PropertyType value();
}
