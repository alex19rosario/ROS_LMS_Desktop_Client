package com.ros.lmsdesktopclient.util.annotations;

import com.ros.lmsdesktopclient.util.enums.CommandType;
import dagger.MapKey;

@MapKey
public @interface CommandTypeKey {
    CommandType value();
}
