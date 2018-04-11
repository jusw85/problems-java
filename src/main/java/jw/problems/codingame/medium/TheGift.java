package jw.problems.codingame.medium;

import java.util.Arrays;
import java.util.Scanner;

/**
 * https://www.codingame.com/ide/puzzle/the-gift
 *
 * The Goal
 * The Oods want to offer a present to one of them. The thing is, they all have different budgets to invest in this present. Of course, their unique wish is to find the fairest method that will determine the maximum budget that each Ood can afford. The Oods have been discussing this issue for days, and up until now, they have not managed to find a totally satisfactory solution.
 *
 * The Doctor decides to give a helping hand by creating a program. His idea is to check if the Oods have enough money to buy the present, and if so, to calculate how much each Ood should pay, according to their respective budget limit.
 * Rules
 * The Doctor has in hand the list of maximum budgets for each Ood. The Doctor's aim is to share the cost very precisely. To respect the Oods democratic values and to select the best solution, the Doctor decides that:
 *
 * no Ood can pay more than his maximum budget
 * the optimal solution is the one for which the highest contribution is minimal
 * if there are several optimal solutions, then the best solution is the one for which the highest second contribution is minimal, and so on and so forth...
 *
 * Moreover, to facilitate the calculations, the Doctor wants each financial participation to be an integer of the local currency (nobody should give any cents).
 * Examples
 *
 * For example, the Oods wish to buy a gift that cost 100. The first Ood has a budget of 3, the second has a budget of 45 and the third has a budget of 100.
 *
 * In that case:
 * Budget 	Solution
 * 3 	3
 * 45 	45
 * 100 	52
 *
 * Second example: they still wish to buy a gift that costs 100 but the second Ood has a budget of 100 this time.
 *
 * In that case:
 * Budget 	Solution
 * 3 	3
 * 100 	48
 * 100 	49
 * Game Input
 * Input
 *
 * Line 1: the number N of participants
 *
 * Line 2: the price C of the gift
 *
 * N following lines: the list of budgets B of participants.
 * Output
 *
 * If it is possible to buy the present : N lines, each containing the contribution of a participant. The list is sorted in ascending order.
 * Otherwise the word IMPOSSIBLE.
 *
 * Constraints
 * 0 < N ≤ 2000
 * 0 ≤ C ≤ 1000000000
 * 0 ≤ B ≤ 1000000000
 */
public class TheGift {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int totalCost = in.nextInt();

        int[] maxBudget = new int[N];
        for (int i = 0; i < N; i++) {
            int B = in.nextInt();
            maxBudget[i] = B;
        }
        Arrays.sort(maxBudget);

        int idx = 0;
        int remainingCost = totalCost;
        int remainingOods = N;
        int[] budget = new int[N];

        while (idx < budget.length) {
            int cost = remainingCost / remainingOods;
            if (cost < maxBudget[idx]) {
                int remaining = remainingCost % remainingOods;
                for (; idx < budget.length - remaining; idx++) {
                    budget[idx] = cost;
                }
                for (; idx < budget.length; idx++) {
                    budget[idx] = cost + 1;
                }
                remainingCost = 0;
            } else {
                budget[idx] = maxBudget[idx];
                remainingCost -= maxBudget[idx];
                remainingOods--;
                idx++;
            }
        }
        if (remainingCost > 0) {
            System.out.println("IMPOSSIBLE");
        } else {
            for (int i = 0; i < budget.length; i++) {
                System.out.println(budget[i]);
            }
        }
    }

}
