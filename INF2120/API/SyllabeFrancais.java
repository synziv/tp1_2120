package INF2120.API;

import java.util.*;


/**
 * Représente l'unité de base pour la prononciation en français.
 *
 * Une syllabe est composé d'un groupe de syllabe (le noyau).
 * optionnelement, elle peu avoir deux groupes de consonne.
 * Le groupe de consonne avant le noyau est l'attaque de la syllabe.
 * Le groupe de consonne après le noyau est le coda de la syllabe.
 *
 * @see ConsonneFrancais
 * @see VoyelleFrancais
 * @see <a href="https://fr.wiktionary.org/wiki/Annexe:Prononciation/fran%C3%A7ais">référence</a>
 */
public class SyllabeFrancais {
    /**
     * Le groupe de consonne pour l'attaque de la syllabe.  S'il n'est pas présent, alors la valeur est à {@code null}.
     */
    protected ConsonneFrancais attaque = null;

    /**
     * Le groupe de voyelle pour la syllabe.  Ne doit pas être {@code null}.
     */
    protected VoyelleFrancais noyau;

    /**
     * Le groupe de consonne pour le code de la syllabe.  S'il n'est pas présent, alors la valeur est à {@code null}.
     */
    protected ConsonneFrancais coda = null;


    /**
     * Le nombre d'occurences de cette syllabe dans un TexteSonore. Est incrémenté lors de l'appel de la méthode
     * occurenceSyllabe.
     */
    private int nombreOccurences =0;

    public int getNombreOccurences() {
        return nombreOccurences;
    }

    /**
     * Construit une syllabe avec un noyau seulement.
     *
     * @param noyau le groupe de voyelle utilisé pour la syllabe.  Ne doit pas être {@code null}.
     */
    public SyllabeFrancais( VoyelleFrancais noyau) {
        this.noyau = noyau;
    }


    /**
     * Construit une syllabe avec une attaque et un noyau.
     *
     * @param attaque le groupe de consonne utilisé pour la syllabe.
     * @param noyau le groupe de voyelle utilisé pour la syllabe.  Ne doit pas être {@code null}.
     */
    public SyllabeFrancais( ConsonneFrancais attaque, VoyelleFrancais noyau) {
        this.attaque = attaque;
        this.noyau = noyau;
    }


    /**
     * Construit une syllabe avec une attaque, un noyau et un coda.
     *
     * @param attaque le groupe de consonne utilisé pour la syllabe.
     * @param noyau le groupe de voyelle utilisé pour la syllabe.  Ne doit pas être {@code null}.
     * @param coda le groupe de consonne utilisé pour la syllabe.
     */
    public SyllabeFrancais(ConsonneFrancais attaque, VoyelleFrancais noyau, ConsonneFrancais coda ) {
        this.attaque = attaque;
        this.noyau = noyau;
        this.coda = coda;
    }


    /**
     * Construit une syllabe avec un noyau et un coda.
     *
     * @param noyau le groupe de voyelle utilisé pour la syllabe.  Ne doit pas être {@code null}.
     * @param coda le groupe de consonne utilisé pour la syllabe.
     */
    public SyllabeFrancais(VoyelleFrancais noyau, ConsonneFrancais coda ) {
        this.noyau = noyau;
        this.coda = coda;
    }


    /**
     * Lit une syllabe dans le {@code Scanner}.
     *
     * Cherche possiblement un groupe de consonne qui servira d'attaque, ensuite un groupe de voyelle qui
     * servira de noyau et finalement un autre groupe de consonne pour le coda.
     *
     * @param scanner le {@code Scanner} dans lequel la lecture est effectué.
     * @return la voyelle lu.
     * @exception NoSuchElementException s'il n'y a pas de {@code SyllabeFrancais} valide.
     * @exception IllegalStateException si le {@code Scanner} est fermé.
     */
    public static SyllabeFrancais lire( Scanner scanner ) {
        ConsonneFrancais attaque = null;
        VoyelleFrancais noyau;
        ConsonneFrancais coda = null;

        try {
            attaque = ConsonneFrancais.lire( scanner );
        } catch ( NoSuchElementException e ) {
        }

        noyau = VoyelleFrancais.lire( scanner );

        try {
            coda = ConsonneFrancais.lire( scanner );
        } catch ( NoSuchElementException e ) {
        }


        return new SyllabeFrancais( attaque, noyau, coda );
    }



    /**
     * Compare l'attaque entre 2 syllabes en vérifiant si la consonne1 et la consonne2 sont égales.
     *
     * Permet de contourner le @NullPointerException lancé par la méthode .equals().
     *
     * @param syllabe1 instance SyllabeFrancais à comparer avec syllabe2
     * @param syllabe2 instance SyllabeFrancais à comparer avec syllabe1
     *
     * @return true si les 2 consonnes sont les mêmes dans les 2 syllabes
     */
    private boolean comparaisonAttaque(SyllabeFrancais syllabe1, SyllabeFrancais syllabe2){

        boolean estPareil = false;
        boolean consonne1Pareille = false;
        boolean consonne2Pareille = false;

        if(syllabe1.attaque != null && syllabe2.attaque != null){

            consonne1Pareille = syllabe1.attaque.consonne1.equals(syllabe2.attaque.consonne1);

            if(syllabe1.attaque.consonne2 != null && syllabe2.attaque.consonne2 != null){
                consonne2Pareille = syllabe1.attaque.consonne2.equals(syllabe2.attaque.consonne2);
            }else{
                consonne2Pareille = (syllabe1.attaque.consonne2 == null && syllabe2.attaque.consonne2 == null);
            }
            estPareil = consonne1Pareille && consonne2Pareille;


        }else {
            estPareil = (syllabe1.attaque == null && syllabe2.attaque == null);
        }
        return estPareil;
    }

