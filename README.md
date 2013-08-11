strong-filter-coffee
====================

An attempt at solving the problem "Perplexing Puzzle" at http://bloomreach.com/puzzles/.
====================

Finding synonyms for words or phrases is an integral part of search retrieval. Not all users use the same terms for searching and understanding user intent is critical for finding relevant content. There are several online resources for finding synonyms for common words in English. However, finding synonyms for phrases used in specific domains such as commercial products can be tricky.

In this challenge, we keep things simpler, and try to build for a program that can consider a candidate pair of words or phrases and flag it as a synonym. For the purpose of this exercise, we’ll focus on shopping queries, and consider two candidate phrases to be synonyms if a typical shopper would use the two candidates interchangeably. For example, “ac adapter” is a synonym for “power adapter” but is not a synonym for “power tools.”

Input
A two-column, comma-separated file of candidate synonym pairs:
https://s3.amazonaws.com/br-user/puzzles/candidate_synonyms.txt

Output
A three column comma separated file with each line corresponding to the input file and third column is one of “true” or “false” denoting whether the candidate is a synonym or not respectively.
