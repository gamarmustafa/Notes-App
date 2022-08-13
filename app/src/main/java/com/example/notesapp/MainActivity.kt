package com.example.notesapp

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.databinding.DialogAddBinding
import com.example.notesapp.databinding.DialogUpdateBinding
import com.example.notesapp.databinding.ItemLayoutBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private var binding: ActivityMainBinding? = null


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val noteDao = (application as NoteApp).db.noteDao()

        binding?.flAdd?.setOnClickListener {
            addRecord(noteDao = noteDao)
        }
        lifecycleScope.launch {
            noteDao.fetchAllEmployees().collect {
                val list = ArrayList(it)
                setUpListOfDataIntoRecyclerView(list, noteDao)
            }
        }
    }

    private fun addRecord(noteDao: NoteDao) {
        val addDialog = Dialog(this, R.style.Theme_Dialog)
        addDialog.setCanceledOnTouchOutside(true)
        val binding = DialogAddBinding.inflate(layoutInflater)
        addDialog.setContentView(binding.root)

            binding.tvAdd.setOnClickListener {
                val note = binding?.etAddNote?.text.toString()
                val c = Calendar.getInstance()
                val dateTime = c.time
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val date = sdf.format(dateTime)
                if (note.isNotEmpty()) {
                    lifecycleScope.launch {
                        noteDao.insert(NoteEntity(note = note, date = date))
                        Toast.makeText(applicationContext, "Record added.", Toast.LENGTH_SHORT).show()
                        addDialog.dismiss()
                    }
                } else {
                    Toast.makeText(applicationContext, "A note cannot be blank!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.tvCancel.setOnClickListener {
                addDialog.dismiss()
            }
            addDialog.show()

    }

    private fun setUpListOfDataIntoRecyclerView(
        employeesList: ArrayList<NoteEntity>,
        noteDao: NoteDao
    ) {
        if (employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeesList,
                { updateId ->
                    updateRecordDialog(updateId, noteDao)
                },
                { deleteId ->
                    deleteRecordAlertDialog(deleteId, noteDao)
                }
            )
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
        }
    }

    private fun updateRecordDialog(id: Int, noteDao: NoteDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            noteDao.fetchEmployeeById(id).collect {
                if (it != null) {
                    binding.etUpdateNote.setText(it.note)

                }
            }
        }
        binding.tvUpdate.setOnClickListener {
            val c = Calendar.getInstance()
            val dateTime = c.time
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = sdf.format(dateTime)
            val note = binding.etUpdateNote.text.toString()
            if (note.isNotEmpty()) {
                lifecycleScope.launch {
                    noteDao.update(NoteEntity(id, note, date))
                    Toast.makeText(applicationContext, "Record updated.", Toast.LENGTH_SHORT).show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Note cannot be blank!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }


    private fun deleteRecordAlertDialog(id: Int, noteDao: NoteDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record?")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                noteDao.delete(id)
                Toast.makeText(applicationContext, "Record deleted.", Toast.LENGTH_SHORT).show()
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