    /**
     * Compare le noyau de 2 syllabes en vérifiant si la voyelle et la semiVoyelle sont égales, et si
     * elles ont la même nasalité.
     *
     * Permet de contourner le @NullPointerException lancé par la méthode .equals().
     *
     * @param syllabe1 instance SyllabeFrancais à comparer avec syllabe2
     * @param syllabe2 instance SyllabeFrancais à comparer avec syllabe1
     *
     * @return true si les 2 consonnes sont les mêmes dans les 2 syllabes
     */
    private boolean comparaisonNoyau(SyllabeFrancais syllabe1, SyllabeFrancais syllabe2){

        boolean estPareil = false;
        boolean voyellePareille = false;
        boolean semiVoyellePareille = false;
        boolean memeNasalite = false;

        voyellePareille = (syllabe1.noyau.voyelle.equals(syllabe2.noyau.voyelle));

        if(syllabe1.noyau.semiVoyelle != null && syllabe2.noyau.semiVoyelle != null){
            semiVoyellePareille = syllabe1.noyau.semiVoyelle.equals(syllabe2.noyau.semiVoyelle);
        }else{
            semiVoyellePareille = (syllabe1.noyau.semiVoyelle == null && syllabe2.noyau.semiVoyelle == null);
        }

        memeNasalite = ((syllabe1.noyau.estNasal() && syllabe2.noyau.estNasal()) ||
                !syllabe1.noyau.estNasal() && !syllabe2.noyau.estNasal());


        estPareil = memeNasalite && voyellePareille && semiVoyellePareille;

        return estPareil;
    }

    /**
     * Compare le coda entre 2 syllabes en vérifiant si la consonne1 et la consonne2 sont égales.
     *
     * Permet de contourner le @NullPointerException lancé par la méthode .equals().
     *
     * @param syllabe1 instance SyllabeFrancais à comparer avec syllabe2
     * @param syllabe2 instance SyllabeFrancais à comparer avec syllabe1
     *
     * @return true si les 2 consonnes sont les mêmes dans les 2 syllabes
     */
    private boolean comparaisonCoda(SyllabeFrancais syllabe1, SyllabeFrancais syllabe2){

        boolean estPareil = false;
        boolean consonne1Pareille = false;
        boolean consonne2Pareille = false;

        if(syllabe1.coda != null && syllabe2.coda != null){

            consonne1Pareille = syllabe1.coda.consonne1.equals(syllabe2.coda.consonne1);

            if(syllabe1.coda.consonne2 != null && syllabe2.coda.consonne2 != null){
                consonne2Pareille = syllabe1.coda.consonne2.equals(syllabe2.coda.consonne2);
            }else{
                consonne2Pareille = (syllabe1.coda.consonne2 == null && syllabe2.coda.consonne2 == null);
            }

            estPareil = consonne1Pareille && consonne2Pareille;

        }else {
            estPareil = (syllabe1.coda == null && syllabe2.coda == null);
        }

        return estPareil;
    }

    /**
     * Calcule le nombre d'occurence de la syllabe dans un TexteSonore{@code texte}
     *
     * Utilise la méthode estPareille. Si la syllabe est pareille, this.nombreOccurences
     * est incremente.
     *
     * @param texte Le TexteSonore dans lequel on compte.
     */

    public void occurenceSyllabe(TexteSonore texte){

           for(int i = 0; i < texte.size(); ++i){
               if(this.estPareille(texte.get(i))){
                   nombreOccurences ++;
               }
           }
    }

    /**
     * Compare les deux syllabes prises en argument pour s'assurer qu'elles sont pareilles.
     *
     * Utilise les méthodes comparaisonAttaque, comparaisonCoda et comparaisonNoyau
     *
     * @param syllabe Syllabe avec laquelle comparer avec
     * @return true si l'attaque, le noyau et le coda sont identiques.
     */
    public boolean estPareille(SyllabeFrancais syllabe){
        return comparaisonAttaque(this, syllabe)&& comparaisonCoda(this, syllabe) &&
                comparaisonNoyau(this, syllabe);
    }





    // À COMPLÉTER

    public int distance(SyllabeFrancais syllabe){
        return 0;
    }


    /**
     * retourne une chaîne de caractère composée des phonèmes de la syllabe.
     *
     * @return la chaîne contenant les symboles des phonèmes de la syllabe.
     */
    @Override
    public String toString() {
        return "" + ( null == attaque ? "" : attaque )
                + noyau
                + ( null == coda ? "" : coda );
    }


}
