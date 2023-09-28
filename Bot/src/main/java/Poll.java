import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class Poll {
    private static HashMap<Integer, Vote> votes; //voteID Vote
    private static HashMap<Integer, Integer> groupVotes; //groupID VoteID

    public static String getFields() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String votesString = objectMapper.writeValueAsString(votes);
            String groupsString = objectMapper.writeValueAsString(groupVotes);
            String[] fields = new String[]{votesString, groupsString};
            return objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setFields(String data) {
        if (data == null) {
            votes = new HashMap<>();
            groupVotes = new HashMap<>();
            return;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] fields = objectMapper.readValue(data, String[].class);
            votes = objectMapper.readValue(fields[0], new TypeReference<HashMap<Integer, Vote>>() {
            });
            groupVotes = objectMapper.readValue(fields[1], new TypeReference<HashMap<Integer, Integer>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            votes = new HashMap<>();
            groupVotes = new HashMap<>();
        }
    }

    public static boolean invokeAnswerMessage() {
        return false;
    }

    public static boolean invokeAnswerGroupMessage() {
        return true;
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

    public static String answerMessage(String text, int senderID) {
        return null;
    }

    public static String sendMessage(int receiverID) {
        return null;
    }

    public static String answerGPMessage(String text, int senderID, int groupID) {
        if (text.equals("/help")) {
            return "/create <question> + <nextLine> + <OptionsCount> + <option> (separate With NextLine) + \n"
                    + "/vote <index> (To Vote) + /get (To Get Vote Results) /end (To End The Vote)";
        }
        if (text.equals("/end")) {
            if (!groupVotes.containsKey(groupID)) return "No Active Votes On This Group";
            Vote vote = votes.get(groupVotes.get(groupID));
            votes.remove(groupVotes.get(groupID));
            groupVotes.remove(groupID);
            return "Vote Ended\n" + vote.voteState();
        }
        if (text.equals("/get")) {
            if (!groupVotes.containsKey(groupID)) return "No Active Votes On This Group";
            Vote vote = votes.get(groupVotes.get(groupID));
            votes.remove(groupVotes.get(groupID));
            groupVotes.remove(groupID);
            return "Vote Results :\n" + vote.voteState();
        }
        if (text.startsWith("/create ")) {
            if (groupVotes.containsKey(groupID)) return "A Vote Is Already In Progress In This Group";
            try {
                System.out.println(text);
                Scanner scanner = new Scanner(text);
                scanner.next();
                String question = scanner.nextLine();
                int count = scanner.nextInt();
                scanner.nextLine();
                ArrayList<String> options = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    options.add(scanner.nextLine());
                }
                Random random = new Random();
                int newVoteCode;
                do {
                    newVoteCode = random.nextInt();
                } while (votes.containsKey(newVoteCode) || newVoteCode == 0);
                Vote vote = new Vote(options, question);
                groupVotes.put(groupID, newVoteCode);
                votes.put(newVoteCode, vote);
                return vote.voteState();
            } catch (Exception e) {
                e.printStackTrace();
                return "Invalid Command";
            }
        }
        if (text.startsWith("/vote ")) {
            if (groupVotes.containsKey(groupID)) {
                try {
                    int voteIndex = Integer.parseInt(text.substring(6));
                    Vote vote = votes.get(groupVotes.get(groupID));
                    return vote.doVote(voteIndex, senderID);
                } catch (Exception e) {
                    return "Invalid Vote Index";
                }
            }
        }
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

    public static class Vote {
        private HashMap<String, Integer> options;
        private ArrayList<Integer> votedPeople;
        private String question;

        public Vote() {

        }

        public Vote(ArrayList<String> options, String question) {
            this.question = question;
            this.options = new HashMap<>();
            options.forEach(option -> this.options.put(option, 0));
            votedPeople = new ArrayList<>();
        }

        private String doVote(int optionIndex, int ID) {
            if (optionIndex < 1 || options.size() < optionIndex) return "Index Bigger Than Options Count";
            String option = (String) options.keySet().toArray()[optionIndex - 1];
            if (votedPeople.contains(ID)) return "You've Already Voted";
            if (!options.containsKey(option)) return "This Option Doesn't Exist";
            votedPeople.add(ID);
            int count = options.get(option) + 1;
            options.replace(option, count);
            return "Successfully Voted\n" + voteState();
        }

        private String voteState() {
            StringBuilder voteState = new StringBuilder(question + "\n");
            for (Map.Entry<String, Integer> option :
                    options.entrySet()) {
                voteState.append(option.getKey()).append(" ").append(option.getValue()).append("\n");
            }
            return voteState.toString();
        }

        public HashMap<String, Integer> getOptions() {
            return options;
        }

        public void setOptions(HashMap<String, Integer> options) {
            this.options = options;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public ArrayList<Integer> getVotedPeople() {
            return votedPeople;
        }

        public void setVotedPeople(ArrayList<Integer> votedPeople) {
            this.votedPeople = votedPeople;
        }
    }
}
