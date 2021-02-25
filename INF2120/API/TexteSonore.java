package INF2120.API;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Une classe pour contenir une suite de syllabe.
 *
 * Cette classe permet la gestion d'une suite de syllabe.
 *
 * @see SyllabeFrancais
 */
public class TexteSonore extends ArrayList< SyllabeFrancais > {
    /**
     * Le caractère utilisé pour séparé les syllabes lors de la lecture et de l'écriture.
     */
    public static final String SEPARATEUR = ".";

    /**
     * Construit une suite de syllabe vide.
     */
    public TexteSonore() {}

    /**
     * Construit une suite de syllabes à partir du contenu d'un fichier.
     *
     * @param nomFichier Le nom du fichier qui contient la suite de syllabes.
     */
    public TexteSonore( String nomFichier ) {
        File fichier = new File( nomFichier );
        Scanner scanner = null;

        try{
            scanner = new Scanner( fichier );
        } catch( FileNotFoundException e ) {
            Erreur.FICHIER_INEXISTANT.lancer( "\"" + nomFichier +"\"" );
        }

        scanner.useDelimiter( "" );
        lire( scanner );
        scanner.close();
    }

    public TexteSonore sansDoublon;


    /**
     * Lit une suite de syllabe dans le {@code Scanner}.
     *
     * Consulte le {@code Scanner} pour lire une suite de syllabe séparé par le caractère {@code SEPARATEUR}.
     *
     * @param scanner le {@code Scanner} dans lequel la lecture est effectué.
     * @return le groupe de consonne lu.
     * @exception NoSuchElementException s'il n'y a pas de {@code API_Consonne} valide.
     * @exception IllegalStateException si le {@code Scanner} est fermé.
     */
    private void lire( Scanner scanner ) {
        try{
            while( scanner.hasNext() ) {
                add( SyllabeFrancais.lire( scanner ) );
                scanner.next( SEPARATEUR );
            }
        } catch( NoSuchElementException e ) {
        }
    }

    /**
     * Calcule l'occurence de chacune des syllabes du TexteSonore.
     *
     * Utilise la méthode occurenceSyllabe de SyllabeFrancais
     *
     */
    public void occurenceSyllabes(){
        for(int i = 0; i < size(); ++i){
            get(i).occurenceSyllabe(this);
        }
    }

















    /**
     * Copie le texte sonore dans un nouveau texte sonore.
     * @return Une copie du textesonore
     */
    public TexteSonore copier(){
        TexteSonore copie = new TexteSonore();
            copie.addAll(this);
        return copie;
    }

    /**
     * Copie ce TexteSonore en éliminant les doublons.
     *
     * Cela facilite la comparaison de distance entre chacune des syllabes.
     *
     * @return Une copie du TexteSonore sans doublon.
     */
    public void eliminerDoublon() {

        TexteSonore copie;

        copie = copier();

        for(int i = 0; i < this.size(); ++i){
            for(int j = 0; j < copie.size(); ++j){
                if(this.get(i).estPareille(copie.get(j))){
                    copie.remove(j);
                }
            }
            copie.add(this.get(i));
        }
        sansDoublon = copie;
    }

    /**
     * Compte le nombre de syllabes différentes dans le texteSonore
     *
     * @return Le .size() du TexteSonore sansDoublon.
     */
    public int nombreSyllabesDifferentes(){
        return sansDoublon.size();
    }


    /**
     * Compte le nombre de syllabes différentes dans le texteSonore
     *
     * @return Le .size() du TexteSonore sansDoublon.
     */
    public void reduire(){

        SyllabeFrancais syllabe1 = sansDoublon.get(0);
        SyllabeFrancais syllabe2 = sansDoublon.get(1);
        int distance = 42;

        for(int i = 0; i < sansDoublon.size(); ++i){
            for(int j = 0; j < sansDoublon.size(); ++j){
                if(!sansDoublon.get(i).estPareille(sansDoublon.get(j))){
                    if(sansDoublon.get(i).calculDistance(sansDoublon.get(j)) <= distance){
                        syllabe1 = sansDoublon.get(i);
                        syllabe2 = sansDoublon.get(j);
                    }
                }
            }
        }
        echanger(syllabe1, syllabe2);
    }

    public void echanger(SyllabeFrancais syllabe1, SyllabeFrancais syllabe2){

        SyllabeFrancais syllabeGardee;
        SyllabeFrancais syllabeChangee;

        if(syllabe1.getNombreOccurences()>syllabe2.getNombreOccurences()){
            syllabeGardee = syllabe1;
            syllabeChangee = syllabe2;
        }else{
            syllabeGardee = syllabe2;
            syllabeChangee = syllabe1;
        }

        for(int i = 0; i <size(); ++i){
            if(this.get(i).estPareille(syllabeChangee)){
                this.set(i,syllabeGardee);
            }
        }
    }



    /**
     * Construit une chaîne de caractères contenant la suite de syllabe représenté par les symboles de l'API.
     *
     * @return la chaîne construite.  S'il n'y a pas de syllabe dans la suite, alors la chaîne sera vide.
     */
    @Override
    public String toString() {
        return stream().map( SyllabeFrancais::toString ).collect( Collectors.joining( SEPARATEUR ) );
    }
}
