/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

/**
 *
 * @author menezes
 */
public class KnockKnockProtocol {
    private static final int WAITING = 0;
    private static final int SENTKNOCKKNOCK = 1;
    private static final int SENTCLUE = 2;
    private static final int ANOTHER = 3;

    private static final int NUMJOKES = 5;

    private int state = WAITING;
    private int currentJoke = 0;

    private String[] clues = { "Prato", "A Divina", "Repete", "Who", "Who" };
    private String[] answers = { "Prato ver como essa piada é ruim",
                                 "Adivina quem é e te dou um prêmio",
                                 "quem, quem, quem...",
                                 "Is there an owl in here?",
                                 "Is there an echo in here?" };

    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING && theInput.equalsIgnoreCase("piada")) {
            theOutput = "Toc! Toc!";
            state = SENTKNOCKKNOCK;
        } else if (state == SENTKNOCKKNOCK) {
            if (theInput.equalsIgnoreCase("quem eh?")) {
                theOutput = clues[currentJoke];
                state = SENTCLUE;
            } else {
                theOutput = "Você deveria dizer \"quem eh?\"! " +
			    "De novo. Toc! Toc!";
            }
        } else if (state == SENTCLUE) {
            if (theInput.equalsIgnoreCase(clues[currentJoke] + " quem?")) {
                theOutput = answers[currentJoke] + " Outra piada? (s/n)";
                state = ANOTHER;
            } else {
                theOutput = "Você deveria dizer \"" + 
			    clues[currentJoke] + 
			    " quem?\"" + 
			    "! De novo. Toc! Toc!";
                state = SENTKNOCKKNOCK;
            }
        } else if (state == ANOTHER) {
            if (theInput.equalsIgnoreCase("s")) {
                theOutput = "Toc! Toc!";
                if (currentJoke == (NUMJOKES - 1))
                    currentJoke = 0;
                else
                    currentJoke++;
                state = SENTKNOCKKNOCK;
            } else {
                theOutput = "Bye.";
                state = WAITING;
            }
        }
        return theOutput;
    }
}
