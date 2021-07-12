package com.lds_api.model;

public enum MicroMeasures {
	levenshtein ,
	normalizedLevenshtein ,
	damerauLevenshtein ,
	optimalStringAligment ,
	jaroWinkler ,
	longestCommonSubsequence ,
	metricLongestCommonSubsequence ,
	nGram ,
	qGram ,
	cosineSimilarity ,
	jaccardIndex ,
	sorensenDiceCoefficient ,
	ratcliffObershelp ,
	numeric ,
    list ;
    
    public static String getPath(MicroMeasures measure){
        if(measure == levenshtein){
            return "Levenshtein" ;
        }
        if(measure == normalizedLevenshtein){
            return "Normalized Levenshtein" ;
        }
        if(measure == damerauLevenshtein){
            return "Damerau-Levenshtein" ;
        }
        if(measure == optimalStringAligment){
            return "Optimal String Alignment" ;
        }
        if(measure == jaroWinkler){
            return "Jaro-Winkler" ;
        }
        if(measure == longestCommonSubsequence){
            return "Longest Common Subsequence" ;
        }
        if(measure == metricLongestCommonSubsequence){
            return "Metric Longest Common Subsequence" ;
        }
        if(measure == nGram){
            return "N-Gram" ;
        }
        if(measure == qGram){
            return "Q-Gram" ;
        }
        if(measure == cosineSimilarity){
            return "Cosine similarity" ;
        }
        if(measure == jaccardIndex){
            return "Jaccard index" ;
        }
        if(measure == sorensenDiceCoefficient){
            return "Sorensen-Dice coefficient" ;
        }
        if(measure == ratcliffObershelp){
            return "Ratcliff-Obershelp" ;
        }
        if(measure == numeric){
            return "Numeric" ;
        }        
        if(measure == list){
            return "List" ;
        }
        
        return null;
    }
    
    public static String getName(MicroMeasures measure){
        return measure.toString();
    }
    
    public static String getDescription(MicroMeasures measure){
        if(measure == levenshtein){
            return "The Levenshtein distance between two words is the minimum number of single-character edits (insertions, deletions or substitutions) required to change one word into the other." ;
        }
        if(measure == normalizedLevenshtein){
            return "This distance is computed as levenshtein distance divided by the length of the longest string." ;
        }
        if(measure == damerauLevenshtein){
            return "Damerau-Levenshtein distance with transposition is the minimum number of operations needed to transform one string into the other, where an operation is defined as an insertion, deletion, or substitution of a single character, or a transposition of two adjacent characters." ;
        }
        if(measure == optimalStringAligment){
            return "Computes the number of edit operations needed to make the strings equal under the condition that no substring is edited more than once, whereas the true Damerau–Levenshtein presents no such restriction." ;
        }
        if(measure == jaroWinkler){
            return "The Jaro–Winkler distance metric is designed and best suited for short strings such as person names, and to detect typos." ;
        }
        if(measure == longestCommonSubsequence){
            return "Consists in finding the longest subsequence common to two sequences. It differs from problems of finding common substrings: unlike substrings, subsequences are not required to occupy consecutive positions within the original sequences." ;
        }
        if(measure == metricLongestCommonSubsequence){
            return "Distance metric based on Longest Common Subsequence." ;
        }
        if(measure == nGram){
            return "The algorithm uses affixing with special character \'\\n\' to increase the weight of first characters. The normalization is achieved by dividing the total similarity score the original length of the longest word." ;
        }
        if(measure == qGram){
            return "Approximate string-matching with q-grams and maximal matches" ;
        }
        if(measure == cosineSimilarity){
            return "The similarity between the two strings is the cosine of the angle between these two vectors representation." ;
        }
        if(measure == jaccardIndex){
            return "Metric distance. Like Q-Gram distance, the input strings are first converted into sets of n-grams, but this time the cardinality of each n-gram is not taken into account. Each input string is simply a set of n-grams." ;
        }
        if(measure == sorensenDiceCoefficient){
            return "Similar to Jaccard index." ;
        }
        if(measure == ratcliffObershelp){
            return "Ratcliff-Obershelp Pattern Recognition is a string-matching algorithm for determining the similarity of two strings." ;
        }
        if(measure == numeric){
            return "Compare two numbers and get the difference in percentage." ;
        }        
        if(measure == list){
            return "Compare list of concept" ;
        }
        
        return null;
    }
}
