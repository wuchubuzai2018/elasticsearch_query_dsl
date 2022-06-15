package com.wuchubuzai.dsl;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class Regex {
    public static final int UNICODE_CHARACTER_CLASS = 256;

    public Regex() {
    }

    public static boolean isSimpleMatchPattern(String str) {
        return str.indexOf(42) != -1;
    }

    public static boolean isMatchAllPattern(String str) {
        return str.equals("*");
    }

    public static boolean simpleMatch(String pattern, String str) {
        if (pattern != null && str != null) {
            int firstIndex = pattern.indexOf(42);
            if (firstIndex == -1) {
                return pattern.equals(str);
            } else if (firstIndex == 0) {
                if (pattern.length() == 1) {
                    return true;
                } else {
                    int nextIndex = pattern.indexOf(42, firstIndex + 1);
                    if (nextIndex == -1) {
                        return str.endsWith(pattern.substring(1));
                    } else if (nextIndex == 1) {
                        return simpleMatch(pattern.substring(1), str);
                    } else {
                        String part = pattern.substring(1, nextIndex);

                        for(int partIndex = str.indexOf(part); partIndex != -1; partIndex = str.indexOf(part, partIndex + 1)) {
                            if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                                return true;
                            }
                        }

                        return false;
                    }
                }
            } else {
                return str.length() >= firstIndex && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) && simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex));
            }
        } else {
            return false;
        }
    }

    public static boolean simpleMatch(String[] patterns, String str) {
        if (patterns != null) {
            String[] var2 = patterns;
            int var3 = patterns.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String pattern = var2[var4];
                if (simpleMatch(pattern, str)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean simpleMatch(List<String> patterns, String str) {
        return patterns != null && simpleMatch((String[])patterns.toArray(Strings.EMPTY_ARRAY), str);
    }

    public static boolean simpleMatch(String[] patterns, String[] types) {
        if (patterns != null && types != null) {
            String[] var2 = types;
            int var3 = types.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String type = var2[var4];
                String[] var6 = patterns;
                int var7 = patterns.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String pattern = var6[var8];
                    if (simpleMatch(pattern, type)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static Pattern compile(String regex, String flags) {
        int pFlags = flags == null ? 0 : flagsFromString(flags);
        return Pattern.compile(regex, pFlags);
    }

    public static int flagsFromString(String flags) {
        int pFlags = 0;
        String[] var2 = Strings.delimitedListToStringArray(flags, "|");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            if (!s.isEmpty()) {
                s = s.toUpperCase(Locale.ROOT);
                if ("CASE_INSENSITIVE".equals(s)) {
                    pFlags |= 2;
                } else if ("MULTILINE".equals(s)) {
                    pFlags |= 8;
                } else if ("DOTALL".equals(s)) {
                    pFlags |= 32;
                } else if ("UNICODE_CASE".equals(s)) {
                    pFlags |= 64;
                } else if ("CANON_EQ".equals(s)) {
                    pFlags |= 128;
                } else if ("UNIX_LINES".equals(s)) {
                    pFlags |= 1;
                } else if ("LITERAL".equals(s)) {
                    pFlags |= 16;
                } else if ("COMMENTS".equals(s)) {
                    pFlags |= 4;
                } else {
                    if (!"UNICODE_CHAR_CLASS".equals(s) && !"UNICODE_CHARACTER_CLASS".equals(s)) {
                        throw new IllegalArgumentException("Unknown regex flag [" + s + "]");
                    }

                    pFlags |= 256;
                }
            }
        }

        return pFlags;
    }

    public static String flagsToString(int flags) {
        StringBuilder sb = new StringBuilder();
        if ((flags & 2) != 0) {
            sb.append("CASE_INSENSITIVE|");
        }

        if ((flags & 8) != 0) {
            sb.append("MULTILINE|");
        }

        if ((flags & 32) != 0) {
            sb.append("DOTALL|");
        }

        if ((flags & 64) != 0) {
            sb.append("UNICODE_CASE|");
        }

        if ((flags & 128) != 0) {
            sb.append("CANON_EQ|");
        }

        if ((flags & 1) != 0) {
            sb.append("UNIX_LINES|");
        }

        if ((flags & 16) != 0) {
            sb.append("LITERAL|");
        }

        if ((flags & 4) != 0) {
            sb.append("COMMENTS|");
        }

        if ((flags & 256) != 0) {
            sb.append("UNICODE_CHAR_CLASS|");
        }

        return sb.toString();
    }
}
