/*
Класс управления созданными графами.
 */

import java.util.*;

@SuppressWarnings("unchecked")
public class GraphManager<V> {
    private Graph<V>[] graphs;
    private int numberGraphs;

    public GraphManager() {
        graphs = new Graph[10];
        numberGraphs = 0;
    }

    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        Graph<V>[] newGraphs = new Graph[graphs.length * 2];
        System.arraycopy(graphs, 0, newGraphs, 0, numberGraphs);
        graphs = newGraphs;
    }

    public Graph<V> createGraph(boolean isOriented) {
        if (numberGraphs >= graphs.length) {
            expandCapacity();
        }
        Graph<V> g = new Graph<>(isOriented);
        graphs[numberGraphs++] = g;
        System.out.println("Создан граф с id = " + g.getId() + ".");
        return g;
    }

    public void removeGraphById(int id) {
        for (int i = 0; i < numberGraphs; i++) {
            if (graphs[i].getId() == id) {
                graphs[i].destroy();
                System.arraycopy(graphs, i + 1, graphs, i, numberGraphs - i - 1);
                graphs[numberGraphs - 1] = null;
                numberGraphs--;
                return;
            }
        }
        throw new IllegalArgumentException("Граф " + id + " не найден.");
    }

    public void listGraphs() {
        if (numberGraphs == 0) {
            System.out.println("Список графов пуст.");
            return;
        }
        System.out.print("Текущие графы: ");
        for (int i = 0; i < numberGraphs; i++) {
            System.out.print("id = " + graphs[i].getId() + " ");
        }
        System.out.println();
    }

    public Graph<V> getGraphById(int id) {
        for (int i = 0; i < numberGraphs; i++) {
            if (graphs[i].getId() == id)
                return graphs[i];
        }
        return null;
    }
}

