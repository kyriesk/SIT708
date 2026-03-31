package com.example.quizapp.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
        initializeQuestions();
    }

    private void initializeQuestions() {
        // Sample quiz questions
        questions.add(new Question(
                "What is the capital of France?",
                new String[]{"London", "Berlin", "Paris", "Madrid"},
                2
        ));

        questions.add(new Question(
                "What is 2 + 2?",
                new String[]{"3", "4", "5", "6"},
                1
        ));

        questions.add(new Question(
                "Which planet is closest to the sun?",
                new String[]{"Venus", "Mercury", "Earth", "Mars"},
                1
        ));

        questions.add(new Question(
                "What is the largest ocean on Earth?",
                new String[]{"Atlantic", "Indian", "Arctic", "Pacific"},
                3
        ));

        questions.add(new Question(
                "In what year did World War II end?",
                new String[]{"1943", "1944", "1945", "1946"},
                2
        ));
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public Question getQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        }
        return null;
    }
}

