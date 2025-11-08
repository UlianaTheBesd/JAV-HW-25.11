/*
Класс графа.
 */

public class Graph<V> {
    private static int counter = 0;
    private int id;
    private V[] vertexMas; // ["A", "B", "C", null, null, ...] вершины.
    private Edge<V>[][] adjacentMas; // [(
    private int vertexCount;
    private boolean isOriented; // true or false.

    @SuppressWarnings("unchecked") // во избежание "Unchecked cast: 'java.lang.Object[]' to 'V[]'".
    public Graph(boolean isOriented) {
        this.isOriented = isOriented;
        this.id = ++counter;
        vertexMas = (V[]) new Object[10]; // по умолчанию capacity = 10.
        adjacentMas = (Edge<V>[][]) new Edge[10][];
        vertexCount = 0;
    }

    @SuppressWarnings("unchecked")
    private void expandCapacityTwofold() {
        int newCapacity = vertexMas.length * 2;
        V[] newVertexMas = (V[]) new Object[newCapacity];
        Edge<V>[][] newAdjacentMas = (Edge<V>[][]) new Edge[newCapacity][];
        System.arraycopy(vertexMas, 0, newVertexMas, 0, vertexCount);
        System.arraycopy(adjacentMas, 0, newAdjacentMas, 0, vertexCount);
        vertexMas = newVertexMas;
        adjacentMas = newAdjacentMas;
    }

    public int getId() {
        return id;
    }

