package org.apache.bookkeeper.bookieClassTests.tests;

import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookieClassTests.Utils.BookKeeperClusterTestCase;
import org.apache.bookkeeper.bookieClassTests.entity.ReadEntryAux;
import io.netty.buffer.ByteBuf;
import org.apache.bookkeeper.bookie.Bookie;
import org.apache.bookkeeper.bookie.BookieException;
import org.apache.bookkeeper.client.BookKeeper;
import org.apache.bookkeeper.client.LedgerHandle;
import org.apache.bookkeeper.conf.ServerConfiguration;
import org.apache.bookkeeper.net.BookieSocketAddress;
import org.apache.bookkeeper.proto.BookkeeperInternalCallbacks;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class ReadEntryTest extends BookKeeperClusterTestCase {

    private ReadEntryAux testEntity;
    private static Bookie bookie;
    private static LedgerHandle ledgerHandle;
    private static byte[] masterKey = "masterKey".getBytes();

    public ReadEntryTest(ReadEntryAux testEntity) {
        super(3);
        this.testEntity = testEntity;
    }

    @Parameterized.Parameters
    public static Collection<ReadEntryAux> getParameters() {

        return Arrays.asList(new ReadEntryAux(-1L, -1L, false),
                new ReadEntryAux(0L, 1L, false),
                new ReadEntryAux(1L, 0L, false),
                new ReadEntryAux(1234L, 1L, true));
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        File test = new File("/tmp/bk-data/current");
        test.mkdirs();
        ServerConfiguration conf = bsConfs.get(0);
        bookie = bs.get(0).getBookie();

        if (testEntity.getLedgerID() == 1234L) {
            ledgerHandle = bkc.createLedger(BookKeeper.DigestType.CRC32, "pswd".getBytes());

            ByteBuf newEntry3 = Unpooled.buffer(1024);
            newEntry3.writeLong(ledgerHandle.getId()); // ledger id
            newEntry3.writeLong(testEntity.getEntryID()); // entry id
            newEntry3.writeBytes("matteo".getBytes());

            bookie.addEntry(newEntry3,false,writeCallback,null,masterKey);
            testEntity.setLedgerID(ledgerHandle.getId());

        }
    }

    private static final BookkeeperInternalCallbacks.WriteCallback  writeCallback = new BookkeeperInternalCallbacks.WriteCallback() {
        @Override
        public void writeComplete(int rc, long ledgerId, long entryId, BookieSocketAddress addr, Object ctx) {

        }
    };


    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(new File("/tmp/bk-data/"));
    }

    @Test
    public void testMethod() {
        Boolean expected = testEntity.getExpected();
        Boolean result;
        Long ledgerId = testEntity.getLedgerID();
        Long entryID = testEntity.getEntryID();
        try {
            ByteBuf test = bookie.readEntry(ledgerId, entryID);
            byte[] resultBytes = new byte[test.capacity()];
            test.getBytes(0,resultBytes);
            String testString = new String(resultBytes);

            if (testString.contains("matteo")) {

                result = true;
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertEquals(expected, result);
    }

}

