package org.shredzone.repowatch.web.util;

public class Sequencer {
    private int pos = 0;
    private final String[] sequence;
    
    public Sequencer(String... sequence) {
        if (sequence.length == 0)
            throw new IllegalArgumentException("At least one item is required!");

        this.sequence = sequence;
        this.pos = 0;
    }
    
    public String getNext() {
        if (pos >= sequence.length) pos = 0;
        return sequence[pos++];
    }
        
}
