/*
Класс main.

--------------------------------------------------------------------- 1
ОПИСАНИЕ ВАРИАНТА.
Вариант 3. Реализация структуры данных “Граф” и алгоритмов работы с ним.
(1) Реализовать класс Graph<V>, поддерживающий:
- void addVertex(V v) — добавление вершины;
- void addEdge(V from, V to, int weight) — добавление ребра
- void removeVertex(V v) — удаление вершины;
- void removeEdge(V from, V to) — удаление ребра;
- List<V> getAdjacent(V v) — список смежных вершин.
- void dfs(V start) — обход в глубину;
- void bfs(V start) — обход в ширину;
(2) Не использовать готовые коллекции.
(3) Поддерживать ориентированные и неориентированные графы.
(4) Все коллекции д.б. параметризованы.
Дедлайн 25.11.25
--------------------------------------------------------------------- 2
ОПИСАНИЕ КЛАССОВ.
1) Main: менеджер работы с графами, использование меню.
2) TypeOutMenu: схема меню + вывод нужного меню.
3) GraphManager: хранение, управление, создание графов.
4) Graph: граф + методы графа (ориент.+неориент.).
5) Edge: вспомогательный тип "Ребро".
---------------------------------------------------------------------
 */

import java.util.InputMismatchException;
import java.util.Scanner;
//import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        GraphManager<String> manager = new GraphManager<>();

        while (true) {
            try {
                TypeOutMenu.printMainMenu(); // Главное меню: (PRINT)
                int choice = in.nextInt();
                in.nextLine();

                if (choice == 1) { // 1. Создать граф. (PRINT)
                    while (true) {
                        try {
                            TypeOutMenu.printMakeGraph();
                            int choice2 = in.nextInt();
                            in.nextLine();
                            if (choice2 == 1) { // 1.1. Ориентированный.
                                manager.createGraph(true);
                            } else if (choice2 == 2) { // 1.2. Неориентированный.
                                manager.createGraph(false);
                            } else if (choice2 == 3) { // 1.3. Выйти в главное меню.
                                break;
                            } else {
                                System.out.println("Ошибка: такой опции не существует. Попробуйте снова.");
                                continue;
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Ошибка: вы ввели не число. Попробуйте снова.");
                            in.nextLine();
                        }
                    }
                }

                else if (choice == 2) { // 2. Управлять графом.
                    MetkaForBreak:
                    while (true) {
                        try {
                            System.out.print("Введите id графа: ");
                            int graphI = in.nextInt();
                            in.nextLine();
                            Graph<String> graph = manager.getGraphById(graphI);
                            if (graph == null) {
                                System.out.println("Ошибка: граф с таким id не найден.");
                                break; // если было нажато по ошибке - и id ещё пока нет / забыли.
                            }
                            while (true) {
                                try {
                                    TypeOutMenu.printManageGraph(); // 2.1. Что бы вы хотели сделать? (PRINT)
                                    int action = in.nextInt();
                                    in.nextLine();

                                    if (action == 1) { // 2.1.1. Добавить вершину.
                                        try {
                                            System.out.print("Введите имя новой вершины: ");
                                            String v = in.nextLine();
                                            v = v.strip();
                                            graph.addVertex(v);
                                            //in.nextLine();
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Ошибка: " + e.getMessage());
                                        }
                                    }
                                    else if (action == 2) { // 2.1.2. Добавить ребро.
                                        System.out.print("От вершины: ");
                                        String from = in.nextLine();
                                        from = from.strip();
                                        System.out.print("К вершине: ");
                                        String to = in.nextLine();
                                        to = to.strip();
                                        System.out.print("Вес ребра: ");
                                        int weight = in.nextInt();
                                        //in.nextLine();
                                        try {
                                            graph.addEdge(from, to, weight);
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Ошибка: " + e.getMessage());
                                        } catch (InputMismatchException e) {
                                            System.out.println("Ошибка: вы ввели не число. Попробуйте снова.");
                                            in.nextLine();
                                        }
                                    }
                                    else if (action == 3) { // 2.1.3. Удалить вершину.
                                        System.out.print("Введите вершину: ");
                                        String v = in.nextLine();
                                        v = v.strip();
                                        //in.nextLine();
                                        try {
                                            graph.removeVertex(v);
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Ошибка: " + e.getMessage());
                                        } catch (InputMismatchException e) {
                                            System.out.println("Ошибка: вы ввели не число. Попробуйте снова.");
                                            in.nextLine();
                                        }
                                    }
                                    else if (action == 4) { // 2.1.4. Удалить ребро.
                                        System.out.print("От вершины: ");
                                        String from = in.nextLine();
                                        from = from.strip();
                                        System.out.print("К вершине: ");
                                        String to = in.nextLine();
                                        to = to.strip();
                                        //in.nextLine();
                                        try {
                                            graph.removeEdge(from, to);
                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Ошибка: " + e.getMessage());
                                        }  catch (InputMismatchException e) {
                                            System.out.println("Ошибка: вы ввели не число. Попробуйте снова.");
                                            in.nextLine();
                                        }
                                    }
                                    else if (action == 5) { // 2.1.5. Вывести список смежных вершин.
                                        if (graph.getVertexCount() != 0) {
                                            System.out.print("Введите вершину: ");
                                            String v = in.nextLine();
                                            v = v.strip();
                                            Edge<String>[] edges = graph.getAdjacentEdges(v);
                                            //in.nextLine();
                                            if (edges.length == 0) {
                                                System.out.println("Смежных вершин нет.");
                                            } else {
                                                System.out.print("Смежные вершины: ");
                                                for (Edge<String> i : edges) {
                                                    System.out.print(i.to + "(" + i.weight + ") ");
                                                }
                                                System.out.println();
                                            }
                                        } else {
                                            System.out.println("Ошибка: граф пуст.");
                                        }
                                    }
                                    else if (action == 6) { // 2.1.6. Обход в глубину.
                                        if (graph.getVertexCount() != 0) {
                                            System.out.print("Начальная вершина: ");
                                            String start = in.nextLine();
                                            start = start.strip();
                                            //in.nextLine();
                                            graph.DFS(start);
                                        } else {
                                            System.out.println("Ошибка: граф пуст.");
                                        }
                                    }
                                    else if (action == 7) { // 2.1.7. Обход в ширину.
                                        if (graph.getVertexCount() != 0) {
                                            System.out.print("Начальная вершина: ");
                                            String start = in.nextLine();
                                            start = start.strip();
                                            //in.nextLine();
                                            graph.BFS(start);
                                        } else {
                                            System.out.println("Ошибка: граф пуст.");
                                        }
                                    }
                                    else if (action == 8) { // 2.1.8. Удалить граф.
                                        manager.removeGraphById(graphI);
                                        break MetkaForBreak; // сразу выход в главное меню
                                    }
                                    else if (action == 9) { // 2.1.9. Выйти в главное меню.
                                        break MetkaForBreak;
                                    }
                                    else {
                                        System.out.println("Ошибка: такой опции не существует. Попробуйте снова.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Ошибка: вы ввели не число. Попробуйте снова.");
                                    in.nextLine();
                                }
                            }
                        } catch(InputMismatchException e){
                            System.out.println("Ошибка: вы ввели не число. Попробуйте снова.");
                            in.nextLine();
                        }
                    }
                }

                else if (choice == 3) { // 3. Вывести список всех графов.
                    manager.listGraphs();
                }

                else if (choice == 4) { // 4. Выйти из программы.
                    System.out.println("Спасибо за работу. Выход из программы.");
                    break;
                }

                else {
                    System.out.println("Ошибка: такой опции не существует. Попробуйте снова.");
                }

            }
            catch (InputMismatchException e) {
                System.out.println("Ошибка: вы ввели не число. Попробуйте снова.");
                in.nextLine();
            }
            catch (Exception e) {
                System.out.println("Ошибка: " + (e.getMessage() != null ? e.getMessage() : "неизвестная ошибка") + ". Попробуйте снова.");
            }
        }
        in.close();
    }
}
