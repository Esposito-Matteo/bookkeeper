package org.apache.bookkeeper.bookie;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.entities.InternalReadEntryAux;
import org.apache.bookkeeper.bookieClassTests.Utils.BookKeeperClusterTestCase;
import org.apache.bookkeeper.client.BookKeeper;
import org.apache.bookkeeper.client.LedgerHandle;
import org.apache.bookkeeper.conf.ServerConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class InternalReadEntryTest extends BookKeeperClusterTestCase {

    private EntryLogger entryLogger;
    private InternalReadEntryAux testEntity;
    private ServerConfiguration conf = super.baseConf;
    private LedgerHandle ledgerHandle;
    private Long location;

    public InternalReadEntryTest(InternalReadEntryAux inTestEntity) {
        super(3);
        testEntity = inTestEntity;
    }

    @Parameterized.Parameters
    public static Collection<?> getParameters() {

        return Arrays.asList(

                new InternalReadEntryAux(-1L, -1L, -1L, true, false),
                new InternalReadEntryAux(0L, 1L, 0L, false, false),
                new InternalReadEntryAux(1L, 0L, 1L, false, false),
                new InternalReadEntryAux(1234L, 1L, 0L, false, true));
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        baseConf.setOpenFileLimit(1);
        File test = new File("/tmp/bk-data/current");
        test.mkdirs();

        entryLogger = new EntryLogger(super.baseConf);


        if (testEntity.getLedgerID() == 1234L) {
            ledgerHandle = bkc.createLedger(BookKeeper.DigestType.CRC32, "pswd".getBytes());

            ByteBuf newEntry3 = Unpooled.buffer(1024);
            newEntry3.writeLong(ledgerHandle.getId()); // ledger id
            newEntry3.writeLong(testEntity.getEntryID()); // entry id
            newEntry3.writeBytes("matteo".getBytes());

            location = entryLogger.addEntry(ledgerHandle.getId(), newEntry3, true);
            testEntity.setLedgerID(ledgerHandle.getId());
            testEntity.setLocation(location);
        }
    }

    @After
    public void tearDown() {
        File test = new File("/tmp/bk-data/current");
        test.delete();
    }

    @Test
    public void testMethod() throws Exception {
        try {
            ByteBuf testBuf = entryLogger.internalReadEntry(testEntity.getLedgerID(), testEntity.getEntryID(),
                                                            testEntity.getLocation(), testEntity.getValidateEntry());
            byte[] testByte = new byte[testBuf.capacity()];
            testBuf.getBytes(0, testByte);
            String testString = new String(testByte);
            ledgerHandle.close();
            Assert.assertEquals(testEntity.getExpected(), (testString.contains(new String("matteo"))));
        } catch (NullPointerException | IOException | IllegalArgumentException e) {
            Assert.assertEquals(testEntity.getExpected(), false);


        }
    }
}
