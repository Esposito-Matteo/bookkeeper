package org.apache.bookkeeper.bookie;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookieClassTests.Utils.BookKeeperClusterTestCase;
import org.apache.bookkeeper.client.BookKeeper;
import org.apache.bookkeeper.client.LedgerHandle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class AddEntryForCompactionTest extends BookKeeperClusterTestCase {

    private EntryLogger entryLogger;
    private LedgerHandle ledgerHandle;
    private Long location;
    private Triple<Boolean,Long, byte[]> triple;

    public AddEntryForCompactionTest(Triple<Boolean,Long, byte[]> inTriple) {
        super(3);
        this.triple = inTriple;

    }

        @Parameterized.Parameters
        public static Collection<?> getParameters() {

                return Arrays.asList(
                        new ImmutableTriple<>(true,1234L,"content".getBytes()),  // 1234 is a placehoder,
                                                                                            // it gets changed to 0
                                                                        // giving the fact that it will be the only
                                                                        // valid ledger created thus avoiding another
                                                                        // test with parameter 0
                        new ImmutableTriple<>(false,-1L,"".getBytes()),   // not valid,
                                                                                   // throws exception
                                                                                   // java.lang.IllegalArgumentException
                        new ImmutableTriple<>(false,1L,null),    // "valid" ( long > 0 )  ledgerID
                                                                                //  but null byte content and notFound
                                                                                // ledgerID
                        new ImmutableTriple<>(true,12345L,"content".getBytes())   // Added for Jacoco Branch Coverage
                        );

        }


        @Before
    public void setUp() throws Exception {
        super.setUp();
        baseConf.setOpenFileLimit(1);
        File test = new File("/tmp/bk-data/current");
        test.mkdirs();

        entryLogger = new EntryLogger(super.baseConf);

        ledgerHandle = bkc.createLedger(BookKeeper.DigestType.CRC32, "pswd".getBytes());

    }

    @After
    public void tearDown() {
        File tempFile = new File("/tmp/bk-data/current");
        boolean exists = tempFile.exists();
        if(exists) {
            try {
                FileUtils.deleteDirectory(new File("/tmp/bk-data/current"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMethod() throws IOException {

            ByteBuf newEntry3 = null;
            if(triple.getRight()!=null) {
                    newEntry3 = Unpooled.buffer(1024);
                    newEntry3.writeLong(ledgerHandle.getId()); // ledger id
                    newEntry3.writeLong(1); // entry id
                    newEntry3.writeBytes(triple.getRight());
                    if(triple.getMiddle()==12345L){
                       entryLogger.createNewCompactionLog();       // Added for Jacoco Branch Coverage
                    }
            }
            try {
                if(triple.getMiddle()==1234L||triple.getMiddle()==12345L){
                    location = entryLogger.addEntryForCompaction(ledgerHandle.getId(), newEntry3);
                }else{
                    location = entryLogger.addEntryForCompaction(triple.getMiddle(), newEntry3);
                }

                    entryLogger.flushCompactionLog();

                    File file = entryLogger.getCurCompactionLogFile();
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    String testStringer = new String(fileContent);

                    Boolean testB = testStringer.contains(new String(triple.getRight()));
                    Assert.assertEquals(triple.getLeft(),testB);
            }catch(NullPointerException |java.lang.IllegalArgumentException | IOException e) {
                    if(triple.getRight()==null||triple.getMiddle()==-1L) {
                            Assert.assertEquals(triple.getLeft(), false);
                    }else{

                            Assert.assertEquals(triple.getLeft(), true);
                    }
                    }

    }

}

