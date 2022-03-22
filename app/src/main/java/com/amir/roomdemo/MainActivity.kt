package com.amir.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amir.roomdemo.databinding.ActivityMainBinding
import com.amir.roomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        /**after we create the EmployeeApp
         *we need to get the application in order to get db object that we just created in EmployeeDb
         *and then we can access the abstract employeeDAO in EmployeeDatabase which is a room data base
         */
        val employeeDAO = (application as EmployeeApp).db.employeeDao()

        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDAO)

        }
        //this will have to run in the background, because we loading data
        lifecycleScope.launch {
            employeeDAO.fetchAllEmployees().collect {
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, employeeDAO)
            }
        }
    }

    /**we need an employeeDAO to add the record, otherwise we cannot store the data
     * because we need to use the DAO to call its insert method, which needs an employeeEntity
     *how can we have access to DAO when we call addRecord method?
     *  We setup an application. look at EmployeeApp
     */
    private fun addRecord(employeeDAO: EmployeeDAO) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()
        /**
         * when both aren't empty we do the insert, but because
         * we need to use the coroutine here, we are going to make use of lifeCycleScope
         * we lunch our coroutine
         */

        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch { employeeDAO.insert(EmployeeEntity(name = name, email = email)) }
            Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
            binding?.etName?.text?.clear()
            binding?.etEmailId?.text?.clear()

        } else {
            Toast.makeText(applicationContext, "Name or Email cannot be blank", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun setupListOfDataIntoRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDAO: EmployeeDAO
    ) {
        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeesList,
                { updateId ->
                    updateRecordDialog(updateId, employeeDAO)
                },
                { deleteId ->
                    deleteAlertDialog(deleteId, employeeDAO)
                })
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    //we need the id of the item that we want to update
    //we need the DAO in order make changes to our database
    private fun updateRecordDialog(id: Int, employeeDAO: EmployeeDAO) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            employeeDAO.fetchEmployeeById(id).collect {
                if(it != null){
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                }

            }
        }
        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    employeeDAO.update(EmployeeEntity(id, name, email))
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_LONG).show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()

    }

    private fun deleteAlertDialog(id: Int, employeeDAO: EmployeeDAO) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDAO.delete(EmployeeEntity(id))
                Toast.makeText(applicationContext, "Record deleted successfully", Toast.LENGTH_LONG)
                    .show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


}






