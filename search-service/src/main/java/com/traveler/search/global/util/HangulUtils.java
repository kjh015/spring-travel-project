package com.traveler.search.global.util;

public class HangulUtils {
    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_BASE_UNIT = 588; // 초성 간의 간격
    private static final char[] CHOSUNG = {
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public static String extractChosung(String text) {
        if (text == null || text.isEmpty()) return text;

        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= HANGUL_BEGIN_UNICODE && c <= 55203) { // 한글 범위
                int index = (c - HANGUL_BEGIN_UNICODE) / HANGUL_BASE_UNIT;
                sb.append(CHOSUNG[index]);
            } else {
                sb.append(c); // 한글이 아니면 그대로 유지
            }
        }
        return sb.toString();
    }
}
