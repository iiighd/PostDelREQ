package com.example.putdelapi



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var etName : EditText
    lateinit var btSave: Button
    lateinit var btView: Button
    lateinit var rvMain: RecyclerView

    private lateinit var rvAdapter: RVAdapter
    private lateinit var info: ArrayList<String>

    var apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        btSave = findViewById(R.id.btSave)
        btView = findViewById(R.id.btView)
        rvMain = findViewById(R.id.rvMain)

        info =  arrayListOf()
        rvAdapter = RVAdapter(info)

        btSave.setOnClickListener {
            if (etName.text.isNotEmpty() ) {
                addSingleUser()
                etName.text.clear()
                etName.clearFocus()
            } else {
                Toast.makeText(applicationContext, "please add a name", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btView.setOnClickListener {
            getAllUsers()
            rvAdapter.update()
        }
    }

    private fun addSingleUser() {
        val nameIN = etName.text.toString()
        val locationIN = "KSA"
        apiInterface?.addUser(UsersData.UsersDataItem(locationIN,nameIN))?.enqueue(object : Callback<UsersData.UsersDataItem?> {
            override fun onResponse(
                call: Call<UsersData.UsersDataItem?>,
                response: Response<UsersData.UsersDataItem?>
            ) {
                Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show();
            }

            override fun onFailure(call: Call<UsersData.UsersDataItem?>, t: Throwable) {
                Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show();
            }

        })
    }

    private fun getAllUsers() {
        apiInterface?.getUser()?.enqueue(object : Callback<List<UsersData.UsersDataItem>> {
            override fun onResponse(call: Call<List<UsersData.UsersDataItem>>,
                                    response: Response<List<UsersData.UsersDataItem>>
            ) {
                for (User in response.body()!!) {
                    val fullInfo = "Name: ${User.name} \nLocation: ${User.location} "
                    info.add(fullInfo)
                }
                rvMain.adapter = rvAdapter
                rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
                rvMain.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<UsersData.UsersDataItem>>, t: Throwable) {
                Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}