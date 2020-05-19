package by.krukouski.sleeptracker.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDatabaseDao: SleepDatabaseDao
    private lateinit var database: SleepDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        database = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        sleepDatabaseDao = database.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = SleepNight()
        sleepDatabaseDao.insert(night)
        val tonight = sleepDatabaseDao.getTonight()
        assertEquals(-1, tonight?.sleepQuality)
    }
}