package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class LigneEcritureComptableTest {

    @Test
    public void toString1() {
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable();
        CompteComptable compteComptable = new CompteComptable();
        compteComptable.setNumero(666);
        compteComptable.setLibelle("Achats de goodies Alice Cooper");
        ligneEcritureComptable.setCompteComptable(compteComptable);
        ligneEcritureComptable.setLibelle("CD mortel");
        ligneEcritureComptable.setDebit(new BigDecimal("16.66"));
        ligneEcritureComptable.setCredit(null);

        Assert.assertEquals("LigneEcritureComptable{compteComptable=CompteComptable{numero=666, " +
                "libelle='Achats de goodies Alice Cooper'}, " +
                "libelle='CD mortel', debit=16.66, credit=null}",
                ligneEcritureComptable.toString());
        Assert.assertNotEquals( "LigneEcritureComptable{compteComptable=CompteComptable{numero=777, " +
                "libelle='Achats de goodies Christophe Mae'}, " +
                "libelle='CD mortellement ennuyant', debit=null, credit=null}", ligneEcritureComptable.toString());

    }
}
