package org.apache.bookkeeper.bookieClassTests.entity;

public class ReadEntryAux {
    private Long ledgerID;
    private Long entryID;
    private Boolean expected;

    public ReadEntryAux(Long ledgerID, Long entryID, Boolean expected) {
        this.ledgerID = ledgerID;
        this.entryID = entryID;
        this.expected = expected;
    }

    public Long getLedgerID() {
        return ledgerID;
    }

    public void setLedgerID(Long ledgerID) {
        this.ledgerID = ledgerID;
    }

    public Long getEntryID() {
        return entryID;
    }

    public void setEntryID(Long entryID) {
        this.entryID = entryID;
    }

    public Boolean getExpected() {
        return expected;
    }

    public void setExpected(Boolean expected) {
        this.expected = expected;
    }
}
