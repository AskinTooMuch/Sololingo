package Bean;

import java.io.Serializable;

public class Word implements Serializable {
    private int id;
    private int subjectId;
    private String word;
    private String definition;

    public Word() {
    }

    public Word(int id, int subjectId, String word, String definition) {
        this.id = id;
        this.subjectId = subjectId;
        this.word = word;
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
