package br.com.blecaute.inventory.util;

import lombok.Data;

@Data(staticConstructor = "of")
public class Pair<K, V> {

    private final K key;
    private final V value;

}