    public static int getCounter() {
        return counter;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    private int getVertexIndex(V v) {
        for (int i = 0; i < vertexCount; i++) {
            if (vertexMas[i] != null && vertexMas[i].equals(v)) // работает и на просто "if (vertexMas[i].equals(v))".
                return i;
        }
        return -1;
    }

    public void addVertex(V v) {
        if (getVertexIndex(v) != -1) {
            throw new IllegalArgumentException("Вершина " + v + " уже существует в графе.");
        }
        if (vertexCount >= vertexMas.length) {
            expandCapacityTwofold();
        }
        vertexMas[vertexCount] = v;
        adjacentMas[vertexCount] = new Edge[0];
        vertexCount++;
    }

    public void removeVertex(V v) {
        int removeIndex = getVertexIndex(v);
        if (removeIndex == -1) {
            //System.out.println("Вершина " + v + " не найдена.");
            //return;
            throw new IllegalArgumentException("Вершина " + v + " не найдена.");
        }
        for (int i = 0; i < vertexCount; i++) {
            if (i != removeIndex) {
                adjacentMas[i] = removeSpecificEdge(adjacentMas[i], v);
            }
        }
        System.arraycopy(vertexMas, removeIndex + 1, vertexMas, removeIndex, vertexCount - removeIndex - 1);
        System.arraycopy(adjacentMas, removeIndex + 1, adjacentMas, removeIndex, vertexCount - removeIndex - 1);
        vertexMas[vertexCount - 1] = null;
        adjacentMas[vertexCount - 1] = null;
        vertexCount--;
        System.out.println("Вершина " + v + " удалена.");
    }

    public void addEdge(V from, V to, int weight) {
        int fromI = getVertexIndex(from);
        int toI = getVertexIndex(to);
        if (fromI == -1) {
            //System.out.println("Вершина " + from + "не найдена.");
            //return;
            throw new IllegalArgumentException("Вершина " + from + " не найдена.");
        }
        if (toI == -1) {
            //System.out.println("Вершина " + to + "не найдена.");
            //return;
            throw new IllegalArgumentException("Вершина " + to + " не найдена.");
        }
        if (isOriented) {
            Edge<V>[] prevEdges = adjacentMas[fromI]; // [(B,1), (C,2)]
            Edge<V>[] newEdges = new Edge[prevEdges.length + 1]; // [null, null, null]
            System.arraycopy(prevEdges, 0, newEdges, 0, prevEdges.length); // [(B,1), (C,2), null]
            newEdges[prevEdges.length] = new Edge<>(to, weight); // [(B,1), (C,2), (to, weight)]
                                        // можно написать и new Edge<V>(...) - будет одно и то же.
            adjacentMas[fromI] = newEdges;
        }
        if (!isOriented) {
            // from -> to.
            Edge<V>[] prevEdgesFrom = adjacentMas[fromI];
            Edge<V>[] newEdgesFrom = new Edge[prevEdgesFrom.length + 1];
            System.arraycopy(prevEdgesFrom, 0, newEdgesFrom, 0, prevEdgesFrom.length);
            newEdgesFrom[prevEdgesFrom.length] = new Edge<>(to, weight);
            adjacentMas[fromI] = newEdgesFrom;
            // to -> from.
            Edge<V>[] prevEdgesTo = adjacentMas[toI];
            Edge<V>[] newEdgesTo = new Edge[prevEdgesTo.length + 1];
            System.arraycopy(prevEdgesTo, 0, newEdgesTo, 0, prevEdgesTo.length);
            newEdgesTo[prevEdgesTo.length] = new Edge<>(from, weight);
            adjacentMas[toI] = newEdgesTo;
        }
    }

    @SuppressWarnings("unchecked")
    private Edge<V>[] removeSpecificEdge(Edge<V>[] edgesMas, V v) {
        int indexToRemove = -1;
        for (int i = 0; i < edgesMas.length; i++) {
            if (edgesMas[i].to.equals(v)) { // edgesMas[i].to = v - (?)
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove == -1) {
            return edgesMas;
        }
        Edge<V>[] newEdges = (Edge<V>[]) new Edge[edgesMas.length - 1];
        System.arraycopy(edgesMas, 0, newEdges, 0, indexToRemove);
        System.arraycopy(edgesMas, indexToRemove + 1, newEdges, indexToRemove, edgesMas.length - indexToRemove - 1);
        return newEdges;
    }

    public void removeEdge(V from, V to) {
        int fromI = getVertexIndex(from);
        int toI = getVertexIndex(to);
        if (fromI == -1) {
            //System.out.println("Вершина " + from + " не найдена.");
            //return;
            throw new IllegalArgumentException("Вершина " + from + " не найдена.");
        }
        if (toI == -1) {
            //System.out.println("Вершина " + to + " не найдена.");
            //return;
            throw new IllegalArgumentException("Вершина " + to + " не найдена.");
        }
        Edge<V>[] oldEdgesFrom = adjacentMas[fromI];
        Edge<V>[] newEdgesFrom = removeSpecificEdge(oldEdgesFrom, to);
        if (newEdgesFrom != oldEdgesFrom) {
            adjacentMas[fromI] = newEdgesFrom;
            System.out.println("Ребро " + from + " - " + to + " удалено.");
        }
        if (!isOriented) {
            Edge<V>[] oldEdgesTo = adjacentMas[toI];
            Edge<V>[] newEdgesTo = removeSpecificEdge(oldEdgesTo, from);
            if (newEdgesTo != oldEdgesTo) {
                adjacentMas[toI] = newEdgesTo;
                System.out.println("Обратное ребро " + to + " - " + from + " удалено.");
            }
        }
    }

    public Edge<V>[] getAdjacentEdges(V v) { // List<V> getAdjacent(V v).
        int index = getVertexIndex(v);
        if (index == -1)
            return new Edge[0];
        return adjacentMas[index];
    }

    private void DFSHelper(V v, boolean[] checked) {
        int i = getVertexIndex(v);
        if ((i == -1)|| (checked[i]))
            return;
        checked[i] = true;
        System.out.print(v + " ");
        for (Edge<V> a : adjacentMas[i]) {
            DFSHelper(a.to, checked);
        }
    }

    public void DFS(V start) {
        boolean[] checked = new boolean[vertexCount]; // по умолчанию все ячейки false.
        DFSHelper(start, checked);
        System.out.println();
    }

    public void BFS(V start) {
        boolean[] checked = new boolean[vertexCount];
        V[] queue = (V[]) new Object[vertexCount]; // "Queue" через массив.
        int head = 0;
        int tail = 0;

        queue[0] = start; // = queue[tail++] = start;
        tail++;
        while (head < tail) {
            V v = queue[head];
            int i = getVertexIndex(v);
            head++;
            if (i == -1 || checked[i])
                continue;
            checked[i] = true;
            System.out.print(v + " ");
            for (Edge<V> a : adjacentMas[i]) {
                int neighI = getVertexIndex(a.to);
                if (!checked[neighI]) {
                    queue[tail] = a.to;
                    tail++;
                }
            }
        }
        System.out.println();
    }

    public void destroy() {
        for (int i = 0; i < vertexCount; i++) {
            vertexMas[i] = null;
            adjacentMas[i] = null;
        }
        vertexMas = null;
        adjacentMas = null;
        vertexCount = 0;
    }
}
