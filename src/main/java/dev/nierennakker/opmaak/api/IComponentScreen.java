package dev.nierennakker.opmaak.api;

public interface IComponentScreen {
    default boolean displayComponent(IComponent component) {
        return false;
    }
}
