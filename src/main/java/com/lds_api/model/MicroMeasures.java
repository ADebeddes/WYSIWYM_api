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
    
}
