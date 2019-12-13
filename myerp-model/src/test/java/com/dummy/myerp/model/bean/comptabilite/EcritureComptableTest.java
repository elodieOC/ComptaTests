package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test (expected = Test.None.class)
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

    @Test (expected = Test.None.class)
    public void getTotalDebit() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        Assert.assertEquals( "301.00",vEcriture.getTotalDebit().toString());
        Assert.assertNotEquals( null,vEcriture.getTotalDebit().toString());
    }

    @Test (expected = Test.None.class)
    public void getTotalCredit() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        Assert.assertEquals("33", vEcriture.getTotalCredit().toString());
        Assert.assertNotEquals(null, vEcriture.getTotalCredit().toString());
    }

    @Test (expected = Test.None.class)
    public void getSetLibelle() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.setLibelle("Gobelet Recyclable");
        Assert.assertEquals("Gobelet Recyclable", vEcriture.getLibelle());
        Assert.assertNotEquals("Trombone", vEcriture.getLibelle());
    }

    @Test (expected = Test.None.class)
    public void getSetDate() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.setDate(java.sql.Date.valueOf("1984-12-24"));
        Assert.assertEquals("1984-12-24", vEcriture.getDate().toString());
        Assert.assertNotEquals("2019-01-01", vEcriture.getDate().toString());
    }

    @Test (expected = Test.None.class)
    public void getSetJournal() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.setJournal(new JournalComptable("TE", "Test"));
        Assert.assertEquals("TE", vEcriture.getJournal().getCode());
        Assert.assertEquals("Test", vEcriture.getJournal().getLibelle());
        Assert.assertNotEquals("AC", vEcriture.getJournal().getCode());
        Assert.assertNotEquals("Achat", vEcriture.getJournal().getLibelle());
    }

    @Test (expected = Test.None.class)
    public void getSetReference() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.setReference("XM-1984/00001");
        Assert.assertEquals("XM-1984/00001", vEcriture.getReference());
        Assert.assertNotEquals("AC-2019/00032", vEcriture.getReference());
    }

    @Test (expected = Test.None.class)
    public void toString1() {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(-1);
        ecritureComptable.setLibelle("Câble ethernet");
        Assert.assertEquals(ecritureComptable.toString(), "EcritureComptable{id=-1, journal=null, reference='null', " +
                "date=null, libelle='Câble ethernet', totalDebit=0, totalCredit=0, listLigneEcriture=[\n" +
                "\n" +
                "]}");
        Assert.assertNotEquals(ecritureComptable.toString(), "EcritureComptable{id=-1, journal=null, reference='null', " +
                "date=null, libelle='tapis de souris', totalDebit=0, totalCredit=0, listLigneEcriture=[\n" +
                "\n" +
                "]}");
    }
}
