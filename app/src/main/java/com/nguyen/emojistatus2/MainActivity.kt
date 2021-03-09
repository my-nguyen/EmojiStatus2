package com.nguyen.emojistatus2

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nguyen.emojistatus2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore
        val query = firestore.collection("users")
        val options = FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .setLifecycleOwner(this)
                .build()
        val adapter = UserAdapter(options)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_logout -> {
                Log.i(TAG, "Logout")
                auth.signOut()
                Intent(this, LoginActivity::class.java).apply {
                    // remove MainActivity from the back stack and return control back to LoginActivity
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(this)
                }
            }
            R.id.mi_edit -> {
                Log.i(TAG, "Show alert dialog to edit status")
                showAlertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog() {
        val editText = EditText(this)
        // a unicode is between 2 and 4 characters. length of 9 restricts to between 2 and 4 unicodes
        val lengthFilter = InputFilter.LengthFilter(9)
        val emojiFilter = EmojiFilter(this)
        editText.filters = arrayOf(lengthFilter, emojiFilter)

        val dialog = AlertDialog.Builder(this)
                .setTitle("Update your emojis")
                .setView(editText)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null)
                .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener {
                    Log.i(TAG, "Clicked on positive button!")
                    val emojis = editText.text.toString()
                    if (emojis.isBlank()) {
                        Toast.makeText(this, "Cannot submit empty text", Toast.LENGTH_SHORT).show()
                    } else {
                        val currentUser = auth.currentUser
                        if (currentUser == null) {
                            Toast.makeText(this, "No signed-in user", Toast.LENGTH_SHORT).show()
                        } else {
                            // update Firestore with the new emojis
                            firestore.collection("users")
                                    .document(currentUser.uid)
                                    .update("emojis", emojis)
                            dialog.dismiss()
                        }
                    }
                }
    }
}