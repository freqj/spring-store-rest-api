package dev.alexa.store.security;

import javax.validation.constraints.Min;

public class View {

    public static interface MessageView{
        public static interface MinimalView{}

        public static interface FullInfo extends MinimalView {}
    }

    public static interface ItemView{
        public static interface MinimalView{}
        public static interface FullInfo extends MinimalView{}
    }
    public static interface RoleView{
        public static interface MinimalView {}
    }
    public static interface ResponseView{
        public static interface MinimalView{}
        public static interface FullInfo extends MinimalView{}
    }
    public static interface UserView
    {
        public static interface MinimalView{}
        public static interface FullInfo extends MinimalView{}
    }
}
