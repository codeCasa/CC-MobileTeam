package com.codingwithcasa.matchthecard.data;

import java.util.ArrayList;
import java.util.Collections;

public class GameModel {

    private static GameModel gameModel = null;

    private GameModel() {
    }

    public static GameModel getInstance() {
        if (gameModel == null) {
            gameModel = new GameModel();
        }
        return gameModel;
    }

    public static final int EMPTY = 0;
    public static final int MATCH1 = 1;
    public static final int MATCH2 = 2;
    public static final int MATCH3 = 3;
    public static final int UNTOUCHED = 4;
    public static final int TOUCHED = 5;


    private int[][] model = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };


    private int[][] cover = {
            {UNTOUCHED, UNTOUCHED, UNTOUCHED},
            {UNTOUCHED, UNTOUCHED, UNTOUCHED},
            {UNTOUCHED, UNTOUCHED, UNTOUCHED}
    };


    public void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                model[i][j] = EMPTY;
            }
        }
    }

    public int getFieldContent(int x, int y) {
        return model[x][y];
    }


    public int getCoverContent(int x, int y) {

        return cover[x][y];
    }

    public int setCoverContent(int x, int y, int content) {
        return (cover[x][y] = content);
    }

    public ArrayList<Integer> shuffleCards() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            list.add(MATCH1);
            list.add(MATCH2);
            list.add(MATCH3);
        }
        Collections.shuffle(list);
        return list;
    }

    public void setCards() {
        ArrayList cards = shuffleCards();
        int field = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                model[i][j] = (int) cards.get(field);
                field += 1;
            }
        }

    }


}
