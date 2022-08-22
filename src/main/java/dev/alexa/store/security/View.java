package dev.alexa.store.security;

public class View {
    public static interface ResponseView{}
    public static interface UserView
    {
        public static class MinimalView{}
        public static class FullInfo extends MinimalView{}
    }
}
