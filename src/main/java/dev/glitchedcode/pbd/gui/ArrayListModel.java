package dev.glitchedcode.pbd.gui;

import javax.annotation.Nonnull;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayListModel<E> implements ListModel<E> {

    private final List<E> list;

    public ArrayListModel() {
        this.list = new ArrayList<>();
    }

    public ArrayListModel(@Nonnull Collection<E> collection) {
        if (collection instanceof List) {
            this.list = (List<E>) collection;
            return;
        }
        this.list = new ArrayList<>();
        list.addAll(collection);
    }

    public void add(E e) {
        list.add(e);
    }

    public void remove(E e) {
        list.remove(e);
    }

    public void remove(int index) {
        list.remove(index);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public E getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        // ignored
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        // ignored
    }
}