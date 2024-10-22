package dev.dskarpets;

import java.util.*;

class QueenState {
    int[] queens;
    int depth;
    int heuristic;

    QueenState(int[] queens, int depth) {
        this.queens = Arrays.copyOf(queens, queens.length);
        this.depth = depth;
        this.heuristic = calculateHeuristic();
    }

    private int calculateHeuristic() {
        int attacks = 0;
        for (int i = 0; i < queens.length; i++) {
            for (int j = i + 1; j < queens.length; j++) {
                if (queens[i] == queens[j] || Math.abs(queens[i] - queens[j]) == Math.abs(i - j)) {
                    attacks++;
                }
            }
        }
        return attacks;
    }

    boolean isGoal() {
        return heuristic == 0;
    }

}

class EightQueens {

    static final int N = 8;
    static int iterations = 0;
    static int generatedStates = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Виберіть алгоритм: \n1 - BFS\n 2 - RBFS");
        int choice = scanner.nextInt();

        int[] initial = generateInitialBoard();
        System.out.println("Початкова дошка:");
        printBoard(initial);

        if (choice == 1) {
            bfsSearch(initial);
        } else if (choice == 2) {
            rbfsSearch(initial);
        } else {
            System.out.println("Невірний вибір.");
        }
    }

    static int[] generateInitialBoard() {
        int[] initial = new int[N];
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            initial[i] = rand.nextInt(N);
        }
        return initial;
    }

    static void bfsSearch(int[] initial) {
        Queue<QueenState> queue = new LinkedList<>();
        queue.add(new QueenState(initial, 0));
        generatedStates++;

        while (!queue.isEmpty()) {
            QueenState current = queue.poll();
            iterations++;

            if (current.isGoal()) {
                System.out.println("Результуюча дошка:");
                printBoard(current.queens);
                System.out.println("К-ть ітерацій: " + iterations);
                System.out.println("К-ть згенерованих станів: " + generatedStates);
                return;
            }

            for (int col = 0; col < N; col++) {
                int row = current.depth;
                if (isSafe(current.queens, row, col)) {
                    int[] newState = Arrays.copyOf(current.queens, N);
                    newState[row] = col;
                    queue.add(new QueenState(newState, row + 1)); // Глибина збільшується на 1
                    generatedStates++;
                }
            }
        }

        System.out.println("Рішення не знайдено.");
    }

        static void rbfsSearch(int[] initial) {
        iterations = 0; // Скидаємо лічильник ітерацій
        generatedStates = 0; // Скидаємо лічильник згенерованих станів

        boolean result = rbfs(new QueenState(initial, 0), Integer.MAX_VALUE);

        if (result) {
            System.out.println("Рішення знайдено.");
        } else {
            System.out.println("Рішення не знайдено.");
        }

        System.out.println("К-ть ітерацій: " + iterations);
        System.out.println("К-ть згенерованих станів: " + generatedStates);
    }

    static boolean rbfs(QueenState node, int limit) {
        if (node.isGoal()) {
            System.out.println("Результуюча дошка:");
            printBoard(node.queens);
            return true;
        }

        List<QueenState> successors = new ArrayList<>();
        for (int col = 0; col < N; col++) {
            int row = node.depth;
            if (isSafe(node.queens, row, col)) {
                int[] newState = Arrays.copyOf(node.queens, N);
                newState[row] = col;
                successors.add(new QueenState(newState, row + 1));
                generatedStates++;
            }
        }

        if (successors.isEmpty()) {
            return false;
        }

        successors.sort(Comparator.comparingInt(a -> a.heuristic));

        QueenState best = successors.get(0);

        while (true) {
            iterations++;

            if (best.isGoal()) {
                System.out.println("Результуюча дошка:");
                printBoard(best.queens);
                return true;
            }

            int alternative = (successors.size() > 1) ? successors.get(1).heuristic : Integer.MAX_VALUE;

            if (best.heuristic >= limit) {
                return false;
            }

            int newLimit = Math.min(limit, alternative);
            boolean result = rbfs(best, newLimit);

            if (result) {
                return true;
            }

            successors.remove(0);
            if (successors.isEmpty()) {
                return false;
            }

            best = successors.get(0);
        }
    }

    static boolean isSafe(int[] queens, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (queens[i] == col || Math.abs(queens[i] - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }

    static void printBoard(int[] queens) {
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (queens[row] == col) {
                    System.out.print(" Q ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
    }
}
