package com.amir.roomdemo
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
/**
 * we need here some additional information about database
 * we need the version and entities
 */
@Database(entities = [EmployeeEntity::class], version = 1)
abstract class EmployeeDatabase : RoomDatabase() {
     //connecting the database to our DAO
    abstract fun employeeDao(): EmployeeDAO

    /**
     * then we need to define a companion object, which allows us to add functions on the employee database class,
     * for example, classes can call employee database, get instance context to instantiate a new employee database.
     */
    companion object {
        /**
         * this will keep a reference to any database returned via get instance.
         * This will help us to avoid repeatedly initializing the database, which is expensive in terms of performance.
         */
        /**
         * The value of a volatile variable will never be cached, and all writes and reads will be done to and from the main memory.
         * It means the changes made by one thread to share data are visible to other threads also, and we need
         * to add the @volatile keyword here.
         */
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        //singleTon
        fun getInstance(context: Context): EmployeeDatabase {
            /**
             * We need to pass the lock to it, which will be this and then we can execute some code.
             * here, multiple threads can ask for the database at the same time and to ensure we only initialize at once.
             * By using this synchronized function, only one thread may enter a synchronized block at a time.
             */
            synchronized(this) {
                /**
                 * we need to copy the current value of instance, to a local variable.
                 * So Kotlin can SmartCast, so SmartCast is only available to local variables.
                 */
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EmployeeDatabase::class.java,
                        "employee_database"
                        /**
                         * here is that it wipes and rebuilds instead of migrating if no migration object exists.
                         *
                         */
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}