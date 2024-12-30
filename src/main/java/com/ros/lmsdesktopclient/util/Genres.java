package com.ros.lmsdesktopclient.util;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public enum Genres {
    MYSTERY("MYSTERY"),
    SCIENCE_FICTION("SCIENCE FICTION"),
    ROMANCE("ROMANCE"),
    FANTASY("FANTASY"),
    THRILLER("THRILLER"),
    HISTORICAL_FICTION("HISTORICAL FICTION"),
    HORROR("HORROR"),
    BIOGRAPHY("BIOGRAPHY"),
    SELF_HELP("SELF-HELP"),
    ADVENTURE("ADVENTURE"),
    CHILDREN_LITERATURE("CHILDREN'S LITERATURE"),
    NON_FICTION("NON-FICTION"),
    GRAPHIC_NOVEL("GRAPHIC NOVEL"),
    POETRY("POETRY"),
    DYSTOPIAN("DYSTOPIAN"),
    CRIME("CRIME"),
    AUTOBIOGRAPHY("AUTOBIOGRAPHY"),
    ESSAYS("ESSAYS"),
    PHILOSOPHY("PHILOSOPHY"),
    RELIGION("RELIGION"),
    SCIENCE("SCIENCE"),
    ART("ART"),
    COOKING("COOKING"),
    TRAVEL("TRAVEL"),
    SPORTS("SPORTS"),
    TECHNOLOGY("TECHNOLOGY"),
    DRAMA("DRAMA"),
    POLITICAL_FICTION("POLITICAL FICTION"),
    LANGUAGE("LANGUAGE");

    private final String str;

    Genres(String str){
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
