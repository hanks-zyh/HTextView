package com.hanks.htextview.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CharacterUtils
 * Created by hanks on 15-12-14.
 */
public class CharacterUtils {

    public static List<CharacterDiffResult> diff(CharSequence oldText, CharSequence newText) {

        List<CharacterDiffResult> differentList = new ArrayList<>();
        Set<Integer> skip = new HashSet<>();

        for (int i = 0; i < oldText.length(); i++) {
            char c = oldText.charAt(i);
            for (int j = 0; j < newText.length(); j++) {
                if (!skip.contains(j) && c == newText.charAt(j)) {
                    skip.add(j);
                    CharacterDiffResult different = new CharacterDiffResult();
                    different.c = c;
                    different.fromIndex = i;
                    different.moveIndex = j;
                    differentList.add(different);
                    break;
                }
            }
        }
        return differentList;
    }

    public static int needMove(int index, List<CharacterDiffResult> differentList) {
        for (CharacterDiffResult different : differentList) {
            if (different.fromIndex == index) {
                return different.moveIndex;
            }
        }
        return -1;
    }

    public static boolean stayHere(int index, List<CharacterDiffResult> differentList) {
        for (CharacterDiffResult different : differentList) {
            if (different.moveIndex == index) {
                return true;
            }
        }
        return false;
    }


    public static float getOffset(int from, int move, float progress, float startX, float oldStartX,
                                  List<Float> gaps, List<Float> oldGaps) {

        float dist = startX;
        for (int i = 0; i < move; i++) {
            dist += gaps.get(i);
        }

        float cur = oldStartX;
        for (int i = 0; i < from; i++) {
            cur += oldGaps.get(i);
        }

        return cur + (dist - cur) * progress;

    }

}
