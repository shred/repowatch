package org.shredzone.repowatch.repository;

import java.io.Serializable;

public class SearchDTO implements Serializable {
    private static final long serialVersionUID = 1311197118497286370L;
    
    private String term;
    private int page;
    private boolean desc;

    /**
     * Gets the page currently shown in the search result.
     * 
     * @return Page number
     */
    public int getPage()                    { return page; }
    public void setPage(int page)           { this.page = page; }

    /**
     * Gets the search term.
     * 
     * @return Search term
     */
    public String getTerm()                 { return term; }
    public void setTerm(String term)        { this.term = term; }

    /**
     * Checks if the summary and description are to be searched as well.
     * 
     * @return true: also search in summary and description
     */
    public boolean isDescriptions()         { return desc; }
    public void setDescriptions(boolean desc) { this.desc = desc; }
    
}
