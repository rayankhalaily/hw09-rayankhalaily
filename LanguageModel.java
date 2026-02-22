import java.util.HashMap;
import java.util.Random;

public class LanguageModel {
    HashMap<String, List> CharDataMap;
    int windowLength;
    private Random randomGenerator;

    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);
        while ((!in.isEmpty()) && (window.length() < windowLength)) {
            c = in.readChar();
            window += c;
        }
        while (!in.isEmpty()) {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window += c;
            window = window.substring(1);
        }
        for (List probs : CharDataMap.values())
            calculateProbabilities(probs);
    }

    void calculateProbabilities(List probs) {
        int windowTotal = 0;
        for (int i = 0; i < probs.getSize(); ++i) {
            windowTotal += probs.get(i).count;
        }

        double cumulativeProb = 0;
        for (int i = 0; i < probs.getSize(); ++i) {
            CharData current = probs.get(i);
            current.p = current.count / (double) windowTotal;
            cumulativeProb += current.p;
            current.cp = cumulativeProb;
        }
    }

    char getRandomChar(List probs) {
        double random = randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); ++i) {
            if (random < probs.get(i).cp) {
                return probs.get(i).chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr;
    }

    public String generate(String initialText, int textLength) {
        if (initialText.length() < windowLength) return initialText;
        String generatedText = initialText;
        for (int i = 0; i < textLength; ++i) {
            String window = generatedText.substring(generatedText.length() - windowLength);
            List probs = CharDataMap.get(window);
            if (probs == null) return generatedText;
            generatedText += getRandomChar(probs);
        }
        return generatedText;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            str.append(key + " : " + CharDataMap.get(key) + "\n");
        }
        return str.toString();
    }
}
