package by.krukouski.sleeptracker.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.krukouski.sleeptracker.database.SleepDatabaseDao
import by.krukouski.sleeptracker.database.SleepNight
import kotlinx.coroutines.*

class SleepDetailViewModel(
    private val sleepNightKey: Long = 0L,
    val dataSource: SleepDatabaseDao
) : ViewModel() {

    /** Coroutine setup variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _night = MutableLiveData<SleepNight>()
    val night : LiveData<SleepNight>
        get() = _night

    init {
        initializeNight()
    }

    private suspend fun getNightFromDatabase(): SleepNight? {
       return withContext(Dispatchers.IO) {
                dataSource.get(sleepNightKey)
            }

    }

    private fun initializeNight() {
        uiScope.launch {
            _night.value = getNightFromDatabase()
        }
    }

    /**
     * Variable that tells the fragment whether it should navigate to [SleepTrackerFragment].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the [Fragment]
     */
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    /**
     * When true immediately navigate back to the [SleepTrackerFragment]
     */
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    /**
     * Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work.
     *
     * onCleared() gets called when the ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    /**
     * Call this immediately after navigating to [SleepTrackerFragment]
     */
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onClose() {
        _navigateToSleepTracker.value = true
    }

}
