package me.lime.lightCore.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for string manipulation
 */
public final class StringUtil {

    private StringUtil() {}

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORX]");
    private static final Pattern ALT_COLOR_PATTERN = Pattern.compile("(?i)&([0-9A-FK-ORX])");
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern MULTISPACE = Pattern.compile("\\s+");
    private static final Pattern NON_DIGITS = Pattern.compile("\\D+");
    private static final SecureRandom RANDOM = new SecureRandom();

    public static boolean isNullOrEmpty(@Nullable String input) {
        return input == null || input.trim().isEmpty();
    }

    @NotNull
    public static String defaultIfNull(@Nullable String input, @NotNull String def) {
        return input == null ? def : input;
    }

    @NotNull
    public static String capitalize(@NotNull String input) {
        return input.isEmpty()
                ? input
                : input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1);
    }

    @NotNull
    public static String capitalizeFully(@NotNull String input) {
        if (input.isEmpty()) return input;

        String[] words = input.toLowerCase(Locale.ROOT).split(" ");
        StringBuilder sb = new StringBuilder(input.length());

        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(capitalize(word)).append(' ');
            }
        }

        return sb.toString().trim();
    }

    @NotNull
    public static String reverse(@NotNull String input) {
        return new StringBuilder(input).reverse().toString();
    }

    public static boolean equalsIgnoreCase(@Nullable String a, @Nullable String b) {
        return Objects.equals(
                a == null ? null : a.toLowerCase(Locale.ROOT),
                b == null ? null : b.toLowerCase(Locale.ROOT)
        );
    }

    @NotNull
    public static String join(@NotNull Collection<String> parts, @NotNull String delimiter) {
        return String.join(delimiter, parts);
    }

    @NotNull
    public static List<String> split(@NotNull String input) {
        return Arrays.stream(MULTISPACE.split(input.trim()))
                .filter(s -> !s.isEmpty())
                .toList();
    }

    @NotNull
    public static String[] splitByLength(@NotNull String input, int length) {
        if (length <= 0) return new String[]{input};

        int chunks = (int) Math.ceil((double) input.length() / length);
        String[] result = new String[chunks];

        for (int i = 0; i < chunks; i++) {
            int start = i * length;
            int end = Math.min(input.length(), (i + 1) * length);
            result[i] = input.substring(start, end);
        }
        return result;
    }

    @NotNull
    public static String parsePlaceholders(@NotNull String input, String... args) {
        if (input.isEmpty()) return input;
        String result = input;

        for (int i = 0; i + 1 < args.length; i += 2) {
            result = result.replace(args[i], args[i + 1]);
        }

        return result;
    }

    @NotNull
    public static String parsePlaceholders(@NotNull String input, @NotNull Map<String, String> map) {
        String result = input;

        for (Map.Entry<String, String> e : map.entrySet()) {
            result = result.replace(e.getKey(), e.getValue());
        }

        return result;
    }

    @NotNull
    public static String stripColor(@NotNull String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static int countColors(@NotNull String input) {
        Matcher matcher = STRIP_COLOR_PATTERN.matcher(input);
        int count = 0;

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    @NotNull
    public static String slugify(@NotNull String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String noAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return NON_ALPHANUMERIC.matcher(noAccents.toLowerCase(Locale.ROOT))
                .replaceAll("-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }

    @NotNull
    public static String repeat(@NotNull String input, int count) {
        return count <= 0 ? "" : input.repeat(count);
    }

    @NotNull
    public static String truncate(@NotNull String input, int maxLength, @NotNull String suffix) {
        return input.length() <= maxLength
                ? input
                : input.substring(0, Math.max(0, maxLength - suffix.length())) + suffix;
    }

    public static boolean isNumeric(@NotNull String input) {
        return NON_DIGITS.matcher(input).replaceAll("").equals(input);
    }

    @NotNull
    public static String padLeft(@NotNull String input, int length, char padChar) {
        return input.length() >= length
                ? input
                : String.valueOf(padChar).repeat(length - input.length()) + input;
    }

    @NotNull
    public static String padRight(@NotNull String input, int length, char padChar) {
        return input.length() >= length
                ? input
                : input + String.valueOf(padChar).repeat(length - input.length());
    }

    @NotNull
    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }

        return sb.toString();
    }

    @NotNull
    public static String pickRandom(@NotNull List<String> list) {
        return list.isEmpty() ? "" : list.get(RANDOM.nextInt(list.size()));
    }

    @NotNull
    public static String shuffle(@NotNull String input) {
        List<Character> chars = new ArrayList<>();
        for (char c : input.toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);

        StringBuilder sb = new StringBuilder(chars.size());
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static boolean containsIgnoreCase(@NotNull String text, @NotNull String query) {
        return text.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
    }

    public static boolean startsWithIgnoreCase(@NotNull String text, @NotNull String prefix) {
        return text.toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT));
    }

    public static boolean endsWithIgnoreCase(@NotNull String text, @NotNull String suffix) {
        return text.toLowerCase(Locale.ROOT).endsWith(suffix.toLowerCase(Locale.ROOT));
    }

    public static int levenshtein(@NotNull String a, @NotNull String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1)
                );
            }
        }
        return dp[a.length()][b.length()];
    }

    public static double similarity(@NotNull String a, @NotNull String b) {
        if (a.isEmpty() && b.isEmpty()) return 1.0;
        int max = Math.max(a.length(), b.length());
        int distance = levenshtein(a, b);
        return 1.0 - ((double) distance / max);
    }
}
