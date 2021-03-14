package com.T05.krowdtrialz.model.interfaces;

import java.util.List;

/**
 * Implementing classes maintain a set of tag strings
 */
public interface Tagged {
    /**
     * Note: Firebase cannot serialize a Set so this must return a List.
     * @return A list of unique tags.
     */
    public List<String> getTags();
}
