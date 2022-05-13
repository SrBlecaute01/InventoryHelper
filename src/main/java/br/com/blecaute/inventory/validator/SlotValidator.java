package br.com.blecaute.inventory.validator;

@FunctionalInterface
public interface SlotValidator {

    boolean validate(int slot);

}