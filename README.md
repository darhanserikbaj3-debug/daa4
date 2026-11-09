# Assignment 4. Patient Zero

Student ID: 254430
Name: Serikbay Darkhan
Group: SE-2301

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

```
Mutations: 78
Shared core: 916
Marker hits: 7
First hit: 4943
Border length: 4
Shared words: 347
Tests needed: 42
Cheapest testing: 166
Longest chain: 32
```

My outbreak report:

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?
> The cell `d[i][j]` represents the minimum edit distance between prefixes `A[0..i-1]` and `B[0..j-1]`, computed as the minimum cost of replacing (`d[i-1][j-1] + cost`), deleting (`d[i-1][j] + 1`), or inserting (`d[i][j-1] + 1`) a character.

Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?
> Longest common subsequence allows non-contiguous matching characters with gaps, making the Shared core larger than exact positional matches because insertions and deletions shift matching letters to different indices while preserving sequence order.

Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?
> The prefix function `pi[i]` stores the length of the longest proper prefix of `P[0..i]` that is also a suffix, allowing KMP to skip ahead without backtracking the text pointer, with Border length 4 indicating the length of the longest matching prefix-suffix overlap at the end of the marker.

Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?
> Comparing characters rules out false alarms caused by hash collisions, preventing Rabin-Karp's time complexity from degrading from $O(n + m)$ to $O(n \cdot k)$ when many distinct windows produce identical hash values.

Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?
> The DP tracks testing costs for person $u$ as `take[u]` ($cost[u] + \sum \min(take[v], skip[v])$) and `skip[u]` ($\sum take[v]$), outperforming a greedy high-degree node strategy which fails when testing a central node forces testing surrounding high-cost individuals instead of cheaper optimal vertex covers.

## AI use

The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.

> **AI Tool**: Gemini (Google AI)
>
> **Usage**: Used to clarify Dynamic Programming state transitions (Tree DP & MCM recurrence), verify prefix function boundary conditions, review KMP pointer logic, and structure the analytical explanations in the README report.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 | 23.6 ms |
| naive search | 40 000 | 55.9 ms |
| naive search | 80 000 | 116.3 ms |
| KMP | 20 000 | 1.1 ms |
| KMP | 40 000 | 0.9 ms |
| KMP | 80 000 | 0.3 ms |
| edit distance | 500 | 3.5 ms |
| edit distance | 1000 | 3.3 ms |
| edit distance | 2000 | 12.3 ms |
| edit distance | 4000 | 28.4 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.

> The timing results demonstrate the operational performance scaling of each algorithm. KMP scales linearly ($O(n)$); doubling the text size roughly doubles the execution time because the text pointer never backtracks. Conversely, Naive Search scales quadratically on repetitive inputs due to frequent full re-checks, and Edit Distance scales quadratically ($O(n \cdot m)$), quadrupling execution time whenever the input sequence length is doubled.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(n * m) | O(n * m) | Full DP table constructed for strain lengths n and m. |
| LCS | O(n * m) | O(n * m) | Full DP table constructed for strain lengths n and m. |
| Prefix function and KMP | O(n + m) | O(m) | Text pointer never backtracks; O(m) space for LPS array. |
| Rabin-Karp (shared words) | O(n + m) expected | O(m) | O(1) rolling hash transition; worst-case O(n * k) under collisions. |
| Tree DP | O(v) | O(v) | Tree rooted at node 0; traversed in BFS and reverse post-order. |

## Before you submit

* The four files in `data` and the screenshot `output/outbreak.png` are in the repository.

* `java tools/Check.java` shows all examples right.

* This README is filled in, the program output and the section AI use included.

* The work is pushed to main (at least one commit per mission) and the repository is public.

* The link is attached in Moodle.