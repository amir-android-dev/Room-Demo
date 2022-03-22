package com.amir.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDAO {
    /**
     *we need to make it the suspend function using that coroutine class, because this is an
    operation that will take quite some time or relatively speaking will take some time.
    And this mustn't be done on the main threat.
    It has to be done on a background threat, which is achievable by using coroutines.
     */
    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)//it must store entity in our db

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)


    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)
    /**now we have to retrieve all employees
     * there are two ways: 1: retrieve all of them /
     * 2: get a particular one where we have the id, that want to be deleted
     */

    /**
     * a flow is part of the coroutine class used to hold values that can always change at runtime.
     * That's because it automatically emits(auswerfen, abgeben) value more like a life update.
     * With Flow, all you need is to collect the value from the variable or method without needing to always
    repeat codes to update the user interface.
     * The collect method keeps emitting data as it changes.
     */

    /**
     * There are other ways or methods you can use to listen to a flow depending on your needs.
     * for example collectLatest:
     * just like the name, it returns the lost value from an update and forgets the previous ones.
     * collectedIndexed:
     ** you can get an index of an element with its value.
     *  combine:
     *  you can transform flows and return its values automatically when they change.
     */
    /**
     *  select * means all entries from this table.
     *And then this is the function that we need to call in order to get all the entries, and it will return
    a flow which is part of the coroutine class.
    Just like the suspense keyword, it runs the operation on a different thread, but with an additional
    feature of omitting updates automatically when they occur(auftreten, passieren).
     */
    @Query("SELECT * FROM `employee-table`")//this table is which we create in our EmployeeEntity
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `employee-table` where id=:id")
    //this time doesn't returns a list of employees but rather an individual employee item
    fun fetchEmployeeById(id: Int): Flow<EmployeeEntity>

}