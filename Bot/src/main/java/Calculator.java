public class Calculator {
    public static String getFields() {
        return null;
    }

    public static void setFields(String data) {
    }

    public static boolean invokeAnswerMessage() {
        return true;
    }

    public static boolean invokeAnswerGroupMessage() {
        return false;
    }

    public static boolean invokeSendMessage() {
        return false;
    }

    public static boolean invokeSendGroupMessage() {
        return false;
    }

    public static boolean invokeSendComment() {
        return false;
    }

    public static boolean invokeSendTweet() {
        return false;
    }

    public static boolean acceptInvite() {
        return false;
    }

    //----------------------------
    private static String remP(String polynomial) {
        StringBuilder realPoly = new StringBuilder();
        boolean hasp = false;
        for (int i = 0; i < polynomial.length() - 1; i++) {
            if (polynomial.charAt(i + 1) == '(') {
                hasp = true;
                if (polynomial.charAt(i) == '+') {
                    int closeP = openFinder(i + 1, polynomial);
                    realPoly.append(polynomial, i + 2, closeP);

                    i = closeP;
                } else if (polynomial.charAt(i) == '-') {
                    int closeP = openFinder(i + 1, polynomial);
                    realPoly.append(minus(polynomial.substring(i + 2, closeP)));

                    i = closeP;
                }
            } else {
                realPoly.append(polynomial.charAt(i));
            }
        }
        if (polynomial.charAt(polynomial.length() - 1) != ')') {
            realPoly.append(polynomial.charAt(polynomial.length() - 1));
        }
        if (hasp) {
            return remP(realPoly.toString());
        } else {
            return realPoly.toString();
        }
    }

    private static String fix(String polynomial) {
        for (int i = 0; i < polynomial.length(); i++) {
            if (polynomial.charAt(i) == '(') {
                if (polynomial.charAt(i + 1) == '+' || polynomial.charAt(i + 1) == '-') {
                    polynomial = polynomial.substring(0, i + 1) + "+0" + polynomial.substring(i + 1);
                    i += 2;
                } else {
                    polynomial = polynomial.substring(0, i + 1) + "+0+" + polynomial.substring(i + 1);
                    i += 3;
                }
            }
        }
        return polynomial;
    }

    private static String minus(String polynomial) {
        int[] help = solve(polynomial);
        for (int i = 0; i < 1001; i++) {
            help[i] = -help[i];
        }
        return toPoly(help);
    }

    private static int coefficient(String a) {
        boolean help = false;
        int index = -1;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == 'x') {
                help = true;
                index = i;
                break;
            }
        }
        if (index > 0) {
            if (a.charAt(index - 1) == '+') {
                return 1;
            }
            if (a.charAt(index - 1) == '-') {
                return -1;
            }
        }
        if (!help) {
            index = a.length();
        }
        String a2 = a.substring(0, index);
        boolean ifPow = false;
        for (int i = 0; i < a2.length(); i++) {
            if (a2.charAt(i) == '^') {
                ifPow = true;
                index = i;
                break;
            }
        }
        if (!ifPow) {
            return Integer.parseInt(a2);
        }
        int first = Integer.parseInt(a2.substring(0, index));
        int second = Integer.parseInt(a2.substring(index + 1));
        if (first >= 0) {
            return (int) Math.pow(first, second);
        } else {
            return (int) -Math.pow(Math.abs(first), second);
        }

    }

    private static int power(String a) {
        boolean help = false;
        int index = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == 'x') {
                help = true;
                index = i;
                break;
            }
        }
        if (index == a.length() - 1) {
            return 1;
        }
        if (help) {
            String ans = a.substring(index + 2);
            return Integer.parseInt(ans);
        } else {
            return 0;
        }
    }

    private static int openFinder(int index, String polynomial) {
        int open = 0;
        int close = 0;
        for (int i = index; i < polynomial.length(); i++) {
            if (polynomial.charAt(i) == '(') {
                open++;
            } else if (polynomial.charAt(i) == ')') {
                close++;
            }
            if (open == close) {
                return i;
            }
        }
        return -1;
    }

    private static int closeFinder(int index, String polynomial) {
        int open = 0;
        int close = 0;
        for (int i = index; i >= 0; i--) {
            if (polynomial.charAt(i) == '(') {
                open++;
            } else if (polynomial.charAt(i) == ')') {
                close++;
            }
            if (open == close) {
                return i;
            }
        }
        return -1;
    }

    private static int[] solve(String polynomial) {
        if (polynomial.isEmpty()) {
            int[] ans = new int[1001];
            for (int i = 0; i < 1001; i++) {
                ans[i] = 0;
            }
            return ans;
        }
        if (polynomial.charAt(0) != '+' && polynomial.charAt(0) != '-') {
            polynomial = '+' + polynomial;
        }
        for (int i = 0; i < polynomial.length(); i++) {
            if (polynomial.charAt(i) == '#') {
                int l = closeFinder(i - 1, polynomial);
                int r = openFinder(i + 1, polynomial);
                String first = polynomial.substring(l + 1, i - 1);
                String second = polynomial.substring(i + 2, r);
                int[] done = hashtag(first, second);
                String mid = toPoly(done);
                mid = '(' + mid + ')';
                String part1 = polynomial.substring(0, l);
                String part2 = polynomial.substring(r + 1);
                polynomial = part1 + mid + part2;
                return solve(polynomial);
            }
        }
        for (int i = 0; i < polynomial.length() - 1; i++) {
            if (polynomial.charAt(i) == ')' && polynomial.charAt(i + 1) == '(') {
                int l = closeFinder(i, polynomial);
                int r = openFinder(i + 1, polynomial);
                int[] multi = new int[1001];
                multi = multiply(polynomial.substring(l + 1, i), polynomial.substring(i + 2, r));
                String multi2 = toPoly(multi);
                multi2 = '(' + multi2 + ')';
                polynomial = polynomial.substring(0, l) + multi2 + polynomial.substring(r + 1);
                return solve(polynomial);
            }
        }
        polynomial = remP(polynomial);
        for (int i = polynomial.length() - 1; i >= 0; i--) {
            if (polynomial.charAt(i) == '-') {
                String part = polynomial.substring(i);
                polynomial = polynomial.substring(0, i);
                int[] ans = solve(polynomial);
                ans[power(part)] += coefficient(part);
                return ans;
            } else if (polynomial.charAt(i) == '+') {
                String part = polynomial.substring(i);
                polynomial = polynomial.substring(0, i);
                int[] ans = solve(polynomial);
                ans[power(part)] += coefficient(part);
                return ans;
            }
        }
        return new int[1001];
    }

    private static String toPoly(int[] poly) {
        StringBuilder ans = new StringBuilder();
        for (int i = poly.length - 1; i >= 2; i--) {
            if (poly[i] > 1) {
                ans.append("+").append(poly[i]).append("x^").append(i);
            } else if (poly[i] == 1) {
                ans.append("+x^").append(i);
            } else if (poly[i] == -1) {
                ans.append("-x^").append(i);
            } else if (poly[i] < 0) {
                ans.append(poly[i]).append("x^").append(i);
            }

        }
        if (poly[1] > 1) {
            ans.append("+").append(poly[1]).append("x");
        } else if (poly[1] == 1) {
            ans.append("+x");
        } else if (poly[1] < -1) {
            ans.append(poly[1]).append("x");
        } else if (poly[1] == -1) {
            ans.append("-x");
        }
        if (poly[0] >= 1) {
            ans.append("+").append(poly[0]);
        } else if (poly[0] < 0) {
            ans.append(poly[0]);
        }
        return ans.toString();
    }

    private static int[] hashtag(String a, String b) {
        int[] first = solve(a);
        int[] second = solve(b);
        int[] ans = new int[1001];
        for (int i = 0; i < 1001; i++) {
            ans[i] = first[i] * second[i];
        }
        return ans;
    }

    private static int[] multiply(String a, String b) {
        int[] first = solve(a);
        int[] second = solve(b);
        int[] ansHelp = new int[2001];
        for (int i = 0; i < 1001; i++) {
            for (int j = 0; j < 1001; j++) {
                ansHelp[i + j] += first[i] * second[j];
            }
        }
        int[] ans = new int[1001];
        for (int i = 0; i < 1001; i++) {
            ans[i] = ansHelp[i];
        }
        return ans;
    }

    public static String answerMessage(String text, int senderID) {
        try {
            String firstText = text;
            if (text.charAt(0) != '-' && text.charAt(0) != '+') {
                text = '+' + text;
            }
            text = "+0" + text;
            text = fix(text);
            String ans = toPoly(solve(text));
            if (ans.isEmpty()) {
                ans = "0";
            } else if (ans.charAt(0) == '+') {
                ans = ans.substring(1);
            }
            return firstText + " = " + ans;
        } catch (Exception ignored) {
            return "Input Not Valid";
        }

    }

    public static String sendMessage(int receiverID) {
        return null;
    }

    public static String answerGPMessage(String text, int senderID, int groupID) {
        return null;
    }

    public static String sendGPMessage(int groupID) {
        return null;
    }

    public static String sendTweet(String text) {
        return null;
    }

    public static String sendComment(String text, int senderID, int postID) {
        return null;
    }
}
