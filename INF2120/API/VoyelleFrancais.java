package INF2120.API;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Décrit le son d'un groupe de voyelle en français.
 *
 * Cette description permet de contenir une ou deux voyelles afin de décrire le son des voyelles dans une syllabe.
 * S'il y a deux voyelles, alors la première voyelle du groupe devrait être une semi-voyelle.
 *
 * @see API_Consonne
 * @see SyllabeFrancais
 * @see <a href="https://fr.wiktionary.org/wiki/Annexe:Prononciation/fran%C3%A7ais">référence</a>
 */
public class VoyelleFrancais {
    /**
     * code utf-16 pour le tilde utilisé pour indiquer les voyelles nasales selon l'API.
     */
    public static final int TILDE_CODE_POINT = 0x0303;

    /**
     * Contient un {@code Pattern} de reconnaissance pour identifier si un caractère est un tilde.
     */
    private static final Pattern TILDE_PATTERN = Pattern.compile( Character.toString( TILDE_CODE_POINT ) );

    /**
     * La semi-voyelle du groupe de voyelle.
     * La valeur {@code null} est utilisé pour indiquer qu'elle n'est pas présente dans le groupe.
     */
    protected API_Voyelle semiVoyelle = null;

    /**
     * La voyelle de base du groupe.
     * Ne doit pas être {@code null}.
     */
    protected API_Voyelle voyelle;

    /**
     * Indique si la voyelle de base est une voyelle nasale.
     */
    protected boolean nasal = false;

    /**
     * Construit une voyelle simple.
     *
     * @param voyelle La voyelle de base du groupe.  Ne doit pas être {@code null}.
     */
    public VoyelleFrancais( API_Voyelle voyelle ) {
        this.voyelle = voyelle;
    }

    /**
     * Construit une voyelle simple, avec la possibilité de lui attribuer la caractéristique nasale.
     *
     * @param voyelle La voyelle de base du groupe.  Ne doit pas être {@code null}.
     * @param nasal {@code true} si la voyelle de base est nasale.
     */
    public VoyelleFrancais( API_Voyelle voyelle, boolean nasal ) {
        this.voyelle = voyelle;
        this.nasal = nasal;
    }

    /**
     * Construit un groupe de voyelle avec deux voyelles.
     *
     * @param semiVoyelle La semi-voyelle du groupe.
     * @param voyelle La voyelle de base du groupe.  Ne doit pas être {@code null}.
     */
    public VoyelleFrancais( API_Voyelle semiVoyelle, API_Voyelle voyelle ) {
        this.semiVoyelle = semiVoyelle;
        this.voyelle = voyelle;
    }
    /**
     * Construit un groupe de voyelle avec deux voyelles, avec la possibilité de lui attribuer la caractéristique
     * nasale.
     *
     * @param semiVoyelle La semi-voyelle du groupe.
     * @param voyelle La voyelle de base du groupe.  Ne doit pas être {@code null}.
     * @param nasal {@code true} si la voyelle de base est nasale.
     */
    public VoyelleFrancais( API_Voyelle semiVoyelle, API_Voyelle voyelle, boolean nasal ) {
        this.semiVoyelle = semiVoyelle;
        this.voyelle = voyelle;
        this.nasal = nasal;
    }


    /**
     * Consulte la caractéristique sonore 'nasale' du groupe de voyelle.
     * @return {@code true} si le groupe est nasal.
     */
    public boolean estNasal() {
        return nasal;
    }

    /**
     * Calcule la distance entre le groupe de voyelles de l'instance et un autre groupe de voyelle
     * @param vf1 VoyelleFrancais avec laquelle calculer la distance de l'instance
     * @return un int de la distance entre les 2 groupes de voyelles
     * */
    public int calculDistanceGroupeVoyelle(VoyelleFrancais vf1){
        int distanceEntreVf=this.voyelle.calculDistanceVoyelle(vf1.voyelle);
        if(vf1.semiVoyelle != null && this.semiVoyelle != null)
            distanceEntreVf += this.semiVoyelle.calculDistanceVoyelle(vf1.semiVoyelle);
        else
            distanceEntreVf += 4;
        if(vf1.estNasal() || this.estNasal())
            distanceEntreVf+=1;
        return  distanceEntreVf;
    }

    /**
     * Lit un groupe de voyelles dans le {@code Scanner}.
     *
     * Vérifie si les deux prochains caractères du {@code scanner} représentent des voyelles.  Si une seule voyelle est
     * trouvée, alors elle devient la voyelle de base du groupe.  Si deux voyelles sont trouvées, alors la première
     * devient la semi-voyelle et la seconde devient la voyelle de base.  Ensuite, vérifie si le prochain caractère
     * est le caractère désignant une voyelle nasale.  Si oui, alors la caractéristique nasale est ajouté au groupe
     * de voyelle.
     *
     * @param scanner le {@code Scanner} dans lequel la lecture est effectué.
     * @return le groupe de voyelle lu.
     * @exception NoSuchElementException s'il n'y a pas de {@code API_Voyelle} valide.
     * @exception IllegalStateException si le {@code Scanner} est fermé.
     */
    public static VoyelleFrancais lire( Scanner scanner ) {
        API_Voyelle voyelle = API_Voyelle.lire( scanner );
        API_Voyelle voyelle2 = null;
        boolean estNasal = false;

        try {
            voyelle2 = API_Voyelle.lire( scanner );
        } catch ( NoSuchElementException e ) {
        }

        try {
            scanner.next( TILDE_PATTERN );
            estNasal = true;
        } catch ( NoSuchElementException e ) {
        }

        return null == voyelle2
                ? new VoyelleFrancais( voyelle, estNasal )
                : new VoyelleFrancais( voyelle, voyelle2, estNasal );
    }


    /**
     * retourne une chaîne de caractère composée des voyelles du groupe.
     *
     * @return la chaîne contenant les symboles des voyelles du groupe et le symbole de nasalité si c'est le cas.
     */
    @Override
    public String toString() {
        return "" + ( null == semiVoyelle ? "" : semiVoyelle )
                + voyelle
                + ( nasal ? Character.toString( TILDE_CODE_POINT ) : "" );
    }
}
