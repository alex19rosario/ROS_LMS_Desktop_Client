package com.ros.lmsdesktopclient.util;

import dagger.MapKey;

@MapKey
public @interface CommandTypeKey {
    CommandType value();
}
